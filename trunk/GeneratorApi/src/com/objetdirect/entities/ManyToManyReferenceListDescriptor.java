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
import com.objetdirect.engine.Rewrite;

public class ManyToManyReferenceListDescriptor extends ReferenceListDescriptor {

	public ManyToManyReferenceListDescriptor(
		EntityDescriptor owner, 
		EntityDescriptor target,  
		String name,
		boolean composition) 
	{
		super(owner, target, name, composition);
		init();
	}
		
	protected AttributeDescriptor buildAttribute() {
		AttributeDescriptor attr = super.buildAttribute();
		if (isComposition()) {
			attr.addAnnotation(JavaxPersistence.ManyToMany,"cascade={CascadeType.REMOVE}");
			attr.replace("CascadeType", JavaxPersistence.CascadeType);
		}
		else
			attr.addAnnotation(JavaxPersistence.ManyToMany);
		return attr;
	}
		
	@Override
	public ManyToManyReferenceListDescriptor setReverse(
		RelationshipDescriptor reverse,
		boolean isManager) 
	{
		super.setReverse(reverse, isManager);
		adjustAttribute(reverse);
		adjustAdder(reverse);
		adjustRemover(reverse);
		adjustPreRemove(reverse);
		return this;
	}

	protected void adjustAttribute(RelationshipDescriptor reverse) {
		if (!isManager())
			getAttribute().addAnnotationParam(JavaxPersistence.ManyToMany, "mappedBy=\""+reverse.getAttribute().getName()+"\"");
	}

	protected void adjustPreRemove(RelationshipDescriptor reverse) {
		MethodDescriptor preRemove = getOwner().getPreRemoveMethod();
		modifyPreRemove(preRemove, false, reverse);
	}
		
	public void modifyPreRemove(MethodDescriptor preRemove, boolean protect, RelationshipDescriptor reverse) {
		String[] text = {
			"for (TargetEntity element : attribute) {",
			"	if (!element.deletionFlag)",
			"		element.reverse.remove(this);",
			"}"
		};
		if (protect)
			text = Rewrite.replace(text, "(!element.deletionFlag)", "(element.reverse!=null && !element.deletionFlag)");
		preRemove.insertLines("/// pre remove instructions here", text);
		preRemove.replace("element", elementName);
		preRemove.replace("TargetEntity", getTarget().getClassDescriptor());
		preRemove.replace("attribute", getAttribute());
		preRemove.replace("deletionFlag", reverse.getOwner().getInDeletion());
		preRemove.replace("reverse", reverse.getAttribute());
	}

	protected void adjustAdder(RelationshipDescriptor reverse) {
		modifyAdder(adder, false, reverse);
	}
	
	public void modifyAdder(MethodDescriptor adder, boolean protect, RelationshipDescriptor reverse) {
		ManyToManyReferenceListDescriptor inverse = (ManyToManyReferenceListDescriptor)getReverse();
		if (protect) {
			adder.insertLines("/// insert post-processing here",
				"if (param.reverse != null)",
				"	param.reverse.add(this);"
			);
		} else {
			adder.insertLines("/// insert post-processing here",
				"param.reverse.add(this);"
			);
		}
		adder.replace("reverse", inverse.getAttribute());
		adder.replace("param", elementName);
	}

	protected void adjustRemover(RelationshipDescriptor reverse) {
		modifyRemover(remover, false, reverse);
	}

	public void modifyRemover(MethodDescriptor remover, boolean protect, RelationshipDescriptor reverse) {
		ManyToManyReferenceListDescriptor inverse = (ManyToManyReferenceListDescriptor)getReverse();
		if (protect) {
			remover.insertLines("/// insert post-processing here",
				"if (param.reverse != null)",
				"	param.reverse.remove(this);"
			);
		} else {
			remover.insertLines("/// insert post-processing here",
				"param.reverse.remove(this);"
			);
		}
		remover.replace("reverse", inverse.getAttribute());
		remover.replace("param", elementName);
	}
}
