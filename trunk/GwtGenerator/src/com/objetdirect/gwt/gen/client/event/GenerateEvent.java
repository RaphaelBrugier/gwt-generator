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
 package com.objetdirect.gwt.gen.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.objetdirect.gwt.gen.client.event.GenerateEvent.GenerateEventHandler;

/**
 * Event to request the generation of code from the current displayed diagram.
 * Event system based on {@link http://code.google.com/intl/fr/webtoolkit/doc/latest/tutorial/mvp-architecture.html }
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class GenerateEvent extends GwtEvent<GenerateEventHandler>{

	public static Type<GenerateEventHandler> TYPE = new Type<GenerateEventHandler>();
	
	public interface GenerateEventHandler extends EventHandler {
		void onGenerateEvent(GenerateEvent event);
	}

	public GenerateEvent() {}

	@Override
	protected void dispatch(GenerateEventHandler handler) {
		handler.onGenerateEvent(this);
	}

	@Override
	public Type<GenerateEventHandler> getAssociatedType() {
		  return TYPE;
	}
}
