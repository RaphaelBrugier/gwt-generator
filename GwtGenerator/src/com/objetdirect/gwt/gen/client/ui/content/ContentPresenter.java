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
package com.objetdirect.gwt.gen.client.ui.content;

import java.util.LinkedList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent.EditDiagramEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorToaster;
import com.objetdirect.gwt.gen.client.ui.popup.MessageToaster;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassRelationLinkArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.UMLArtifact;
import com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;


/**
 * Presenter : Manage the content panel
 * The content panel displays the modeler and the generated code.
 * It also displays a navigation bar to do some simple actions on the current diagram displayed.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class ContentPresenter {

	public interface Display {
		/**
		 * @return The view as a widget.
		 */
		Widget asWidget();
		
		/**
		 * @return The modeler container
		 */
		LayoutPanel getModelerContainer();
		
		Button getSaveButton();
		
		/**
		 * This button allow to switch between the modeler and the generated code.
		 * @return the button;
		 */
		Button getSwitchModeButton();
		
		/**
		 * Display a message in the center of the content panel while loading.
		 * @param message
		 */
		void displayLoadingMessage(String message);
		
		/** Remove message from the view, like the loading message. 
		 */
		void clearAllMessages();
		
		/**
		 * Add  tab to the widget to display the code of the given class.
		 * @param className the name of the class
		 * @param codeLines the lines of code of the class
		 */
		public void addClassCode(String className, List<String> codeLines);
		
		/** Clean all code tab. */
		public void cleanAllCode();
		
		/** Display the first tab. */
		public void goToFirstClass();
	}
	
	private final HandlerManager eventBus;
	
	private final Display display;
	
	private final DiagramServiceAsync diagramService;
	
	private final GeneratorServiceAsync generatorService;
	
	private DiagramDto currentDiagram;
	
	private DrawerPanel drawer;
	
	private boolean isModelerMode;
	
	public ContentPresenter(HandlerManager eventBus, Display display, DiagramServiceAsync diagramService, GeneratorServiceAsync generatorService) {
		this.eventBus = eventBus;
		this.display = display;
		this.diagramService = diagramService;
		this.generatorService = generatorService;
		
		bindToEventBus();
		bind();
		
		setButtonsActivation(false);
		isModelerMode = true;
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
	
	private void bind() {
		display.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doSaveDiagram();
			}
		});
		
		display.getSwitchModeButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(isModelerMode) {
					doGenerateCode();
					isModelerMode = false;
				} else {
					doDisplayDrawer();
					isModelerMode = true;
				}
			}
		});
	}
	
	private void bindToEventBus() {
		eventBus.addHandler(EditDiagramEvent.TYPE, new EditDiagramEventHandler() {
			@Override
			public void onEditDiagramEvent(EditDiagramEvent event) {
				setButtonsActivation(true);
				doLoadDiagram(event.getDiagramDto());
			}
		});
	}
	
	private void setButtonsActivation(boolean activated) {
		display.getSaveButton().setEnabled(activated);
		display.getSwitchModeButton().setEnabled(activated);
	}
	
	/**
	 * Load a diagram from the base and setup it on the canvas.
	 * @param diagramDto the diagram to load.
	 */
	private void doLoadDiagram(DiagramDto diagramDto) {
		display.displayLoadingMessage("Loading the diagram");
		diagramService.getDiagram(diagramDto.getKey(), new AsyncCallback<DiagramDto>() {
			
			@Override
			public void onSuccess(DiagramDto diagramFound) {
				currentDiagram = diagramFound;
				int canvasWidth = Window.getClientWidth() - 0;
				int canvasHeight = Window.getClientHeight() - 30;
				UMLCanvas umlCanvas = diagramFound.getCanvas();
				umlCanvas.setUpAfterDeserialization(canvasWidth, canvasHeight);
				
				drawer = new DrawerPanel(umlCanvas);
				display.getModelerContainer().clear();
				display.getModelerContainer().add(drawer);
				
				forceModelerResize();
				MessageToaster.show("Diagram loaded");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				ErrorToaster.show("Failed to load the diagram, please retry in few moments or contact the administrator if the problem persist.");
				Log.error(caught.getMessage());
				display.clearAllMessages();
			}
		});
	}

	/**
	 * Save the current diagram and its canvas in the base.
	 */
	private void doSaveDiagram() {
		currentDiagram.setCanvas(drawer.getUMLCanvas());
		diagramService.saveDiagram(currentDiagram, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				MessageToaster.show("Diagram " + currentDiagram.getName() + " saved with success.");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to save the diagram " + caught.getMessage());
				ErrorToaster.show("Failed to save the diagram, please retry in few moments or contact the administrator if the problem persist.");
			}
		});
	}
	
	private void doDisplayDrawer() {
		GWTUMLDrawerHelper.disableBrowserEvents();
		display.getSwitchModeButton().setText("Generate");
		display.getModelerContainer().clear();
		display.getModelerContainer().add(drawer);
	}
	
	private void doGenerateCode() {
		GWTUMLDrawerHelper.enableBrowserEvents();
		display.getSwitchModeButton().setText("Back to modeler");
		display.cleanAllCode();
		
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
		
		display.displayLoadingMessage("Generating the code, please wait...");
		String packageName = "com.od.test";
		generatorService.generateClassesCode(umlClasses, umlRelations, packageName, new AsyncCallback<List<GeneratedCode>>() {
			@Override
			public void onSuccess(List<GeneratedCode> result) {
				for (GeneratedCode generatedCode : result) {
					String className = generatedCode.getClassName();
					display.addClassCode(className, generatedCode.getLinesOfCode());
				}
				
				display.goToFirstClass();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				ErrorToaster.show(caught.getMessage());
			}
		});
	}

	/**
	 *  Force the resize of the modeler container and of all its children, including the drawerPanel and the canvas. 
	 */
	private void forceModelerResize() {
		display.getModelerContainer().onResize();
	}
}
