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
import com.objetdirect.entities.EntityDescriptor;

import junit.framework.TestCase;

public class TestValueObjectMaker extends TestCase {

	public void testSimpleValueObject() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client");
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
		TestUtil.assertText(valueObjectMaker.getText(),
			"package com.myapp.vo;",
			"",
			"import java.io.Serializable;",
			"",
			"public class ClientVO implements Serializable {",
			"",
			"	int id;",
			"	int version;",
			"	boolean inDeletion = false;",
			"",
			"	public ClientVO() {",
			"	}",
			"	",
			"	public ClientVO(boolean dummy) {",
			"	}",
			"",
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",
			"	public void setId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public void loadId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public void setVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void loadVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void remove() {",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"		}",
			"	}",
			"",
			"}"
		);
	}

	public void testFieldDeclarations() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField(true, 2, 40, "firstName", null).
			addStringField(true, 2, 40, "lastName", null);
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
		TestUtil.assertText(valueObjectMaker.getText(),
			"package com.myapp.vo;",
			"",
			"import java.io.Serializable;",
			"",
			"public class ClientVO implements Serializable {",
			"",
			"	int id;",
			"	int version;",
			"	boolean inDeletion = false;",
			"	String firstName;",
			"	String lastName;",
			"",
			"	public ClientVO() {",
			"	}",
			"	",
			"	public ClientVO(String firstName, String lastName) {",
			"		this.firstName = firstName;",
			"		this.lastName = lastName;",
			"	}",
			"",
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",
			"	public void setId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public void loadId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public void setVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void loadVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void remove() {",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"		}",
			"	}",
			"	",
			"	public String getFirstName() {",
			"		return firstName;",
			"	}",
			"	",
			"	public void setFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public void loadFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public String getLastName() {",
			"		return lastName;",
			"	}",
			"	",
			"	public void setLastName(String lastName) {",
			"		this.lastName = lastName;",
			"	}",
			"	",
			"	public void loadLastName(String lastName) {",
			"		this.lastName = lastName;",
			"	}",
			"",
			"}"
		);
	}

	public void testReferenceListDeclarations() {
		EntityDescriptor product = new EntityDescriptor("com.myapp.domain", "Product").
			addStringField("firstName", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("firstName", null);
		RelationshipDescriptor r1 = new OneToManyReferenceListDescriptor(client, product, "products", true);
		RelationshipDescriptor r2 = new ManyToOneReferenceDescriptor(product, client, "client", false, false);
		r1.setReverse(r2, false);
		r2.setReverse(r1, true);
		client.addRelationship(r1);
		product.addRelationship(r2);
		ValueObjectDescriptor clientVOMaker = new ValueObjectDescriptor("com.myapp.vo", client);
		new ValueObjectDescriptor("com.myapp.vo", product);
		TestUtil.assertText(clientVOMaker.getText(),
			"package com.myapp.vo;",
			"",
			"import com.myapp.domain.Product;",
			"import java.io.Serializable;",
			"import java.util.ArrayList;",
			"import java.util.Collections;",
			"import java.util.List;",
			"",
			"public class ClientVO implements Serializable {",
			"",
			"	int id;",
			"	int version;",
			"	boolean inDeletion = false;",
			"	String firstName;",
			"	List<ProductVO> products;",
			"",
			"	public ClientVO() {",
			"	}",
			"	",
			"	public ClientVO(String firstName) {",
			"		this.firstName = firstName;",
			"		this.products = new ArrayList<ProductVO>();",
			"	}",
			"",
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",
			"	public void setId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public void loadId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public void setVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void loadVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void remove() {",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"			for (Product product : products) {",
			"				if (!product.inDeletion)",
			"					product.client = null;",
			"			}",
			"		}",
			"	}",
			"	",
			"	public String getFirstName() {",
			"		return firstName;",
			"	}",
			"	",
			"	public void setFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public void loadFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public List<ProductVO> getProducts() {",
			"		return Collections.unmodifiableList(products);",
			"	}",
			"	",
			"	public void loadProducts(List<ProductVO> products) {",
			"		this.products = products;",
			"	}",
			"	",
			"	public boolean addProduct(ProductVO product) {",
			"		if (product==null)",
			"			throw new NullPointerException();",
			"		if (this.products!=null && !this.products.contains(product)) {",
			"			this.products.add(product);",
			"			if (product.client != null && product.client.products != null)",
			"				product.client.products.remove(this);",
			"			product.client = this;",
			"			return true;",
			"		}",
			"		else",
			"			return false;",
			"	}",
			"	",
			"	public boolean removeProduct(ProductVO product) {",
			"		if (product==null)",
			"			throw new NullPointerException();",
			"		if (this.products!=null && this.products.contains(product)) {",
			"			this.products.remove(product);",
			"			product.client = null;",
			"			return true;",
			"		}",
			"		else",
			"			return false;",
			"	}",
			"",
			"}"
		);				
	}

	public void testReferenceDeclarations() {
		EntityDescriptor product = new EntityDescriptor("com.myapp.domain", "Product").
			addStringField("firstName", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("firstName", null);
		RelationshipDescriptor r1 = new OneToManyReferenceListDescriptor(client, product, "products", true);
		RelationshipDescriptor r2 = new ManyToOneReferenceDescriptor(product, client, "client", false, false);
		r1.setReverse(r2, false);
		r2.setReverse(r1, true);
		client.addRelationship(r1);
		product.addRelationship(r2);
		new ValueObjectDescriptor("com.myapp.vo", client);
		ValueObjectDescriptor productVOMaker = new ValueObjectDescriptor("com.myapp.vo", product);
		TestUtil.assertText(productVOMaker.getText(),
			"package com.myapp.vo;",
			"",
			"import com.objetdirect.frmk.FieldRequiredException;",
			"import java.io.Serializable;",
			"",
			"public class ProductVO implements Serializable {",
			"",
			"	int id;",
			"	int version;",
			"	boolean inDeletion = false;",
			"	String firstName;",
			"	ClientVO client;",
			"",
			"	public ProductVO() {",
			"	}",
			"	",
			"	public ProductVO(String firstName) {",
			"		this.firstName = firstName;",
			"		this.client = null;",
			"	}",
			"",
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",
			"	public void setId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public void loadId(int id) {",
			"		this.id = id;",
			"	}",
			"	",
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public void setVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void loadVersion(int version) {",
			"		this.version = version;",
			"	}",
			"	",
			"	public void remove() {",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"			if (this.client != null && this.client.products != null && !this.client.inDeletion)",
			"				this.client.products.remove(this);",
			"		}",
			"	}",
			"	",
			"	public String getFirstName() {",
			"		return firstName;",
			"	}",
			"	",
			"	public void setFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public void loadFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public ClientVO getClient() {",
			"		return client;",
			"	}",
			"	",
			"	public void loadClient(ClientVO client) {",
			"		this.client = client;",
			"	}",
			"	",
			"	public void setClient(ClientVO client) {",
			"		if (this.client == client)",
			"			return;",
			"		if (this.client != null && this.client.products != null)",
			"			this.client.products.remove(this);",
			"		this.client = client;",
			"		if (client != null && client.products != null)",
			"			client.products.add(this);",
			"	}",
			"",
			"}"
		);
	}

	public void testOneToOneReferenceDeclarations() {
		EntityDescriptor address = new EntityDescriptor("com.myapp.domain", "Address").
			addStringField("street", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("firstName", null);
		RelationshipDescriptor r1 = new OneToOneReferenceDescriptor(client, address, "address", false, true, true);
		RelationshipDescriptor r2 = new OneToOneReferenceDescriptor(address, client, "client", true, false, false);
		r1.setReverse(r2, false);
		r2.setReverse(r1, true);
		client.addRelationship(r1);
		address.addRelationship(r2);
		ValueObjectDescriptor clientVOMaker =new ValueObjectDescriptor("com.myapp.vo", client);
		ValueObjectDescriptor addressVOMaker = new ValueObjectDescriptor("com.myapp.vo", address);
		TestUtil.assertExists(addressVOMaker.getText(),
			"public AddressVO(String street, ClientVO client) {",
			"	this.street = street;",
			"	setClient(client);",
			"}"
		);
		TestUtil.assertExists(addressVOMaker.getText(),
			"public void remove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (!this.client.inDeletion)",
			"			this.client.address = null;",
			"	}",
			"}"
		);
		TestUtil.assertExists(addressVOMaker.getText(),
			"public ClientVO getClient() {",
			"	return client;",
			"}",
			"",
			"public void loadClient(ClientVO client) {",
			"	this.client = client;",
			"}",
			"",
			"public void setClient(ClientVO client) {",
			"	if (client == null)",
			"		throw new FieldRequiredException(this, \"client\");",
			"	if (this.client == client)",
			"		return;",
			"	if (client.address != null)",
			"		client.address.client = null;",
			"	if (this.client != null)",
			"		this.client.address = null;",
			"	this.client = client;",
			"	client.address = this;",
			"}"
		);

		TestUtil.assertExists(clientVOMaker.getText(),
			"public void remove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		if (this.address != null && !this.address.inDeletion)",
			"			throw new FieldRequiredException(this.address, \"client\");",
			"	}",
			"}"
		);
		TestUtil.assertExists(clientVOMaker.getText(),
			"public AddressVO getAddress() {",
			"	return address;",
			"}",
			"",
			"public void loadAddress(AddressVO address) {",
			"	this.address = address;",
			"}",
			"",
			"public void setAddress(AddressVO address) {",
			"	if (this.address == address)",
			"		return;",
			"	if (this.address != null)",
			"		throw new FieldRequiredException(this.address, \"client\");",
			"	if (address != null && address.client != null)",
			"		address.client.address = null;",
			"	this.address = address;",
			"	if (address != null)",
			"		address.client = this;",
			"}"
		);
	}

	public void testManyToManyReferenceListDeclarations() {
		EntityDescriptor book = new EntityDescriptor("com.myapp.domain", "Book").
			addStringField("title", null);
		EntityDescriptor keyword = new EntityDescriptor("com.myapp.domain", "Keyword").
			addStringField("label", null);
		RelationshipDescriptor r1 = new ManyToManyReferenceListDescriptor(book, keyword, "keywords", false);
		RelationshipDescriptor r2 = new ManyToManyReferenceListDescriptor(keyword, book, "books", false);
		r1.setReverse(r2, false);
		r2.setReverse(r1, true);
		book.addRelationship(r1);
		keyword.addRelationship(r2);
		ValueObjectDescriptor bookVOMaker =new ValueObjectDescriptor("com.myapp.vo", book);
		new ValueObjectDescriptor("com.myapp.vo", keyword);
		TestUtil.assertExists(bookVOMaker.getText(),
			"public BookVO(String title) {",
			"	this.title = title;",
			"	this.keywords = new ArrayList<KeywordVO>();",
			"}"
		);
		TestUtil.assertExists(bookVOMaker.getText(),
			"public void remove() {",
			"	if (!inDeletion) {",
			"		inDeletion = true;",
			"		for (Keyword keyword : keywords) {",
			"			if (keyword.books!=null && !keyword.inDeletion)",
			"				keyword.books.remove(this);",
			"		}",
			"	}",
			"}"
		);
		TestUtil.assertExists(bookVOMaker.getText(),
			"public List<KeywordVO> getKeywords() {",
			"	return Collections.unmodifiableList(keywords);",
			"}",
			"",
			"public void loadKeywords(List<KeywordVO> keywords) {",
			"	this.keywords = keywords;",
			"}",
			"",
			"public boolean addKeyword(KeywordVO keyword) {",
			"	if (keyword==null)",
			"		throw new NullPointerException();",
			"	if (this.keywords!=null && !this.keywords.contains(keyword)) {",
			"		this.keywords.add(keyword);",
			"		if (keyword.books != null)",
			"			keyword.books.add(this);",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}",
			"",
			"public boolean removeKeyword(KeywordVO keyword) {",
			"	if (keyword==null)",
			"		throw new NullPointerException();",
			"	if (this.keywords!=null && this.keywords.contains(keyword)) {",
			"		this.keywords.remove(keyword);",
			"		if (keyword.books != null)",
			"			keyword.books.remove(this);",
			"		return true;",
			"	}",
			"	else",
			"		return false;",
			"}"
		);
	}

}
