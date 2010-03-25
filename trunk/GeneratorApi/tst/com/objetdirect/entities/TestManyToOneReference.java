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

public class TestManyToOneReference extends TestCase {

	public void testManyToOneUnidirectionnalReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		ManyToOneReferenceDescriptor ref = 
			new ManyToOneReferenceDescriptor(e1, e2, "link", false, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.link = null;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.ManyToOne;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@ManyToOne",
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
	}

	public void testManyToOneUnidirectionnalMandatoryReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		ManyToOneReferenceDescriptor ref = 
			new ManyToOneReferenceDescriptor(e1, e2, "link", true, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity(RightEntity link) {",
			"	setLink(link);",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.ManyToOne;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@ManyToOne",
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

	public void testManyToOneBidirectionnalReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		ManyToOneReferenceDescriptor ref1 = 
			new ManyToOneReferenceDescriptor(e1, e2, "link", false, false);
		OneToManyReferenceListDescriptor ref2 = 
			new OneToManyReferenceListDescriptor(e2, e1, "rlink", false);
		ref1.setReverse(ref2, true);
		ref2.setReverse(ref1, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.link = null;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.ManyToOne;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.Transient;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.PreRemove;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@ManyToOne",
			"RightEntity link;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@OneToMany(mappedBy=\"link\")",
			"List<LeftEntity> rlink;");		
		TestUtil.assertText(ref1.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertText(ref1.getSetter().getText(),
			"public void setLink(RightEntity link) {",
			"	if (this.link == link)",
		    "		return;",
		    "	if (this.link != null)",
		    "		this.link.rlink.remove(this);",
		    "	this.link = link;",
		    "	if (link != null)",
		    "		link.rlink.add(this);",
			"}");
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (this.link != null && !this.link.inDeletion)",
			"			this.link.rlink.remove(this);",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
	}

	public void testManyToOneBidirectionnalMandatoryReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		ManyToOneReferenceDescriptor ref1 = 
			new ManyToOneReferenceDescriptor(e1, e2, "link", true, false);
		OneToManyReferenceListDescriptor ref2 = 
			new OneToManyReferenceListDescriptor(e2, e1, "rlink", true);
		ref1.setReverse(ref2, false);
		ref2.setReverse(ref1, true);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity(RightEntity link) {",
			"	setLink(link);",
			"}");
		TestUtil.assertText(ref1.getGetter().getText(),
			"public RightEntity getLink() {",
			"	return link;",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import com.objetdirect.frmk.FieldRequiredException;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.ManyToOne;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.Transient;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.PreRemove;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@ManyToOne(mappedBy=\"rlink\")",
			"@NotNull",
			"RightEntity link;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@OneToMany(cascade={CascadeType.REMOVE})",
			"List<LeftEntity> rlink;");
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
		    "	if (this.link != null)",
		    "		this.link.rlink.remove(this);",
		    "	this.link = link;",
		    "	link.rlink.add(this);",
			"}");
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (!this.link.inDeletion)",
			"			this.link.rlink.remove(this);",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
	}
	
}
