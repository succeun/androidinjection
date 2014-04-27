/*
 * Copyright (c) 2005, Jeong-Ho Eun
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 */
package androidinjection.util.bean;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import androidinjection.util.bean.conversion.Convertor;


/**
 * Bean ��ü �Ǵ� public Field�� ���� �Ϲ� ��ü�� �м��Ͽ�, set, get, field ����
 * ��Ҹ� �����Ͽ� �ڵ鸵 �� �� �ֵ��� �����ִ� Ŭ�����̴�.
 * @author Eun Jeong-Ho, silver@intos.biz
 * @since 2005. 6. 16.
 */
public class BeanInfo
{
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet<Class<?>>();
	private static final Map<Class<?>, BeanInfo> CLASS_INFO_MAP = Collections.synchronizedMap(new LinkedHashMap<Class<?>, BeanInfo>());

	static
	{
		SIMPLE_TYPE_SET.add(byte.class);
		SIMPLE_TYPE_SET.add(short.class);
		SIMPLE_TYPE_SET.add(char.class);
		SIMPLE_TYPE_SET.add(int.class);
		SIMPLE_TYPE_SET.add(long.class);
		SIMPLE_TYPE_SET.add(float.class);
		SIMPLE_TYPE_SET.add(double.class);
		SIMPLE_TYPE_SET.add(boolean.class);

		SIMPLE_TYPE_SET.add(String.class);
		SIMPLE_TYPE_SET.add(Byte.class);
		SIMPLE_TYPE_SET.add(Short.class);
		SIMPLE_TYPE_SET.add(Character.class);
		SIMPLE_TYPE_SET.add(Integer.class);
		SIMPLE_TYPE_SET.add(Long.class);
		SIMPLE_TYPE_SET.add(Float.class);
		SIMPLE_TYPE_SET.add(Double.class);
		SIMPLE_TYPE_SET.add(Boolean.class);
		SIMPLE_TYPE_SET.add(Date.class);
		SIMPLE_TYPE_SET.add(Class.class);
		SIMPLE_TYPE_SET.add(BigInteger.class);
		SIMPLE_TYPE_SET.add(BigDecimal.class);

		SIMPLE_TYPE_SET.add(Collection.class);
		SIMPLE_TYPE_SET.add(Set.class);
		SIMPLE_TYPE_SET.add(Map.class);
		SIMPLE_TYPE_SET.add(List.class);
		SIMPLE_TYPE_SET.add(HashMap.class);
		SIMPLE_TYPE_SET.add(TreeMap.class);
		SIMPLE_TYPE_SET.add(ArrayList.class);
		SIMPLE_TYPE_SET.add(LinkedList.class);
		SIMPLE_TYPE_SET.add(HashSet.class);
		SIMPLE_TYPE_SET.add(TreeSet.class);
		SIMPLE_TYPE_SET.add(Vector.class);
		SIMPLE_TYPE_SET.add(Hashtable.class);
		SIMPLE_TYPE_SET.add(Enumeration.class);
	}

	/**
	 * Ŭ������ �˷��� Ŭ����(JDK���� �ִ� String, List��)���� ���θ� ��ȯ�Ѵ�.
	 * @param clazz Ŭ����
	 * @return True �˷����ٸ� true
	 */
	public static boolean isKnownType(Class<?> clazz)
	{
		if (SIMPLE_TYPE_SET.contains(clazz))
		{
			return true;
		}
		else if (Collection.class.isAssignableFrom(clazz))	// Collection�� ��ӹްų�, ������ ������ �Ǵ�
		{
			return true;
		}
		else if (Map.class.isAssignableFrom(clazz))
		{
			return true;
		}
		else if (List.class.isAssignableFrom(clazz))
		{
			return true;
		}
		else if (Set.class.isAssignableFrom(clazz))
		{
			return true;
		}
		else return Iterator.class.isAssignableFrom(clazz);
	}

	/**
	 * Ư�� Ŭ������ ���Ͽ� Ŭ���� ������ ��� �ִ�
	 * ClassInfo�� ��ȯ�Ѵ�.
	 * @param clazz Ŭ����
	 * @return ĳ���Ǿ� Ŭ���� ������ ��� �ִ� ClassInfo
	 */
	public static BeanInfo getInstance(Class<?> clazz)
	{
		return getInstance(clazz, false);
	}

    /**
	 * Ư�� Ŭ������ ���Ͽ� Ŭ���� ������ ��� �ִ�
	 * ClassInfo�� ��ȯ�Ѵ�.
	 * @param clazz Ŭ����
	 * @return ĳ���Ǿ� Ŭ���� ������ ��� �ִ� ClassInfo
	 */
	public static BeanInfo getInstance(Class<?> clazz, boolean isPrivateAccess)
	{
		synchronized (clazz)
		{
			BeanInfo cache = (BeanInfo) CLASS_INFO_MAP.get(clazz);
			if (cache == null)
			{
				cache = new BeanInfo(clazz, isPrivateAccess);
				CLASS_INFO_MAP.put(clazz, cache);
			}
			return cache;
		}
	}

    /**
	 * {@link Method#invoke Method.invoke()}�� ���� �޼ҵ� ����� �Ͼ
	 * �θ��� Throwable, �� ������ �Ǵ� Throwable�� ��ȯ�Ѵ�.
	 * @param t - Throwable
	 * @return ���� ������ �Ǵ� Throwable
	 */
	public static Throwable unwrapThrowable(Throwable t)
	{
		Throwable t2 = t;
		while (true)
		{
			if (t2 instanceof InvocationTargetException)
			{
				t2 = ((InvocationTargetException) t).getTargetException();
			}
			else if (t instanceof UndeclaredThrowableException)
			{
				t2 = ((UndeclaredThrowableException) t).getUndeclaredThrowable();
			}
			else
			{
				return t2;
			}
		}
	}

    /**
	 * Bean Property�� ���� �����Ѵ�.
	 * � ��ü�� �־��� Property���� set�޼ҵ带 ȣ���Ͽ� ���� �����Ѵ�.
	 * ���� ���, property���� name �̶�� setName()�� ȣ���Ͽ� ���� �����Ѵ�.
	 * @param obj Bean ��ü set �޼ҵ带 ���� ��ü
	 * @param name Property ��
	 * @param value Property �� ���ڿ��� set �޼ҵ尡 ���ϴ� Ÿ������ ��ȯ�Ͽ� �����Ѵ�.
	 */
	public static void setProperty(Object obj, String name, String value)
	{
		BeanInfo info = BeanInfo.getInstance(obj.getClass());
		info.invokeSetter(obj, name, value);
	}

	/**
	 * ��ü ���� ��� �ʵ忡 ���Ͽ� ���ڿ��� ����Ѵ�.
	 * @return String
	 */
	public static String toString(Object obj)
	{
		StringBuffer buf = new StringBuffer();

		BeanInfo info = BeanInfo.getInstance(obj.getClass());
		String fullname = info.getClassName();

        buf.append(fullname).append(":{");
		Field[] fields = info.getFields();
		for (int i = 0; i < fields.length; i++)
		{
			try
			{
				if (i != 0) buf.append(',');
                buf.append(fields[i].getName()).append('=');
				Object f = fields[i].get(obj);
				Class<?> fc = fields[i].getType();
				if (fc.isArray())
				{
					buf.append('[');
					int length = Array.getLength(f);
					for (int j = 0; j < length; j++)
					{
						if (j != 0) buf.append(',');
						Object element = Array.get(f, j);
						buf.append(element.toString());
					}
					buf.append(']');
				}
				else
					buf.append(f.toString());
			}
			catch (Exception e)
			{
			}
		}

		if (fields.length > 0)
			buf.append(',');

		String[] methodNames = info.getGetterNames();
		for (int i = 0; i < methodNames.length; i++)
		{
			try
			{
				if (i != 0) buf.append(',');

                buf.append(methodNames[i]).append('=');
				Object f = info.invokeGetter(obj, methodNames[i]);
				Class<?> fc = info.getGetter(methodNames[i]).getReturnType();
				if (fc.isArray())
				{
					buf.append('[');
					int length = Array.getLength(f);
					for (int j = 0; j < length; j++)
					{
						if (j != 0) buf.append(',');
						Object element = Array.get(f, j);
						buf.append(element.toString());
					}
					buf.append(']');
				}
				else
					buf.append(f.toString());
			}
			catch (Exception e)
			{
			}
		}
		buf.append('}');
		return buf.toString();
	}

	/**
	 * <code>toString</code>�� �޸� ������ ���ڿ��� ��ȯ�Ѵ�.
	 * @param obj Object
	 * @return String
	 * @throws java.lang.IllegalAccessException
	 */
	public static String toSimpleString(Object obj) throws IllegalAccessException, InvocationTargetException
	{

		StringBuffer strBuf = new StringBuffer();
        strBuf.append('{');
		BeanInfo info = BeanInfo.getInstance(obj.getClass());

		Field[] fields = info.getFields();

		if (fields.length > 0)
        {
            for (int i = 0; i < fields.length - 1; i++)
                strBuf.append(fields[i].get(obj)).append(',');
            strBuf.append(fields[fields.length - 1].get(obj));
        }

        Method[] methods = info.getGetters();

		if (methods.length > 0)
        {
            if (fields.length > 0)
                strBuf.append(',');

            for (int i = 0; i < methods.length - 1; i++)
                strBuf.append(methods[i].invoke(obj, new Object[0])).append(',');
            strBuf.append(methods[methods.length - 1].invoke(obj, new Object[0]));
        }
        strBuf.append('}');
        return strBuf.toString();

	}


    private String className;
	private Class<?> targetClass;
	private String[] getterNames = EMPTY_STRING_ARRAY;
	private String[] setterNames = EMPTY_STRING_ARRAY;
	private Map<String, Method> setMethods = new LinkedHashMap<String, Method>();
	private Map<String, Method> getMethods = new LinkedHashMap<String, Method>();
	private Map<String, Class<?>> setTypes = new LinkedHashMap<String, Class<?>>();
	private Map<String, Class<?>> getTypes = new LinkedHashMap<String, Class<?>>();
	private Map<String, Field> fields = new LinkedHashMap<String, Field>();
	private String[] fieldNames = EMPTY_STRING_ARRAY;
	private Map<String, Class<?>> fieldTypes = new LinkedHashMap<String, Class<?>>();
	private boolean isPrivateAccess = false;    // ���� ��� �ʵ�, �޼ҵ带 �ٷ��� ����


	/**
	 * Ŭ���� ���ο� ������ �ִ� ������ ��ȯ�Ѵ�.
	 * @param clazz
	 * @param isPrivateAccess
	 */
	private BeanInfo(Class<?> clazz, boolean isPrivateAccess)
	{
		this.isPrivateAccess = isPrivateAccess;
        className = clazz.getName();
		targetClass = clazz;
		addMethods(clazz);
		addFields(clazz);
		Class<?> superClass = clazz.getSuperclass();
		while (superClass != null)
		{
			addMethods(superClass);
			addFields(superClass);
			superClass = superClass.getSuperclass();
		}
		getterNames = (String[]) getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
		setterNames = (String[]) setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
		fieldNames = (String[]) fields.keySet().toArray(new String[fields.keySet().size()]);
	}

	/**
	 * �м��� ����� �Ǵ� Ŭ������ ��ȯ�Ѵ�.
	 * @return �м� Ŭ����
	 */
	public Class<?> getTargetClass()
	{
		return this.targetClass;
	}

	/**
	 * public {@link Field Field}�� �˻��Ͽ� �߰��Ѵ�.
	 * @param cls Class
	 */
	private void addFields(Class<?> cls)
	{
		Field[] fields = (isPrivateAccess) ? cls.getDeclaredFields() : cls.getFields();
		for (int i = 0; i < fields.length; i++)
		{
            if (isPrivateAccess)
                fields[i].setAccessible(true);
            String fieldName = fields[i].getName();
			Class<?> fieldType = fields[i].getType();
			this.fields.put(fieldName, fields[i]);
			fieldTypes.put(fieldName, fieldType);
		}
	}

	/**
	 * get~, set~, is~�� ������ �޼ҵ带 ã�� Ÿ�Ժ��� �и��Ѵ�.
	 * @param cls Class
	 */
	private void addMethods(Class<?> cls)
	{
        Method[] methods = (isPrivateAccess) ? cls.getDeclaredMethods() : cls.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			if (isPrivateAccess)
                methods[i].setAccessible(true);                
            String name = methods[i].getName();
			if (name.equals("getClass"))	// getClass() �޼ҵ�� �����Ŵ
				continue;

			if (name.startsWith("set") && name.length() > 3)
			{
				if (methods[i].getParameterTypes().length == 1 && methods[i].getReturnType() == void.class)
				{
					name = dropCase(name);
					setMethods.put(name, methods[i]);
					setTypes.put(name, methods[i].getParameterTypes()[0]);
				}
			}
			else if (name.startsWith("get") && name.length() > 3)
			{
				if (methods[i].getParameterTypes().length == 0)
				{
					name = dropCase(name);
					getMethods.put(name, methods[i]);
					getTypes.put(name, methods[i].getReturnType());
				}
			}
			else if (name.startsWith("is") && name.length() > 2)
			{
				if (methods[i].getParameterTypes().length == 0)
				{
					name = dropCase(name);
					getMethods.put(name, methods[i]);
					getTypes.put(name, methods[i].getReturnType());
				}
			}
		}
	}

	/**
	 * �޼ҵ���� is, set, get�� �� ��Ӽ����� ��ȯ�Ѵ�.
	 * @param name �̸�
	 * @return �� �Ӽ���
	 */
	private static String dropCase(String name)
	{
		if (name.startsWith("is"))
			name = name.substring(2);
		else if (name.startsWith("get") || name.startsWith("set"))
			name = name.substring(3);

		if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1))))
			name = name.substring(0, 1).toLowerCase(Locale.US) + name.substring(1);

		return name;
	}

	/**
	 * Ŭ���� �̸��� ��ȯ�Ѵ�.
	 * @return Ŭ������
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Field �Ӽ��� �迭�� ��ȯ�Ѵ�.
	 * @return �Ӽ� �迭
	 */
	public Field[] getFields()
	{
		return fields.values().toArray(new Field[0]);
	}

	/**
	 * Setter �޼ҵ��� �迭�� ��ȯ�Ѵ�.
	 * @return Setter �޼ҵ� �迭
	 */
	public Method[] getSetters()
	{
		return setMethods.values().toArray(new Method[0]);
	}

	/**
	 * Getter �޼ҵ��� �迭�� ��ȯ�Ѵ�.
	 * @return Getter �޼ҵ� �迭
	 */
	public Method[] getGetters()
	{
		return getMethods.values().toArray(new Method[0]);
	}

	/**
	 * �ش� �Ӽ����� ���� Setter �޼ҵ带 ��ȯ�Ѵ�.
	 * @param propertyName Property ��
	 * @return Method Setter
	 */
	public Method getSetter(String propertyName)
	{
		Method method = setMethods.get(propertyName);
		if (method == null)
			throw new BeanNotAccessException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
		return method;
	}

	/**
	 * �ش� �Ӽ����� ���� Getter �޼ҵ带 ��ȯ�Ѵ�.
	 * @param propertyName - Property ��
	 * @return Method Getter
	 */
	public Method getGetter(String propertyName)
	{
		Method method = getMethods.get(propertyName);
		if (method == null)
			throw new BeanNotAccessException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
		return method;
	}

	/**
	 * �ش� �Ӽ����� ���� �ʵ带 ��ȯ�Ѵ�.
	 * @param propertyName - Property ��
	 * @return Field
	 */
	public Field getField(String propertyName)
	{
		Field field = fields.get(propertyName);
		if (field == null)
			throw new BeanNotAccessException("There is no FIELD named '" + propertyName + "' in class '" + className + "'");
		return field;
	}

	/**
	 * Setter �Ӽ��� ���� Ÿ���� ��ȯ�Ѵ�.
	 * @param propertyName Property ��
	 * @return Setter �Ӽ��� ���� Ÿ��Ŭ����
	 */
	public Class<?> getSetterType(String propertyName)
	{
		Class<?> clazz = setTypes.get(propertyName);
		if (clazz == null)
			throw new BeanNotAccessException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
		return clazz;
	}

	/**
	 * Getter �Ӽ��� ��ȯ Ÿ���� ��ȯ�Ѵ�.
	 * @param propertyName Property ��
	 * @return Getter �Ӽ��� ��ȯ Ÿ��Ŭ����
	 */
	public Class<?> getGetterType(String propertyName)
	{
		Class<?> clazz = getTypes.get(propertyName);
		if (clazz == null)
			throw new BeanNotAccessException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
		return clazz;
	}

	/**
	 * Field �Ӽ��� Ÿ���� ��ȯ�Ѵ�.
	 * @param propertyName Property ��
	 * @return Field�� ��ȯ Ÿ��Ŭ����
	 */
	public Class<?> getFieldType(String propertyName)
	{
		Class<?> clazz = fieldTypes.get(propertyName);
		if (clazz == null)
			throw new BeanNotAccessException("There is no FIELD property named '" + propertyName + "' in class '" + className + "'");
		return clazz;
	}

    /**
	 * �ش� �Ӽ����� ���� Setter �޼ҵ带 ��ȯ�Ѵ�.
	 * @param bean ����� �Ǵ� Bean ��ü
	 * @param propertyName Property ��
	 * @param param Method Setter�� �Ķ���Ͱ�
	 */
	public void invokeSetter(Object bean, String propertyName, Object param)
    {
        Method method = setMethods.get(propertyName);
		if (method == null)
			throw new BeanNotAccessException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");

		try
		{
            if (param != null)
			{
				if (targetClass.isAssignableFrom(param.getClass()))
					method.invoke(bean, new Object[]{param});
				else
				{
					Class<?> clsParam = setTypes.get(propertyName);
					Object tmp = Convertor.convert(clsParam, param);   // Convertor�� ��ȯ �� �Ҽ��� ����
					method.invoke(bean, new Object[]{tmp});
				}
			}
        }
		catch (IllegalAccessException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
		catch (InvocationTargetException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
	}

	/**
	 * �ش� �Ӽ����� ���� Getter �޼ҵ带 ��ȯ�Ѵ�.
	 * @param bean ����� �Ǵ� Bean ��ü
	 * @param propertyName - Property ��
	 * @return Method Getter�� ��ȯ��
	 */
	public Object invokeGetter(Object bean, String propertyName)
	{
		Method method = getMethods.get(propertyName);
		if (method == null)
			throw new BeanNotAccessException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");

		try
		{
			return method.invoke(bean, new Object[]{});
		}
		catch (IllegalAccessException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
		catch (InvocationTargetException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}

	}

    /**
	 * �ش� �Ӽ����� ���� Field�� ���� �����Ѵ�.
	 * @param bean ����� �Ǵ� Bean ��ü
	 * @param propertyName Property ��
	 * @param param Method Setter�� �Ķ���Ͱ�
	 */
	public void setField(Object bean, String propertyName, Object param)
    {
		Field field = fields.get(propertyName);
		if (field == null)
			throw new BeanNotAccessException("There is no FIELD property named '" + propertyName + "' in class '" + className + "'");

		try
		{
			if (param != null)
			{
				if (targetClass.isAssignableFrom(param.getClass()))
					field.set(bean, param);
				else
				{
					Class<?> clsParam = fieldTypes.get(propertyName);
					Object tmp = Convertor.convert(clsParam, param);
					field.set(bean, tmp);
				}
			}
        }
		catch (IllegalAccessException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
	}

	/**
	 * �ش� �Ӽ����� ���� Field�� ���� ��ȯ�Ѵ�.
	 * @param bean ����� �Ǵ� Bean ��ü
	 * @param propertyName - Property ��
	 * @return Field�� ��ȯ��
	 */
	public Object getField(Object bean, String propertyName)
	{
		Field field = fields.get(propertyName);
		if (field == null)
			throw new BeanNotAccessException("There is no FIELD property named '" + propertyName + "' in class '" + className + "'");

		try
		{
			return field.get(bean);
		}
		catch (IllegalAccessException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new BeanNotAccessException("failed Method invoke. ", e);
		}
	}

	/**
	 * Readable �Ӽ������ �迭�� ��ȯ�Ѵ�.
	 * @return �Ӽ��� �迭
	 */
	public String[] getGetterNames()
	{
		return getterNames;
	}

	/**
	 * Writeable �Ӽ������ �迭�� ��ȯ�Ѵ�.
	 * @return �Ӽ��� �迭
	 */
	public String[] getSetterNames()
	{
		return setterNames;
	}

	/**
	 * Field �Ӽ������ �迭�� ��ȯ�Ѵ�.
	 * @return �Ӽ��� �迭
	 */
	public String[] getFieldNames()
	{
		return fieldNames;
	}

	/**
	 * Writeable �Ӽ��߿� �ش� �Ӽ��� �ִ��� Ȯ���Ѵ�.
	 * @param propertyName üũ�� �Ӽ���
	 * @return True Writeable �Ӽ��߿� �ش� �Ӽ��� �����ϴ��� ����
	 */
	public boolean hasSetter(String propertyName)
	{
		return setMethods.keySet().contains(propertyName);
	}

	/**
	 * Readable �Ӽ��߿� �ش� �Ӽ��� �ִ��� Ȯ���Ѵ�.
	 * @param propertyName üũ�� �Ӽ���
	 * @return True Readable �Ӽ��߿� �ش� �Ӽ��� �����ϴ��� ����
	 */
	public boolean hasGetter(String propertyName)
	{
		return getMethods.keySet().contains(propertyName);
	}

	/**
	 * Field �߿� �ش� �Ӽ��� �ִ��� Ȯ���Ѵ�.
	 * @param propertyName üũ�� �Ӽ���
	 * @return True Field �߿� �ش� �Ӽ��� �����ϴ��� ����
	 */
	public boolean hasField(String propertyName)
	{
		return fields.keySet().contains(propertyName);
	}

    /**
     * Getter, Setter�� Bean ����
     */
    public final static int METHOD_TYPE = 0;

    /**
     * Public Field�� Bean ����
     */
    public final static int FIELD_TYPE = 1;

    /**
     * Bean�� Ÿ���� ��ȯ�Ѵ�.
     * Getter�� ���翩�θ� Ÿ���� �����Ѵ�.
     * @return {@link METHOD_TYPE} �Ǵ� {@link FIELD_TYPE}
     */
    public int getType()
    {
        String[] names = getGetterNames();
        return (names.length > 0) ? METHOD_TYPE : FIELD_TYPE;
    }
}
