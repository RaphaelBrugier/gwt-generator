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

import junit.framework.TestCase;

public class TestFragmentDescriptor extends TestCase {
	
	public void testReplacements() {
		ClassDescriptor beanClass = new ClassDescriptor("com.objetdirect.beans", "BeanClass");
		AttributeDescriptor attribute = new AttributeDescriptor();
		attribute.init(beanClass, TypeDescriptor.String, "myParameter");
		MethodDescriptor getter = StandardMethods.getter(attribute, "public");
		AttributeDescriptor flag = new AttributeDescriptor();
		flag.init(beanClass, TypeDescriptor.rBoolean, "opened");
		MethodDescriptor flagGetter = StandardMethods.getter(flag, "public");
		MethodDescriptor action = new MethodDescriptor();
		action.init(beanClass, TypeDescriptor.String, "doIt").
			addModifier("public");
		FragmentDescriptor facelet = new FragmentDescriptor(
			"<body>",
			"	<f:view>",
			"		<h:form rendered=\"#{bean.visible}\">",
			"			<h:input name=\"one\" value=\"#{bean.field}\"/>",
			"			<h:commandLink action=\"#{bean.actionMethod}\"/>",
			"		</h:form>",
			"	</f:view>",
			"</body>"
		);
		facelet.replace("bean", "pageAnimator", "one", "parameter").
			replaceProperty("field", getter).
			replaceProperty("visible", flagGetter).
			replaceMethod("actionMethod", action);
		TestUtil.assertText(facelet.getText(), 
			"<body>",
			"	<f:view>",
			"		<h:form rendered=\"#{pageAnimator.opened}\">",
			"			<h:input name=\"parameter\" value=\"#{pageAnimator.myParameter}\"/>",
			"			<h:commandLink action=\"#{pageAnimator.doIt}\"/>",
			"		</h:form>",
			"	</f:view>",
			"</body>"
		);
	}
	
	public void testReplaceAndInsert() {
		FragmentDescriptor facelet = new FragmentDescriptor(
			"<ui:composition name-spaces",
			"	template=\"/layout/template.xhtml\">"
		);
		facelet.replaceAndInsertLines("name-spaces", 
			"xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\""
		);
		TestUtil.assertText(facelet.getText(), 
			"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\"",
			"	template=\"/layout/template.xhtml\">"
		);
	}
	
	public void testInsertAndRemoveLine() {
		FragmentDescriptor facelet = new FragmentDescriptor(
			"<ui:composition",
			"	// name spaces here",
			"	template=\"/layout/template.xhtml\">"
		);
		facelet.insertLines("// name spaces here", 
			"xmlns=\"http://www.w3.org/1999/xhtml\"",
			"xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"xmlns:ui=\"http://java.sun.com/jsf/facelets\"");
		facelet.insertLines("// name spaces here", 
			"xmlns:f=\"http://java.sun.com/jsf/core\"",
			"xmlns:h=\"http://java.sun.com/jsf/html\"",
			"xmlns:ice=\"http://www.icesoft.com/icefaces/component\""
		);
		facelet.removeLine("// name spaces here");
		TestUtil.assertText(facelet.getText(), 
			"<ui:composition",
			"	xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\"",
			"	template=\"/layout/template.xhtml\">"
		);
	}
	
	public void testRemove() {
		FragmentDescriptor facelet = new FragmentDescriptor(
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"			<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
			"				<tr>",
			"					<td class=\"iceDatTblColHdr2\">",
			"						<ice:outputText value=\"page title here\"/>",
			"					</td>",
			"				</tr>",
			"			</table>",
			"			<p>page summary here</p>",
			"",
			"			<div class=\"dialog\">",
			"				// page content here",
			"			</div>",
			"		</ice:panelGroup>"
		);
		facelet.remove("page title here").
			remove("page summary here");
		TestUtil.assertText(facelet.getText(),
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"			<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
			"				<tr>",
			"					<td class=\"iceDatTblColHdr2\">",
			"						<ice:outputText value=\"\"/>",
			"					</td>",
			"				</tr>",
			"			</table>",
			"			<p></p>",
			"",
			"			<div class=\"dialog\">",
			"				// page content here",
			"			</div>",
			"		</ice:panelGroup>"
		);
	}

	public void testRemoveLines() {
		FragmentDescriptor facelet = new FragmentDescriptor(
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"			<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
			"				<tr>",
			"					<td class=\"iceDatTblColHdr2\">",
			"						<ice:outputText value=\"page title here\"/>",
			"					</td>",
			"				</tr>",
			"			</table>",
			"			<p>page summary here</p>",
			"",
			"			<div class=\"dialog\">",
			"				// page content here",
			"			</div>",
			"		</ice:panelGroup>"
		);
		facelet.removeLines(
			"<td class=\"iceDatTblColHdr2\">",
			"	<ice:outputText value=\"page title here\"/>",
			"</td>"
		).removeLines(
			"<p>page summary here</p>"
		);
		TestUtil.assertText(facelet.getText(),
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"			<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
			"				<tr>",
			"				</tr>",
			"			</table>",
			"",
			"			<div class=\"dialog\">",
			"				// page content here",
			"			</div>",
			"		</ice:panelGroup>"
		);
	}
	
	public void testFragmentManipulation() {
		FragmentDescriptor title = new FragmentDescriptor(
			"<td class=\"iceDatTblColHdr2\">",
			"	<ice:outputText value=\"Edit agencies\"/>",
			"</td>"
		);
		FragmentDescriptor summaryParagraph1 = new FragmentDescriptor(
			"<p>Here is the site plan<p>",
			"<img src=\"img/myPlan.jpg\"/>"
		);
		FragmentDescriptor summaryParagraph2 = new FragmentDescriptor(
			"<p>online documentation is accessible from main menu<p>"
		);
		FragmentDescriptor facelet = new FragmentDescriptor(
			"	<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"		<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
			"			<tr>",
			"				// title here",
			"			</tr>",
			"		</table>",
			"		// summary here",
			"",
			"		<div class=\"dialog\">",
			"			// page content here",
			"		</div>",
			"	</ice:panelGroup>"
		);
		facelet.replaceByFragment("// title here", title);
		facelet.insertFragment("// summary here", summaryParagraph1);
		facelet.insertFragment("// summary here", summaryParagraph2);
		facelet.removeLine("// summary here");
		TestUtil.assertText(facelet.getText(),
			"	<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"		<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
			"			<tr>",
			"				<td class=\"iceDatTblColHdr2\">",
			"					<ice:outputText value=\"Edit agencies\"/>",
			"				</td>",
			"			</tr>",
			"		</table>",
			"		<p>Here is the site plan<p>",
			"		<img src=\"img/myPlan.jpg\"/>",
			"		<p>online documentation is accessible from main menu<p>",
			"",
			"		<div class=\"dialog\">",
			"			// page content here",
			"		</div>",
			"	</ice:panelGroup>"
		);
	}
	
}
