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
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation.createUniDirectionalOneToMany;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * Test the generation of the one to many relations
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceOneToMany extends TestCase {

	private GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	public void testUnidirectional() throws Exception {
		UMLClass leftEntity = new UMLClass("Trainer");
		UMLClass rightEntity = new UMLClass("Tiger");
		
		String rightRole = "trainedTigers";
		UMLRelation relation = createUniDirectionalOneToMany(leftEntity, rightEntity, rightRole);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(leftEntity);
		classes.add(rightEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedClassesCode = null;
		generatedClassesCode = service.generateHibernateCode(classes, relations, TestUtil.packageName);
		
		In(generatedClassesCode).
			theCodeOfClass(leftEntity.getName()).
			contains(
				"import javax.persistence.OneToMany;").
			contains(
				"@OneToMany",
				"List<Tiger> trainedTigers;").
			contains(
				"public Trainer(boolean dummy) {",
				"	this.trainedTigers = new ArrayList<Tiger>();").
			contains(
				"public static Trainer createTrainer() {",
				"	Trainer trainer = new Trainer();",
				"	trainer.trainedTigers = new ArrayList<Tiger>();",
				"	return trainer;").
			contains(
				"public List<Tiger> getTrainedTigers() {",
				"	return Collections.unmodifiableList(trainedTigers);").
			contains(
				"public List<Tiger> getTrainedTigers() {",
				"	return Collections.unmodifiableList(trainedTigers);").
			verify();
	}

	public void testBidirectional() throws Exception {
		UMLClass leftEntity = new UMLClass("Troop");
		UMLClass rightEntity = new UMLClass("Soldier");
		
		UMLRelation relation = new UMLRelation(ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("1");
		relation.setLeftConstraint("");
		relation.setLeftRole("troop");
		relation.setLeftStereotype("<<owner>>");
		relation.setLeftTarget(leftEntity);
		
		relation.setRightAdornment(LinkAdornment.NONE);
		relation.setRightCardinality("*");
		relation.setRightConstraint("");
		relation.setRightRole("soldiers");
		relation.setRightStereotype("");
		relation.setRightTarget(rightEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(leftEntity);
		classes.add(rightEntity);
		relations.add(relation);
		
		try {
			service.generateHibernateCode(classes, relations, TestUtil.packageName);
			fail("Exception expected");
		} catch (UMLException e) {
		}
	}
}
