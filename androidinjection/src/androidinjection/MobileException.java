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
	 * ���� ���ο� Exception�� ����� �Ʒ��� ���� �߻���ų ���,
	 * <pre>
	 * try
	 * {
	 *     throw new AnyException('���� �߻�');
	 * }
	 * catch(Exception e)
	 * {
	 *     throw new MobileException(e);
	 * }
	 * </pre>
	 * �̷������� <code>try</code>�ȿ��� �߻��� �� ���,
	 * <code>printStackTrace()</code>�� ��½� �� ������,
	 * <code>StackTraceElement</code>�� ��ġ�� <code>catch</code>
	 * ���� ���� ��ġ�� �ȴ�. ����, ���� �ҽ��� ����κп���
	 * ������ �߻��ߴ��� �˼��� ����, �׷��ٰ� ��� Exception�� ���ؼ�
	 * ���� �� ����.
	 * ���� �� ���� StackTraceElement�� �պ��ϹǷν�,
	 * ���� �ҽ����� ��ġ�� �˷��ش�.
	 * @param source �ڽ��� StackTraceElement
	 * @param target �պ��� StackTraceElement
	 * @return �պ��� StackTraceElement
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
	 * <code>Exception</code> �߻��� �߻��� ��ġ�� ������ ����Ѵ�.
	 * @param e
	 * @return ��ġ �� ȭ�ϳ��� �߻� �޼ҵ���� ��Ÿ���� ���ڿ�
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
	 * Exception�� ���� StackTrace�� ���ڿ��� ��ȯ�Ѵ�.
	 * @param e Throwable
	 * @return ���ڿ�
	 */
	public static String toStrackTraceString(Throwable e) {
		StringWriter write = new StringWriter();
		PrintWriter p = new PrintWriter(write);
		e.printStackTrace(p);
		return write.toString();
	}
}

