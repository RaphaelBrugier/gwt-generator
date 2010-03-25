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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.seam.layouts.CollapsiblePanelDescriptor;
import com.objetdirect.seam.layouts.HorizontalPanelDescriptor;
import com.objetdirect.seam.layouts.SplitPanelDescriptor;
import com.objetdirect.seam.layouts.TabSetPanelDescriptor;
import com.objetdirect.seam.layouts.VerticalPanelDescriptor;
import com.objetdirect.seam.lists.PersistentList;

public class LayoutDescriptor extends BaseComponent {
	
	List<FormDescriptor> forms = new ArrayList<FormDescriptor>();
	List<PersistentList> lists = new ArrayList<PersistentList>();

	public void buildJavaPart() {
		for (FormDescriptor form : forms) {
			if (submissionProtection)
				form.protectSubmission();
			form.buildJavaPart();
		}
		for (PersistentList pld : lists) {
			pld.buildJavaPart();
		}
	}
	
	public void buildFaceletPart() {
		buildFaceletFragment();
		for (Component child : elements) {
			child.buildFaceletPart();
			getFragment().insertFragment("/// insert children here", child.getFragment());
		}
	}
	
	public boolean isComplex() {
		return !lists.isEmpty();
	}
	
	public LayoutDescriptor addForm(FormDescriptor form) {
		return this.addForm(form, null);
	}

	public LayoutDescriptor addForm(FormDescriptor form, String parentLabel) {
		forms.add(form);
		form.setOwner(this);
		if (parentLabel!=null)
			panels.get(parentLabel).register(form);
		else
			elements.add(form);
		return this;
	}
	
	public LayoutDescriptor addPersistentList(PersistentList persistentList) {
		return this.addPersistentList(persistentList, null);
	}
	
	public LayoutDescriptor addPersistentList(PersistentList persistentList, String parentLabel) {
		lists.add(persistentList);
		persistentList.setOwner(this);
		if (parentLabel!=null)
			panels.get(parentLabel).register(persistentList);
		else
			elements.add(persistentList);
		return this;
	}
	
	public List<FieldRendererDescriptor> getAllFields() {
		List<FieldRendererDescriptor> fields = new ArrayList<FieldRendererDescriptor>();
		for (FormDescriptor form : forms) {
			fields.addAll(form.getAllFields());
		}
		return fields;
	}
	
	protected void buildFaceletFragment() {
		fragment = new FragmentDescriptor(defaultDetailPattern) {
			public String[] getText() {
				removeLine("/// insert children here");
				return super.getText();
			}
		};
	}
	
	public FormDescriptor getForm(int index) {
		return forms.get(index);
	}
	
	public PersistentList getPersistentList(int index) {
		return lists.get(index);
	}

	static final String[] defaultDetailPattern = {
		"/// insert children here",
	};
	
	List<Component> elements = new ArrayList<Component>();
	Map<String, PanelDescriptor> panels = new HashMap<String, PanelDescriptor>();
	
	public void registerPanel(PanelDescriptor panel) {
		if (panel.getLabel()!=null)
			panels.put(panel.getLabel(), panel);
	}
	
	public LayoutDescriptor addPanel(PanelDescriptor panel, String parent) {
		if (parent!=null) {
			PanelDescriptor parentPanel = panels.get(parent);
			parentPanel.register(panel);
		}
		else {
			elements.add(panel);
		}
		panel.setOwner(this);
		return this;
	}

	public LayoutDescriptor addTabSet(String parentLabel, String ... tabs) {
		addPanel(new TabSetPanelDescriptor(tabs), parentLabel);
		return this;
	}

	public LayoutDescriptor addSplitPanel(String parentLabel, String first, String second) {
		addPanel(new SplitPanelDescriptor(first, second), parentLabel);
		return this;
	}

	public LayoutDescriptor addVerticalPanel(String parentLabel, String label) {
		addPanel(new VerticalPanelDescriptor(label), parentLabel);
		return this;
	}

	public LayoutDescriptor addHorizontalPanel(String parentLabel, String label) {
		addPanel(new HorizontalPanelDescriptor(label), parentLabel);
		return this;
	}

	public LayoutDescriptor addCollapsiblePanel(String parentLabel, String label, String title, boolean expanded) {
		addPanel(new CollapsiblePanelDescriptor(label, title, expanded), parentLabel);
		return this;
	}

	boolean submissionProtection = false;
	
	public LayoutDescriptor setSubmissionProtection() {
		submissionProtection = true;
		return this;
	}
}
