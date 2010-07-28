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
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.seam.Hibernate;

public class ManyToOneReferenceDescriptor extends ReferenceDescriptor {

	public ManyToOneReferenceDescriptor(
		EntityDescriptor owner, 
		EntityDescriptor target,  
		String name,
		boolean mandatory,
		boolean initialized) 
	{
		super(owner, target, name, mandatory, false, initialized);
		init();
	}
	
	protected AttributeDescriptor buildAttribute() {
		AttributeDescriptor attr = super.buildAttribute();
		attr.addAnnotation(JavaxPersistence.ManyToOne);
		if (isMandatory()) {
			attr.addAnnotation(Hibernate.NotNull);
		}		
		return attr;
	}
	
	public MethodDescriptor makeSetter(EntityDescriptor owner, AttributeDescriptor attr) {
		MethodDescriptor setter = super.makeSetter(owner, attr);
		if (isMandatory()) {
			setter.insertLines("/// insert pre-processing here",
				"if (attribute == null)",
				"	throw new MandatoryException(this, \"attribute\");"
			);
			setter.replace("MandatoryException", EntityFramework.RequiredException);
		}
		return setter;
	}

	@Override
	public ManyToOneReferenceDescriptor setReverse(
			RelationshipDescriptor reverse,
			boolean isManager) 
	{
		super.setReverse(reverse, isManager);
		adjustAttribute(reverse);
		adjustSetter(reverse);
		adjustPreRemove(reverse);
		return this;
	}

	protected void adjustAttribute(RelationshipDescriptor reverse) {
		if (!isManager())
			getAttribute().addAnnotationParam(JavaxPersistence.ManyToOne, "mappedBy=\""+reverse.getAttribute().getName()+"\"");
	}

	protected void adjustPreRemove(RelationshipDescriptor reverse) {
		MethodDescriptor preRemove = getOwner().getPreRemoveMethod();
		if (!isMandatory())
			preRemove.insertLines("/// pre remove instructions here", 
				"if (this.attribute != null && !this.attribute.deletionFlag)",
				"	this.attribute.reverse.remove(this);"
			);
		else {
			preRemove.insertLines("/// pre remove instructions here", 
				"if (!this.attribute.deletionFlag)",
				"	this.attribute.reverse.remove(this);"
			);
		}
		preRemove.replace("attribute", getAttribute());
		preRemove.replace("deletionFlag", reverse.getOwner().getInDeletion());
		preRemove.replace("reverse", reverse.getAttribute());
	}
	
	public void modifyPreRemove(MethodDescriptor preRemove, boolean protect, RelationshipDescriptor reverse) {
		if (!isMandatory())
			if (protect) {
				preRemove.insertLines("/// pre remove instructions here", 
					"if (this.attribute != null && this.attribute.reverse != null && !this.attribute.deletionFlag)",
					"	this.attribute.reverse.remove(this);"
				);
			} else {
				preRemove.insertLines("/// pre remove instructions here", 
					"if (this.attribute != null && !this.attribute.deletionFlag)",
					"	this.attribute.reverse.remove(this);"
				);
			}
		else {
			if (protect) {
				preRemove.insertLines("/// pre remove instructions here", 
					"if (!this.attribute.deletionFlag)",
					"	this.attribute.reverse.remove(this);"
				);
			} else {
				preRemove.insertLines("/// pre remove instructions here", 
					"if (this.attribute != null && !this.attribute.deletionFlag)",
					"	this.attribute.reverse.remove(this);"
				);
			}
		}
		preRemove.replace("attribute", getAttribute());
		preRemove.replace("deletionFlag", reverse.getOwner().getInDeletion());
		preRemove.replace("reverse", reverse.getAttribute());
	}

	protected void adjustSetter(RelationshipDescriptor reverse) {
		modifySetter(setter, false, reverse);
	}
	
	public void modifySetter(MethodDescriptor setter, boolean protect, RelationshipDescriptor reverse) {
		OneToManyReferenceListDescriptor inverse = (OneToManyReferenceListDescriptor)getReverse();
		setter.insertLines("/// insert pre-processing here",
			"if (this.attribute == attribute)",
			"	return;"
		);
		if (protect) {
			setter.insertLines("/// insert pre-processing here",
			    "if (this.attribute != null && this.attribute.reverse != null)",
			    "	this.attribute.reverse.remove(this);"
			);
		} else {
			setter.insertLines("/// insert pre-processing here",
			    "if (this.attribute != null)",
			    "	this.attribute.reverse.remove(this);"
			);
		}
		if (!isMandatory()) {
			if (protect) {
				setter.insertLines("/// insert post-processing here",
					"if (attribute != null && attribute.reverse != null)",
					"	attribute.reverse.add(this);"
				);
			} else {
				setter.insertLines("/// insert post-processing here",
					"if (attribute != null)",
					"	attribute.reverse.add(this);"
				);
			}
		}
		else {
			if (protect) {
				setter.insertLines("/// insert post-processing here",
					"if (attribute.reverse != null)",
					"	attribute.reverse.add(this);"
				);
			} else {
				setter.insertLines("/// insert post-processing here",
					"attribute.reverse.add(this);"
				);
			}
		}
		setter.replace("reverse", inverse.getAttribute());
		setter.replace("MandatoryException", EntityFramework.RequiredException);
	}
}

