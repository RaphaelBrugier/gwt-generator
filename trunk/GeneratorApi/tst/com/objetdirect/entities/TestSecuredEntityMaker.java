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

public class TestSecuredEntityMaker extends TestCase {

	public void testSimpleEntity() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true);
		TestUtil.assertText(entityMaker.getText(),
			"package com.myapp.domain;",
			"",
			"import com.objetdirect.ejbfrmk.BaseEntity;",
			"import javax.persistence.Entity;",
			"",
			"@Entity",
			"public class Client extends BaseEntity {",
			"",
			"",
			"	public Client() {",
			"	}",
			"	",
			"	public Client(boolean dummy) {",
			"	}",
			"",
			"	public static Client createClient() {",
			"		Client client = new Client();",
			"		return client;",
			"	}",
			"",
			"}"
		);
	}

	public void testFieldDeclarations() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true).
			addStringField("firstName", null).
			addStringField("lastName", null);
		TestUtil.assertText(entityMaker.getText(),
			"package com.myapp.domain;",
			"",
			"import com.objetdirect.ejbfrmk.BaseEntity;",
			"import javax.persistence.Entity;",
			"",
			"@Entity",
			"public class Client extends BaseEntity {",
			"",
			"	String firstName;",
			"	String lastName;",
			"",
			"	public Client() {",
			"	}",
			"	",
			"	public Client(String firstName, String lastName) {",
			"		this.firstName = firstName;",
			"		this.lastName = lastName;",
			"	}",
			"",
			"	public static Client createClient() {",
			"		Client client = new Client();",
			"		return client;",
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
			"	public String getLastName() {",
			"		verifyRead();",
			"		return lastName;",
			"	}",
			"	",
			"	public void setLastName(String lastName) {",
			"		verifyWrite();",
			"		this.lastName = lastName;",
			"	}",
			"",
			"}"
		);
	}
		
	public void testRelationshipDeclarations() {
		EntityDescriptor address = new EntityDescriptor("com.myapp.domain", "Address").
			addStringField("street", null);
		EntityDescriptor product = new EntityDescriptor("com.myapp.domain", "Product").
			addStringField("code", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client", true).
			addStringField("firstName", null);
		RelationshipDescriptor r1 = new OneToManyReferenceListDescriptor(client, product, "products", true);
		RelationshipDescriptor r2 = new ManyToOneReferenceDescriptor(product, client, "client", false, false);
		r1.setReverse(r2, false);
		r2.setReverse(r1, true);
		new OneToOneReferenceDescriptor(client, address, "address", false, false, false);
		TestUtil.assertText(client.getText(),				
			"package com.myapp.domain;",
			"",
			"import com.objetdirect.ejbfrmk.BaseEntity;",
			"import com.objetdirect.ejbfrmk.UserManager;",
			"import java.util.ArrayList;",
			"import java.util.List;",
			"import javax.persistence.CascadeType;",
			"import javax.persistence.Entity;",
			"import javax.persistence.OneToMany;",
			"import javax.persistence.OneToOne;",
			"import javax.persistence.PreRemove;",
			"import javax.persistence.Transient;",
			"import org.hibernate.Hibernate;",
			"",
			"@Entity",
			"public class Client extends BaseEntity {",
			"",
			"	String firstName;",
			"	@OneToMany(cascade={CascadeType.REMOVE}, mappedBy=\"client\")",
			"	List<Product> products;",
			"	@Transient",
			"	boolean inDeletion = false;",
			"	@OneToOne",
			"	Address address;",
			"",
			"	public Client() {",
			"	}",
			"	",	
			"	public Client(String firstName) {",
			"		this.firstName = firstName;",
			"		this.products = new ArrayList<Product>();",
			"		this.address = null;",
			"	}",
			"",
			"	public static Client createClient() {",
			"		Client client = new Client();",
			"		client.products = new ArrayList<Product>();",
			"		return client;",
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
			"	public List<Product> getProducts() {",
			"		verifyRead();",
			"		return UserManager.filter(products);",
			"	}",
			"	",	
			"	public boolean addProduct(Product product) {",
			"		verifyWrite();",
			"		if (product==null)",
			"			throw new NullPointerException();",
			"		if (product.client != this) {",
			"			this.products.add(product);",
			"			if (product.client != null)",
			"				product.client.products.remove(this);",
			"			product.client = this;",
			"			return true;",
			"		}",
			"		else",
			"			return false;",
			"	}",
			"	",	
			"	public void loadProducts() {",
			"		if (!Hibernate.isInitialized(products))",
			"			Hibernate.initialize(products);",
			"	}",
			"	",	
			"	@PreRemove",
			"	void preRemove() {",
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
			"	public boolean removeProduct(Product product) {",
			"		verifyWrite();",
			"		if (product==null)",
			"			throw new NullPointerException();",
			"		if (product.client == this) {",
			"			this.products.remove(product);",
			"			product.client = null;",
			"			return true;",
			"		}",
			"		else",
			"			return false;",
			"	}",
			"	",
			"	public Address getAddress() {",
			"		verifyRead();",
			"		return address;",
			"	}",
			"	",
			"	public void setAddress(Address address) {",
			"		verifyWrite();",
			"		this.address = address;",
			"	}",			
			"",
			"}"
		);
	}
}
