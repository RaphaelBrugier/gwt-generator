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
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent.EditDiagramEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorToaster;
import com.objetdirect.gwt.gen.client.ui.popup.MessageToaster;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;


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
		
		HasClickHandlers getGenerateButton();
		
		void displayLoadingMessage();
		
		/** Remove message from the view, like the loading message. 
		 */
		void clearAllMessages();
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
		
		bindToEventBus();
		bind();
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
		
		display.getGenerateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		});
	}
	
	private void bindToEventBus() {
		eventBus.addHandler(EditDiagramEvent.TYPE, new EditDiagramEventHandler() {
			@Override
			public void onEditDiagramEvent(EditDiagramEvent event) {
				doLoadDiagram(event.getDiagramDto());
			}
		});
	}
	
	
	/**
	 * Load a diagram from the base and setup it on the canvas.
	 * @param diagramDto the diagram to load.
	 */
	private void doLoadDiagram(DiagramDto diagramDto) {
		display.displayLoadingMessage();
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

	/**
	 *  Force the resize of the modeler container and of all its children, including the drawerPanel and the canvas. 
	 */
	private void forceModelerResize() {
		display.getModelerContainer().onResize();
	}
}
