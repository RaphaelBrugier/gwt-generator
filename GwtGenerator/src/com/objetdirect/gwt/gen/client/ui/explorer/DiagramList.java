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
package com.objetdirect.gwt.gen.client.ui.explorer;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorPopUp;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;

/**
 * Widget that displays the list of the stored diagrams for the logged user.
 * From each line of the list the user can edit or delete a diagram.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DiagramList extends SimplePanel {

	final private  DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	private final HandlerManager eventBus;
	
	private SimplePanel container;
	
	private FlexTable table;
	private int rowPosition;
	
	public DiagramList(HandlerManager eventBus) {
		this.eventBus = eventBus;
		container = new SimplePanel();
		add(container);
	}
	
	public void go(HasWidgets parentContainer) {
		fetchDiagramList();
		parentContainer.clear();
		parentContainer.add(this.container);
	}
	
	
	private void doDeleteDiagram(Long key) {
		diagramService.deleteDiagram(key, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				fetchDiagramList();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				ErrorPopUp errorPopUp = new ErrorPopUp(caught);
				errorPopUp.show();
			}
		});
	}
	
	public void fetchDiagramList() {
		Image loader = new Image(ImageResources.INSTANCE.ajaxLoader());
		container.clear();
		container.add(loader);
		table = new FlexTable();
		table.setWidth("80%");
		table.setCellSpacing(0);
		table.addStyleName("DiagramList");  
		addColumns("Name", 0, "40%");
		addColumns("Type", 1, "20%");
		addColumns("Edit", 2, "20%");
		addColumns("Delete", 3, "20%");
		
		
		diagramService.getDiagrams(new AsyncCallback<Collection<DiagramDto>>() {
			
			@Override
			public void onSuccess(Collection<DiagramDto> result) {
				if (result.size()==0) {
					container.clear();
					container.add(new Label("No diagram found."));
				} else {
					container.clear();
					rowPosition = 1; // row 0 is the header row 
					for (DiagramDto d : result) {
						rowPosition++;
						addRow(d);
					}
					container.add(table);
					applyDataRowStyles();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.debug(caught.getMessage());
				Window.alert("Unable to retrieve diagrams from the base.");
			}
		});
	}
	
	private void addRow(final DiagramDto diagram) {
		Label name = new Label(diagram.getName());
		table.setWidget(rowPosition, 0, name);
		table.getCellFormatter().addStyleName(rowPosition, 0, "DiagramList-Cell");
		
		Label type = new Label(diagram.getType().toString().toLowerCase());
		table.setWidget(rowPosition, 1, type);
		table.getCellFormatter().addStyleName(rowPosition, 1, "DiagramList-Cell");
		
		Anchor edit = new Anchor("edit");
		table.setWidget(rowPosition, 2, edit);
		table.getCellFormatter().addStyleName(rowPosition, 2, "DiagramList-Cell");
		
		edit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditDiagramEvent(diagram));
			}
		});
		
		Anchor delete = new Anchor("delete");
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doDeleteDiagram(diagram.getKey());
			}
		});
		table.setWidget(rowPosition, 3, delete);
		table.getCellFormatter().addStyleName(rowPosition, 3, "DiagramList-Cell");
	}
	
	private void addColumns(String columnHeaderLabel, int columnPosition, String width) {
		Label columnLabel = new Label(columnHeaderLabel);
		columnLabel.addStyleName("DiagramList-ColumnLabel");
		table.getColumnFormatter().setWidth(columnPosition, width);
		
		table.setWidget(0, columnPosition, columnLabel);
		table.getCellFormatter().addStyleName(0, columnPosition,"DiagramList-ColumnLabelCell");
	}
	
	private void applyDataRowStyles() {
	    HTMLTable.RowFormatter rf = table.getRowFormatter();
	    
	    for (int row = 1; row < table.getRowCount(); ++row) {
	      if ((row % 2) != 0) {
	        rf.addStyleName(row, "DiagramList-OddRow");
	      }
	      else {
	        rf.addStyleName(row, "DiagramList-EvenRow");
	      }
	    }
	  }
}
