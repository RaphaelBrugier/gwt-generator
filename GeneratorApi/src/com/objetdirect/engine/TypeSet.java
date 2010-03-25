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

public class TypeSet {

	BasicClassDescriptor owner;
	Map<String, InternalTypeDescriptor> types = new HashMap<String, InternalTypeDescriptor>();
	List<InternalTypeDescriptor> orderedTypes = new ArrayList<InternalTypeDescriptor>();
	
	public TypeSet(BasicClassDescriptor owner) {
		this.owner = owner;
	}
	
	public TypeSet addType(InternalTypeDescriptor type) {
		if (types.containsKey(type.getTypeName()))
			throw new GeneratorException("Name already used on class "+owner.getPackageName()+"."+owner.getTypeName()+" : "+type.getTypeName());
		types.put(type.getTypeName(), type);
		orderedTypes.add(type);
		return this;
	}
	
	public boolean accept(String typeName) {
		if (Keywords.isKeyword(typeName))
			return false;
		return !types.containsKey(typeName);
	}
	
	public InternalTypeDescriptor getMethod(String typeName) {
		return types.get(typeName);
	}
	
	public String[] getText() {
		String[] text = {
			"/// types here"
		};
		boolean first = true;
		for (InternalTypeDescriptor type : orderedTypes) {
			if (first) 
				first = false;
			else
				text = Rewrite.insertLines(text, "/// types here", "");
			text = Rewrite.insertLines(text, "/// types here", type.getText());
		}
		return Rewrite.removeLine(text, "/// types here");
	}
}