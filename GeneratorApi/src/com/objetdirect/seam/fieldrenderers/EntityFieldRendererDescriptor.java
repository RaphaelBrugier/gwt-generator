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

import java.util.StringTokenizer;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.EntityHolder;
import com.objetdirect.seam.FieldRendererDescriptor;

public class EntityFieldRendererDescriptor extends FieldRendererDescriptor {

	public EntityFieldRendererDescriptor(
		String fieldName, 
		String fieldPath, 
		String fieldTitle, 
		EntityDescriptor entity,
		String labels,
		int length) 
	{
		super(fieldName, fieldPath, fieldTitle);
		this.entity = entity;
		this.length = length;
		this.labels = labels;
	}

	AttributeDescriptor entityList;
	MethodDescriptor getEntities;
	EntityDescriptor entity;
	String labels;
	int length;

	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor(); 
		entityList = getEntityListAttribute(javaClass);
		getEntities = getEntitiesMethod(javaClass);
	}

	protected AttributeDescriptor getEntityListAttribute(ClassDescriptor javaClass) {
		SemanticDescriptor semantic = new SemanticDescriptor("all", entity);
		AttributeDescriptor attr = javaClass.getAttributes().getAttribute(semantic);
		if (attr==null) {
			attr = new AttributeDescriptor();
			attr.init(javaClass, TypeDescriptor.List(entity.getClassDescriptor()),
				new StandardNameMaker(NamingUtil.toPlural(entity.getName())));
			attr.initToNull();
			attr.setSemantic(semantic);
			javaClass.addAttribute(attr);
		}
		return attr;
	}

	protected MethodDescriptor getEntitiesMethod(ClassDescriptor javaClass) {
		SemanticDescriptor semantic = new SemanticDescriptor("all", entity);
		MethodDescriptor meth = javaClass.getMethods().getMethod(semantic);
		if (meth==null) {
			meth = new MethodDescriptor();
			meth.init(javaClass, TypeDescriptor.List(entity.getClassDescriptor()), 
				new StandardNameMaker("get", NamingUtil.toPlural(entity.getName()), null))
				.addModifier("public")
				.addAnnotation(TypeDescriptor.SuppressWarnings, "\"unchecked\"");
			meth.setContent(
				"if (entities==null) {",
				"	entities = entityManager.createQuery(\"from EntityClass\").getResultList();",
				"}",
				"return entities;"
			);
			meth.setSemantic(semantic);
			meth.replace("entities", entityList);
			meth.replace("EntityClass", entity.getClassDescriptor());
			meth.replace("entityManager", getDocument().getEntityManager());
			javaClass.addMethod(meth);
		}
		return meth;
	}

	String getLabels(String labels) {
		StringTokenizer st = new StringTokenizer(labels, " ");
		String result = "row."+st.nextToken();
		while (st.hasMoreTokens()) {
			result+=" row."+st.nextToken();
		}
		return result;
	}

	public FragmentDescriptor buildFragment() {
		EntityHolder parent = getParent(EntityHolder.class);
		// to verify the path
		if (getFieldName()!=null)
			PathUtils.getGetters(parent.getEntityType().getClassDescriptor(), getFieldName());
		FragmentDescriptor fd = new FragmentDescriptor(
			"<h:selectOneMenu value=\"#{entity.field}\">",
			"	<s:selectItems value=\"#{beanName.entities}\" var=\"row\"  label=\"#{"+getLabels(labels)+"}\" noSelectionLabel=\"Choisissez...\"/>",
			"	<s:convertEntity />",
			"</h:selectOneMenu>"
		);
		if (getFieldName()!=null)
			fd.replace("field", getFieldName());
		else
			fd.remove(".field");	
		fd.replace("row", NamingUtil.toMember(entity.getName()));
		fd.replace("beanName", getDocument().getBeanName());
		fd.replaceProperty("entities", getEntities);
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
		String criterion = "new Criterion(\"criterionLabel\", \"criterionLabel : \", \"entity\").setItems(\"#{beanName.criterionItems}\").setLine(\"#{criterionLine}\")";
		criterion = Rewrite.replace(criterion, "criterionLabel", this.getFieldTitle());
		criterion = Rewrite.replace(criterion, "criterionItems", FragmentDescriptor.getProperty(this.getEntities));
		String label = getLabels(labels);
		criterion = Rewrite.replace(criterion, "criterionLine", Rewrite.replace(label, "row", "entity"));
		criterion = Rewrite.replace(criterion, "beanName", getDocument().getBeanName());
		return criterion;
	}
}
