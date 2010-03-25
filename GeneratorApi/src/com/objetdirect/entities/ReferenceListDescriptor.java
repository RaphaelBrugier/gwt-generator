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

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.BasicClassDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.Hibernate;

public abstract class ReferenceListDescriptor extends RelationshipDescriptor {
	AttributeDescriptor attribute;
	MethodDescriptor adder;
	MethodDescriptor loader;
	MethodDescriptor remover;
	String elementName;

	public ReferenceListDescriptor(
		EntityDescriptor owner, 
		EntityDescriptor target,  
		String name,
		boolean composition) 
	{
		super(owner, target, name, composition);
		this.elementName = NamingUtil.toSingular(name);
	}
	
	protected void init() {
		this.attribute = buildAttribute();
		MethodDescriptor getter = buildGetter();
		setGetter(getter);
		this.adder = buildAdder();
		this.loader = buildLoader();
		this.remover = buildRemover();
		adjustBusinessConstructor();
		adjustCreator();
	}
	
	public AttributeDescriptor makeAttribute(ClassDescriptor owner, ClassDescriptor target) {
		AttributeDescriptor attribute = new AttributeDescriptor();
		attribute.init(owner,  TypeDescriptor.List(target), getName());
		attribute.setSemantic(new SemanticDescriptor("implements", this));
		return attribute;
	}

	protected final MethodDescriptor buildGetter() {
		MethodDescriptor getter = null; 
		if (owner.isSecured()) {
			getter = super.makeGetter(owner, getAttribute());
			getter.replace("return attribute", "return UserManager.filter(attribute)");
			getter.replace("UserManager", Frmk.UserManager);
		}
		else {
			getter = makeGetter(getOwner(), getAttribute());
		}
		if (getter!=null)
			owner.getClassDescriptor().addMethod(getter);
		return getter;
	}
	
	@Override
	public MethodDescriptor makeGetter(EntityDescriptor owner, AttributeDescriptor attr) {
		MethodDescriptor getter = super.makeGetter(owner, attr);
		getter.replace("return attribute", "return CollectionUtil.unmodifiableList(attribute)");
		getter.replace("CollectionUtil", TypeDescriptor.Collections);
		return getter;
	}
	
	protected ClassDescriptor getTarget(AttributeDescriptor attr) {
		return (ClassDescriptor)attr.getType().getParameters()[0];
	}
	
	protected MethodDescriptor buildLoader() {
		MethodDescriptor loader = makeLoader(getOwner(), getAttribute());
		getOwner().getClassDescriptor().addMethod(loader);
		return loader;
	}

	public MethodDescriptor makeLoader(EntityDescriptor owner, AttributeDescriptor attr) {
		MethodDescriptor loader = new MethodDescriptor();
		loader.init(
			(BasicClassDescriptor)attr.getOwner(), TypeDescriptor.rVoid, 
			new StandardNameMaker("load", NamingUtil.toProperty(attr.getName()), null)); 
		loader.addModifier("public");
		loader.setContent(
			"if (!Hibernate.isInitialized(attribute))",
			"	Hibernate.initialize(attribute);"
		);
		loader.replace("Hibernate", Hibernate.Hibernate);
		loader.lastReplace("attribute", attribute);
		return loader;
	}
	
	protected MethodDescriptor buildAdder() {
		MethodDescriptor adder = makeAdder(getOwner(), false, getAttribute());
		getOwner().getClassDescriptor().addMethod(adder);
		return adder;
	}
	
	public MethodDescriptor makeAdder(EntityDescriptor owner, boolean protect,AttributeDescriptor attr) {
		MethodDescriptor adder = new MethodDescriptor();
		adder.init(
			(BasicClassDescriptor)attr.getOwner(), TypeDescriptor.rBoolean, 
			new StandardNameMaker("add", NamingUtil.toProperty(this.elementName), null)); 
		adder.addModifier("public");
		adder.addParameter(getTarget(attr), elementName);
		adder.setContent(
			"/// insert pre-processing here",
			"if (param==null)",
			"	throw new NPE();",
			"if (!this.attribute.contains(param)) {",
			"	this.attribute.add(param);",
			"	/// insert post-processing here",
			"	return true;",
			"}",
			"else",
			"	return false;");
		if (protect)
			adder.replace("(!this.attribute.contains(param))", "(this.attribute!=null && !this.attribute.contains(param))");
		adder.replace("NPE", TypeDescriptor.NullPointerException);
		adder.lastReplace("param", elementName);
		adder.lastReplace("attribute", attribute);
		if (owner.isSecured()) {
			adder.insertLines("/// insert pre-processing here", "verifyWrite();");
		}
		return adder;
	}

	protected MethodDescriptor buildRemover() {
		MethodDescriptor remover = makeRemover(getOwner(), false, getAttribute());
		getOwner().getClassDescriptor().addMethod(remover);
		return remover;
	}

	public MethodDescriptor makeRemover(EntityDescriptor owner, boolean protect, AttributeDescriptor attr) {
		MethodDescriptor remover = new MethodDescriptor();
		remover.init(
			(BasicClassDescriptor)attr.getOwner(), TypeDescriptor.rBoolean, 
			new StandardNameMaker("remove", NamingUtil.toProperty(this.elementName), null)); 
		remover.addModifier("public");
		remover.addParameter(getTarget(attr), elementName);
		remover.setContent(
			"/// insert pre-processing here",
			"if (param==null)",
			"	throw new NPE();",
			"if (this.attribute.contains(param)) {",
			"	this.attribute.remove(param);",
			"	/// insert post-processing here",
			"	return true;",
			"}",
			"else",
			"	return false;");
		if (protect)
			remover.replace("(this.attribute.contains(param))", "(this.attribute!=null && this.attribute.contains(param))");
		remover.replace("NPE", TypeDescriptor.NullPointerException);
		remover.lastReplace("param", elementName);
		remover.lastReplace("attribute", attribute);
		if (owner.isSecured()) {
			remover.insertLines("/// insert pre-processing here", "verifyWrite();");
		}
		return remover;
	}

	public AttributeDescriptor getAttribute() {
		return attribute;
	}

	protected void adjustBusinessConstructor() {
		ConstructorDescriptor constructor = getOwner().getBusinessConstructor();
		adjustBusinessConstructor(constructor, attribute);
	}
	
	protected void adjustCreator() {
		MethodDescriptor creator = getOwner().getCreator();
		adjustCreator(creator, attribute);
	}

	public void adjustBusinessConstructor(ConstructorDescriptor constructor, AttributeDescriptor attr) {
		constructor.insertLines("/// initializations here", "this.attribute = new ImplList<TargetEntity>();");
		constructor.replace("attribute", attr);
		constructor.replace("ImplList", TypeDescriptor.ArrayList(null));
		constructor.replace("TargetEntity", getTarget(attr));		
	}

	public void adjustCreator(MethodDescriptor creator, AttributeDescriptor attr) {
		creator.insertLines("/// creator initializations here", "entityInstance.attribute = new ImplList<TargetEntity>();");
		creator.replace("attribute", attr);
		creator.replace("ImplList", TypeDescriptor.ArrayList(null));
		creator.replace("TargetEntity", getTarget(attr));		
	}

	public MethodDescriptor getAdder() {
		return adder;
	}
	
	public MethodDescriptor getRemover() {
		return remover;
	}

	public MethodDescriptor getLoader() {
		return loader;
	}
	
	public abstract void modifyAdder(MethodDescriptor adder, boolean protect, RelationshipDescriptor reverse);
	public abstract void modifyRemover(MethodDescriptor remover, boolean protect, RelationshipDescriptor reverse);
}
