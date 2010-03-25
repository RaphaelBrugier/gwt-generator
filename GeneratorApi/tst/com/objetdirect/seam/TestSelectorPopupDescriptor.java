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
import com.objetdirect.seam.lists.TransientSelectOneListDescriptor;

import junit.framework.TestCase;

public class TestSelectorPopupDescriptor extends TestCase {

	public void testEmptySelectorPopup() {
		Seam.clear();
		EntityDescriptor agency = 
			new EntityDescriptor("com.objetdirect.domain", "Agency");
		SelectorPopupDescriptor selector = new SelectorPopupDescriptor(
			"com.objetdirect.actions","SelectAgency", "views", "select-agency",
			agency);
		selector.build();
		TestUtil.println(selector.getJavaText());
		TestUtil.assertText(selector.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"selectAgency\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class SelectAgencyAnimator {",
			"",
			"	boolean opened = false;",
			"	Selector selector = null;",
			"",
			"",
			"	public boolean isOpened() {",
			"		return opened;",
			"	}",
			"	",
			"	public void setOpened(boolean opened) {",
			"	}",
			"	",
			"	public void open() {",
			"		opened = true;",
			"	}",
			"	",
			"	public void close() {",
			"		opened = false;",
			"	}",
			"	",
			"	public void setSelector(Selector selector) {",
			"		this.selector = selector;",
			"	}",
			"	",
			"	public void selectAgency(Agency agency) {",
			"		selector.select(agency);",
			"		close();",
			"	}",
			"",
			"	public interface Selector {",
			"	",
			"		void select(Agency agency);",
			"	}",
			"}"
		);
		TestUtil.assertText(selector.getFaceletText(),
			"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
			"				\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
			"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\">",
			"",
			"	<h:panelGroup rendered=\"#{selectAgency.opened}\">",
			"		<div class=\"modal\"/>",
			"		<ice:panelPopup draggable=\"true\"",
			"				visible=\"#{selectAgency.opened}\"",
			"				autoCentre=\"true\"",
			"				styleClass=\"corePopup\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGroup styleClass=\"popupHeaderWrapper\">",
			"					<ice:commandButton type=\"button\"",
			"						image=\"/img/cancel.png\"",
			"						actionListener=\"#{selectAgency.close}\"",
			"						styleClass=\"popupHeaderImage\"",
			"						title=\"Close Popup\" alt=\"Close\"/>",
			"				</ice:panelGroup>",
			"			</f:facet>",
			"			<f:facet name=\"body\">",
			"			</f:facet>",
			"		</ice:panelPopup>",
			"	</h:panelGroup>",
			"</ui:composition>"
		);
	}
	
	public void testSimpleSelectorPopup() {
		Seam.clear();
		EntityDescriptor agency = 
			new EntityDescriptor("com.objetdirect.domain", "Agency")
			.addStringField("name", null);
		SelectorPopupDescriptor selector = new SelectorPopupDescriptor(
			"com.objetdirect.actions","SelectAgency", "views", "select-agency",
			agency);
		TransientSelectOneListDescriptor list = 
			new TransientSelectOneListDescriptor(agency).showField("name", "Name", 20);
		selector.setFeature(list);
		selector.build();
		TestUtil.assertText(selector.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import java.util.List;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"selectAgency\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class SelectAgencyAnimator {",
			"",
			"	boolean opened = false;",
			"	Selector selector = null;",
			"	@In",
			"	EntityManager entityManager;",
			"	List<Agency> agencies = null;",
			"",
			"",
			"	public boolean isOpened() {",
			"		return opened;",
			"	}",
			"	",
			"	public void setOpened(boolean opened) {",
			"	}",
			"	",
			"	public void open() {",
			"		opened = true;",
			"	}",
			"	",
			"	public void close() {",
			"		opened = false;",
			"	}",
			"	",
			"	public void setSelector(Selector selector) {",
			"		this.selector = selector;",
			"	}",
			"	",
			"	public void selectAgency(Agency agency) {",
			"		selector.select(agency);",
			"		close();",
			"	}",
			"	",
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<Agency> getAgencies() {",
			"		if (agencies==null) {",
			"			agencies = entityManager.createQuery(\"from Agency\").getResultList();",
			"		}",
			"		return agencies;",
			"	}",
			"",
			"	public interface Selector {",
			"	",
			"		void select(Agency agency);",
			"	}",
			"}"			
		);
		TestUtil.assertText(selector.getFaceletText(),
			"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
			"				\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
			"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\">",
			"",
			"	<h:panelGroup rendered=\"#{selectAgency.opened}\">",
			"		<div class=\"modal\"/>",
			"		<ice:panelPopup draggable=\"true\"",
			"				visible=\"#{selectAgency.opened}\"",
			"				autoCentre=\"true\"",
			"				styleClass=\"corePopup\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGroup styleClass=\"popupHeaderWrapper\">",
			"					<ice:commandButton type=\"button\"",
			"						image=\"/img/cancel.png\"",
			"						actionListener=\"#{selectAgency.close}\"",
			"						styleClass=\"popupHeaderImage\"",
			"						title=\"Close Popup\" alt=\"Close\"/>",
			"				</ice:panelGroup>",
			"			</f:facet>",
			"			<f:facet name=\"body\">",
			"				<h:panelGroup>",
			"					<ice:dataTable id=\"agencies\" value=\"#{selectAgency.agencies}\" var=\"agency\"",
			"						rows=\"10\" resizable=\"true\" columnWidths=\"200px\">",
			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
			"							<ice:rowSelector selectionAction=\"#{selectAgency.selectAgency(agency)}\" multiple=\"false\" />",
			"							<h:outputText value=\"#{agency.name}\" />",
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
			"			</f:facet>",
			"		</ice:panelPopup>",
			"	</h:panelGroup>",
			"</ui:composition>"
		);
	}
}
