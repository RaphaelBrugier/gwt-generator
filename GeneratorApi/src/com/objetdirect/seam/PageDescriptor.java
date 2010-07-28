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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.ConstructorDescriptor;
import com.objetdirect.engine.FragmentDescriptor;

public class PageDescriptor extends DocumentDescriptor {
	
	public PageDescriptor(
		String classPackageName, String className, 
		String viewPackageName, String viewName) 
	{
		super(classPackageName, className, viewPackageName, viewName);
		popupPattern = defaultPopupPattern;
	}

	public void buildJavaPart() 
	{
		super.buildJavaPart();
		if (confirmPopup!=null) {
			confirmPopup.buildJavaPart();
		}
	}

	public void buildFaceletPart() 
	{
		super.buildFaceletPart();
		if (confirmPopup!=null) {
			confirmPopup.buildFaceletPart();
			facelet.insertFragment("/// popups here", confirmPopup.getFragment());
		}
	}
	
	
	protected ConstructorDescriptor buildJavaConstructor(ClassDescriptor javaClass) {
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(javaClass).setContent( 
			"if (Conversation.instance().isLongRunning()) {",
			"	Conversation.instance().end(true);",
			"	Conversation.instance().leave();",
			"}",
			"Conversation.instance().begin();"
		);
		constructor.addModifier("public");
		constructor.replace("Conversation", Seam.Conversation);
		return constructor;
	}
	
	String[] popupPattern;
	
	protected FaceletDescriptor buildFacelet(String packageName, String name) {
		FaceletDescriptor facelet = new FaceletDescriptor(packageName, name, defaultContentFacelet) {
			public String[] getText() {
				writePopups(this);
				removeLines(defaultTitle);
				removeLines(defaultSummary);
				removeLine("/// page content here");
				removeLine("/// popups here");
				return super.getText();
			}
		};
		return facelet;
	}
	
	protected void writePopups(FaceletDescriptor facelet) {
		for (FragmentDescriptor popup : popupFragments) {
			facelet.insertFragment("/// popups here", popup);
		}
		for (PopupDescriptor popup : popups) {
			FragmentDescriptor fragment = new FragmentDescriptor(popupPattern);
			fragment.replace("popupUrl", popup.getUrl());
			facelet.insertFragment("/// popups here", fragment);
		}
	}
	
	Set<PopupDescriptor> popups = new HashSet<PopupDescriptor>();
	List<FragmentDescriptor> popupFragments = new ArrayList<FragmentDescriptor>();
	
	public PageDescriptor addPopup(PopupDescriptor popup) {
		popups.add(popup);
		return this;
	}
	
	public PageDescriptor addPopup(FragmentDescriptor popup) {
		popupFragments.add(popup);
		return this;
	}

	public void initConfirmPopup() {
		if (confirmPopup == null) {
			confirmPopup = new ConfirmPopupDescriptor();
			confirmPopup.setOwner(this);
		}
	}

	public ConfirmPopupDescriptor getConfirmPopup() {
		return confirmPopup;
	}
		
	
	
	ConfirmPopupDescriptor confirmPopup = null;
	
	
	
	static final String[] defaultContentFacelet = {
		"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"", 
		"			\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
		"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
		"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
		"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
		"	xmlns:f=\"http://java.sun.com/jsf/core\"",
		"	xmlns:h=\"http://java.sun.com/jsf/html\"",
		"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\"",
		"	template=\"/layout/template.xhtml\">",
		"",
		"<ui:define name=\"body\">",
		"",
		"	<h:messages styleClass=\"message\"/>",
		"",
		"	<ice:form id=\"pageForm\">",
		"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
		"			<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
		"				<tr>",
		"					<td class=\"iceDatTblColHdr2\">",
		"						<ice:outputText value=\"page title here\"/>",
		"					</td>",
		"				</tr>",
		"			</table>",
		"			<p>page summary here<p>",
		"",
		"			<div class=\"dialog\">",
		"				/// page content here",
		"			</div>",
		"		</ice:panelGroup>",
		"		/// popups here",
		"	</ice:form>",
		"</ui:define>", 
		"</ui:composition>"
	};

	public static final String[] defaultTitle = {
		"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
		"	<tr>",
		"		<td class=\"iceDatTblColHdr2\">",
		"			<ice:outputText value=\"page title here\"/>",
		"		</td>",
		"	</tr>",
		"</table>",
	};

	public static final String[] defaultSummary = {
		"<p>page summary here<p>",
	};

	public static final String[] defaultPopupPattern = {
		"<ui:include src=\"popupUrl\"/>"
	};
}
