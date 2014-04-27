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
 * Bean 객체 또는 public Field를 가진 일반 객체를 분석하여, set, get, field 등의
 * 요소를 추출하여 핸들링 할 수 있도록 도와주는 클래스이다.
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
	 * 클래스가 알려진 클래스(JDK내에 있는 String, List등)인지 여부를 반환한다.
	 * @param clazz 클래스
	 * @return True 알려졌다면 true
	 */
	public static boolean isKnownType(Class<?> clazz)
	{
		if (SIMPLE_TYPE_SET.contains(clazz))
		{
			return true;
		}
		else if (Collection.class.isAssignableFrom(clazz))	// Collection을 상속받거나, 구현한 넘인지 판단
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
	 * 특정 클래스에 대하여 클래스 정보를 담고 있는
	 * ClassInfo를 반환한다.
	 * @param clazz 클래스
	 * @return 캐쉬되어 클래스 정보를 담고 있는 ClassInfo
	 */
	public static BeanInfo getInstance(Class<?> clazz)
	{
		return getInstance(clazz, false);
	}

    /**
	 * 특정 클래스에 대하여 클래스 정보를 담고 있는
	 * ClassInfo를 반환한다.
	 * @param clazz 클래스
	 * @return 캐쉬되어 클래스 정보를 담고 있는 ClassInfo
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
	 * {@link Method#invoke Method.invoke()}와 같이 메소드 실행시 일어난
	 * 부모의 Throwable, 즉 원인이 되는 Throwable를 반환한다.
	 * @param t - Throwable
	 * @return 실제 원인이 되는 Throwable
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
	 * Bean Property에 값을 설정한다.
	 * 어떤 객체에 주어진 Property명의 set메소드를 호출하여 값을 설정한다.
	 * 예를 들어, property명이 name 이라면 setName()을 호출하여 값을 설정한다.
	 * @param obj Bean 객체 set 메소드를 가진 객체
	 * @param name Property 명
	 * @param value Property 값 문자열을 set 메소드가 원하는 타입으로 변환하여 설정한다.
	 */
	public static void setProperty(Object obj, String name, String value)
	{
		BeanInfo info = BeanInfo.getInstance(obj.getClass());
		info.invokeSetter(obj, name, value);
	}

	/**
	 * 객체 내의 모든 필드에 대하여 문자열로 출력한다.
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
	 * <code>toString</code>와 달리 간단한 문자열을 반환한다.
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
	private boolean isPrivateAccess = false;    // 내부 모든 필드, 메소드를 다룰지 여부


	/**
	 * 클래스 내부에 가지고 있는 정보를 반환한다.
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
	 * 분석의 대상이 되는 클래스를 반환한다.
	 * @return 분석 클래스
	 */
	public Class<?> getTargetClass()
	{
		return this.targetClass;
	}

	/**
	 * public {@link Field Field}를 검색하여 추가한다.
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
	 * get~, set~, is~의 빈형태 메소드를 찾아 타입별로 분리한다.
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
			if (name.equals("getClass"))	// getClass() 메소드는 통과시킴
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
	 * 메소드명에서 is, set, get을 뺀 빈속성명을 반환한다.
	 * @param name 이름
	 * @return 빈 속성명
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
	 * 클래스 이름을 반환한다.
	 * @return 클래스명
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Field 속성의 배열을 반환한다.
	 * @return 속성 배열
	 */
	public Field[] getFields()
	{
		return fields.values().toArray(new Field[0]);
	}

	/**
	 * Setter 메소드의 배열을 반환한다.
	 * @return Setter 메소드 배열
	 */
	public Method[] getSetters()
	{
		return setMethods.values().toArray(new Method[0]);
	}

	/**
	 * Getter 메소드의 배열을 반환한다.
	 * @return Getter 메소드 배열
	 */
	public Method[] getGetters()
	{
		return getMethods.values().toArray(new Method[0]);
	}

	/**
	 * 해당 속성명을 가진 Setter 메소드를 반환한다.
	 * @param propertyName Property 명
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
	 * 해당 속성명을 가진 Getter 메소드를 반환한다.
	 * @param propertyName - Property 명
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
	 * 해당 속성명을 가진 필드를 반환한다.
	 * @param propertyName - Property 명
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
	 * Setter 속성의 설정 타입을 반환한다.
	 * @param propertyName Property 명
	 * @return Setter 속성의 설정 타입클래스
	 */
	public Class<?> getSetterType(String propertyName)
	{
		Class<?> clazz = setTypes.get(propertyName);
		if (clazz == null)
			throw new BeanNotAccessException("There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
		return clazz;
	}

	/**
	 * Getter 속성의 반환 타입을 반환한다.
	 * @param propertyName Property 명
	 * @return Getter 속성의 반환 타입클래스
	 */
	public Class<?> getGetterType(String propertyName)
	{
		Class<?> clazz = getTypes.get(propertyName);
		if (clazz == null)
			throw new BeanNotAccessException("There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
		return clazz;
	}

	/**
	 * Field 속성의 타입을 반환한다.
	 * @param propertyName Property 명
	 * @return Field의 반환 타입클래스
	 */
	public Class<?> getFieldType(String propertyName)
	{
		Class<?> clazz = fieldTypes.get(propertyName);
		if (clazz == null)
			throw new BeanNotAccessException("There is no FIELD property named '" + propertyName + "' in class '" + className + "'");
		return clazz;
	}

    /**
	 * 해당 속성명을 가진 Setter 메소드를 반환한다.
	 * @param bean 대상이 되는 Bean 객체
	 * @param propertyName Property 명
	 * @param param Method Setter의 파라미터값
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
					Object tmp = Convertor.convert(clsParam, param);   // Convertor가 변환 못 할수도 있음
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
	 * 해당 속성명을 가진 Getter 메소드를 반환한다.
	 * @param bean 대상이 되는 Bean 객체
	 * @param propertyName - Property 명
	 * @return Method Getter의 반환값
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
	 * 해당 속성명을 가진 Field에 값을 세팅한다.
	 * @param bean 대상이 되는 Bean 객체
	 * @param propertyName Property 명
	 * @param param Method Setter의 파라미터값
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
	 * 해당 속성명을 가진 Field의 값을 반환한다.
	 * @param bean 대상이 되는 Bean 객체
	 * @param propertyName - Property 명
	 * @return Field의 반환값
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
	 * Readable 속성명들의 배열을 반환한다.
	 * @return 속성명 배열
	 */
	public String[] getGetterNames()
	{
		return getterNames;
	}

	/**
	 * Writeable 속성명들의 배열을 반환한다.
	 * @return 속성명 배열
	 */
	public String[] getSetterNames()
	{
		return setterNames;
	}

	/**
	 * Field 속성명들의 배열을 반환한다.
	 * @return 속성명 배열
	 */
	public String[] getFieldNames()
	{
		return fieldNames;
	}

	/**
	 * Writeable 속성중에 해당 속성이 있는지 확인한다.
	 * @param propertyName 체크할 속성명
	 * @return True Writeable 속성중에 해당 속성이 존재하는지 여부
	 */
	public boolean hasSetter(String propertyName)
	{
		return setMethods.keySet().contains(propertyName);
	}

	/**
	 * Readable 속성중에 해당 속성이 있는지 확인한다.
	 * @param propertyName 체크할 속성명
	 * @return True Readable 속성중에 해당 속성이 존재하는지 여부
	 */
	public boolean hasGetter(String propertyName)
	{
		return getMethods.keySet().contains(propertyName);
	}

	/**
	 * Field 중에 해당 속성이 있는지 확인한다.
	 * @param propertyName 체크할 속성명
	 * @return True Field 중에 해당 속성이 존재하는지 여부
	 */
	public boolean hasField(String propertyName)
	{
		return fields.keySet().contains(propertyName);
	}

    /**
     * Getter, Setter로 Bean 구성
     */
    public final static int METHOD_TYPE = 0;

    /**
     * Public Field로 Bean 구성
     */
    public final static int FIELD_TYPE = 1;

    /**
     * Bean의 타입을 반환한다.
     * Getter의 존재여부르 타입을 구분한다.
     * @return {@link METHOD_TYPE} 또는 {@link FIELD_TYPE}
     */
    public int getType()
    {
        String[] names = getGetterNames();
        return (names.length > 0) ? METHOD_TYPE : FIELD_TYPE;
    }
}
