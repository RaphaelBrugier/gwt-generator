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
import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

public class TestDynamicCriteria extends TestCase {

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
		TransientDynamicCriteriaDescriptor criteria = new TransientDynamicCriteriaDescriptor().
			editStringField("firstName", "First Name", 30).
			editStringField("lastName", "Last Name", 30).
			editStringField("agency.name", "Agency", 20);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Employee;",
			"import com.objetdirect.jsffrmk.Criterion;",
			"import java.util.ArrayList;",
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
			"	List<Criterion> selectedCriteria = new ArrayList<Criterion>();",
			"	Criterion criteriaFirstName = new Criterion(\"First Name\", \"First Name : \", \"text\").setSize(30);",
			"	Criterion criteriaLastName = new Criterion(\"Last Name\", \"Last Name : \", \"text\").setSize(30);",
			"	Criterion criteriaAgencyName = new Criterion(\"Agency\", \"Agency : \", \"text\").setSize(20);",
			"	List<Criterion> criteria = null;",
			"	Criterion selectedCriterion = null;",
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
			"	public String getCriteriaFirstName() {",
			"		if (!selectedCriteria.contains(criteriaFirstName))",
			"			return null;",
			"		return (String)criteriaFirstName.getValue();",
			"	}",
			"	",
			"	public void setCriteriaFirstName(String firstName) {",
			"		this.criteriaFirstName.setValue(firstName);",
			"	}",
			"	",
			"	public String getCriteriaLastName() {",
			"		if (!selectedCriteria.contains(criteriaLastName))",
			"			return null;",
			"		return (String)criteriaLastName.getValue();",
			"	}",
			"	",
			"	public void setCriteriaLastName(String lastName) {",
			"		this.criteriaLastName.setValue(lastName);",
			"	}",
			"	",
			"	public String getCriteriaAgencyName() {",
			"		if (!selectedCriteria.contains(criteriaAgencyName))",
			"			return null;",
			"		return (String)criteriaAgencyName.getValue();",
			"	}",
			"	",
			"	public void setCriteriaAgencyName(String name) {",
			"		this.criteriaAgencyName.setValue(name);",
			"	}",
			"	",
			"	public List<Criterion> getSelectedCriteria() {",
			"		return selectedCriteria;",
			"	}",
			"	",
			"	List<Criterion> getCriteria() {",
			"		if (criteria==null) {",
			"			criteria = new ArrayList<Criterion>();",
			"			criteria.add(criteriaFirstName);",
			"			criteria.add(criteriaLastName);",
			"			criteria.add(criteriaAgencyName);",
			"		}",
			"		return criteria;",
			"	}",
			"	",
			"	List<Criterion> getRemainingCriteria() {",
			"		List<String> result = new ArrayList<String>();",
			"		for (Criterion c : getCriteria()) {",
			"			if (!selectedCriteria.contains(c))",
			"				result.add(c.getName());",
			"		}",
			"		return result;",
			"	}",
			"	",
			"	public Criterion getSelectedCriterion() {",
			"		return selectedCriterion;",
			"	}",
			"	",
			"	public void setSelectedCriterion(Criterion selectedCriterion) {",
			"		this.selectedCriterion = selectedCriterion;",
			"	}",
			"	",
			"	Criterion getCriterion(String criterionName) {",
			"		for (Criterion criterion : getCriteria()) {",
			"			if (criterion.getName().equals(criterionName))",
			"				return criterion;",
			"		}",
			"		return null;",
			"	}",
			"	",
			"	public void addCriterion() {",
			"		if (selectedCriterion!=null) {",
			"			selectedCriteria.add(getCriterion(selectedCriterion));",
			"			selectedCriterion = null;",
			"		}",
			"	}",
			"	",
			"	public void removeCriterion(Criterion criterion) {",
			"		if (criterion!=null) {",
			"			selectedCriteria.remove(criterion);",
			"		}",
			"	}",
			"	",
			"	public void validateEmployeesCriteria() {",
			"		employees = null;",
			"	}",
			"	",
			"	public void clearEmployeesCriteria() {",
			"		criteriaFirstName.setValue(null);",
			"		criteriaLastName.setValue(null);",
			"		criteriaAgencyName.setValue(null);",
			"	}",
			"	",
			"	Criteria getEmployeesCriteria() {",
			"		Session session = (Session)entityManager.getDelegate();",
			"		Criteria c = session.createCriteria(Employee.class);",
			"		if (getCriteriaLastName() != null && getCriteriaLastName().length()>0)",
			"			c.add(Restrictions.like(\"lastName\", getCriteriaLastName()+\"%\"));",
			"		if (getCriteriaFirstName() != null && getCriteriaFirstName().length()>0)",
			"			c.add(Restrictions.like(\"firstName\", getCriteriaFirstName()+\"%\"));",
			"		if (getCriteriaAgencyName() != null && getCriteriaAgencyName().length()>0) {",
			"			Criteria c1 = c.createCriteria(\"agency\");",
			"			c1.add(Restrictions.like(\"name\", getCriteriaAgencyName()+\"%\"));",
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
			"					<h:panelGroup>",
			"						<s:validateAll>",
			"							<h:panelGroup>",
			"								<h:outputText value=\"Liste des criteres : \" />",
			"								<s:decorate template=\"/layout/edit.xhtml\">",
			"									<h:selectOneMenu value=\"#{editEmployees.selectedCriterion}\">",
			"										<s:selectItems value=\"#{editEmployees.remainingCriteria}\" var=\"criterionName\"  label=\"#{criterionName}\" noSelectionLabel=\"Choisissez...\"/>",
			"									</h:selectOneMenu>",
			"									<h:commandButton value=\"ajouter\" action=\"#{editEmployees.addCriterion}\"/>",
			"								</s:decorate>",
			"							</h:panelGroup>",
			"							<h:panelGrid columns=\"1\">",
			"								<ui:repeat value=\"#{editEmployees.selectedCriteria}\" var=\"criterion\">",
			"									<h:panelGrid columns=\"3\">",
			"										<h:outputText value=\"#{criterion.label}\"/>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='text'}\">",
			"											<h:inputText value=\"#{criterion.value}\" size=\"#{criterion.size}\"/>",
			"										</s:decorate>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='number'}\">",
			"											<h:inputText value=\"#{criterion.value}\" size=\"#{criterion.size}\">",
			"												<f:convertNumber pattern=\"#{criterion.pattern}\" />",
			"											</h:inputText>",
			"										</s:decorate>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='enumList'}\">",
			"											<h:selectOneMenu value=\"#{criterion.value}\">",
			"												<s:selectItems value=\"#{criterion.items}\" var=\"type\"  label=\"#{type.label}\" noSelectionLabel=\"Choisissez...\"/>",
			"												<s:convertEnum />",
			"											</h:selectOneMenu>",
			"										</s:decorate>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='enumRadio'}\">",
			"											<h:selectOneRadio value=\"#{criterion.value}\">",
			"												<s:selectItems value=\"#{criterion.items}\" var=\"type\"  label=\"#{type.label}\" noSelectionLabel=\"Choisissez...\"/>",
			"												<s:convertEnum />",
			"											</h:selectOneRadio>",
			"										</s:decorate>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='entity'}\">",
			"											<h:selectOneMenu value=\"#{criterion.value}\">",
			"												<s:selectItems value=\"#{criterion.items}\" var=\"entity\"  label=\"#{criterion.line}\" noSelectionLabel=\"Choisissez...\"/>",
			"												<s:convertEntity />",
			"											</h:selectOneMenu>",
			"										</s:decorate>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='boolean'}\">",
			"											<h:selectBooleanCheckbox value=\"#{criterion.value}\"/>",
			"										</s:decorate>",
			"										<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='date'}\">",
			"											<ice:selectInputDate value=\"#{criterion.value}\" renderAsPopup=\"true\">",
			"												<f:convertDateTime pattern=\"#{criterion.pattern}\"/>",
			"											</ice:selectInputDate>",
			"										</s:decorate>",
			"									<h:commandButton value=\"del\" action=\"#{editEmployees.removeCriterion(criterion)}\"/>",
			"								</h:panelGrid>",
			"							</ui:repeat>",
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

	public void testOtherFieldTypesInCriteria() {
		Seam.clear();
		EnumDescriptor employeeType = new EnumDescriptor("com.objetdirect.domain", "EmployeeType").
			addConstant("manager", "Manager").
			addConstant("engineer", "Engineer");
		EntityDescriptor city = new EntityDescriptor("com.objetdirect.domain", "City").
			addStringField("name", null);
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addManyToOne(city, "city", true, false);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addEnumField("type", employeeType, null).
			addDateField("birthday", null).
			addIntField("rank", null).
			addManyToOne(agency, "agency", true, false);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 30).
			showField("lastName", "Last Name", 30).
			showField("agency.name", "Agency", 20);
		TransientDynamicCriteriaDescriptor criteria = new TransientDynamicCriteriaDescriptor().
			editDateField("birthday", "Birthday", "dd/MM/yyyy").
			editNumberField("rank", "Rank", "###", 5).
			editEnumField("type", "Type", employeeType, 20).
			editEntityField("agency", "Agency", agency, "name", 20);
		feature.setCriteria(criteria);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"Criterion criteriaBirthday = new Criterion(\"Birthday\", \"Birthday : \", \"date\").setPattern(\"dd/MM/yyyy\");",
			"Criterion criteriaRank = new Criterion(\"Rank\", \"Rank : \", \"number\").setSize(5).setPattern(\"###\");",
			"Criterion criteriaType = new Criterion(\"Type\", \"Type : \", \"enumList\").setItems(\"#{editEmployees.employeeTypes}\");",
			"Criterion criteriaAgency = new Criterion(\"Agency\", \"Agency : \", \"entity\").setItems(\"#{editEmployees.agencies}\").setLine(\"#{entity.name}\");"
		);
		TestUtil.assertExists(page.getJavaText(),
			"public EmployeeType[] getEmployeeTypes() {",
			"	return EmployeeType.values();",
			"}"
		);
		TestUtil.assertExists(page.getJavaText(),
			"@SuppressWarnings(\"unchecked\")",
			"public List<Agency> getAgencies() {",
			"	if (agencies==null) {",
			"		agencies = entityManager.createQuery(\"from Agency\").getResultList();",
			"	}",
			"	return agencies;",
			"}"
		);
	}
		
}

