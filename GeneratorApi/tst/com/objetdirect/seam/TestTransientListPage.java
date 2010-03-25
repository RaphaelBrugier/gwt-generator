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

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ManyToOneReferenceDescriptor;
import com.objetdirect.entities.OneToOneReferenceDescriptor;
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

public class TestTransientListPage extends TestCase {

	public void testSimpleList() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10).
			showField("email", "E-mail", 20);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import java.util.List;",
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
			"						rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px,200px\">",        
  			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
			"							<h:outputText value=\"#{agency.name}\" />",
			"						</ice:column>",
  			"						<ice:column>",
			"							<f:facet name=\"header\">Phone</f:facet>",
			"							<h:outputText value=\"#{agency.phone}\" />",
			"						</ice:column>",
  			"						<ice:column>",
			"							<f:facet name=\"header\">E-mail</f:facet>",
			"							<h:outputText value=\"#{agency.email}\" />",
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
			"	</ice:form>",
			"</ui:define>", 
			"</ui:composition>"
		);
	}

	public void testColumnsWithPath() {	
		Seam.clear();
		EntityDescriptor city = new EntityDescriptor("com.objetdirect.domain", "City").
			addStringField("name", null);
		EntityDescriptor address = new EntityDescriptor("com.objetdirect.domain", "Address").
			addStringField("number", null).
			addStringField("street", null).
			addStringField("zipcode", null);
		address.addRelationship(new ManyToOneReferenceDescriptor(address, city, "city", true, false));
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		agency.addRelationship(new OneToOneReferenceDescriptor(agency, address, "address", true, true, false));
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10).
			showField("address.number", "No", 10).
			showField("address.street", "Street", 30).
			showField("address.city.name", "Czity", 20).
			showField("email", "E-mail", 20);
		page.setFeature(feature);
		page.build();
		TestUtil.assertExists(page.getFaceletText(), "rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px,100px,300px,200px,200px\">");
		TestUtil.assertExists(page.getFaceletText(),
			"<h:outputText value=\"#{agency.address.number}\" />");
		TestUtil.assertExists(page.getFaceletText(),
			"<h:outputText value=\"#{agency.address.street}\" />");
		TestUtil.assertExists(page.getFaceletText(),
			"<h:outputText value=\"#{agency.address.street}\" />");
	}

	public void testListWithDelete() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10).
			showField("email", "E-mail", 20).
			addDelete("Voulez-vous supprimer l'agence : #{agencyToDelete.name} ?");
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import java.util.List;",
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
			"	Agency targetAgencyForDeletion = null;", // line added
			"	static final int OP_NONE = 0;",
			"	static final int OP_DELETE = 1;",
			"	int operation = OP_NONE;",
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
			"	public void requestAgencyDeletion(Agency agency) {",
			"		targetAgencyForDeletion = agency;",
			"		operation = OP_DELETE;",
			"	}",
			"	",
			"	public Agency getTargetAgencyForDeletion() {",
			"		return targetAgencyForDeletion;",
			"	}",	
			"	",
			"	void deleteAgency(Agency agency) {",
			"		agencies.remove(agency);",
			"		entityManager.remove(agency);",
			"	}",
			"	",
			"	public int getOperation() {",
			"		return operation;",
			"	}",
			"	",
			"	public void setOperation(int operation) {",
			"		this.operation = operation;",
			"	}",
			"	",
			"	public void cancelOperation() {",
			"		operation = OP_NONE;",
			"	}",
			"	",
			"	public void continueOperation() {",
			"		if (operation == OP_DELETE) {",
			"			if (targetAgencyForDeletion != null) {",
			"				deleteAgency(targetAgencyForDeletion);",
			"				targetAgencyForDeletion = null;",
			"			}",
			"		}",
			"		operation = OP_NONE;",
			"	}",
			"	",
			"	public boolean isConfirmRequested() {",
			"		return operation != OP_NONE;",
			"	}",
			"	",
			"	public void setConfirmRequested(boolean confirmRequested) {",
			"		if (!confirmRequested) {",
			"			cancelOperation();",
			"		}",
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
			"						rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px,200px,100px\">",  // column insertion     
  			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
			"							<h:outputText value=\"#{agency.name}\" />",
			"						</ice:column>",
  			"						<ice:column>",
			"							<f:facet name=\"header\">Phone</f:facet>",
			"							<h:outputText value=\"#{agency.phone}\" />",
			"						</ice:column>",
  			"						<ice:column>",
			"							<f:facet name=\"header\">E-mail</f:facet>",
			"							<h:outputText value=\"#{agency.email}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<h:commandButton value=\"del\" action=\"#{editAgencies.requestAgencyDeletion(agency)}\"/>",
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
					// changes begin here
			"		<ice:panelPopup modal=\"true\" draggable=\"true\" styleClass=\"popup\" rendered=\"#{editAgencies.confirmRequested}\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGrid styleClass=\"title\" cellpadding=\"0\" cellspacing=\"0\" columns=\"2\">",
			"					<ice:outputText value=\"Confirmation\"/>",
			"				</ice:panelGrid>",
			"			</f:facet>",
			"			<f:facet name=\"body\">",
			"				<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
			"					<ice:outputText value=\"Voulez-vous supprimer l'agence : #{editAgencies.targetAgencyForDeletion.name} ?\" rendered=\"#{editAgencies.operation==1}\"/>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"valider\" action=\"#{editAgencies.continueOperation}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{editAgencies.cancelOperation}\"/>",
			"					</div>",
			"				</ice:panelGrid>",
			"			</f:facet>",
			"		</ice:panelPopup>", 
					// changes end here
			"	</ice:form>",
			"</ui:define>", 
			"</ui:composition>"
		);
	}
	
	public void testColumnFields() {	
		Seam.clear();
		EnumDescriptor enumDesc = new EnumDescriptor("com.objetdirect.domain", "DummyEnum").
			addConstant("A", "first letter").
			addConstant("B", "second letter").
			addConstant("Z", "last letter");
		EntityDescriptor linkedDummy = new EntityDescriptor("com.objetdirect.domain", "LinkedDummy").
			addStringField("name", null);
		EntityDescriptor dummy = new EntityDescriptor("com.objetdirect.domain", "Dummy").
			addStringField("stringField", null).
			addIntField("intField", null).
			addBooleanField("boolField", null).
			addEnumField("enumField", enumDesc, null).
			addManyToOne(linkedDummy, "linkField", false, false);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(dummy).
			showField("stringField", "String field", 20).
			showNumberField("intField", "Int field", "####", 20).
			showDateField("intField", "Date field", "dd/MM/yyyy").
			showBooleanField("boolField", "Boolean field", "TRUE", "FALSE").
			showEnumField("enumField", "Enum field", 15).
			showEntityField("linkField", "Link field", "name", 15);
		page.setFeature(feature);
		page.build();
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
			"					<ice:dataTable id=\"dummies\" value=\"#{editAgencies.dummies}\" var=\"dummy\"",
			"						rows=\"10\" resizable=\"true\" columnWidths=\"200px,200px,100px,50px,150px,150px\">",
			"						<ice:column>",
			"							<f:facet name=\"header\">String field</f:facet>",
			"							<h:outputText value=\"#{dummy.stringField}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Int field</f:facet>",
			"							<h:outputText value=\"#{dummy.intField}\">",
			"								<f:convertNumber pattern=\"####\"/>",
			"							</h:outputText>",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Date field</f:facet>",
			"							<h:outputText value=\"#{dummy.intField}\">",
			"								<f:convertDateTime pattern=\"dd/MM/yyyy\"/>",
			"							</h:outputText>",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Boolean field</f:facet>",
			"							<h:outputText value=\"#{dummy.boolField?'TRUE':'FALSE'}\"/>",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Enum field</f:facet>",
			"							<h:outputText value=\"#{dummy.enumField.label}\"/>",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Link field</f:facet>",
			"							<h:outputText value=\"#{dummy.linkField.name}\"/>",
			"						</ice:column>",
			"					</ice:dataTable>",
			"					<ice:dataPaginator for=\"dummies\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
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
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}	

	public void testListWithMultipleSelection() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "EditAgencies", "views", "edit-agencies");
		TransientSelectOneListDescriptor feature = new TransientSelectOneListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10).
			showField("email", "E-mail", 20);
		feature.requestMultipleSelection();
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import java.util.HashMap;",
			"import java.util.List;",
			"import java.util.Map;",
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
			"	Map<Agency, Boolean> selected = new HashMap<Agency, Boolean>();",
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
			"	public Map<Agency, Boolean> getSelected() {",
			"		return selected;",
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
			"						rows=\"10\" resizable=\"true\" columnWidths=\"50px,200px,100px,200px\">",
			"						<ice:column>",
			"							<h:selectBooleanCheckbox value=\"#{editAgencies.selected[agency]}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
			"							<h:outputText value=\"#{agency.name}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Phone</f:facet>",
			"							<h:outputText value=\"#{agency.phone}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">E-mail</f:facet>",
			"							<h:outputText value=\"#{agency.email}\" />",
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
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}
}
