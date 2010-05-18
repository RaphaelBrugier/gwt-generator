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
import com.objetdirect.gwt.gen.client.event.DisplayDiagramsEvent.DisplayDiagramsEventHandler;
import com.objetdirect.gwt.gen.shared.entities.Directory;




/**
 * Event to request the display of the diagrams of the given directory.
 * Event system based on {@link http://code.google.com/intl/fr/webtoolkit/doc/latest/tutorial/mvp-architecture.html }
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
@Deprecated
public class DisplayDiagramsEvent extends GwtEvent<DisplayDiagramsEventHandler>{

	public static Type<DisplayDiagramsEventHandler> TYPE = new Type<DisplayDiagramsEventHandler>();
	
	public interface DisplayDiagramsEventHandler extends EventHandler {
		void onDisplayDiagramsEvent(DisplayDiagramsEvent event);
	}
	
	private Directory directory;

	public DisplayDiagramsEvent(Directory directory) {
		this.directory = directory;
	}

	@Override
	protected void dispatch(DisplayDiagramsEventHandler handler) {
		handler.onDisplayDiagramsEvent(this);
	}

	@Override
	public Type<DisplayDiagramsEventHandler> getAssociatedType() {
		  return TYPE;
	}
	
	/**
	 * @return the directory
	 */
	public Directory getDirectory() {
		return directory;
	}
}
