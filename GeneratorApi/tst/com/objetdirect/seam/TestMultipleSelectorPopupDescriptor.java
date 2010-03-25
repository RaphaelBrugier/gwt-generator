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

public class TestMultipleSelectorPopupDescriptor extends TestCase {

	public void testSimpleSelectorPopup() {
		Seam.clear();
		EntityDescriptor agency = 
			new EntityDescriptor("com.objetdirect.domain", "Agency")
				.addStringField("name", null);
		MultipleSelectorPopupDescriptor selector = new MultipleSelectorPopupDescriptor(
			"com.objetdirect.actions","SelectAgencies", "views", "select-agencies",
			agency);
		TransientSelectOneListDescriptor list = 
			new TransientSelectOneListDescriptor(agency).showField("name", "Name", 20);
		selector.setFeature(list);
		selector.build();
		TestUtil.assertText(selector.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Agency;",
			"import java.util.ArrayList;",
			"import java.util.HashMap;",
			"import java.util.HashSet;",
			"import java.util.List;",
			"import java.util.Map;",
			"import java.util.Set;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"selectAgencies\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class SelectAgenciesAnimator {",
			"",
			"	boolean opened = false;",
			"	@In",
			"	EntityManager entityManager;",
			"	List<Agency> agencies = null;",
			"	Map<Agency, Boolean> selected = new HashMap<Agency, Boolean>();",
			"	Set<Agency> wasSelected;",
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
			"	",
			"	public void setSelector(Selector selector, List<Agency> selected) {",
			"		this.selector = selector;",
			"		this.selected.clear();",
			"		this.wasSelected = new HashSet<Agency>(selected);",
			"		for (Agency agency : selected) {",
			"			this.selected.put(agency, true);",
			"		}",
			"	}",
			"	",
			"	public void selectAgencies() {",
			"		List<Agency> addToSelected = new ArrayList<Agency>();",
			"		List<Agency> removeFromSelected = new ArrayList<Agency>();",
			"		for (Map.Entry<Agency, Boolean> entry : this.selected.entrySet()) {",
			"			if (entry.getValue()!=null && entry.getValue()) {",
			"				if (!wasSelected.contains(entry.getKey()))",
			"					addToSelected.add(entry.getKey());",
			"			}",
			"		}",
			"		for (Agency agency : wasSelected) {",
			"			Boolean sel = this.selected.get(agency);",
			"			if (sel==null || !sel)",
			"				removeFromSelected.add(agency);",
			"		}",
			"		selector.select(addToSelected, removeFromSelected);",
			"		close();",
			"	}",
			"",
			"	public interface Selector {",
			"	",
			"		void select(List<Agency> addToSelected, List<Agency> removeFromSelected);",
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
			"	<h:panelGroup rendered=\"#{selectAgencies.opened}\">",
			"		<div class=\"modal\"/>",
			"		<ice:panelPopup draggable=\"true\"",
			"				visible=\"#{selectAgencies.opened}\"",
			"				autoCentre=\"true\"",
			"				styleClass=\"corePopup\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGroup styleClass=\"popupHeaderWrapper\">",
			"					<ice:commandButton type=\"button\"",
			"						image=\"/img/cancel.png\"",
			"						actionListener=\"#{selectAgencies.close}\"",
			"						styleClass=\"popupHeaderImage\"",
			"						title=\"Close Popup\" alt=\"Close\"/>",
			"				</ice:panelGroup>",
			"			</f:facet>",
			"			<f:facet name=\"body\">",
			"				<h:panelGroup>",
			"					<ice:dataTable id=\"agencies\" value=\"#{selectAgencies.agencies}\" var=\"agency\"",
			"						rows=\"10\" resizable=\"true\" columnWidths=\"50px,200px\">",
			"						<ice:column>",
			"							<h:selectBooleanCheckbox value=\"#{selectAgencies.selected[agency]}\" />",
			"						</ice:column>",
			"						<ice:column>",
			"							<f:facet name=\"header\">Name</f:facet>",
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
			"					<h:panelGroup>",
			"						<h:commandButton value=\"Select\" action=\"#{selectAgencies.selectAgencies}\"/>",
			"						<h:commandButton value=\"Cancel\" action=\"#{selectAgencies.close}\"/>",
			"					</h:panelGroup>",
			"				</h:panelGroup>",
			"			</f:facet>",
			"		</ice:panelPopup>",
			"	</h:panelGroup>",
			"</ui:composition>"
		);
	}

}
