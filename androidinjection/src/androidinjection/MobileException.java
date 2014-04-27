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
package androidinjection;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * MobileException
 * @author Eun Jeong-Ho, silver@hihds.com
 * @version 2012. 10. 31
 */
public class MobileException extends RuntimeException
{
	private static final long serialVersionUID = -8287667115772786637L;

	private String msg = "";

	public MobileException() {
		super();
	}

	public MobileException(String msg) {
		super();
		this.msg = msg;
	}

	public MobileException(String msg, Throwable e) {
		super(e);
		this.msg += " Msg:[" + msg + "]";
	}

	public MobileException(Throwable e) {
		super(e);
		this.msg = "Caused:[" + e.toString() + "]";
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}
	
	public String toExceptionString() {
		return toExceptionString(this.getMessage(), 
				merge(this.getStackTrace(), this.getCause().getStackTrace()));
	}
	
	/**
	 * 보통 새로운 Exception을 만들어 아래와 같이 발생시킬 경우,
	 * <pre>
	 * try
	 * {
	 *     throw new AnyException('에러 발생');
	 * }
	 * catch(Exception e)
	 * {
	 *     throw new MobileException(e);
	 * }
	 * </pre>
	 * 이런식으로 <code>try</code>안에서 발생이 된 경우,
	 * <code>printStackTrace()</code>의 출력시 맨 마지막,
	 * <code>StackTraceElement</code>의 위치가 <code>catch</code>
	 * 에서 잡힌 위치가 된다. 따라서, 실제 소스의 어느부분에서
	 * 에러가 발생했는지 알수가 없다, 그렇다고 모든 Exception에 대해서
	 * 잡을 수 없다.
	 * 따라서 두 개의 StackTraceElement을 합병하므로써,
	 * 실제 소스상의 위치를 알려준다.
	 * @param source 자신의 StackTraceElement
	 * @param target 합병할 StackTraceElement
	 * @return 합병된 StackTraceElement
	 */
	public static StackTraceElement[] merge(StackTraceElement[] source, StackTraceElement[] target) {
		int x = (source.length > 2) ? 1 : 0;
		List<StackTraceElement> list = new ArrayList<StackTraceElement>();
		for (int i = 0; i < target.length; i++) {
			if (!source[x].equals(target[i]))
				list.add(target[i]);
			else
				break;
		}

		for (int i = 0; i < source.length; i++) {
			list.add(source[i]);
		}

		return (StackTraceElement[]) list.toArray(new StackTraceElement[]{});
	}
	
	/**
	 * <code>Exception</code> 발생시 발생된 위치와 내용을 출력한다.
	 * @param e
	 * @return 위치 및 화일내용 발생 메소드등을 나타내는 문자열
	 */
	public static String toExceptionString(String msg, StackTraceElement[] elements) {
		StackTraceElement element = elements[0];

		String filename = element.getFileName();
		String methodname = element.getMethodName();
		int linenumber = element.getLineNumber();

		StringBuffer buf = new StringBuffer();
		buf.append('(').append(filename).append(':')
				.append(methodname).append("():")
				.append(linenumber).append(") ")
				.append(msg);

		return buf.toString();
	}

	/**
	 * Exception에 대한 StackTrace를 문자열로 반환한다.
	 * @param e Throwable
	 * @return 문자열
	 */
	public static String toStrackTraceString(Throwable e) {
		StringWriter write = new StringWriter();
		PrintWriter p = new PrintWriter(write);
		e.printStackTrace(p);
		return write.toString();
	}
}

