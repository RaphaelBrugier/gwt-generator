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

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;

public class TestFieldEdition extends TestCase {

	public void testSimpleFields() {
		Seam.clear();
		EntityDescriptor dummy = new EntityDescriptor("com.objetdirect.domain", "Dummy")
			.addStringField("stringField", null)
			.addBooleanField("boolField", null)
			.addDateField("dateField", null)
			.addIntField("intField", null);
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateDummy", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(dummy,
			new ScriptDescriptor(
				"return new Dummy(\"string\", true, new Date(), 1);"
			)
			.replace("Dummy", dummy.getClassDescriptor())
			.replace("Date", TypeDescriptor.Date)
		);
		feature.addForm(new FormDescriptor()
			.editStringField("stringField","String field", 30)
			.editBooleanField("boolField","Boolean field")
			.editDateField("dateField","Date field", "jj/MM/yyyy")
			.editNumberField("intField","Number field", "# ### ### ###", 13)
		);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Dummy;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import java.util.Date;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createDummy\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateDummyAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Dummy dummy = null;",
			"	String dummyStringField;",
			"	boolean dummyBoolField;",
			"	Date dummyDateField;",
			"	int dummyIntField;",
			"",
			"	public CreateDummyAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isDummyValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Dummy buildDummy() {",
			"		return new Dummy(\"string\", true, new Date(), 1);",
			"	}",
			"	",
			"	public void cancelDummy() {",
			"		GuiUtil.cancelGui();",
			"		if (dummy!=null)",
			"			entityManager.refresh(dummy);",
			"		else",
			"			dummy = null;",
			"	}",
			"	",
			"	public void createDummy() {",
			"		if (isDummyValid()) {",
			"			Dummy dummy = this.dummy;",
			"			if (dummy==null)",
			"				dummy = buildDummy();",
			"			dummy.setStringField(dummyStringField);",
			"			dummy.setBoolField(dummyBoolField);",
			"			dummy.setDateField(dummyDateField);",
			"			dummy.setIntField(dummyIntField);",
			"			if (this.dummy==null)",
			"				entityManager.persist(dummy);",
			"			dummyStringField = \"\";",
			"			dummyBoolField = false;",
			"			dummyDateField = new Date();",
			"			dummyIntField = 0;",
			"			this.dummy = null;",
			"		}",
			"	}",
			"	",
			"	public String getDummyStringField() {",
			"		return dummyStringField;",
			"	}",
			"	",
			"	public void setDummyStringField(String dummyStringField) {",
			"		this.dummyStringField = dummyStringField;",
			"	}",
			"	",
			"	public boolean isDummyBoolField() {",
			"		return dummyBoolField;",
			"	}",
			"	",
			"	public void setDummyBoolField(boolean dummyBoolField) {",
			"		this.dummyBoolField = dummyBoolField;",
			"	}",
			"	",
			"	public Date getDummyDateField() {",
			"		return dummyDateField;",
			"	}",
			"	",
			"	public void setDummyDateField(Date dummyDateField) {",
			"		this.dummyDateField = dummyDateField;",
			"	}",
			"	",
			"	public int getDummyIntField() {",
			"		return dummyIntField;",
			"	}",
			"	",
			"	public void setDummyIntField(int dummyIntField) {",
			"		this.dummyIntField = dummyIntField;",
			"	}",
			"",
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
			"							<h:outputText value=\"String field : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{createDummy.dummyStringField}\" size=\"30\"/>",
			"							</s:decorate>",
			"							<h:outputText value=\"Boolean field : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:selectBooleanCheckbox value=\"#{createDummy.dummyBoolField}\"/>",
			"							</s:decorate>",
			"							<h:outputText value=\"Date field : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<ice:selectInputDate value=\"#{createDummy.dummyDateField}\"",
			"									highlightClass=\"weekend:\"",
			"									highlightUnit=\"DAY_OF_WEEK: MONTH\"",
			"									highlightValue=\"1,7: 8\">",
			"									<f:convertDateTime pattern=\"jj/MM/yyyy\"/>",
			"								</ice:selectInputDate>",
			"							</s:decorate>",
			"							<h:outputText value=\"Number field : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{createDummy.dummyIntField}\" size=\"13\">",
			"									<f:convertNumber pattern=\"# ### ### ###\"/>",
			"								</h:inputText>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createDummy.createDummy}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createDummy.cancelDummy}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}
	
	public void testEnumField() {
		Seam.clear();
		EnumDescriptor enumDesc = new EnumDescriptor("com.objetdirect.domain", "Color")
			.addConstant("RED", "red")
			.addConstant("BLUE", "blue")
			.addConstant("YELLOW", "yellow")
			.addConstant("BLACK", "black");
		EntityDescriptor dummy = new EntityDescriptor("com.objetdirect.domain", "Dummy")
			.addEnumField("enumField", enumDesc, null);
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateDummy", "views", "create-dummy");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(dummy,
			new ScriptDescriptor(
				"return new Dummy(Color.BLACK);"
			)
			.replace("Dummy", dummy.getClassDescriptor())
			.replace("Color", enumDesc)
		);
		feature.addForm(new FormDescriptor()
			.editEnumField("enumField","Enum field", enumDesc, 50)
		);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(), 
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Color;",
			"import com.objetdirect.domain.Dummy;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createDummy\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateDummyAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Dummy dummy = null;",
			"	Color dummyEnumField;",
			"",
			"	public CreateDummyAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isDummyValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Dummy buildDummy() {",
			"		return new Dummy(Color.BLACK);",
			"	}",
			"	",
			"	public void cancelDummy() {",
			"		GuiUtil.cancelGui();",
			"		if (dummy!=null)",
			"			entityManager.refresh(dummy);",
			"		else",
			"			dummy = null;",
			"	}",
			"	",
			"	public void createDummy() {",
			"		if (isDummyValid()) {",
			"			Dummy dummy = this.dummy;",
			"			if (dummy==null)",
			"				dummy = buildDummy();",
			"			dummy.setEnumField(dummyEnumField);",
			"			if (this.dummy==null)",
			"				entityManager.persist(dummy);",
			"			dummyEnumField = null;",
			"			this.dummy = null;",
			"		}",
			"	}",
			"	",
			"	public Color getDummyEnumField() {",
			"		return dummyEnumField;",
			"	}",
			"	",
			"	public void setDummyEnumField(Color dummyEnumField) {",
			"		this.dummyEnumField = dummyEnumField;",
			"	}",
			"	",
			"	public Color[] getColors() {",
			"		return Color.values();",
			"	}",
			"",
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
			"							<h:outputText value=\"Enum field : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:selectOneMenu value=\"#{createDummy.dummyEnumField}\">",
			"									<s:selectItems value=\"#{createDummy.colors}\" var=\"color\"  label=\"#{color.label}\" noSelectionLabel=\"Choisissez...\"/>",
			"									<s:convertEnum />",
			"								</h:selectOneMenu>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createDummy.createDummy}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createDummy.cancelDummy}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}

	public void testCoupleOfEnumField() {
		Seam.clear();
		EnumDescriptor enumDesc = new EnumDescriptor("com.objetdirect.domain", "Color")
			.addConstant("RED", "red")
			.addConstant("BLUE", "blue")
			.addConstant("YELLOW", "yellow")
			.addConstant("BLACK", "black");
		EntityDescriptor dummy = new EntityDescriptor("com.objetdirect.domain", "Dummy")
			.addEnumField("firstColor", enumDesc, null)
			.addEnumField("lastColor", enumDesc, null);
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateDummy", "views", "create-dummy");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(dummy,
			new ScriptDescriptor(
				"return new Dummy(Color.BLACK, Color.RED);"
			)
			.replace("Dummy", dummy.getClassDescriptor())
			.replace("Color", enumDesc)
		);
		feature.addForm(new FormDescriptor()
			.editEnumField("firstColor","First color", enumDesc, 50)
			.editEnumField("lastColor","Last color", enumDesc, 50)
		);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Color;",
			"import com.objetdirect.domain.Dummy;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createDummy\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateDummyAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Dummy dummy = null;",
			"	Color dummyFirstColor;",
			"	Color dummyLastColor;",
			"",
			"	public CreateDummyAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isDummyValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Dummy buildDummy() {",
			"		return new Dummy(Color.BLACK, Color.RED);",
			"	}",
			"	",
			"	public void cancelDummy() {",
			"		GuiUtil.cancelGui();",
			"		if (dummy!=null)",
			"			entityManager.refresh(dummy);",
			"		else",
			"			dummy = null;",
			"	}",
			"	",
			"	public void createDummy() {",
			"		if (isDummyValid()) {",
			"			Dummy dummy = this.dummy;",
			"			if (dummy==null)",
			"				dummy = buildDummy();",
			"			dummy.setFirstColor(dummyFirstColor);",
			"			dummy.setLastColor(dummyLastColor);",
			"			if (this.dummy==null)",
			"				entityManager.persist(dummy);",
			"			dummyFirstColor = null;",
			"			dummyLastColor = null;",
			"			this.dummy = null;",
			"		}",
			"	}",
			"	",
			"	public Color getDummyFirstColor() {",
			"		return dummyFirstColor;",
			"	}",
			"	",
			"	public void setDummyFirstColor(Color dummyFirstColor) {",
			"		this.dummyFirstColor = dummyFirstColor;",
			"	}",
			"	",
			"	public Color getDummyLastColor() {",
			"		return dummyLastColor;",
			"	}",
			"	",
			"	public void setDummyLastColor(Color dummyLastColor) {",
			"		this.dummyLastColor = dummyLastColor;",
			"	}",
			"	",
			"	public Color[] getColors() {",
			"		return Color.values();",
			"	}",
			"",
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
			"							<h:outputText value=\"First color : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:selectOneMenu value=\"#{createDummy.dummyFirstColor}\">",
			"									<s:selectItems value=\"#{createDummy.colors}\" var=\"color\"  label=\"#{color.label}\" noSelectionLabel=\"Choisissez...\"/>",
			"									<s:convertEnum />",
			"								</h:selectOneMenu>",
			"							</s:decorate>",
			"							<h:outputText value=\"Last color : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:selectOneMenu value=\"#{createDummy.dummyLastColor}\">",
			"									<s:selectItems value=\"#{createDummy.colors}\" var=\"color\"  label=\"#{color.label}\" noSelectionLabel=\"Choisissez...\"/>",
			"									<s:convertEnum />",
			"								</h:selectOneMenu>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createDummy.createDummy}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createDummy.cancelDummy}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}

	public void testEntityField() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency")
			.addStringField("name", null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee")
			.addStringField("lastName", null)
			.addManyToOne(agency, "agency", false, false);
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Employee(\"name\");"
			)
			.replace("Employee", employee.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor()
			.editEntityField("agency","Agency", agency, "name", 50)
		);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(), 
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import com.objetdirect.domain.Employee;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import java.util.List;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createEmployee\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateEmployeeAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Employee employee = null;",
			"	Agency employeeAgency;",
			"	List<Agency> agencies = null;",
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
			"		return new Employee(\"name\");",
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
			"			employee.setAgency(employeeAgency);",
			"			if (this.employee==null)",
			"				entityManager.persist(employee);",
			"			employeeAgency = null;",
			"			this.employee = null;",
			"		}",
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
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<Agency> getAgencies() {",
			"		if (agencies==null) {",
			"			agencies = entityManager.createQuery(\"from Agency\").getResultList();",
			"		}",
			"		return agencies;",
			"	}",
			"",
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
			"							<h:outputText value=\"Agency : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:selectOneMenu value=\"#{createEmployee.employeeAgency}\">",
			"									<s:selectItems value=\"#{createEmployee.agencies}\" var=\"agency\"  label=\"#{agency.name}\" noSelectionLabel=\"Choisissez...\"/>",
			"									<s:convertEntity />",
			"								</h:selectOneMenu>",
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
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}
	
	public void testReadOnlyFields() {
		Seam.clear();
		EnumDescriptor enumDesc = new EnumDescriptor("com.objetdirect.domain", "DummyEnum").
			addConstant("A", "first letter").
			addConstant("B", "second letter").
			addConstant("Z", "last letter");
		EntityDescriptor linkedDummy = new EntityDescriptor("com.objetdirect.domain", "LinkedDummy").
			addStringField("name", null);
		EntityDescriptor dummy = new EntityDescriptor("com.objetdirect.domain", "Dummy").
			addStringField("stringField", null).
			addIntField("intField", null).
			addDateField("dateField", null).
			addBooleanField("boolField", null).
			addEnumField("enumField", enumDesc, null).
			addManyToOne(linkedDummy, "linkField", false, false);
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateDummy", "views", "create-dummy");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(dummy,
			new ScriptDescriptor(
				"return new Dummy(\"string\", 12, true, DummyEnum.A, null);"
			)
			.replace("DummyEnum", enumDesc)
			.replace("Dummy", dummy.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			showField("stringField", "String field", 20).
			showNumberField("intField", "Int field", "####", 20).
			showDateField("dateField", "Date field", "dd/MM/yyyy").
			showBooleanField("boolField", "Boolean field", "TRUE", "FALSE").
			showEnumField("enumField", "Enum field", 15).
			showEntityField("linkField", "Link field", "name", 15)
		);
		page.setFeature(feature);
		page.build();
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
			"							<h:outputText value=\"String field : \" />",
			"							<h:outputText value=\"#{createDummy.dummyStringField}\" />",
			"							<h:outputText value=\"Int field : \" />",
			"							<h:outputText value=\"#{createDummy.dummyIntField}\">",
			"								<f:convertNumber pattern=\"####\"/>",
			"							</h:outputText>",
			"							<h:outputText value=\"Date field : \" />",
			"							<h:outputText value=\"#{createDummy.dummyDateField}\">",
			"								<f:convertDateTime pattern=\"dd/MM/yyyy\"/>",
			"							</h:outputText>",
			"							<h:outputText value=\"Boolean field : \" />",
			"							<h:outputText value=\"#{createDummy.dummyBoolField?'TRUE':'FALSE'}\"/>",
			"							<h:outputText value=\"Enum field : \" />",
			"							<h:outputText value=\"#{createDummy.dummyEnumField.label}\"/>",
			"							<h:outputText value=\"Link field : \" />",
			"							<h:outputText value=\"#{createDummy.dummyLinkField.name}\"/>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createDummy.createDummy}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createDummy.cancelDummy}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}

	public void testCompoundEntityField() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency")
			.addStringField("name", null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee")
			.addStringField("lastName", null)
			.addManyToOne(agency, "agency", false, false);
		SelectorPopupDescriptor selector = new SelectorPopupDescriptor(
			"com.objetdirect.actions","SelectAgency", "views", "select-agency", agency);
		selector.build();
		PageDescriptor page = new PageDescriptor("com.objetdirect.actions","CreateEmployee", "views", "create-employee");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(employee,
			new ScriptDescriptor(
				"return new Employee(\"name\");"
			)
			.replace("Employee", employee.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor());
		feature.getForm(0).editCompoundEntityField("agency","Agency", agency)
			.showField("name", "Agency name", 50)
			.chooseReference(selector);
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
			"	Agency employeeAgency;",
			"	String employeeAgencyName;",
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
			"		return new Employee(\"name\");",
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
			"			employee.setAgency(employeeAgency);",
			"			employee.getAgency().setName(employeeAgencyName);",
			"			employee.setAgency(employeeAgency);",
			"			if (this.employee==null)",
			"				entityManager.persist(employee);",
			"			employeeAgency = null;",
			"			employeeAgencyName = \"\";",
			"			employeeAgency = null;",
			"			this.employee = null;",
			"		}",
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
			"	public String getEmployeeAgencyName() {",
			"		return employeeAgencyName;",
			"	}",
			"	",
			"	public void setEmployeeAgencyName(String employeeAgencyName) {",
			"		this.employeeAgencyName = employeeAgencyName;",
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
			"							<h:outputText value=\"Agency : \" />",
			"							<h:panelGroup rendered=\"#{createEmployee.employeeAgency!=null}\">",
			"								<h:outputText value=\"#{createEmployee.employeeAgencyName}\" />",
			"								<h:commandButton value=\"sel\" immediate=\"true\" action=\"#{createEmployee.openPopupForEmployeeAgency}\"/>",
			"							</h:panelGroup>",
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
