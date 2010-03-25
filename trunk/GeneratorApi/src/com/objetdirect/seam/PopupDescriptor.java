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

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;

public class PopupDescriptor extends DocumentDescriptor {
	
	public PopupDescriptor(
		String classPackageName, String className, 
		String viewPackageName, String viewName) 
	{
		super(classPackageName, className, viewPackageName, viewName);
	}

	@Override
	protected FaceletDescriptor buildFacelet(String packageName, String name) {
		FaceletDescriptor facelet = new FaceletDescriptor(packageName, name, defaultContentFacelet) {
			public String[] getText() {
				removeLine("<ice:outputText value=\"popup title here\" styleClass=\"popupHeaderText\"/>");
				removeLine("/// page content here");
				removeLine("/// popups here");
				return super.getText();
			}
		};
		facelet.replace("beanName", getBeanName());
		facelet.replaceMethod("doOpen", getOpenMethod());
		facelet.replaceMethod("doClose", getCloseMethod());
		facelet.replaceProperty("openedPopup", getOpenedGetter());
		return facelet;
	}

	AttributeDescriptor opened;
	MethodDescriptor openedGetter;
	MethodDescriptor openedSetter;
	MethodDescriptor openMethod;
	MethodDescriptor closeMethod;
	
	public AttributeDescriptor getOpened() {
		return opened;
	}
	
	public MethodDescriptor getOpenedGetter() {
		return openedGetter;
	}
	
	public MethodDescriptor getOpenedSetter() {
		return openedSetter;
	}
	
	public MethodDescriptor getOpenMethod() {
		return openMethod;
	}
	
	public MethodDescriptor getCloseMethod() {
		return closeMethod;
	}

	protected ClassDescriptor buildJavaClass(String packageName, String name) {
		ClassDescriptor javaClass = super.buildJavaClass(packageName, name);
		buildOpenedAttributeAndMethods(javaClass);
		return javaClass;
	}
	
	protected void buildOpenedAttributeAndMethods(ClassDescriptor javaClass) {
		opened = buildOpenedAttribute(javaClass);
		javaClass.addAttribute(opened);
		openedGetter = buildOpenedGetter(javaClass);
		openedSetter = buildOpenedSetter(javaClass);
		openMethod = buildOpenMethod(javaClass);
		closeMethod = buildCloseMethod(javaClass);
	}
	
	protected AttributeDescriptor buildOpenedAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rBoolean, "opened")
			.initWithPattern("false");
		return attr;
	}
	
	protected MethodDescriptor buildOpenedGetter(ClassDescriptor javaClass) {
		MethodDescriptor openedGetter = StandardMethods.getter(getOpened(), "public");
		javaClass.addMethod(openedGetter);
		return openedGetter;
	}
	
	protected MethodDescriptor buildOpenedSetter(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("setOpened")).addParameter(TypeDescriptor.rBoolean, "opened");
		meth.addModifier("public");
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildOpenMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("open"));
		meth.addModifier("public");
		meth.setContent("flag = true;").replace("flag", getOpened());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCloseMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("close"));
		meth.addModifier("public");
		meth.setContent("flag = false;").replace("flag", getOpened());
		javaClass.addMethod(meth);
		return meth;
	}

	String[] defaultContentFacelet={
		"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"", 
		"				\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
		"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
		"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
		"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
		"	xmlns:f=\"http://java.sun.com/jsf/core\"",
		"	xmlns:h=\"http://java.sun.com/jsf/html\"",
		"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\">",
		"",
		"	<h:panelGroup rendered=\"#{beanName.openedPopup}\">",
		"		<div class=\"modal\"/>",
		"		<ice:panelPopup draggable=\"true\"",
		"				visible=\"#{beanName.openedPopup}\"",
		"				autoCentre=\"true\"",
		"				styleClass=\"corePopup\">",
		"			<f:facet name=\"header\">",
		"				<ice:panelGroup styleClass=\"popupHeaderWrapper\">",
		"					<ice:outputText value=\"popup title here\" styleClass=\"popupHeaderText\"/>",
		"					<ice:commandButton type=\"button\"",
		"						image=\"/img/cancel.png\"",
		"						actionListener=\"#{beanName.doClose}\"",
		"						styleClass=\"popupHeaderImage\"",
		"						title=\"Close Popup\" alt=\"Close\"/>",
		"				</ice:panelGroup>",
		"			</f:facet>",
		"			<f:facet name=\"body\">",
		"				/// page content here",
		"			</f:facet>",
		"		</ice:panelPopup>",
		"	</h:panelGroup>",
		"	/// popups here",
		"</ui:composition>"		 
	};

}
