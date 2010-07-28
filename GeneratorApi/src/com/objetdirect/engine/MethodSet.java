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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSet {

	BasicJavaType owner;
	Map<String, MethodDescriptor> methods = new HashMap<String, MethodDescriptor>();
	List<MethodDescriptor> orderedMethods = new ArrayList<MethodDescriptor>();
	
	public MethodSet(BasicJavaType owner) {
		this.owner = owner;
	}
	
	public MethodSet addMethod(MethodDescriptor method) {
		if (methods.containsKey(method.getName()))
			throw new GeneratorException("Name already used on class "+owner.getPackageName()+"."+owner.getTypeName()+" : "+method.getName());
		methods.put(method.getName(), method);
		orderedMethods.add(method);
		return this;
	}
	
	public boolean accept(String methodName) {
		if (Keywords.isKeyword(methodName))
			return false;
		return !methods.containsKey(methodName);
	}
	
	public MethodDescriptor getMethod(String methodName) {
		return methods.get(methodName);
	}
	
	public String[] getText() {
		String[] text = {
			"/// methods here"
		};
		boolean first = true;
		for (MethodDescriptor method : orderedMethods) {
			if (first) 
				first = false;
			else
				text = Rewrite.insertLines(text, "/// methods here", "");
			text = Rewrite.insertLines(text, "/// methods here", method.getText());
		}
		return Rewrite.removeLine(text, "/// methods here");
	}
	
	public MethodDescriptor getMethod(SemanticDescriptor semantic) {
		for (MethodDescriptor meth : orderedMethods) {
			if (meth.recognizes(semantic))
				return meth;
		}
		return null;
	}
}
