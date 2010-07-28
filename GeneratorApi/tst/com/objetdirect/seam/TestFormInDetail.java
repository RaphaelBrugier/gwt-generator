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

import junit.framework.TestCase;

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.SelectOneDetailDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

public class TestFormInDetail extends TestCase {
	
	public void testSimpleInLineForm() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10);
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.addForm(new FormDescriptor().
			editStringField("name", "Nom de l'agence", 30).
			editStringField("phone", "Telephone", 10)
		);
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.println(page.getJavaText());
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import com.objetdirect.jsffrmk.ExpressionUtil;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import java.util.List;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"editAgencies\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class EditAgenciesAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	List<Agency> agencies = null;",
			"	Agency currentAgency = null;",
			// changes begin here
			"	String currentAgencyName;",
			"	String currentAgencyPhone;",
			// changes end here
			"",
			"	public EditAgenciesAnimator() {",	
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<Agency> getAgencies() {",
			"		if (agencies==null) {",
			"			agencies = entityManager.createQuery(\"from Agency\").getResultList();",
			"		}",
			"		return agencies;",
			"	}",
			"	",	
			"	public Agency getCurrentAgency() {",
			"		return currentAgency;",
			"	}",
			"	",
			"	void setCurrentAgency(Agency currentAgency) {",
			"		if (this.currentAgency != currentAgency) {",
			"			this.currentAgency = currentAgency;",
			"			currentAgencyName = currentAgency.getName();",
			"			currentAgencyPhone = currentAgency.getPhone();",
			"		}",
			"	}",
			"	",
			"	public void selectAgency(Agency agency) {",
			"		if (currentAgency!=null)",
			"			return;",
			"		setCurrentAgency(agency);",
			"	}",
			"	",
			"	public boolean isCurrentAgencyVisible() {",
			"		return currentAgency != null;",
			"	}",
			"	",
			"	public void setCurrentAgencyVisible(boolean visible) {",
			"	}",
			"	",
			"	void clearCurrentAgency() {",
			"		currentAgency = null;",
			"	}", 
			"	",
			"	public boolean isSelectedAgency() {",
			"		Agency agency =(Agency)ExpressionUtil.getValue(\"#{agency}\");",
			"		return agency==currentAgency;",
			"	}",
			"	",
			"	public void setSelectedAgency(Agency agency) {",
			"	}",
			"	",
			"	boolean isCurrentAgencyValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	public void cancelCurrentAgency() {",
			"		GuiUtil.cancelGui();",
			"		clearCurrentAgency();",
			"	}",
			"	",
			"	public void validateCurrentAgency() {",
			"		if (isCurrentAgencyValid()) {",
			"			currentAgency.setName(currentAgencyName);",
			"			currentAgency.setPhone(currentAgencyPhone);",
			"			clearCurrentAgency();",
			"		}",
			"	}",
			"	",
			"	public String getCurrentAgencyName() {",
			"		return currentAgencyName;",
			"	}",
			"	",
			"	public void setCurrentAgencyName(String currentAgencyName) {",
			"		this.currentAgencyName = currentAgencyName;",
			"	}",
			"	",
			"	public String getCurrentAgencyPhone() {",
			"		return currentAgencyPhone;",
			"	}",
			"	",
			"	public void setCurrentAgencyPhone(String currentAgencyPhone) {",
			"		this.currentAgencyPhone = currentAgencyPhone;",
			"	}",
			"",
			"}"
		);
		TestUtil.assertText(page.getFaceletText(),
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
			"",
			"			<div class=\"dialog\">",
			"				<h:panelGroup>",
			"					<ice:dataTable id=\"agencies\" value=\"#{editAgencies.agencies}\" var=\"agency\"", 
			"						rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px\">",        
  			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
			"							<ice:rowSelector selectionAction=\"#{editAgencies.selectAgency(agency)}\" value=\"#{editAgencies.selectedAgency}\" multiple=\"false\" />",
			"							<h:outputText value=\"#{agency.name}\" />",
			"						</ice:column>",
  			"						<ice:column>",
			"							<f:facet name=\"header\">Phone</f:facet>",
			"							<h:outputText value=\"#{agency.phone}\" />",
			"						</ice:column>",
			"					</ice:dataTable>",
			"					<ice:dataPaginator for=\"agencies\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"						<f:facet name=\"first\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"						</f:facet>",
			"						<f:facet name=\"last\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"previous\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"next\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"fastforward\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"fastrewind\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"					</ice:dataPaginator>",
        	"				</h:panelGroup>",
			"				<h:panelGroup rendered=\"#{editAgencies.currentAgencyVisible}\">",
			"					<div class=\"mask\"/>",
			"					<h:panelGroup style=\"position:relative\">",
			"						<s:validateAll>",
			"							<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"								<h:outputText value=\"Nom de l'agence : \" />",
			"								<s:decorate template=\"/layout/edit.xhtml\">",
			"									<h:inputText value=\"#{editAgencies.currentAgencyName}\" size=\"30\"/>",
			"								</s:decorate>",
			"								<h:outputText value=\"Telephone : \" />",
			"								<s:decorate template=\"/layout/edit.xhtml\">",
			"									<h:inputText value=\"#{editAgencies.currentAgencyPhone}\" size=\"10\"/>",
			"								</s:decorate>",
        	"							</h:panelGrid>",
			"						</s:validateAll>",
			"						<div class=\"actionButtons\">",
			"							<h:commandButton value=\"valider\" action=\"#{editAgencies.validateCurrentAgency}\"/>",
			"							<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editAgencies.cancelCurrentAgency}\"/>",
			"						</div>", 
			"					</h:panelGroup>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>", 
			"</ui:composition>"
		);
	}

	public void testSimplePopupForm() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10);
		feature.setDetailInPopup("Edition de l'agence #{entity.name}");
		SelectOneDetailDescriptor detail = new SelectOneDetailDescriptor();
		detail.addForm(new FormDescriptor().
			editStringField("name", "Nom de l'agence", 30).
			editStringField("phone", "Telephone", 10)
		);
		feature.setDetail(detail);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(
			page.getJavaText(),
			"boolean currentAgencyFormCentered = true;"
		);
		TestUtil.assertExists(
			page.getJavaText(),
			"public boolean isCurrentAgencyFormCentered() {",
			"	boolean result = currentAgencyFormCentered;",
			"	currentAgencyFormCentered = false;",
			"	return result;",
			"}"
		);
		TestUtil.assertText(page.getFaceletText(),
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
			"",
			"			<div class=\"dialog\">",
			"				<h:panelGroup>",
			"					<ice:dataTable id=\"agencies\" value=\"#{editAgencies.agencies}\" var=\"agency\"",
			"						rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px\">",
			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
			"							<ice:rowSelector selectionAction=\"#{editAgencies.selectAgency(agency)}\" value=\"#{editAgencies.selectedAgency}\" multiple=\"false\" />",
			"							<h:outputText value=\"#{agency.name}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Phone</f:facet>",
			"							<h:outputText value=\"#{agency.phone}\" />",
			"						</ice:column>",
			"					</ice:dataTable>",
			"					<ice:dataPaginator for=\"agencies\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"						<f:facet name=\"first\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"						</f:facet>",
			"						<f:facet name=\"last\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"previous\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"next\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"fastforward\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"						<f:facet name=\"fastrewind\">",
			"							<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"						</f:facet>",
			"					</ice:dataPaginator>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"		<h:panelGroup rendered=\"#{editAgencies.currentAgencyVisible}\">",
			"			<div class=\"modal\"/>",
			"			<ice:panelPopup draggable=\"true\"",
			"				autoCentre=\"#{editAgencies.currentAgencyFormCentered}\"",
			"				styleClass=\"corePopup\">",
			"				<f:facet name=\"header\">",
			"					<ice:panelGroup styleClass=\"popupHeaderWrapper\">",
			"						<ice:outputText value=\"Edition de l'agence #{currentAgency.name}\" styleClass=\"popupHeaderText\"/>",
			"						<ice:commandButton type=\"button\" image=\"/img/cancel.png\"",
			"							actionListener=\"#{editAgencies.cancelCurrentAgency}\"",
			"							styleClass=\"popupHeaderImage\" title=\"Close Popup\" alt=\"Close\"/>",
			"					</ice:panelGroup>",
			"				</f:facet>",
			"				<f:facet name=\"body\">",
			"					<ice:panelGroup styleClass=\"popupBody\">",
			"						<s:validateAll>",
			"							<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"								<h:outputText value=\"Nom de l'agence : \" />",
			"								<s:decorate template=\"/layout/edit.xhtml\">",
			"									<h:inputText value=\"#{editAgencies.currentAgencyName}\" size=\"30\"/>",
			"								</s:decorate>",
			"								<h:outputText value=\"Telephone : \" />",
			"								<s:decorate template=\"/layout/edit.xhtml\">",
			"									<h:inputText value=\"#{editAgencies.currentAgencyPhone}\" size=\"10\"/>",
			"								</s:decorate>",
			"							</h:panelGrid>",
			"						</s:validateAll>",
			"						<div class=\"actionButtons\">",
			"							<h:commandButton value=\"valider\" action=\"#{editAgencies.validateCurrentAgency}\"/>",
			"							<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editAgencies.cancelCurrentAgency}\"/>",
			"						</div>",
			"					</ice:panelGroup>",
			"				</f:facet>",
			"			</ice:panelPopup>",
			"		</h:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}
}
