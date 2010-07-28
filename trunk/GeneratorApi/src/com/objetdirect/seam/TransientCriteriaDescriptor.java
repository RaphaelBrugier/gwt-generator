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

import java.util.List;
import java.util.Map;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.CriteriaProcessor.Slot;

public class TransientCriteriaDescriptor
	extends BaseComponent
	implements CriteriaDescriptor, FormHolder {

	String[] criteriaPattern = defaultCriteriaPattern;
	
	public TransientCriteriaDescriptor() {
	}

	public EntityDescriptor getEntity() {
		return getParent(EntityHolder.class).getEntityType();
	}
		
	public void buildJavaPart() {
		form.buildJavaPart();
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
		modifyGetListMethod();
	}
	
	public void buildFaceletPart() {
		form.buildFaceletPart();
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
		for (AttributeDescriptor attr : form.getAttributes()) {
			meth.insertLines("/// clearing here", "attribute = null;");
			meth.replace("attribute", attr);
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
			getEntity(), form.getFields(), form.getAttributes());
		String[] content = process(meth.getContent(), rootSlot, "c", true);
		meth.setContent(content);
		meth.replace("HSession", Hibernate.Session);
		meth.replace("HCriteria", Hibernate.Criteria);
		meth.replace("entityMngr", getDocument().getEntityManager());
		meth.replace("Entity", getEntity().getClassDescriptor());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected String[] process(String[] target, Slot slot, String slotName, boolean root) {
		for (Map.Entry<String, AttributeDescriptor> entry : slot.attributes.entrySet()) {
			boolean unique = slot.getInvolvedAttributes().size()==1;
			AttributeDescriptor attr = entry.getValue();
			String[] pattern = getAttributePattern(root, unique, attr);
			pattern = Rewrite.replace(pattern, "fieldVar", entry.getKey());
			pattern = Rewrite.replace(pattern, "criteriaVar", slotName);
			pattern = Rewrite.replace(pattern, "attributeVar", entry.getValue().getName());
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
		String[] pattern;
		if (attr.getType()==TypeDescriptor.String) {
			if (unique && !root)
				pattern = new String[] {
					"criteriaVar.add(HRestrictions.like(\"fieldVar\", attributeVar+\"%\"));"
				};
			else
				pattern = new String[] {
					"if (attributeVar != null && attributeVar.length()>0)",
					"	criteriaVar.add(HRestrictions.like(\"fieldVar\", attributeVar+\"%\"));"
				};
		}
		else
			if (unique && !root)
				pattern = new String[] {
					"if (attributeVar != null)",
					"	criteriaVar.add(HRestrictions.eq(\"fieldVar\", attributeVar));"
				};
			else
				pattern = new String[] {
					"if (attributeVar != null)",
					"	criteriaVar.add(HRestrictions.eq(\"fieldVar\", attributeVar));"
				};
		return pattern;
	}

	protected String[] getConditionPattern(int i, List<AttributeDescriptor> attrs,
			AttributeDescriptor attr) {
		String[] condPattern = null;
		if (i==0 && i==attrs.size()-1) {
			if (attr.getType()==TypeDescriptor.String)
				condPattern = new String[] {"attributeVar != null && attributeVar.length()>0"};
			else
				condPattern = new String[] {"attributeVar != null"};
		}
		else {
			String condition;
			if (attr.getType()==TypeDescriptor.String)
				condition = "(attributeVar != null && attributeVar.length()>0)";
			else
				condition = "attributeVar != null";
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
		condPattern = Rewrite.replace(condPattern, "attributeVar", attr.getName());
		return condPattern;
	}
	
	protected void buildFaceletFragment(FaceletDescriptor facelet) {
		setFragment(new FragmentDescriptor(this.criteriaPattern));
		getFragment().insertFragment("/// insert form here", form.getFragment());
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceMethod("validateCriteria", validateCriteria);
		getFragment().replaceMethod("clearCriteria", clearCriteria);
	}

	public String[] getInitText() {
		String[] pattern = {"getCriteriaMethod().list()"};
		pattern = Rewrite.replace(pattern, "entityManager", getDocument().getEntityManager().getName());
		pattern = Rewrite.replace(pattern,"EntityClass", getEntity().getClassDescriptor().getTypeName());
		return pattern;
	}

	FormDescriptor form;
	
	public TransientCriteriaDescriptor setForm(FormDescriptor form) {
		this.form = form;
		form.setOwner(this);
		return this;
	}

	public String getNamePrefix() {
		return NamingUtil.toMember(getEntity().getName());
	}

	public String getNameSuffix() {
		return "Criterion";
	}

	public MethodDescriptor getInitFormMethod() {
		return null;
	}

	public String getInitFormTag() {
		return null;
	}
	
	public MethodDescriptor getFillFormMethod() {
		return null;
	}

	public String getFillFormTag() {
		return null;
	}

	public String getInitTarget() {
		return null;
	}

	public MethodDescriptor[] getSubmitFormMethods() {
		return null;
	}

	public String getSubmitFormTag() {
		return null;
	}

	public String getSubmitTarget() {
		return null;
	}

	public MethodDescriptor getValidateFormMethod() {
		return null;
	}

	public String getValidateFormTag() {
		return null;
	}

	public String getValidateTarget() {
		return null;
	}
	
	public MethodDescriptor[] getCheckDirtyMethods() {
		return null;
	}

	public String getCheckDirtyTag() {
		return null;
	}
	
	static final String[] defaultCriteriaPattern = {
		"<h:panelGroup>",
		"	/// insert form here",
		"	<div class=\"actionButtons\">",
		"		<h:commandButton value=\"valider\" action=\"#{beanName.validateCriteria}\"/>",
		"		<h:commandButton value=\"reinitialiser\" action=\"#{beanName.clearCriteria}\"/>",
		"	</div>",
		"</h:panelGroup>"
	};
}
