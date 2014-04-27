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
 * 일반적인 객체의 타입 형변환을 다루는 클래스이다.<br>
 * 기본적으로 등록된 형변환 객체는 prmitive 객체와 String, Date 객체, 배열이다.<br>
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
 * 기본적으로 등록된 컨버터 말고 또다른 임의의 컨버터를 등록하고자 한다면..<br>
 * <pre>
 * // 생성
 * public class AnyObjectConvertor extends Convertor
 * {
 *     public AnyObjectConvertor()
 *     {
 *         register(AnyObject.class);    // 자신이 반환되는 객체가 어떤 클래스인지를 등록한다.
 *     }
 *
 *     protected Object convert(Object obj)
 *    {
 *         return ...;                   // 변환
 *    }
 * }
 *
 * // Convertor에 등록 (생성을 하면 등록된다.)
 * new AnyObjectConvertor();
 *
 * // 사용
 * // 12345 문자열을 AnyObject으로 변환
 * AnyObject value = (AnyObject) Convertor.convert(AnyObject.class, new String("12345"));
 * </pre>
 * 새로이 생성시 주의할 점은, 기본적인 객체 밑 경우의 수를 모두 따져서 만들어야 한다.
 * 왜냐하면, 어떤 객체가 파라미터로 들어올지 모르기 때문이다.
 * 보통, 임의의 객체의 {@link Object#toString toString()}를 얻은 후에
 * 변환하는 것이 낫은 방법일 것이다.
 * @author Eun Jeong-Ho, silver@intos.biz
 * @version 2005. 9. 30. 
 */
public abstract class Convertor
{
	private static Map<Class<?>, Convertor> convertors = new HashMap<Class<?>, Convertor>();

	/**
	 * 자신의 컨버터는 어떤 클래스 타입을 컨버터 할지를 등록한다.
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
	 * 컨버터를 등록한다.
	 * @param con
	 */
	public static synchronized void register(Convertor con)
	{
		Class<?> cls = con.getTargetClass();
    	convertors.put(cls, con);
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * 타켓이 배열형태이며, 소스의 값이 배열이 아니라면, 배열로 변환되어 반환되며,
	 * 타켓이 배열이 아닌 일반형태이며, 소스가 배열이라면, 배열의 첫번째 값을 변환하여 반환된다.
	 * 또한, int 등의 Primitive 타입은 Wrapper 객체인 Integer로 반환하며,
	 * 배열의 경우, int[] 식의 Primitive 배열을 원할 경우,
	 * Integer[]의 래핑된 타입의 배열을 반환하는 것이 아니라
	 * int[]으로 반환한다.
	 * 이유인즉, int 는 Object이 아니지만, int[]은 Object이므로 반환 될수 있다.
	 * 변환할 수 없다면, 객체를 그대로 반환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param obj 객체
	 * @return 변환된 객체
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
                // 만약 targetClass가 primitive일 경우, 값이 null 이라면, int 의 경우, 0 과 같은
				// 기본값을 반환 해야 하지 않을까...
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
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, int i)
	{
		return convert(targetClass, new Integer(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, short i)
	{
		return convert(targetClass, new Short(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, long i)
	{
		return convert(targetClass, new Long(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, float i)
	{
		return convert(targetClass, new Float(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, double i)
	{
		return convert(targetClass, new Double(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, byte i)
	{
		return convert(targetClass, new Byte(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, char i)
	{
		return convert(targetClass, new Character(i));
	}

	/**
	 * 원하는 타입으로 변환한다.
	 * @param targetClass 변환하고자 하는 클래스
	 * @param i 값
	 * @return 변환된 객체
	 */
	public static Object convert(Class<?> targetClass, boolean i)
	{
		return convert(targetClass, Boolean.valueOf(i));
	}

	/**
	 * 변환할 타입이 배열일 경우, 객체를 변환한다.
	 * 참고적으로, int[] 식의 Primitive 배열을 원할 경우,
	 * Integer[]의 래핑된 타입의 배열을 반환하는 것이라
	 * int[]으로 반환한다.
	 * @param obj 객체
	 * @return 변환된 객체
	 */
	private static Object convertAsArray(Class<?> targetClass, Object obj)
	{
		if (obj == null)
            return (Object[]) Array.newInstance(targetClass, 0);

		// 변환할 Class가 배열이라면,
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
	 * 해당 값을 int형으로 가져온다.
	 * @param obj Object
	 * @return int 값, 기본값은 0
	 */
	public static int getInt(Object obj)
	{
		Object value = Convertor.convert(Integer.class, obj);
		return (value == null) ? 0 : ((Integer) value).intValue();
	}

	/**
	 * 해당 값을 byte형으로 가져온다.
	 * @param obj Object
	 * @return byte 값, 기본값은 0
	 */
	public static byte getByte(Object obj)
	{
		Object value = Convertor.convert(Byte.class, obj);
		return (value == null) ? 0 : ((Byte) value).byteValue();
	}

	/**
	 * 해당 값을 char형으로 가져온다.
	 * @param obj Object
	 * @return char 값, 기본값은 0
	 */
	public static char getChar(Object obj)
	{
		Object value = Convertor.convert(Character.class, obj);
		return (value == null) ? 0 : ((Character) value).charValue();
	}

	/**
	 * 해당 값을 int형으로 가져온다.
	 * @param obj Object
	 * @return short 값, 기본값은 0
	 */
	public static short getShort(Object obj)
	{
		Object value = Convertor.convert(Short.class, obj);
		return (value == null) ? 0 : ((Short) value).shortValue();
	}

	/**
	 * 해당 값을 long형으로 가져온다.
	 * @param obj Object
	 * @return long 값, 기본값은 0
	 */
	public static long getLong(Object obj)
	{
		Object value = Convertor.convert(Long.class, obj);
		return (value == null) ? 0 : ((Long) value).longValue();
	}

	/**
	 * 해당 값을 long형으로 가져온다.
	 * @param obj Object
	 * @return long 값, 기본값은 0
	 */
	public static float getFloat(Object obj)
	{
		Object value = Convertor.convert(Float.class, obj);
		return (value == null) ? 0 : ((Float) value).floatValue();
	}

	/**
	 * 해당 값을 double형으로 가져온다.
	 * @param obj Object
	 * @return double 값, 기본값은 0
	 */
	public static double getDouble(Object obj)
	{
		Object value = Convertor.convert(Double.class, obj);
		return (value == null) ? 0 : ((Double) value).doubleValue();
	}

	/**
	 * 해당 값을 문자열로 반환한다.
	 * @param obj Object
	 * @return 문자열 key에 값이 null 이면 빈문자열을 반환한다.
	 */
	public static String getString(Object obj)
	{
		Object value = Convertor.convert(String.class, obj);
		return (value == null) ? "" : (String) value;
	}

	/**
	 * 해당 값을 boolean형으로 가져온다.
	 * @param obj Object
	 * @return boolean 값, 기본값은 false
	 */
	public static boolean getBoolean(Object obj)
	{
        Object value = Convertor.convert(Boolean.class, obj);
		return (value != null) && ((Boolean) value).booleanValue();
	}

	/**
	 * 해당 값을 {@link java.util.Date Date}형으로 가져온다.
	 * @param obj Object
	 * @return Date 값, 기본값은 null
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
	 * 객체를 원하는 타입으로 변환한다.
	 * @param obj 객체
	 * @return 변환된 객체
	 */
	protected abstract Object convert(Object obj);
}
