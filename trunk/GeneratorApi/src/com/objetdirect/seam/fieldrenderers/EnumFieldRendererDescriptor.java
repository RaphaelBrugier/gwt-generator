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

import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.FieldRendererDescriptor;

public class EnumFieldRendererDescriptor extends FieldRendererDescriptor {

	public EnumFieldRendererDescriptor(
		String fieldName, 
		String fieldPath, 
		String fieldTitle, 
		EnumDescriptor enumSet,
		int length) 
	{
		super(fieldName, fieldPath, fieldTitle);
		this.enumSet = enumSet;
		this.length = length;
	}

	MethodDescriptor getEnums;
	EnumDescriptor enumSet;
	int length;
	
	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor(); 
		getEnums = getEnumMethod(javaClass);
	}
	
	protected MethodDescriptor getEnumMethod(ClassDescriptor javaClass) {
		SemanticDescriptor semantic = new SemanticDescriptor("all", enumSet);
		MethodDescriptor meth = javaClass.getMethods().getMethod(semantic);
		if (meth==null) {
			meth = new MethodDescriptor();
			meth.init(javaClass, TypeDescriptor.array(enumSet), 
				new StandardNameMaker("get", NamingUtil.toPlural(enumSet.getTypeName()), null))
				.addModifier("public");
			meth.setContent(
				"return EnumClass.values();"
			);
			meth.setSemantic(semantic);
			meth.replace("EnumClass", enumSet);
			javaClass.addMethod(meth);
		}
		return meth;
	}
	
	public FragmentDescriptor buildFragment() {
		EntityHolder parent = getParent(EntityHolder.class);
		// to verify the path
		if (getFieldName()!=null)
			PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), getFieldName());
		FragmentDescriptor fd = new FragmentDescriptor(
			"<h:selectOneMenu value=\"#{entity.field}\">",
			"	<s:selectItems value=\"#{beanName.enums}\" var=\"type\"  label=\"#{type.label}\" noSelectionLabel=\"Choisissez...\"/>",
			"	<s:convertEnum />",
			"</h:selectOneMenu>"
		);
		if (getFieldName()!=null)
			fd.replace("field", getFieldName());
		else
			fd.remove(".field");	
		fd.replace("type", NamingUtil.toMember(enumSet.getTypeName()));
		fd.replace("beanName", getDocument().getBeanName());
		fd.replaceProperty("enums", getEnums);
		return fd;
	}

	@Override
	public int getWidth() {
		return length*10;
	}
	
	public boolean isEditableField() {
		return true;
	}
	
	public String getCriterion() {	
		String criterion = "new Criterion(\"criterionLabel\", \"criterionLabel : \", \"enumList\").setItems(\"#{beanName.criterionItems}\")";
		criterion = Rewrite.replace(criterion, "criterionLabel", this.getFieldTitle());
		criterion = Rewrite.replace(criterion, "criterionItems", FragmentDescriptor.getProperty(this.getEnums));
		criterion = Rewrite.replace(criterion, "beanName", getDocument().getBeanName());
		return criterion;
	}
}
