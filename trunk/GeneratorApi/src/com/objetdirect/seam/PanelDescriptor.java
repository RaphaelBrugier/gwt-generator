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

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.FragmentDescriptor;

public abstract class PanelDescriptor extends BaseComponent {

	String label;
	
	public String getLabel() {
		return label;
	}
	
	public void buildJavaPart() { 
	}
	
	public abstract String[] getPattern();
	
	public void buildFaceletPart() {
		setFragment(new FragmentDescriptor(getPattern()) {
			public String[] getText() {
				removeLine("/// insert children here");
				return super.getText();
			}
		});
		for (Component child : children) {
			child.buildFaceletPart();
			getFragment().insertFragment("/// insert children here", child.getFragment());
		}
	}
	
	public PanelDescriptor(String label) {
		this.label = label;
	}
	
	public void setOwner(Component owner) {
		super.setOwner(owner);
		((LayoutDescriptor)getOwner()).registerPanel(this);
	}
	
	public List<Component> getChildren() {
		return children;
	}
	
	List<Component> children = new ArrayList<Component>();
	
	public void register(Component child) {
		children.add((Component)child);
	}
}
