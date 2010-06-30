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
package com.objetdirect.gwt.gen.client.content;

import static com.objetdirect.gwt.mockutil.MockUtil.buildClickable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent.EditDiagramEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
import com.objetdirect.gwt.gen.client.ui.content.ContentPresenter;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.mockutil.Clickable;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * Not maintained.
 * This test was written to test the mock framework mockito and its integration with the mvp pattern.
 * @author Raphael
 */
@Deprecated
public class TestContentPresenter {
	
	private final String DIAGRAM_KEY = "0";
	private final String DIRECTORY_KEY = "1";
	private final String DIAGRAM_NAME = "theDiagram";
	private final DiagramType DIAGRAM_TYPE = DiagramType.CLASS;
	private final DiagramDto DIAGRAM_FOUND = new DiagramDto(DIAGRAM_KEY, DIRECTORY_KEY, DIAGRAM_NAME, DIAGRAM_TYPE, mock(UMLCanvas.class));
	
	@Mock(answer = Answers.RETURNS_DEEP_STUBS) 
	HandlerManager eventBusMock;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS) 
	GeneratorServiceAsync generatorServiceMock;
	
	@Mock(answer = Answers.RETURNS_DEEP_STUBS) 
	ContentPresenter.Display displayMock;
	
	@Mock Button saveButtonMock;
	@Mock Button switchModeButtonMock;

	DiagramServiceAsync diagramServiceMock;

	// Tested Presenter
	ContentPresenter contentPresenter;
	
	// Click handlers to trigger click event on the buttons.
	Clickable saveButton;
	
	EditDiagramEventHandler editDiagramEventHandler;
	
	@Before
	public void setUp() throws Exception {
		GWTMockUtilities.disarm();
		MockitoAnnotations.initMocks(this);
		
		diagramServiceMock = mock(DiagramServiceAsync.class, new Answer<DiagramServiceAsync>() {
			@Override
			public DiagramServiceAsync answer(InvocationOnMock invocation) throws Throwable {
					if (invocation.getMethod().getName().equals("getDiagram")) {
						AsyncCallback<DiagramDto> callBack = (AsyncCallback<DiagramDto>) invocation.getArguments()[1];
						callBack.onSuccess(DIAGRAM_FOUND);
					}
				return null;
			}
		});
		
		createHandlers();
		
//		when(displayMock.getSaveButton()).thenReturn(saveButtonMock);
//		when(displayMock.getSwitchModeButton()).thenReturn(switchModeButtonMock);
		
		contentPresenter = new ContentPresenter(eventBusMock, displayMock, diagramServiceMock, generatorServiceMock);
	}
	
	
	/** Create and attached convenient handlers to triggered events */
	private void createHandlers() {
		saveButton = buildClickable(saveButtonMock);
		
		when(eventBusMock.addHandler(eq(EditDiagramEvent.TYPE), any(EditDiagramEventHandler.class))).thenAnswer(new Answer<HandlerRegistration>() {
			@Override
			public HandlerRegistration answer(InvocationOnMock invocation) throws Throwable {
				editDiagramEventHandler = (EditDiagramEventHandler) invocation.getArguments()[1];
				return null;
			}
		});
	}
	
	
	private void fireDiagramEvent() {
		DiagramDto diagramDto = new DiagramDto(DIAGRAM_KEY, "1", "Diagram", DiagramType.CLASS);
		EditDiagramEvent editDiagramEvent = new EditDiagramEvent(diagramDto);
		editDiagramEventHandler.onEditDiagramEvent(editDiagramEvent);
	}

	
	@Test
	public void buttonDisabledWhenConstructed() {
		verify(saveButtonMock).setEnabled(false);
		verify(switchModeButtonMock).setEnabled(false);
	}
	
	@Test
	public void buttonEnbleddWhenADiagramIsLoaded() {
		fireDiagramEvent();
	}
	
	@Test
	public void testEditDiagramEventCallGetDiagramService() throws Exception {
		fireDiagramEvent();
		verify(diagramServiceMock).getDiagram(eq(DIAGRAM_KEY), (AsyncCallback<DiagramDto>) any());
	}
	
	
	@Test
	public void clickOnSaveButtonCallService() throws Exception {
		fireDiagramEvent();
//		saveButtonClickHandler.onClick(null);
		saveButton.click();
		
		verify(diagramServiceMock).saveDiagram(null, null);
	}
	
	@After
	public void tearDown() throws Exception {
		GWTMockUtilities.restore();
	}
}
