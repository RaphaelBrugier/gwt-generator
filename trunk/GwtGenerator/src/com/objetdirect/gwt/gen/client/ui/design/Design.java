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
package com.objetdirect.gwt.gen.client.ui.design;

import java.util.ArrayList;
import java.util.List;

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
import com.objetdirect.gwt.gen.client.ui.Main;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.shared.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassRelationLinkArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.UMLArtifact;
import com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper;
import com.objetdirect.gwt.umlapi.client.helpers.HotKeyManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.Session;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram.Type;

/**
 * Widget containing the UMl Modeler.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class Design extends Composite {

	private static DesignUiBinder uiBinder = GWT.create(DesignUiBinder.class);

	interface DesignUiBinder extends UiBinder<Widget, Design> {
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
	MenuItem backToExplorer;
	
	@UiField
	SimplePanel contentPanel;
	
	final private CodePanel codePanel = new CodePanel();
	
	final private LoadingPopUp loadingPopUp = new LoadingPopUp();
	
	final private DrawerPanel drawer;

	final private Main parent;
	
	public Design(Main parent) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.parent = parent;
		
		OptionsManager.initialize();
		OptionsManager.set("DiagramType", UMLDiagram.Type.CLASS.getIndex());
		HotKeyManager.forceStaticInit();
		drawer = new DrawerPanel();
		drawer.addDefaultNode(Type.CLASS);
		contentPanel.setHeight("100%");
		contentPanel.setWidth("100%");
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
		
		backToExplorer.setCommand(new Command() {
			@Override
			public void execute() {
				parent.goToHomeScreen();
			}
		});
	}
	
	/** Command to generate the pojos from the diagram. 
	 * Trigger the display of the codePanel after generate the code.
	 */
	private void generatePojo() {
		codePanel.cleanAllCode();
		
		List<UMLClass> umlClasses = new ArrayList<UMLClass>();
		List<UMLRelation> umlRelations = new ArrayList<UMLRelation>();
		
		for (final UMLArtifact umlArtifact : UMLArtifact.getArtifactList().values()) {
			if (umlArtifact instanceof ClassArtifact) {
				ClassArtifact classArtifact  = (ClassArtifact)umlArtifact;
				umlClasses.add(classArtifact.toUMLComponent());
			} else if (umlArtifact instanceof ClassRelationLinkArtifact) {
				ClassRelationLinkArtifact relationLinkArtifact = (ClassRelationLinkArtifact)umlArtifact;
				umlRelations.add(relationLinkArtifact.toUMLComponent());
			}
		}
		
		if (umlClasses.size() ==0) {
			//TODO display a message to inform the user that he should add at least one class before he can generate pojo
		} else {
			Log.debug(this.getClass().getName() + "::generatePojo() Starting generation");
			
			contentPanel.clear();
			HotKeyManager.setInputEnabled(false);
			GWTUMLDrawerHelper.enableBrowserEvents();
			
			loadingPopUp.startProcessing("Generating code ...");
			
			generatorService.generateClassesCode(umlClasses, umlRelations, "com.od.test", new AsyncCallback<List<GeneratedCode>>() {
				
				@Override
				public void onSuccess(List<GeneratedCode> result) {
					for (GeneratedCode generatedCode : result) {
						String className = generatedCode.getClassName();
						codePanel.addClassCode(generatedCode.getLinesOfCode(), className);
					}
					
					contentPanel.add(codePanel);
					codePanel.goToFirstClass();
					loadingPopUp.stopProcessing();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					ErrorPopUp errorPopUp = new ErrorPopUp(caught);
					errorPopUp.addClosedCommand(new Command() {
						@Override
						public void execute() {
							contentPanel.clear();
							contentPanel.add(drawer);
							HotKeyManager.setInputEnabled(true);
							GWTUMLDrawerHelper.disableBrowserEvents();
						}
					});
					
					loadingPopUp.stopProcessing();
					errorPopUp.show();
				}
			});
		}
	}
}
