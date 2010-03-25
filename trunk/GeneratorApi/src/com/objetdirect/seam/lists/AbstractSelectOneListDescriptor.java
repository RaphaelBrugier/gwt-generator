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
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.DetailHolder;
import com.objetdirect.seam.FieldRendererDescriptor;
import com.objetdirect.seam.Frmk;
import com.objetdirect.seam.PageDescriptor;

public abstract class AbstractSelectOneListDescriptor extends AbstractListDescriptor 
	implements DetailHolder {

	String[] rowSelectorPattern = defaultRowSelectorPattern;
	String[] deletionColumnPattern = defaultDeleteColumnPattern;
	String[] multipleSelectionPattern = defaultMultipleSelectionPattern;
	
	AttributeDescriptor currentEntity;
	MethodDescriptor selectEntity;
	MethodDescriptor getCurrentEntity;
	MethodDescriptor setCurrentEntity;
	MethodDescriptor isCurrentEntityVisible;
	MethodDescriptor setCurrentEntityVisible;
	MethodDescriptor clearCurrentEntity;
	MethodDescriptor multipleSelectionGetter;
	MethodDescriptor isEntitySelected;
	MethodDescriptor setEntitySelected;
	
	boolean multipleSelection = false;
	AttributeDescriptor selectedMapAttr;
	MethodDescriptor selectedMapGetter;

	String deleteMessage;
	
	AttributeDescriptor targetForDeletion;
	MethodDescriptor requestEntityForDeletion;
	MethodDescriptor getTargetForDeletion;
	MethodDescriptor deleteEntity;

	public AbstractSelectOneListDescriptor showField(FieldRendererDescriptor field) {
		super.showField(field);
		return this;	
	}

	public AbstractSelectOneListDescriptor showField(String fieldName, String fieldTitle, int length) {
		super.showField(fieldName, fieldTitle, length);
		return this;
	}

	public AbstractSelectOneListDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		super.showNumberField(fieldName, fieldTitle, pattern, length);
		return this;
	}

	public AbstractSelectOneListDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		super.showDateField(fieldName, fieldTitle, pattern);
		return this;
	}

	public AbstractSelectOneListDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		super.showBooleanField(fieldName, fieldTitle, trueValue, falseValue);
		return this;
	}

	public AbstractSelectOneListDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		super.showEnumField(fieldName, fieldTitle, length);
		return this;
	}

	public AbstractSelectOneListDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		super.showEntityField(fieldName, fieldTitle, labels, length);
		return this;
	}

	protected void addSelectionColumnToFragment() {
		FragmentDescriptor columnFragment = new FragmentDescriptor(multipleSelectionPattern);
		columnFragment.replace("beanName", getDocument().getBeanName());
		columnFragment.replaceProperty("selectedMap", getSelectedMapGetterMethod());
		getFragment().insertFragment("/// insert columns here", columnFragment);
		getFragment().replace("colWidth", "50px,colWidth");
		getFragment().replace("line", getEntityAlias());
	}
	
	protected void addColumnsToFragment() {
		if (multipleSelection)
			addSelectionColumnToFragment();
		super.addColumnsToFragment();
	}
	protected void manageSelection(FieldRendererDescriptor column, FragmentDescriptor columnFragment, boolean first) {
		if (first && getSelectEntityMethod()!=null) {
			FragmentDescriptor selectorFragment = new FragmentDescriptor(rowSelectorPattern);
			selectorFragment.replaceMethod("selectEntity", getSelectEntityMethod());
			selectorFragment.replace("line", getEntityAlias());
			if (isEntitySelected!=null)
				selectorFragment.replaceProperty("entitySelected", isEntitySelected);
			else
				selectorFragment.remove("value=\"#{beanName.entitySelected}\" ");
			selectorFragment.replace("beanName", getDocument().getBeanName());
			columnFragment.insertFragment("/// insert field here", selectorFragment);
		}
	}

	protected void buildListPart() {
		super.buildListPart();
		if (deleteMessage!=null)
			addDeleteToFragment();
	}

	protected void addDeleteToFragment() {
		FragmentDescriptor deleteFragment = new FragmentDescriptor(deletionColumnPattern);		
		deleteFragment.replace("beanName", getDocument().getBeanName());
		deleteFragment.replace("line", getEntityAlias());
		deleteFragment.replaceMethod("requestEntityDeletion", requestEntityForDeletion);
		getFragment().insertFragment("/// insert columns here", deleteFragment);
		getFragment().replace("colWidth", 100+"px,colWidth");
	}
	
	public AbstractSelectOneListDescriptor setDetail(SelectOneDetailDescriptor detail) {
		super.setDetail(detail);
		return this;
	}

	protected MethodDescriptor getIsDetailVisibleMethod() {
		return getIsCurrentEntityVisibleMethod();
	}
	
	public MethodDescriptor getIsCurrentEntityDirtyMethod() {
		return null;
	}
	
	protected MethodDescriptor getCloseDetailMethod() {
		return getDetail().getCancelCurrentEntityMethod();
	}
	
	protected SelectOneDetailDescriptor getDetail() {
		return (SelectOneDetailDescriptor)super.getDetail();
	}
	
	protected MethodDescriptor getEntityShownInDetailMethod() {
		return getGetCurrentEntityMethod();
	}
	
	
	public void buildJavaElements(ClassDescriptor javaClass) {
		super.buildJavaElements(javaClass);
		if (deleteMessage!=null)
			initDelete(javaClass);
		if (multipleSelection) {
			selectedMapAttr = buildSelectedAttribute(javaClass);
			selectedMapGetter = buildSelectedGetterMethod(javaClass);
		}
	}

	public AttributeDescriptor getCurrentEntityAttribute() {
		return currentEntity;
	}
	public void setSelectEntityMethod(MethodDescriptor selectEntity) {
		this.selectEntity = selectEntity;
	}
	public MethodDescriptor getSelectEntityMethod() {
		return selectEntity;
	}
	public MethodDescriptor getGetCurrentEntityMethod() {
		return getCurrentEntity;
	}
	public MethodDescriptor getSetCurrentEntityMethod() {
		return setCurrentEntity;
	}
	public MethodDescriptor getIsCurrentEntityVisibleMethod() {
		return isCurrentEntityVisible;
	}
	public MethodDescriptor setCurrentEntityVisibleMethod() {
		return setCurrentEntityVisible;
	}
	public MethodDescriptor getClearCurrentEntityMethod() {
		return clearCurrentEntity;
	}
	public MethodDescriptor getIsEntitySelectedMethod() {
		return isEntitySelected;
	}
	public MethodDescriptor getSetEntitySelectedMethod() {
		return setEntitySelected;
	}
	
	protected void buildDetailJavaPart(ClassDescriptor javaClass) {
		currentEntity = buildCurrentEntityAttribute(javaClass);
		getCurrentEntity = buildGetCurrentEntityMethod(javaClass);
		setCurrentEntity = buildSetCurrentEntityMethod(javaClass);
		selectEntity = buildSelectEntityMethod(javaClass);
		isCurrentEntityVisible = buildIsCurrentEntityVisible(javaClass);
		setCurrentEntityVisible = buildSetCurrentEntityVisible(javaClass);
		clearCurrentEntity = buildClearCurrentEntityMethod(javaClass);
		isEntitySelected = buildIsEntitySelectedMethod(javaClass);
		setEntitySelected = buildSetEntitySelectedMethod(javaClass);
		super.buildDetailJavaPart(javaClass);
	}

	protected AttributeDescriptor buildCurrentEntityAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor current = new AttributeDescriptor();
		current.init(javaClass, entityType, 
			new StandardNameMaker("current", null, entityType))
			.initToNull();
		javaClass.addAttribute(current);
		return current;
	}
	
	protected MethodDescriptor buildGetCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor getCurrent = 
			StandardMethods.getter(getCurrentEntityAttribute(), null);
		getCurrent.addModifier("public");
		javaClass.addMethod(getCurrent);
		return getCurrent;
	}

	protected MethodDescriptor buildSetCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor setCurrent = 
			StandardMethods.protectedSetter(getCurrentEntityAttribute(), null);
		javaClass.addMethod(setCurrent);
		return setCurrent;
	}

	PageDescriptor getPage() {
		return (PageDescriptor)getDocument();
	}
		
	protected MethodDescriptor buildSelectEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String paramName = NamingUtil.toMember(entityType.getTypeName());
		MethodDescriptor meth = new MethodDescriptor()		{
			public String[] getText() {
				if (getPage().getConfirmPopup()!=null) {
					replace("operAttr", getPage().getConfirmPopup().getOperation());
					replace("NONE_OP", getPage().getConfirmPopup().getConst("NONE"));
				}
				else {
					remove(" || operAttr != NONE_OP");
				}
				return super.getText();
			}
		};
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("select", null, entityType)); 
		meth.addParameter(entityType, paramName)
			.addModifier("public")
			.setContent(
				"if (currentEntity!=null || operAttr != NONE_OP)",
				"	return;",
				"setCurrentEntity(entity);")
			.replace("currentEntity", getCurrentEntityAttribute())
			.replace("setCurrentEntity", getSetCurrentEntityMethod())
			.replace("entity", paramName);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildIsCurrentEntityVisible(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean,
			new StandardNameMaker("is", "visible", currentEntity))
			.addModifier("public")
			.setContent(
				"return currentEntity != null;")
			.replace("currentEntity", currentEntity);
		javaClass.addMethod(meth);
		return meth;
	}
		
	protected MethodDescriptor buildSetCurrentEntityVisible(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid,
			new StandardNameMaker("set", "visible", currentEntity))
			.addParameter(TypeDescriptor.rBoolean, "visible")
			.addModifier("public");
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildClearCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid,
			new StandardNameMaker("clear", null, currentEntity));
		meth.setContent(
			"currentEntity = null;",
			"/// continue clearing here")
		.replace("currentEntity", currentEntity);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildIsEntitySelectedMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean,
			new StandardNameMaker("isSelected", getEntityType().getName(), null)).addModifier("public")
		.setContent(
			"EntityType entityInstance =(EntityType)ExpressionUtil.getValue(\"#{entityAlias}\");",
			"return entityInstance==currentEntity;")
		.replace("EntityType", getEntityType().getClassDescriptor())
		.replace("entityInstance", getEntityAlias())
		.replace("entityAlias", getEntityAlias())
		.replace("ExpressionUtil", Frmk.ExpressionUtil)
		.replace("currentEntity", currentEntity);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildSetEntitySelectedMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid,
			new StandardNameMaker("setSelected", getEntityType().getName(), null)).addModifier("public");
		meth.addParameter(getEntityType().getClassDescriptor(), getEntityAlias());
		javaClass.addMethod(meth);
		return meth;
	}

	public void requestMultipleSelection() {
		multipleSelection = true;
	}
	
	protected AttributeDescriptor buildSelectedAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor selectedAttr = 
			new AttributeDescriptor();
			selectedAttr.init(javaClass, TypeDescriptor.Map(
					getEntityType().getClassDescriptor(), TypeDescriptor.Boolean), "selected" );
		selectedAttr.initWithNew(TypeDescriptor.HashMap(getEntityType().getClassDescriptor(), TypeDescriptor.Boolean));
		javaClass.addAttribute(selectedAttr);
		return selectedAttr;
	}

	protected MethodDescriptor buildSelectedGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor selectorGetter = 
			StandardMethods.getter(selectedMapAttr, "public");
		javaClass.addMethod(selectorGetter);
		return selectorGetter;
	}
		
	public AttributeDescriptor getSelectedMapAttribute() {
		return selectedMapAttr;
	}
	
	public MethodDescriptor getSelectedMapGetterMethod() {
		return selectedMapGetter;
	}
	
	public AbstractSelectOneListDescriptor addDelete(String deleteMessage) {
		this.deleteMessage = deleteMessage;
		return this;
	}

	protected void initDelete(ClassDescriptor javaClass) {
		targetForDeletion = buildTargetForDeletionAttribute(javaClass);
		requestEntityForDeletion = buildRequestEntityForDeletionMethod(javaClass);
		getTargetForDeletion = buildGetTargetForDeletionMethod(javaClass);
		deleteEntity = buildDeleteEntityMethod(javaClass);
		declareDeleteOperation();
	}
	
	protected void declareDeleteOperation() {
		getPage().initConfirmPopup();
		String[] deleteCall = {
			"if (entity != null) {",
			"	delete(entity);",
			"	entity = null;",
			"}"
		};
		deleteCall = Rewrite.replace(deleteCall, "delete", deleteEntity.getName());
		deleteCall = Rewrite.replace(deleteCall, "entity", targetForDeletion.getName());
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String entityName = NamingUtil.toMember(entityType.getTypeName());
		String fragment = getDocument().getBeanName()+"."+FragmentDescriptor.getProperty(getTargetForDeletion);
		deleteMessage = Rewrite.replace(deleteMessage, entityName+"ToDelete", fragment);
		getPage().getConfirmPopup().addOperation("DELETE", deleteCall, deleteMessage);
	}
	
	public AttributeDescriptor getTargetForDeletionAttribute() {
		return targetForDeletion;
	}
	public MethodDescriptor getRequestEntityForDeletionMethod() {
		return requestEntityForDeletion;
	}
	public MethodDescriptor getTargetForDeletionGetterMethod() {
		return getTargetForDeletion;
	}
	public MethodDescriptor getDeleteEntityMethod() {
		return deleteEntity;
	}
	
	protected AttributeDescriptor buildTargetForDeletionAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor target = new AttributeDescriptor();
		target.init(javaClass, entityType,
			new StandardNameMaker("target", "ForDeletion", entityType)).initToNull();
		javaClass.addAttribute(target);
		return target;
	}
	
	protected MethodDescriptor buildGetTargetForDeletionMethod(ClassDescriptor javaClass) {
		MethodDescriptor getter = StandardMethods.getter(targetForDeletion, "public");
		javaClass.addMethod(getter);
		return getter;
	}
		
	protected MethodDescriptor buildRequestEntityForDeletionMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String paramName = NamingUtil.toMember(entityType.getTypeName());
		MethodDescriptor meth = new MethodDescriptor() {
			public String[] getText() {
				replace("operAttr", getPage().getConfirmPopup().getOperation());
				replace("DEL_OP", getPage().getConfirmPopup().getConst("DELETE"));
				return super.getText();
			}
		};
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("request", "Deletion", entityType));
		meth.addParameter(entityType, paramName)
			.addModifier("public")
			.setContent(
				"targetEntityForDeletion = param;",
				"operAttr = DEL_OP;"
			)
			.replace("targetEntityForDeletion",  targetForDeletion)
			.replace("param",  paramName);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildDeleteEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String paramName = NamingUtil.toMember(entityType.getTypeName());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("delete", null, entityType));
		meth.addParameter(entityType, paramName)
			.setContent(
				"removeEntityFromList(param);",
				"entityManager.remove(param);",
				"/// insert cleaning here"
			);
		meth.replace("entityManager", getDocument().getEntityManager())
			.replace("removeEntityFromList(param);", removeEntityFromList(paramName))
			.replace("param", paramName);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected abstract String[] removeEntityFromList(String entity);
	
	public AbstractSelectOneListDescriptor setDetailInPopup(String detailTitle) {
		super.setDetailInPopup(detailTitle);
		return this;
	}
	
	static final String[] defaultRowSelectorPattern = {
		"<ice:rowSelector selectionAction=\"#{beanName.selectEntity(line)}\" value=\"#{beanName.entitySelected}\" multiple=\"false\" />"
	};

	static final String[] defaultDeleteColumnPattern = {
		"<ice:column>",
		"	<h:commandButton value=\"del\" action=\"#{beanName.requestEntityDeletion(line)}\"/>",
		"</ice:column>",
	};

	static final String[] defaultMultipleSelectionPattern = {
		"<ice:column>",
		"	<h:selectBooleanCheckbox value=\"#{beanName.selectedMap[line]}\" />",
		"</ice:column>"
	};

}
