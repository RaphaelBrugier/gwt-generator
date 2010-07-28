/*
 * This file is part of the Gwt-Generator project and was written by Raphaël Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.server.services;

import static com.objetdirect.gwt.gen.AssertGeneratedCode.In;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkKind.ASSOCIATION_RELATION;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * Test the generation of the many to many relations
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceManyToMany extends TestCase {

	private GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	public void testUnidirectional() throws Exception {
		UMLClass storeEntity = new UMLClass("Store");
		UMLClass cityEntity = new UMLClass("City");
		
		UMLRelation relation = new UMLRelation(ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("*");
		relation.setLeftConstraint("");
		relation.setLeftRole("");
		relation.setLeftStereotype("");
		relation.setLeftTarget(storeEntity);
		
		relation.setRightAdornment(LinkAdornment.WIRE_ARROW);
		relation.setRightCardinality("*");
		relation.setRightConstraint("");
		relation.setRightRole("localizations");
		relation.setRightStereotype("");
		relation.setRightTarget(cityEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(storeEntity);
		classes.add(cityEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedClassesCode = null;
	
		generatedClassesCode = service.generateHibernateCode(classes, relations, TestUtil.packageName);
		
		In(generatedClassesCode).
			theCodeOfClass(storeEntity.getName()).
			contains(
				"import javax.persistence.ManyToMany;").
			contains(
				"@ManyToMany",
				"List<City> localizations;").
			contains(
				"public Store(boolean dummy) {",
	        	"	this.localizations = new ArrayList<City>();").
	        contains(
        		"public static Store createStore() {",
    			"	Store store = new Store();",
    			"	store.localizations = new ArrayList<City>();",
    			"	return store;").	
			verify();
	}
	
	public void testBidirectional() throws Exception {
		UMLClass storeEntity = new UMLClass("Store");
		UMLClass customerEntity = new UMLClass("Customer");
		
		UMLRelation relation = new UMLRelation(ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("*");
		relation.setLeftConstraint("");
		relation.setLeftRole("stores");
		relation.setLeftStereotype("<<owner>>");
		relation.setLeftTarget(storeEntity);
		
		relation.setRightAdornment(LinkAdornment.NONE);
		relation.setRightCardinality("*");
		relation.setRightConstraint("");
		relation.setRightRole("customers");
		relation.setRightStereotype("");
		relation.setRightTarget(customerEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(storeEntity);
		classes.add(customerEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedClassesCode = null;
		generatedClassesCode = service.generateHibernateCode(classes, relations, TestUtil.packageName);
		
		In(generatedClassesCode).
			theCodeOfClass(storeEntity.getName()).
			contains(
				"import javax.persistence.ManyToMany;").
			contains(
				"@ManyToMany",
				"List<Customer> customers;",
				"@Transient",
				"boolean inDeletion = false;").
			contains(
				"public Store(boolean dummy) {",
				"	this.customers = new ArrayList<Customer>();").
			contains(
				"import javax.persistence.ManyToMany;").
			contains(
				"public static Store createStore() {",
				"	Store store = new Store();",
				"	store.customers = new ArrayList<Customer>();",
				"	return store;").
			verify();
		
		In(generatedClassesCode).
		theCodeOfClass(customerEntity.getName()).
			contains(
				"import javax.persistence.ManyToMany;").
			contains(
				"@ManyToMany(mappedBy=\"customers\")",
				"List<Store> stores;",
				"@Transient",
				"boolean inDeletion = false;").
			contains(
				"public Customer(boolean dummy) {",
		        "	this.stores = new ArrayList<Store>();").
	        contains(
        		"public static Customer createCustomer() {",
        		"	Customer customer = new Customer();",
        		"	customer.stores = new ArrayList<Store>();",
	        	"	return customer;").
			verify();
	}
}
