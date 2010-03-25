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

public class TestCallSelector extends TestCase {

	public void testSimplePage() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency")
			.addStringField("name",null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee")
			.addStringField("firstName",null);
		employee.addManyToOne(agency, "agency", false, false);

		SelectorPopupDescriptor selector = new SelectorPopupDescriptor(
			"com.objetdirect.actions","SelectAgency", "views", "select-agency", agency);
		selector.build();
		
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Category(\"Nom de l'employee\");"
			)
			.replace("Employee", employee.getClassDescriptor()));
		feature.addForm(new FormDescriptor()
			.editStringField("firstName","Nom", 30)
			.chooseReference("agency", selector));
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(), 
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import com.objetdirect.domain.Employee;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
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
			"	String employeeFirstName;",
			"	Agency employeeAgency;",
			"	@In(create=true)",
			"	SelectAgencyAnimator selectAgency;",
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
			"		return new Category(\"Nom de l'employee\");",
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
			"	public void createEmployee() {",
			"		if (isEmployeeValid()) {",
			"			Employee employee = this.employee;",
			"			if (employee==null)",
			"				employee = buildEmployee();",
			"			employee.setFirstName(employeeFirstName);",
			"			employee.setAgency(employeeAgency);",
			"			if (this.employee==null)",
			"				entityManager.persist(employee);",
			"			employeeFirstName = \"\";",
			"			employeeAgency = null;",
			"			this.employee = null;",
			"		}",
			"	}",
			"	",
			"	public String getEmployeeFirstName() {",
			"		return employeeFirstName;",
			"	}",
			"	",
			"	public void setEmployeeFirstName(String employeeFirstName) {",
			"		this.employeeFirstName = employeeFirstName;",
			"	}",
			"	",
			"	public Agency getEmployeeAgency() {",
			"		return employeeAgency;",
			"	}",
			"	",
			"	public void setEmployeeAgency(Agency employeeAgency) {",
			"		this.employeeAgency = employeeAgency;",
			"	}",
			"	",
			"	public void openPopupForEmployeeAgency() {",
			"		selectAgency.selectAgency(new EmployeeAgencySelector());",
			"		selectAgency.open();",
			"	}",
			"",
			"	static class EmployeeAgencySelector implements SelectAgencyAnimator.Selector {",
			"	",
			"	",
			"	",
			"		public void select(Agency agency) {",
			"			CreateEmployeeAnimator parent = ((CreateEmployeeAnimator) Component",
			"				.getInstance(CreateEmployeeAnimator.class));",
			"			parent.setEmployeeAgency(agency);",
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
			"								<h:inputText value=\"#{createEmployee.employeeFirstName}\" size=\"30\"/>",
			"							</s:decorate>",
			"							<h:outputText/>",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:commandButton value=\"sel\" immediate=\"true\" action=\"#{createEmployee.openPopupForEmployeeAgency}\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createEmployee.createEmployee}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createEmployee.cancelEmployee}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"		<ui:include src=\"/views/select-agency.xhtml\"/>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}
}
