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

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.BasicClassDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;

public class EntityDescriptor {

	String packageName;
	String className;
	
	public EntityDescriptor(String packageName, String className, boolean secured) {
		this.packageName = packageName;
		this.className = className;
		this.secured = secured;
		init();
	}

	public EntityDescriptor(String packageName, String className) {
		this(packageName, className, false);
	}

	ClassDescriptor entityClass;
	
	void init() {
		entityClass = new ClassDescriptor(packageName, className);
		entityClass.addSemantic(new SemanticDescriptor("implements", this));
		if (secured)
			entityClass.setSuperClass(Frmk.BaseEntity);
		else
			entityClass.addInterface(TypeDescriptor.Serializable);
		entityClass.addAnnotation(JavaxPersistence.Entity);
		if (!secured)
			initStandardAttributes();
		initTechnicalConstructor();
		initBusinessConstructor();
		initCreator();
	}

	protected void initStandardAttributes() {
		id = new AttributeDescriptor();
		id.init(entityClass, TypeDescriptor.rInt, "id");
		id.addAnnotation(JavaxPersistence.Id);
		id.addAnnotation(JavaxPersistence.GeneratedValue);
		entityClass.addAttribute(id);
		version = new AttributeDescriptor();
		version.init(entityClass, TypeDescriptor.rInt, "version");
		version.addAnnotation(JavaxPersistence.Version);
		entityClass.addAttribute(version);
		getId = StandardMethods.getter(id, "public");
		entityClass.addMethod(getId);
		getVersion = StandardMethods.getter(version, "public");
		entityClass.addMethod(getVersion);
	}
	
	protected void initTechnicalConstructor() {
		technicalConstructor = new ConstructorDescriptor();
		technicalConstructor.init(entityClass).addModifier("public");
		entityClass.addConstructor(technicalConstructor);
	}
	
	protected void initBusinessConstructor() {
		businessConstructor = new ConstructorDescriptor();
		businessConstructor.init(entityClass)
			.addModifier("public");
		businessConstructor.setContent("/// initializations here");
		entityClass.addConstructor(businessConstructor);
	}

	protected void initCreator() {
		creator = new MethodDescriptor();
		creator.init(entityClass, entityClass, new StandardNameMaker("create", null, entityClass)).addModifier("public static");
		creator.setContent(
			"EntityClass entityInstance = new EntityClass();",
			"/// creator initializations here",
			"return entityInstance;");
		creator.replace("EntityClass", entityClass);
		String entityInstance = NamingUtil.toMember(getName());
		creator.lastReplace("entityInstance", entityInstance);
		entityClass.addMethod(creator);
	}

	AttributeDescriptor inDeletion = null;
	
	public AttributeDescriptor getInDeletion() {
		if (inDeletion==null) {
			inDeletion = buildInDeletion(entityClass);
			inDeletion.addAnnotation(JavaxPersistence.Transient);
			entityClass.addAttribute(inDeletion);
			if (preRemove==null)
				initPreRemoveMethod();
		}
		return inDeletion;
	}
	
	public AttributeDescriptor buildInDeletion(ClassDescriptor owner) {
		AttributeDescriptor inDeletion = new AttributeDescriptor();
		inDeletion.init(owner, TypeDescriptor.rBoolean, "inDeletion").
			initWithPattern("false");
		return inDeletion;
	}
	
	protected void initPreRemoveMethod() {
		preRemove = new MethodDescriptor();
		preRemove = makeRemoveMethod(entityClass, "preRemove");
		preRemove.addAnnotation(JavaxPersistence.PreRemove);
		if (isSecured()) {
			preRemove.insertLines("/// insert pre-processing here", "verifyWrite();");
		}
		entityClass.addMethod(preRemove);
	}
	
	public MethodDescriptor makeRemoveMethod(ClassDescriptor owner, String title) {
		MethodDescriptor remove = new MethodDescriptor(); 
		remove.init(owner, TypeDescriptor.rVoid, title);
		remove.setContent(
			"/// insert pre-processing here",
			"if (!deletionFlag) {",
			"	deletionFlag = true;",
			"	/// pre remove instructions here",
			"}"
		).replace("deletionFlag", getInDeletion());
		return remove;
	}

	public String[] getText() {
		adjustBusinessConstructor();
		return entityClass.getText();
	}
	
	protected void adjustBusinessConstructor() {
		if (!businessConstructorHasParameters())
			businessConstructor.addParameter(TypeDescriptor.rBoolean, "dummy");
	}
	
	public boolean businessConstructorHasParameters() {
		for (FieldDescriptor field : fields) {
			if (field.getValue()==null)
				return true;
		}
		return false;
	}
	
	AttributeDescriptor id;
	AttributeDescriptor version;
	MethodDescriptor getId;
	MethodDescriptor getVersion;
	ConstructorDescriptor technicalConstructor;
	ConstructorDescriptor businessConstructor;
	MethodDescriptor creator;
	MethodDescriptor preRemove;

	public ClassDescriptor getClassDescriptor() {
		return entityClass;
	}

	public MethodDescriptor getPreRemoveMethod() {
		if (preRemove==null) 
			initPreRemoveMethod();
		return preRemove;
	}
	
	List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();

	public MemberDescriptor getMember(String name) {
		for (FieldDescriptor field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		for (RelationshipDescriptor relationship : relationships) {
			if (relationship.getName().equals(name))
				return relationship;
		}
		return null;
	}
	
	public List<FieldDescriptor> getFields() {
		return fields;
	}
	
	public EntityDescriptor addField(FieldDescriptor field) {
		fields.add(field);
		if (field.getAttribute()!=null)
			entityClass.addAttribute(field.getAttribute());
		if (field.getGetter()!=null) {
			if (secured)
				field.getGetter().insertLines("/// insert pre-processing here", "verifyRead();");
			entityClass.addMethod(field.getGetter());
		}
		if (field.getSetter()!=null) {
			if (secured)
				field.getSetter().insertLines("/// insert pre-processing here", "verifyWrite();");
			entityClass.addMethod(field.getSetter());
		}
		field.addInitialization(businessConstructor);
		return this;
	}

	public EntityDescriptor addStringField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.String, name, value, params));
		return this;
	}
	
	public EntityDescriptor addStringField(boolean notNull, int minLength, int maxLength, String name, Object value,  Object ... params) {
		FieldDescriptor fd = new FieldDescriptor(this, TypeDescriptor.String, name, value, params);
		fd.setNotNull(notNull);
		if (minLength!=-1 && maxLength!=-1)
			fd.setLength(minLength, maxLength);
		addField(fd);
		return this;
	}

	public EntityDescriptor addIntField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rInt, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperIntField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Int, name, value, params));
		return this;
	}

	public EntityDescriptor addLongField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rLong, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperLongField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Long, name, value, params));
		return this;
	}

	public EntityDescriptor addByteField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rByte, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperByteField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Byte, name, value, params));
		return this;
	}

	public EntityDescriptor addShortField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rShort, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperShortField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Short, name, value, params));
		return this;
	}

	public EntityDescriptor addBooleanField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rBoolean, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperBooleanField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Boolean, name, value, params));
		return this;
	}

	public EntityDescriptor addCharField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rChar, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperCharField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Char, name, value, params));
		return this;
	}

	public EntityDescriptor addFloatField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rFloat, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperFloatField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Float, name, value, params));
		return this;
	}

	public EntityDescriptor addDoubleField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.rDouble, name, value, params));
		return this;
	}

	public EntityDescriptor addWrapperDoubleField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Double, name, value, params));
		return this;
	}

	public EntityDescriptor addDateField(String name, Object value,  Object ... params) {
		addField(new FieldDescriptor(this, TypeDescriptor.Date, name, value, params));
		return this;
	}

	public EntityDescriptor addEnumField(String name, EnumDescriptor enumType, Object value, Object ... params) {
		addField(new FieldDescriptor(this, enumType, name, value, params));
		return this;
	}
	
	public ConstructorDescriptor getBusinessConstructor() {
		return this.businessConstructor;
	}

	public MethodDescriptor getCreator() {
		return this.creator;
	}

	List<RelationshipDescriptor> relationships = new ArrayList<RelationshipDescriptor>();

	public List<RelationshipDescriptor> getRelationships() {
		return relationships;
	}
	
	public void addRelationship(RelationshipDescriptor reference) {
		relationships.add(reference);
	}

	public RelationshipDescriptor getRelationship(String name) {
		for (RelationshipDescriptor rd : relationships) {
			if (rd.getName().equals(name))
				return rd;
		}
		return null;
	}
	
	public String getName() {
		return className;
	}
	
	public MethodDescriptor getGetter(String fieldName) {
		for (FieldDescriptor field : fields) {
			if (field.getName().equals(fieldName))
				return field.getGetter();
		}
		for (RelationshipDescriptor relationship : relationships) {
			if (relationship.getName().equals(fieldName))
				return relationship.getGetter();
		}
		return null;
	}

	public EntityDescriptor addOneToOne(
		EntityDescriptor target,
		String name, boolean mandatory, boolean composition, boolean initialized) 
	{
		RelationshipDescriptor rs = new OneToOneReferenceDescriptor(this, target, name, mandatory, composition, initialized);
		addRelationship(rs);
		return this;
	}
	
	public EntityDescriptor addOneToOne(
		EntityDescriptor target,
		String name, boolean mandatory, boolean composition, boolean initialized, 
		String reverse, boolean reverseMandatory, boolean reverseComposition, boolean reverseInitialized) 
	{
		RelationshipDescriptor rs = new OneToOneReferenceDescriptor(this, target, name, mandatory, composition, initialized);
		RelationshipDescriptor rrs = new OneToOneReferenceDescriptor(target, this, reverse, reverseMandatory, reverseComposition, reverseInitialized);
		rs.setReverse(rrs, true);
		rrs.setReverse(rs, false);
		addRelationship(rs);
		addRelationship(rrs);
		return this;
	}

	public EntityDescriptor addOneToMany(
		EntityDescriptor target,
		String name, boolean composition)
	{
		RelationshipDescriptor rs = new OneToManyReferenceListDescriptor(this, target, name, composition);
		addRelationship(rs);
		return this;
	}

	public EntityDescriptor addOneToMany(
		EntityDescriptor target,
		String name, boolean composition,
		String reverse, boolean reverseMandatory, boolean reverseInitialized) 
	{
		RelationshipDescriptor rs = new OneToManyReferenceListDescriptor(this, target, name, composition);
		RelationshipDescriptor rrs = new ManyToOneReferenceDescriptor(target, this, reverse, reverseMandatory, reverseInitialized);
		rs.setReverse(rrs, true);
		addRelationship(rs);
		addRelationship(rrs);
		return this;
	}

	public EntityDescriptor addManyToOne(
		EntityDescriptor target,
		String name, boolean mandatory, boolean initialized) 
	{
		RelationshipDescriptor rs = new ManyToOneReferenceDescriptor(this, target, name, mandatory, initialized);
		addRelationship(rs);
		return this;
	}

	public EntityDescriptor addManyToOne(
		EntityDescriptor target,
		String name, boolean mandatory, boolean initialized,
		String reverse, boolean reverseComposition) 
	{
		RelationshipDescriptor rs = new ManyToOneReferenceDescriptor(this, target, name, mandatory, initialized);
		RelationshipDescriptor rrs = new OneToManyReferenceListDescriptor(target, this, reverse, reverseComposition);
		rs.setReverse(rrs, true);
		rrs.setReverse(rs, false);
		addRelationship(rs);
		addRelationship(rrs);
		return this;
	}

	public EntityDescriptor addManyToMany(
		EntityDescriptor target,
		String name, boolean composition) 
	{
		RelationshipDescriptor rs = new ManyToManyReferenceListDescriptor(this, target, name, composition);
		addRelationship(rs);
		return this;
	}

	public EntityDescriptor addManyToMany(
		EntityDescriptor target,
		String name, boolean composition, 
		String reverse, boolean reverseComposition) 
	{
		RelationshipDescriptor rs = new ManyToManyReferenceListDescriptor(this, target, name, composition);
		RelationshipDescriptor rrs = new ManyToManyReferenceListDescriptor(target, this, reverse, reverseComposition);
		rs.setReverse(rrs, true);
		rrs.setReverse(rs, false);
		addRelationship(rs);
		addRelationship(rrs);
		return this;
	}
	
	List<ConstraintDescriptor> constraints = new ArrayList<ConstraintDescriptor>();
	
	public EntityDescriptor addConstraint(ConstraintDescriptor constraint) {
		constraints.add(constraint);
		constraint.buildValidationMethod(this);
		return this;
	}
	
	public List<ConstraintDescriptor> getConstraints() {
		return constraints;
	}

	public EntityDescriptor addUniciyConstraint(String message, String ... fieldNames) {
		MemberDescriptor[] fields = getMembers(fieldNames);
		UnicityDescriptor constraint = new UnicityDescriptor(message, fields);
		addConstraint(constraint);
		return this;
	}

	public MemberDescriptor[] getMembers(String... fieldNames) {
		MemberDescriptor[] fields = new MemberDescriptor[fieldNames.length];
		for (int i=0; i<fieldNames.length; i++)
			fields[i] = getMember(fieldNames[i]);
		return fields;
	}

	public static EntityDescriptor getEntityDescriptor(BasicClassDescriptor clazz) {
		SemanticDescriptor semantic = clazz.getSemantic("implements");
		if (semantic==null)
			return null;
		if (semantic.getParam(0) instanceof EntityDescriptor)
			return (EntityDescriptor)semantic.getParam(0);
		return null;
	}
	
	boolean secured = false;

	public boolean isSecured() {
		return secured;
	}
	
	public String getClassName() {
		return className;
	}
}