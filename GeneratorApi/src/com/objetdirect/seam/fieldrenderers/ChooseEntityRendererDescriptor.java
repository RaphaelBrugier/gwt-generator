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

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.InternalClassDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.SignatureDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.FieldRendererDescriptor;
import com.objetdirect.seam.PageDescriptor;
import com.objetdirect.seam.Seam;
import com.objetdirect.seam.SelectorPopupDescriptor;

public class ChooseEntityRendererDescriptor extends FieldRendererDescriptor {

	public ChooseEntityRendererDescriptor(
			String fieldName, String fieldPath, SelectorPopupDescriptor selector) {
		super(fieldName, fieldPath, null);
		this.selector = selector;
	}

	SelectorPopupDescriptor selector;
	
	AttributeDescriptor selectEntityAnimator;
	MethodDescriptor openPopup;
	InternalClassDescriptor entitySelector;
	MethodDescriptor select;
	
	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		selectEntityAnimator = getSelectEntityAnimatorAttribute(javaClass);
		entitySelector = getEntitySelectorClass(javaClass);
		select = getSelectMethod(javaClass);
		openPopup = getOpenPopupMethod(javaClass);
	}
	
	protected AttributeDescriptor getSelectEntityAnimatorAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = 
			new AttributeDescriptor();
			attr.init(javaClass, selector.getClassDescriptor(), 
					new StandardNameMaker("select", null, selector.getEntity().getClassDescriptor()));
		attr.addAnnotation(Seam.In,"create=true");
		javaClass.addAttribute(attr);
		return attr;
	}

	protected MethodDescriptor getOpenPopupMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("openPopupFor", null, getAttribute()))
			.addModifier("public");
		meth.setContent(
			"selectEntityAnimator.setEntitySelector(new EntitySelector());",
			"selectEntityAnimator.doOpen();"
		);
		meth.replace("selectEntityAnimator", selectEntityAnimator);
		meth.replace("setEntitySelector", selector.getSelectEntityMethod());
		meth.replace("EntitySelector", entitySelector);
		meth.replace("doOpen", selector.getOpenMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected InternalClassDescriptor getEntitySelectorClass(ClassDescriptor javaClass) {
		InternalClassDescriptor clazz = new InternalClassDescriptor(javaClass, 
			new StandardNameMaker(null, "Selector", getAttribute()));
		clazz.addInterface(selector.getSelectorInterface());
		javaClass.addType(clazz);
		return clazz;
	}

	protected MethodDescriptor getSelectMethod(ClassDescriptor javaClass) {
		SignatureDescriptor signature = (SignatureDescriptor)selector.getSelectorInterface().getMethod("select");
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, signature.getName());
		meth.addModifier("public");
		meth.addParameter(signature.getParameterType(0), signature.getParameterName(0));
		
		meth.setContent(
			"MyAnimatorClass parent = ((MyAnimatorClass) Component",
			"	.getInstance(MyAnimatorClass.class));",
			"parent.selectEntity(param);"
		);
		meth.replace("MyAnimatorClass", javaClass);
		meth.replace("Component", Seam.Component);
		meth.replace("selectEntity", getSetter());
		meth.replace("param", signature.getParameterName(0));
		
		entitySelector.addMethod(meth);
		return meth;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	public FragmentDescriptor buildFragment() {
		FragmentDescriptor fd = new FragmentDescriptor(
			"<h:commandButton value=\"sel\" immediate=\"true\" action=\"#{beanName.openPopup}\"/>"
		);
		fd.replace("beanName", getDocument().getBeanName());
		fd.replaceMethod("openPopup", openPopup);
		PageDescriptor page = getParent(PageDescriptor.class);
		page.addPopup(selector);
		return fd;
	}

	public boolean isEditableField() {
		return true;
	}
	
}
