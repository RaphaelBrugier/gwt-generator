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

import java.util.Map;

import junit.framework.TestCase;

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.ConstraintDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.UnicityDescriptor;
import com.objetdirect.seam.lists.SelectOneDetailDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

public class TestConstraintManager extends TestCase {

	public void testInvolvedEntitiesLookup() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		EntityDescriptor address = new EntityDescriptor("com.objetdirect.domain", "Address").
			addStringField("number", null).
			addStringField("street", null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null);
		employee.addOneToOne(address, "address", false, true, false);
		employee.addManyToOne(agency, "agency", false, false);
		agency.addOneToOne(address, "address", false, true, false);
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 20).
			showField("lastName", "Last Name", 20);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		FormDescriptor form = new FormDescriptor().
			editStringField("firstName", "Prenom de l'employe", 20).
			editStringField("lastName", "Nom de l'employe", 20).
			editStringField("address.street", "Adresse de l'employe", 30).
			editStringField("agency.name", "Nom de l'agence", 30).
			editStringField("agency.phone", "Telephone", 10).
			editStringField("agency.address.street", "Adresse de l'agence", 30);
		detail.addForm(form);
		ConstraintManager constraintManager = new ConstraintManager(null, form.getFields());
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		Map<String, EntityDescriptor> entities = constraintManager.getInvolvedEntities();
		assertEquals(4, entities.size());
		assertEquals(address, entities.get("address"));
		assertEquals(address, entities.get("agency.address"));
		assertEquals(employee, entities.get(""));
		assertEquals(agency, entities.get("agency"));
	}

	public void testFilteredEntitiesLookup() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		agency.addUniciyConstraint("Name already used", "name");
		EntityDescriptor address = new EntityDescriptor("com.objetdirect.domain", "Address").
			addStringField("number", null).
			addStringField("street", null).
			addStringField("city", null);
		address.addUniciyConstraint("Number and street already used", "number", "street");
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null);
		employee.addUniciyConstraint("Last name already used", "lastName");
		employee.addOneToOne(address, "address", false, true, false);
		employee.addManyToOne(agency, "agency", false, false);
		agency.addOneToOne(address, "address", false, true, false);
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 20).
			showField("lastName", "Last Name", 20);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		FormDescriptor form = new FormDescriptor().
			editStringField("firstName", "Prenom de l'employe", 20).
			editStringField("lastName", "Nom de l'employe", 20).
			editStringField("address.street", "Adresse de l'employe", 30).
			editStringField("agency.phone", "Telephone", 10).
			editStringField("agency.address.city", "Adresse de l'agence", 30);
		detail.addForm(form);
		ConstraintManager constraintManager = new ConstraintManager(null, form.getFields());
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		Map<String, EntityDescriptor> entities = constraintManager.filterInvolvedEntities();
		assertEquals(2, entities.size());
		assertEquals(address, entities.get("address"));
		assertEquals(employee, entities.get(""));
	}

	public void testConstraintParams() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		EntityDescriptor address = new EntityDescriptor("com.objetdirect.domain", "Address").
			addStringField("number", null).
			addStringField("street", null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null);
		employee.addOneToOne(address, "address", false, true, false);
		employee.addManyToOne(agency, "agency", false, false);
		agency.addOneToOne(address, "address", false, true, false);

		ConstraintDescriptor c1 = new UnicityDescriptor("Le nom n'est pas unique",
			employee.getMember("firstName"),
			employee.getMember("lastName")
		);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 20).
			showField("lastName", "Last Name", 20);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		FormDescriptor form = new FormDescriptor().
			editStringField("lastName", "Nom de l'employe", 20).
			editStringField("address.street", "Adresse de l'employe", 30).
			editStringField("agency.name", "Nom de l'agence", 30).
			editStringField("agency.phone", "Telephone", 10).
			editStringField("agency.address.street", "Adresse de l'agence", 30);
		detail.addForm(form);
		ConstraintManager constraintManager = new ConstraintManager(null, form.getFields());
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		assertEquals(
			"currentEmployee.getFirstName(), this.currentEmployeeLastName",
			constraintManager.getParams("", c1, "this", "currentEmployee"));
	}

	public void testValidationText() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		agency.addUniciyConstraint("Name already used", "name");
		EntityDescriptor address = new EntityDescriptor("com.objetdirect.domain", "Address").
			addStringField("number", null).
			addStringField("street", null).
			addStringField("city", null);
		address.addUniciyConstraint("Number and street already used", "number", "street");
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null);
		employee.addUniciyConstraint("Last name already used", "lastName");
		employee.addOneToOne(address, "address", false, true, false);
		employee.addManyToOne(agency, "agency", false, false);
		agency.addOneToOne(address, "address", false, true, false);
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(employee).
			showField("firstName", "First Name", 20).
			showField("lastName", "Last Name", 20);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		FormDescriptor form = new FormDescriptor().
			editStringField("firstName", "Prenom de l'employe", 20).
			editStringField("lastName", "Nom de l'employe", 20).
			editStringField("address.street", "Adresse de l'employe", 30).
			editStringField("agency.phone", "Telephone", 10).
			editStringField("agency.address.city", "Adresse de l'agence", 30);
		detail.addForm(form);
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"import javax.faces.application.FacesMessage;"
		);
		TestUtil.assertExists(page.getJavaText(),
			"import javax.faces.context.FacesContext;"
		);
		TestUtil.assertExists(page.getJavaText(),
			"boolean isCurrentEmployeeValid() {",
			"	String message;",
			"	message = currentEmployee.verifyLastNameUnicity(entityManager, this.currentEmployeeLastName);",
			"	if (message!=null)",
			"		FacesContext.getCurrentInstance().addMessage(\"\", new FacesMessage(message));",
			"	if (currentEmployee.getAddress() != null) {",
			"		Address currentEmployeeAddress = currentEmployee.getAddress();",
			"		message = currentEmployeeAddress.verifyNumberAndStreetUnicity(entityManager, currentEmployeeAddress.getNumber(), this.currentEmployeeAddressStreet);",
			"		if (message!=null)",
			"			FacesContext.getCurrentInstance().addMessage(\"\", new FacesMessage(message));",
			"	}",
			"	return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"}"
		);
	}
	 
	public void testConstraintOnRelationship() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		EntityDescriptor address = new EntityDescriptor("com.objetdirect.domain", "Address").
			addStringField("number", null).
			addStringField("street", null).
			addStringField("city", null);
		agency.addOneToOne(address, "address", false, true, false);
		agency.addUniciyConstraint("Address already used", "address");
		
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-employees");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("address.street", "Street", 20);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		FormDescriptor form = new FormDescriptor().
			editStringField("name", "Nom de l'agence", 20).
			editEntityField("address", "Address", address, "street", 20);
		detail.addForm(form);
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getJavaText(),
			"public void validateCurrentAgency() {",
			"	if (isCurrentAgencyValid()) {",
			"		currentAgency.setName(currentAgencyName);",
			"		currentAgency.setAddress(currentAgencyAddress);",
			"		clearCurrentAgency();",
			"	}",
			"}"
		);
	}
}
