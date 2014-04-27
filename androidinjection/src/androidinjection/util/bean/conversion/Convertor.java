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
package androidinjection.util.bean.conversion;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * �Ϲ����� ��ü�� Ÿ�� ����ȯ�� �ٷ�� Ŭ�����̴�.<br>
 * �⺻������ ��ϵ� ����ȯ ��ü�� prmitive ��ü�� String, Date ��ü, �迭�̴�.<br>
 * <pre>
 * // Array
 * String[] values = (String[]) Convertor.convert(String[].class, new Object[]{new Long(0), new Long(1), new Integer(1)});
 * // Normal
 * String value = (String) Convertor.convert(String.class, new BigDecimal(100));
 * // Primitive
 * int value = Convertor.getInt(new BigDecimal(100));
 * long value = Convertor.getLong(new BigDecimal(100));
 * boolean value = Convertor.getBoolean(new BigDecimal(100));
 * </pre>
 * �⺻������ ��ϵ� ������ ���� �Ǵٸ� ������ �����͸� ����ϰ��� �Ѵٸ�..<br>
 * <pre>
 * // ����
 * public class AnyObjectConvertor extends Convertor
 * {
 *     public AnyObjectConvertor()
 *     {
 *         register(AnyObject.class);    // �ڽ��� ��ȯ�Ǵ� ��ü�� � Ŭ���������� ����Ѵ�.
 *     }
 *
 *     protected Object convert(Object obj)
 *    {
 *         return ...;                   // ��ȯ
 *    }
 * }
 *
 * // Convertor�� ��� (������ �ϸ� ��ϵȴ�.)
 * new AnyObjectConvertor();
 *
 * // ���
 * // 12345 ���ڿ��� AnyObject���� ��ȯ
 * AnyObject value = (AnyObject) Convertor.convert(AnyObject.class, new String("12345"));
 * </pre>
 * ������ ������ ������ ����, �⺻���� ��ü �� ����� ���� ��� ������ ������ �Ѵ�.
 * �ֳ��ϸ�, � ��ü�� �Ķ���ͷ� ������ �𸣱� �����̴�.
 * ����, ������ ��ü�� {@link Object#toString toString()}�� ���� �Ŀ�
 * ��ȯ�ϴ� ���� ���� ����� ���̴�.
 * @author Eun Jeong-Ho, silver@intos.biz
 * @version 2005. 9. 30. 
 */
public abstract class Convertor
{
	private static Map<Class<?>, Convertor> convertors = new HashMap<Class<?>, Convertor>();

	/**
	 * �ڽ��� �����ʹ� � Ŭ���� Ÿ���� ������ ������ ����Ѵ�.
	 * @param targetClass
	 */
	protected void register(Class<?> targetClass)
	{
        if (targetClass == null)
            throw new NullPointerException("targetClass is null.");
		this.targetClass = targetClass;

        register(this);
    }

	/**
	 * �����͸� ����Ѵ�.
	 * @param con
	 */
	public static synchronized void register(Convertor con)
	{
		Class<?> cls = con.getTargetClass();
    	convertors.put(cls, con);
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * Ÿ���� �迭�����̸�, �ҽ��� ���� �迭�� �ƴ϶��, �迭�� ��ȯ�Ǿ� ��ȯ�Ǹ�,
	 * Ÿ���� �迭�� �ƴ� �Ϲ������̸�, �ҽ��� �迭�̶��, �迭�� ù��° ���� ��ȯ�Ͽ� ��ȯ�ȴ�.
	 * ����, int ���� Primitive Ÿ���� Wrapper ��ü�� Integer�� ��ȯ�ϸ�,
	 * �迭�� ���, int[] ���� Primitive �迭�� ���� ���,
	 * Integer[]�� ���ε� Ÿ���� �迭�� ��ȯ�ϴ� ���� �ƴ϶�
	 * int[]���� ��ȯ�Ѵ�.
	 * ��������, int �� Object�� �ƴ�����, int[]�� Object�̹Ƿ� ��ȯ �ɼ� �ִ�.
	 * ��ȯ�� �� ���ٸ�, ��ü�� �״�� ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param obj ��ü
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, Object obj)
	{
		if (targetClass.isArray())
			return convertAsArray(targetClass, obj);
		else
		{
			if (obj != null && obj.getClass().isArray())
				obj = Array.get(obj, 0);

			Class<?> ctargetClass = ConvertorUtil.toPrimitiveType(targetClass);

			if (obj == null)
			{
                // ���� targetClass�� primitive�� ���, ���� null �̶��, int �� ���, 0 �� ����
				// �⺻���� ��ȯ �ؾ� ���� ������...
				if (targetClass.isPrimitive())
					return ConvertorUtil.getDefaultValue(ctargetClass);

            	return obj;
			}
			else
			{
                if (ctargetClass.isAssignableFrom(obj.getClass()))
                {
                    return obj;
                }
                else
                {
                    Convertor con = (Convertor) convertors.get(ctargetClass);
                    if (con == null)
                        return obj;
                    return con.convert(obj);
                }
            }
		}
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, int i)
	{
		return convert(targetClass, new Integer(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, short i)
	{
		return convert(targetClass, new Short(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, long i)
	{
		return convert(targetClass, new Long(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, float i)
	{
		return convert(targetClass, new Float(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, double i)
	{
		return convert(targetClass, new Double(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, byte i)
	{
		return convert(targetClass, new Byte(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, char i)
	{
		return convert(targetClass, new Character(i));
	}

	/**
	 * ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param targetClass ��ȯ�ϰ��� �ϴ� Ŭ����
	 * @param i ��
	 * @return ��ȯ�� ��ü
	 */
	public static Object convert(Class<?> targetClass, boolean i)
	{
		return convert(targetClass, Boolean.valueOf(i));
	}

	/**
	 * ��ȯ�� Ÿ���� �迭�� ���, ��ü�� ��ȯ�Ѵ�.
	 * ����������, int[] ���� Primitive �迭�� ���� ���,
	 * Integer[]�� ���ε� Ÿ���� �迭�� ��ȯ�ϴ� ���̶�
	 * int[]���� ��ȯ�Ѵ�.
	 * @param obj ��ü
	 * @return ��ȯ�� ��ü
	 */
	private static Object convertAsArray(Class<?> targetClass, Object obj)
	{
		if (obj == null)
            return (Object[]) Array.newInstance(targetClass, 0);

		// ��ȯ�� Class�� �迭�̶��,
		if (targetClass.isArray())
			targetClass = targetClass.getComponentType();

		Class<?> cls = obj.getClass();

		if (cls.isArray())
		{
			int size = Array.getLength(obj);
            Object newObj = Array.newInstance(targetClass, size);
			for (int i = 0; i < size; i++)
				Array.set(newObj, i, Convertor.convert(targetClass, Array.get(obj, i)));
			return newObj;
		}
		else if (obj instanceof Collection)
		{
			Collection<Object> list = (Collection<Object>) obj;
			Object newObj = Array.newInstance(targetClass, list.size());
			Iterator<Object> itr = list.iterator();
			int i = 0;
			while (itr.hasNext())
			{
				Array.set(newObj, i++, Convertor.convert(targetClass, itr.next()));
			}
			return newObj;
		}
		else
        {
            Object newObj = Array.newInstance(targetClass, 1);
            Array.set(newObj, 0, Convertor.convert(targetClass, obj));
            return newObj;
        }
	}


	/**
	 * �ش� ���� int������ �����´�.
	 * @param obj Object
	 * @return int ��, �⺻���� 0
	 */
	public static int getInt(Object obj)
	{
		Object value = Convertor.convert(Integer.class, obj);
		return (value == null) ? 0 : ((Integer) value).intValue();
	}

	/**
	 * �ش� ���� byte������ �����´�.
	 * @param obj Object
	 * @return byte ��, �⺻���� 0
	 */
	public static byte getByte(Object obj)
	{
		Object value = Convertor.convert(Byte.class, obj);
		return (value == null) ? 0 : ((Byte) value).byteValue();
	}

	/**
	 * �ش� ���� char������ �����´�.
	 * @param obj Object
	 * @return char ��, �⺻���� 0
	 */
	public static char getChar(Object obj)
	{
		Object value = Convertor.convert(Character.class, obj);
		return (value == null) ? 0 : ((Character) value).charValue();
	}

	/**
	 * �ش� ���� int������ �����´�.
	 * @param obj Object
	 * @return short ��, �⺻���� 0
	 */
	public static short getShort(Object obj)
	{
		Object value = Convertor.convert(Short.class, obj);
		return (value == null) ? 0 : ((Short) value).shortValue();
	}

	/**
	 * �ش� ���� long������ �����´�.
	 * @param obj Object
	 * @return long ��, �⺻���� 0
	 */
	public static long getLong(Object obj)
	{
		Object value = Convertor.convert(Long.class, obj);
		return (value == null) ? 0 : ((Long) value).longValue();
	}

	/**
	 * �ش� ���� long������ �����´�.
	 * @param obj Object
	 * @return long ��, �⺻���� 0
	 */
	public static float getFloat(Object obj)
	{
		Object value = Convertor.convert(Float.class, obj);
		return (value == null) ? 0 : ((Float) value).floatValue();
	}

	/**
	 * �ش� ���� double������ �����´�.
	 * @param obj Object
	 * @return double ��, �⺻���� 0
	 */
	public static double getDouble(Object obj)
	{
		Object value = Convertor.convert(Double.class, obj);
		return (value == null) ? 0 : ((Double) value).doubleValue();
	}

	/**
	 * �ش� ���� ���ڿ��� ��ȯ�Ѵ�.
	 * @param obj Object
	 * @return ���ڿ� key�� ���� null �̸� ���ڿ��� ��ȯ�Ѵ�.
	 */
	public static String getString(Object obj)
	{
		Object value = Convertor.convert(String.class, obj);
		return (value == null) ? "" : (String) value;
	}

	/**
	 * �ش� ���� boolean������ �����´�.
	 * @param obj Object
	 * @return boolean ��, �⺻���� false
	 */
	public static boolean getBoolean(Object obj)
	{
        Object value = Convertor.convert(Boolean.class, obj);
		return (value != null) && ((Boolean) value).booleanValue();
	}

	/**
	 * �ش� ���� {@link java.util.Date Date}������ �����´�.
	 * @param obj Object
	 * @return Date ��, �⺻���� null
	 */
	public static Date getDate(Object obj)
	{
		Object value = Convertor.convert(Date.class, obj);
		return (value == null) ? null : (Date) value;
	}

	static
	{
		new BooleanConvertor();
		new ByteConvertor();
		new CharacterConvertor();
		new DateConvertor();
		new DoubleConvertor();
		new FloatConvertor();
		new IntegerConvertor();
		new LongConvertor();
		new ShortConvertor();
		new StringConvertor();
		new SQLDateConvertor();
		new SQLTimeConvertor();
		new SQLTimestampConvertor();
	}




	private Class<?> targetClass;

	public Class<?> getTargetClass()
	{
    	return targetClass;
	}

	/**
	 * ��ü�� ���ϴ� Ÿ������ ��ȯ�Ѵ�.
	 * @param obj ��ü
	 * @return ��ȯ�� ��ü
	 */
	protected abstract Object convert(Object obj);
}
