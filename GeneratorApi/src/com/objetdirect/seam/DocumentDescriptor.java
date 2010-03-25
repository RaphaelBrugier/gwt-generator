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

import java.util.HashSet;
import java.util.Set;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.BasicClassDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.NameMaker;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.entities.JavaxPersistence;

public abstract class DocumentDescriptor implements Component {

	String classPackageName;
	String className;
	String viewPackageName;
	String viewName;
	ClassDescriptor javaClass;
	FaceletDescriptor facelet;
	String beanName;
	AttributeDescriptor entityManager;
	ConstructorDescriptor javaConstructor;
	
	public DocumentDescriptor(
		String classPackageName, String className, 
		String viewPackageName, String viewName) 
	{
		this.classPackageName = classPackageName;
		this.className = className;
		this.viewPackageName = viewPackageName;
		this.viewName = viewName;
		beanName = buildBeanName(viewPackageName, className);
	}

	public void build() 
	{
		buildJavaPart();
		buildFaceletPart();
	}

	public void buildJavaPart() 
	{
		javaClass = buildJavaClass(classPackageName, className);
		javaConstructor = buildJavaConstructor(javaClass);
		if (javaConstructor!=null)
			javaClass.addConstructor(javaConstructor);
		if (feature!=null) {
			buildFeatureJavaPart(feature);
		}
	}

	protected void buildFeatureJavaPart(DocumentFeature feature) {
		feature.buildJavaPart();
	}
	
	public void buildFaceletPart() 
	{
		facelet = buildFacelet(viewPackageName, viewName);
		if (feature!=null) {
			buildFeatureFaceletPart(feature);
			facelet.insertFragment("/// page content here", feature.getFragment());
		}
	}
	
	protected void buildFeatureFaceletPart(DocumentFeature feature) {
		feature.buildFaceletPart();
	}

	protected String getScopeType() {
		return "ScopeType.CONVERSATION";
	}
	
	protected ClassDescriptor buildJavaClass(String packageName, String name) {
		ClassDescriptor javaClass = new ClassDescriptor(packageName, new StandardNameMaker(name, "Animator", null));
		javaClass.addSemantic(new SemanticDescriptor("implements", this));
		javaClass.addAnnotation(Seam.Name, "\""+getBeanName()+"\"");
		javaClass.addAnnotation(Seam.Scope, getScopeType());
		javaClass.replace("ScopeType", Seam.ScopeType);
		return javaClass;
	}
	
	public String[] getJavaText() {
		return javaClass.getText();
	}
	
	public String[] getFaceletText() {
		return facelet.getText();
	}

	public String getBeanName() {
		return beanName;
	}
	
	public String getUrl() {
		return "/"+viewPackageName+"/"+viewName+".xhtml";
	}
	
	DocumentFeature feature = null;
	
	public void setFeature(DocumentFeature feature) {
		this.feature = feature;
		feature.setOwner(this);
	}

	public DocumentFeature getFeature() {
		return feature;
	}
	
	public ClassDescriptor getClassDescriptor() {
		return javaClass;
	}
	
	public void declareEntityManager() {
		if (entityManager==null) {
			entityManager = new AttributeDescriptor();
			entityManager.init(javaClass, JavaxPersistence.EntityManager, "entityManager");
			entityManager.addAnnotation(Seam.In);
			javaClass.addAttribute(entityManager);
		}
	}
	
	public AttributeDescriptor getEntityManager() {
		declareEntityManager();
		return entityManager;
	}
	
	public FaceletDescriptor getFragment() {
		return facelet;
	}

	protected String buildBeanName(String packageName, String className) {
		return Seam.getName(packageName, className);
	}
	
	protected ConstructorDescriptor buildJavaConstructor(ClassDescriptor javaClass) {
		return null;
	}
	
	protected abstract FaceletDescriptor buildFacelet(String packageName, String name);
	
	public DocumentDescriptor getDocument() {
		return this;
	}
	

	public String makeId(String name){
		return makeId(new StandardNameMaker(name));
	}
	
	public String makeId(NameMaker nameMaker){
		while (true) {
			String name = nameMaker.makeMemberName();
			if (!ids.contains(name)) {
				ids.add(name);
				return name;
			}
		}
	}
	
	Set<String> ids = new HashSet<String>(); 
	
	public <T> T getParent(Class<T> type) {
		return null;
	}

	public static DocumentDescriptor getDocumentDescriptor(BasicClassDescriptor clazz) {
		SemanticDescriptor semantic = clazz.getSemantic("implements");
		if (semantic==null)
			return null;
		if (semantic.getParam(0) instanceof DocumentDescriptor)
			return (DocumentDescriptor)semantic.getParam(0);
		return null;
	}
	
}
