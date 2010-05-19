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

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent.CreateDiagramEventHandler;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent.EditDiagramEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.MessagePopUp;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram;


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
		
		HasClickHandlers getSaveButton();
	}
	
	private final HandlerManager eventBus;
	
	private final Display display;
	
	private final DiagramServiceAsync diagramService;
	
	private DiagramDto currentDiagram;
	
	private DrawerPanel drawer;
	
	public ContentPresenter(HandlerManager eventBus, Display display, DiagramServiceAsync diagramService) {
		this.eventBus = eventBus;
		this.display = display;
		this.diagramService = diagramService;
		
		bindToBus();
		bind();
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
	
	private void bind() {
		display.getSaveButton().addClickHandler(new ClickHandler(
				) {
			
			@Override
			public void onClick(ClickEvent event) {
				doSaveDiagram();
			}
		});
	}
	
	private void bindToBus() {
		eventBus.addHandler(CreateDiagramEvent.TYPE, new CreateDiagramEventHandler() {
			
			@Override
			public void onCreateDiagramEvent(CreateDiagramEvent event) {
				doCreateNewDiagram(event.getDiagramInformations());
			}
		});
		
		eventBus.addHandler(EditDiagramEvent.TYPE, new EditDiagramEventHandler() {
			@Override
			public void onEditDiagramEvent(EditDiagramEvent event) {
				doLoadDiagram(event.getDiagramDto());
			}
		});
	}
	
	/**
	 * Create a new diagram in the base and setup the canvas with it.
	 * @param diagramInformations
	 */
	private void doCreateNewDiagram(final DiagramDto diagramInformations) {
		LoadingPopUp.getInstance().startProcessing("Creating a new diagram and loading the designer, please wait...");
		
		diagramService.createDiagram(diagramInformations.getDirectoryKey(), diagramInformations.getType(), diagramInformations.getName(), new 
			AsyncCallback<String>() {
				@Override
				public void onSuccess(String key) {
					Log.debug("ContentPresenter::doCreateDiagram() diagram.Type = " + diagramInformations.getType());
					currentDiagram = new DiagramDto(key,diagramInformations.getDirectoryKey(), diagramInformations.getName(), diagramInformations.getType());
					
					UMLDiagram.Type diagramType = UMLDiagram.Type.getUMLDiagramFromIndex(diagramInformations.getType().ordinal()); 
					drawer = new DrawerPanel(diagramType);
					display.getModelerContainer().clear();
					display.getModelerContainer().add(drawer);
					
					drawer.addDefaultNode(diagramType);
					
					LoadingPopUp.getInstance().stopProcessing();
					forceModelerResize();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Log.error("Error while creation the diagram " + caught.getMessage());
					new ErrorPopUp(caught).show();
				}
			});
	};
	
	/**
	 * Load a diagram from the base and setup it on the canvas.
	 * @param diagramDto the diagram to load.
	 */
	private void doLoadDiagram(DiagramDto diagramDto) {
		LoadingPopUp.getInstance().startProcessing("Loading the diagram "+ diagramDto.getName() +" and the designer, please wait...");
		
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
				
				LoadingPopUp.getInstance().stopProcessing();
				forceModelerResize();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				LoadingPopUp.getInstance().stopProcessing();
				Log.error(caught.getMessage());
			}
		});
	}
	
	/**
	 * Save the current diagram and its canvas in the base.
	 */
	private void doSaveDiagram() {
		LoadingPopUp.getInstance().startProcessing("Saving the diagram, please wait...");
		currentDiagram.setCanvas(drawer.getUMLCanvas());
		diagramService.saveDiagram(currentDiagram, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				LoadingPopUp.getInstance().stopProcessing();
				MessagePopUp popUp = new MessagePopUp("Saved with success");
				popUp.show();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to save the diagram " + caught.getMessage());
				LoadingPopUp.getInstance().stopProcessing();
				ErrorPopUp errorPopUp = new ErrorPopUp(caught);
				errorPopUp.show();
			}
		});
	}

	/**
	 *  Force the resize of the modeler container and all its children, including the drawerPanel and the canvas. 
	 */
	private void forceModelerResize() {
		display.getModelerContainer().onResize();
	}
}
