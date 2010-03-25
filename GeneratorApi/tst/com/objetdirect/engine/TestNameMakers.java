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

package com.objetdirect.engine;

import junit.framework.TestCase;

public class TestNameMakers extends TestCase {

	public void testSimpleStandardNameMaker() {
		NameMaker nm = new StandardNameMaker("client");
		assertEquals("client", nm.makeMemberName());
		assertEquals("client1", nm.makeMemberName());
		assertEquals("client2", nm.makeMemberName());
	}
	
	public void testPrefixedNameMaker() {
		ClassDescriptor ca = new ClassDescriptor("com.objetdirect.actions", "ClientAnimator");
		ClassDescriptor ce = new ClassDescriptor("com.objetdirect.domain", "Client");
		AttributeDescriptor att = new AttributeDescriptor();
		att.init(ca, ce, "client");
		NameMaker nm = new StandardNameMaker("current", null, att);
		assertEquals("currentClient", nm.makeMemberName());
		assertEquals("currentClient1", nm.makeMemberName());
		assertEquals("currentClient2", nm.makeMemberName());
	}
	
	public void testSuffixedNameMaker() {
		ClassDescriptor ca = new ClassDescriptor("com.objetdirect.actions", "ClientAnimator");
		ClassDescriptor ce = new ClassDescriptor("com.objetdirect.domain", "Client");
		AttributeDescriptor att = new AttributeDescriptor();
		att.init(ca, ce, "client");
		NameMaker nm = new StandardNameMaker(null, "Sorter", att);
		assertEquals("clientSorter", nm.makeMemberName());
		assertEquals("client1Sorter", nm.makeMemberName());
		assertEquals("client2Sorter", nm.makeMemberName());
	}
	
	public void testPrefixedAndSuffixedNameMaker() {
		ClassDescriptor ca = new ClassDescriptor("com.objetdirect.actions", "ClientAnimator");
		ClassDescriptor ce = new ClassDescriptor("com.objetdirect.domain", "Client");
		AttributeDescriptor att = new AttributeDescriptor();
		att.init(ca, ce, "client");
		NameMaker nm = new StandardNameMaker("get", "Paginator", att);
		assertEquals("getClientPaginator", nm.makeMemberName());
		assertEquals("getClient1Paginator", nm.makeMemberName());
		assertEquals("getClient2Paginator", nm.makeMemberName());
	}
	
	public void testPrefixedAndSuffixedOnlyNameMaker() {
		NameMaker nm = new StandardNameMaker("add", "Element", null);		
		assertEquals("addElement", nm.makeMemberName());
		assertEquals("addElement1", nm.makeMemberName());
	}
	
	public void testPropertyNameMaker() {
		NameMaker nm = new StandardNameMaker("client");
		assertEquals("client", nm.makeMemberName());
		assertEquals("Client1", nm.makePropertyName());
		NameMaker nm2 = new StandardNameMaker("Product");
		assertEquals("product", nm2.makeMemberName());
		assertEquals("Product1", nm2.makePropertyName());
	}
	
	public void testNameBasedOnTypeOrMethod() {
		ClassDescriptor ce = new ClassDescriptor("com.objetdirect.domain", "Client");
		MethodDescriptor m = new MethodDescriptor();
		m.init(ce, TypeDescriptor.rVoid, "doIt");
		NameMaker nm1 = new StandardNameMaker("select", null, ce);
		assertEquals("selectClient", nm1.makeMemberName());
		NameMaker nm2 = new StandardNameMaker(null, "wrapper", m);
		assertEquals("doItWrapper", nm2.makeMemberName());
	}
	
}
