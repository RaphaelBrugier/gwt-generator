/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright Â© 2009 Objet Direct
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

import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.PersistentSelectOneListDescriptor;
import com.objetdirect.seam.lists.SelectOneDetailDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

import junit.framework.TestCase;

public class TestPersistentSelectOneListPage extends TestCase {

	public void testPersistentListWithReverseRelationship() {
		Seam.clear();
		EntityDescriptor category = new EntityDescriptor("com.objetdirect.domain", "Category").
			addStringField("name", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		category.addOneToMany(skill, "skills", true, "category", true, true);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditSkills", "views", "edit-skills");
		TransientSelectOneListDescriptor listFeature = new TransientSelectOneListDescriptor(category).
			showField("name", "Name", 20).
			addDelete("Voulez-vous supprimer la categorie : #{categoryToDelete.name} ?");
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor().addCreate(
			new ScriptDescriptor(
				"return new Category(\"Nom de la catégorie\");"
			).replace("Category", category.getClassDescriptor())
		);
		detail.addForm(new FormDescriptor().
			editStringField("name", "Nom de la categorie", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skills")
			.showField("name", "Name", 20)
			.addDelete("Voulez-vous supprimer la competence : #{skillToDelete.name} ?");
		SelectOneDetailDescriptor skillDetail = new SelectOneDetailDescriptor().addCreate(
			new ScriptDescriptor(
				"return new Skill(\"Nom de la compétence\");"
			).replace("Skill", skill.getClassDescriptor())
		);
		skillDetail.addForm(new FormDescriptor().
			editStringField("name", "Nom de la competence", 30)
		);
		persistentList.setDetail(skillDetail);
		detail.addPersistentList(persistentList);
		listFeature.setDetail(detail);
		page.setFeature(listFeature);
		page.build();
		// java
		verifyAttributes(page);
		verifyConstructor(page);
		verifyGetTransientList(page);
		verifyCurrentParentEntityManagement(page);
		verifyDeleteMainEntityManagement(page);
		verifyMainEntityValidation(page);
		verifySaveMainEntity(page);
		verifyMainEntityCreation(page);
		verifyMainEntityValidateButtonLabel(page);
		verifyMainEntityForm(page);
		verifyIsPersistentListVisible(page);
		verifyGetPersistentList(page);
		verifyCurrentChildEntityManagement(page);
		verifyChildEntityDeletion(page);
		verifyChildEntityValidation(page);
		verifyChildEntityCreation(page);
		verifyChildEntityValidateButtonLabel(page);
		verifyChildEntityForm(page);
		// facelet
		
		verifyConfirmPopup(page);
		verifyFaceletHeader(page);
		verifyFirstListOnFacelet(page);
		verifyMainEntityFormOnFacelet(page);
		verifyPersistentListOnFacelet(page);
		verifyChildEntityFormOnFacelet(page);
		verifyMainCommandsOnFacelet(page);
		verifyConfirmPopupOnFacelet(page);
	}
	
	public void testPersistentListWithoutReverseRelationship() {
		Seam.clear();
		EntityDescriptor category = new EntityDescriptor("com.objetdirect.domain", "Category").
			addStringField("name", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		category.addOneToMany(skill, "skills", true);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditSkills", "views", "edit-skills");
		TransientSelectOneListDescriptor listFeature = new TransientSelectOneListDescriptor(category).
			showField("name", "Name", 20).
			addDelete("Voulez-vous supprimer la categorie : #{categoryToDelete.name} ?");
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor().addCreate(
			new ScriptDescriptor(
				"return new Category(\"Nom de la catégorie\");"
			).replace("Category", category.getClassDescriptor())
		);
		detail.addForm(new FormDescriptor().
			editStringField("name", "Nom de la categorie", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skills")
			.showField("name", "Name", 20)
			.addDelete("Voulez-vous supprimer la competence : #{skillToDelete.name} ?");
		SelectOneDetailDescriptor skillDetail = new SelectOneDetailDescriptor().addCreate(
			new ScriptDescriptor(
				"return new Skill(\"Nom de la compétence\");"
			).replace("Skill", skill.getClassDescriptor())
		);
		skillDetail.addForm(new FormDescriptor().
			editStringField("name", "Nom de la competence", 30)
		);
		persistentList.setDetail(skillDetail);
		detail.addPersistentList(persistentList);
		listFeature.setDetail(detail);
		page.setFeature(listFeature);
		page.build();
		// java
		verifyAttributes(page);
		verifyConstructor(page);
		verifyGetTransientList(page);
		verifyCurrentParentEntityManagement(page);
		verifyDeleteMainEntityManagement(page);
		verifyMainEntityValidation(page);
		verifySaveMainEntity(page);
		verifyMainEntityCreation(page);
		verifyMainEntityValidateButtonLabel(page);
		verifyMainEntityForm(page);
		verifyIsPersistentListVisible(page);
		verifyGetPersistentList(page);
		verifyCurrentChildEntityManagement(page);
		verifyChildEntityDeletionWithoutReverse(page);
		verifyChildEntityValidationWithoutReverse(page);
		verifyChildEntityCreation(page);
		verifyChildEntityValidateButtonLabel(page);
		verifyChildEntityForm(page);
		// facelet
		verifyConfirmPopup(page);
		verifyFaceletHeader(page);
		verifyFirstListOnFacelet(page);
		verifyMainEntityFormOnFacelet(page);
		verifyPersistentListOnFacelet(page);
		verifyChildEntityFormOnFacelet(page);
		verifyConfirmPopupOnFacelet(page);
	}

	void verifyAttributes(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	List<Category> categories = null;",
			"	Category targetCategoryForDeletion = null;",
			"	Category currentCategory = null;",
			"	boolean isNewCategory = false;",
			"	boolean isRecentCategory = false;",
			"	String currentCategoryName;",
			"	Skill targetSkillForDeletion = null;",
			"	Skill currentSkill = null;",
			"	boolean isNewSkill = false;",
			"	String currentSkillName;",
			"	static final int OP_NONE = 0;",
			"	static final int OP_DELETE = 1;",
			"	int operation = OP_NONE;",
			""
		);
	}
	
	void verifyConstructor(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public EditSkillsAnimator() {",
			"	if (Conversation.instance().isLongRunning()) {",
			"		Conversation.instance().end(true);",
			"		Conversation.instance().leave();",
			"	}",
			"	Conversation.instance().begin();",
			"}"
		);
	}
	
	void verifyGetTransientList(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"@SuppressWarnings(\"unchecked\")",
			"public List<Category> getCategories() {",
			"	if (categories==null) {",
			"		categories = entityManager.createQuery(\"from Category\").getResultList();",
			"	}",
			"	return categories;",
			"}"
						
		);
	}

	void verifyCurrentParentEntityManagement(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"void setCurrentCategory(Category currentCategory) {",
			"	if (this.currentCategory != currentCategory) {",
			"		this.currentCategory = currentCategory;",
			"		currentCategoryName = currentCategory.getName();",
			"		currentSkill = null;",
			"	}",
			"}",
			"",
			"public void selectCategory(Category category) {",
			"	if (currentCategory!=null || operation != OP_NONE)",
			"		return;",
			"	setCurrentCategory(category);",
			"}",
			"",
			"public boolean isCurrentCategoryVisible() {",
			"	return currentCategory != null;",
			"}",
			"",
			"public void setCurrentCategoryVisible(boolean visible) {",
			"}",
			"",
			"void clearCurrentCategory() {",
			"	currentCategory = null;",
			"	isNewCategory = false;",
			"	isRecentCategory = false;",
			"	currentSkill = null;",
			"}"
		);
	}
	
	void verifyDeleteMainEntityManagement(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public void requestCategoryDeletion(Category category) {",
			"	targetCategoryForDeletion = category;",
			"	operation = OP_DELETE;",
			"}",
			"",
			"public Category getTargetCategoryForDeletion() {",
			"	return targetCategoryForDeletion;",
			"}",
			"",
			"void deleteCategory(Category category) {",
			"	categories.remove(category);",
			"	entityManager.remove(category);",
			"	if (category==currentCategory)",
			"		currentCategory = null;",
			"}"
		);
	}

	void verifyMainEntityValidation(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"boolean isCurrentCategoryValid() {",
			"	return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"}",
			"",
			"public void cancelCurrentCategory() {",
			"	GuiUtil.cancelGui();",
			"	clearCurrentCategory();",
			"}",
			"",
			"public void validateCurrentCategory() {",
			"	if (isCurrentCategoryValid()) {",
			"		currentCategory.setName(currentCategoryName);",
			"		if (isNewCategory) {",
			"			entityManager.persist(currentCategory);",
			"			categories.add(currentCategory);",
			"			createCategory();",
			"		}",
			"		else",
			"			clearCurrentCategory();",
			"	}",
			"}"
		);
	}

	void verifySaveMainEntity(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public void saveCurrentCategory() {",
			"	if (isCurrentCategoryValid()) {",
			"		currentCategory.setName(currentCategoryName);",
			"		if (isNewCategory) {",
			"			entityManager.persist(currentCategory);",
			"			categories.add(currentCategory);",
			"			isNewCategory = false;",
			"		}",
			"	}",
			"}"
		);
	}
	
	void verifyMainEntityCreation(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"protected Category buildCategory() {",
			"	return new Category(\"Nom de la catégorie\");",
			"}",
			"",
			"public void createCategory() {",
			"	setCurrentCategory(buildCategory());",
			"	isNewCategory = true;",
			"	isRecentCategory = true;",
			"}"
		);
	}

	void verifyMainEntityValidateButtonLabel(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public String getValidateCategoryButtonLabel() {",
			"	if (isNewCategory)",
			"		return \"Valider et nouveau\";",
			"	else",
			"		return \"Valider\";",
			"}"
		);
	}

	void verifyMainEntityForm(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public String getCurrentCategoryName() {",
			"	return currentCategoryName;",
			"}",
			"",
			"public void setCurrentCategoryName(String currentCategoryName) {",
			"	this.currentCategoryName = currentCategoryName;",
			"}"
		);
	}

	void verifyIsPersistentListVisible(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public boolean isCategorySkillsVisible() {",
			"	return currentCategory !=null && !isNewCategory;",
			"}"
		);
	}

	void verifyGetPersistentList(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"@SuppressWarnings(\"unchecked\")",
			"public List<Skill> getCategorySkills() {",
			"	return currentCategory.getSkills();",
			"}"
		);
	}
	
	void verifyCurrentChildEntityManagement(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"void setCurrentSkill(Skill currentSkill) {",
			"	if (this.currentSkill != currentSkill) {",
			"		this.currentSkill = currentSkill;",
			"		currentSkillName = currentSkill.getName();",
			"	}",
			"}",
			"",
			"public void selectSkill(Skill skill) {",
			"	if (currentSkill!=null || operation != OP_NONE)",
			"		return;",
			"	setCurrentSkill(skill);",
			"}",
			"",
			"public boolean isCurrentSkillVisible() {",
			"	return isCategorySkillsVisible() && currentSkill != null;",
			"}",
			"",
			"public void setCurrentSkillVisible(boolean visible) {",
			"}",
			"",
			"void clearCurrentSkill() {",
			"	currentSkill = null;",
			"	isNewSkill = false;",
			"}"
		);
	}

	void verifyChildEntityDeletion(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public void requestSkillDeletion(Skill skill) {",
			"	targetSkillForDeletion = skill;",
			"	operation = OP_DELETE;",
			"}",
			"",
			"public Skill getTargetSkillForDeletion() {",
			"	return targetSkillForDeletion;",
			"}",
			"",
			"void deleteSkill(Skill skill) {",
			"	skill.setCategory(null);",
			"	entityManager.remove(skill);",
			"	if (skill==currentSkill)",
			"		currentSkill = null;",
			"}"
		);
	}

	void verifyChildEntityValidation(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"boolean isCurrentSkillValid() {",
			"	return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"}",
			"",
			"public void cancelCurrentSkill() {",
			"	GuiUtil.cancelGui();",
			"	clearCurrentSkill();",
			"}",
			"",
			"public void validateCurrentSkill() {",
			"	if (isCurrentSkillValid()) {",
			"		currentSkill.setName(currentSkillName);",
			"		if (isNewSkill) {",
			"			entityManager.persist(currentSkill);",
			"			currentSkill.setCategory(currentCategory);",
			"			createSkill();",
			"		}",
			"		else",
			"			clearCurrentSkill();",
			"	}",
			"}"
		);
	}

	void verifyChildEntityCreation(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"protected Skill buildSkill() {",
			"	return new Skill(\"Nom de la compétence\");",
			"}",
			"",
			"public void createSkill() {",
			"	setCurrentSkill(buildSkill());",
			"	isNewSkill = true;",
			"}"
		);
	}

	void verifyChildEntityValidateButtonLabel(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public String getValidateSkillButtonLabel() {",
			"	if (isNewSkill)",
			"		return \"Valider et nouveau\";",
			"	else",
			"		return \"Valider\";",
			"}"
		);
	}
	
	void verifyChildEntityForm(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public String getCurrentSkillName() {",
			"	return currentSkillName;",
			"}",
			"",
			"public void setCurrentSkillName(String currentSkillName) {",
			"	this.currentSkillName = currentSkillName;",
			"}"
		);
	}

	void verifyConfirmPopup(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public int getOperation() {",
			"	return operation;",
			"}",
			"",
			"public void setOperation(int operation) {",
			"	this.operation = operation;",
			"}",
			"",
			"public void cancelOperation() {",
			"	operation = OP_NONE;",
			"}",
			"",
			"public void continueOperation() {",
			"	if (operation == OP_DELETE) {",
			"		if (targetCategoryForDeletion != null) {",
			"			deleteCategory(targetCategoryForDeletion);",
			"			targetCategoryForDeletion = null;",
			"		}",
			"		if (targetSkillForDeletion != null) {",
			"			deleteSkill(targetSkillForDeletion);",
			"			targetSkillForDeletion = null;",
			"		}",
			"	}",
			"	operation = OP_NONE;",
			"}",
			"",
			"public boolean isConfirmRequested() {",
			"	return operation != OP_NONE;",
			"}",
			"",
			"public void setConfirmRequested(boolean confirmRequested) {",
			"	if (!confirmRequested) {",
			"		cancelOperation();",
			"	}",
			"}"
		);
	}

	void verifyFaceletHeader(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
			"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
			"			\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
			"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\"",
			"	template=\"/layout/template.xhtml\">",
			"",
			"<ui:define name=\"body\">",
			"",
			"	<h:messages styleClass=\"message\"/>",
			"",
			"	<ice:form id=\"pageForm\">",
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">");
	}
	
	void verifyFirstListOnFacelet(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
			"<div class=\"dialog\">",
			"	<h:panelGroup>",
			"		<ice:dataTable id=\"categories\" value=\"#{editSkills.categories}\" var=\"category\"",
			"			rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px\">",
			"			<ice:column>",
			"				<f:facet name=\"header\">Name</f:facet>",
			"				<ice:rowSelector selectionAction=\"#{editSkills.selectCategory(category)}\" value=\"#{editSkills.selectedCategory}\" multiple=\"false\" />",
			"				<h:outputText value=\"#{category.name}\" />",
			"			</ice:column>",
			"			<ice:column>",
			"				<h:commandButton value=\"del\" action=\"#{editSkills.requestCategoryDeletion(category)}\" disabled=\"#{editSkills.currentCategoryVisible}\"/>",
			"			</ice:column>",
			"		</ice:dataTable>",
			"		<ice:dataPaginator for=\"categories\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"			<f:facet name=\"first\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"			</f:facet>",
			"			<f:facet name=\"last\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"previous\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"next\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"fastforward\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"fastrewind\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"		</ice:dataPaginator>",
			"		<h:panelGroup rendered=\"#{!editSkills.currentCategoryVisible}\">",
			"			<h:commandButton value=\"Creer\" action=\"#{editSkills.createCategory}\"/>",
			"		</h:panelGroup>",
			"	</h:panelGroup>"
		);
	}
	
	void verifyMainEntityFormOnFacelet(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editSkills.currentCategoryVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<s:validateAll>",
			"			<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"				<h:outputText value=\"Nom de la categorie : \" />",
			"				<s:decorate template=\"/layout/edit.xhtml\">",
			"					<h:inputText value=\"#{editSkills.currentCategoryName}\" size=\"30\"/>",
			"				</s:decorate>",
			"			</h:panelGrid>",
			"		</s:validateAll>"
		);
	};
	
	void verifyPersistentListOnFacelet(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editSkills.categorySkillsVisible}\">",
			"	<h:panelGroup>",
			"		<ice:dataTable id=\"categorySkills\" value=\"#{editSkills.categorySkills}\" var=\"categorySkill\"",
			"			rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px\">",
			"			<ice:column>",
			"				<f:facet name=\"header\">Name</f:facet>",
			"				<ice:rowSelector selectionAction=\"#{editSkills.selectSkill(categorySkill)}\" value=\"#{editSkills.selectedSkill}\" multiple=\"false\" />",
			"				<h:outputText value=\"#{categorySkill.name}\" />",
			"			</ice:column>",
			"			<ice:column>",
			"				<h:commandButton value=\"del\" action=\"#{editSkills.requestSkillDeletion(categorySkill)}\" disabled=\"#{editSkills.currentSkillVisible}\"/>",
			"			</ice:column>",
			"		</ice:dataTable>",
			"		<ice:dataPaginator for=\"categorySkills\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"			<f:facet name=\"first\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"			</f:facet>",
			"			<f:facet name=\"last\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"previous\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"next\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"fastforward\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"			<f:facet name=\"fastrewind\">",
			"				<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"			</f:facet>",
			"		</ice:dataPaginator>",
			"		<h:panelGroup rendered=\"#{!editSkills.currentSkillVisible}\">",
			"			<h:commandButton value=\"Creer\" action=\"#{editSkills.createSkill}\"/>",
			"		</h:panelGroup>",
			"	</h:panelGroup>"
		);
	}
	

	void verifyChildEntityFormOnFacelet(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
				"<h:panelGroup rendered=\"#{editSkills.currentSkillVisible}\">",
				"	<div class=\"mask\"/>",
				"	<h:panelGroup style=\"position:relative\">",
				"		<s:validateAll>",
				"			<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
				"				<h:outputText value=\"Nom de la competence : \" />",
				"				<s:decorate template=\"/layout/edit.xhtml\">",
				"					<h:inputText value=\"#{editSkills.currentSkillName}\" size=\"30\"/>",
				"				</s:decorate>",
				"			</h:panelGrid>",
				"		</s:validateAll>",
				"		<div class=\"actionButtons\">",
				"			<h:commandButton value=\"#{editSkills.validateSkillButtonLabel}\" action=\"#{editSkills.validateCurrentSkill}\"/>",
				"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editSkills.cancelCurrentSkill}\"/>",
				"		</div>",
				"	</h:panelGroup>",
				"</h:panelGroup>"
		);
	}

	void verifyMainCommandsOnFacelet(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
			"<div class=\"actionButtons\">",
			"	<h:commandButton value=\"#{editSkills.validateCategoryButtonLabel}\" action=\"#{editSkills.validateCurrentCategory}\"/>",
			"	<h:commandButton value=\"sauver\" action=\"#{editSkills.saveCurrentCategory}\"/>",
			"	<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editSkills.cancelCurrentCategory}\"/>",
			"</div>"
		);
	}

	void verifyConfirmPopupOnFacelet(PageDescriptor page) {
		TestUtil.assertExists(page.getFaceletText(),
			"<f:facet name=\"body\">",
			"	<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
			"		<ice:outputText value=\"Voulez-vous supprimer la categorie : #{editSkills.targetCategoryForDeletion.name} ?\" rendered=\"#{editSkills.operation==1}\"/>",
			"		<ice:outputText value=\"Voulez-vous supprimer la competence : #{editSkills.targetSkillForDeletion.name} ?\" rendered=\"#{editSkills.operation==1}\"/>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editSkills.continueOperation}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editSkills.cancelOperation}\"/>",
			"		</div>",
			"	</ice:panelGrid>",
			"</f:facet>"
		);
	}
	
	void verifyChildEntityDeletionWithoutReverse(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"public void requestSkillDeletion(Skill skill) {",
			"	targetSkillForDeletion = skill;",
			"	operation = OP_DELETE;",
			"}",
			"",
			"public Skill getTargetSkillForDeletion() {",
			"	return targetSkillForDeletion;",
			"}",
			"",
			"void deleteSkill(Skill skill) {",
			"	currentCategory.removeSkill(skill);",
			"	entityManager.remove(skill);",
			"	if (skill==currentSkill)",
			"		currentSkill = null;",
			"}"
		);
	}	
	
	void verifyChildEntityValidationWithoutReverse(PageDescriptor page) {
		TestUtil.assertExists(page.getJavaText(),
			"boolean isCurrentSkillValid() {",
			"	return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"}",
			"",
			"public void cancelCurrentSkill() {",
			"	GuiUtil.cancelGui();",
			"	clearCurrentSkill();",
			"}",
			"",
			"public void validateCurrentSkill() {",
			"	if (isCurrentSkillValid()) {",
			"		currentSkill.setName(currentSkillName);",
			"		if (isNewSkill) {",
			"			entityManager.persist(currentSkill);",
			"			currentCategory.addSkill(currentSkill);",
			"			createSkill();",
			"		}",
			"		else",
			"			clearCurrentSkill();",
			"	}",
			"}"
		);
	}
}
