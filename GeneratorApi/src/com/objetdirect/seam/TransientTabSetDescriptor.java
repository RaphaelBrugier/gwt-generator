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

package com.objetdirect.seam;

import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.PersistentListHolder;
import com.objetdirect.seam.lists.PersistentSelectOneListDescriptor;

public class TransientTabSetDescriptor
	extends BaseComponent
	implements DocumentFeature, 
		EntityHolder, FormHolder, PersistentListHolder, CriteriaHolder, NavigationPopupHolder {

	public TransientTabSetDescriptor(EntityDescriptor entity, String tabLabel) {
		this.entity = entity;
		this.tabLabel = tabLabel;
		setCriteria(new SimpleCriteriaDescriptor());
		layout = new LayoutDescriptor();
		layout.setOwner(this);
		layout.setSubmissionProtection();
		navigationPopup = new NavigationPopupDescriptor();
		navigationPopup.setOwner(this);
	}
	
	String tabLabel;
	EntityDescriptor entity;
	CriteriaDescriptor criteria;
	ScriptDescriptor creationScript;
	LayoutDescriptor layout;
	NavigationPopupDescriptor navigationPopup;
	
	String[] tabSetPattern = defaultTabSetPattern;
	String[] creationButtonPattern = defaultCreationButtonPattern;
	String[] deletionButtonPattern = defaultDeletionButtonPattern;
	
	public LayoutDescriptor getLayout() {
		return layout;
	}

	public TransientTabSetDescriptor addForm(FormDescriptor form) {
		layout.addForm(form);
		return this;
	}

	public TransientTabSetDescriptor addPersistentList(PersistentSelectOneListDescriptor persistentList) {
		layout.addPersistentList(persistentList);
		return this;
	}
	
	public TransientTabSetDescriptor setCriteria(CriteriaDescriptor criteria) {
		this.criteria = criteria;
		this.criteria.setOwner(this);
		return this;
	}
	
	public TransientTabSetDescriptor addCreate(ScriptDescriptor creationScript) {
		this.creationScript = creationScript;
		return this;
	}

	String deleteMessage;
	
	public TransientTabSetDescriptor addDelete(String deleteMessage) {
		this.deleteMessage = deleteMessage;
		return this;
	}
	
	public EntityDescriptor getEntityType() {
		return entity;
	}
	
	public void buildFaceletPart() {
		setFragment(new FragmentDescriptor(tabSetPattern));
		getFragment().replace(
			"tabLabel", tabLabel,
			"lineEntity", getEntityAlias());
		getFragment().replaceProperty("entities", listGetter);
		getFragment().replaceProperty("lineIndex", currentEntityIndexGetter);
		getFragment().replaceMethod("validateCurrentEntity", validateCurrentEntity);
		getFragment().replaceMethod("cancelCurrentEntity", cancelCurrentEntity);
		if (creationScript!=null) {
			getFragment().insertLines("/// insert commands here", creationButtonPattern);
			getFragment().replaceMethod("createEntity", createEntity);
		}
		if (deleteMessage!=null) {
			getFragment().insertLines("/// insert commands here", deletionButtonPattern);
			getFragment().replaceMethod("requestCurrentEntityDeletion", requestCurrentEntityDeletion);
		}
		getFragment().replaceProperty("entityListVisible", isEntityListVisible);
		if (criteria!=null) {
			criteria.buildFaceletPart();
			fragment.insertFragment("/// criteria content here", criteria.getFragment());
		}
		layout.buildFaceletPart();
		getFragment().insertFragment("/// detail content here", layout.getFragment());
		getFragment().removeLine("/// detail content here");
		getFragment().replace("beanName", getDocument().getBeanName());
		navigationPopup.buildFaceletPart();
	}

	public void buildJavaPart() {
		ClassDescriptor javaClass = getClassDescriptor();
		getDocument().declareEntityManager();
		navigationNONE = buildNavigationNONEAttribute(javaClass);
		if (creationScript!=null)
			navigationCREATE = buildNavigationCREATEAttribute(javaClass);
		if (criteria instanceof TransientCriteriaDescriptor)
			navigationAPPLYCRITERIA = buildNavigationAPPLYCRITERIAAttribute(javaClass);
		navigationIndex = buildNavigationIndexAttribute(javaClass);
		listAttr =  buildListAttribute(javaClass);
		currentEntity = buildCurrentEntityAttribute(javaClass);
		currentEntityIndex = buildCurrentEntityIndexAttribute(javaClass);
		currentEntityIndexSetter = buildCurrentEntityIndexSetterMethod(javaClass);
		listGetter = buildListGetterMethod(javaClass);
		isEntityListVisible = buildIsEntityListVisibleMethod(javaClass);
		currentEntityIndexGetter = buildCurrentEntityIndexGetterMethod(javaClass);
		currentEntityGetter = buildCurrentEntityGetterMethod(javaClass);
		currentEntitySetter = buildCurrentEntitySetterMethod(javaClass);
		isEntityValid = buildIsEntityValidMethod(javaClass);
		changeCurrentEntityIndex = buildChangeCurrentEntityIndexMethod(javaClass);
		if (creationScript!=null) {
			isNewEntity = buildIsNewEntityAttribute(javaClass);
		}
		isEntityDirty = buildIsEntityDirtyMethod(javaClass);
		if (creationScript!=null) {
			buildEntity = buildBuildEntityMethod(javaClass);
			doCreateEntity = buildDoCreateEntityMethod(javaClass);
			createEntity = buildCreateEntityMethod(javaClass);
		}
		validateCurrentEntity = buildValidateCurrentEntityMethod(javaClass);
		if (deleteMessage!=null) {
			getPage().initConfirmPopup();
			deleteCurrentEntity = buildDeleteCurrentEntityMethod(javaClass);
			requestCurrentEntityDeletion = buildRequestCurrentEntityDeletionMethod(javaClass);
			declareDeleteOperation();
		}
		isConfirmNavigationRequested = buildIsConfirmNavigationRequestedMethod(javaClass);
		cancelNavigation = buildCancelNavigationMethod(javaClass);
		criteria.buildJavaPart();
		if (criteria instanceof TransientCriteriaDescriptor) {
			doValidateCriteria = buildDoValidateCriteriaMethod(javaClass);
		}
		justNavigate = buildJustNavigateMethod(javaClass);
		saveAndNavigate = buildSaveAndNavigateMethod(javaClass);
		cancelCurrentEntity = buildCancelCurrentEntityMethod(javaClass);
		navigate = buildNavigateMethod(javaClass);
		layout.buildJavaPart();
		completeValidation();
		navigationPopup.buildJavaPart();
	}

	PageDescriptor getPage() {
		return (PageDescriptor)getDocument();
	}
	protected void declareDeleteOperation() {
		((PageDescriptor)getDocument()).initConfirmPopup();
		String[] deleteCall = {
			"deleteCurrentEntity();"
		};
		deleteCall = Rewrite.replace(deleteCall, "deleteCurrentEntity", deleteCurrentEntity.getName());
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String entityName = NamingUtil.toMember(entityType.getTypeName());
		String fragment = getDocument().getBeanName()+"."+FragmentDescriptor.getProperty(currentEntityGetter);
		deleteMessage = Rewrite.replace(deleteMessage, entityName+"ToDelete", fragment);
		getPage().getConfirmPopup().addOperation("DELETE", deleteCall, deleteMessage);
	}
	
	public String getEntityAlias() {
		return NamingUtil.toMember(getEntityType().getName());
	}

	public String getPostProcessingTag() {
		return "//// insert post-processing here";
	}
	
	public String getClearingTag() {
		return "/// continue clearing here";
	}

	public MethodDescriptor getClearEntityMethod() {
		return null;
	}

	public AttributeDescriptor getEntityAttribute() {
		return getCurrentEntityAttribute();
	}

	public MethodDescriptor getSetEntityMethod() {
		return getCurrentEntitySetterMethod();
	}
	
	public MethodDescriptor getFillFormMethod() {
		return currentEntitySetter;
	}

	public String getFillFormTag() {
		return "/// insert post-processing here";
	}

	public MethodDescriptor getInitFormMethod() {
		return null;
	}

	public String getInitFormTag() {
		return null;
	}

	public String getNamePrefix() {
		return getCurrentEntityAttribute().getName();
	}

	public String getNameSuffix() {
		return null;
	}

	public MethodDescriptor[] getSubmitFormMethods() {
		return new MethodDescriptor[] {getValidateCurrentEntityMethod()};
	}

	public String getSubmitFormTag() {
		return "/// insert form submission";
	}

	public MethodDescriptor getValidateFormMethod() {
		return getIsEntityValidMethod();
	}

	public String getValidateFormTag() {
		return "/// insert validations here";
	}

	public String getInitTarget() {
		return getCurrentEntityAttribute().getName();
	}
	
	public String getSubmitTarget() {
		return getCurrentEntityAttribute().getName();
	}
	
	public String getValidateTarget() {
		return getCurrentEntityAttribute().getName();
	}
	
	AttributeDescriptor listAttr;

	protected AttributeDescriptor buildListAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.List(getEntityType().getClassDescriptor()), 
			new StandardNameMaker(null, NamingUtil.toPlural(getEntityType().getName()), null)).initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
	
	public AttributeDescriptor getListAttribute() {
		return listAttr;
	}

	MethodDescriptor listGetter;
	
	public MethodDescriptor getListGetterMethod() {
		return listGetter;
	}

	protected MethodDescriptor buildListGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, listAttr.getType(), 
			new StandardNameMaker("get", null, listAttr));
		meth.addAnnotation(TypeDescriptor.SuppressWarnings, "\"unchecked\"");
		meth.addModifier("public");
		meth.setContent(
	 		"if (listAttr==null) {",
			"	listAttr = initialization;",
			"	if (listAttr.size()==0) {",
			"		currentEntityIndex=NONE;",
			"		currentEntity = null;",
			"	}",
			"	else",
			"		setCurrentEntityIndex(0);",
			"}",
			"return listAttr;"
		);
		meth.replace("listAttr", listAttr);
		meth.replace("NONE", navigationNONE.getName());
		meth.replace("currentEntityIndex", currentEntityIndex);
		meth.replace("currentEntity", currentEntity);
		meth.replace("setCurrentEntityIndex", currentEntityIndexSetter);
		meth.replace("initialization", criteria.getInitText());
		javaClass.addMethod(meth);
		return meth;
	}
	
	AttributeDescriptor currentEntityIndex;
	
	public AttributeDescriptor getCurrentEntityIndex() {
		return currentEntityIndex;
	}
	
	protected AttributeDescriptor buildCurrentEntityIndexAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.rInt, 
			new StandardNameMaker(null, "Index", currentEntity)).initWithPattern("-1");
		javaClass.addAttribute(attr);
		return attr;
	}

	AttributeDescriptor currentEntity;
	
	public AttributeDescriptor getCurrentEntityAttribute() {
		return currentEntity;
	}

	protected AttributeDescriptor buildCurrentEntityAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, entityType, 
			new StandardNameMaker("current", null, entityType))
			.initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor isEntityListVisible;
	
	public MethodDescriptor getIsEntityListVisibleMethod() {
		return isEntityListVisible;
	}
	
	protected MethodDescriptor buildIsEntityListVisibleMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("is", "ListVisible", getEntityType().getClassDescriptor()));
		meth.addModifier("public");
		meth.setContent(
	 		"return getEntities().size()>0;"
		);
		meth.replace("getEntities", listGetter);
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor currentEntityIndexGetter;
	
	public MethodDescriptor getCurrentEntityIndexGetterMethod() {
		return currentEntityIndexGetter;
	}

	protected MethodDescriptor buildCurrentEntityIndexGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.getter(currentEntityIndex, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor currentEntityGetter;
	
	public MethodDescriptor getCurrentEntityGetterMethod() {
		return currentEntityGetter;
	}
	
	protected MethodDescriptor buildCurrentEntityGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = 
			StandardMethods.getter(getCurrentEntityAttribute(), null);
		meth.addModifier("public");
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor currentEntitySetter;
	
	public MethodDescriptor getCurrentEntitySetterMethod() {
		return currentEntitySetter;
	}
	
	protected MethodDescriptor buildCurrentEntitySetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = 
			StandardMethods.protectedSetter(getCurrentEntityAttribute(), null);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor changeCurrentEntityIndex;
	
	public MethodDescriptor getChangeCurrentEntityIndexMethod() {
		return changeCurrentEntityIndex;
	}

	protected MethodDescriptor buildChangeCurrentEntityIndexMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("change", null, currentEntityIndex));
		meth.addParameter(currentEntityIndex.getType(), currentEntityIndex.getName());
		meth.setContent(
			"this.currentEntityIndex = currentEntityIndex;",
			"if (this.currentEntityIndex>=entities.size())",
			"	this.currentEntityIndex = entities.size()-1;",
			"if (this.currentEntityIndex>=0)",
			"	setCurrentEntity(entities.get(this.currentEntityIndex));"
		);
		meth.replace("currentEntityIndex", currentEntityIndex);
		meth.replace("entities", listAttr);
		meth.replace("setCurrentEntity", currentEntitySetter);
		javaClass.addMethod(meth);
		return meth;
	}

	AttributeDescriptor isNewEntity = null;
	
	public AttributeDescriptor getIsNewEntityAttribute() {
		return isNewEntity;
	}
	
	protected AttributeDescriptor buildIsNewEntityAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isNew", getEntityType().getName(), null)).initWithPattern("false");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor buildEntity = null;
	
	public MethodDescriptor getBuildEntityMethod() {
		return buildEntity;
	}
	
	protected MethodDescriptor buildBuildEntityMethod(ClassDescriptor javaClass) {
		ClassDescriptor entityType = getEntityType().getClassDescriptor();
		creationScript.setOwner(getClassDescriptor());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, entityType, 
				new StandardNameMaker("build", null, entityType))
			.addModifier("protected")
			.setContent(creationScript.getText());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor doCreateEntity = null;
	
	public MethodDescriptor getDoCreateEntityMethod() {
		return doCreateEntity;
	}
	
	protected MethodDescriptor buildDoCreateEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("doCreate", getEntityType().getName(), null));
		meth.setContent(
			"EntityClass entityInstance = buildEntity();",
			"if (currentEntityIndex==entities.size()-1) {",
			"	entities.add(entityInstance);",
			"	currentEntityIndex = entities.size()-1;",
			"}",
			"else {",
			"	currentEntityIndex++;",
			"	entities.add(currentEntityIndex, entityInstance);",
			"}",
			"setCurrentEntity(entityInstance);",
			"isNewEntity = true;"
		);
		meth.replace("EntityClass", getEntityType().getClassDescriptor());
		meth.replace("entityInstance", getEntityType().getName());
		meth.replace("buildEntity", buildEntity);
		meth.replace("currentEntityIndex", currentEntityIndex);
		meth.replace("entities", listAttr);
		meth.replace("setCurrentEntity", currentEntitySetter);
		meth.replace("isNewEntity", isNewEntity);
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor createEntity = null;
	
	public MethodDescriptor getCreateEntityMethod() {
		return createEntity;
	}
	
	protected MethodDescriptor buildCreateEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("create", getEntityType().getName(), null));
		meth.addModifier("public");
		meth.setContent(
			"if (isEntityDirty())",
			"	navigationIndex = CREATE;",
			"else",
			"	createNewEntity();"
		);
		meth.replace("isEntityDirty", isEntityDirty);
		meth.replace("CREATE", navigationCREATE);
		meth.replace("navigationIndex", navigationIndex);
		meth.replace("createNewEntity", doCreateEntity);
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor validateCurrentEntity = null;
	
	public MethodDescriptor getValidateCurrentEntityMethod() {
		return validateCurrentEntity;
	}
	
	protected MethodDescriptor buildValidateCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
				new StandardNameMaker("validate", null, currentEntity))
			.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"	/// persist if new",
			"	return true;",
			"}",
			"else",
			"	return false;"
		);
		if (creationScript!=null) {
			meth.insertLines("/// persist if new", 
				"if (isNewEntity) {",
				"	entityManager.persist(currentEntity);",
				"	isNewEntity = false;",
				"}"
			);
			meth.replace("entityManager", getDocument().getEntityManager());
			meth.replace("isNewEntity", isNewEntity);
			meth.replace("currentEntity", currentEntity);
		}
		meth.replace("isEntityValid", isEntityValid);
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor isEntityValid;
	
	public MethodDescriptor getIsEntityValidMethod() {
		return isEntityValid;
	}
	
	protected MethodDescriptor buildIsEntityValidMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String paramName = NamingUtil.toMember(entityType.getTypeName());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("is", "valid", currentEntity));
		meth.setContent(
				"/// insert validations here",
				"return !FacesContext.getCurrentInstance().getMessages().hasNext();")
			.replace("FacesContext", JSF.FacesContext)
			.replace("entity", paramName);
		javaClass.addMethod(meth);
		return meth;
	}

	protected void completeValidation() {
		List<FieldRendererDescriptor> fields = layout.getAllFields(); 
		if (fields.size()>0) {
			ConstraintManager constraintMngr = new ConstraintManager(
				getCurrentEntityAttribute(), fields);
			String[] text = constraintMngr.getText();
			if (text.length>0) {
				isEntityValid.insertLines("/// insert validations here", "String message;");
				isEntityValid.insertLines("/// insert validations here", text);
			}
		}
	}
	
	MethodDescriptor cancelCurrentEntity;
	
	public MethodDescriptor getCancelCurrentEntity() {
		return cancelCurrentEntity;
	}
	
	protected MethodDescriptor buildCancelCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("cancel", null, currentEntity));
		meth.setContent(
				"GuiUtil.cancelGui();",
				"/// remove if new",
				"changeCurrentEntityIndex(currentEntityIndex);");
		meth.addModifier("public");
		meth.replace("GuiUtil", Frmk.GuiUtil);
		meth.replace("changeCurrentEntityIndex", changeCurrentEntityIndex);
		meth.replace("currentEntityIndex", currentEntityIndex);
		if (creationScript!=null) {
			meth.insertLines("/// remove if new", 
				"if (isNewEntity) {",
				"	entities.remove(currentEntity);",
				"	isNewEntity = false;",
				"}"
			);
			meth.replace("isNewEntity", isNewEntity);
			meth.replace("entities", listAttr);
			meth.replace("currentEntity", currentEntity);
		}
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor deleteCurrentEntity;
	
	public MethodDescriptor getDeleteCurrentEntityMethod() {
		return deleteCurrentEntity;
	}
	
	protected MethodDescriptor buildDeleteCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("delete", null, currentEntity));
		meth.setContent(
			"/// process deletion",
			"entities.remove(currentEntity);",
			"changeCurrentEntityIndex(currentEntityIndex);"
		);
		meth.replace("entities", listAttr);
		meth.replace("changeCurrentEntityIndex", changeCurrentEntityIndex);
		meth.replace("currentEntityIndex", currentEntityIndex);
		if (creationScript!=null) {
			meth.insertLines("/// process deletion", 
				"if (!isNewEntity)",
				"	entityManager.remove(currentEntity);",
				"else",
				"	isNewEntity = false;"
			);
			meth.replace("isNewEntity", isNewEntity);
		}
		else {
			meth.insertLines("/// process deletion", 
				"entityManager.remove(currentEntity);"
			);
		}
		meth.replace("currentEntity", currentEntity);
		meth.replace("entityManager", getDocument().getEntityManager());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor requestCurrentEntityDeletion;
	
	public MethodDescriptor getRequestForDeletion() {
		return requestCurrentEntityDeletion;
	}
	
	protected MethodDescriptor buildRequestCurrentEntityDeletionMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor() {
			public String[] getText() {
				replace("operAttr", getPage().getConfirmPopup().getOperation());
				replace("DEL_OP", getPage().getConfirmPopup().getConst("DELETE"));
				return super.getText();
			}
		};
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("request", "Deletion", currentEntity))
			.addModifier("public")
			.setContent(
				"operAttr = DEL_OP;"
			);
		javaClass.addMethod(meth);
		return meth;
	}
	
	AttributeDescriptor navigationNONE;
	AttributeDescriptor navigationCREATE;
	AttributeDescriptor navigationAPPLYCRITERIA;

	public AttributeDescriptor getNavigationNONEAttribute() {
		return navigationNONE;
	}
	public AttributeDescriptor getNavigationCREATEAttribute() {
		return navigationCREATE;
	}
	public AttributeDescriptor getNavigationAPPLYCRITERIAAttribute() {
		return navigationAPPLYCRITERIA;
	}

	public AttributeDescriptor buildNavigationNONEAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.rInt, 
			new StandardNameMaker("NAV_NONE", null, null)).initWithPattern("-1");
		attr.addModifier("static final");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	public AttributeDescriptor buildNavigationCREATEAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.rInt, 
			new StandardNameMaker("NAV_CREATE", null, null)).initWithPattern("-2");
		attr.addModifier("static final");
		javaClass.addAttribute(attr);
		return attr;
	}

	public AttributeDescriptor buildNavigationAPPLYCRITERIAAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.rInt, 
			new StandardNameMaker("NAV_APPLY_CRITERIA", null, null)).initWithPattern("-3");
		attr.addModifier("static final");
		javaClass.addAttribute(attr);
		return attr;
	}

	AttributeDescriptor navigationIndex;
	
	public AttributeDescriptor getNavigationIndexAttribute() {
		return navigationIndex;
	}
	
	public AttributeDescriptor buildNavigationIndexAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.rInt, 
			new StandardNameMaker("navigationIndex", null, null)).initWithPattern(navigationNONE.getName());
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor isConfirmNavigationRequested;
	
	public MethodDescriptor getIsConfirmNavigationRequestedMethod() {
		return isConfirmNavigationRequested;
	}
	
	protected MethodDescriptor buildIsConfirmNavigationRequestedMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isConfirmNavigationRequested", null, null));
		meth.addModifier("public");
		meth.setContent(
			"return navigationIndex!=NONE;"
		);
		meth.replace("navigationIndex", navigationIndex);
		meth.replace("NONE", navigationNONE.getName());
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor cancelNavigation;
	
	public MethodDescriptor getCancelNavigationMethod() {
		return cancelNavigation;
	}
	
	protected MethodDescriptor buildCancelNavigationMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("cancelNavigation", null, null));
		meth.addModifier("public");
		meth.setContent(
			"navigationIndex=NONE;"
		);
		meth.replace("navigationIndex", navigationIndex);
		meth.replace("NONE", navigationNONE.getName());
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor doValidateCriteria;
	
	public MethodDescriptor getDoValidateCriteriaMethod() {
		return doValidateCriteria;
	}
	
	protected MethodDescriptor buildDoValidateCriteriaMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("doValidateCriteria", null, null));
		MethodDescriptor validate = ((TransientCriteriaDescriptor)criteria).getValidateCriteriaMethod();
		meth.setContent(validate);
		validate.setContent(
			"if (isEntityDirty())",
			"	navigationIndex = APPLYCRITERIA;",
			"else",
			"	doValidateCriteria();"
		);
		validate.replace("isEntityDirty", isEntityDirty);
		validate.replace("navigationIndex", navigationIndex);
		validate.replace("APPLYCRITERIA", navigationAPPLYCRITERIA);
		validate.replace("doValidateCriteria", meth);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor justNavigate;
	
	public MethodDescriptor getJustNavigateMethod() {
		return justNavigate;
	}
	
	protected MethodDescriptor buildJustNavigateMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("justNavigate", null, null));
		if (doCreateEntity!=null || doValidateCriteria!=null) {
			meth.setContent(
				"/// insert continue operation here",
				"else",
				"	setCurrentEntityIndex(navigationIndex);",
				"navigationIndex = NONE;"
			);
			if (doCreateEntity!=null) {
				meth.insertLines("/// insert continue operation here", 
					"if (navigationIndex==CREATE)",
					"	doCreateEntity();"
				);
				meth.replace("doCreateEntity", doCreateEntity);
				meth.replace("CREATE", navigationCREATE);
			}
			if (doValidateCriteria!=null) {
				meth.insertLines("/// insert continue operation here", 
					"if (navigationIndex==APPLYCRITERIA)",
					"	doValidateCriteria();"
				);
				meth.replace("doValidateCriteria", doValidateCriteria);
				meth.replace("APPLYCRITERIA", navigationAPPLYCRITERIA);
			}
			meth.removeLine("/// insert continue operation here");
		}
		else {
			meth.setContent(
				"setCurrentEntityIndex(navigationIndex);",
				"navigationIndex = NONE;"
			);
		}
		meth.replace("setCurrentEntityIndex", currentEntityIndexSetter);
		meth.replace("navigationIndex", navigationIndex);
		meth.replace("NONE", navigationNONE.getName());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor saveAndNavigate;
	
	public MethodDescriptor getSaveAndNavigateMethod() {
		return saveAndNavigate;
	}
	
	protected MethodDescriptor buildSaveAndNavigateMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("saveAndNavigate", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (validateCurrentEntity())",
			"	justNavigate();"
		);
		meth.replace("validateCurrentEntity", validateCurrentEntity);
		meth.replace("justNavigate", justNavigate);
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor navigate;
	
	public MethodDescriptor getNavigateMethod() {
		return navigate;
	}
	
	protected MethodDescriptor buildNavigateMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("navigate", null, null));
		meth.addModifier("public");
		meth.setContent(
			"cancelCurrentEntity();",
			"justNavigate();"
		);
		meth.replace("cancelCurrentEntity", cancelCurrentEntity);
		meth.replace("justNavigate", justNavigate);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor isEntityDirty;
	
	public MethodDescriptor getIsEntityDirtyMethod() {
		return isEntityDirty;
	}
	
	public MethodDescriptor[] getCheckDirtyMethods() {
		return new MethodDescriptor[] {isEntityDirty};
	}

	public String getCheckDirtyTag() {
		return "/// insert dirty checking here";
	}
	
	protected MethodDescriptor buildIsEntityDirtyMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("is", "Dirty", currentEntity));
		meth.setContent(
			"/// check new entity here",
			"if (currentEntity==null)",
			"	return false;",
			"/// insert dirty checking here",
			"return false;"
		);
		if (creationScript!=null) {
			meth.insertLines("/// check new entity here", 
				"if(isNewEntity)",
				"	return true;"
			);
			meth.replace("isNewEntity", isNewEntity);
		}
		meth.replace("currentEntity", currentEntity);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor currentEntityIndexSetter;
	
	public MethodDescriptor getCurrentEntityIndexSetter() {
		return currentEntityIndexSetter;
	}

	protected MethodDescriptor buildCurrentEntityIndexSetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor() 
		{
			public String[] getText() {
				setContent(
					"if (isEntityDirty()) {",
					"	navigationIndex = currentEntityIndex;",
					"}",
					"else {",
					"	GuiUtil.invalidateSubmit();",
					"	changeCurrentEntityIndex(currentEntityIndex);",
					"}"
				);
				replace("isEntityDirty", isEntityDirty);
				replace("navigationIndex", navigationIndex);
				replace("currentEntityIndex", currentEntityIndex);
				replace("GuiUtil", Frmk.GuiUtil);
				replace("changeCurrentEntityIndex", changeCurrentEntityIndex);
				return super.getText();
			}
		};
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("set", null, currentEntityIndex));
		meth.addModifier("public");
		meth.addParameter(currentEntityIndex.getType(), currentEntityIndex.getName());
		javaClass.addMethod(meth);
		return meth;
	}

	static final String[] defaultTabSetPattern = new String[] {
		"<h:panelGroup>",
		"	/// criteria content here",
		"	<ice:panelTabSet var=\"lineEntity\" value=\"#{beanName.entities}\" selectedIndex=\"#{beanName.lineIndex}\"",
		"		tabPlacement=\"top\" rendered=\"#{beanName.entityListVisible}\">",
		"		<ice:panelTab label=\"tabLabel\" >",
		"			/// detail content here",
		"		</ice:panelTab>",
		"	</ice:panelTabSet>",
		"	<h:panelGroup>",
		"		<h:commandButton value=\"Valider\" action=\"#{beanName.validateCurrentEntity}\" rendered=\"#{beanName.entityListVisible}\"/>",
		"		<h:commandButton value=\"Annuler\" immediate=\"true\" action=\"#{beanName.cancelCurrentEntity}\" rendered=\"#{beanName.entityListVisible}\"/>",
		"		/// insert commands here",
		"	</h:panelGroup>",
		"</h:panelGroup>"
	};
	
	static final String[] defaultCreationButtonPattern = new String[] {
		"<h:commandButton value=\"Creer\" action=\"#{beanName.createEntity}\"/>"
	};
	
	static final String[] defaultDeletionButtonPattern = new String[] {
		"<h:commandButton value=\"Supprimer\" action=\"#{beanName.requestCurrentEntityDeletion}\" rendered=\"#{beanName.entityListVisible}\"/>",
	};

	public MethodDescriptor getCancelAndNavigateMethod() {
		return getNavigateMethod();
	}

}
