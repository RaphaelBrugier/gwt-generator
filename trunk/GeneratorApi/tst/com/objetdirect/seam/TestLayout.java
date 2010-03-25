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

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.SelectOneDetailDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

import junit.framework.TestCase;

public class TestLayout extends TestCase {

	public void testTabset() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("email", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employee");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("firstName", "First Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.getLayout().addTabSet(null, "identity", "Identity", "contact", "Contact");
		FormDescriptor form1 = new FormDescriptor().
			editStringField("firstName", "First Name", 30).
			editStringField("lastName", "Last Name", 30);
		detail.getLayout().addForm(form1, "identity");
		FormDescriptor form2 = new FormDescriptor().
			editStringField("email", "EMail", 20).
			editStringField("phone", "Phone", 10);
		detail.getLayout().addForm(form2, "contact");
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editEmployees.currentEmployeeVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<ice:panelTabSet>",
			"			<ice:panelTab label=\"Identity\">",
			"				<s:validateAll>",
			"					<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"						<h:outputText value=\"First Name : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeFirstName}\" size=\"30\"/>",
			"						</s:decorate>",
			"						<h:outputText value=\"Last Name : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeLastName}\" size=\"30\"/>",
			"						</s:decorate>",
			"					</h:panelGrid>",
			"				</s:validateAll>",
			"			</ice:panelTab>",
			"			<ice:panelTab label=\"Contact\">",
			"				<s:validateAll>",
			"					<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"						<h:outputText value=\"EMail : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeEmail}\" size=\"20\"/>",
			"						</s:decorate>",
			"						<h:outputText value=\"Phone : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeePhone}\" size=\"10\"/>",
			"						</s:decorate>",
			"					</h:panelGrid>",
			"				</s:validateAll>",
			"			</ice:panelTab>",
			"		</ice:panelTabSet>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editEmployees.validateCurrentEmployee}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editEmployees.cancelCurrentEmployee}\"/>",
			"		</div>",
			"	</h:panelGroup>",
			"</h:panelGroup>"
		);
	}

	public void testSplitPanel() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("email", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employee");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("firstName", "First Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.getLayout().addSplitPanel(null, "left", "right");
		FormDescriptor form1 = new FormDescriptor().
			editStringField("firstName", "First Name", 30).
			editStringField("lastName", "Last Name", 30);
		detail.getLayout().addForm(form1, "left");
		FormDescriptor form2 = new FormDescriptor().
			editStringField("email", "EMail", 20).
			editStringField("phone", "Phone", 10);
		detail.getLayout().addForm(form2, "right");
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editEmployees.currentEmployeeVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<ice:panelDivider>",
			"			<f:facet name=\"first\">",
			"				<h:panelGroup>",
			"					<s:validateAll>",
			"						<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"							<h:outputText value=\"First Name : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.currentEmployeeFirstName}\" size=\"30\"/>",
			"							</s:decorate>",
			"							<h:outputText value=\"Last Name : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.currentEmployeeLastName}\" size=\"30\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"				</h:panelGroup>",
			"			</f:facet>",
			"			<f:facet name=\"second\">",
			"				<h:panelGroup>",
			"					<s:validateAll>",
			"						<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"							<h:outputText value=\"EMail : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.currentEmployeeEmail}\" size=\"20\"/>",
			"							</s:decorate>",
			"							<h:outputText value=\"Phone : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{editEmployees.currentEmployeePhone}\" size=\"10\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"				</h:panelGroup>",
			"			</f:facet>",
			"		</ice:panelDivider>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editEmployees.validateCurrentEmployee}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editEmployees.cancelCurrentEmployee}\"/>",
			"		</div>",
			"	</h:panelGroup>",
			"</h:panelGroup>"
		);
	}

	public void testVerticalPanel() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("email", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employee");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("firstName", "First Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.getLayout().addVerticalPanel(null, "panel");
		FormDescriptor form1 = new FormDescriptor().
			editStringField("firstName", "First Name", 30).
			editStringField("lastName", "Last Name", 30);
		detail.getLayout().addForm(form1, "panel");
		FormDescriptor form2 = new FormDescriptor().
			editStringField("email", "EMail", 20).
			editStringField("phone", "Phone", 10);
		detail.getLayout().addForm(form2, "panel");
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editEmployees.currentEmployeeVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<h:panelGrid columns=\"1\">",
			"			<s:validateAll>",
			"				<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"					<h:outputText value=\"First Name : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeFirstName}\" size=\"30\"/>",
			"					</s:decorate>",
			"					<h:outputText value=\"Last Name : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeLastName}\" size=\"30\"/>",
			"					</s:decorate>",
			"				</h:panelGrid>",
			"			</s:validateAll>",
			"			<s:validateAll>",
			"				<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"					<h:outputText value=\"EMail : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeEmail}\" size=\"20\"/>",
			"					</s:decorate>",
			"					<h:outputText value=\"Phone : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeePhone}\" size=\"10\"/>",
			"					</s:decorate>",
			"				</h:panelGrid>",
			"			</s:validateAll>",
			"		</h:panelGrid>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editEmployees.validateCurrentEmployee}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editEmployees.cancelCurrentEmployee}\"/>",
			"		</div>",
			"	</h:panelGroup>",
			"</h:panelGroup>"
		);
	}
	
	public void testHorizontalPanel() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("email", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employee");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("firstName", "First Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.getLayout().addHorizontalPanel(null, "panel");
		FormDescriptor form1 = new FormDescriptor().
			editStringField("firstName", "First Name", 30).
			editStringField("lastName", "Last Name", 30);
		detail.getLayout().addForm(form1, "panel");
		FormDescriptor form2 = new FormDescriptor().
			editStringField("email", "EMail", 20).
			editStringField("phone", "Phone", 10);
		detail.getLayout().addForm(form2, "panel");
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editEmployees.currentEmployeeVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<h:panelGrid columns=\"2\">",
			"			<s:validateAll>",
			"				<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"					<h:outputText value=\"First Name : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeFirstName}\" size=\"30\"/>",
			"					</s:decorate>",
			"					<h:outputText value=\"Last Name : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeLastName}\" size=\"30\"/>",
			"					</s:decorate>",
			"				</h:panelGrid>",
			"			</s:validateAll>",
			"			<s:validateAll>",
			"				<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"					<h:outputText value=\"EMail : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeEmail}\" size=\"20\"/>",
			"					</s:decorate>",
			"					<h:outputText value=\"Phone : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeePhone}\" size=\"10\"/>",
			"					</s:decorate>",
			"				</h:panelGrid>",
			"			</s:validateAll>",
			"		</h:panelGrid>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editEmployees.validateCurrentEmployee}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editEmployees.cancelCurrentEmployee}\"/>",
			"		</div>",
			"	</h:panelGroup>",
			"</h:panelGroup>"
		);
	}
	
	public void testCollapsiblePanel() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("email", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employee");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("firstName", "First Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.getLayout().addCollapsiblePanel(null, "panel", "Data", true);
		FormDescriptor form1 = new FormDescriptor().
			editStringField("firstName", "First Name", 30).
			editStringField("lastName", "Last Name", 30);
		detail.getLayout().addForm(form1, "panel");
		FormDescriptor form2 = new FormDescriptor().
			editStringField("email", "EMail", 20).
			editStringField("phone", "Phone", 10);
		detail.getLayout().addForm(form2, "panel");
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editEmployees.currentEmployeeVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<ice:panelCollapsible expanded=\"true\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGroup>",
			"					<ice:outputText value=\"Data\"/>",
			"				</ice:panelGroup>",
			"			</f:facet>",
			"			<ice:panelGroup>",
			"				<s:validateAll>",
			"					<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"						<h:outputText value=\"First Name : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeFirstName}\" size=\"30\"/>",
			"						</s:decorate>",
			"						<h:outputText value=\"Last Name : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeLastName}\" size=\"30\"/>",
			"						</s:decorate>",
			"					</h:panelGrid>",
			"				</s:validateAll>",
			"				<s:validateAll>",
			"					<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"						<h:outputText value=\"EMail : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeEmail}\" size=\"20\"/>",
			"						</s:decorate>",
			"						<h:outputText value=\"Phone : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeePhone}\" size=\"10\"/>",
			"						</s:decorate>",
			"					</h:panelGrid>",
			"				</s:validateAll>",
			"			</ice:panelGroup>",
			"		</ice:panelCollapsible>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editEmployees.validateCurrentEmployee}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editEmployees.cancelCurrentEmployee}\"/>",
			"		</div>",
			"	</h:panelGroup>",
			"</h:panelGroup>"
		);
	}
	
	public void testMixLayouts() {
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null).
			addStringField("email", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditEmployees", "views", "edit-employee");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("firstName", "First Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		FormDescriptor form1 = new FormDescriptor().
			editStringField("firstName", "First Name", 30);
		FormDescriptor form2 = new FormDescriptor().
			editStringField("lastName", "Last Name", 30);
		FormDescriptor form3 = new FormDescriptor().
			editStringField("email", "EMail", 20);
		FormDescriptor form4 = new FormDescriptor().
			editStringField("phone", "Phone", 10);
		detail.getLayout().addForm(form1);
		detail.getLayout().addVerticalPanel(null, "vertical");
		detail.getLayout().addForm(form2, "vertical");
		detail.getLayout().addHorizontalPanel("vertical", "horizontal");
		detail.getLayout().addForm(form3, "horizontal");
		detail.getLayout().addForm(form4, "horizontal");
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(),
			"<h:panelGroup rendered=\"#{editEmployees.currentEmployeeVisible}\">",
			"	<div class=\"mask\"/>",
			"	<h:panelGroup style=\"position:relative\">",
			"		<s:validateAll>",
			"			<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"				<h:outputText value=\"First Name : \" />",
			"				<s:decorate template=\"/layout/edit.xhtml\">",
			"					<h:inputText value=\"#{editEmployees.currentEmployeeFirstName}\" size=\"30\"/>",
			"				</s:decorate>",
			"			</h:panelGrid>",
			"		</s:validateAll>",
			"		<h:panelGrid columns=\"1\">",
			"			<s:validateAll>",
			"				<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"					<h:outputText value=\"Last Name : \" />",
			"					<s:decorate template=\"/layout/edit.xhtml\">",
			"						<h:inputText value=\"#{editEmployees.currentEmployeeLastName}\" size=\"30\"/>",
			"					</s:decorate>",
			"				</h:panelGrid>",
			"			</s:validateAll>",
			"			<h:panelGrid columns=\"2\">",
			"				<s:validateAll>",
			"					<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"						<h:outputText value=\"EMail : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeeEmail}\" size=\"20\"/>",
			"						</s:decorate>",
			"					</h:panelGrid>",
			"				</s:validateAll>",
			"				<s:validateAll>",
			"					<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"						<h:outputText value=\"Phone : \" />",
			"						<s:decorate template=\"/layout/edit.xhtml\">",
			"							<h:inputText value=\"#{editEmployees.currentEmployeePhone}\" size=\"10\"/>",
			"						</s:decorate>",
			"					</h:panelGrid>",
			"				</s:validateAll>",
			"			</h:panelGrid>",
			"		</h:panelGrid>",
			"		<div class=\"actionButtons\">",
			"			<h:commandButton value=\"valider\" action=\"#{editEmployees.validateCurrentEmployee}\"/>",
			"			<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editEmployees.cancelCurrentEmployee}\"/>",
			"		</div>",
			"	</h:panelGroup>",
			"</h:panelGroup>"
		);
	}

}
