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

public class AttributeSet {

	BasicJavaType owner;
	Map<String, AttributeDescriptor> attributes = new HashMap<String, AttributeDescriptor>();
	List<AttributeDescriptor> orderedAttributes = new ArrayList<AttributeDescriptor>();
	
	public AttributeSet(BasicJavaType owner) {
		this.owner = owner;
	}
	
	public AttributeSet addAttribute(AttributeDescriptor attribute) {
		if (attributes.containsKey(attribute.getName()))
			throw new GeneratorException("Name already used on class "+owner.getPackageName()+"."+owner.getTypeName()+" : "+attribute.getName());
		attributes.put(attribute.getName(), attribute);
		orderedAttributes.add(attribute);
		return this;
	}
	
	public boolean accept(String attributeName) {
		if (Keywords.isKeyword(attributeName))
			return false;
		return !attributes.containsKey(attributeName);
	}
	
	public String[] getText() {
		String[] text = {
			"/// attributes here"	
		};
		for (AttributeDescriptor attribute : orderedAttributes) {
			text = Rewrite.insertLines(text, "/// attributes here", attribute.getText());
		}
		return Rewrite.removeLine(text, "/// attributes here");
	}
	
	public AttributeDescriptor getAttribute(SemanticDescriptor semantic) {
		for (AttributeDescriptor attr : orderedAttributes) {
			if (attr.recognizes(semantic))
				return attr;
		}
		return null;
	}
}
