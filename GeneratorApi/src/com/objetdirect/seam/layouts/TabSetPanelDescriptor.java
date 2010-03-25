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
import com.objetdirect.seam.Component;
import com.objetdirect.seam.PanelDescriptor;

public class TabSetPanelDescriptor extends PanelDescriptor {

	public TabSetPanelDescriptor(String ... tabs) {
		super(null);
		for (int i =0; i<tabs.length; i+=2) {
			TabPanelDescriptor tab = new TabPanelDescriptor(tabs[i], tabs[i+1]);
			register(tab);
		}
	}

	public void setOwner(Component owner) {
		super.setOwner(owner);
		for (Component child : getChildren()) {
			((TabPanelDescriptor)child).setOwner(owner);
		}
	}
	
	@Override
	public String[] getPattern() {
		return pattern;
	}

	static final String[] pattern = {
		"<ice:panelTabSet>",
		"	/// insert children here",
		"</ice:panelTabSet>"
	};
	
	public static class TabPanelDescriptor extends PanelDescriptor {

		public TabPanelDescriptor(String label, String title) {
			super(label);
			this.title = title;
		}

		String title;
		
		@Override
		public String[] getPattern() {
			String[] tabPattern = Rewrite.replace(pattern, "title", title); 
			return tabPattern;
		}
		
		static final String[] pattern = {
			"<ice:panelTab label=\"title\">",
			"	/// insert children here",
			"</ice:panelTab>"			
		};
	}
	
}

