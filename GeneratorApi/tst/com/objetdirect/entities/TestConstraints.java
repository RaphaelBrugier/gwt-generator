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

public class TestConstraints extends TestCase {

	public void testSimpleUnicityConstraint() {
		EntityDescriptor entity = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("name", null);
		entity.addConstraint(new UnicityDescriptor("A client with this name already exist", entity.getMember("name")));
		TestUtil.assertText(entity.getText(),
			"package com.myapp.domain;",
			"",
			"import java.io.Serializable;",
			"import java.util.List;",
			"import javax.persistence.Entity;",
			"import javax.persistence.EntityManager;",
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
			"	String name;",
			"",
			"	public Client() {",
			"	}",
			"	",
			"	public Client(String name) {",
			"		this.name = name;",
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
			"	public String getName() {",
			"		return name;",
			"	}",
			"	",
			"	public void setName(String name) {",
			"		this.name = name;",
			"	}",
			"	",
			"	public String verifyNameUnicity(EntityManager entityManager, String name) {",
			"		List<Client> clients = EntityManager.createQuery(",
			"			\"from Client e where e.name = :name\").",
			"			setParameter(\"name\", name).",
			"			getResultList();",
			"		for (Client client : clients) {",
			"			if (client!=this) {",
			"				return \"A client with this name already exist\";",
			"			}",
			"		}",
			"		return null;",
			"	}",
			"",
			"}"
		);
	}	
	
	public void testUnicityConstraintOnTwoFields() {
		EntityDescriptor entity = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("firstName", null).
			addStringField("lastName", null);
		entity.addConstraint(new UnicityDescriptor("A client with this first name and last name already exist", 
				entity.getMember("firstName"), entity.getMember("lastName")));
		TestUtil.assertExists(entity.getText(),
			"public String verifyFirstNameAndLastNameUnicity(EntityManager entityManager, String firstName, String lastName) {",
			"	List<Client> clients = EntityManager.createQuery(",
			"		\"from Client e where e.firstName = :firstName and e.lastName = :lastName\").",
			"		setParameter(\"firstName\", firstName).",
			"		setParameter(\"lastName\", lastName).",
			"		getResultList();",
			"	for (Client client : clients) {",
			"		if (client!=this) {",
			"			return \"A client with this first name and last name already exist\";",
			"		}",
			"	}",
			"	return null;",
			"}"
		);
	}	

	public void testUnicityConstraintOnMoreThanTwoFields() {
		EntityDescriptor entity = new EntityDescriptor("com.myapp.domain", "Client").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("nickname", null);
		entity.addConstraint(new UnicityDescriptor("A client with this first name, last name and nickname already exist", 
			entity.getMember("firstName"), 
			entity.getMember("lastName"),
			entity.getMember("nickname")));
		TestUtil.assertExists(entity.getText(),
			"public String verifyFirstNameLastNameAndNicknameUnicity(EntityManager entityManager, String firstName, String lastName, String nickname) {",
			"	List<Client> clients = EntityManager.createQuery(",
			"		\"from Client e where e.firstName = :firstName and e.lastName = :lastName and e.nickname = :nickname\").",
			"		setParameter(\"firstName\", firstName).",
			"		setParameter(\"lastName\", lastName).",
			"		setParameter(\"nickname\", nickname).",
			"		getResultList();",
			"	for (Client client : clients) {",
			"		if (client!=this) {",
			"			return \"A client with this first name, last name and nickname already exist\";",
			"		}",
			"	}",
			"	return null;",
			"}"
		);
	}	
}

/*
 */

