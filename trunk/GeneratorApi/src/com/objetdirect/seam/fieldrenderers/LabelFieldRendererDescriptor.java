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

import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.FieldRendererDescriptor;

public class LabelFieldRendererDescriptor extends FieldRendererDescriptor {

	int length;
	
	public LabelFieldRendererDescriptor(
		String fieldName, 
		String fieldPath, 
		String fieldTitle, 
		int length) 
	{
		super(fieldName, fieldPath, fieldTitle);
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	boolean pdfFlag = false;
	
	public LabelFieldRendererDescriptor setPdfFlag(boolean pdfFlag) {
		this.pdfFlag = pdfFlag;
		return this;
	}
	
	public FragmentDescriptor buildFragment() {
		EntityHolder parent = getParent(EntityHolder.class);
		// to verify the path
		if (getFieldName()!=null)
			PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), getFieldName());
		FragmentDescriptor fragment = new FragmentDescriptor("<h:outputText value=\"#{entity.field}\" />");
		if (pdfFlag)
			fragment.replace("h:outputText", "p:text");
		if (getFieldName()!=null)
			fragment.replace("field", getFieldName());
		else
			fragment.remove(".field");
		return fragment;
	}

	@Override
	public int getWidth() {
		return length*10;
	}
	
	public boolean isEditableField() {
		return false;
	}
}
