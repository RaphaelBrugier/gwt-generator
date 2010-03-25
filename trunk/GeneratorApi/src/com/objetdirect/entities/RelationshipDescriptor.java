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
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.TypeDescriptor;

public abstract class RelationshipDescriptor implements MemberDescriptor {
	EntityDescriptor owner;
	private EntityDescriptor target;
	private String name;
	private boolean composition;
	private MethodDescriptor getter;

	public RelationshipDescriptor(
		EntityDescriptor owner, 
		EntityDescriptor target,  
		String name,
		boolean composition) 
	{
		this.owner = owner;
		this.target = target;
		this.name = name;
		this.composition = composition;
	}
	
	protected AttributeDescriptor buildAttribute() {
		AttributeDescriptor attr = makeAttribute(getOwner().getClassDescriptor(), getTarget().getClassDescriptor());
		getOwner().getClassDescriptor().addAttribute(attr);
		return attr;
	}
	
	public abstract AttributeDescriptor makeAttribute(ClassDescriptor owner, ClassDescriptor target);
	
	protected MethodDescriptor buildGetter() {
		MethodDescriptor getter = makeGetter(getOwner(), getAttribute());
		if (getter!=null)
			owner.getClassDescriptor().addMethod(getter);
		return getter;
	}

	public MethodDescriptor makeGetter(EntityDescriptor owner, AttributeDescriptor attr) {
		MethodDescriptor getter = StandardMethods.getter(attr, "public");
		if (owner.isSecured()) {
			getter.insertLines("/// insert pre-processing here", "verifyRead();");
		}
		return getter;
	}

	protected abstract void adjustBusinessConstructor();
	
	protected void setGetter(MethodDescriptor getter) {
		this.getter = getter;
	}
	
	public EntityDescriptor getOwner() {
		return owner;
	}
	
	public EntityDescriptor getTarget() {
		return target;
	}

	public String getName() {
		return name;
	}

	public abstract AttributeDescriptor getAttribute();
	
	public MethodDescriptor getGetter() {
		return getter;
	}

	public boolean isComposition() {
		return composition;
	}
	
	RelationshipDescriptor reverse = null;
	boolean isManager = true;
	
	public RelationshipDescriptor setReverse(RelationshipDescriptor reverse, boolean isManager) {
		this.reverse = reverse;
		this.isManager = isManager;
		return this;
	}
	
	public RelationshipDescriptor getReverse(){
		return reverse;
	}
	
	public boolean isManager() {
		return isManager;
	}
	
	public TypeDescriptor getType() {
		return getAttribute().getType();
	}
	
	public abstract void modifyPreRemove(MethodDescriptor preRemove, boolean protect, RelationshipDescriptor reverse);
}

