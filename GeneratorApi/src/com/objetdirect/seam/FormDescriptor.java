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
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.fieldrenderers.BooleanFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.ChooseEntityRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.CompoundEntityFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.DateFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.EntityFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.EnumFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.InputFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.LabelFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.NumberFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintBooleanFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintDateFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintEntityFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintEnumFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintNumberFieldRendererDescriptor;

public class FormDescriptor extends BaseComponent {

	String[] formEnvelopPattern = defaultFormEnvelopPattern;
	String[] editableFieldPattern = defaultEditableFieldPattern;
	String[] readOnlyFieldPattern = defaultReadOnlyFieldPattern;
	boolean submissionProtection = false;
	
	public FormDescriptor protectSubmission() {
		submissionProtection = true;
		return this;
	}
	
	List<FieldRendererDescriptor> fields = new ArrayList<FieldRendererDescriptor>();

	public FormDescriptor addField(FieldRendererDescriptor field) {
		field.setOwner(this);
		fields.add(field);
		return this;
	}
	
	public FormDescriptor showField(String fieldName, String fieldTitle, int length) {
		return addField(new LabelFieldRendererDescriptor(null, fieldName, fieldTitle, length));
	}

	public FormDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		addField(new PrintNumberFieldRendererDescriptor(null, fieldName, fieldTitle, pattern, length));
		return this;
	}

	public FormDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		addField(new PrintDateFieldRendererDescriptor(null, fieldName, fieldTitle, pattern));
		return this;
	}

	public FormDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		addField(new PrintBooleanFieldRendererDescriptor(null, fieldName, fieldTitle, trueValue, falseValue));
		return this;
	}

	public FormDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		addField(new PrintEnumFieldRendererDescriptor(null, fieldName, fieldTitle, length));
		return this;
	}

	public FormDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		addField(new PrintEntityFieldRendererDescriptor(null, fieldName, fieldTitle, labels, length));
		return this;
	}
	
	public FormDescriptor editStringField(String fieldName, String fieldTitle, int length) {
		return addField(new InputFieldRendererDescriptor(null, fieldName, fieldTitle, length));
	}

	public FormDescriptor editDateField(String fieldName, String fieldTitle, String pattern) {
		return addField(new DateFieldRendererDescriptor(null, fieldName, fieldTitle, pattern));
	}

	public FormDescriptor editBooleanField(String fieldName, String fieldTitle) {
		return addField(new BooleanFieldRendererDescriptor(null, fieldName, fieldTitle));
	}

	public FormDescriptor editNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		return addField(new NumberFieldRendererDescriptor(null, fieldName, fieldTitle, pattern, length));
	}

	public FormDescriptor editEnumField(String fieldName, String fieldTitle, EnumDescriptor enumDesc, int length) {
		return addField(new EnumFieldRendererDescriptor(null, fieldName, fieldTitle, enumDesc, length));
	}

	public FormDescriptor editEntityField(String fieldName, String fieldTitle, EntityDescriptor entity, String labels, int length) {
		return addField(new EntityFieldRendererDescriptor(null, fieldName, fieldTitle, entity, labels, length));
	}

	public CompoundEntityFieldRendererDescriptor editCompoundEntityField(String fieldName, String fieldTitle, EntityDescriptor entity) {
		CompoundEntityFieldRendererDescriptor rd = new CompoundEntityFieldRendererDescriptor(null, fieldName, fieldTitle);
		addField(rd);
		return rd;
	}

	public FormDescriptor chooseReference(String fieldName, SelectorPopupDescriptor selector) {
		return addField(new ChooseEntityRendererDescriptor(null, fieldName, selector));
	}

	public FormHolder getFormHolder() {
		return getParent(FormHolder.class);
	}
	
	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
	}
	
	public void buildFaceletPart() {
		buildFaceletFragment();
	}

	protected void buildJavaElements(ClassDescriptor javaClass) {
		createAttributes(javaClass);
		for (FieldRendererDescriptor frd : fields)
			frd.buildJavaPart();
		initForm(javaClass);
		fillForm(javaClass);
		submitForm(javaClass);
		checkDirty(javaClass);
	}

	protected void initForm(ClassDescriptor javaClass) {
		MethodDescriptor target = getFormHolder().getInitFormMethod();
		List<FieldRendererDescriptor> fields = getAllFields();
		if (target!=null) {
			for (FieldRendererDescriptor field : fields) {
				AttributeDescriptor attr = field.getAttribute();
				String pattern = "attributeToSet = defaultValue;";
				pattern = Rewrite.replace(pattern, "attributeToSet", attr.getName());
				pattern = Rewrite.replace(pattern, "defaultValue", field.getDefaultValue());
				target.insertLines(getFormHolder().getInitFormTag(), pattern);
			}
		}
	}

	protected void fillForm(ClassDescriptor javaClass) {
		MethodDescriptor target = getFormHolder().getFillFormMethod();
		if (target!=null) {
			List<FieldRendererDescriptor> fields = getAllFields();
			for (FieldRendererDescriptor field : fields) {
				AttributeDescriptor attr = field.getAttribute();
				String pattern = "attributeToSet = target.getValue();";
				pattern = Rewrite.replace(pattern, "target", getFormHolder().getInitTarget());
				pattern = Rewrite.replace(pattern, "attributeToSet", attr.getName());
				pattern = Rewrite.replace(pattern, "getValue()", 
					PathUtils.getGetterCalls((ClassDescriptor)field.getStartType(), field.getFieldPath()));
				target.insertLines(getFormHolder().getFillFormTag(), pattern);
			}
		}
	}
	
	protected void submitForm(ClassDescriptor javaClass) {
		MethodDescriptor[] targets = getFormHolder().getSubmitFormMethods();
		if (targets != null) {
			List<FieldRendererDescriptor> fields = getAllFields();
			for (FieldRendererDescriptor field : fields) {
				AttributeDescriptor attr = field.getAttribute();
				String pattern = "target.setValue(attributeToSet);";
				pattern = Rewrite.replace(pattern, "target", getFormHolder().getSubmitTarget());
				pattern = Rewrite.replace(pattern, "attributeToSet", attr.getName());
				pattern = Rewrite.replace(pattern, "setValue", 
					PathUtils.getSetterCalls((ClassDescriptor)field.getStartType(), field.getFieldPath()));
				for (MethodDescriptor md : targets) {
					md.insertLines(getFormHolder().getSubmitFormTag(), pattern);
				}
			}
		}
	}

	protected void checkDirty(ClassDescriptor javaClass){
		MethodDescriptor[] targets = getFormHolder().getCheckDirtyMethods();
		if (targets != null) {
			List<FieldRendererDescriptor> fields = getAllFields();
			for (FieldRendererDescriptor field : fields) {
				AttributeDescriptor attr = field.getAttribute();
				String[] pattern = {
					"if (!GuiUtil.equals(attributeToSet, target.getValue()))",
					"	return true;"
				};
				javaClass.getImportSet().addType(Frmk.GuiUtil);
				pattern = Rewrite.replace(pattern, "GuiUtil", Frmk.GuiUtil.getUsageName(javaClass));
				pattern = Rewrite.replace(pattern, "target", getFormHolder().getSubmitTarget());
				pattern = Rewrite.replace(pattern, "attributeToSet", attr.getName());
				pattern = Rewrite.replace(pattern, "getValue()", 
					PathUtils.getGetterCalls((ClassDescriptor)field.getStartType(), field.getFieldPath()));
				for (MethodDescriptor md : targets) {
					md.insertLines(getFormHolder().getCheckDirtyTag(), pattern);
				}
			}
		}
	}
	
	List<FieldRendererDescriptor> getAllFields() {
		List<FieldRendererDescriptor> result = new ArrayList<FieldRendererDescriptor>();
		for (FieldRendererDescriptor field : fields) {
			result.add(field);
			result.addAll(field.getAllFields());
		}
		return result;
	}
	
	List<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
	
	protected AttributeDescriptor getAttribute(SemanticDescriptor semantic) {
		for (AttributeDescriptor attr : attributes) {
			if (attr.recognizes(semantic))
				return attr;
		}
		return null;
	}
	
	protected void createAttributes(ClassDescriptor javaClass) {
		for (FieldRendererDescriptor field : getAllFields()) {
			AttributeDescriptor fieldAttr = PathUtils.getAttribute(
				(ClassDescriptor)field.getStartType(),
				field.getFieldPath());
			SemanticDescriptor semantic = 
				new SemanticDescriptor("protect", fieldAttr.getSemantic().getParam(0));
			AttributeDescriptor attr = getAttribute(semantic);
			MethodDescriptor getter;
			MethodDescriptor setter;			
			if (attr==null) {
				String attributeName = NamingUtil.pathToName(field.getFieldPath());
				if (getFormHolder().getNamePrefix()!=null)
					attributeName = getFormHolder().getNamePrefix()+ attributeName;
				else
					attributeName = NamingUtil.toMember(attributeName);
				if (getFormHolder().getNameSuffix()!=null)
					attributeName+=NamingUtil.toProperty(getFormHolder().getNameSuffix());
				attr = new AttributeDescriptor();
				attr.init(
					javaClass, field.getType(), attributeName);
				attr.setSemantic(semantic);
				javaClass.addAttribute(attr);
				attributes.add(attr);
				getter = StandardMethods.getter(attr, "public");
				javaClass.addMethod(getter);
				setter = buildSetter(attr);
				javaClass.addMethod(setter);
			}
			else {
				getter = StandardMethods.getGetter(attr);
				setter = StandardMethods.getSetter(attr);
			}
			field.setAttribute(attr);
			field.setGetter(getter);
			field.setSetter(setter);
		}
	}

	MethodDescriptor buildSetter(AttributeDescriptor attr) {
		MethodDescriptor setter = StandardMethods.setter(attr, "public");
		if (submissionProtection) {
			String[] text = setter.getContent();
			setter.setContent(new String[] {
				"if (GuiUtil.isSubmitValid())",
				"	/// insert setter content here"
			});
			setter.replace("GuiUtil", Frmk.GuiUtil);
			setter.insertLines("/// insert setter content here", text);
		}
		return setter;
	}

	protected void buildFaceletFragment() {
		setFragment(new FragmentDescriptor(formEnvelopPattern));
		for (int i=0; i<fields.size(); i++) {
			FieldRendererDescriptor field = fields.get(i);
			FragmentDescriptor fragment =
				new FragmentDescriptor(
					field.isEditableField()? editableFieldPattern : readOnlyFieldPattern
				);
			if (field.getFieldTitle()==null)
				fragment.replaceAndInsertLines("<h:outputText value=\"Title : \" />", "<h:outputText/>");
			else
				fragment.replace("Title", field.getFieldTitle());
			field.setFragmentFiller(
				new FieldRendererDescriptor.FragmentFiller() {
					public void fillFragment(FragmentDescriptor fragment, FieldRendererDescriptor field) {
						fragment.replace("entity", "beanName.field");
						fragment.replace("beanName", getDocument().getBeanName());
						fragment.replaceProperty("field", field.getGetter());
					}
				}
			);
			field.buildFaceletPart();
			FragmentDescriptor fieldFragment = field.getFragment();
			fragment.replaceByFragment("/// insert field here", fieldFragment);
			getFragment().insertFragment("/// insert fields here", fragment);
		}
	}

	static final String[] defaultFormEnvelopPattern = {
		"<s:validateAll>",
		"	<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
		"		/// insert fields here",
    	"	</h:panelGrid>",
		"</s:validateAll>",
	};

	static final String[] defaultEditableFieldPattern = {
		"<h:outputText value=\"Title : \" />",
		"<s:decorate template=\"/layout/edit.xhtml\">",
		"	/// insert field here",
		"</s:decorate>",
	};

	static final String[] defaultReadOnlyFieldPattern = {
		"<h:outputText value=\"Title : \" />",
		"/// insert field here"
	};

	public List<AttributeDescriptor> getAttributes() {
		List<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
		for (FieldRendererDescriptor field : fields) {
			if (!attributes.contains(field.getAttribute()))
				attributes.add(field.getAttribute());
		}
		return attributes;
	}

	public List<FieldRendererDescriptor> getFields() {
		return fields;
	}
}
