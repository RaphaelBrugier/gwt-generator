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

public class TestSecuredValueObjectMaker extends TestCase {

	public void testSimpleValueObject() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true);
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
		TestUtil.assertText(valueObjectMaker.getText(),
			"package com.myapp.vo;",
			"",
			"import com.objetdirect.ejbfrmk.BaseValueObject;",
			"",
			"public class ClientVO extends BaseValueObject {",
			"",
			"	boolean inDeletion = false;",
			"",
			"	public ClientVO() {",
			"	}",
			"	",
			"	public ClientVO(boolean dummy) {",
			"	}",
			"",
			"	public void remove() {",
			"		verifyWrite();",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"		}",
			"	}",
			"",
			"}"
		);
	}

	public void testFieldDeclarations() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true).
			addStringField(true, 2, 40, "firstName", null).
			addStringField(true, 2, 40, "lastName", null);
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
		TestUtil.assertText(valueObjectMaker.getText(),
			"package com.myapp.vo;",
			"",
			"import com.objetdirect.ejbfrmk.BaseValueObject;",
			"",
			"public class ClientVO extends BaseValueObject {",
			"",
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
			"	public void remove() {",
			"		verifyWrite();",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"		}",
			"	}",
			"	",
			"	public String getFirstName() {",
			"		verifyRead();",
			"		return firstName;",
			"	}",
			"	",
			"	public void setFirstName(String firstName) {",
			"		verifyWrite();",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public void loadFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public String getLastName() {",
			"		verifyRead();",
			"		return lastName;",
			"	}",
			"	",
			"	public void setLastName(String lastName) {",
			"		verifyWrite();",
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
		EntityDescriptor product = new EntityDescriptor("com.myapp.domain", "Product", true).
			addStringField("firstName", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client", true).
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
			"import com.objetdirect.ejbfrmk.BaseValueObject;",
			"import java.util.ArrayList;",
			"import java.util.Collections;",
			"import java.util.List;",
			"",
			"public class ClientVO extends BaseValueObject {",
			"",
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
			"	public void remove() {",
			"		verifyWrite();",
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
			"		verifyRead();",
			"		return firstName;",
			"	}",
			"	",
			"	public void setFirstName(String firstName) {",
			"		verifyWrite();",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public void loadFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public List<ProductVO> getProducts() {",
			"		verifyRead();",
			"		return Collections.unmodifiableList(products);",
			"	}",
			"	",
			"	public void loadProducts(List<ProductVO> products) {",
			"		this.products = products;",
			"	}",
			"	",
			"	public boolean addProduct(ProductVO product) {",
			"		verifyWrite();",
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
			"		verifyWrite();",
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
		EntityDescriptor product = new EntityDescriptor("com.myapp.domain", "Product",true).
			addStringField("firstName", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client", true).
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
			"import com.objetdirect.ejbfrmk.BaseValueObject;",
			"import com.objetdirect.frmk.FieldRequiredException;",
			"",
			"public class ProductVO extends BaseValueObject {",
			"",
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
			"	public void remove() {",
			"		verifyWrite();",
			"		if (!inDeletion) {",
			"			inDeletion = true;",
			"			if (this.client != null && this.client.products != null && !this.client.inDeletion)",
			"				this.client.products.remove(this);",
			"		}",
			"	}",
			"	",
			"	public String getFirstName() {",
			"		verifyRead();",
			"		return firstName;",
			"	}",
			"	",
			"	public void setFirstName(String firstName) {",
			"		verifyWrite();",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public void loadFirstName(String firstName) {",
			"		this.firstName = firstName;",
			"	}",
			"	",
			"	public ClientVO getClient() {",
			"		verifyRead();",
			"		return client;",
			"	}",
			"	",
			"	public void loadClient(ClientVO client) {",
			"		this.client = client;",
			"	}",
			"	",
			"	public void setClient(ClientVO client) {",
			"		verifyWrite();",
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

}
