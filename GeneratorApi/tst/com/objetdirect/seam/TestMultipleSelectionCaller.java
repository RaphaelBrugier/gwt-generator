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

import junit.framework.TestCase;

import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.PersistentSelectOneListDescriptor;
import com.objetdirect.seam.lists.SelectOneDetailDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

public class TestMultipleSelectionCaller extends TestCase {

	public void testDirectInsertWithMultipleCaller() {
		Seam.clear();
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("lastName", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		employee.addManyToMany(skill, "skills", false);
		
		MultipleSelectorPopupDescriptor selector = new MultipleSelectorPopupDescriptor(
			"com.objetdirect.actions","SelectSkills", "views", "select-skills",
			skill);
		TransientSelectOneListDescriptor list = 
			new TransientSelectOneListDescriptor(skill).showField("name", "Name", 20);
		selector.setFeature(list);
		selector.build();
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Employee(\"Nom de l'employee\");"
			).replace("Employee", employee.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("lastName", "Nom", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skills")
			.showField("name", "Name", 20)
			.addDelete("Voulez-vous supprimer la competence : #{skillToDelete.name} ?");
		persistentList.setSelectionCaller(new MultipleSelectionCallerDescriptor(selector));
		feature.addPersistentList(persistentList);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Employee;",
			"import com.objetdirect.domain.Skill;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import java.util.ArrayList;",
			"import java.util.List;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.Component;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createEmployee\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateEmployeeAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Employee employee = null;",
			"	String employeeLastName;",
			"	Skill targetSkillForDeletion = null;",
			"	@In(create=true)",
			"	SelectSkillsAnimator selectSkills;",
			"	static final int OP_NONE = 0;",
			"	static final int OP_DELETE = 1;",
			"	int operation = OP_NONE;",
			"",
			"	public CreateEmployeeAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isEmployeeValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Employee buildEmployee() {",
			"		return new Employee(\"Nom de l'employee\");",
			"	}",
			"	",
			"	public void cancelEmployee() {",
			"		GuiUtil.cancelGui();",
			"		if (employee!=null)",
			"			entityManager.refresh(employee);",
			"		else",
			"			employee = null;",
			"	}",
			"	",
			"	public void saveEmployee() {",
			"		if (isEmployeeValid()) {",
			"			Employee employee = this.employee;",
			"			if (employee==null)",
			"				employee = buildEmployee();",
			"			employee.setLastName(employeeLastName);",
			"			if (this.employee==null) {",
			"				entityManager.persist(employee);",
			"				this.employee = employee;",
			"			}",
			"		}",
			"	}",
			"	",
			"	public void createEmployee() {",
			"		if (isEmployeeValid()) {",
			"			Employee employee = this.employee;",
			"			if (employee==null)",
			"				employee = buildEmployee();",
			"			employee.setLastName(employeeLastName);",
			"			if (this.employee==null)",
			"				entityManager.persist(employee);",
			"			employeeLastName = \"\";",
			"			this.employee = null;",
			"		}",
			"	}",
			"	",
			"	public String getEmployeeLastName() {",
			"		return employeeLastName;",
			"	}",
			"	",
			"	public void setEmployeeLastName(String employeeLastName) {",
			"		this.employeeLastName = employeeLastName;",
			"	}",
			"	",
			"	public boolean isEmployeeSkillsVisible() {",
			"		return employee !=null;",
			"	}",
			"	",
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<Skill> getEmployeeSkills() {",
			"		return employee.getSkills();",
			"	}",
			"	",
			"	public void requestSkillDeletion(Skill skill) {",
			"		targetSkillForDeletion = skill;",
			"		operation = OP_DELETE;",
			"	}",
			"	",
			"	public Skill getTargetSkillForDeletion() {",
			"		return targetSkillForDeletion;",
			"	}",
			"	",
			"	void deleteSkill(Skill skill) {",
			"		employee.removeSkill(skill);",
			"		entityManager.remove(skill);",
			"	}",
			"	",
			"	List<Skill> getSelectedSkills() {",
			"		List<Skill> result = new ArrayList<Skill>(employee.getSkills());",
			"		return result;",
			"	}",
			"	",
			"	public void openPopupForSkills() {",
			"		selectSkills.setEntitieSelector(new SkillsSelector(), getSelectedSkills());",
			"		selectSkills.open();",
			"	}",
			"	",
			"	public void addSkills(List<Skill> skillsToAdd) {",
			"		for (Skill skill : skillsToAdd) {",
			"			employee.addSkill(skill);",
			"		}",
			"	}",
			"	",
			"	public void removeSkills(List<Skill> skillsToRemove) {",
			"		for (Skill skill : skillsToRemove) {",
			"			employee.removeSkill(skill);",
			"		}",
			"	}",
			"	",
			"	public int getOperation() {",
			"		return operation;",
			"	}",
			"	",
			"	public void setOperation(int operation) {",
			"		this.operation = operation;",
			"	}",
			"	",
			"	public void cancelOperation() {",
			"		operation = OP_NONE;",
			"	}",
			"	",
			"	public void continueOperation() {",
			"		if (operation == OP_DELETE) {",
			"			if (targetSkillForDeletion != null) {",
			"				deleteSkill(targetSkillForDeletion);",
			"				targetSkillForDeletion = null;",
			"			}",
			"		}",
			"		operation = OP_NONE;",
			"	}",
			"	",
			"	public boolean isConfirmRequested() {",
			"		return operation != OP_NONE;",
			"	}",
			"	",
			"	public void setConfirmRequested(boolean confirmRequested) {",
			"		if (!confirmRequested) {",
			"			cancelOperation();",
			"		}",
			"	}",
			"",
			"	static class SkillsSelector implements SelectSkillsAnimator.Selector {",
			"	",
			"	",
			"	",
			"		public void select(List<Skill> addToSelected, List<Skill> removeFromSelected) {",
			"			CreateEmployeeAnimator parent = ((CreateEmployeeAnimator) Component",
			"				.getInstance(CreateEmployeeAnimator.class));",
			"			parent.addSkills(addToSelected);",
			"			parent.removeSkills(removeFromSelected);",
			"		}",
			"	",
			"	}",
			"}"
		);
		TestUtil.assertText(page.getFaceletText(),
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
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"",
			"			<div class=\"dialog\">",
			"				<h:panelGroup>",
			"					<s:validateAll>",
			"						<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"							<h:outputText value=\"Nom : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{createEmployee.employeeLastName}\" size=\"30\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<h:panelGroup rendered=\"#{createEmployee.employeeSkillsVisible}\">",
			"						<h:panelGroup>",
			"							<ice:dataTable id=\"employeeSkills\" value=\"#{createEmployee.employeeSkills}\" var=\"employeeSkill\"",
			"								rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px\">",
			"								<ice:column>",
			"									<f:facet name=\"header\">Name</f:facet>",
			"									<h:outputText value=\"#{employeeSkill.name}\" />",
			"								</ice:column>",
			"								<ice:column>",
			"									<h:commandButton value=\"del\" action=\"#{createEmployee.requestSkillDeletion(employeeSkill)}\"/>",
			"								</ice:column>",
			"							</ice:dataTable>",
			"							<ice:dataPaginator for=\"employeeSkills\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"								<f:facet name=\"first\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"								</f:facet>",
			"								<f:facet name=\"last\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"previous\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"next\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"fastforward\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"fastrewind\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"							</ice:dataPaginator>",
			"							<h:panelGroup>",
			"								<h:commandButton value=\"Ajouter\" action=\"#{createEmployee.openPopupForSkills}\"/>",
			"							</h:panelGroup>",
			"						</h:panelGroup>",
			"					</h:panelGroup>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createEmployee.createEmployee}\"/>",
			"						<h:commandButton value=\"sauver\" action=\"#{createEmployee.saveEmployee}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createEmployee.cancelEmployee}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"		<ice:panelPopup modal=\"true\" draggable=\"true\" styleClass=\"popup\" rendered=\"#{createEmployee.confirmRequested}\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGrid styleClass=\"title\" cellpadding=\"0\" cellspacing=\"0\" columns=\"2\">",
			"					<ice:outputText value=\"Confirmation\"/>",
			"				</ice:panelGrid>",
			"			</f:facet>",
			"			<f:facet name=\"body\">",
			"				<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
			"					<ice:outputText value=\"Voulez-vous supprimer la competence : #{createEmployee.targetSkillForDeletion.name} ?\" rendered=\"#{createEmployee.operation==1}\"/>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"valider\" action=\"#{createEmployee.continueOperation}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createEmployee.cancelOperation}\"/>",
			"					</div>",
			"				</ice:panelGrid>",
			"			</f:facet>",
			"		</ice:panelPopup>",
			"		<ui:include src=\"/views/select-skills.xhtml\"/>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}
	
	public void testUndirectInsertWithMultipleCaller() {
		Seam.clear();
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("lastName", null);
		EntityDescriptor skillLevel = new EntityDescriptor("com.objetdirect.domain", "SkillLevel").
			addIntField("level", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		employee.addOneToMany(skillLevel, "skillLevels", false);
		skillLevel.addManyToOne(skill, "skill", true, true);
		
		MultipleSelectorPopupDescriptor selector = new MultipleSelectorPopupDescriptor(
			"com.objetdirect.actions","SelectSkills", "views", "select-skills",
			skill);
		TransientSelectOneListDescriptor list = 
			new TransientSelectOneListDescriptor(skill).showField("name", "Name", 20);
		selector.setFeature(list);
		selector.build();
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Employee(\"Nom de l'employee\");"
			).replace("Employee", employee.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("lastName", "Nom", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skillLevels")
			.showField("skill.name", "Name", 20)
			.showNumberField("level", "Level", "###", 5);
		persistentList.setSelectionCaller(new MultipleSelectionCallerDescriptor(selector, "skill",
			new ScriptDescriptor(
				"SkillLevel skillLevel = new SkillLevel(1, skill);",
				"entityManager.persist(skillLevel);",
				"return skillLevel;")
			.replace("SkillLevel", skillLevel.getClassDescriptor())
		));
		feature.addPersistentList(persistentList);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Employee;",
			"import com.objetdirect.domain.Skill;",
			"import com.objetdirect.domain.SkillLevel;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import java.util.ArrayList;",
			"import java.util.List;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.Component;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createEmployee\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateEmployeeAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Employee employee = null;",
			"	String employeeLastName;",
			"	@In(create=true)",
			"	SelectSkillsAnimator selectSkills;",
			"",
			"	public CreateEmployeeAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isEmployeeValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Employee buildEmployee() {",
			"		return new Employee(\"Nom de l'employee\");",
			"	}",
			"	",
			"	public void cancelEmployee() {",
			"		GuiUtil.cancelGui();",
			"		if (employee!=null)",
			"			entityManager.refresh(employee);",
			"		else",
			"			employee = null;",
			"	}",
			"	",
			"	public void saveEmployee() {",
			"		if (isEmployeeValid()) {",
			"			Employee employee = this.employee;",
			"			if (employee==null)",
			"				employee = buildEmployee();",
			"			employee.setLastName(employeeLastName);",
			"			if (this.employee==null) {",
			"				entityManager.persist(employee);",
			"				this.employee = employee;",
			"			}",
			"		}",
			"	}",
			"	",
			"	public void createEmployee() {",
			"		if (isEmployeeValid()) {",
			"			Employee employee = this.employee;",
			"			if (employee==null)",
			"				employee = buildEmployee();",
			"			employee.setLastName(employeeLastName);",
			"			if (this.employee==null)",
			"				entityManager.persist(employee);",
			"			employeeLastName = \"\";",
			"			this.employee = null;",
			"		}",
			"	}",
			"	",
			"	public String getEmployeeLastName() {",
			"		return employeeLastName;",
			"	}",
			"	",
			"	public void setEmployeeLastName(String employeeLastName) {",
			"		this.employeeLastName = employeeLastName;",
			"	}",
			"	",
			"	public boolean isEmployeeSkillLevelsVisible() {",
			"		return employee !=null;",
			"	}",
			"	",
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<SkillLevel> getEmployeeSkillLevels() {",
			"		return employee.getSkillLevels();",
			"	}",
			"	",
			"	List<Skill> getSelectedSkills() {",
			"		List<Skill> result = new ArrayList<Skill>();",
			"		for (SkillLevel skillLevel : employee.getSkillLevels()) {",
			"			if (skillLevel.getSkill() != null)",
			"				result.add(skillLevel.getSkill());",
			"		}",
			"		return result;",
			"	}",
			"	",
			"	public void openPopupForSkillLevels() {",
			"		selectSkills.setEntitieSelector(new SkillLevelsSelector(), getSelectedSkills());",
			"		selectSkills.open();",
			"	}",
			"	",
			"	void deleteSkillLevel(SkillLevel skillLevelToDelete) {",
			"		employee.removeSkillLevel(skillLevelToDelete);",
			"		entityManager.remove(skillLevelToDelete);",
			"	}",
			"	",
			"	List<SkillLevel> buildSkillLevel(Skill skillToAdd) {",
			"		SkillLevel skillLevel = new SkillLevel(1, skill);",
			"		entityManager.persist(skillLevel);",
			"		return skillLevel;",
			"	}",
			"	",
			"	public void addSkills(List<Skill> skillsToAdd) {",
			"		for (Skill skill : skillsToAdd) {",
			"			SkillLevel skillLevel = buildSkillLevel(skill);",
			"			employee.addSkillLevel(skillLevel);",
			"		}",
			"	}",
			"	",
			"	public void removeSkills(List<Skill> skillsToRemove) {",
			"		Map<Skill, SkillLevel> actual = new HashMap<Skill, SkillLevel>();",
			"		for (SkillLevel skillLevel : employee.getSkillLevels()) {",
			"			if (skillLevel.getSkill() != null)",
			"				actual.put(skillLevel.getSkill(), skillLevel);",
			"		}",
			"		for (Skill skill : skillsToRemove) {",
			"			SkillLevel skillLevel = actual.get(skill);",
			"			employee.removeSkillLevel(skillLevel);",
			"			deleteSkillLevel(skillLevel);",
			"		}",
			"	}",
			"",
			"	static class SkillLevelsSelector implements SelectSkillsAnimator.Selector {",
			"	",
			"	",
			"	",
			"		public void select(List<Skill> addToSelected, List<Skill> removeFromSelected) {",
			"			CreateEmployeeAnimator parent = ((CreateEmployeeAnimator) Component",
			"				.getInstance(CreateEmployeeAnimator.class));",
			"			parent.addSkills(addToSelected);",
			"			parent.removeSkills(removeFromSelected);",
			"		}",
			"	",
			"	}",
			"}"
		);
		TestUtil.assertText(page.getFaceletText(),
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
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"",
			"			<div class=\"dialog\">",
			"				<h:panelGroup>",
			"					<s:validateAll>",
			"						<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"							<h:outputText value=\"Nom : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{createEmployee.employeeLastName}\" size=\"30\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<h:panelGroup rendered=\"#{createEmployee.employeeSkillLevelsVisible}\">",
			"						<h:panelGroup>",
			"							<ice:dataTable id=\"employeeSkillLevels\" value=\"#{createEmployee.employeeSkillLevels}\" var=\"employeeSkillLevel\"",
			"								rows=\"10\" resizable=\"true\" columnWidths=\"200px,50px\">",
			"								<ice:column>",
			"									<f:facet name=\"header\">Name</f:facet>",
			"									<h:outputText value=\"#{employeeSkillLevel.skill.name}\" />",
			"								</ice:column>",
			"								<ice:column>",
			"									<f:facet name=\"header\">Level</f:facet>",
			"									<h:outputText value=\"#{employeeSkillLevel.level}\">",
			"										<f:convertNumber pattern=\"###\"/>",
			"									</h:outputText>",
			"								</ice:column>",
			"							</ice:dataTable>",
			"							<ice:dataPaginator for=\"employeeSkillLevels\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"								<f:facet name=\"first\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"								</f:facet>",
			"								<f:facet name=\"last\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"previous\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"next\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"fastforward\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"fastrewind\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"							</ice:dataPaginator>",
			"							<h:panelGroup>",
			"								<h:commandButton value=\"Ajouter\" action=\"#{createEmployee.openPopupForSkillLevels}\"/>",
			"							</h:panelGroup>",
			"						</h:panelGroup>",
			"					</h:panelGroup>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createEmployee.createEmployee}\"/>",
			"						<h:commandButton value=\"sauver\" action=\"#{createEmployee.saveEmployee}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createEmployee.cancelEmployee}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"		<ui:include src=\"/views/select-skills.xhtml\"/>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}

	public void testUndirectInsertWithMultipleCallerAndSelection() {
		Seam.clear();
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("lastName", null);
		EntityDescriptor skillLevel = new EntityDescriptor("com.objetdirect.domain", "SkillLevel").
			addIntField("level", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		employee.addOneToMany(skillLevel, "skillLevels", false);
		skillLevel.addManyToOne(skill, "skill", true, true);
		
		MultipleSelectorPopupDescriptor selector = new MultipleSelectorPopupDescriptor(
			"com.objetdirect.actions","SelectSkills", "views", "select-skills",
			skill);
		TransientSelectOneListDescriptor list = 
			new TransientSelectOneListDescriptor(skill).showField("name", "Name", 20);
		selector.setFeature(list);
		selector.build();
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Employee(\"Nom de l'employee\");"
			).replace("Employee", employee.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("lastName", "Nom", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skillLevels")
			.showField("skill.name", "Name", 20);
		persistentList.setSelectionCaller(new MultipleSelectionCallerDescriptor(selector, "skill",
			new ScriptDescriptor(
				"SkillLevel skillLevel = new SkillLevel(1, skill);",
				"entityManager.persist(skillLevel);",
				"return skillLevel;")
			.replace("SkillLevel", skillLevel.getClassDescriptor())
		));
		FormDescriptor form = new FormDescriptor()
			.showEntityField("skill", "Skill Name", "name", 15);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor()
			.addForm(form);
		persistentList.setDetail(detail);
		feature.addPersistentList(persistentList);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"void deleteSkillLevel(SkillLevel skillLevelToDelete) {",
			"	employee.removeSkillLevel(skillLevelToDelete);",
			"	entityManager.remove(skillLevelToDelete);",
			"	if (skillLevelToDelete==currentSkillLevel)",
			"		currentSkillLevel = null;",
			"}"
		);
	}
	
	public void testUndirectInsertWithReuseOfDeleteEntityMethod() {
		Seam.clear();
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("lastName", null);
		EntityDescriptor skillLevel = new EntityDescriptor("com.objetdirect.domain", "SkillLevel").
			addIntField("level", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		employee.addOneToMany(skillLevel, "skillLevels", false);
		skillLevel.addManyToOne(skill, "skill", true, true);
		
		MultipleSelectorPopupDescriptor selector = new MultipleSelectorPopupDescriptor(
			"com.objetdirect.actions","SelectSkills", "views", "select-skills",
			skill);
		TransientSelectOneListDescriptor list = 
			new TransientSelectOneListDescriptor(skill).showField("name", "Name", 20);
		selector.setFeature(list);
		selector.build();
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Employee(\"Nom de l'employee\");"
			).replace("Employee", employee.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("lastName", "Nom", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skillLevels")
			.showField("skill.name", "Name", 20)
			.addDelete("Voulez-vous supprimer la competence : #{skillLevelToDelete.skill.name} ?");
		persistentList.setSelectionCaller(new MultipleSelectionCallerDescriptor(selector, "skill",
			new ScriptDescriptor(
				"SkillLevel skillLevel = new SkillLevel(1, skill);",
				"entityManager.persist(skillLevel);",
				"return skillLevel;")
			.replace("SkillLevel", skillLevel.getClassDescriptor())
		));
		feature.addPersistentList(persistentList);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"void deleteSkillLevel(SkillLevel skillLevel) {",
			"	employee.removeSkillLevel(skillLevel);",
			"	entityManager.remove(skillLevel);",
			"}"
		);		
		TestUtil.assertExists(page.getJavaText(),
			"public void openPopupForSkillLevels() {",
			"	selectSkills.setEntitieSelector(new SkillLevelsSelector(), getSelectedSkills());",
			"	selectSkills.open();",
			"}",
			"",
			"List<SkillLevel> buildSkillLevel(Skill skillToAdd) {",
			"	SkillLevel skillLevel = new SkillLevel(1, skill);",
			"	entityManager.persist(skillLevel);",
			"	return skillLevel;",
			"}"
		);
	}
	
}

