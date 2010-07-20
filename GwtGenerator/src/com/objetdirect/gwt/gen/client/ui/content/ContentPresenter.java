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


import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.ChangeDiagramButtonsStateEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.GenerateEvent;
import com.objetdirect.gwt.gen.client.event.SaveDiagramEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent.EditDiagramEventHandler;
import com.objetdirect.gwt.gen.client.event.GenerateEvent.GenerateEventHandler;
import com.objetdirect.gwt.gen.client.event.SaveDiagramEvent.SaveDiagramEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorToaster;
import com.objetdirect.gwt.gen.client.ui.popup.MessageToaster;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.ObjectDiagramDto;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.Drawer;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ObjectDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;


/**
 * Presenter : Manage the content panel
 * The content panel displays the modeler and the generated code.
 * It also displays a navigation bar to do some simple actions on the current diagram displayed.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class ContentPresenter {

	private static final String PACKAGE_NAME = "com.od.test";

	public interface Display {
		/**
		 * @return The view as a widget.
		 */
		Widget asWidget();

		/**
		 * @return The main container panel.
		 */
		LayoutPanel getMainContainer();

		HasClickHandlers getBackToModelerButton();

		/** Clear the main container panel and set the given widget
		 * @param w The widget to set in the container.
		 */
		void setInMainContainer(Widget widget);

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
		 * @param diagramType type of the diagram
		 * @return The build drawerPanel
		 */
		Drawer buildDrawer(UMLCanvas umlCanvas, DiagramType diagramType);

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

	private Drawer drawer;

	public ContentPresenter(HandlerManager eventBus, Display display, DiagramServiceAsync diagramService, GeneratorServiceAsync generatorService) {
		this.eventBus = eventBus;
		this.display = display;
		this.diagramService = diagramService;
		this.generatorService = generatorService;

		bindToEventBus();
		bind();
	}


	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	private void bindToEventBus() {
		eventBus.addHandler(EditDiagramEvent.TYPE, new EditDiagramEventHandler() {
			@Override
			public void onEditDiagramEvent(EditDiagramEvent event) {
				doLoadDiagram(event.getDiagramDto());
			}
		});

		eventBus.addHandler(SaveDiagramEvent.TYPE, new SaveDiagramEventHandler() {
			@Override
			public void onSaveDiagramEvent(SaveDiagramEvent event) {
				doSaveDiagram();
			}
		});


		eventBus.addHandler(GenerateEvent.TYPE, new GenerateEventHandler() {
			@Override
			public void onGenerateEvent(GenerateEvent event) {
				doGenerateCode();
			}
		});
	}

	private void bind() {
		display.getBackToModelerButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doDisplayDrawer();
			}
		});
	}

	/**
	 * Load a diagram from the base and setup it on the canvas.
	 * @param diagramDto the diagram to load.
	 */
	private void doLoadDiagram(final DiagramDto diagramDto) {
		Widget loadingWidget = display.buildLoadingWidget("Loading the diagram, please wait ...");
		display.setInMainContainer(loadingWidget);
		diagramService.getDiagram(diagramDto.getKey(), new AsyncCallback<DiagramDto>() {

			@Override
			public void onSuccess(DiagramDto diagramFound) {
				currentDiagram = diagramFound;
				int canvasWidth = 0;
				int canvasHeight = 0;
				UMLCanvas umlCanvas = diagramFound.getCanvas();
				if (umlCanvas == null) {
					umlCanvas = UMLCanvas.createUmlCanvas(diagramFound.getType());
					diagramFound.setCanvas(umlCanvas);
				}
				
				umlCanvas.setUpAfterDeserialization(canvasWidth, canvasHeight);


				drawer = display.buildDrawer(umlCanvas, diagramFound.getType());
				display.getMainContainer().clear();
				display.getMainContainer().add(drawer);
				
				 // In the case where we are displaying the seam class diagram, we disable the edition
				if (diagramDto.seamSpecialDiagram) {
					umlCanvas.setMouseEnabled(false);
					drawer.setHotKeysEnabled(false);
				}

				forceModelerResize();
				MessageToaster.show("Diagram loaded");
				eventBus.fireEvent(new ChangeDiagramButtonsStateEvent(true));
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
		currentDiagram.setCanvas(drawer.getUmlCanvas());
		diagramService.saveDiagram(currentDiagram, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				MessageToaster.show("Diagram " + currentDiagram.getName() + " saved with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("ContentPresenter::doSaveDiagram() Failed to save the diagram " + caught.getMessage());
				ErrorToaster.show("Failed to save the diagram, please retry in few moments or contact the administrator if the problem persist." + caught.getMessage());
			}
		});
	}

	private void doDisplayDrawer() {
		display.getMainContainer().clear();
		display.getMainContainer().add(drawer);
	}

	private void doGenerateCode() {
		display.cleanAllCode();
		Widget loadingWidget = display.buildLoadingWidget("Generating the code, please wait...");
		display.setInMainContainer(loadingWidget);
		
		switch(currentDiagram.getType()) {
			case CLASS : doGenerateHibernateCode(); break;
			case OBJECT : doGenerateSeamCode();
		}
	}
	
	private void doGenerateHibernateCode() {
		ClassDiagram classDiagram = (ClassDiagram)drawer.getUmlCanvas();
		List<UMLClass> umlClasses = classDiagram.getUmlClasses();
		List<UMLRelation> umlRelations = classDiagram.getClassRelations();

		generatorService.generateHibernateCode(umlClasses, umlRelations, PACKAGE_NAME, new AsyncCallback<List<GeneratedCode>>() {
			@Override
			public void onSuccess(List<GeneratedCode> result) {
				doDisplayGeneratedCode(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				doFailWhileGeneratingCode(caught);
			}
		});
	}
	
	private void doGenerateSeamCode() {
		ObjectDiagram objectDiagram = (ObjectDiagram) drawer.getUmlCanvas();
		
		
		List<UMLObject> umlObjects = objectDiagram.getObjects();
		List<ObjectRelation> objectRelations = objectDiagram.getObjectRelations();
		List<UMLClass> classes = objectDiagram.getClasses();
		
		ObjectDiagramDto objectDiagramDto = new ObjectDiagramDto(classes, umlObjects, objectRelations);
		
		generatorService.generateSeamCode(objectDiagramDto, new AsyncCallback<List<GeneratedCode>>() { 
			@Override
			public void onSuccess(List<GeneratedCode> result) {
				doDisplayGeneratedCode(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				doFailWhileGeneratingCode(caught);
			}
		});
	}
	
	private void doDisplayGeneratedCode(List<GeneratedCode> generatedClasses)  {
		for (GeneratedCode generatedCode : generatedClasses) {
			String className = generatedCode.getClassName();
			display.addClassCode(className, generatedCode.getLinesOfCode());
		}

		display.goToFirstClass();
	}
	
	private void doFailWhileGeneratingCode(Throwable caught) {
		if (caught instanceof UMLException || caught instanceof  GWTGeneratorException) {
			Widget errorWidget = display.buildInformationWidget(caught.getMessage());
			display.setInMainContainer(errorWidget);
		} else {
			ErrorToaster.show(caught.getMessage());
		}
	}

	/**
	 *  Force the resize of the modeler container and of all its children, including the drawerPanel and the canvas.
	 */
	private void forceModelerResize() {
		display.getMainContainer().onResize();
	}
}
