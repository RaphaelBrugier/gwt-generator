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
import com.objetdirect.gwt.gen.client.event.BackToHomeEvent.BackToHomeEventHandler;

/**
 * Event to request a back to the home page.
 * Event system based on {@link http://code.google.com/intl/fr/webtoolkit/doc/latest/tutorial/mvp-architecture.html }
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class BackToHomeEvent extends GwtEvent<BackToHomeEventHandler>{

	public static Type<BackToHomeEventHandler> TYPE = new Type<BackToHomeEventHandler>();
	
	public interface BackToHomeEventHandler extends EventHandler {
		void onBackToHomeEventEvent(BackToHomeEvent event);
	}

	public BackToHomeEvent() {}

	@Override
	protected void dispatch(BackToHomeEventHandler handler) {
		handler.onBackToHomeEventEvent(this);
	}

	@Override
	public Type<BackToHomeEventHandler> getAssociatedType() {
		  return TYPE;
	}
	
}
