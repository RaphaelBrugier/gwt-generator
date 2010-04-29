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

import java.util.LinkedList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.DrawerPanel;
import com.objetdirect.gwt.gen.client.event.BackToHomeEvent;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent;
import com.objetdirect.gwt.gen.client.event.DesignAsAGuestEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent.CreateDiagramEventHandler;
import com.objetdirect.gwt.gen.client.event.DesignAsAGuestEvent.DesignAsAGuestEventHandler;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent.EditDiagramEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.MessagePopUp;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassRelationLinkArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.UMLArtifact;
import com.objetdirect.gwt.umlapi.client.engine.GeometryManager;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper;
import com.objetdirect.gwt.umlapi.client.helpers.HotKeyManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.Session;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager.Theme;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram.Type;

/**
 * Widget containing the UML Modeler.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class Design extends Composite {

	private static DesignUiBinder uiBinder = GWT.create(DesignUiBinder.class);

	interface DesignUiBinder extends UiBinder<Widget, Design> {
	}

	private final static GeneratorServiceAsync generatorService = GWT.create(GeneratorService.class);
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	
	/*********************** Ui Items **********************************/
	
	@UiField
	MenuItem designMenuItem;
	
//	@UiField
//	MenuItem cleanUrl;
//	
//	@UiField
//	MenuItem exportToUrl;
	
	@UiField
	MenuItem exportToSVG;
	
	@UiField
	MenuItem exportToPOJO;
	
	@UiField 
	MenuItem saveAndBack;
	
	@UiField 
	MenuItem save;
	
	@UiField
	SimplePanel contentPanel;
	
	@UiField
	Label diagramName;
	
	final private CodePanel codePanel = new CodePanel();
	
	final private LoadingPopUp loadingPopUp = new LoadingPopUp();
	
	private DrawerPanel drawer;
	
	/*********************** Control objects **********************************/
	final private HandlerManager eventBus;
	
	private DiagramInformations currentDiagram;
	
	public Design(HandlerManager eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		
		
		/////////// Gfx platform initialization //TODO move this in the drawer panel ?
		OptionsManager.initialize();
		HotKeyManager.forceStaticInit();
		HotKeyManager.setInputEnabled(false);
		ThemeManager.setCurrentTheme((Theme.getThemeFromIndex(OptionsManager.get("Theme"))));
		GfxManager.setPlatform(OptionsManager.get("GraphicEngine"));
		GeometryManager.setPlatform(OptionsManager.get("GeometryStyle"));
		///
		
		contentPanel.setHeight("100%");
		contentPanel.setWidth("100%");
		bindHandlersToEventBus();
		bindCommands();
	}
	
	
	/** Attached handlers to the eventbus. */
	private void bindHandlersToEventBus() {
		eventBus.addHandler(CreateDiagramEvent.TYPE, new CreateDiagramEventHandler() {
			
			@Override
			public void onCreateDiagramEvent(CreateDiagramEvent event) {
				doCreateNewDiagram(event.getDiagramInformations());
			}
		});
		
		eventBus.addHandler(EditDiagramEvent.TYPE, new EditDiagramEventHandler() {
			@Override
			public void onEditDiagramEvent(EditDiagramEvent event) {
				doLoadDiagram(event.getDiagramInformations());
			}
		});
		
		eventBus.addHandler(DesignAsAGuestEvent.TYPE, new DesignAsAGuestEventHandler() {
			@Override
			public void onDesignAsAGuestEvent(DesignAsAGuestEvent event) {
				doLoadDiagramForAGuest();
			}
		});
	}

	/**
	 * Attached command to the menu items. 
	 */
	private void bindCommands(){
		
		designMenuItem.setCommand(new Command() {
			@Override
			public void execute() {
				contentPanel.clear();
				contentPanel.add(drawer);
				HotKeyManager.setInputEnabled(true);
				GWTUMLDrawerHelper.disableBrowserEvents();
			}
		});
		
//		cleanUrl.setCommand(new Command() {
//			@Override
//			public void execute() {
//				HistoryManager.upgradeDiagramURL("");
//			}
//		});
//		
//		exportToUrl.setCommand(new Command() {
//			@Override
//			public void execute() {
//				HistoryManager.upgradeDiagramURL(Session.getActiveCanvas().toUrl());
//			}
//		});
		
		exportToSVG.setCommand(new Command() {
			@Override
			public void execute() {
				String svg = "<?xml version='1.0' standalone='no'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>";
				Session.getActiveCanvas().clearArrows();
				svg += DOM.getInnerHTML((Element) Session.getActiveCanvas().getContainer().getElement().getFirstChildElement());
				Window.open("data:image/xml+svg," + svg, "SVG export", "top");
				Session.getActiveCanvas().makeArrows();
			}
		});
		
		exportToPOJO.setCommand(new Command() {
			@Override
			public void execute() {
				doGeneratePojo();
			}
		});
		
		save.setCommand(new Command() {
			@Override
			public void execute() {
				HotKeyManager.setInputEnabled(false);
				doSaveDiagram(false);
			}
		});
		
		saveAndBack.setCommand(new Command() {
			@Override
			public void execute() {
				HotKeyManager.setInputEnabled(false);
				doSaveDiagram(true);
			}
		});
		
	}
	
	
	/**
	 * Create a new diagram in the base and setup the canvas with it.
	 * @param diagramInformations
	 */
	private void doCreateNewDiagram(final DiagramInformations diagramInformations) {
		final LoadingPopUp loadingPopUp = new LoadingPopUp();
		loadingPopUp.startProcessing("Creating a new diagram and loading the designer, please wait...");
		
		diagramService.createDiagram(diagramInformations.getType(), diagramInformations.getName(), new 
			AsyncCallback<Long>() {
				@Override
				public void onSuccess(Long key) {
					currentDiagram = new DiagramInformations(key, diagramInformations.getName(), diagramInformations.getType());
					
					OptionsManager.set("DiagramType", diagramInformations.getType().ordinal());
					drawer = new DrawerPanel();
					contentPanel.clear();
					contentPanel.add(drawer);
					diagramName.setText(diagramInformations.getName());
//					drawer.addDefaultNode();
					
					HotKeyManager.setInputEnabled(true);
					// DesignPanel can not be attached in a container and should be inserted directly into the root of the document
					RootLayoutPanel.get().clear();
					RootLayoutPanel.get().add(Design.this);
					loadingPopUp.stopProcessing();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Log.debug(caught.getMessage());
					eventBus.fireEvent(new BackToHomeEvent());
				}
			});
	};
	
	
	/**
	 * Load a diagram from the base and setup it on the canvas.
	 * @param diagramInformations the diagram to load.
	 */
	private void doLoadDiagram(DiagramInformations diagramInformations) {
		final LoadingPopUp loadingPopUp = new LoadingPopUp();
		loadingPopUp.startProcessing("Loading the diagram "+ diagramInformations.getName() +" and the designer, please wait...");
		
		diagramService.getDiagram(diagramInformations.getKey(), new AsyncCallback<DiagramInformations>() {
			
			@Override
			public void onSuccess(DiagramInformations diagramFound) {
				currentDiagram = diagramFound;
				OptionsManager.set("DiagramType", diagramFound.getType().ordinal());
				int canvasWidth = Window.getClientWidth() - 0;
				int canvasHeight = Window.getClientHeight() - 30;
				UMLCanvas umlCanvas = diagramFound.getCanvas();
				umlCanvas.setUpAfterDeserialization(canvasWidth, canvasHeight);
				umlCanvas.clearArrows();
				
				drawer = new DrawerPanel(umlCanvas);
				contentPanel.clear();
				contentPanel.add(drawer);
				diagramName.setText(diagramFound.getName());
//				drawer.getUMLCanvas().fromURL(diagramFound.getGeneratedUrl(), false);
				
				HotKeyManager.setInputEnabled(true);

				// DesignPanel can not be attached in a container and should be inserted directly into the root of the document
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(Design.this);
				loadingPopUp.stopProcessing();
				umlCanvas.clearArrows();
				umlCanvas.makeArrows(); // Replace the arrows on the right position.
			}
			
			@Override
			public void onFailure(Throwable caught) {
				loadingPopUp.stopProcessing();
				Log.debug(caught.getMessage());
				eventBus.fireEvent(new BackToHomeEvent());
			}
		});
	}
	
	/**
	 * Save the current diagram and its canvas in the base.
	 * @param backToHome True if the application should close the designer and back to the home screen.
	 */
	private void doSaveDiagram(final boolean backToHome) {
		final LoadingPopUp loadingPopUp = new LoadingPopUp();
		loadingPopUp.startProcessing("Saving the diagram, please wait...");
		currentDiagram.setGeneratedUrl(drawer.getUMLCanvas().toUrl());
		currentDiagram.setCanvas(drawer.getUMLCanvas());
		diagramService.saveDiagram(currentDiagram, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				loadingPopUp.stopProcessing();
				if (backToHome) {
					eventBus.fireEvent(new BackToHomeEvent());
				} else {
					MessagePopUp popUp = new MessagePopUp("Saved with success");
					popUp.show();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				loadingPopUp.stopProcessing();
				ErrorPopUp errorPopUp = new ErrorPopUp(caught);
				errorPopUp.show();
			}
		});
	}
	
	/** Load an empty class diagram for a guest without the possibility to save it. */
	private void doLoadDiagramForAGuest() {
		final LoadingPopUp loadingPopUp = new LoadingPopUp();
		loadingPopUp.startProcessing("Loading a class diagram in the designer, please login for a full features access, please wait...");
		
		OptionsManager.set("DiagramType", Type.CLASS.getIndex()); // Class diagram by default
		drawer = new DrawerPanel();
		drawer.addWelcomeClass();
		contentPanel.clear();
		contentPanel.add(drawer);
		
		saveAndBack.setVisible(false);
		
		HotKeyManager.setInputEnabled(true);
		// DesignPanel can not be attached in a container and should be inserted directly into the root of the document
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(Design.this);
		loadingPopUp.stopProcessing();
	}
	
	/** Command to generate the pojos from the diagram. 
	 *  Trigger the display of the codePanel after generate the code.
	 */
	private void doGeneratePojo() {
		codePanel.cleanAllCode();
		
		List<UMLClass> umlClasses = new LinkedList<UMLClass>();
		List<UMLRelation> umlRelations = new LinkedList<UMLRelation>();
		
		for (final UMLArtifact umlArtifact : drawer.getUMLCanvas().getArtifactById().values()) {
			if (umlArtifact instanceof ClassArtifact) {
				ClassArtifact classArtifact  = (ClassArtifact)umlArtifact;
				umlClasses.add(classArtifact.toUMLComponent());
			} else if (umlArtifact instanceof ClassRelationLinkArtifact) {
				ClassRelationLinkArtifact relationLinkArtifact = (ClassRelationLinkArtifact)umlArtifact;
				umlRelations.add(relationLinkArtifact.toUMLComponent());
			}
		}
		
		if (umlClasses.size() ==0) {
			ErrorPopUp errorPopup = new ErrorPopUp("Your diagram must hava at least one class to generate a POJO.");
			errorPopup.show();
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
