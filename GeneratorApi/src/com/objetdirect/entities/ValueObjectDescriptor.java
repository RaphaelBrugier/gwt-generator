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

package com.objetdirect.entities;

import java.util.HashMap;
import java.util.Map;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.TypeDescriptor;

public class ValueObjectDescriptor {

	EntityDescriptor entityDesc;
	String packageName;
	
	ClassDescriptor valueObjectClass;
		
	public ValueObjectDescriptor(String packageName, EntityDescriptor entityDesc) {
		this.packageName = packageName;
		this.entityDesc = entityDesc;
		init();
	}
	
	void init() {
		valueObjectClass = new ClassDescriptor(packageName, entityDesc.getClassName()+"VO");
		valueObjectClass.addSemantic(new SemanticDescriptor("copy", entityDesc));
		entityDesc.getClassDescriptor().addSemantic(new SemanticDescriptor("copy", this));
		if (entityDesc.isSecured())
			valueObjectClass.setSuperClass(Frmk.BaseValueObject);
		else
			valueObjectClass.addInterface(TypeDescriptor.Serializable);
		if (!entityDesc.isSecured())
			initStandardAttributes();
		initTechnicalConstructor();
		initBusinessConstructor();
		initRemoveMethod();
	}

	public static ValueObjectDescriptor getVODescriptor(EntityDescriptor entityClass) {
		return (ValueObjectDescriptor)entityClass.getClassDescriptor().getSemantic("copy").getParam(0);
	}
	
	public EntityDescriptor getEntityDescriptor() {
		return entityDesc;
	}
	
	public String getName() {
		return entityDesc.getClassName()+"VO";
	}
	
	AttributeDescriptor id;
	AttributeDescriptor version;
	MethodDescriptor getId;
	MethodDescriptor getVersion;
	MethodDescriptor setId;
	MethodDescriptor setVersion;
	MethodDescriptor loadId;
	MethodDescriptor loadVersion;
	ConstructorDescriptor technicalConstructor;
	ConstructorDescriptor businessConstructor;
	MethodDescriptor remove;

	protected void initStandardAttributes() {
		id = new AttributeDescriptor();
		id.init(valueObjectClass, TypeDescriptor.rInt, "id");
		valueObjectClass.addAttribute(id);
		version = new AttributeDescriptor();
		version.init(valueObjectClass, TypeDescriptor.rInt, "version");
		valueObjectClass.addAttribute(version);
		getId = StandardMethods.getter(id, "public");
		setId = StandardMethods.setter(id, "public");
		loadId = StandardMethods.loader(id, "public");
		valueObjectClass.addMethod(getId);
		valueObjectClass.addMethod(setId);
		valueObjectClass.addMethod(loadId);
		getVersion = StandardMethods.getter(version, "public");
		setVersion = StandardMethods.setter(version, "public");
		loadVersion = StandardMethods.loader(version, "public");
		valueObjectClass.addMethod(getVersion);
		valueObjectClass.addMethod(setVersion);
		valueObjectClass.addMethod(loadVersion);
	}

	protected void initTechnicalConstructor() {
		technicalConstructor = new ConstructorDescriptor();
		technicalConstructor.init(valueObjectClass).addModifier("public");
		valueObjectClass.addConstructor(technicalConstructor);
	}
	
	protected void initBusinessConstructor() {
		businessConstructor = new ConstructorDescriptor(){
			public String[] getText() {
				removeLine("/// initializations here");
				return super.getText();
			}
		};
		businessConstructor.init(valueObjectClass).addModifier("public");
		businessConstructor.setContent("/// initializations here");
		valueObjectClass.addConstructor(businessConstructor);
	}
	
	public String[] getText() {
		initFields();
		initRelationships();
		adjustBusinessConstructor();
		return valueObjectClass.getText();
	}
	
	protected void adjustBusinessConstructor() {
		if (!entityDesc.businessConstructorHasParameters())
			businessConstructor.addParameter(TypeDescriptor.rBoolean, "dummy");
	}
	
	public ClassDescriptor getClassDescriptor() {
		return valueObjectClass;
	}
	
	Map<FieldDescriptor,AttributeDescriptor> fieldAttrs = new HashMap<FieldDescriptor,AttributeDescriptor>();
	Map<FieldDescriptor,MethodDescriptor> fieldGetters = new HashMap<FieldDescriptor,MethodDescriptor>();
	Map<FieldDescriptor,MethodDescriptor> fieldSetters = new HashMap<FieldDescriptor,MethodDescriptor>();
	Map<FieldDescriptor,MethodDescriptor> fieldLoaders = new HashMap<FieldDescriptor,MethodDescriptor>();
	
	protected void initFields() {
		for (FieldDescriptor field : entityDesc.getFields()) {
			AttributeDescriptor attr = buildFieldAttribute(field);
			MethodDescriptor getter = StandardMethods.getter(attr, "public");
			MethodDescriptor setter = StandardMethods.setter(attr, "public");
			MethodDescriptor loader = StandardMethods.loader(attr, "public");
			if (entityDesc.isSecured()) {
				getter.insertLines("/// insert pre-processing here", "verifyRead();");
				setter.insertLines("/// insert pre-processing here", "verifyWrite();");
			}
			fieldAttrs.put(field, attr);
			fieldGetters.put(field, getter);
			fieldSetters.put(field, setter);
			fieldLoaders.put(field, loader);
			valueObjectClass.addAttribute(attr);
			valueObjectClass.addMethod(getter);
			valueObjectClass.addMethod(setter);
			valueObjectClass.addMethod(loader);
			field.addInitialization(businessConstructor);
		}
	}
	
	protected AttributeDescriptor buildFieldAttribute(FieldDescriptor field) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(getClassDescriptor(), field.getType(), field.getName());
		attr.setSemantic(new SemanticDescriptor("implements", field));
		return attr;
	}
	
	public ValueObjectDescriptor getTarget(RelationshipDescriptor relationship) {
		EntityDescriptor entityDesc = relationship.getTarget();
		return (ValueObjectDescriptor)entityDesc.getClassDescriptor().getSemantic("copy").getParam(0);
	}

	Map<RelationshipDescriptor,AttributeDescriptor> relationshipAttrs = new HashMap<RelationshipDescriptor,AttributeDescriptor>();
	Map<RelationshipDescriptor,MethodDescriptor> relationshipGetters = new HashMap<RelationshipDescriptor,MethodDescriptor>();
	Map<RelationshipDescriptor,MethodDescriptor> relationshipLoaders = new HashMap<RelationshipDescriptor,MethodDescriptor>();
	Map<ReferenceDescriptor,MethodDescriptor> relationshipSetters = new HashMap<ReferenceDescriptor,MethodDescriptor>();
	Map<ReferenceListDescriptor,MethodDescriptor> relationshipAdders = new HashMap<ReferenceListDescriptor,MethodDescriptor>();
	Map<ReferenceListDescriptor,MethodDescriptor> relationshipRemovers = new HashMap<ReferenceListDescriptor,MethodDescriptor>();
	
	protected void initRelationships() {
		for (RelationshipDescriptor relationship : entityDesc.getRelationships()) {
			AttributeDescriptor attr = relationship.makeAttribute(getClassDescriptor(), getTarget(relationship).getClassDescriptor());
			MethodDescriptor getter = relationship.makeGetter(entityDesc, attr);
			MethodDescriptor loader = StandardMethods.loader(attr, "public");
			relationshipAttrs.put(relationship, attr);
			relationshipGetters.put(relationship, getter);
			relationshipLoaders.put(relationship, loader);
			valueObjectClass.addAttribute(attr);
			valueObjectClass.addMethod(getter);
			valueObjectClass.addMethod(loader);
			if (relationship instanceof ReferenceDescriptor) {
				ReferenceDescriptor reference = (ReferenceDescriptor)relationship;
				MethodDescriptor setter = reference.makeSetter(entityDesc, attr);
				relationshipSetters.put(reference, setter);
				valueObjectClass.addMethod(setter);
				reference.adjustBusinessConstructor(businessConstructor, attr, setter);
				if (relationship.getReverse()!=null) {
					reference.modifySetter(setter, true, relationship.getReverse());
				}
			}
			else if (relationship instanceof ReferenceListDescriptor) {
				ReferenceListDescriptor referenceList = (ReferenceListDescriptor)relationship;
				MethodDescriptor adder = referenceList.makeAdder(entityDesc, true, attr);
				MethodDescriptor remover = referenceList.makeRemover(entityDesc, true, attr);
				relationshipAdders.put(referenceList, adder);
				relationshipRemovers.put(referenceList, remover);
				valueObjectClass.addMethod(adder);	
				valueObjectClass.addMethod(remover);	
				referenceList.adjustBusinessConstructor(businessConstructor, attr);
				if (relationship.getReverse()!=null) {
					referenceList.modifyAdder(adder, true, relationship.getReverse());
					referenceList.modifyRemover(remover, true, relationship.getReverse());
				}
			}
			if (relationship.getReverse()!=null) {
				relationship.modifyPreRemove(remove, true, relationship.getReverse());
			}
		}
	}
	
	AttributeDescriptor inDeletion = null;
	
	public AttributeDescriptor buildInDeletion(ClassDescriptor owner) {
		AttributeDescriptor inDeletion = new AttributeDescriptor();
		inDeletion.init(owner, TypeDescriptor.rBoolean, "inDeletion").
			initWithPattern("false");
		getClassDescriptor().addAttribute(inDeletion);
		return inDeletion;
	}
	
	protected void initRemoveMethod() {
		inDeletion = buildInDeletion(getClassDescriptor());
		remove = new MethodDescriptor();
		remove = entityDesc.makeRemoveMethod(getClassDescriptor(), "remove");
		remove.addModifier("public");
		if (entityDesc.isSecured()) {
			remove.insertLines("/// insert pre-processing here", "verifyWrite();");
		}
		getClassDescriptor().addMethod(remove);
	}
	
}
