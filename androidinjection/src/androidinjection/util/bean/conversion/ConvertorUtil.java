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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Eun Jeong-Ho, silver@intos.biz
 * @version 2005. 9. 30. 
 */
public class ConvertorUtil
{
    /**
     * Primitive Type의 경우 클래스를 Wrapper Type에 해당하는 클래스로 반환한다.
     * boolean, char, byte, short, int, long, float, double의 Wrapper Class인
     * Boolean, Character, Byte, Short, Integer, Long, Float, Double을 반환한다.
     * 배열 클래스 일경우, Wrapper Class의 배열로 반환한다.
     * 예를 들어, int[] 을 경우, integer[]로 반환한다.
     * @param cls 클래스
     * @return Wrapper Class, 일반 클래스라면, 입력된 클래스를 반환한다.
     */
    public static Class<?> toPrimitiveType(Class<?> cls)
    {
        Class<?> pType = cls;
        if (!cls.isArray())
        {
            if (Boolean.TYPE == cls)
                pType = Boolean.class;
            else if (Character.TYPE == cls)
                pType = Character.class;
            else if (Byte.TYPE == cls)
                pType = Byte.class;
            else if (Short.TYPE == cls)
                pType = Short.class;
            else if (Integer.TYPE == cls)
                pType = Integer.class;
            else if (Long.TYPE == cls)
                pType = Long.class;
            else if (Float.TYPE == cls)
                pType = Float.class;
            else if (Double.TYPE == cls)
                pType = Double.class;
        }
        else
        {
            Class<?> itemType = cls.getComponentType();
            if (Boolean.TYPE == itemType)
                pType = Boolean[].class;
            else if (Character.TYPE == itemType)
                pType = Character[].class;
            else if (Byte.TYPE == itemType)
                pType = Byte[].class;
            else if (Short.TYPE == itemType)
                pType = Short[].class;
            else if (Integer.TYPE == itemType)
                pType = Integer[].class;
            else if (Long.TYPE == itemType)
                pType = Long[].class;
            else if (Float.TYPE == itemType)
                pType = Float[].class;
            else if (Double.TYPE == itemType)
                pType = Double[].class;
        }
        return pType;
    }

    /**
     * 클래스의 기본객체를 반환한다.
     *
     * @param cls
     * @return 각 클래스의 기본 객체
     */
    public static Object getDefaultValue(Class<?> cls)
    {
        if (!cls.isArray())
        {
            if (String.class.isAssignableFrom(cls))
                return "";
            else if  (Short.class.isAssignableFrom(cls) || short.class.isAssignableFrom(cls))
                return new Short((short)0);
            else if  (Character.class.isAssignableFrom(cls) || char.class.isAssignableFrom(cls))
                return new Character((char)0);
            else if  (Byte.class.isAssignableFrom(cls) || byte.class.isAssignableFrom(cls))
                return new Byte((byte)0);
            else if  (Integer.class.isAssignableFrom(cls) || int.class.isAssignableFrom(cls))
                return new Integer(0);
            else if  (Long.class.isAssignableFrom(cls) || long.class.isAssignableFrom(cls))
                return new Long(0);
            else if  (Float.class.isAssignableFrom(cls) || float.class.isAssignableFrom(cls))
                return new Float((float) 0);
            else if  (Double.class.isAssignableFrom(cls) || double.class.isAssignableFrom(cls))
                return new Double(0);
            else if  (Boolean.class.isAssignableFrom(cls) || boolean.class.isAssignableFrom(cls))
                return Boolean.FALSE;
            else if  (BigInteger.class.isAssignableFrom(cls))
                return new BigInteger("0");
            else if  (BigDecimal.class.isAssignableFrom(cls))
                return new BigDecimal(0);
            else if  (Date.class.isAssignableFrom(cls))
                return new Date();
            else
            {
                try
                {
                    return cls.newInstance();
                }
                catch (Exception ignored)
                {
                    //
                }

                return null;
            }
        }
        else
        {
            return Array.newInstance(cls.getComponentType(), 0);
        }
    }

    /**
     * 날짜를 20050505 식의 년월일 형태로 반환한다.
     * @param date Date
     * @return 년월일
     */
    public static int getDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return year * 10000 + month * 100 + day;
    }

    /**
     * 날짜를 101010 식의 시분초 형태로 반환한다.
     * @param date Date
     * @return 시분초
     */
    public static int getTime(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour * 10000 + minute * 100 + second;
    }

    /**
     * 날짜를 20050505101010 식의 년월일시분초 형태로 반환한다.
     * @param date Date
     * @return 년월일시분초
     */
    public static long getDateTime(Date date)
    {
        return getDate(date) * 1000000L + getTime(date);
    }
}
