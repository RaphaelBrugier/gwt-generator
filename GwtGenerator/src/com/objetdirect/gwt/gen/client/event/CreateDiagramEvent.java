package com.objetdirect.gwt.gen.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent.CreateDiagramEventHandler;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations;




/**
 * Event to request the creation of a diagram and setup it in the designer.
 * Event system based on {@link http://code.google.com/intl/fr/webtoolkit/doc/latest/tutorial/mvp-architecture.html }
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class CreateDiagramEvent extends GwtEvent<CreateDiagramEventHandler>{

	public static Type<CreateDiagramEventHandler> TYPE = new Type<CreateDiagramEventHandler>();
	
	public interface CreateDiagramEventHandler extends EventHandler {
		void onCreateDiagramEvent(CreateDiagramEvent event);
	}
	
	private DiagramInformations diagramInformations;

	public CreateDiagramEvent(DiagramInformations diagramInformations) {
		this.diagramInformations = diagramInformations;
	}

	@Override
	protected void dispatch(CreateDiagramEventHandler handler) {
		handler.onCreateDiagramEvent(this);
	}

	@Override
	public Type<CreateDiagramEventHandler> getAssociatedType() {
		  return TYPE;
	}
	
	/**
	 * @return the diagramInformations
	 */
	public DiagramInformations getDiagramInformations() {
		return diagramInformations;
	}
}
