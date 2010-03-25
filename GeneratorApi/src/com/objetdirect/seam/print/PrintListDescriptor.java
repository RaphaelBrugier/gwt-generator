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

package com.objetdirect.seam.print;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.BaseComponent;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.FieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.LabelFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintBooleanFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintDateFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintEntityFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintEnumFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintNumberFieldRendererDescriptor;

public class PrintListDescriptor extends BaseComponent implements PrintFeature, EntityHolder {

	public PrintListDescriptor(EntityDescriptor entity) {
		this.entity = entity;
	}

	EntityDescriptor entity;

	public EntityDescriptor getEntityType() {
		return entity;
	}

	public void buildJavaPart() {
		super.buildJavaPart();
		ClassDescriptor javaClass = getClassDescriptor();
		list = buildListAttribute(javaClass);
		listGetter = buildListGetterMethod(javaClass);
		listSetter = buildListSetterMethod(javaClass);
	}
	
	public void buildFaceletPart() {
		super.buildFaceletPart();
		prepareFragment();
		addColumnsToFragment();
	}

	AttributeDescriptor list;
	
	public AttributeDescriptor getListAttribute() {
		return list;
	}
	
	protected AttributeDescriptor buildListAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.List(getEntityType().getClassDescriptor()), 
			new StandardNameMaker(null, NamingUtil.toPlural(getEntityType().getName()), null)).initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor listGetter;
	MethodDescriptor listSetter;
	
	public MethodDescriptor getListGetterMethod() {
		return listGetter;
	}
	
	public MethodDescriptor getListSetterMethod() {
		return listSetter;
	}

	protected MethodDescriptor buildListGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.getter(list, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildListSetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.setter(list, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	List<FieldRendererDescriptor> columns = new ArrayList<FieldRendererDescriptor>();
	
	public PrintListDescriptor showField(FieldRendererDescriptor field) {
		field.setOwner(this);
		columns.add(field);
		return this;
	}

	public PrintListDescriptor showField(String fieldName, String fieldTitle, int length) {
		showField(new LabelFieldRendererDescriptor(fieldName, fieldName, fieldTitle, length).setPdfFlag(true));
		return this;
	}

	public PrintListDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		showField(new PrintNumberFieldRendererDescriptor(fieldName, fieldName, fieldTitle, pattern, length).setPdfFlag(true));
		return this;
	}

	public PrintListDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		showField(new PrintDateFieldRendererDescriptor(fieldName, fieldName, fieldTitle, pattern).setPdfFlag(true));
		return this;
	}

	public PrintListDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		showField(new PrintBooleanFieldRendererDescriptor(fieldName, fieldName, fieldTitle, trueValue, falseValue).setPdfFlag(true));
		return this;
	}

	public PrintListDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		showField(new PrintEnumFieldRendererDescriptor(fieldName, fieldName, fieldTitle, length).setPdfFlag(true));
		return this;
	}

	public PrintListDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		showField(new PrintEntityFieldRendererDescriptor(fieldName, fieldName, fieldTitle, labels, length).setPdfFlag(true));
		return this;
	}
	
	String[] tablePattern = defaultTablePattern;
	
	protected void prepareFragment() {
		setFragment(new FragmentDescriptor(tablePattern) {
			public String[] getText() {
				removeLine("/// insert headers here");
				removeLine("/// insert columns here");
				removeLine("/// insert field here");
				return super.getText();
			}
		});
		String listName = getListName();
		getFragment().replace(
			"beanName", getDocument().getBeanName(),
			"columnCount", ""+columns.size(),
			"entityList", listName,
			"line", getEntityAlias());
	}
	
	public String getEntityAlias() {
		String lineName = NamingUtil.toSingular(getListName());
		return lineName;
	}

	public String getListName() {
		String listName = NamingUtil.extractMember(listGetter);
		return listName;
	}
	
	String[] headerPattern = defaultHeaderPattern;
	String[] columnPattern = defaultColumnPattern;
	
	protected void addColumnsToFragment() {
		for (FieldRendererDescriptor column : columns) {
			FragmentDescriptor headerFragment = new FragmentDescriptor(headerPattern);
			headerFragment.replace("columnTitle", column.getFieldTitle());
			getFragment().insertFragment("/// insert headers here", headerFragment);
		}
		for (FieldRendererDescriptor column : columns) {
			FragmentDescriptor columnFragment = new FragmentDescriptor(columnPattern);
			column.setFragmentFiller(new FieldRendererDescriptor.FragmentFiller() {
				public void fillFragment(
						FragmentDescriptor fragment,
						FieldRendererDescriptor field) 
				{
					fragment.replace("entity", getEntityAlias());
				}
				
			});
			column.buildFaceletPart();
			FragmentDescriptor fieldFragment = column.getFragment();
			columnFragment.insertFragment("/// insert field here", fieldFragment);
			getFragment().insertFragment("/// insert columns here", columnFragment);
		}
	}
	
	static final String[] defaultTablePattern = {
		"<p:table columns=\"columnCount\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"	/// insert headers here",
		"	<ui:repeat value=\"#{beanName.entityList}\" var=\"line\">",
		"		/// insert columns here",
		"	</ui:repeat>",
		"</p:table>"
	};
	
	static final String[] defaultHeaderPattern = {
		"<p:cell>",
		"	<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"		<p:paragraph>",
		"			<p:text value=\"columnTitle\" />", 
		"		</p:paragraph>",
		"	</p:font>",
		"</p:cell>"
	};

	static final String[] defaultColumnPattern = {
		"<p:cell colspan=\"1\">",
		"	<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"		<p:paragraph>",
		"			/// insert field here", 
		"		</p:paragraph>",
		"	</p:font>",
		"</p:cell>",
	};
}

