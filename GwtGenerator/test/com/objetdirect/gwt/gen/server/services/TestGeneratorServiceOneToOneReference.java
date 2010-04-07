package com.objetdirect.gwt.gen.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.objetdirect.gwt.TestUtil;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLLink.LinkKind;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;

public class TestGeneratorServiceOneToOneReference extends TestCase {

	GeneratorServiceImpl service = new GeneratorServiceImpl();
	
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

		List<UMLClass> classes = new ArrayList<UMLClass>();
		List<UMLRelation> relations = new ArrayList<UMLRelation>();
		
		classes.add(leftEntity);
		classes.add(rightEntity);
		relations.add(relation);
		
		Map<String, List<String>> generatedCode = service.generateClassCode(classes, relations, TestUtil.packageName);
		
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
		
		Map<String, List<String>> generatedCode = service.generateClassCode(classes, relations, TestUtil.packageName);
		
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
		relation.setLeftConstraint("{owner}");
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
		
		Map<String, List<String>> generatedCode = service.generateClassCode(classes, relations, TestUtil.packageName);
	
//		for (String className : generatedCode.keySet()) {
//			TestUtil.println(generatedCode.get(className));
//		}
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
				"import javax.persistence.OneToOne;"
			);
		
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
			    "@OneToOne",
			    "RightEntity link;",
			    "@Transient",
			    "boolean inDeletion = false;"
		    );
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
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
		
		TestUtil.assertExist(leftEntity.getName(), generatedCode,
		    "@PreRemove",
		    "void preRemove() {",
	        "	if (!inDeletion) {",
            "		inDeletion = true;",
            "		if (this.link != null && !this.link.inDeletion)",
            "			this.link.rlink = null;"
            );
	}
}
