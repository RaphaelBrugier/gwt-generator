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
import com.objetdirect.seam.FieldRendererDescriptor;
import com.objetdirect.seam.NavigationPopupDescriptor;
import com.objetdirect.seam.PageDescriptor;

public abstract class AbstractSelectManyListDescriptor extends AbstractListDescriptor 
	implements SelectManyDetailHolder {

	String[] rowSelectorPattern = defaultRowSelectorPattern;
	String[] deletionColumnPattern = defaultDeleteColumnPattern;
	String[] commandsPattern = defaultCommandsPattern;
	
	AttributeDescriptor currentEntity;
	MethodDescriptor getCurrentEntity;
	MethodDescriptor setCurrentEntity;
	MethodDescriptor isCurrentEntityVisible;
	MethodDescriptor setCurrentEntityVisible;
	MethodDescriptor clearCurrentEntity;
	String deleteMessage;
	AttributeDescriptor targetForDeletion;
	MethodDescriptor requestEntityForDeletion;
	MethodDescriptor getTargetForDeletion;
	MethodDescriptor deleteEntity;

	public void buildJavaPart() {
		super.buildJavaPart();
		if (navigationPopup!=null)
			navigationPopup.buildJavaPart();
	}
	
	public void buildFaceletPart() {
		super.buildFaceletPart();
		if (navigationPopup!=null)
			navigationPopup.buildFaceletPart();
	}

	public AbstractSelectManyListDescriptor showField(FieldRendererDescriptor field) {
		super.showField(field);
		return this;	
	}

	public AbstractSelectManyListDescriptor showField(String fieldName, String fieldTitle, int length) {
		super.showField(fieldName, fieldTitle, length);
		return this;
	}

	public AbstractSelectManyListDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		super.showNumberField(fieldName, fieldTitle, pattern, length);
		return this;
	}

	public AbstractSelectManyListDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		super.showDateField(fieldName, fieldTitle, pattern);
		return this;
	}

	public AbstractSelectManyListDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		super.showBooleanField(fieldName, fieldTitle, trueValue, falseValue);
		return this;
	}

	public AbstractSelectManyListDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		super.showEnumField(fieldName, fieldTitle, length);
		return this;
	}

	public AbstractSelectManyListDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		super.showEntityField(fieldName, fieldTitle, labels, length);
		return this;
	}

	protected void manageSelection(FieldRendererDescriptor column, FragmentDescriptor columnFragment, boolean first) {
		if (first && getSelectedEntitiesGetterMethod()!=null) {
			FragmentDescriptor selectorFragment = new FragmentDescriptor(rowSelectorPattern);
			selectorFragment.replaceProperty("selectedEntities", getSelectedEntitiesGetterMethod());
			selectorFragment.replace("line", getEntityAlias());
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
		if (getDetail()!=null)
			deleteFragment.replaceProperty("currentEntityVisible", getIsCurrentEntityVisibleMethod());
		else
			deleteFragment.remove(" disabled=\"#{beanName.currentEntityVisible}\"");
		deleteFragment.replace("beanName", getDocument().getBeanName());
		deleteFragment.replace("line", getEntityAlias());
		deleteFragment.replaceMethod("requestEntityDeletion", requestEntityForDeletion);
		getFragment().insertFragment("/// insert columns here", deleteFragment);
		getFragment().replace("colWidth", 100+"px,colWidth");
	}
	
	NavigationPopupDescriptor navigationPopup;
	
	public NavigationPopupDescriptor getNavigationPopup() {
		return navigationPopup;
	}

	public AbstractSelectManyListDescriptor setDetail(SelectManyDetailDescriptor detail) {
		super.setDetail(detail);
		if (detail!=null) {
			navigationPopup = new NavigationPopupDescriptor();
			navigationPopup.setOwner(this);
		}
		return this;
	}

	public AbstractSelectManyListDescriptor setDetailInPopup(String detailTitle) {
		super.setDetailInPopup(detailTitle);
		return this;
	}	
	
	protected MethodDescriptor getIsDetailVisibleMethod() {
		return getIsCurrentEntityVisibleMethod();
	}

	protected MethodDescriptor getCloseDetailMethod() {
		return getDetail().getCancelCurrentEntityMethod();
	}

	protected SelectManyDetailDescriptor getDetail() {
		return (SelectManyDetailDescriptor)super.getDetail();
	}
	
	protected MethodDescriptor getEntityShownInDetailMethod() {
		return getGetCurrentEntityMethod();
	}
	
	public void buildJavaElements(ClassDescriptor javaClass) {
		super.buildJavaElements(javaClass);
		if (deleteMessage!=null)
			initDelete(javaClass);
	}

	public AttributeDescriptor getCurrentEntityAttribute() {
		return currentEntity;
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

	public MethodDescriptor getClearCurrentEntityMethod() {
		return clearCurrentEntity;
	}
	
	protected void buildDetailJavaPart(ClassDescriptor javaClass) {
		selectedEntities = buildSelectedEntitiesAttributes(javaClass);
		selectedEntitiesGetter = buildSelectedEntitiesGetterMethod(javaClass);
		openedEntities = buildOpenedEntitiesAttribute(javaClass);
		currentEntityIndex = buildCurrentEntityIndexAttribute(javaClass);
		currentEntity = buildCurrentEntityAttribute(javaClass);
		getCurrentEntity = buildGetCurrentEntityMethod(javaClass);
		setCurrentEntity = buildSetCurrentEntityMethod(javaClass);
		isCurrentEntityVisible = buildIsCurrentEntityVisible(javaClass);
		setCurrentEntityVisible = buildSetCurrentEntityVisible(javaClass);
		clearCurrentEntity = buildClearCurrentEntityMethod(javaClass);			
		modifyListGetter();
		super.buildDetailJavaPart(javaClass);
		openSelectedEntities = buildOpenSelectedEntitiesMethod(javaClass);
		openAllEntities = buildOpenAllEntitiesMethod(javaClass);
	}

	protected void modifyListGetter() {
		getListGetterMethod().replace("selectedEntities", getSelectedEntitiesAttribute());
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
		MethodDescriptor setCurrent = new MethodDescriptor();
		setCurrent.init(javaClass, TypeDescriptor.rVoid,
			new StandardNameMaker("set", null, currentEntity));
		String param = "selectedIndex";
		setCurrent.addParameter(TypeDescriptor.rInt, param);
		setCurrent.setContent(
			"if (openedEntities.size()==0)",
			"	return;",
			"currentEntity = openedEntities.get(index);",
			"/// insert post-processing here"
		);
		setCurrent.replace("currentEntity", currentEntity);
		setCurrent.replace("openedEntities", openedEntities);
		javaClass.addMethod(setCurrent);
		return setCurrent;
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
			.addModifier("public")
			.addParameter(TypeDescriptor.rBoolean, "visible")
			.setContent();
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildClearCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("clear", null, currentEntity));
		meth.setContent(
			"openedEntities = null;",
			"currentEntity = null;",
			"/// continue clearing here"
		);
		meth.replace("openedEntities", openedEntities);
		meth.replace("currentEntity", currentEntity);
		javaClass.addMethod(meth);
		return meth;
	}
	
	
	public AbstractSelectManyListDescriptor addDelete(String deleteMessage) {
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
	
	PageDescriptor getPage() {
		return (PageDescriptor)getDocument();
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
			new StandardNameMaker("request", "Deletion", entityType))
			.addParameter(entityType, paramName)
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
		MethodDescriptor meth = new MethodDescriptor() {
			public String[] getText() {
				if (selectedEntities!=null)
					replace("selectedEntities", selectedEntities);
				else
					removeLine("selectedEntities.remove");
				return super.getText();
			}
		};
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("delete", null, entityType));
		meth.addParameter(entityType, paramName)
			.setContent(
				"removeEntityFromList(param);",
				"entityManager.remove(param);",
				"selectedEntities.remove(param);",
				"/// insert cleaning here"
			);
		meth.replace("entityManager", getDocument().getEntityManager())
			.replace("removeEntityFromList(param);", removeEntityFromList(paramName))
			.replace("param", paramName);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected abstract String[] removeEntityFromList(String entity);
	
	AttributeDescriptor selectedEntities;
	
	public AttributeDescriptor getSelectedEntitiesAttribute() {
		return selectedEntities;
	}
	
	protected AttributeDescriptor buildSelectedEntitiesAttributes(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, 
			TypeDescriptor.Map(getEntityType().getClassDescriptor(), TypeDescriptor.Boolean), 
			new StandardNameMaker("selected", getListName(), null)).
			initWithNew(TypeDescriptor.HashMap(getEntityType().getClassDescriptor(), TypeDescriptor.Boolean));
		javaClass.addAttribute(attr);
		return attr;
	} 
	
	MethodDescriptor selectedEntitiesGetter;
	
	public MethodDescriptor getSelectedEntitiesGetterMethod() {
		return selectedEntitiesGetter;
	}
	
	protected MethodDescriptor buildSelectedEntitiesGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.getter(selectedEntities, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	
	AttributeDescriptor openedEntities; 
	
	public AttributeDescriptor getOpenedEntitiesAttribute() {
		return openedEntities;
	}

	protected AttributeDescriptor buildOpenedEntitiesAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.List(getEntityType().getClassDescriptor()),
				new StandardNameMaker("opened", getListName(), null)).
				initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
		
	AttributeDescriptor currentEntityIndex; 
	
	public AttributeDescriptor getCurrentEntityIndexAttribute() {
		return currentEntityIndex;
	}

	protected AttributeDescriptor buildCurrentEntityIndexAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
				new StandardNameMaker("current", "Index", openedEntities)).
				initWithPattern("0");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor openSelectedEntities;
	MethodDescriptor openAllEntities;
	
	public MethodDescriptor getOpenSelectedEntitiesMethod() {
		return openSelectedEntities;
	}
	public MethodDescriptor getOpenAllEntitiesMethod() {
		return openAllEntities;
	}
	
	protected MethodDescriptor buildOpenSelectedEntitiesMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("openSelected", getListName(), null));
		meth.addModifier("public");
		meth.setContent(
			"openedEntities = new ArrayList<EntityType>();",
			"for (EntityType entityInstance : getEntities()) {",
			"	if (selectedEntities.containsKey(entityInstance) && selectedEntities.get(entityInstance)) {",
			"		openedEntities.add(entityInstance);",
			"	}",
			"}",
			"openDetail();"
		);
		meth.replace("openedEntities", openedEntities);
		meth.replace("ArrayList", TypeDescriptor.ArrayList(null));
		meth.replace("EntityType", getEntityType().getClassDescriptor());
		meth.replace("getEntities", getListGetterMethod());
		meth.replace("selectedEntities", selectedEntities);
		meth.replace("entityInstance", getEntityAlias());
		meth.replace("openDetail", getDetail().getOpenDetailMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildOpenAllEntitiesMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("openAll", getListName(), null));
		meth.addModifier("public");
		meth.setContent(
			"openedEntities = new ArrayList<EntityType>(getEntities());",
			"openDetail();"
		);
		meth.replace("openedEntities", openedEntities);
		meth.replace("ArrayList", TypeDescriptor.ArrayList(null));
		meth.replace("EntityType", getEntityType().getClassDescriptor());
		meth.replace("getEntities", getListGetterMethod());
		meth.replace("openDetail", getDetail().getOpenDetailMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected void buildDetailPart() {
		FragmentDescriptor commands = new FragmentDescriptor(commandsPattern);
		commands.replace("beanName", getDocument().getBeanName());
		commands.replaceMethod("openSelectedEntities", openSelectedEntities);
		commands.replaceMethod("openAllEntities", openAllEntities);
		commands.replaceProperty("currentEntityVisible", isCurrentEntityVisible);
		getFragment().insertFragment("/// insert commands here", commands);
		super.buildDetailPart();
	}
	
	public MethodDescriptor getCancelAndNavigateMethod() {
		return getDetail().getCancelAndNavigateInsideEntitiesMethod();
	}

	public MethodDescriptor getCancelNavigationMethod() {
		return getDetail().getCancelNavigationMethod();
	}

	public MethodDescriptor getIsConfirmNavigationRequestedMethod() {
		return getDetail().getIsConfirmNavigationRequestedMethod();
	}

	public MethodDescriptor getSaveAndNavigateMethod() {
		return getDetail().getSaveAndNavigateInsideEntitiesMethod();
	}
	
	static final String[] defaultRowSelectorPattern = {
		"<ice:rowSelector value=\"#{beanName.selectedEntities[line]}\" multiple=\"true\" />"
	};

	static final String[] defaultDeleteColumnPattern = {
		"<ice:column>",
		"	<h:commandButton value=\"del\" action=\"#{beanName.requestEntityDeletion(line)}\" disabled=\"#{beanName.currentEntityVisible}\"/>",
		"</ice:column>"
	};

	static final String[] defaultCommandsPattern = {
		"<h:commandButton value=\"ouvrir\" action=\"#{beanName.openSelectedEntities}\" rendered=\"#{!beanName.currentEntityVisible}\"/>",
		"<h:commandButton value=\"ouvrir tout\" action=\"#{beanName.openAllEntities}\" rendered=\"#{!beanName.currentEntityVisible}\"/>",
	};
}
