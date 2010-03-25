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

public class TestPathUtils extends TestCase {

	public void testSimpleGetterPaths() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");
		ClassDescriptor productType = new ClassDescriptor("com.objetdirect.domain", "ProductType");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productGetter = StandardMethods.getter(productAttr, "public");
		client.addMethod(productGetter);

		AttributeDescriptor productTypeAttr = new AttributeDescriptor();
		productTypeAttr.init(product, productType, "type");
		product.addAttribute(productTypeAttr);
		MethodDescriptor productTypeGetter = StandardMethods.getter(productTypeAttr, "public"); 
		product.addMethod(productTypeGetter);

		AttributeDescriptor productTypeName = new AttributeDescriptor();
		productTypeName.init(productType, TypeDescriptor.String, "name");
		productType.addAttribute(productTypeName);
		MethodDescriptor productTypeNameGetter = StandardMethods.getter(productTypeName, "public"); 
		productType.addMethod(productTypeNameGetter);

		MethodDescriptor[] path = PathUtils.getGetters(client, "product.type.name");
		assertEquals(3, path.length);
		assertSame(productGetter, path[0]);
		assertSame(productTypeGetter, path[1]);
		assertSame(productTypeNameGetter, path[2]);
		
		AttributeDescriptor productTypeSpecial = new AttributeDescriptor();
		productTypeSpecial.init(productType, TypeDescriptor.rBoolean, "special");
		productType.addAttribute(productTypeSpecial);
		MethodDescriptor productTypeSpecialGetter = StandardMethods.getter(productTypeSpecial, "public"); 
		assertEquals(productTypeSpecialGetter.getName(), "isSpecial");
		productType.addMethod(productTypeSpecialGetter);

		path = PathUtils.getGetters(client, "product.type.special");
		assertEquals(3, path.length);
		assertSame(productTypeSpecialGetter, path[2]);
	}
	
	public void testSpecialGetterPaths() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productGetter = StandardMethods.getter(productAttr, "public");
		client.addMethod(productGetter);

		MethodDescriptor[] path = PathUtils.getGetters(client, "");
		assertEquals(0, path.length);

		path = PathUtils.getGetters(client, "product");
		assertEquals(1, path.length);
		assertSame(productGetter, path[0]);
	}

	public void testSimpleSetterPaths() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");
		ClassDescriptor productType = new ClassDescriptor("com.objetdirect.domain", "ProductType");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productGetter = StandardMethods.getter(productAttr, "public");
		client.addMethod(productGetter);

		AttributeDescriptor productTypeAttr = new AttributeDescriptor();
		productTypeAttr.init(product, productType, "type");
		product.addAttribute(productTypeAttr);
		MethodDescriptor productTypeGetter = StandardMethods.getter(productTypeAttr, "public"); 
		product.addMethod(productTypeGetter);

		AttributeDescriptor productTypeName = new AttributeDescriptor();
		productTypeName.init(productType, TypeDescriptor.String, "name");
		productType.addAttribute(productTypeName);
		MethodDescriptor productTypeNameSetter = StandardMethods.setter(productTypeName, "public"); 
		productType.addMethod(productTypeNameSetter);

		MethodDescriptor[] path = PathUtils.getSetters(client, "product.type.name");
		assertEquals(3, path.length);
		assertSame(productGetter, path[0]);
		assertSame(productTypeGetter, path[1]);
		assertSame(productTypeNameSetter, path[2]);
	}
	
	public void testSpecialSetterPaths() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productSetter = StandardMethods.setter(productAttr, "public");
		client.addMethod(productSetter);

		MethodDescriptor[] path = PathUtils.getSetters(client, "");
		assertEquals(0, path.length);

		path = PathUtils.getSetters(client, "product");
		assertEquals(1, path.length);
		assertSame(productSetter, path[0]);
	}

	public void testExceptions() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");
		ClassDescriptor productType = new ClassDescriptor("com.objetdirect.domain", "ProductType");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productGetter = StandardMethods.getter(productAttr, "public");
		client.addMethod(productGetter);

		AttributeDescriptor productTypeAttr = new AttributeDescriptor();
		productTypeAttr.init(product, productType, "type");
		product.addAttribute(productTypeAttr);
		MethodDescriptor productTypeGetter = StandardMethods.getter(productTypeAttr, "public"); 
		product.addMethod(productTypeGetter);

		AttributeDescriptor productTypeName = new AttributeDescriptor();
		productTypeName.init(productType, TypeDescriptor.String, "name");
		productType.addAttribute(productTypeName);
		MethodDescriptor productTypeNameGetter = StandardMethods.getter(productTypeName, "public"); 
		productType.addMethod(productTypeNameGetter);
		
		try {
			PathUtils.getGetters(client, "product.order.name");
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("No getter for \"order\" in path : product.order.name", e.getMessage());
		}
		try {
			PathUtils.getGetters(client, "product.type.name.detail");
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Invalid getter for \"name\" in path : product.type.name.detail", e.getMessage());
		}
		try {
			PathUtils.getSetters(client, "product.type.name");
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("No setter for \"name\" in path : product.type.name", e.getMessage());
		}
	}

	public void testGetterCalls() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");
		ClassDescriptor productType = new ClassDescriptor("com.objetdirect.domain", "ProductType");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productGetter = StandardMethods.getter(productAttr, "public");
		client.addMethod(productGetter);

		AttributeDescriptor productTypeAttr = new AttributeDescriptor();
		productTypeAttr.init(product, productType, "type");
		product.addAttribute(productTypeAttr);
		MethodDescriptor productTypeGetter = StandardMethods.getter(productTypeAttr, "public"); 
		product.addMethod(productTypeGetter);

		AttributeDescriptor productTypeName = new AttributeDescriptor();
		productTypeName.init(productType, TypeDescriptor.String, "name");
		productType.addAttribute(productTypeName);
		MethodDescriptor productTypeNameGetter = StandardMethods.getter(productTypeName, "public"); 
		productType.addMethod(productTypeNameGetter);

		String call = PathUtils.getGetterCalls(client, "product.type.name"); 
		assertEquals("getProduct().getType().getName()", call);
		
		AttributeDescriptor productTypeSpecial = new AttributeDescriptor();
		productTypeSpecial.init(productType, TypeDescriptor.rBoolean, "special");
		productType.addAttribute(productTypeSpecial);
		MethodDescriptor productTypeSpecialGetter = StandardMethods.getter(productTypeSpecial, "public"); 
		productType.addMethod(productTypeSpecialGetter);

		call = PathUtils.getGetterCalls(client, "product");
		assertEquals("getProduct()", call);
		call = PathUtils.getGetterCalls(client, "product.type.special");
		assertEquals("getProduct().getType().isSpecial()", call);
	}
	
	public void testSetterCalls() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		ClassDescriptor product = new ClassDescriptor("com.objetdirect.domain", "Product");
		ClassDescriptor productType = new ClassDescriptor("com.objetdirect.domain", "ProductType");

		AttributeDescriptor productAttr = new AttributeDescriptor();
		productAttr.init(client, product, "product");
		client.addAttribute(productAttr);
		MethodDescriptor productGetter = StandardMethods.getter(productAttr, "public");
		client.addMethod(productGetter);
		MethodDescriptor productSetter = StandardMethods.setter(productAttr, "public");
		client.addMethod(productSetter);

		AttributeDescriptor productTypeAttr = new AttributeDescriptor();
		productTypeAttr.init(product, productType, "type");
		product.addAttribute(productTypeAttr);
		MethodDescriptor productTypeGetter = StandardMethods.getter(productTypeAttr, "public"); 
		product.addMethod(productTypeGetter);

		AttributeDescriptor productTypeName = new AttributeDescriptor();
		productTypeName.init(productType, TypeDescriptor.String, "name");
		productType.addAttribute(productTypeName);
		MethodDescriptor productTypeNameSetter = StandardMethods.setter(productTypeName, "public"); 
		productType.addMethod(productTypeNameSetter);

		String call = PathUtils.getSetterCalls(client, "product.type.name"); 
		assertEquals("getProduct().getType().setName", call);
		call = PathUtils.getSetterCalls(client, "product");
		assertEquals("setProduct", call);
	}
	
	public void testPathStore() {
		PathUtils.PathStore ps = PathUtils.storePathes(
			"", "Berard",
			"", "Adrien",
			"address.city", "Paris",
			"address.city.country", "France",
			"product.type", "Car",
			"product.type", "Skoda",
			"company.headquarters.addess.city", "London");
		assertEquals(
			"[] {\n"+
			"	Berard\n"+
			"	Adrien\n"+
			"	[address.city] {\n"+
			"		Paris\n"+
			"		[country] {\n"+
			"			France\n"+
			"		}\n"+
			"	}\n"+
			"	[product.type] {\n"+
			"		Car\n"+
			"		Skoda\n"+
			"	}\n"+
			"	[company.headquarters.addess.city] {\n"+
			"		London\n"+
			"	}\n"+
			"}\n", ps.toString());
	}

	public void testSplitPath() {
		String[] pathes = PathUtils.splitPath("");
		assertEquals(0, pathes.length);
		pathes = PathUtils.splitPath("adress.city.country");
		assertEquals(3, pathes.length);
		assertEquals("adress", pathes[0]);
		assertEquals("adress.city", pathes[1]);
		assertEquals("adress.city.country", pathes[2]);
	}
	
	public void testGetOnePathStoreText() {
		ClassDescriptor animator = new ClassDescriptor("com.od.actions", "Animator");
		ClassDescriptor employee = new ClassDescriptor("com.od.domain", "Employee");
		ClassDescriptor address = new ClassDescriptor("com.od.domain", "Address");
		ClassDescriptor agency = new ClassDescriptor("com.od.domain", "Agency");
		AttributeDescriptor empAddress = new AttributeDescriptor();
		empAddress.init(employee, address, "address");
		employee.addAttribute(empAddress);
		AttributeDescriptor empAgency = new AttributeDescriptor();
		empAgency.init(employee, agency, "agency");
		employee.addAttribute(empAgency);
		AttributeDescriptor agAddress = new AttributeDescriptor();
		agAddress.init(agency, address, "address");
		agency.addAttribute(agAddress);
		employee.addMethod(StandardMethods.getter(empAddress, "public"));
		employee.addMethod(StandardMethods.getter(empAgency, "public"));
		agency.addMethod(StandardMethods.getter(agAddress, "public"));
		
		PathUtils.StoreFiller sf = new PathUtils.StoreFiller() {
			public String[] getText(
					ClassDescriptor owner,
					String path,
					ClassDescriptor currentClass, 
					String currentInstance) 
			{
				return new String[] {
					currentInstance+".doIt();"
				};
			}
		};
		Object[] result = new Object[2];
		TestUtil.assertText(
			PathUtils.getText(animator, employee, "currentEmployee", "", "agency.address", result, sf),
			"if (currentEmployee.getAgency() != null && currentEmployee.getAgency().getAddress() != null) {",
			"	Address currentEmployeeAddress = currentEmployee.getAgency().getAddress();",
			"	currentEmployeeAddress.doIt();",
			"	/// insert sub text here",
			"}"
		);
		assertEquals(address, result[0]);
		assertEquals("currentEmployeeAddress", result[1]);
	}

	public void testGetPathStoreText() {
		ClassDescriptor animator = new ClassDescriptor("com.od.actions", "Animator");
		ClassDescriptor employee = new ClassDescriptor("com.od.domain", "Employee");
		ClassDescriptor address = new ClassDescriptor("com.od.domain", "Address");
		ClassDescriptor agency = new ClassDescriptor("com.od.domain", "Agency");
		AttributeDescriptor empAddress = new AttributeDescriptor();
		empAddress.init(employee, address, "address");
		employee.addAttribute(empAddress);
		AttributeDescriptor empAgency = new AttributeDescriptor();
		empAgency.init(employee, agency, "agency");
		employee.addAttribute(empAgency);
		AttributeDescriptor agAddress = new AttributeDescriptor();
		agAddress.init(agency, address, "address");
		agency.addAttribute(agAddress);
		employee.addMethod(StandardMethods.getter(empAddress, "public"));
		employee.addMethod(StandardMethods.getter(empAgency, "public"));
		agency.addMethod(StandardMethods.getter(agAddress, "public"));

		PathUtils.PathStore ps = PathUtils.storePathes(
			"address", "Montrouge",
			"agency", "ODParis",
			"agency.address", "Paris");

		PathUtils.StoreFiller sf = new PathUtils.StoreFiller() {
			public String[] getText(
					ClassDescriptor owner,
					String path,
					ClassDescriptor currentClass, 
					String currentInstance) 
			{
				return new String[] {
					currentInstance+".doIt(\""+path+"\");"
				};
			}
		};
		TestUtil.assertText(PathUtils.getText(animator, "", employee, "currentEmployee", ps, sf),
			"currentEmployee.doIt(\"\");",
			"if (currentEmployee.getAddress() != null) {",
			"	Address currentEmployeeAddress = currentEmployee.getAddress();",
			"	currentEmployeeAddress.doIt(\"address\");",
			"}",
			"if (currentEmployee.getAgency() != null) {",
			"	Agency currentEmployeeAgency = currentEmployee.getAgency();",
			"	currentEmployeeAgency.doIt(\"agency\");",
			"	if (currentEmployeeAgency.getAddress() != null) {",
			"		Address currentEmployeeAgencyAddress = currentEmployeeAgency.getAddress();",
			"		currentEmployeeAgencyAddress.doIt(\"agency.address\");",
			"	}",
			"}"
		);
	}

}

