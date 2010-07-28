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

import java.util.ArrayList;
import java.util.List;

public class AnnotationDescriptor {

	TypeDescriptor type;
	CodeDescriptor codeOwner;
	BasicJavaType classOwner;
	List<String> params = new ArrayList<String>();
	
	public AnnotationDescriptor(TypeDescriptor type) {
		this.type = type;
	}
	
	public void setOwner(CodeDescriptor owner) {
		this.codeOwner = owner;
		owner.getOwner().getImportSet().addType(type);
	}
	
	public void setOwner(BasicJavaType owner) {
		this.classOwner = owner;
		owner.getImportSet().addType(type);
	}

	public void addParam(String param) {
		params.add(param);
	}
	
	public BasicJavaType getOwnerClass() {
		if (classOwner!=null)
			return classOwner;
		else
			return codeOwner.getOwner();
	}
	
	public TypeDescriptor getType() {
		return type;
	}
	
	public String[] getText() {
		String[] text = Rewrite.replace(
			new String[] {"@Annotation(annParam)" }, "Annotation", type.getUsageName(getOwnerClass()));
		for (String param : params) {
			text = Rewrite.replace(text, "annParam", param+", annParam");
		}
		text = Rewrite.remove(text, "(annParam)");
		text = Rewrite.remove(text, ", annParam");
		return text;
	}
}
