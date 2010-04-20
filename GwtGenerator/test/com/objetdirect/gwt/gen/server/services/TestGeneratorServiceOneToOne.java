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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.objetdirect.gwt.TestUtil;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.UMLComponentException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLLink.LinkKind;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;

/**
 * Test the generation of the one to one relations
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceOneToOne extends TestCase {

	private GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	public void testOneToOneUnidirectionnalReference() {
		UMLClass leftEntity = new UMLClass("LeftEntity");
		UMLClass rightEntity = new UMLClass("RightEntity");
		
		UMLRelation relation = new UMLRelation(LinkKind.ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("");
		relation.setLeftConstraint("");
		relation.setLeftRole("");
		relation.setLeftTarget(leftEntity);
		
		relation.setRightAdornment(LinkAdornment.WIRE_ARROW);
		relation.setRightCardinality("1");
		relation.setRightConstraint("");
		relation.setRightRole("link");
		relation.setRightTarget(rightEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(leftEntity);
		classes.add(rightEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedCode = null;
		try {
			generatedCode = service.generateClassesCode(classes, relations, TestUtil.packageName);
		} catch (UMLComponentException e) {
			fail();
		}
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"import javax.persistence.OneToOne;");

		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"public LeftEntity(boolean dummy) {",
				"	this.link = null;",
				"}");
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"@OneToOne",
				"RightEntity link;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"public RightEntity getLink() {",
				"	return link;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"public void setLink(RightEntity link) {",
				"	this.link = link;");
	}
	
	
	public void testOneToOneUnidirectionnalCompositionReference() {
		UMLClass leftEntity = new UMLClass("LeftEntity");
		UMLClass rightEntity = new UMLClass("RightEntity");
		
		UMLRelation relation = new UMLRelation(LinkKind.COMPOSITION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.INVERTED_SOLID_DIAMOND);
		relation.setLeftCardinality("");
		relation.setLeftConstraint("");
		relation.setLeftRole("");
		relation.setLeftTarget(leftEntity);
		
		relation.setRightAdornment(LinkAdornment.WIRE_ARROW);
		relation.setRightCardinality("1");
		relation.setRightConstraint("");
		relation.setRightRole("link");
		relation.setRightTarget(rightEntity);

		List<UMLClass> classes = new ArrayList<UMLClass>();
		List<UMLRelation> relations = new ArrayList<UMLRelation>();
		
		classes.add(leftEntity);
		classes.add(rightEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedCode = null;
		try {
			generatedCode = service.generateClassesCode(classes, relations, TestUtil.packageName);
		} catch (UMLComponentException e) {
			fail();
		}
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
		"import javax.persistence.OneToOne;");

		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"public LeftEntity(boolean dummy) {",
				"	this.link = null;",
				"}");
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"@OneToOne(cascade={CascadeType.REMOVE})",
				"RightEntity link;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"public RightEntity getLink() {",
				"	return link;");
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"public void setLink(RightEntity link) {",
				"	this.link = link;");
	}
	
	
	
	public void testOneToOneBidirectionnalReference() {
		UMLClass leftEntity = new UMLClass("LeftEntity");
		UMLClass rightEntity = new UMLClass("RightEntity");
		
		UMLRelation relation = new UMLRelation(LinkKind.ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("1");
		relation.setLeftStereotype("<<owner>>");
		relation.setLeftRole("rlink");
		relation.setLeftTarget(leftEntity);
		
		relation.setRightAdornment(LinkAdornment.NONE);
		relation.setRightCardinality("1");
		relation.setRightConstraint("");
		relation.setRightRole("link");
		relation.setRightTarget(rightEntity);

		List<UMLClass> classes = new ArrayList<UMLClass>();
		List<UMLRelation> relations = new ArrayList<UMLRelation>();
		
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
				"import javax.persistence.OneToOne;"
			);
		
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
			    "@OneToOne",
			    "RightEntity link;",
			    "@Transient",
			    "boolean inDeletion = false;"
		    );
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
			"public void setLink(RightEntity link) {",
	        "	if (this.link == link)",
            "		return;",
	        "	if (link != null && link.rlink != null)",
            "		link.rlink.link = null;",
	        "	if (this.link != null)",
            "		this.link.rlink = null;",
        	"	this.link = link;",
	        "	if (link != null)",
        	"		link.rlink = this;"
	        );
		
		TestUtil.assertExist(leftEntity.getName(), generatedClassesCode,
		    "@PreRemove",
		    "void preRemove() {",
	        "	if (!inDeletion) {",
            "		inDeletion = true;",
            "		if (this.link != null && !this.link.inDeletion)",
            "			this.link.rlink = null;"
            );
	}
}
