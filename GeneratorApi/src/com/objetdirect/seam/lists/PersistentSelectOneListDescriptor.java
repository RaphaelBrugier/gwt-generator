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

package com.objetdirect.seam.lists;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ReferenceDescriptor;
import com.objetdirect.entities.ReferenceListDescriptor;
import com.objetdirect.entities.RelationshipDescriptor;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.MultipleSelectionCaller;
import com.objetdirect.seam.MultipleSelectionCallerDescriptor;

public class PersistentSelectOneListDescriptor 
	extends AbstractSelectOneListDescriptor 
	implements PersistentList, MultipleSelectionCaller, PersistentListHolder {

	String relationshipName = null;
	MethodDescriptor isListVisible;
	MultipleSelectionCallerDescriptor selectionCaller;
	
	public PersistentSelectOneListDescriptor(String relationshipName) {
		this.relationshipName = relationshipName;
	}

	public EntityDescriptor getParentEntityType() {
		return getParent(EntityHolder.class).getEntityType();
	}
	
	protected String getPseudoAttributeName() {
		return 
			NamingUtil.toMember(getParentEntityType().getName())+
			NamingUtil.toProperty(relationshipName);
	}
	
	public RelationshipDescriptor getRelationship() {
		EntityDescriptor entity = getParentEntityType();
		return entity.getRelationship(relationshipName);
	}
	
	public EntityDescriptor getEntityType() {
		EntityDescriptor entity = getParentEntityType();
		return entity.getRelationship(relationshipName).getTarget();
	}
	
	public MethodDescriptor getIsListVisibleMethod() {
		return isListVisible;
	}

	public void buildJavaPart() {
		super.buildJavaPart();
		if (selectionCaller!=null)
			selectionCaller.buildJavaPart();
		adjustParentSetCurrentEntity();
		adjustParentClearCurrentEntity();
	}
	
	public void buildFaceletPart() {
		buildListPart();
		if (selectionCaller!=null) {
			selectionCaller.buildFaceletPart();
			getFragment().insertFragment("/// insert commands here", selectionCaller.getFragment());
		}
		if (getDetail()!=null) {
			buildDetailPart();
		}
	}

	public void buildJavaElements(ClassDescriptor javaClass) {
		getDocument().declareEntityManager();
		isListVisible = buildIsListVisibleMethod(javaClass);
		super.buildJavaElements(javaClass);
	}

	protected MethodDescriptor buildIsCurrentEntityVisible(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean,
			new StandardNameMaker("is", "visible", currentEntity))
			.addModifier("public")
			.setContent(
				"return isListVisible() && currentEntity != null;")
			.replace("isListVisible", getIsListVisibleMethod())
			.replace("currentEntity", currentEntity);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildIsListVisibleMethod(ClassDescriptor javaClass) {
		PersistentListHolder list = getParent(PersistentListHolder.class);
		SelectOneDetailDescriptor detail = getParent(SelectOneDetailDescriptor.class);
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("is", getPseudoAttributeName()+"Visible", null))			
				.addModifier("public");
		meth.setContent("return currentEntity !=null && !isNewEntity;");
		if (detail==null)
			meth.remove(" && !isNewEntity");
		else
			meth.replace("isNewEntity", detail.getIsNewEntityAttribute());
		meth
			.replace("currentEntity", list.getEntityAttribute());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected void adjustParentSetCurrentEntity() {
		PersistentListHolder list = getParent(PersistentListHolder.class);
		if (list!=null) {
			MethodDescriptor setCurrentEntity = list.getSetEntityMethod();
			if (setCurrentEntity!=null)
				setCurrentEntity
					.insertLines(list.getPostProcessingTag(), "currentChildEntity = null;")
					.replace("currentChildEntity", getCurrentEntityAttribute());
		}
	}
	
	protected void adjustParentClearCurrentEntity(){
		PersistentListHolder list = getParent(PersistentListHolder.class);
		if (list!=null) {
			MethodDescriptor clearCurrentEntity = list.getClearEntityMethod();
			if (clearCurrentEntity!=null && getCurrentEntityAttribute()!=null)
				clearCurrentEntity
					.insertLines(list.getClearingTag(), "currentChildEntity = null;")
					.replace("currentChildEntity", getCurrentEntityAttribute());
		}
	}
	
	public PersistentSelectOneListDescriptor showField(
		String fieldName,
		String fieldTitle, 
		int length) 
	{
		super.showField(fieldName, fieldTitle, length);
		return this;
	}

	public PersistentSelectOneListDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		super.showNumberField(fieldName, fieldTitle, pattern, length);
		return this;
	}

	public PersistentSelectOneListDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		super.showDateField(fieldName, fieldTitle, pattern);
		return this;
	}

	public PersistentSelectOneListDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		super.showBooleanField(fieldName, fieldTitle, trueValue, falseValue);
		return this;
	}

	public PersistentSelectOneListDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		super.showEnumField(fieldName, fieldTitle, length);
		return this;
	}

	public PersistentSelectOneListDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		super.showEntityField(fieldName, fieldTitle, labels, length);
		return this;
	}
	
	public PersistentSelectOneListDescriptor addDelete(String deleteMessage) {
		super.addDelete(deleteMessage);
		return this;
	}

	protected MethodDescriptor buildListGetterMethod() {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			getClassDescriptor(), TypeDescriptor.List(getEntityType().getClassDescriptor()), 
			new StandardNameMaker("get", getPseudoAttributeName(), null))
			.addModifier("public");
		meth.setContent("return currentEntity.getChildren();");
		PersistentListHolder ld = getParent(PersistentListHolder.class);
		meth.replace("currentEntity", ld.getEntityAttribute());
		meth.replace("getChildren", getRelationship().getGetter());
		return meth;
	}

	public String[] addEntityToList(String entity) {
		if (getRelationship().getReverse()!=null && 
			(getRelationship().getReverse() instanceof ReferenceDescriptor)) {
			PersistentListHolder parent = getParent(PersistentListHolder.class);
			String[] text = {
				"currentChild.setRelationship(currentParent);"	
			};
			ReferenceDescriptor rd = (ReferenceDescriptor)getRelationship().getReverse();
			text = Rewrite.replace(text, "setRelationship", rd.getSetter().getName());
			text = Rewrite.replace(text, "currentParent", parent.getEntityAttribute().getName());
			text = Rewrite.replace(text, "currentChild", getCurrentEntityAttribute().getName());
			return text;
		}
		else {
			PersistentListHolder parent = getParent(PersistentListHolder.class);
			String[] text = {
				"currentParent.addEntity(currentChild);"	
			};
			ReferenceListDescriptor rld = (ReferenceListDescriptor)getRelationship();
			text = Rewrite.replace(text, "addEntity", rld.getAdder().getName());
			text = Rewrite.replace(text, "currentParent", parent.getEntityAttribute().getName());
			text = Rewrite.replace(text, "currentChild", getCurrentEntityAttribute().getName());
			return text;
		}
	}

	public String[] removeEntityFromList(String entity) {
		if (getRelationship().getReverse()!=null && 
				(getRelationship().getReverse() instanceof ReferenceDescriptor)) {
			String[] text = {
				"entity.setRelationship(null);"	
			};
			ReferenceDescriptor rd = (ReferenceDescriptor)getRelationship().getReverse();
			text = Rewrite.replace(text, "setRelationship", rd.getSetter().getName());
			text = Rewrite.replace(text, "entity", entity);
			return text;
		}
		else {
			PersistentListHolder parent = getParent(PersistentListHolder.class);
			String[] text = {
				"currentParent.removeEntity(entity);"	
			};
			ReferenceListDescriptor rld = (ReferenceListDescriptor)getRelationship();
			text = Rewrite.replace(text, "removeEntity", rld.getRemover().getName());
			text = Rewrite.replace(text, "currentParent", parent.getEntityAttribute().getName());
			text = Rewrite.replace(text, "entity", entity);
			return text;
		}
	}
	
	protected void prepareFragment() {
		FragmentDescriptor fd = new FragmentDescriptor(
			"<h:panelGroup rendered=\"#{beanName.entityListVisible}\">",
			"	/// insert list here",
			"</h:panelGroup>"
		);
		fd.replace("beanName", getDocument().getBeanName());
		fd.replaceProperty("entityListVisible", getIsListVisibleMethod());
		super.prepareFragment();
		fd.replaceAndInsertLines("/// insert list here", getFragment().getContent());
		getFragment().setContent(fd.getContent());
	}
	
	public PersistentSelectOneListDescriptor setSelectionCaller(MultipleSelectionCallerDescriptor selectionCaller) {
		this.selectionCaller = selectionCaller;
		selectionCaller.setOwner(this);
		return this;
	}
	
	public AttributeDescriptor getOwningEntityAttribute() {
		PersistentListHolder parent = getParent(PersistentListHolder.class);
		return parent.getEntityAttribute();
	}

	public MethodDescriptor getClearEntityMethod() {
		return getClearCurrentEntityMethod();
	}

	public AttributeDescriptor getEntityAttribute() {
		return getCurrentEntityAttribute();
	}

	public MethodDescriptor getSetEntityMethod() {
		return getSetCurrentEntityMethod();
	}

	public String getPostProcessingTag() {
		return "/// insert post-processing here";
	}
	
	public String getClearingTag() {
		return "/// continue clearing here";
	}

}
