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



/**
 * 
 * @author Eun Jeong-Ho, silver@intos.biz
 * @version 2005. 9. 30. 
 */
public class CharacterConvertor extends Convertor
{
	public CharacterConvertor()
	{
    	register(Character.class);
	}

	/**
	 * °´Ã¼¸¦ º¯È¯ÇÑ´Ù.
	 * @param obj °´Ã¼
	 * @return º¯È¯µÈ °´Ã¼
	 */
	public Object convert(Object obj)
	{
		String str = (String) convert(String.class,  obj);
		if (str != null && str.length() > 0)
			return new Character(str.charAt(0));
		else
			return new Character((char)0);
	}

}
