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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eun Jeong-Ho, silver@intos.biz
 * @version 2006. 5. 4
 */
public class BeanWrapper
{
    private Object bean;
    private BeanInfo info;
    private Map<String, String> names = new HashMap<String, String>();

    public BeanWrapper(Class<?> beanClass) throws IllegalAccessException, InstantiationException
    {
        bean = beanClass.newInstance();
        init(bean, false);
    }

    public BeanWrapper(Object bean)
    {
        init(bean, false);
    }

    public BeanWrapper(Class<?> beanClass, boolean isPrivateAccess) throws IllegalAccessException, InstantiationException
    {
        bean = beanClass.newInstance();
        init(bean, isPrivateAccess);
    }

    public BeanWrapper(Object bean, boolean isPrivateAccess)
    {
        init(bean, isPrivateAccess);
    }

    private void init(Object bean, boolean isPrivateAccess)
    {
        this.bean = bean;
        info = BeanInfo.getInstance(bean.getClass(), isPrivateAccess);
        String[] tmp = info.getFieldNames();
        for (int i = 0; i < tmp.length; i++)
            names.put(tmp[i].toLowerCase(), tmp[i]);

        tmp = info.getGetterNames();
        for (int i = 0; i < tmp.length; i++)
            names.put(tmp[i].toLowerCase(), tmp[i]);

        tmp = info.getSetterNames();
        for (int i = 0; i < tmp.length; i++)
            names.put(tmp[i].toLowerCase(), tmp[i]);
    }

    public void set(String name, Object value)
    {
        String tname = names.get(name.toLowerCase());
        if (tname == null)
            throw new BeanNotAccessException("There is no FIELD property or Method named '" + name + "' in class '" + bean.getClass().getName() + "'");

        if (info.hasSetter(tname))
            info.invokeSetter(bean, tname, value);
        else
            info.setField(bean, tname, value);
    }

    public void set(String name, Object value, int index)
    {
        String tname = names.get(name.toLowerCase());
        if (tname == null)
            throw new BeanNotAccessException("There is no FIELD property or Method named '" + name + "' in class '" + bean.getClass().getName() + "'");

        Object val;
        if (info.hasGetter(tname))
            val = info.invokeGetter(bean, tname);
        else
            val = info.getField(bean, tname);

        Class<?> cls = val.getClass();
        if (cls.isArray())
        {
            int len = Array.getLength(val);
            if (index >= len)
                throw new ArrayIndexOutOfBoundsException(index);
            Array.set(val, index, value);   
        }
        else if (val instanceof List)
        {
            List<Object> list = (List<Object>) val;
            list.add(index, value);
        }
    }

    public Object get(String name)
    {
        String tname = (String) names.get(name.toLowerCase());
        if (tname == null)
            throw new BeanNotAccessException("There is no FIELD property or Method named '" + name + "' in class '" + bean.getClass().getName() + "'");

        Object value;
        if (info.hasGetter(tname))
            value = info.invokeGetter(bean, tname);
        else
            value = info.getField(bean, tname);

        return value;
    }

    public Object get(String name, int index)
    {
        String tname = (String) names.get(name.toLowerCase());
        if (tname == null)
            throw new BeanNotAccessException("There is no FIELD property or Method named '" + name + "' in class '" + bean.getClass().getName() + "'");

        Object value;
        if (info.hasGetter(tname))
            value = info.invokeGetter(bean, tname);
        else
            value = info.getField(bean, tname);

        Class<?> cls = value.getClass();
        if (cls.isArray())
        {
            int len = Array.getLength(value);
            if (index >= len)
                throw new ArrayIndexOutOfBoundsException(index);
            for (int i = 0; i < len; i++)
                value = Array.get(value, i);
        }
        else if (value instanceof Collection)
        {
            Collection<Object> list = (Collection<Object>) value;
            if (index >= list.size())
                throw new IndexOutOfBoundsException("Collection index out of range: " + index);
            Iterator<Object> itr = list.iterator();
            int i = 0;
            while(itr.hasNext())
            {
                if (i == index)
                    value = itr.next();
            }
        }
        return value;
    }

    public Class<?> getType(String name)
    {
        String tname = (String) names.get(name.toLowerCase());
        if (tname == null)
            throw new BeanNotAccessException("There is no FIELD property or Method named '" + name + "' in class '" + bean.getClass().getName() + "'");

        Class<?> value;
        if (info.hasGetter(tname))
            value = info.getGetterType(tname);
        else
            value = info.getFieldType(tname);

        return value;
    }

    public int size(String name)
    {
        String tname = (String) names.get(name.toLowerCase());
        if (tname == null)
            throw new BeanNotAccessException("There is no FIELD property or Method named '" + name + "' in class '" + bean.getClass().getName() + "'");

        Object value;
        if (info.hasGetter(tname))
            value = info.invokeGetter(bean, tname);
        else
            value = info.getField(bean, tname);

        if (value != null)
        {
            Class<?> cls = value.getClass();
            if (cls.isArray())
            {
                return Array.getLength(value);
            }
            else if (value instanceof Collection)
            {
                Collection<Object> list = (Collection<Object>) value;
                return list.size();
            }
            else
            {
                return 1;
            }
        }
        else
            return 0;
    }

    public boolean contains(String name)
    {
        String tname = (String) names.get(name.toLowerCase());
        return (tname != null);
    }

    public Object getBean()
    {
        return bean;
    }
}
