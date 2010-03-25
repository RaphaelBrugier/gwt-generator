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
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardMethods;

public abstract class ReferenceDescriptor extends RelationshipDescriptor {
	boolean mandatory;
	boolean initialized;
	AttributeDescriptor attribute;
	MethodDescriptor setter;

	public ReferenceDescriptor(
		EntityDescriptor owner, 
		EntityDescriptor target,  
		String name,
		boolean mandatory,
		boolean composition,
		boolean initialized) 
	{
		super(owner, target, name, composition);
		this.mandatory = mandatory;
		this.initialized = initialized;
	}
	
	protected void init() {
		this.attribute = buildAttribute();
		setGetter(buildGetter());
		this.setter = buildSetter();
		adjustBusinessConstructor();
	}
	
	public AttributeDescriptor makeAttribute(ClassDescriptor owner, ClassDescriptor target) {
		AttributeDescriptor attribute = new AttributeDescriptor();
		attribute.init(owner, target, getName());
		attribute.setSemantic(new SemanticDescriptor("implements", this));
		return attribute;
	}
		
	protected final MethodDescriptor buildSetter() {
		MethodDescriptor setter = makeSetter(getOwner(), getAttribute());
		getOwner().getClassDescriptor().addMethod(setter);
		return setter;
	}
		
	public MethodDescriptor makeSetter(EntityDescriptor owner, AttributeDescriptor attr) {
		MethodDescriptor setter = StandardMethods.setter(attr, "public");
		if (owner.isSecured()) {
			setter.insertLines("/// insert pre-processing here", "verifyWrite();");
		}
		return setter;
	}

	public AttributeDescriptor getAttribute() {
		return attribute;
	}

	protected void adjustBusinessConstructor() {
		ConstructorDescriptor constructor = getOwner().getBusinessConstructor();
		adjustBusinessConstructor(constructor, attribute, setter);
	}
	
	public void adjustBusinessConstructor(ConstructorDescriptor constructor, AttributeDescriptor attr, MethodDescriptor setter) {
		if (isInitialized()) {
			constructor.addParameter(attr.getType(), attr.getName());
			constructor.insertLines("/// initializations here", "setter(attribute);");
			constructor.replace("setter", setter);
		}
		else
			constructor.insertLines("/// initializations here", "this.attribute = null;");
		constructor.replace("attribute", attr);
	}

	public MethodDescriptor getSetter() {
		return setter;
	}
	
	public boolean isMandatory() {
		return mandatory;
	}

	public boolean isInitialized() {
		return mandatory || initialized;
	}

	public abstract void modifySetter(MethodDescriptor setter, boolean protect, RelationshipDescriptor reverse);	
}
