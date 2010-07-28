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

package com.objetdirect.entities;

import com.objetdirect.engine.TestUtil;

import junit.framework.TestCase;

public class TestOneToOneReference extends TestCase {

	public void testOneToOneUnidirectionnalReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref = 
			new OneToOneReferenceDescriptor(e1, e2, "link", false, false, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.link = null;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@OneToOne",
			"RightEntity link;");		
		TestUtil.assertText(ref.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	this.link = link;",
			"}");
		TestUtil.assertNotExisting(e1.getText(), "preRemove");
		
		//TODO add a test to check if two default constructors are generated 
		
//		TestUtil.println(e1.getText());
//		TestUtil.println(e2.getText());
	}

	public void testOneToOneUnidirectionnalMandatoryReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref = 
			new OneToOneReferenceDescriptor(e1, e2, "link", true, false, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity(RightEntity link) {",
			"	setLink(link);",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@OneToOne",
			"@NotNull",
			"RightEntity link;");		
		TestUtil.assertText(ref.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	if (link == null)",
			"		throw new FieldRequiredException(this, \"link\");",
			"	this.link = link;",
			"}");
		TestUtil.assertNotExisting(e1.getText(), "preRemove");
		
	}

	public void testOneToOneUnidirectionnalCompositionReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref = 
			new OneToOneReferenceDescriptor(e1, e2, "link", false, true, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.link = null;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.CascadeType;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@OneToOne(cascade={CascadeType.REMOVE})",
			"RightEntity link;");		
		TestUtil.assertText(ref.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	this.link = link;",
			"}");
		TestUtil.assertNotExisting(e1.getText(), "preRemove");
		
//		System.out.println("\n\n\n");
//		TestUtil.println(e1.getText());
//		TestUtil.println(e2.getText());
	}

	public void testOneToOneUnidirectionnalMandatoryCompositionReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref = 
			new OneToOneReferenceDescriptor(e1, e2, "link", true, true, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity(RightEntity link) {",
			"	setLink(link);",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.CascadeType;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@OneToOne(cascade={CascadeType.REMOVE})",
			"@NotNull",
			"RightEntity link;");		
		TestUtil.assertText(ref.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	if (link == null)",
			"		throw new FieldRequiredException(this, \"link\");",
			"	this.link = link;",
			"}");
		TestUtil.assertNotExisting(e1.getText(), "preRemove");
		
//		System.out.println("\n\n\n");
//		TestUtil.println(e1.getText());
//		TestUtil.println(e2.getText());
	}

	public void testOneToOneBidirectionnalReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref1 = 
			new OneToOneReferenceDescriptor(e1, e2, "link", false, false, false);
		OneToOneReferenceDescriptor ref2 = 
			new OneToOneReferenceDescriptor(e2, e1, "rlink", false, false, false);
		ref1.setReverse(ref2, true);
		ref2.setReverse(ref1, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.link = null;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.Transient;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.PreRemove;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@OneToOne",
			"RightEntity link;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@OneToOne(mappedBy=\"link\")",
			"LeftEntity rlink;");		
		TestUtil.assertText(ref1.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref1.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	if (this.link == link)",
		    "		return;",
		    "	if (link != null && link.rlink != null)",
		    "		link.rlink.link = null;",
		    "	if (this.link != null)",
		    "		this.link.rlink = null;",
		    "	this.link = link;",
		    "	if (link != null)",
		    "		link.rlink = this;",
			"}");
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (this.link != null && !this.link.inDeletion)",
			"			this.link.rlink = null;",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
		
//		System.out.println("\n\n\n");
//		TestUtil.println(e1.getText());
//		TestUtil.println(e2.getText());
	}

	public void testOneToOneBidirectionnalMandatoryReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref1 = 
			new OneToOneReferenceDescriptor(e1, e2, "link", true, true, false);
		OneToOneReferenceDescriptor ref2 = 
			new OneToOneReferenceDescriptor(e2, e1, "rlink", false, false, false);
		ref1.setReverse(ref2, false);
		ref2.setReverse(ref1, true);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity(RightEntity link) {",
			"	setLink(link);",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import com.objetdirect.frmk.FieldRequiredException;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.CascadeType;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.Transient;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.PreRemove;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@OneToOne(cascade={CascadeType.REMOVE}, mappedBy=\"rlink\")",
			"@NotNull",
			"RightEntity link;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@OneToOne",
			"LeftEntity rlink;");		
		TestUtil.assertText(ref1.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref1.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	if (link == null)",
			"		throw new FieldRequiredException(this, \"link\");",
			"	if (this.link == link)",
		    "		return;",
		    "	if (link.rlink != null)",
		    "		link.rlink.link = null;",
		    "	if (this.link != null)",
		    "		this.link.rlink = null;",
		    "	this.link = link;",
		    "	link.rlink = this;",
			"}");
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (!this.link.inDeletion)",
			"			this.link.rlink = null;",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
		
//		System.out.println("\n\n\n");
//		TestUtil.println(e1.getText());
//		System.out.println("\n");
//		TestUtil.println(e2.getText());
		
	}

	public void testOneToOneBidirectionnalReverseMandatoryReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToOneReferenceDescriptor ref1 = 
			new OneToOneReferenceDescriptor(e1, e2, "link", false, false, false);
		OneToOneReferenceDescriptor ref2 = 
			new OneToOneReferenceDescriptor(e2, e1, "rlink", true, false, false);
		ref1.setReverse(ref2, true);
		ref2.setReverse(ref1, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.link = null;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import com.objetdirect.frmk.FieldRequiredException;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToOne;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.Transient;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.PreRemove;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@OneToOne",
			"RightEntity link;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@OneToOne(mappedBy=\"link\")",
			"@NotNull",
			"LeftEntity rlink;");		
		TestUtil.assertText(ref1.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref1.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	if (this.link == link)",
		    "		return;",
		    "	if (this.link != null)",
		    "		throw new FieldRequiredException(this.link, \"rlink\");",
		    "	if (link != null && link.rlink != null)",
		    "		link.rlink.link = null;",
		    "	this.link = link;",
		    "	if (link != null)",
		    "		link.rlink = this;",
			"}");
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (this.link != null && !this.link.inDeletion)",
		    "			throw new FieldRequiredException(this.link, \"rlink\");",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
	}
}
