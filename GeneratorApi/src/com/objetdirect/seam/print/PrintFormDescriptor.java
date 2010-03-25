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

import com.objetdirect.engine.FragmentDescriptor;
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

public class PrintFormDescriptor extends BaseComponent implements EntityHolder, PrintElement {

	public PrintFormDescriptor() {
	}

	public EntityDescriptor getEntityType() {
		return getParent(EntityHolder.class).getEntityType();
	}

	public void buildFaceletPart() {
		super.buildFaceletPart();
		prepareFragment();
		addFieldsToFragment();
	}

	List<FieldRendererDescriptor> fields = new ArrayList<FieldRendererDescriptor>();
	
	public PrintFormDescriptor showField(FieldRendererDescriptor field) {
		field.setOwner(this);
		fields.add(field);
		return this;
	}

	public PrintFormDescriptor showField(String fieldName, String fieldTitle, int length) {
		showField(new LabelFieldRendererDescriptor(fieldName, fieldName, fieldTitle, length).setPdfFlag(true));
		return this;
	}

	public PrintFormDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		showField(new PrintNumberFieldRendererDescriptor(fieldName, fieldName, fieldTitle, pattern, length).setPdfFlag(true));
		return this;
	}

	public PrintFormDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		showField(new PrintDateFieldRendererDescriptor(fieldName, fieldName, fieldTitle, pattern).setPdfFlag(true));
		return this;
	}

	public PrintFormDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		showField(new PrintBooleanFieldRendererDescriptor(fieldName, fieldName, fieldTitle, trueValue, falseValue).setPdfFlag(true));
		return this;
	}

	public PrintFormDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		showField(new PrintEnumFieldRendererDescriptor(fieldName, fieldName, fieldTitle, length).setPdfFlag(true));
		return this;
	}

	public PrintFormDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		showField(new PrintEntityFieldRendererDescriptor(fieldName, fieldName, fieldTitle, labels, length).setPdfFlag(true));
		return this;
	}
	
	String[] tablePattern = defaultTablePattern;
	
	protected void prepareFragment() {
		setFragment(new FragmentDescriptor(tablePattern) {
			public String[] getText() {
				removeLine("/// insert fields here");
				removeLine("/// insert field here");
				return super.getText();
			}
		});
		getFragment().replace(
			"beanName", getDocument().getBeanName());
	}
	
	public String getEntityAlias() {
		return getEntityType().getName();
	}

	String[] fieldPattern = defaultFieldPattern;
	
	protected String getPath() {
		return getParent(PrintHolder.class).getCurrentPath();
	}
	
	protected void addFieldsToFragment() {
		for (FieldRendererDescriptor field : fields) {
			FragmentDescriptor attrFragment = new FragmentDescriptor(defaultFieldPattern);
			field.setFragmentFiller(
				new FieldRendererDescriptor.FragmentFiller() {
					public void fillFragment(FragmentDescriptor fragment, FieldRendererDescriptor field) {
						fragment.replace("entity", getPath());
						fragment.replace("beanName", getDocument().getBeanName());
						fragment.replace("field", field.getFieldPath());
					}
				}
			);
			field.buildFaceletPart();
			FragmentDescriptor fieldFragment = field.getFragment();
			attrFragment.replace("fieldTitle", field.getFieldTitle());
			attrFragment.insertFragment("/// insert field here", fieldFragment);
			getFragment().insertFragment("/// insert fields here", attrFragment);
		}
	}
	
	static final String[] defaultTablePattern = {
		"<p:table columns=\"4\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"	/// insert fields here",
		"</p:table>"
	};
	
	static final String[] defaultFieldPattern = {
		"<p:cell border=\"0\">",
		"	<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"		<p:paragraph>",
		"			<p:text value=\"fieldTitle\" />", 
		"		</p:paragraph>",
		"	</p:font>",
		"</p:cell>",
		"<p:cell border=\"0\">",
		"	<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"		<p:paragraph>",
		"			/// insert field here", 
		"		</p:paragraph>",
		"	</p:font>",
		"</p:cell>",
	};
}
