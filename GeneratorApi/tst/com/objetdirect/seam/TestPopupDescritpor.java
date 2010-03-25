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

import junit.framework.TestCase;

public class TestPopupDescritpor extends TestCase {

	public void testSimplePage() {
		Seam.clear();
		PopupDescriptor popup = new PopupDescriptor("com.objetdirect.actions","SelectAgency", "views", "select-agency");
		popup.build();
		TestUtil.assertText(popup.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"selectAgency\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class SelectAgencyAnimator {",
			"",
			"	boolean opened = false;",
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
			"",
			"}"
		);
		TestUtil.assertText(popup.getFaceletText(),
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
	
}
