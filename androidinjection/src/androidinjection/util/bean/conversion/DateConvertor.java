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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Eun Jeong-Ho, silver@intos.biz
 * @version 2005. 9. 30. 
 */
public class DateConvertor extends Convertor
{
	public DateConvertor()
	{
		register(Date.class);
	}

    private Pattern ptrnNumber = Pattern.compile("^\\d+");
    private Pattern ptrnDateStr = Pattern.compile("([0-9]{2,4})(-|/|년|\\.)?\\s*([0-9]{1,2})(-|/|월|\\.)?\\s*([0-9]{1,2})(일)?\\s*(([0-9]{1,2})(:|시)?\\s*([0-9]{1,2})(:|분)?\\s*([0-9]{1,2})(초)?\\s*(.*([0-9]{1,3}))?)?");
    /**
	 * 객체를 변환한다.
	 * @param obj 객체
	 * @return 변환된 객체
	 */
	public Object convert(Object obj)
	{
		try
		{
			if (obj instanceof String && obj != null && ((String) obj).length() > 0 )
			{
				String str = (String) obj;
		        Matcher matcher = ptrnNumber.matcher(str);
                if (matcher.matches())
				{
					return getDateInNumber(obj.toString().trim());
				}
				else
				{
					matcher = ptrnDateStr.matcher(str);
					StringBuffer buf = new StringBuffer();
					StringBuffer style = new StringBuffer();
					if (matcher.find())
					{
						String year = matcher.group(1);
						if (year != null)
						{
							buf.append(year).append(' ');
							style.append("yyyy ");
						}
						String month = matcher.group(3);
						if (month != null)
						{
							buf.append(month).append(' ');
							style.append("MM ");
						}
						String day = matcher.group(5);
						if (day != null)
						{
							buf.append(day).append(' ');
							style.append("dd ");
						}
						String hour = matcher.group(8);
						if (hour != null)
						{
							buf.append(hour).append(' ');
							style.append("hh ");
						}
						String min = matcher.group(10);
						if (min != null)
						{
							buf.append(min).append(' ');
							style.append("ss ");
						}
						String sec = matcher.group(11);
						if (sec != null)
						{
							buf.append(sec).append(' ');
							style.append("mm ");
						}
						String msec = matcher.group(14);
						if (msec != null)
						{
							buf.append(msec).append(' ');
							style.append("SS ");
						}
					}
					String tmp = buf.toString();
					if (tmp != null && tmp.length() > 0)
					{
						DateFormat dateFormat = new SimpleDateFormat(style.toString());
			  			return dateFormat.parse(tmp);
					}
					return new Date(Long.parseLong(str));
				}

			}
			else if (obj instanceof Number)
			{
				return getDateInNumber(obj.toString());
			}
			else if (obj instanceof Date)
				return (Date) obj;
		}
		catch(ParseException e)
		{
			//
		}
		return null;
	}
	private static Date getDateInNumber(String number) throws ParseException
	{
		if (number.length() == "20050101010101000".length())	// 17
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhssmmSSS");
  			return dateFormat.parse(number);
		}
		else if (number.length() == "20050101010101".length())	// 14
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhssmm");
  			return dateFormat.parse(number);
		}
		else if (number.length() == "20050101".length())	// 8
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
  			return dateFormat.parse(number);
		}
		else if (number.length() == "010101".length())	// 6
		{
			DateFormat dateFormat = new SimpleDateFormat("hhssmm");
  			return dateFormat.parse(number);
		}
		else if (number.length() == "010101000".length())	// 9
		{
			DateFormat dateFormat = new SimpleDateFormat("hhssmmSSS");
  			return dateFormat.parse(number);
		}
		else
			return new Date(Long.parseLong(number));
	}

}
