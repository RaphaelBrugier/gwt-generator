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
import com.objetdirect.gwt.umlapi.client.UMLException;
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
		 * @return The main container panel.
		 */
		LayoutPanel getMainContainer();
		
		/** Clear the main container panel and set the given widget
		 * @param w The widget to set in the container.
		 */
		void setInMainContainer(Widget widget);
		
		/**
		 * This buttons save the current displayed diagram
		 * @return The button to save.
		 */
		Button getSaveButton();
		
		/**
		 * This button allow to switch between the modeler and the generated code.
		 * @return the button;
		 */
		Button getSwitchModeButton();
		
		/**
		 * Return a widget containing a loading image and a message.
		 * @param message The message to display in the widget
		 * @return The built widget
		 */
		Widget buildLoadingWidget(String message);
		
		/**
		 * Return a widget containing a message.
		 * @param message The message to display in the widget
		 * @return The built widget
		 */
		Widget buildInformationWidget(String message);
		
		/**
		 * Build a drawerPanel from the given umlCanvas 
		 * @param umlcanvas
		 * @return The build drawerPanel
		 */
		DrawerPanel buildDrawer(UMLCanvas umlCanvas);
		
		/**
		 * Add  tab to the widget to display the code of the given class.
		 * @param className the name of the class
		 * @param codeLines the lines of code of the class
		 */
		void addClassCode(String className, List<String> codeLines);
		
		/** Clean all the generated code in the tabs panel. */
		void cleanAllCode();
		
		/** Display the first tab. */
		void goToFirstClass();
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
				Log.debug("ContentPresenter::Bind() click event received on save button");
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
	
	/**
	 * Enable or disable the buttons for the content panel. 
	 * The buttons should been activated only when a diagram is loaded on the content panel.
	 * @param activated true if the buttons must been activated
	 */
	private void setButtonsActivation(boolean activated) {
		display.getSaveButton().setEnabled(activated);
		display.getSwitchModeButton().setEnabled(activated);
	}
	
	/**
	 * Load a diagram from the base and setup it on the canvas.
	 * @param diagramDto the diagram to load.
	 */
	private void doLoadDiagram(DiagramDto diagramDto) {
		Widget loadingWidget = display.buildLoadingWidget("Loading the diagram, please wait ...");
		display.setInMainContainer(loadingWidget);
		diagramService.getDiagram(diagramDto.getKey(), new AsyncCallback<DiagramDto>() {
			
			@Override
			public void onSuccess(DiagramDto diagramFound) {
				currentDiagram = diagramFound;
//				int canvasWidth = Window.getClientWidth() - 0;
//				int canvasHeight = Window.getClientHeight() - 30;
				int canvasWidth = 0;
				int canvasHeight = 0;
				UMLCanvas umlCanvas = diagramFound.getCanvas();
				umlCanvas.setUpAfterDeserialization(canvasWidth, canvasHeight);
				
				drawer = display.buildDrawer(umlCanvas);
				display.getMainContainer().clear();
				display.getMainContainer().add(drawer);
				
				forceModelerResize();
				MessageToaster.show("Diagram loaded");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				ErrorToaster.show("Failed to load the diagram, please retry in few moments or contact the administrator if the problem persist.");
				Log.error(caught.getMessage());
				display.getMainContainer().clear();
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
		display.getMainContainer().clear();
		display.getMainContainer().add(drawer);
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

		Widget loadingWidget = display.buildLoadingWidget("Generating the code, please wait...");
		display.setInMainContainer(loadingWidget);
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
				if (caught instanceof UMLException) {
					Widget errorWidget = display.buildInformationWidget(caught.getMessage());
					display.setInMainContainer(errorWidget);
				} else {
					ErrorToaster.show(caught.getMessage());
				}
			}
		});
	}

	/**
	 *  Force the resize of the modeler container and of all its children, including the drawerPanel and the canvas. 
	 */
	private void forceModelerResize() {
		display.getMainContainer().onResize();
	}
}
