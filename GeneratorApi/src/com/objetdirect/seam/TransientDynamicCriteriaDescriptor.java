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
import java.util.Map;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.BasicClassDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.CriteriaProcessor.Slot;
import com.objetdirect.seam.fieldrenderers.BooleanFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.DateFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.EntityFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.EnumFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.InputFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.NumberFieldRendererDescriptor;

public class TransientDynamicCriteriaDescriptor
	extends BaseComponent
	implements CriteriaDescriptor {

	String[] criteriaPattern = defaultCriteriaPattern;
	
	public TransientDynamicCriteriaDescriptor() {
	}

	public EntityDescriptor getEntity() {
		return getParent(EntityHolder.class).getEntityType();
	}
		
	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
		modifyGetListMethod();
	}
	
	protected TransientDynamicCriteriaDescriptor addField(FieldRendererDescriptor field) {
		field.setOwner(this);
		fields.add(field);
		return this;
	}
	
	public TransientDynamicCriteriaDescriptor editStringField(String fieldName, String fieldTitle, int length) {
		return addField(new InputFieldRendererDescriptor(null, fieldName, fieldTitle, length));
	}

	public TransientDynamicCriteriaDescriptor editDateField(String fieldName, String fieldTitle, String pattern) {
		return addField(new DateFieldRendererDescriptor(null, fieldName, fieldTitle, pattern));
	}

	public TransientDynamicCriteriaDescriptor editBooleanField(String fieldName, String fieldTitle) {
		return addField(new BooleanFieldRendererDescriptor(null, fieldName, fieldTitle));
	}

	public TransientDynamicCriteriaDescriptor editNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		return addField(new NumberFieldRendererDescriptor(null, fieldName, fieldTitle, pattern, length));
	}

	public TransientDynamicCriteriaDescriptor editEnumField(String fieldName, String fieldTitle, EnumDescriptor enumDesc, int length) {
		return addField(new EnumFieldRendererDescriptor(null, fieldName, fieldTitle, enumDesc, length));
	}

	public TransientDynamicCriteriaDescriptor editEntityField(String fieldName, String fieldTitle, EntityDescriptor entity, String labels, int length) {
		return addField(new EntityFieldRendererDescriptor(null, fieldName, fieldTitle, entity, labels, length));
	}
	
	public void buildFaceletPart() {
		FaceletDescriptor facelet = getDocument().getFragment();
		buildFaceletFragment(facelet);
	}

	protected void modifyGetListMethod() {
		CriteriaHolder parent = getParent(CriteriaHolder.class);
		parent.getListGetterMethod().replace("getCriteriaMethod", getCriteria);
	}
	
	MethodDescriptor validateCriteria;
	MethodDescriptor clearCriteria;
	MethodDescriptor getCriteria;
	
	MethodDescriptor getValidateCriteriaMethod() {
		return validateCriteria;
	}
	MethodDescriptor getClearCriteriaMethod() {
		return clearCriteria;
	}
	MethodDescriptor getCriteriaGetterMethod() {
		return getCriteria;
	}
	
	protected void buildJavaElements(ClassDescriptor javaClass) {
		buildFields(javaClass);
		selectedCriteria = buildSelectedCriteriaAttribute(javaClass);
		createAttributes(javaClass);
		criterionList = buildCriterionListAttribute(javaClass);
		selectedCriteriaGetter = buildSelectedCriteriaGetterMethod(javaClass);
		criterionListGetter = buildCriterionListGetterMethod(javaClass);
		remainingCriteria = buildRemainingCriteriaMethod(javaClass);
		selectedCriterion = buildSelectedCriterionAttribute(javaClass);
		selectedCriterionGetter = buildSelectedCriterionGetterMethod(javaClass);
		selectedCriterionSetter = buildSelectedCriterionSetterMethod(javaClass);
		getCriterion = buildGetCriterionMethod(javaClass);
		addCriterion = buildAddCriterionMethod(javaClass);
		removeCriterion = buildRemoveCriterionMethod(javaClass);
		validateCriteria = buildValidateCriteriaMethod(javaClass);
		clearCriteria = buildClearCriteriaMethod(javaClass);
		getCriteria = buildGetCriteriaMethod(javaClass);
	}

	protected MethodDescriptor buildValidateCriteriaMethod(ClassDescriptor javaClass) {
		CriteriaHolder parent = getParent(CriteriaHolder.class);
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
					new StandardNameMaker("validate", "Criteria", parent.getListAttribute()));
		meth.addModifier("public");
		meth.setContent("attribute = null;");
		meth.replace("attribute", parent.getListAttribute());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildClearCriteriaMethod(ClassDescriptor javaClass) {
		CriteriaHolder parent = getParent(CriteriaHolder.class);
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("clear", "Criteria", parent.getListAttribute()));
		meth.addModifier("public");
		meth.setContent("/// clearing here");
		for (FieldRendererDescriptor field : getFields()) {
			meth.insertLines("/// clearing here", "attribute.setValue(null);");
			meth.replace("attribute", field.getAttribute());
		}
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildGetCriteriaMethod(ClassDescriptor javaClass) {
		CriteriaHolder parent = getParent(CriteriaHolder.class);
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, Hibernate.Criteria, 
					new StandardNameMaker("get", "Criteria", parent.getListAttribute()));
		meth.setContent(
		    "HSession session = (HSession)entityMngr.getDelegate();",
		    "HCriteria c = session.createCriteria(Entity.class);",
		    "/// insert criterions here",
		    "return c;"
		);
		Slot rootSlot = new CriteriaProcessor().process(
			getEntity(), getFields(), attributes);
		String[] content = process(meth.getContent(), rootSlot, "c", true);
		meth.setContent(content);
		meth.replace("HSession", Hibernate.Session);
		meth.replace("HCriteria", Hibernate.Criteria);
		meth.replace("entityMngr", getDocument().getEntityManager());
		meth.replace("Entity", getEntity().getClassDescriptor());
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor getGetter(AttributeDescriptor attr) {
		for (FieldRendererDescriptor field : fields) {
			if (field.getAttribute()==attr)
				return field.getGetter();
		}
		return null;
	}
	
	protected String[] process(String[] target, Slot slot, String slotName, boolean root) {
		for (Map.Entry<String, AttributeDescriptor> entry : slot.attributes.entrySet()) {
			boolean unique = slot.getInvolvedAttributes().size()==1;
			AttributeDescriptor attr = entry.getValue();
			String[] pattern = getAttributePattern(root, unique, attr);
			pattern = Rewrite.replace(pattern, "fieldVar", entry.getKey());
			pattern = Rewrite.replace(pattern, "criteriaVar", slotName);
			pattern = Rewrite.replace(pattern, "getterVar", getGetter(entry.getValue()).getName());
			getClassDescriptor().getImportSet().addType(Hibernate.Restrictions);
			pattern = Rewrite.replace(pattern, "HRestrictions", Hibernate.Restrictions.getUsageName(getClassDescriptor()));
			target = Rewrite.insertLines(target, "/// insert criterions here", pattern);
		}
		for (Map.Entry<String, Slot> entry : slot.slots.entrySet()) {
			Slot child = entry.getValue();
			String criteriaName = "c"+child.number;
			int i1 = slot.getInvolvedAttributes().size();
			int i2 = child.getInvolvedAttributes().size();
			String[] pattern = getSubcriteriaPattern(entry, root, i1==i2);
			getClassDescriptor().getImportSet().addType(Hibernate.Criteria);
			pattern = Rewrite.replace(pattern, "HCriteria", 
					Hibernate.Criteria.getUsageName(getClassDescriptor()));
			pattern = Rewrite.replace(pattern, "parentCriteria", slotName);
			pattern = Rewrite.replace(pattern, "criteriaName", criteriaName);
			pattern = Rewrite.replace(pattern, "fieldVar", entry.getKey());
			pattern = process(pattern, child, criteriaName, false);
			pattern = Rewrite.removeLine(pattern, "/// insert criterions here");
			target = Rewrite.insertLines(target, "/// insert criterions here", pattern);
		}
		return target;
	}

	String[] getSubcriteriaPattern(Map.Entry<String, Slot> entry, boolean root, boolean unique) {
		String[] pattern;
		if (unique && !root)
			pattern = new String[] {
				"HCriteria criteriaName = parentCriteria.createCriteria(\"fieldVar\");",
				"/// insert criterions here"
			};
		else {
			pattern = new String[] {
				"if (attributeCondition) {",
				"	HCriteria criteriaName = parentCriteria.createCriteria(\"fieldVar\");",
				"	/// insert criterions here",
				"}"
			};
			List<AttributeDescriptor> attrs = entry.getValue().getInvolvedAttributes(); 
			int i=0;
			for (AttributeDescriptor attr : attrs) {
				String[] condPattern = getConditionPattern(i++, attrs, attr);
				pattern = Rewrite.replaceAndInsertLines(pattern, 
					"attributeCondition", condPattern);
			}
		}
		return pattern;
	}

	String[] getAttributePattern(boolean root, boolean unique,
			AttributeDescriptor attr) {
		MethodDescriptor getter = getGetter(attr);
		String[] pattern;
		if (getter.getReturnType()==TypeDescriptor.String) {
			if (unique && !root)
				pattern = new String[] {
					"criteriaVar.add(HRestrictions.like(\"fieldVar\", getterVar()+\"%\"));"
				};
			else
				pattern = new String[] {
					"if (getterVar() != null && getterVar().length()>0)",
					"	criteriaVar.add(HRestrictions.like(\"fieldVar\", getterVar()+\"%\"));"
				};
		}
		else
			if (unique && !root)
				pattern = new String[] {
					"if (getterVar() != null)",
					"	criteriaVar.add(HRestrictions.eq(\"fieldVar\", getterVar()));"
				};
			else
				pattern = new String[] {
					"if (getterVar() != null)",
					"	criteriaVar.add(HRestrictions.eq(\"fieldVar\", getterVar()));"
				};
		return pattern;
	}

	protected String[] getConditionPattern(int i, List<AttributeDescriptor> attrs,
			AttributeDescriptor attr) {
		MethodDescriptor getter = getGetter(attr);
		String[] condPattern = null;
		if (i==0 && i==attrs.size()-1) {
			if (getter.getReturnType()==TypeDescriptor.String)
				condPattern = new String[] {"getterVar() != null && getterVar().length()>0"};
			else
				condPattern = new String[] {"getterVar() != null"};
		}
		else {
			String condition;
			if (getter.getReturnType()==TypeDescriptor.String)
				condition = "(getterVar() != null && getterVar().length()>0)";
			else
				condition = "getterVar() != null";
			if (i==0) {
				condPattern = new String[] {
					condition+" ||", 
					"	attributeCondition"
				};
			}
			else if (i==attrs.size()-1)
				condPattern = new String[] {condition};
			else {
				condPattern = new String[] {
						condition+" ||", 
						"attributeCondition"
					};
			}
		}
		condPattern = Rewrite.replace(condPattern, "getterVar", getGetter(attr).getName());
		return condPattern;
	}
	
	FragmentDescriptor fragment;
	
	protected void buildFaceletFragment(FaceletDescriptor facelet) {
		setFragment(new FragmentDescriptor(this.criteriaPattern));
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceProperty("selectedCriterion", selectedCriterionGetter);
		getFragment().replaceProperty("remainingCriteria", remainingCriteria);
		getFragment().replaceMethod("addCriterion", addCriterion);
		getFragment().replaceMethod("removeCriterion", removeCriterion);
		getFragment().replaceProperty("selectedCriteria", selectedCriteriaGetter);
		getFragment().replaceMethod("validateCriteria", validateCriteria);
		getFragment().replaceMethod("clearCriteria", clearCriteria);
	}

	public String[] getInitText() {
		String[] pattern = {"getCriteriaMethod().list()"};
		pattern = Rewrite.replace(pattern, "entityManager", getDocument().getEntityManager().getName());
		pattern = Rewrite.replace(pattern,"EntityClass", getEntity().getClassDescriptor().getTypeName());
		return pattern;
	}

	List<FieldRendererDescriptor> fields = new ArrayList<FieldRendererDescriptor>();
	
	public List<FieldRendererDescriptor> getFields() {
		return fields;
	}
	
	List<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();

	protected void buildFields(ClassDescriptor javaClass) {
		for (FieldRendererDescriptor field : fields) {
			field.buildJavaPart();
		}
	}

	protected void createAttributes(ClassDescriptor javaClass) {
		for (FieldRendererDescriptor field : fields) {
			AttributeDescriptor fieldAttr = PathUtils.getAttribute(
				(ClassDescriptor)field.getStartType(),
				field.getFieldPath());
			String attributeName = "criteria"+NamingUtil.pathToName(field.getFieldPath());
			AttributeDescriptor attr = new AttributeDescriptor();
			attr.init(
				javaClass, Frmk.Criterion, attributeName).initWithPattern(field.getCriterion());
			javaClass.addAttribute(attr);
			attributes.add(attr);
			MethodDescriptor getter = buildGetter(javaClass, fieldAttr, attr);
			MethodDescriptor setter = buildSetter(javaClass, fieldAttr, attr);
			field.setAttribute(attr);
			field.setGetter(getter);
			field.setSetter(setter);
		}
	}
	
	MethodDescriptor buildGetter(ClassDescriptor javaClass, AttributeDescriptor field, AttributeDescriptor attribute) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			(BasicClassDescriptor)attribute.getOwner(), field.getType(), 
			new StandardNameMaker("get", null, attribute));
		meth.addModifier("public");
		meth.setContent(
			"if (!selectedCriteria.contains(attribute))",
			"	return null;",
			"return (FieldType)attribute.getValue();"
		);
		meth.replace("selectedCriteria", selectedCriteria);
		meth.replace("attribute", attribute);
		meth.replace("FieldType", field.getType());
		meth.setSemantic(new SemanticDescriptor("get", attribute));
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor buildSetter(ClassDescriptor javaClass, AttributeDescriptor field, AttributeDescriptor attribute) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			(BasicClassDescriptor)attribute.getOwner(), TypeDescriptor.rVoid, 
			new StandardNameMaker("set", null, attribute));
		meth.addParameter(field.getType(), field.getName());
		meth.addModifier("public");
		meth.setContent(
			"this.attribute.setValue(field);"
		);
		meth.replace("attribute", attribute);
		meth.replace("field", field);
		meth.setSemantic(new SemanticDescriptor("set", attribute));
		javaClass.addMethod(meth);
		return meth;
	}
	
	AttributeDescriptor criterionList;

	public AttributeDescriptor getCriterionListAttribute() {
		return criterionList;	
	}
	
	protected AttributeDescriptor buildCriterionListAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.List(Frmk.Criterion),
				new StandardNameMaker("criteria", null, null))
			.initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
	
	AttributeDescriptor selectedCriteria;

	public AttributeDescriptor getSelectedCriteriaAttribute() {
		return selectedCriteria;	
	}
	
	protected AttributeDescriptor buildSelectedCriteriaAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.List(Frmk.Criterion),
				new StandardNameMaker("selectedCriteria", null, null))
			.initWithNew(TypeDescriptor.ArrayList(Frmk.Criterion));
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor selectedCriteriaGetter;

	public MethodDescriptor getSelectedCriteriaGetterMethod() {
		return selectedCriteriaGetter;	
	}
	
	protected MethodDescriptor buildSelectedCriteriaGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.getter(selectedCriteria, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor criterionListGetter;

	public MethodDescriptor getCriterionListGetterMethod() {
		return criterionListGetter;	
	}
	
	protected MethodDescriptor buildCriterionListGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			javaClass, criterionList.getType(), 
			new StandardNameMaker("get", null, criterionList));
		meth.setContent(
			"if (criterionList==null) {",
			"	criterionList = new ArrayList<Criterion>();",
			"	/// insert add criterion here",
			"}",
			"return criterionList;"
		);
		for (AttributeDescriptor attr : attributes) {
			meth.insertLines("/// insert add criterion here", "criterionList.add(attribute);");
			meth.replace("attribute", attr);
		}
		meth.replace("criterionList", criterionList);
		meth.replace("ArrayList", TypeDescriptor.ArrayList(null));
		meth.replace("Criterion", Frmk.Criterion);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor remainingCriteria;

	public MethodDescriptor getRemainingCriteriaMethod() {
		return remainingCriteria;	
	}
	
	protected MethodDescriptor buildRemainingCriteriaMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			javaClass, criterionList.getType(), 
			new StandardNameMaker("getRemainingCriteria", null, null));
		meth.setContent(
			"List<String> result = new ArrayList<String>();",
			"for (Criterion c : getCriterionList()) {",
			"	if (!selectedCriteria.contains(c))",
			"		result.add(c.getName());",
			"}",
			"return result;"
		);
		meth.replace("List", TypeDescriptor.List(null));
		meth.replace("String", TypeDescriptor.String);
		meth.replace("ArrayList", TypeDescriptor.ArrayList(null));
		meth.replace("Criterion", Frmk.Criterion);
		meth.replace("getCriterionList", criterionListGetter);
		meth.replace("selectedCriteria", selectedCriteria);
		javaClass.addMethod(meth);
		return meth;
	}
	
	AttributeDescriptor selectedCriterion;

	public AttributeDescriptor getSelectedCriterionAttribute() {
		return selectedCriterion;	
	}
	
	protected AttributeDescriptor buildSelectedCriterionAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, Frmk.Criterion, new StandardNameMaker("selectedCriterion", null, null))
			.initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}

	MethodDescriptor selectedCriterionGetter;

	public MethodDescriptor getSelectedCriterionGetterMethod() {
		return selectedCriterionGetter;	
	}
	
	protected MethodDescriptor buildSelectedCriterionGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.getter(selectedCriterion, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	MethodDescriptor selectedCriterionSetter;

	public MethodDescriptor getSelectedCriterionSetterMethod() {
		return selectedCriterionSetter;	
	}
	
	protected MethodDescriptor buildSelectedCriterionSetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.setter(selectedCriterion, "public");
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor getCriterion;

	public MethodDescriptor getGetCriterionMethod() {
		return getCriterion;	
	}
	
	protected MethodDescriptor buildGetCriterionMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			javaClass, Frmk.Criterion, 
			new StandardNameMaker("getCriterion", null, null));
		meth.addParameter(TypeDescriptor.String, "criterionName");
		meth.setContent(
			"for (Criterion criterion : getCriterionList()) {",
			"	if (criterion.getName().equals(criterionName))",
			"		return criterion;",
			"}",
			"return null;"
		);
		meth.replace("Criterion", Frmk.Criterion);
		meth.replace("getCriterionList", criterionListGetter);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor addCriterion;

	public MethodDescriptor getAddCriterionMethod() {
		return addCriterion;	
	}
	
	protected MethodDescriptor buildAddCriterionMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			javaClass, TypeDescriptor.rVoid,
			new StandardNameMaker("addCriterion", null, null));
		meth.addModifier("public");
		meth.setContent(
			"if (selectedCriterion!=null) {",
			"	selectedCriteria.add(getCriterion(selectedCriterion));",
			"	selectedCriterion = null;",
			"}"
		);
		meth.replace("selectedCriterion", selectedCriterion);
		meth.replace("selectedCriteria", selectedCriteria);
		meth.replace("getCriterion", getCriterion);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor removeCriterion;

	public MethodDescriptor getRemoveCriterionMethod() {
		return removeCriterion;
	}
	
	protected MethodDescriptor buildRemoveCriterionMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(
			javaClass, TypeDescriptor.rVoid,
			new StandardNameMaker("removeCriterion", null, null));
		meth.addModifier("public");
		meth.addParameter(Frmk.Criterion, "criterion");
		meth.setContent(
			"if (criterion!=null) {",
			"	selectedCriteria.remove(criterion);",
			"}"
		);
		meth.replace("selectedCriteria", selectedCriteria);
		javaClass.addMethod(meth);
		return meth;
	}
	
	static final String[] defaultCriteriaPattern = {
		"	<h:panelGroup>",
		"		<s:validateAll>",
		"			<h:panelGroup>",	
		"				<h:outputText value=\"Liste des criteres : \" />",
		"				<s:decorate template=\"/layout/edit.xhtml\">",
		"					<h:selectOneMenu value=\"#{beanName.selectedCriterion}\">",
		"						<s:selectItems value=\"#{beanName.remainingCriteria}\" var=\"criterionName\"  label=\"#{criterionName}\" noSelectionLabel=\"Choisissez...\"/>",
		"					</h:selectOneMenu>",
		"					<h:commandButton value=\"ajouter\" action=\"#{beanName.addCriterion}\"/>",
		"				</s:decorate>",
		"			</h:panelGroup>",
		"			<h:panelGrid columns=\"1\">",
		"				<ui:repeat value=\"#{beanName.selectedCriteria}\" var=\"criterion\">",
		"					<h:panelGrid columns=\"3\">",
		"						<h:outputText value=\"#{criterion.label}\"/>",
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='text'}\">",
		"							<h:inputText value=\"#{criterion.value}\" size=\"#{criterion.size}\"/>",
		"						</s:decorate>",
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='number'}\">",
		"							<h:inputText value=\"#{criterion.value}\" size=\"#{criterion.size}\">",
		"								<f:convertNumber pattern=\"#{criterion.pattern}\" />",
		"							</h:inputText>",
		"						</s:decorate>",					
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='enumList'}\">",
		"							<h:selectOneMenu value=\"#{criterion.value}\">",
		"								<s:selectItems value=\"#{criterion.items}\" var=\"type\"  label=\"#{type.label}\" noSelectionLabel=\"Choisissez...\"/>",
		"								<s:convertEnum />",
		"							</h:selectOneMenu>",
		"						</s:decorate>",
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='enumRadio'}\">",
		"							<h:selectOneRadio value=\"#{criterion.value}\">",
		"								<s:selectItems value=\"#{criterion.items}\" var=\"type\"  label=\"#{type.label}\" noSelectionLabel=\"Choisissez...\"/>",
		"								<s:convertEnum />",
		"							</h:selectOneRadio>",
		"						</s:decorate>",
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='entity'}\">",
		"							<h:selectOneMenu value=\"#{criterion.value}\">",
		"								<s:selectItems value=\"#{criterion.items}\" var=\"entity\"  label=\"#{criterion.line}\" noSelectionLabel=\"Choisissez...\"/>",
		"								<s:convertEntity />",
		"							</h:selectOneMenu>",
		"						</s:decorate>",
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='boolean'}\">",
		"							<h:selectBooleanCheckbox value=\"#{criterion.value}\"/>",
		"						</s:decorate>",
		"						<s:decorate template=\"/layout/edit.xhtml\" rendered=\"#{criterion.type=='date'}\">",
		"							<ice:selectInputDate value=\"#{criterion.value}\" renderAsPopup=\"true\">",
		"								<f:convertDateTime pattern=\"#{criterion.pattern}\"/>",
		"							</ice:selectInputDate>",
		"						</s:decorate>",
		"					<h:commandButton value=\"del\" action=\"#{beanName.removeCriterion(criterion)}\"/>",
		"				</h:panelGrid>",
		"			</ui:repeat>",
		"		</h:panelGrid>",
		"	</s:validateAll>",
		"	<div class=\"actionButtons\">",
		"		<h:commandButton value=\"valider\" action=\"#{beanName.validateCriteria}\"/>",
		"		<h:commandButton value=\"reinitialiser\" action=\"#{beanName.clearCriteria}\"/>",
		"	</div>",
		"</h:panelGroup>"
	};
	
}
