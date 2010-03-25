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

package com.objetdirect.engine;

public class StandardMethods {

	public static MethodDescriptor getter(
		final AttributeDescriptor attribute, 
		String modifiers,
		String ... initPattern) 
	{
		String prefix = 
			(attribute.getType()==TypeDescriptor.Boolean || attribute.getType()==TypeDescriptor.rBoolean) ? 
			"is" : "get"; 
		MethodDescriptor getter = new MethodDescriptor();
		getter.init((BasicClassDescriptor)attribute.getOwner(), attribute.getType(), 
			new StandardNameMaker(prefix, null, attribute)); 
		if (modifiers!=null)
			getter.addModifier(modifiers);
		getter.setContent(
			"/// insert pre-processing here",
			"if (attribute==null) {",
			"	attribute = initialization;",
			"}",
			"return attribute;"
		);
		getter.replace("initialization", initPattern);
		getter.setSemantic(new SemanticDescriptor("get", attribute));

		getter.lastRemoveLines(
			"if (attribute==null) {",
			"	attribute = initialization;",
			"}"
		);
		getter.lastReplace("attribute", attribute);
		return getter;
	}
	
	public static MethodDescriptor setter(
		final AttributeDescriptor attribute,
		String modifiers,
		String ... content) 
	{
		MethodDescriptor setter = new MethodDescriptor();
		setter.init(
			(BasicClassDescriptor)attribute.getOwner(), TypeDescriptor.rVoid, 
			new StandardNameMaker("set", null, attribute));
		setter.addParameter(attribute.getType(), attribute.getName());
		if (modifiers!=null)
			setter.addModifier(modifiers);
		setter.setContent(
			content
		);
		setter.replace("attribute", attribute);
		setter.setSemantic(new SemanticDescriptor("set", attribute));
		setter.lastReplace("attribute", attribute);
		return setter;
	}
	
	public static MethodDescriptor loader(
		final AttributeDescriptor attribute,
		String modifiers,
		String ... content) 
	{
		MethodDescriptor loader = new MethodDescriptor();
		loader.init(
			(BasicClassDescriptor)attribute.getOwner(), TypeDescriptor.rVoid, 
			new StandardNameMaker("load", null, attribute));
		loader.addParameter(attribute.getType(), attribute.getName());
		if (modifiers!=null)
			loader.addModifier(modifiers);
		loader.setContent(
			content
		);
		loader.replace("attribute", attribute);
		loader.setSemantic(new SemanticDescriptor("load", attribute));
		loader.lastReplace("attribute", attribute);
		return loader;
	}

	public static MethodDescriptor setter(
		final AttributeDescriptor attribute,
		String modifiers) 
	{
		return setter(attribute, modifiers,
			"/// insert pre-processing here",
			"this.attribute = attribute;",
			"/// insert post-processing here"
		);
	}

	public static MethodDescriptor loader(
		final AttributeDescriptor attribute,
		String modifiers) 
	{
		return loader(attribute, modifiers,
			"this.attribute = attribute;"
		);
	}

	public static MethodDescriptor protectedSetter(
		final AttributeDescriptor attribute,
		String modifiers) 
	{
		return setter(attribute, modifiers,
			"if (this.attribute != attribute) {",
			"	/// insert pre-processing here",
			"	this.attribute = attribute;",
			"	/// insert post-processing here",
			"}"
		);
	}

	public static MethodDescriptor getGetter(AttributeDescriptor attr) {
		return attr.getOwner().getMethods().getMethod(new SemanticDescriptor("get", attr));
	}
	
	public static MethodDescriptor getSetter(AttributeDescriptor attr) {
		return attr.getOwner().getMethods().getMethod(new SemanticDescriptor("set", attr));
	}

}
