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

package com.objetdirect.entities;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.Hibernate;

public class FieldDescriptor implements MemberDescriptor {
	EntityDescriptor owner;
	TypeDescriptor type;
	String name;
	String[] value;
	Object[] params;
	AttributeDescriptor attribute;
	MethodDescriptor getter;
	MethodDescriptor setter;
	boolean notNull = false;
	int minLength = -1;
	int maxLength = -1;
	
	public FieldDescriptor(
		EntityDescriptor owner, 
		TypeDescriptor type,  String name, 
		Object value, Object ... params) 
	{
		this.owner = owner;
		this.type = type;
		this.name = name;
		this.value = value instanceof String ? new String[] {(String)value} : (String[]) value;
		this.params = params;
		this.attribute = buildAttribute();
		this.getter = buildGetter();
		this.setter = buildSetter();
	}

	protected AttributeDescriptor buildAttribute() {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(owner.getClassDescriptor(), type, name);
		attr.setSemantic(new SemanticDescriptor("implements", this));
		return attr;
	}
	
	protected MethodDescriptor buildGetter() {
		return StandardMethods.getter(attribute, "public");
	}

	protected MethodDescriptor buildSetter() {
		return StandardMethods.setter(attribute, "public");
	}
	
	public TypeDescriptor getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String[] getValue() {
		return value;
	}
	
	public FieldDescriptor setNotNull(boolean notNull) {
		this.notNull = notNull;
		if (notNull)
			getAttribute().addAnnotation(Hibernate.NotNull);
		return this;
	}
	
	public boolean isNotNull() {
		return notNull;
	}
	
	public FieldDescriptor setLength(int minLength, int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
		if (minLength!=-1 || maxLength!=-1) {
			getAttribute().addAnnotation(Hibernate.Length);
			if (minLength!=-1)
				getAttribute().addAnnotationParam(Hibernate.Length, "min="+minLength);
			if (maxLength!=-1)
				getAttribute().addAnnotationParam(Hibernate.Length, "max="+maxLength);
		}
		return this;
	}
	
	public int getMinLength() {
		return minLength;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
	
	public AttributeDescriptor getAttribute() {
		return attribute;
	}
	
	public void addInitialization(ConstructorDescriptor constructor) {
		if (value==null) {
			constructor.addParameter(attribute.getType(), attribute.getName());
			constructor.insertLines("/// initializations here", "this.attribute = attribute;");
			constructor.replace("attribute", attribute);
		}
		else {
			constructor.insertLines("/// initializations here", "this.attribute = value;");
			constructor.replace("value", value);
			for (int i=0; i<params.length; i+=2) {
				String key = (String)params[i];
				Object value = params[i+1];
				if (value instanceof String)
					constructor.replace(key, (String)value);
				else if (value instanceof TypeDescriptor)
					constructor.replace(key, (TypeDescriptor)value);
				else if (value instanceof AttributeDescriptor)
					constructor.replace(key, (AttributeDescriptor)value);
				else if (value instanceof MethodDescriptor)
					constructor.replace(key, (MethodDescriptor)value);
			}
		}
		constructor.replace("attribute", attribute);
	}
	
	public MethodDescriptor getGetter() {
		return getter;
	}

	public MethodDescriptor getSetter() {
		return setter;
	}
}
