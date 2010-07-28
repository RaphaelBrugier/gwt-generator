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

import com.objetdirect.seam.PanelDescriptor;

public class VerticalPanelDescriptor extends PanelDescriptor {

	public VerticalPanelDescriptor(String label) {
		super(label);
	}

	@Override
	public String[] getPattern() {
		return pattern;
	}

	static final String[] pattern = {
		"<h:panelGrid columns=\"1\">",
		"	/// insert children here",
		"</h:panelGrid>"
	};
}
