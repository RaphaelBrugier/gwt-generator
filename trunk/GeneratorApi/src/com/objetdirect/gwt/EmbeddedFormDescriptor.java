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

package com.objetdirect.gwt;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.entities.EntityDescriptor;

public class EmbeddedFormDescriptor {

	String packageName;
	EntityDescriptor entity;
	
	public EmbeddedFormDescriptor(String packageName, EntityDescriptor entity) {
		this.packageName = packageName;
		this.entity = entity;
		define();
	}
	
	ClassDescriptor classDescriptor = null;
	
	public ClassDescriptor getClassDescriptor() {
		if (classDescriptor==null) {
			classDescriptor = buildClassDescriptor();
		}
		return classDescriptor;
	}

	protected ClassDescriptor buildClassDescriptor() {
		ClassDescriptor clazz = new ClassDescriptor(packageName, new StandardNameMaker(entity.getName(), "Form", null));
		clazz.setSuperClass(Tatamix.Form);
		return clazz;
	}
	
	public void define() {
		layout.define();
	}
	
	AttributeDescriptor layout = new AttributeDescriptor() {
		public void define() { 
			define(getClassDescriptor(), Tatamix.FieldSet, "layout"); 
		}
		public String[] getText() {
			return super.getText();
		}
	};
	
	public String[] getText() {
		return getClassDescriptor().getText();
	}

	List<Field> fields = new ArrayList<Field>();
	
	public EmbeddedFormDescriptor addField(Field field) {
		fields.add(field);
		return this;
	}
}
