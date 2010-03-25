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

import com.objetdirect.engine.FragmentDescriptor;

public class NavigationPopupDescriptor  extends BaseComponent {

	String[] popupPattern = defaultPopupPattern;
	
	NavigationPopupHolder getParent() {
		return getParent(NavigationPopupHolder.class);
	}
	
	public void buildFaceletPart() {
		setFragment(new FragmentDescriptor(popupPattern));
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceProperty("confirmNavigationRequested", getParent().getIsConfirmNavigationRequestedMethod());
		getFragment().replaceMethod("cancelAndNavigate", getParent().getCancelAndNavigateMethod());
		getFragment().replaceMethod("saveAndNavigate", getParent().getSaveAndNavigateMethod());
		getFragment().replaceMethod("cancelNavigation", getParent().getCancelNavigationMethod());
		((PageDescriptor)getDocument()).addPopup(getFragment());
	}
	
	static String[] defaultPopupPattern = {
		"<ice:panelPopup modal=\"true\" draggable=\"true\" styleClass=\"popup\" rendered=\"#{beanName.confirmNavigationRequested}\">",
		"	<f:facet name=\"header\">",
		"		<ice:panelGrid styleClass=\"title\" cellpadding=\"0\" cellspacing=\"0\" columns=\"2\">",
		"			<ice:outputText value=\"Confirmation\"/>",
		"		</ice:panelGrid>",
		"	</f:facet> ",
		"	<f:facet name=\"body\">",
		"		<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
  		"			<ice:outputText value=\"Des donnees ont ete modifiees. Voulez vous continuer ?\" />",
  		"			<div class=\"actionButtons\">",
		"				<h:commandButton value=\"continuer\" action=\"#{beanName.cancelAndNavigate}\"/>",
		"				<h:commandButton value=\"sauver\" action=\"#{beanName.saveAndNavigate}\"/>",
		"				<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{beanName.cancelNavigation}\"/>",
		"			</div>",
		"		</ice:panelGrid>",
		"	</f:facet>",	
		"</ice:panelPopup>"
	};

}
