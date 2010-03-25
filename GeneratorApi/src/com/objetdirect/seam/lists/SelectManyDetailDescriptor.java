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
import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.Component;
import com.objetdirect.seam.DetailDescriptor;
import com.objetdirect.seam.DetailHolder;
import com.objetdirect.seam.FormDescriptor;
import com.objetdirect.seam.Frmk;

public class SelectManyDetailDescriptor extends DetailDescriptor {
	
	public SelectManyDetailDescriptor() {
	}
	ScriptDescriptor creationScript = null;
	MethodDescriptor cancelCurrentEntity;
	MethodDescriptor validateCurrentEntity;

	AttributeDescriptor isNewEntity;
	AttributeDescriptor isRecentEntity;

	MethodDescriptor createEntity;
	MethodDescriptor buildEntity;
	
	public ScriptDescriptor getCreationScript() {
		return creationScript;
	}

	public MethodDescriptor getCancelCurrentEntityMethod() {
		return cancelCurrentEntity;
	}
	
	public MethodDescriptor getValidateCurrentEntityMethod() {
		return validateCurrentEntity;
	}
	
	public AttributeDescriptor getIsNewEntityAttribute() {
		return isNewEntity;
	}
	
	public AttributeDescriptor getIsRecentEntityAttribute() {
		return isRecentEntity;
	}

	public MethodDescriptor getCreateEntityMethod() {
		return createEntity;
	}
	
	public MethodDescriptor getBuildEntityMethod() {
		return buildEntity;
	}
	
	protected void buildJavaElements(ClassDescriptor javaClass) {
		super.buildJavaElements(javaClass);
		cancelCurrentEntity = buildCancelCurrentEntityMethod(javaClass);
		validateCurrentEntity = buildValidateCurrentEntityMethod(javaClass);
		isEntityDirty = buildIsEntityDirtyMethod(javaClass);
		fastStep = buildFastStepAttribute(javaClass);
		entitiesNoNavigationConst = buildEntitiesNoNavigationConstAttribute(javaClass);
		entitiesGoFirstConst = buildEntitiesGoFirstConstAttribute(javaClass);
		entitiesGoFastPreviousConst = buildEntitiesGoFastPreviousConstAttribute(javaClass);
		entitiesGoPreviousConst = buildEntitiesGoPreviousConstAttribute(javaClass);
		entitiesGoNextConst = buildEntitiesGoNextConstAttribute(javaClass);
		entitiesGoFastNextConst = buildEntitiesGoFastNextConstAttribute(javaClass);
		entitiesGoLastConst = buildEntitiesGoLastConstAttribute(javaClass);
		closeEntitiesPopupConst = buildCloseEntitiesPopupConstAttribute(javaClass);
		entitiesNavigation = buildEntitiesNavigationAttribute(javaClass);
		isConfirmNavigationRequested = buildIsConfirmNavigationRequestedMethod(javaClass);
		cancelNavigation = buildCancelNavigationMethod(javaClass);		
		doEntitiesGoFirst = buildDoEntitiesGoFirstMethod(javaClass);
		entitiesGoFirst = buildEntitiesGoFirstMethod(javaClass);
		doEntitiesGoFastPrevious = buildDoEntitiesGoFastPreviousMethod(javaClass);
		entitiesGoFastPrevious = buildEntitiesGoFastPreviousMethod(javaClass);
		doEntitiesGoPrevious = buildDoEntitiesGoPreviousMethod(javaClass);
		entitiesGoPrevious = buildEntitiesGoPreviousMethod(javaClass);
		doEntitiesGoNext = buildDoEntitiesGoNextMethod(javaClass);
		entitiesGoNext = buildEntitiesGoNextMethod(javaClass);
		doEntitiesGoFastNext = buildDoEntitiesGoFastNextMethod(javaClass);
		entitiesGoFastNext = buildEntitiesGoFastNextMethod(javaClass);
		doEntitiesGoLast = buildDoEntitiesGoLastMethod(javaClass);
		entitiesGoLast = buildEntitiesGoLastMethod(javaClass);
		
		if (creationScript!=null) {
			isNewEntity = buildIsNewEntityAttribute(javaClass);
			if (isComplex())
				isRecentEntity = buildIsRecentEntityAttribute(javaClass);
			buildEntity = buildBuildEntityMethod(javaClass);
			createEntity = buildCreateEntityMethod(javaClass);
			createEntityConst = buildCreateEntityConstAttribute(javaClass);
			doInsertEntity = buildDoInsertEntityMethod(javaClass);
			insertEntity = buildInsertEntityMethod(javaClass);
			isCreateEntityRendered =  buildIsCreateEntityRenderedMethod(javaClass);
			modifyValidateCurrentEntityMethod();
			modifyClearEntityMethod();
			modifyCancelCurrentEntityMethod();
		}
		modifyListMethods();
		
		closeCurrentEntity = buildCloseCurrentEntityMethod(javaClass);
		navigateInsideEntities = buildNavigateInsideEntitiesMethod(javaClass);
		saveAndNavigateInsideEntities = buildSaveAndNavigateInsideEntitiesMethod(javaClass);
		cancelAndNavigateInsideEntities = buildCancelAndNavigateInsideEntitiesMethod(javaClass);		
	}

	protected void modifyListMethods() {
		DetailHolder parent = getParent(DetailHolder.class);
		MethodDescriptor deleteEntity = parent.getDeleteEntityMethod();
		if (deleteEntity!=null) {
			String[] pattern = {
				"if (paramName==currentEntity)",
				"	currentEntity = null;"
			};
			pattern = Rewrite.replace(pattern, "currentEntity", parent.getCurrentEntityAttribute().getName());
			pattern = Rewrite.replace(pattern, "paramName", deleteEntity.getParameterName(0));
			if (deleteEntity!=null) {
				deleteEntity.insertLines("/// insert cleaning here", pattern);
			}
		}
	}
	
	protected AttributeDescriptor buildIsNewEntityAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor current = new AttributeDescriptor();
		current.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isNew", null, entityType))
			.initWithPattern("false");
		javaClass.addAttribute(current);
		return current;
	}

	protected AttributeDescriptor buildIsRecentEntityAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor current = new AttributeDescriptor();
		current.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isRecent", null, entityType))
			.initWithPattern("false");
		javaClass.addAttribute(current);
		return current;
	}

	protected MethodDescriptor buildBuildEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor();
		creationScript.setOwner(getClassDescriptor());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, entityType, 
				new StandardNameMaker("build", null, entityType))
			.addModifier("protected")
			.setContent(creationScript.getText());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buidGetValidateButtonLabelMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.String, 
			new StandardNameMaker("getValidate", "ButtonLabel", entityType))
			.addModifier("public")
			.setContent(
				"if (isNewEntity)",
				"	return \"Valider et nouveau\";",
				"else",
				"	return \"Valider\";").
			replace("isNewEntity", getIsNewEntityAttribute());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCreateEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("create", null, entityType))
			.addModifier("public")
			.setContent(
				"openedEntities = new ArrayList<EntityType>();",
				"openedEntities.add(buildEntity());",
				"isNewEntity = true;",
				"isRecentEntity = true;",
				"doGoFirst();"
			).
			replace("openedEntities", getParent().getOpenedEntitiesAttribute()).
			replace("EntityType", getEntityType().getClassDescriptor()).
			replace("buildEntity", getBuildEntityMethod()).
			replace("isNewEntity", getIsNewEntityAttribute()).
			replace("doGoFirst", getEntitiesGoFirstMethod());
		if (getIsRecentEntityAttribute()!=null) 
			meth.replace("isRecentEntity", getIsRecentEntityAttribute());
		else
			meth.removeLine("isRecentEntity = true;");
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCancelCurrentEntityMethod(ClassDescriptor javaClass) {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor clearCurrentEntity = parent.getClearCurrentEntityMethod();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("cancel", null, currentEntity))
			.addModifier("public")
			.setContent(
				"GuiUtil.cancelGui();",
				"if (openedEntities.size()==0)",
				"	clearCurrentEntity();",
				"else",
				"	setCurrentEntity(currentIndex);");
		meth.replace("GuiUtil", Frmk.GuiUtil);
		meth.replace("openedEntities", getParent().getOpenedEntitiesAttribute());
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("clearCurrentEntity", clearCurrentEntity);
		meth.replace("setCurrentEntity", getParent().getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildSaveCurrentEntityMethod(ClassDescriptor javaClass) {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("save", null, currentEntity))
			.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"}");
		meth
			.replace("isEntityValid", getIsEntityValidMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildValidateCurrentEntityMethod(ClassDescriptor javaClass) {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("validate", null, currentEntity))
			.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"	return true;",
			"}",
			"else",
			"	return false;"
		);
		meth
			.replace("isEntityValid", getIsEntityValidMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected void modifyValidateCurrentEntityMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		validateCurrentEntity.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"	if (isNewEntity) {",
			"		entityManager.persist(currentEntity);",
			"		addEntityToList(currentEntity);",
			"		isNewEntity = false;",
			"	}",
			"	return true;",
			"}",
			"else",
			"	return false;"
		);
		validateCurrentEntity
			.replace("isEntityValid", getIsEntityValidMethod())
			.replace("addEntityToList(currentEntity);", parent.addEntityToList(currentEntity.getName()))
			.replace("currentEntity", currentEntity)
			.replace("entityManager", getDocument().getEntityManager());
		validateCurrentEntity.replace("isNewEntity", getIsNewEntityAttribute());
	}
	
	protected void modifyClearEntityMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		parent.getClearCurrentEntityMethod()
			.insertLines("/// continue clearing here", 
				"isNewEntity = false;",
				"isRecentEntity = false;")
			.replace("isNewEntity", getIsNewEntityAttribute());
		if (getIsRecentEntityAttribute()!=null)
			parent.getClearCurrentEntityMethod()
				.replace("isRecentEntity", getIsRecentEntityAttribute());
		else
			parent.getClearCurrentEntityMethod()
				.removeLine("isRecentEntity = false;");
	}

	protected void modifyCancelCurrentEntityMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		MethodDescriptor clearCurrentEntity = parent.getClearCurrentEntityMethod();
		cancelCurrentEntity.setContent(
			"GuiUtil.cancelGui();",
			"if (isNewEntity) {",
			"	openedEntities.remove(currentEntity);",
			"	if (currentIndex>=openedEntities.size())",
			"		currentIndex = openedEntities.size()-1;",
			"	isNewEntity = false;",
			"	isRecentEntity = false;",
			"}",
			"if (openedEntities.size()==0)",
			"	clearCurrentEntity();",
			"else",
			"	setCurrentEntity(currentIndex);");
		cancelCurrentEntity.replace("GuiUtil", Frmk.GuiUtil);
		cancelCurrentEntity.replace("isNewEntity", isNewEntity);
		if (isRecentEntity!=null)
			cancelCurrentEntity.replace("isRecentEntity", isRecentEntity);
		else
			cancelCurrentEntity.removeLine("isRecentEntity = false;");
		cancelCurrentEntity.replace("openedEntities", getParent().getOpenedEntitiesAttribute());
		cancelCurrentEntity.replace("currentEntity", getParent().getCurrentEntityAttribute());
		cancelCurrentEntity.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		cancelCurrentEntity.replace("clearCurrentEntity", clearCurrentEntity);
		cancelCurrentEntity.replace("setCurrentEntity", getParent().getSetCurrentEntityMethod());
	}

	public SelectManyDetailDescriptor addCreate(ScriptDescriptor creationScript) {
		this.creationScript = creationScript;
		return this;
	}

	public SelectManyDetailDescriptor addForm(FormDescriptor form) {
		super.addForm(form);
		return this;
	}
	
	public MethodDescriptor getFillFormMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getSetCurrentEntityMethod();
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
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}
	
	public String getSubmitTarget() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}

	public String getValidateTarget() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}
	
	public SelectManyDetailDescriptor addPersistentList(PersistentList persistentList) {
		super.addPersistentList(persistentList);
		return this;
	}

	String[] detailPattern = defaultDetailPattern;

	protected String[] getDetailPattern() {
		return detailPattern;
	}
	
	protected void buildFaceletFragment() {
		super.buildFaceletFragment();
		getFragment().replaceMethod("goFirst", getEntitiesGoFirstMethod());
		getFragment().replaceMethod("goFastPrevious", getEntitiesGoFastPreviousMethod());
		getFragment().replaceMethod("goPrevious", getEntitiesGoPreviousMethod());
		getFragment().replaceMethod("goNext", getEntitiesGoNextMethod());
		getFragment().replaceMethod("goFastNext", getEntitiesGoFastNextMethod());
		getFragment().replaceMethod("goLast", getEntitiesGoLastMethod());

		getFragment().replaceMethod("closeCurrentEntity", getCloseCurrentEntityMethod());
		getFragment().replaceMethod("cancelCurrentEntity", getCancelAndNavigateInsideEntitiesMethod());
		DetailHolder parent = getParent(DetailHolder.class);
		if (parent.getDeleteEntityMethod()!=null) {
			String replaceThis = "<h:commandButton value=\"del\" action=\"#{beanName.requestEntityDeletion(line)}\"/>"; 
			replaceThis = Rewrite.replace(replaceThis, "beanName", getDocument().getBeanName());
			replaceThis = Rewrite.replace(replaceThis, "requestEntityDeletion", 
				FragmentDescriptor.getMethod(parent.getRequestEntityForDeletionMethod()));
			replaceThis = Rewrite.replace(replaceThis, "line", parent.getEntityAlias());
			
			String byThis = Rewrite.replace(replaceThis, "/>", " disabled=\"#{beanName.currentEntityVisible}\"/>");
			byThis = Rewrite.replace(byThis, "beanName", getDocument().getBeanName());
			byThis = Rewrite.replace(byThis, "currentEntityVisible", 
				FragmentDescriptor.getProperty(parent.getIsCurrentEntityVisibleMethod()));
			FragmentDescriptor fragment = ((Component)parent).getFragment();
			fragment.replace(replaceThis, byThis);
		}
		if (getCreationScript()!=null) {
			FragmentDescriptor fragment = ((Component)parent).getFragment();
			fragment.insertLines("/// insert commands here", 
				"<h:commandButton value=\"Creer\" action=\"#{beanName.createEntity}\" rendered=\"#{!beanName.currentEntityVisible}\"/>");
			fragment.replace("rendering condition here", "#{!beanName.currentEntityVisible}");
			fragment.replace("beanName", getDocument().getBeanName());
			fragment.replaceMethod("createEntity", getCreateEntityMethod());
			fragment.replaceProperty("currentEntityVisible", parent.getIsCurrentEntityVisibleMethod());
			getFragment().replaceMethod("insertEntity", getInsertEntityMethod());
			getFragment().replaceProperty("createRendered", getIsCreateEntityRenderedMethod());
		}
		else
			getFragment().removeLine("value=\"creer\"");
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceMethod("closeCurrentEntity", getCloseCurrentEntityMethod());
		getFragment().replaceMethod("cancelCurrentEntity", getCancelCurrentEntityMethod());
		getFragment().replaceMethod("validateCurrentEntity", getValidateCurrentEntityMethod());
	}

	public MethodDescriptor[] getCheckDirtyMethods() {
		return new MethodDescriptor[] {isEntityDirty};
	}

	public String getCheckDirtyTag() {
		return "/// insert dirty checking here";
	}
	
	SelectManyDetailHolder getParent() {
		return (SelectManyDetailHolder) getParent(SelectManyDetailHolder.class);
	}
	
	String getListName() {
		return NamingUtil.toPlural(getParent().getEntityAlias());
	}
	
	AttributeDescriptor fastStep;
	
	public AttributeDescriptor getFastStepAttribute() {
		return fastStep;
	}
	
	protected AttributeDescriptor buildFastStepAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
				new StandardNameMaker(NamingUtil.toConst(getListName()), "_FAST_STEP", null)).
				addModifier("static final").
				initWithPattern("10");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	AttributeDescriptor entitiesNavigation;
	AttributeDescriptor entitiesNoNavigationConst;
	AttributeDescriptor entitiesGoFirstConst;
	AttributeDescriptor entitiesGoFastPreviousConst;
	AttributeDescriptor entitiesGoPreviousConst;
	AttributeDescriptor entitiesGoNextConst;
	AttributeDescriptor entitiesGoFastNextConst;
	AttributeDescriptor entitiesGoLastConst;
	AttributeDescriptor closeEntitiesPopupConst;
	AttributeDescriptor createEntityConst;

	public AttributeDescriptor getEntitiesNavigationAttribute() {
		return entitiesNavigation;
	}

	public AttributeDescriptor getEntitiesNoNavigationConstAttribute() {
		return entitiesNoNavigationConst;
	}

	public AttributeDescriptor getEntitiesGoFirstConstAttribute() {
		return entitiesGoFirstConst;
	}
	
	public AttributeDescriptor getEntitiesGoFastPreviousConstAttribute() {
		return entitiesGoFastPreviousConst;
	}
	
	public AttributeDescriptor getEntitiesGoPreviousConstAttribute() {
		return entitiesGoPreviousConst;
	}
	
	public AttributeDescriptor getEntitiesGoNextConstAttribute() {
		return entitiesGoNextConst;
	}
	
	public AttributeDescriptor getEntitiesGoFastNextConstAttribute() {
		return entitiesGoFastNextConst;
	}
	
	public AttributeDescriptor getEntitiesGoLastConstAttribute() {
		return entitiesGoLastConst;
	}
	
	public AttributeDescriptor getCloseEntitiesPopupConstAttribute() {
		return closeEntitiesPopupConst;
	}
	
	public AttributeDescriptor getCreateEntityConstAttribute() {
		return createEntityConst;
	}

	protected AttributeDescriptor buildEntitiesNavigationAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(getListName(), "Navigation", null)).
			initWithPattern(entitiesNoNavigationConst.getName());
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildEntitiesNoNavigationConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_NO_NAVIGATION", null)).
			addModifier("static final").
			initWithPattern("0");
		javaClass.addAttribute(attr);
		return attr;
	}

	protected AttributeDescriptor buildEntitiesGoFirstConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_GO_FIRST", null)).
			addModifier("static final").
			initWithPattern("1");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildEntitiesGoFastPreviousConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_GO_FAST_PREVIOUS", null)).
			addModifier("static final").
			initWithPattern("2");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildEntitiesGoPreviousConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_GO_PREVIOUS", null)).
			addModifier("static final").
			initWithPattern("3");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildEntitiesGoNextConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_GO_NEXT", null)).
			addModifier("static final").
			initWithPattern("4");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildEntitiesGoFastNextConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_GO_FAST_NEXT", null)).
			addModifier("static final").
			initWithPattern("5");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildEntitiesGoLastConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker(NamingUtil.toConst(getListName()), "_GO_LAST", null)).
			addModifier("static final").
			initWithPattern("6");
		javaClass.addAttribute(attr);
		return attr;
	}
	
	protected AttributeDescriptor buildCloseEntitiesPopupConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker("CLOSE", "_"+NamingUtil.toConst(getListName())+"_POPUP", null)).
			addModifier("static final").
			initWithPattern("7");
		javaClass.addAttribute(attr);
		return attr;
	}

	protected AttributeDescriptor buildCreateEntityConstAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rInt,
			new StandardNameMaker("CREATE", "_"+NamingUtil.toConst(getEntityAlias()), null)).
			addModifier("static final").
			initWithPattern("8");
		javaClass.addAttribute(attr);
		return attr;
	}

	MethodDescriptor isConfirmNavigationRequested;
	MethodDescriptor cancelNavigation;
	
	public MethodDescriptor getIsConfirmNavigationRequestedMethod() {
		return isConfirmNavigationRequested;
	}
	
	public MethodDescriptor getCancelNavigationMethod() {
		return cancelNavigation;
	}

	protected MethodDescriptor buildIsConfirmNavigationRequestedMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isConfirm", "Requested", entitiesNavigation));
		meth.addModifier("public");
		meth.setContent(
			"return navigation!=NONE;"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("NONE", entitiesNoNavigationConst);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCancelNavigationMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("cancel", null, entitiesNavigation));
		meth.addModifier("public");
		meth.setContent(
			"navigation = NONE;"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("NONE", entitiesNoNavigationConst);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor doEntitiesGoFirst;
	MethodDescriptor entitiesGoFirst;
	
	public MethodDescriptor getDoEntitiesGoFirstMethod() {
		return doEntitiesGoFirst;
	}
	public MethodDescriptor getEntitiesGoFirstMethod() {
		return entitiesGoFirst;
	}

	protected MethodDescriptor buildEntitiesGoFirstMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker(getListName()+"GoFirst", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = GO_FIRST;",
			"else",
			"	doGoFirst();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("GO_FIRST", entitiesGoFirstConst);
		meth.replace("doGoFirst", doEntitiesGoFirst);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoEntitiesGoFirstMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("do", getListName()+"GoFirst", null));
		meth.setContent(
			"currentIndex = 0;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("setCurrentEntity", getParent().getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor doEntitiesGoFastPrevious;
	MethodDescriptor entitiesGoFastPrevious;
	
	public MethodDescriptor getDoEntitiesGoFastPreviousMethod() {
		return doEntitiesGoFastPrevious;
	}
	public MethodDescriptor getEntitiesGoFastPreviousMethod() {
		return entitiesGoFastPrevious;
	}

	protected MethodDescriptor buildEntitiesGoFastPreviousMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker(getListName()+"GoFastPrevious", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = GO_FAST_PREVIOUS;",
			"else",
			"	doGoFastPrevious();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("GO_FAST_PREVIOUS", entitiesGoFastPreviousConst);
		meth.replace("doGoFastPrevious", doEntitiesGoFastPrevious);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoEntitiesGoFastPreviousMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("do", getListName()+"GoFastPrevious", null));
		meth.setContent(
			"currentIndex -= FAST_STEP;",
			"if (currentIndex<0)",
			"	currentIndex=0;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("FAST_STEP", fastStep);
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("setCurrentEntity", getParent().getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor doEntitiesGoPrevious;
	MethodDescriptor entitiesGoPrevious;
	
	public MethodDescriptor getDoEntitiesGoPreviousMethod() {
		return doEntitiesGoPrevious;
	}
	public MethodDescriptor getEntitiesGoPreviousMethod() {
		return entitiesGoPrevious;
	}

	protected MethodDescriptor buildEntitiesGoPreviousMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker(getListName()+"GoPrevious", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = GO_PREVIOUS;",
			"else",
			"	doGoPrevious();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("GO_PREVIOUS", entitiesGoPreviousConst);
		meth.replace("doGoPrevious", doEntitiesGoPrevious);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoEntitiesGoPreviousMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("do", getListName()+"GoPrevious", null));
		meth.setContent(
			"currentIndex -= 1;",
			"if (currentIndex<0)",
			"	currentIndex=0;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("setCurrentEntity", getParent().getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor doEntitiesGoNext;
	MethodDescriptor entitiesGoNext;
	
	public MethodDescriptor getDoEntitiesGoNextMethod() {
		return doEntitiesGoNext;
	}
	public MethodDescriptor getEntitiesGoNextMethod() {
		return entitiesGoNext;
	}

	protected MethodDescriptor buildEntitiesGoNextMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker(getListName()+"GoNext", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = GO_NEXT;",
			"else",
			"	doGoNext();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("GO_NEXT", entitiesGoNextConst);
		meth.replace("doGoNext", doEntitiesGoNext);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoEntitiesGoNextMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("do", getListName()+"GoNext", null));
		meth.setContent(
			"currentIndex += 1;",
			"if (currentIndex>=openedEntities.size())",
			"	currentIndex=openedEntities.size()-1;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("openedEntities", getParent().getOpenedEntitiesAttribute());
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("setCurrentEntity", getParent().getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor doEntitiesGoFastNext;
	MethodDescriptor entitiesGoFastNext;
	
	public MethodDescriptor getDoEntitiesGoFastNextMethod() {
		return doEntitiesGoFastNext;
	}
	public MethodDescriptor getEntitiesGoFastNextMethod() {
		return entitiesGoFastNext;
	}

	protected MethodDescriptor buildEntitiesGoFastNextMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker(getListName()+"GoFastNext", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = GO_FAST_NEXT;",
			"else",
			"	doGoFastNext();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("GO_FAST_NEXT", entitiesGoFastNextConst);
		meth.replace("doGoFastNext", doEntitiesGoFastNext);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoEntitiesGoFastNextMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("do", getListName()+"GoFastNext", null));
		meth.setContent(
			"currentIndex += FAST_STEP;",
			"if (currentIndex>=openedEntities.size())",
			"	currentIndex=openedEntities.size()-1;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("FAST_STEP", fastStep);
		meth.replace("openedEntities", getParent().getOpenedEntitiesAttribute());
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("setCurrentEntity", getParent(DetailHolder.class).getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor doEntitiesGoLast;
	MethodDescriptor entitiesGoLast;
	
	public MethodDescriptor getDoEntitiesGoLastMethod() {
		return doEntitiesGoLast;
	}
	public MethodDescriptor getEntitiesGoLastMethod() {
		return entitiesGoLast;
	}

	protected MethodDescriptor buildEntitiesGoLastMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker(getListName()+"GoLast", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = GO_LAST;",
			"else",
			"	doGoLast();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("GO_LAST", entitiesGoLastConst);
		meth.replace("doGoLast", doEntitiesGoLast);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoEntitiesGoLastMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("do", getListName()+"GoLast", null));
		meth.setContent(
			"currentIndex = openedEntities.size()-1;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("openedEntities", getParent().getOpenedEntitiesAttribute());
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("setCurrentEntity", getParent(DetailHolder.class).getSetCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor isEntityDirty;

	public MethodDescriptor getIsEntityDirtyMethod() {
		return isEntityDirty;
	}
	
	protected MethodDescriptor buildIsEntityDirtyMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor() {
			public String[] getText() {
				if (getIsNewEntityAttribute()!=null)
					replace("isNewEntity", getIsNewEntityAttribute());
				else
					removeLines(
						"if (currentEntity==null)",
						"	return false;"
				);
				return super.getText();
			}
		};
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("is", "Dirty", getParent().getCurrentEntityAttribute()));
		meth.setContent(
			"/// check new entity here",
			"if (currentEntity==null)",
			"	return false;",
			"/// insert dirty checking here",
			"return false;"
		);
		if (getCreationScript()!=null) {
			meth.insertLines("/// check new entity here", 
				"if(isNewEntity)",
				"	return true;"
			);
		}
		meth.replace("currentEntity", getParent().getCurrentEntityAttribute());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor doInsertEntity;
	MethodDescriptor insertEntity;
	MethodDescriptor isCreateEntityRendered;
	
	public MethodDescriptor getDoInsertEntityMethod() {
		return doInsertEntity;
	}
	public MethodDescriptor getInsertEntityMethod() {
		return insertEntity;
	}
	public MethodDescriptor getIsCreateEntityRenderedMethod() {
		return isCreateEntityRendered;
	}

	protected MethodDescriptor buildInsertEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("insert", getEntityAlias(), null));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = CREATE_ENTITY;",
			"else",
			"	doInsertEntity();"
		);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("CREATE_ENTITY", createEntityConst);
		meth.replace("doInsertEntity", doInsertEntity);
		meth.replace("isDirty", isEntityDirty);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildIsCreateEntityRenderedMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
				new StandardNameMaker("isCreate", getEntityAlias()+"Rendered", null));
		meth.addModifier("public");
		meth.setContent(
			"return !isNewEntity;"
		);
		meth.replace("isNewEntity", isNewEntity);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildDoInsertEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("doInsert", getEntityAlias(), null));
		meth.setContent(
			"currentIndex++;",
			"EntityType entityInstance = buildEntity();", 
			"if (currentIndex==openedEntities.size())",
			"	openedEntities.add(entityInstance);",
			"else",
			"	openedEntities.add(currentIndex, entityInstance);",
			"isNewEntity = true;",
			"setCurrentEntity(currentIndex);"
		);
		meth.replace("openedEntities", getParent().getOpenedEntitiesAttribute());
		meth.replace("buildEntity", getBuildEntityMethod());
		meth.replace("currentIndex", getParent().getCurrentEntityIndexAttribute());
		meth.replace("EntityType", getEntityType().getClassDescriptor());
		meth.replace("entityInstance", getEntityAlias());
		meth.replace("setCurrentEntity", getParent(DetailHolder.class).getSetCurrentEntityMethod());
		meth.replace("isNewEntity", getIsNewEntityAttribute());
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor getOpenDetailMethod() {
		return entitiesGoFirst;
	}
	
	MethodDescriptor closeCurrentEntity;

	public MethodDescriptor getCloseCurrentEntityMethod() {
		return closeCurrentEntity;
	}

	protected MethodDescriptor buildCloseCurrentEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("close", null, getParent().getCurrentEntityAttribute()));
		meth.addModifier("public");
		meth.setContent(
			"if (isDirty())",
			"	navigation = CLOSE_ENTITY;",
			"else",
			"	doCloseCurrentEntity();"
		);
		meth.replace("isDirty", isEntityDirty);
		meth.replace("navigation", entitiesNavigation);
		meth.replace("CLOSE_ENTITY", closeEntitiesPopupConst);
		meth.replace("doCloseCurrentEntity", getParent().getClearCurrentEntityMethod());
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor navigateInsideEntities;
	MethodDescriptor saveAndNavigateInsideEntities;
	MethodDescriptor cancelAndNavigateInsideEntities;
	
	public MethodDescriptor getNavigateInsideEntitiesMethod() {
		return navigateInsideEntities;
	}
	public MethodDescriptor getSaveAndNavigateInsideEntitiesMethod() {
		return saveAndNavigateInsideEntities;
	}
	public MethodDescriptor getCancelAndNavigateInsideEntitiesMethod() {
		return cancelAndNavigateInsideEntities;
	}

	protected MethodDescriptor buildNavigateInsideEntitiesMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("navigateInside", NamingUtil.toPlural(getEntityAlias()), null));
		meth.setContent(
			"if (navigation==ENTITIES_GO_FIRST)",
			"	doGoFirst();",
			"else if (navigation==ENTITIES_GO_FAST_PREVIOUS)",
			"	doGoFastPrevious();",
			"else if (navigation==ENTITIES_GO_PREVIOUS)",
			"	doGoPrevious();",
			"else if (navigation==ENTITIES_GO_NEXT)",
			"	doGoNext();",
			"else if (navigation==ENTITIES_GO_FAST_NEXT)",
			"	doGoFastNext();",
			"else if (navigation==ENTITIES_GO_LAST)",
			"	doGoLast();",
			"else if (navigation==CLOSE_ENTITY_POPUP)",
			"	closeCurrentEntity();",
			"else if (navigation==CREATE_ENTITY)",
			"	doInsertEntity();",
			"navigation = NONE;"
		);
		if (creationScript!=null) {
			meth.replace("CREATE_ENTITY", createEntityConst);
			meth.replace("doInsertEntity", doInsertEntity);
		}
		else {
			meth.removeLines(
				"else if (navigation==CREATE_ENTITY)",
				"	doInsertEntity();"
			);
		}
		meth.replace("navigation", entitiesNavigation);
		meth.replace("ENTITIES_GO_FIRST", entitiesGoFirstConst);
		meth.replace("doGoFirst", entitiesGoFirst);
		meth.replace("ENTITIES_GO_FAST_PREVIOUS", entitiesGoFastPreviousConst);
		meth.replace("doGoFastPrevious", entitiesGoFastPrevious);
		meth.replace("ENTITIES_GO_PREVIOUS", entitiesGoPreviousConst);
		meth.replace("doGoPrevious", entitiesGoPrevious);
		meth.replace("ENTITIES_GO_NEXT", entitiesGoNextConst);
		meth.replace("doGoNext", entitiesGoNext);
		meth.replace("ENTITIES_GO_FAST_NEXT", entitiesGoFastNextConst);
		meth.replace("doGoFastNext", entitiesGoFastNext);
		meth.replace("ENTITIES_GO_LAST", entitiesGoLastConst);
		meth.replace("doGoLast", entitiesGoLast);
		meth.replace("NONE", entitiesNoNavigationConst);

		meth.replace("CLOSE_ENTITY_POPUP", closeEntitiesPopupConst);
		meth.replace("closeCurrentEntity", closeCurrentEntity);
		
		javaClass.addMethod(meth);
		return meth;
	}
	protected MethodDescriptor buildSaveAndNavigateInsideEntitiesMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("saveAndNavigateInside", NamingUtil.toPlural(getEntityAlias()), null));
		meth.addModifier("public");
		meth.setContent(
			"validateCurrentEntity();",
			"navigate();"
		);
		meth.replace("validateCurrentEntity", validateCurrentEntity);
		meth.replace("navigate", navigateInsideEntities);
		javaClass.addMethod(meth);
		return meth;
	}
	protected MethodDescriptor buildCancelAndNavigateInsideEntitiesMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("cancelAndNavigateInside", NamingUtil.toPlural(getEntityAlias()), null));
		meth.addModifier("public");
		meth.setContent(
			"cancelCurrentEntity();",
			"navigate();"
		);
		meth.replace("cancelCurrentEntity", cancelCurrentEntity);
		meth.replace("navigate", navigateInsideEntities);
		javaClass.addMethod(meth);
		return meth;
	}

	static final String[] defaultDetailPattern = {
		"/// insert layout here",
		"<div class=\"actionButtons\">",
		"	<h:commandButton image=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" action=\"#{beanName.goFirst}\"/>",
		"	<h:commandButton image=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" action=\"#{beanName.goFastPrevious}\"/>",
		"	<h:commandButton image=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" action=\"#{beanName.goPrevious}\"/>",
		"	<h:commandButton value=\"valider\" action=\"#{beanName.validateCurrentEntity}\" />",
		"	<h:commandButton value=\"creer\" action=\"#{beanName.insertEntity}\" rendered=\"#{beanName.createRendered}\"/>",
		"	<h:commandButton value=\"fermer\" action=\"#{beanName.closeCurrentEntity}\"/>",
		"	<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{beanName.cancelCurrentEntity}\"/>",
		"	<h:commandButton image=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" action=\"#{beanName.goNext}\"/>",
		"	<h:commandButton image=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" action=\"#{beanName.goFastNext}\"/>",
		"	<h:commandButton image=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" action=\"#{beanName.goLast}\"/>",
		"</div>"
	};

}
