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

public class StandardNameMaker implements NameMaker {

	String prefix;
	String suffix;
	Object element;
	
	public StandardNameMaker(String prefix, String suffix, Object element) {
		this.prefix= prefix;
		this.element = element;
		this.suffix = suffix;
	}

	public StandardNameMaker(String baseName) {
		this(baseName, null, null);
	}
	
	int index=0;
	
	public String makePropertyName() {
		return NamingUtil.toProperty(makeMemberName());
	}
	
	public String makeMemberName() {
		boolean indexed = false;
		String result = null;
		if (prefix!=null) {
			if (prefix.toUpperCase().equals(prefix))
				result = prefix;
			else
				result = NamingUtil.toMember(prefix);
		}
		
		if (element!=null) {
			if (result!=null)
				result+=NamingUtil.toProperty(getName(element));
			else
				result=getName(element);
			if (index!=0) {
				indexed = true;
				result+=index;
			}
		}
		
		if (result==null) {
			if (suffix==null && suffix.length()>0)
				throw new GeneratorException("No null name can be generated");
			result = NamingUtil.toMember(suffix);
		}
		else if (suffix!=null && suffix.length()>0)
			result+=NamingUtil.toProperty(suffix);
		if (!indexed && index!=0)
			result+=index;
		index++;
		return result;
	}
	
	String getName(Object element) {
		if (element instanceof AttributeDescriptor)
			return ((AttributeDescriptor) element).getName();
		else if (element instanceof TypeDescriptor)
			return ((TypeDescriptor) element).getTypeName();
		else if (element instanceof MethodDescriptor)
			return ((MethodDescriptor) element).getName();
		else
			throw new GeneratorException("Unable to get name of "+element.getClass());
	}

}

