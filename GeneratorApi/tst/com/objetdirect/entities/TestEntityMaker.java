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

public class TestEntityMaker extends TestCase {

	public void testSimpleEntity() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client");
		TestUtil.assertText(entityMaker.getText(),
			"package com.myapp.domain;",
			"",
			"import java.io.Serializable;",
			"import javax.persistence.Entity;",
			"import javax.persistence.GeneratedValue;",
			"import javax.persistence.Id;",
			"import javax.persistence.Version;",
			"",
			"@Entity",
			"public class Client implements Serializable {",
			"",
			"	@Id",
			"	@GeneratedValue",
			"	int id;",
			"	@Version", 
			"	int version;",
			"",
			"	public Client() {",
			"	}",
			"	",
			"	public Client(boolean dummy) {",
			"	}",
			"",
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public static Client createClient() {",
			"		Client client = new Client();",
			"		return client;",
			"	}",
			"",
			"}"
		);
	}

	public void testFieldDeclarations() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField(true, 2, 40, "firstName", null).
			addStringField(true, 2, 40, "lastName", null);
		TestUtil.assertText(entityMaker.getText(),
			"package com.myapp.domain;",
			"",
			"import java.io.Serializable;",
			"import javax.persistence.Entity;",
			"import javax.persistence.GeneratedValue;",
			"import javax.persistence.Id;",
			"import javax.persistence.Version;",
			"import org.hibernate.validator.Length;",
			"import org.hibernate.validator.NotNull;",
			"",
			"@Entity",
			"public class Client implements Serializable {",
			"",
			"	@Id",
			"	@GeneratedValue",
			"	int id;",
			"	@Version", 
			"	int version;",
			"	@NotNull",
			"	@Length(min=2, max=40)",
			"	String firstName;",
			"	@NotNull",
			"	@Length(min=2, max=40)",
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
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public static Client createClient() {",
			"		Client client = new Client();",
			"		return client;",
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
			"	public String getLastName() {",
			"		return lastName;",
			"	}",
			"	",
			"	public void setLastName(String lastName) {",
			"		this.lastName = lastName;",
			"	}",
			"",
			"}"
		);
	}
		
	public void testRelationshipDeclarations() {
		EntityDescriptor product = new EntityDescriptor("com.myapp.domain", "Product").
			addStringField("firstName", null);
		EntityDescriptor client = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("firstName", null);
		RelationshipDescriptor r1 = new OneToManyReferenceListDescriptor(client, product, "products", true);
		RelationshipDescriptor r2 = new ManyToOneReferenceDescriptor(product, client, "client", false, false);
		r1.setReverse(r2, false);
		r2.setReverse(r1, true);
		TestUtil.assertText(client.getText(),				
			"package com.myapp.domain;",
			"",
			"import java.io.Serializable;",
			"import java.util.ArrayList;",
			"import java.util.Collections;",
			"import java.util.List;",
			"import javax.persistence.CascadeType;",
			"import javax.persistence.Entity;",
			"import javax.persistence.GeneratedValue;",
			"import javax.persistence.Id;",
			"import javax.persistence.OneToMany;",
			"import javax.persistence.PreRemove;",
			"import javax.persistence.Transient;",
			"import javax.persistence.Version;",
			"import org.hibernate.Hibernate;",
			"",
			"@Entity",
			"public class Client implements Serializable {",
			"",
			"	@Id",
			"	@GeneratedValue",
			"	int id;",
			"	@Version",
			"	int version;",
			"	String firstName;",
			"	@OneToMany(cascade={CascadeType.REMOVE}, mappedBy=\"client\")",
			"	List<Product> products;",
			"	@Transient",
			"	boolean inDeletion = false;",
			"",
			"	public Client() {",
			"	}",
			"	",	
			"	public Client(String firstName) {",
			"		this.firstName = firstName;",
			"		this.products = new ArrayList<Product>();",
			"	}",
			"",
			"	public int getId() {",
			"		return id;",
			"	}",
			"	",	
			"	public int getVersion() {",
			"		return version;",
			"	}",
			"	",
			"	public static Client createClient() {",
			"		Client client = new Client();",
			"		client.products = new ArrayList<Product>();",
			"		return client;",
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
			"	public List<Product> getProducts() {",
			"		return Collections.unmodifiableList(products);",
			"	}",
			"	",	
			"	public boolean addProduct(Product product) {",
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
			"",
			"}"
		);
	}

}
