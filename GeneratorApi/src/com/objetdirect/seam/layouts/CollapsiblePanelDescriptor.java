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

package com.objetdirect.seam.layouts;

import com.objetdirect.engine.Rewrite;
import com.objetdirect.seam.PanelDescriptor;

public class CollapsiblePanelDescriptor extends PanelDescriptor {

	public CollapsiblePanelDescriptor(String label, String title, boolean expanded) {
		super(label);
		this.title = title;
		this.expanded = expanded;
	}

	String title;
	boolean expanded;
	
	@Override
	public String[] getPattern() {
		String[] panelPattern = Rewrite.replace(pattern, 
			"title here", title,
			"expanded here", ""+expanded);
		return panelPattern;
	}

	static final String[] pattern = {
		"<ice:panelCollapsible expanded=\"expanded here\">",
		"	<f:facet name=\"header\">",
		"		<ice:panelGroup>",
		"			<ice:outputText value=\"title here\"/>",
		"		</ice:panelGroup>",
		"	</f:facet>",
		"	<ice:panelGroup>",
		"		/// insert children here",
		"	</ice:panelGroup>",
		"</ice:panelCollapsible>"
	};
}