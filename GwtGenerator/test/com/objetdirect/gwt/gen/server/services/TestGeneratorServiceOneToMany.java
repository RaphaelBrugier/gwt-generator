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

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.UMLComponentException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLLink.LinkKind;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;

/**
 * Test the generation of the one to many relations
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceOneToMany extends TestCase {

	private GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	public void testUnidirectional() {
		UMLClass leftEntity = new UMLClass("Trainer");
		UMLClass rightEntity = new UMLClass("Tiger");
		
		UMLRelation relation = new UMLRelation(LinkKind.ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("1");
		relation.setLeftConstraint("");
		relation.setLeftRole("");
		relation.setLeftStereotype("");
		relation.setLeftTarget(leftEntity);
		
		relation.setRightAdornment(LinkAdornment.WIRE_ARROW);
		relation.setRightCardinality("*");
		relation.setRightConstraint("");
		relation.setRightRole("trainedTigers");
		relation.setRightStereotype("");
		relation.setRightTarget(rightEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(leftEntity);
		classes.add(rightEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedClassesCode = null;
		try {
			generatedClassesCode = service.generateClassesCode(classes, relations, TestUtil.packageName);
		} catch (UMLComponentException e) {
			fail();
		}
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
			"import javax.persistence.OneToMany;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
			"@OneToMany",
			"List<Tiger> trainedTigers;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
				"public Trainer(boolean dummy) {",
				"	this.trainedTigers = new ArrayList<Tiger>();");
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
				"public static Trainer createTrainer() {",
				"	Trainer trainer = new Trainer();",
				"	trainer.trainedTigers = new ArrayList<Tiger>();",
				"	return trainer;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
				"public List<Tiger> getTrainedTigers() {",
				"	return Collections.unmodifiableList(trainedTigers);");
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
				"public List<Tiger> getTrainedTigers() {",
				"	return Collections.unmodifiableList(trainedTigers);");
	}
	
	public void testBidirectional() {
		UMLClass leftEntity = new UMLClass("Troop");
		UMLClass rightEntity = new UMLClass("Soldier");
		
		UMLRelation relation = new UMLRelation(LinkKind.ASSOCIATION_RELATION);
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
			service.generateClassesCode(classes, relations, TestUtil.packageName);
			fail("Exception expected");
		} catch (UMLComponentException e) {
		}
	}
}
