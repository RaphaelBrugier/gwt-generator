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
import com.objetdirect.engine.Rewrite;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.FieldRendererDescriptor;

public class InputFieldRendererDescriptor extends FieldRendererDescriptor {

	int length;
	
	public InputFieldRendererDescriptor(
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

	public FragmentDescriptor buildFragment() {
		EntityHolder parent = getParent(EntityHolder.class);
		// to verify the path
		if (getFieldName()!=null)
			PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), getFieldName());
		FragmentDescriptor fragment = new FragmentDescriptor(
			"<h:inputText value=\"#{entity.field}\" size=\"fieldSize\"/>");
		if (getFieldName()!=null)
			fragment.replace("field", getFieldName());
		else
			fragment.remove(".field");
		fragment.replace("fieldSize", ""+length);
		return fragment;
	}

	@Override
	public int getWidth() {
		return length*10;
	}
	
	public boolean isEditableField() {
		return true;
	}
	
	public String getCriterion() {	
		String criterion = "new Criterion(\"criterionLabel\", \"criterionLabel : \", \"text\").setSize(criterionSize)";
		criterion = Rewrite.replace(criterion, "criterionLabel", this.getFieldTitle());
		criterion = Rewrite.replace(criterion, "criterionSize", ""+this.getLength());
		return criterion;
	}
}
