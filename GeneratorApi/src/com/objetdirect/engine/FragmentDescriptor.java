/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.engine;

public class FragmentDescriptor {

	String[] content;
	
	public FragmentDescriptor(String ... content) {
		this.content = content;
	}
	
	public String[] getText() {
		content = Rewrite.removeLine(content, "///");
		return content;
	}
	
	public FragmentDescriptor replace(String ... replacements) {
		content = Rewrite.replace(content, replacements);
		return this;
	}
	
	public FragmentDescriptor replaceAndInsertLines(String replaceThis, String ... insertThis) {
		content = Rewrite.replaceAndInsertLines(content, replaceThis, insertThis);
		return this;
	}

	public FragmentDescriptor insertLines(String beforeThis, String ... insertThis) {
		content = Rewrite.insertLines(content, beforeThis, insertThis);
		return this;
	}
	
	public static String getProperty(MethodDescriptor getter) {
		return NamingUtil.extractMember(getter);
	}
	
	public FragmentDescriptor replaceProperty(String replaceThis, MethodDescriptor getter) {
		content = Rewrite.replace(content, replaceThis, getProperty(getter));
		return this;
	}
	
	public static String getMethod(MethodDescriptor method) {
		return method.getName();
	}

	public FragmentDescriptor replaceMethod(String replaceThis, MethodDescriptor method) {
		content = Rewrite.replace(content, replaceThis, getMethod(method));
		return this;
	}

	public FragmentDescriptor remove(String removeThis) {
		content = Rewrite.remove(content, removeThis);
		return this;
	}

	public FragmentDescriptor removeLine(String removeThis) {
		content = Rewrite.removeLine(content, removeThis);
		return this;
	}
	
	public FragmentDescriptor removeLines(String ... removeThis) {
		content = Rewrite.removeLines(content, removeThis);
		return this;
	}

	public String[] getContent() {
		return content;
	}

	public FragmentDescriptor insertFragment(String beforeThis, FragmentDescriptor insertThis) {
		if (insertThis!=null) {
			content = Rewrite.insertLines(content, beforeThis, insertThis.getText());
		}
		return this;
	}
	
	public FragmentDescriptor replaceByFragment(String replaceThis, FragmentDescriptor byThis) {
		if (byThis!=null) {
			content = Rewrite.insertLines(content, replaceThis, byThis.getText());
			content = Rewrite.removeLine(content, replaceThis);
		}
		return this;
	}

	public FragmentDescriptor setContent(String[] content) {
		this.content = content;
		return this;
	}
}
