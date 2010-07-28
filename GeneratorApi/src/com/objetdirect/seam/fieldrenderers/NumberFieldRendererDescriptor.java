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

public class NumberFieldRendererDescriptor extends FieldRendererDescriptor {

	String numberPattern;
	int fieldSize;
	
	public NumberFieldRendererDescriptor(
		String fieldName, 
		String fieldPath, 
		String fieldTitle, 
		String numberPattern,
		int fieldSize) 
	{
		super(fieldName, fieldPath, fieldTitle);
		this.numberPattern = numberPattern;
		this.fieldSize = fieldSize;
	}

	public String getNumberPattern() {
		return numberPattern;
	}

	public int getFieldSize() {
		return fieldSize;
	}
	
	public FragmentDescriptor buildFragment() {
		EntityHolder parent = getParent(EntityHolder.class);
		// to verify the path
		if (getFieldName()!=null)
			PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), getFieldName());
		FragmentDescriptor fragment = new FragmentDescriptor(
			"<h:inputText value=\"#{entity.field}\" size=\"fieldSize\">",
			"	<f:convertNumber pattern=\"numberPattern\"/>",
			"</h:inputText>"
		);	
		if (getFieldName()!=null)
			fragment.replace("field", getFieldName());
		else
			fragment.remove(".field");
		fragment.replace("numberPattern", numberPattern);
		fragment.replace("fieldSize", ""+fieldSize);
		return fragment;
	}

	@Override
	public int getWidth() {
		return fieldSize*10;
	}
	
	public boolean isEditableField() {
		return true;
	}
	
	public String getCriterion() {	
		String criterion = "new Criterion(\"criterionLabel\", \"criterionLabel : \", \"number\").setSize(criterionSize).setPattern(\"criterionPattern\")";
		criterion = Rewrite.replace(criterion, "criterionLabel", this.getFieldTitle());
		criterion = Rewrite.replace(criterion, "criterionSize", ""+this.getFieldSize());
		criterion = Rewrite.replace(criterion, "criterionPattern", ""+this.getNumberPattern());
		return criterion;
	}
}
