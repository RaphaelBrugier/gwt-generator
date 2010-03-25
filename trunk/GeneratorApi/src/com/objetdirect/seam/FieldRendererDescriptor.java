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

package com.objetdirect.seam;

import java.util.ArrayList;
import java.util.Collection;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.TypeDescriptor;

public abstract class FieldRendererDescriptor extends BaseComponent {

	private String fieldName;
	private String fieldPath;
	String fieldTitle;
	String defaultValue = null;
	
	public FieldRendererDescriptor(String fieldName, String fieldPath, String fieldTitle) {
		this.fieldName = fieldName;
		this.fieldPath = fieldPath;
		this.fieldTitle = fieldTitle;
	}

	public void buildJavaPart() {
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public String getDefaultValue() {
		if (defaultValue!=null) 
			return defaultValue;
		else {
			TypeDescriptor type = getType();
			if (type==TypeDescriptor.rBoolean || type==TypeDescriptor.Boolean)
				return "false";
			else if (type==TypeDescriptor.rChar || type==TypeDescriptor.Char)
				return "'\0'";
			else if (type==TypeDescriptor.rInt || type==TypeDescriptor.Int)
				return "0";
			else if (type==TypeDescriptor.rLong || type==TypeDescriptor.Long)
				return "0L";
			else if (type==TypeDescriptor.rByte || type==TypeDescriptor.Byte)
				return "0b";
			else if (type==TypeDescriptor.rShort || type==TypeDescriptor.Short)
				return "0s";
			else if (type==TypeDescriptor.rFloat || type==TypeDescriptor.Float)
				return "0.0f";
			else if (type==TypeDescriptor.rDouble || type==TypeDescriptor.Double)
				return "0.0";
			else if (type==TypeDescriptor.String)
				return "\"\"";
			else if (type==TypeDescriptor.Date)
				return "new Date()";
			return "null";
		}
	}
	
	public Collection<FieldRendererDescriptor> getAllFields() {
		return new ArrayList<FieldRendererDescriptor>();
	}
	
	public abstract int getWidth();

	public TypeDescriptor getStartType() {
		EntityHolder parent = getParent(EntityHolder.class);
		return parent.getEntityType().getClassDescriptor();
	}
	
	public TypeDescriptor getType() {
		EntityHolder parent = getParent(EntityHolder.class);
		MethodDescriptor[] getters = PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), fieldPath);
		return getters[getters.length-1].getReturnType();
	}

	AttributeDescriptor attribute;
	
	public void setAttribute(AttributeDescriptor attribute) {
		this.attribute = attribute;
	}
	
	public AttributeDescriptor getAttribute() {
		return attribute;
	}

	MethodDescriptor getter;
	
	public void setGetter(MethodDescriptor getter) {
		this.getter = getter;
	}
	
	public MethodDescriptor getGetter() {
		return getter;
	}	

	MethodDescriptor setter;
	
	public void setSetter(MethodDescriptor setter) {
		this.setter = setter;
	}
	
	public MethodDescriptor getSetter() {
		return setter;
	}
	
	public abstract boolean isEditableField();
	
	public interface FragmentFiller {
		void fillFragment(FragmentDescriptor fragment, FieldRendererDescriptor field);
	}
	
	FragmentFiller filler;
	
	protected abstract FragmentDescriptor buildFragment();
	
	public void buildFaceletPart() {
		setFragment(buildFragment());
		filler.fillFragment(getFragment(), this);
	}
	
	public void setFragmentFiller(FragmentFiller filler) {
		this.filler = filler;
	}

	public String getCriterion() {
		return null;
	}
}
