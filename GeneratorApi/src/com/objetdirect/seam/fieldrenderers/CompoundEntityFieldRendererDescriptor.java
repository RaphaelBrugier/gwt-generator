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

package com.objetdirect.seam.fieldrenderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.FieldRendererDescriptor;
import com.objetdirect.seam.SelectorPopupDescriptor;

public class CompoundEntityFieldRendererDescriptor extends FieldRendererDescriptor {

	public CompoundEntityFieldRendererDescriptor(String fieldName, String fieldPath, String fieldTitle) {
		super(fieldName, fieldPath, fieldTitle);
	}

	@Override
	public int getWidth() {
		int width = 0;
		for (FieldRendererDescriptor field : fields) {
			width += field.getWidth()+10;
		}
		return width>0 ? width-10 : 0;
	}

	@Override
	public boolean isEditableField() {
		return false;
	}

	public void buildJavaPart() {
		for (FieldRendererDescriptor field : fields) {
			field.buildJavaPart();
		}
	}
	
	public FragmentDescriptor buildFragment() {
		EntityHolder parent = getParent(EntityHolder.class);
		// to verify the path
		if (getFieldName()!=null)
			PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), getFieldName());
		FragmentDescriptor fragment = new FragmentDescriptor(
			"<h:panelGroup rendered=\"#{entity.field!=null}\">",
			"	/// insert fields here",
			"</h:panelGroup>"
		);
		if (getFieldName()!=null)
			fragment.replace("field", getFieldName());
		else
			fragment.remove(".field");
		for (FieldRendererDescriptor field : fields) {
			field.buildFaceletPart();
			FragmentDescriptor fieldFragment = field.getFragment();
			fragment.insertFragment("/// insert fields here", fieldFragment);
		}
		return fragment;
	}

	List<FieldRendererDescriptor> fields = new ArrayList<FieldRendererDescriptor>();

	public Collection<FieldRendererDescriptor> getAllFields() {
		List<FieldRendererDescriptor> result = new ArrayList<FieldRendererDescriptor>();
		for (FieldRendererDescriptor field : fields) {
			result.add(field);
			result.addAll(field.getAllFields());
		}
		return result;
	}

	public CompoundEntityFieldRendererDescriptor addField(FieldRendererDescriptor field) {
		field.setOwner(this);
		fields.add(field);
		return this;
	}
	
	public CompoundEntityFieldRendererDescriptor showField(String fieldName, String fieldTitle, int length) {
		return addField(new LabelFieldRendererDescriptor(null, this.getFieldPath()+"."+fieldName, fieldTitle, length));
	}

	public CompoundEntityFieldRendererDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		return addField(new PrintNumberFieldRendererDescriptor(null, this.getFieldPath()+"."+fieldName, fieldTitle, pattern, length));
	}

	public CompoundEntityFieldRendererDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		return addField(new PrintDateFieldRendererDescriptor(null, this.getFieldPath()+"."+fieldName, fieldTitle, pattern));
	}

	public CompoundEntityFieldRendererDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		return addField(new PrintBooleanFieldRendererDescriptor(null, this.getFieldPath()+"."+fieldName, fieldTitle, trueValue, falseValue));
	}

	public CompoundEntityFieldRendererDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		return addField(new PrintEnumFieldRendererDescriptor(null, this.getFieldPath()+"."+fieldName, fieldTitle, length));
	}

	public CompoundEntityFieldRendererDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		return addField(new PrintEntityFieldRendererDescriptor(null, this.getFieldPath()+"."+fieldName, fieldTitle, labels, length));
	}
	
	public CompoundEntityFieldRendererDescriptor chooseReference(SelectorPopupDescriptor selector) {
		return addField(new ChooseEntityRendererDescriptor(null, this.getFieldPath(), selector));
	}

	@Override
	public void setFragmentFiller(FragmentFiller filler) {
		super.setFragmentFiller(filler);
		for (FieldRendererDescriptor field : fields) {
			field.setFragmentFiller(filler);
		}
	}
	
}
