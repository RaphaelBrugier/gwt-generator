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

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

public class TestCriteria extends TestCase {

	public void testSimpleList() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addManyToOne(agency, "agency", true, false);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 30).
			showField("lastName", "Last Name", 30).
			showField("agency.name", "Agency", 20);
		TransientCriteriaDescriptor criteria = new TransientCriteriaDescriptor().
			setForm(
				new FormDescriptor().
					editStringField("firstName", "First Name", 30).
					editStringField("lastName", "Last Name", 30).
					editStringField("agency.name", "Agency", 20)
			);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();

		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Employee;",
			"import java.util.List;",
			"import javax.persistence.EntityManager;",
			"import org.hibernate.criterion.Restrictions;",
			"import org.hibernate.Criteria;",
			"import org.hibernate.Session;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"editEmployees\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class EditEmployeesAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	List<Employee> employees = null;",
			"	String employeeFirstNameCriterion;",
			"	String employeeLastNameCriterion;",
			"	String employeeAgencyNameCriterion;",
			"",
			"	public EditEmployeesAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<Employee> getEmployees() {",
			"		if (employees==null) {",
			"			employees = getEmployeesCriteria().list();",
			"		}",
			"		return employees;",
			"	}",
			"	",
			"	public String getEmployeeFirstNameCriterion() {",
			"		return employeeFirstNameCriterion;",
			"	}",
			"	",
			"	public void setEmployeeFirstNameCriterion(String employeeFirstNameCriterion) {",
			"		this.employeeFirstNameCriterion = employeeFirstNameCriterion;",
			"	}",
			"	",
			"	public String getEmployeeLastNameCriterion() {",
			"		return employeeLastNameCriterion;",
			"	}",
			"	",
			"	public void setEmployeeLastNameCriterion(String employeeLastNameCriterion) {",
			"		this.employeeLastNameCriterion = employeeLastNameCriterion;",
			"	}",
			"	",
			"	public String getEmployeeAgencyNameCriterion() {",
			"		return employeeAgencyNameCriterion;",
			"	}",
			"	",
			"	public void setEmployeeAgencyNameCriterion(String employeeAgencyNameCriterion) {",
			"		this.employeeAgencyNameCriterion = employeeAgencyNameCriterion;",
			"	}",
			"	",
			"	public void validateEmployeesCriteria() {",
			"		employees = null;",
			"	}",
			"	",
			"	public void clearEmployeesCriteria() {",
			"		employeeFirstNameCriterion = null;",
			"		employeeLastNameCriterion = null;",
			"		employeeAgencyNameCriterion = null;",
			"	}",
			"	",
			"	Criteria getEmployeesCriteria() {",
			"		Session session = (Session)entityManager.getDelegate();",
			"		Criteria c = session.createCriteria(Employee.class);",
			"		if (employeeLastNameCriterion != null && employeeLastNameCriterion.length()>0)",
			"			c.add(Restrictions.like(\"lastName\", employeeLastNameCriterion+\"%\"));",
			"		if (employeeFirstNameCriterion != null && employeeFirstNameCriterion.length()>0)",
			"			c.add(Restrictions.like(\"firstName\", employeeFirstNameCriterion+\"%\"));",
			"		if (employeeAgencyNameCriterion != null && employeeAgencyNameCriterion.length()>0) {",
			"			Criteria c1 = c.createCriteria(\"agency\");",
			"			c1.add(Restrictions.like(\"name\", employeeAgencyNameCriterion+\"%\"));",
			"		}",
			"		return c;",
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
			"							<h:outputText value=\"First Name : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.employeeFirstNameCriterion}\" size=\"30\"/>",
			"							</s:decorate>",
			"							<h:outputText value=\"Last Name : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.employeeLastNameCriterion}\" size=\"30\"/>",
			"							</s:decorate>",
			"							<h:outputText value=\"Agency : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.employeeAgencyNameCriterion}\" size=\"20\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"valider\" action=\"#{editEmployees.validateEmployeesCriteria}\"/>",
			"						<h:commandButton value=\"reinitialiser\" action=\"#{editEmployees.clearEmployeesCriteria}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"				<h:panelGroup>",
			"					<ice:dataTable id=\"employees\" value=\"#{editEmployees.employees}\" var=\"employee\"",
			"						rows=\"10\" resizable=\"true\" columnWidths=\"300px,300px,200px\">",
			"						<ice:column>",
			"							<f:facet name=\"header\">First Name</f:facet>",
			"							<h:outputText value=\"#{employee.firstName}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Last Name</f:facet>",
			"							<h:outputText value=\"#{employee.lastName}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Agency</f:facet>",
			"							<h:outputText value=\"#{employee.agency.name}\" />",
			"						</ice:column>",
			"					</ice:dataTable>",
			"					<ice:dataPaginator for=\"employees\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"						<f:facet name=\"first\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"						</f:facet>",
			"						<f:facet name=\"last\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"previous\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"next\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"fastforward\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"fastrewind\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"					</ice:dataPaginator>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>" 
		);
	}

	public void testEmbeddedSubCriteria1() {
		Seam.clear();
		EntityDescriptor city = new EntityDescriptor("com.objetdirect.domain", "City").
			addStringField("name", null);
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addManyToOne(city, "city", true, false);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addManyToOne(agency, "agency", true, false);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 30).
			showField("lastName", "Last Name", 30).
			showField("agency.name", "Agency", 20);
		TransientCriteriaDescriptor criteria = new TransientCriteriaDescriptor().
			setForm(
				new FormDescriptor().
					editStringField("firstName", "First Name", 30).
					editStringField("lastName", "Last Name", 30).
					editStringField("agency.name", "Name", 20).
					editStringField("agency.city.name", "City", 20)
			);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"Criteria getEmployeesCriteria() {",
			"	Session session = (Session)entityManager.getDelegate();",
			"	Criteria c = session.createCriteria(Employee.class);",
			"	if (employeeLastNameCriterion != null && employeeLastNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"lastName\", employeeLastNameCriterion+\"%\"));",
			"	if (employeeFirstNameCriterion != null && employeeFirstNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"firstName\", employeeFirstNameCriterion+\"%\"));",
			"	if ((employeeAgencyCityNameCriterion != null && employeeAgencyCityNameCriterion.length()>0) ||",
			"		(employeeAgencyNameCriterion != null && employeeAgencyNameCriterion.length()>0)) {",
			"		Criteria c1 = c.createCriteria(\"agency\");",
			"		if (employeeAgencyNameCriterion != null && employeeAgencyNameCriterion.length()>0)",
			"			c1.add(Restrictions.like(\"name\", employeeAgencyNameCriterion+\"%\"));",
			"		if (employeeAgencyCityNameCriterion != null && employeeAgencyCityNameCriterion.length()>0) {",
			"			Criteria c2 = c1.createCriteria(\"city\");",
			"			c2.add(Restrictions.like(\"name\", employeeAgencyCityNameCriterion+\"%\"));",
			"		}",
			"	}",
			"	return c;",
			"}"
		);
	}
	
	public void testEmbeddedSubCriteria2() {
		Seam.clear();
		EntityDescriptor city = new EntityDescriptor("com.objetdirect.domain", "City").
			addStringField("name", null);
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addManyToOne(city, "city", true, false);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addManyToOne(agency, "agency", true, false);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 30).
			showField("lastName", "Last Name", 30).
			showField("agency.name", "Agency", 20);
		TransientCriteriaDescriptor criteria = new TransientCriteriaDescriptor().
			setForm(new FormDescriptor().
				editStringField("firstName", "First Name", 30).
				editStringField("lastName", "Last Name", 30).
				editStringField("agency.city.name", "City", 20)
			);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"Criteria getEmployeesCriteria() {",
			"	Session session = (Session)entityManager.getDelegate();",
			"	Criteria c = session.createCriteria(Employee.class);",
			"	if (employeeLastNameCriterion != null && employeeLastNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"lastName\", employeeLastNameCriterion+\"%\"));",
			"	if (employeeFirstNameCriterion != null && employeeFirstNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"firstName\", employeeFirstNameCriterion+\"%\"));",
			"	if (employeeAgencyCityNameCriterion != null && employeeAgencyCityNameCriterion.length()>0) {",
			"		Criteria c1 = c.createCriteria(\"agency\");",
			"		Criteria c2 = c1.createCriteria(\"city\");",
			"		c2.add(Restrictions.like(\"name\", employeeAgencyCityNameCriterion+\"%\"));",
			"	}",
			"	return c;",
			"}"
		);
	}
	
	
	public void testEmbeddedSubCriteria3() {
		Seam.clear();
		EntityDescriptor city = new EntityDescriptor("com.objetdirect.domain", "City").
			addStringField("name", null);
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addManyToOne(city, "city", true, false);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addManyToOne(agency, "agency", true, false);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 30).
			showField("lastName", "Last Name", 30).
			showField("agency.name", "Agency", 20);
		TransientCriteriaDescriptor criteria = new TransientCriteriaDescriptor().
			setForm(
				new FormDescriptor().
					editStringField("firstName", "First Name", 30).
					editStringField("lastName", "Last Name", 30).
					editStringField("agency.name", "Name", 20).
					editStringField("agency.phone", "Name", 20).
					editStringField("agency.city.name", "City", 20)
			);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"Criteria getEmployeesCriteria() {",
			"	Session session = (Session)entityManager.getDelegate();",
			"	Criteria c = session.createCriteria(Employee.class);",
			"	if (employeeLastNameCriterion != null && employeeLastNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"lastName\", employeeLastNameCriterion+\"%\"));",
			"	if (employeeFirstNameCriterion != null && employeeFirstNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"firstName\", employeeFirstNameCriterion+\"%\"));",
			"	if ((employeeAgencyCityNameCriterion != null && employeeAgencyCityNameCriterion.length()>0) ||",
			"		(employeeAgencyNameCriterion != null && employeeAgencyNameCriterion.length()>0) ||",
			"		(employeeAgencyPhoneCriterion != null && employeeAgencyPhoneCriterion.length()>0)) {",
			"		Criteria c1 = c.createCriteria(\"agency\");",
			"		if (employeeAgencyPhoneCriterion != null && employeeAgencyPhoneCriterion.length()>0)",
			"			c1.add(Restrictions.like(\"phone\", employeeAgencyPhoneCriterion+\"%\"));",
			"		if (employeeAgencyNameCriterion != null && employeeAgencyNameCriterion.length()>0)",
			"			c1.add(Restrictions.like(\"name\", employeeAgencyNameCriterion+\"%\"));",
			"		if (employeeAgencyCityNameCriterion != null && employeeAgencyCityNameCriterion.length()>0) {",
			"			Criteria c2 = c1.createCriteria(\"city\");",
			"			c2.add(Restrictions.like(\"name\", employeeAgencyCityNameCriterion+\"%\"));",
			"		}",
			"	}",
			"	return c;",
			"}"
		);
	}

	
	public void testMinimalCriteria() {
		Seam.clear();
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 30).
			showField("lastName", "Last Name", 30);
		TransientCriteriaDescriptor criteria = new TransientCriteriaDescriptor().
			setForm(
				new FormDescriptor().
					editStringField("firstName", "First Name", 30)
			);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"Criteria getEmployeesCriteria() {",
			"	Session session = (Session)entityManager.getDelegate();",
			"	Criteria c = session.createCriteria(Employee.class);",
			"	if (employeeFirstNameCriterion != null && employeeFirstNameCriterion.length()>0)",
			"		c.add(Restrictions.like(\"firstName\", employeeFirstNameCriterion+\"%\"));",
			"	return c;",
			"}"
		);
	}
	
}

