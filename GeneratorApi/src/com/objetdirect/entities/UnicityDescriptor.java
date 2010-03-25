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

import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;

public class UnicityDescriptor extends ConstraintDescriptor {

	public UnicityDescriptor(String message, MemberDescriptor ... fields) {
		this.fields = fields;
		this.message = message;
	}

	MemberDescriptor[] fields;
	String message;
	MethodDescriptor validation;
	
	@Override
	public MethodDescriptor buildValidationMethod(EntityDescriptor entity) {
		String fieldSuite = NamingUtil.toProperty(fields[fields.length-1].getName())+"Unicity";
		if (fields.length>1)
			fieldSuite = NamingUtil.toProperty(fields[fields.length-2].getName())+"And"+fieldSuite;
		for (int i=fields.length-3; i>=0; i--)
			fieldSuite = NamingUtil.toProperty(fields[i].getName())+fieldSuite;
		validation = new MethodDescriptor();
		validation.init(entity.getClassDescriptor(), TypeDescriptor.String,
			new StandardNameMaker("verify", fieldSuite, null)).addModifier("public");
		validation.addParameter(JavaxPersistence.EntityManager, "entityManager");
		for (MemberDescriptor field : fields) {
			validation.addParameter(field.getType(), field.getName());
		}
		validation.setContent(
			"List<EntityClass> entityList = entityManager.createQuery(",
			"	\"from EntityClass e where otherConditions\").",
			"	otherParameters().",
			"	getResultList();",
			"for (EntityClass entityInstance : entityList) {",
			"	if (entityInstance!=this) {",
			"		return \"message\";",
			"	}",
			"}",
			"return null;"
		);
		validation.replace("List", TypeDescriptor.List(null));
		validation.replace("EntityClass", entity.getClassDescriptor());
		validation.replace("entityList", NamingUtil.toMember(NamingUtil.toPlural(entity.getName())));
		validation.replace("entityInstance", NamingUtil.toMember(entity.getName()));
		validation.replace("entityManager", JavaxPersistence.EntityManager);
		validation.replace("message", message);
		for (MemberDescriptor field : fields) {
			validation.replace("otherConditions", "e.fieldName = :fieldName and otherConditions");
			validation.insertLines("otherParameters().", 	"setParameter(\"fieldName\", fieldName).");
			validation.replace("fieldName", field.getAttribute().getName());
		}
		validation.remove(" and otherConditions");
		validation.removeLine("otherParameters");
		entity.getClassDescriptor().addMethod(validation);
		return null;
	}
	
	public MemberDescriptor[] getFields() {
		return fields;
	}

	public boolean recognize(MemberDescriptor ... fields) {
		for (MemberDescriptor field : fields) {
			for (MemberDescriptor param : this.fields) {
				if (param==field)
					return true;
			}
		}
		return false;
	}

	@Override
	public MethodDescriptor getValidationMethod() {
		return validation;
	}
	
}
