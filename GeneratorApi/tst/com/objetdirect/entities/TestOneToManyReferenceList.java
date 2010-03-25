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

public class TestOneToManyReferenceList extends TestCase {

	public void testManyToOneUnidirectionnalReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToManyReferenceListDescriptor ref = 
			new OneToManyReferenceListDescriptor(e1, e2, "links", false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.links = new ArrayList<RightEntity>();",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToMany;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.List;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.ArrayList;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.Collections;");
		TestUtil.assertText(ref.getAttribute().getText(),
			"@OneToMany",
			"List<RightEntity> links;");		
		TestUtil.assertText(ref.getGetter().getText(),
			"public List<RightEntity> getLinks() {",
			"	return Collections.unmodifiableList(links);",
			"}");
		TestUtil.assertText(ref.getAdder().getText(),
			"public boolean addLink(RightEntity link) {",
			"	if (link==null)",
			"		throw new NullPointerException();",
			"	if (!this.links.contains(link)) {",
			"		this.links.add(link);",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}");
		TestUtil.assertText(ref.getRemover().getText(),
			"public boolean removeLink(RightEntity link) {",
			"	if (link==null)",
			"		throw new NullPointerException();",
			"	if (this.links.contains(link)) {",
			"		this.links.remove(link);",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}");
		TestUtil.assertNotExisting(e1.getText(), "preRemove");
	}

	public void testOneToManyBidirectionnalReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToManyReferenceListDescriptor ref1 = 
			new OneToManyReferenceListDescriptor(e1, e2, "links", true);
		ManyToOneReferenceDescriptor ref2 = 
			new ManyToOneReferenceDescriptor(e2, e1, "rlink", false, false);
		ref1.setReverse(ref2, true);
		ref2.setReverse(ref1, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.links = new ArrayList<RightEntity>();",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToMany;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.CascadeType;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.List;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.ArrayList;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.Collections;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@OneToMany(cascade={CascadeType.REMOVE})",
			"List<RightEntity> links;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@ManyToOne(mappedBy=\"links\")",
			"LeftEntity rlink;");		
		TestUtil.assertText(ref1.getGetter().getText(),
			"public List<RightEntity> getLinks() {",
			"	return Collections.unmodifiableList(links);",
			"}");
		TestUtil.assertText(ref1.getAdder().getText(),
			"public boolean addLink(RightEntity link) {",
			"	if (link==null)",
			"		throw new NullPointerException();",
			"	if (link.rlink != this) {",
			"		this.links.add(link);",
		    "		if (link.rlink != null)",
		    "			link.rlink.links.remove(this);",
		    "		link.rlink = this;",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}");
		TestUtil.assertText(ref1.getRemover().getText(),
			"public boolean removeLink(RightEntity link) {",
			"	if (link==null)",
			"		throw new NullPointerException();",
			"	if (link.rlink == this) {",
			"		this.links.remove(link);",
		    "		link.rlink = null;",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}");
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		for (RightEntity link : links) {",
			"			if (!link.inDeletion)",
			"				link.rlink = null;",
			"		}",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
	}
	
	public void testOneToManyBidirectionnalInverseMandatoryReference() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "LeftEntity");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "RightEntity");
		OneToManyReferenceListDescriptor ref1 = 
			new OneToManyReferenceListDescriptor(e1, e2, "links", true);
		ManyToOneReferenceDescriptor ref2 = 
			new ManyToOneReferenceDescriptor(e2, e1, "rlink", true, false);
		ref1.setReverse(ref2, true);
		ref2.setReverse(ref1, false);
		TestUtil.assertText(e1.getBusinessConstructor().getText(),
			"public LeftEntity() {",
			"	this.links = new ArrayList<RightEntity>();",
			"}");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.OneToMany;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import javax.persistence.CascadeType;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.List;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.ArrayList;");
		TestUtil.assertExists(e1.getClassDescriptor().getImportSet().getText(),
			"import java.util.Collections;");
		TestUtil.assertText(ref1.getAttribute().getText(),
			"@OneToMany(cascade={CascadeType.REMOVE})",
			"List<RightEntity> links;");		
		TestUtil.assertText(ref2.getAttribute().getText(),
			"@ManyToOne(mappedBy=\"links\")",
			"@NotNull",
			"LeftEntity rlink;");		
		TestUtil.assertText(ref1.getGetter().getText(),
			"public List<RightEntity> getLinks() {",
			"	return Collections.unmodifiableList(links);",
			"}");
		TestUtil.assertText(ref1.getAdder().getText(),
			"public boolean addLink(RightEntity link) {",
			"	if (link==null)",
			"		throw new NullPointerException();",
			"	if (link.rlink != this) {",
			"		this.links.add(link);",
		    "		if (link.rlink != null)",
		    "			link.rlink.links.remove(this);",
		    "		link.rlink = this;",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}");
		assertNull(ref1.getRemover());
		TestUtil.assertText(ref1.getOwner().getPreRemoveMethod().getText(),
			"@PreRemove",
			"void preRemove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		for (RightEntity link : links) {",
			"			if (!link.inDeletion)",
			"				throw new FieldRequiredException(link, \"rlink\");",
			"		}",
			"	}",
			"}");
		TestUtil.assertExists(e1.getText(),
			"@Transient",
			"boolean inDeletion = false;"
		);
	}
}
