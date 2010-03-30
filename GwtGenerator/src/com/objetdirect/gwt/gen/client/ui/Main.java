/*
 * This file is part of the Gwt-Generator project and was written by Raphaël Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.client.ui;

import java.util.Map.Entry;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.DrawerPanel;
import com.objetdirect.gwt.gen.client.HistoryManager;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.UMLArtifact;
import com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper;
import com.objetdirect.gwt.umlapi.client.helpers.HotKeyManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.Session;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram.Type;

/**
 * Main ui class
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 *
 */
public class Main extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Main> {
	}

	private final static GeneratorServiceAsync generatorService = GWT.create(GeneratorService.class);
	
	@UiField
	MenuItem designMenuItem;
	
	@UiField
	MenuItem cleanUrl;
	
	@UiField
	MenuItem exportToUrl;
	
	@UiField
	MenuItem exportToSVG;
	
	@UiField
	MenuItem exportToPOJO;
	
	@UiField
	SimplePanel contentPanel;
	
	final CodePanel codePanel = new CodePanel();
	
	DrawerPanel drawer;

	public Main() {
		initWidget(uiBinder.createAndBindUi(this));
		
		OptionsManager.initialize();
		HotKeyManager.forceStaticInit();
		drawer = new DrawerPanel();
		drawer.addDefaultNode(Type.CLASS);
		contentPanel.add(drawer);
		addCommands();
	}
	
	
	/**
	 * Attached command to the menu items. 
	 */
	private void addCommands(){
		
		designMenuItem.setCommand(new Command() {
			
			@Override
			public void execute() {
				contentPanel.clear();
				contentPanel.add(drawer);
				HotKeyManager.setInputEnabled(true);
				GWTUMLDrawerHelper.disableBrowserEvents();
			}
		});
		
		cleanUrl.setCommand(new Command() {
			@Override
			public void execute() {
				HistoryManager.upgradeDiagramURL("");
			}
		});
		
		exportToUrl.setCommand(new Command() {
			@Override
			public void execute() {
				HistoryManager.upgradeDiagramURL(Session.getActiveCanvas().toUrl());
			}
		});
		
		exportToSVG.setCommand(new Command() {
			@Override
			public void execute() {
				String svg = "<?xml version='1.0' standalone='no'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>";
				Session.getActiveCanvas().clearArrows();
				svg += DOM.getInnerHTML((Element) Session.getActiveCanvas().getElement().getFirstChildElement());
				Window.open("data:image/xml+svg," + svg, "SVG export", "top");
				Session.getActiveCanvas().makeArrows();
			}
		});
		
		exportToPOJO.setCommand(new Command() {
			
			@Override
			public void execute() {
				generatePojo();
			}
		});
	}
	
	private void generatePojo() {
		codePanel.cleanAllCode();
		
		for (final Entry<Integer, UMLArtifact> uMLArtifactEntry : UMLArtifact.getArtifactList().entrySet()) {
			final UMLArtifact artifact  = uMLArtifactEntry.getValue();
			
			if (artifact instanceof ClassArtifact) {
				ClassArtifact classArtifact  = (ClassArtifact)artifact;
				final UMLClass clazz = classArtifact.toUMLComponent();
				
				Log.debug("Class name = " + clazz.getName());
				
				generatorService.generateClassCode(clazz, "com.od.test", new AsyncCallback<String[]>() {
					@Override
					public void onSuccess(String[] result) {
						codePanel.addClassCode(result, clazz.getName());
						codePanel.goToFirstClass();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Log.error("serice call failed " + caught.getMessage());
					}
				});
			}
		}
		
		if (UMLArtifact.getArtifactList().size() > 0) {
			contentPanel.clear();
			HotKeyManager.setInputEnabled(false);
			GWTUMLDrawerHelper.enableBrowserEvents();
			
			contentPanel.add(codePanel);
		} //TODO else display message which informs the user that he should add at least one classe before generate pojo
	}
}
