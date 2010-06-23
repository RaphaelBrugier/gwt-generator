/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.mockutil;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class MockUtil {

	/**
	 * Return a Clickable object from a widget implementing HasClickHandlers interface.
	 * A clickable object allow to programmatically click on the widget.
	 * @param widget The widget where attach the clickable object.
	 * @return
	 */
	public static Clickable buildClickable(HasClickHandlers widget) {
		final Clickable clickable = new Clickable();
		
		when(widget.addClickHandler(any(ClickHandler.class))).thenAnswer(new Answer<HandlerRegistration>() {
			@Override
			public HandlerRegistration answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				ClickHandler clickHandler = (ClickHandler) args[0];
				clickable.setClickHandler(clickHandler);
				return null;
			}
		});
		
		return clickable;
	}
}
