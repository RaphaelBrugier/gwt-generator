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

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.DefaultRowRenderer;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;

/**
 * Widget that displays the list of the stored diagrams for the logged user.
 * From each line of the list the user can edit or delete a diagram.
 * 
 * This widget wrap a PaginScrollTable from the gwt-incubator page.
 * For a complete tutorial about using PagingScrollTable, see http://zenoconsulting.wikidot.com/blog:17
 * @see http://zenoconsulting.wikidot.com/blog:17
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DiagramList extends SimplePanel {

	final private  DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	private final HandlerManager eventBus;
	
	private SimplePanel container;
	
	private DataSourceTableModel tableModel;
	private PagingScrollTable<DiagramDto> pagingScrollTable;
	
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
	
	public void fetchDiagramList() {
		container.clear();
		
		PagingScrollTable<DiagramDto> pagingScrollTable = createScrollTable();
		pagingScrollTable.setHeight("450px");
		pagingScrollTable.setWidth("90%");
		PagingOptions pagingOptions = new PagingOptions(pagingScrollTable);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(pagingScrollTable);
		vp.add(pagingOptions);
		
		container.add(vp);
		pagingScrollTable.gotoFirstPage();
	}
	
	private void refreshCurrentPage() {
		tableModel.refreshData();
		pagingScrollTable.gotoPage(pagingScrollTable.getCurrentPage(), true);
	}
	
	
	/**
	 * Initializes the scroll table
	 * @return
	 */
	private PagingScrollTable<DiagramDto> createScrollTable() {
		// create our own table model
		tableModel = new DataSourceTableModel();
		// add it to cached table model
//		CachedTableModel<DiagramDto> cachedTableModel = createCachedTableModel(tableModel);
		
		// create the table definition
		TableDefinition<DiagramDto> tableDef = createTableDefinition();
		
		// create the paging scroll table
		pagingScrollTable = new PagingScrollTable<DiagramDto>(tableModel, tableDef);
		pagingScrollTable.setPageSize(15);
		pagingScrollTable.setEmptyTableWidget(new HTML("There is no diagram to display. Go to page 1 or create a new diagram."));
		pagingScrollTable.getDataTable().setSelectionPolicy(SelectionPolicy.ONE_ROW);
		pagingScrollTable.getDataTable().setWidth("100%");
		
		// setup the bulk renderer
		FixedWidthGridBulkRenderer<DiagramDto> bulkRenderer = new FixedWidthGridBulkRenderer<DiagramDto>(pagingScrollTable.getDataTable(), pagingScrollTable);
		pagingScrollTable.setBulkRenderer(bulkRenderer);
		
		// setup the formatting
		pagingScrollTable.setCellPadding(3);
		pagingScrollTable.setCellSpacing(0);
		pagingScrollTable.setResizePolicy(ResizePolicy.FILL_WIDTH);
		
		pagingScrollTable.setSortPolicy(SortPolicy.SINGLE_CELL);
		
		return pagingScrollTable;
	}
	
	private DefaultTableDefinition<DiagramDto> createTableDefinition() {
		DefaultTableDefinition<DiagramDto> tableDefinition = new DefaultTableDefinition<DiagramDto>();
		
		// set the row renderer
		final String[] rowColors = new String[] { "#FFFFDD", "EEEEEE" };
		tableDefinition.setRowRenderer(new DefaultRowRenderer<DiagramDto>(rowColors));
		
		// Name
		{
			NameColumnDefinition columnDef = new NameColumnDefinition();
			columnDef.setColumnSortable(true);
			columnDef.setColumnTruncatable(false);
//			columnDef.setPreferredColumnWidth(90);
			columnDef.setHeader(0, new HTML("Name"));
			columnDef.setHeaderCount(1);
			columnDef.setHeaderTruncatable(false);
			tableDefinition.addColumnDefinition(columnDef);
		}
		// Type
		{
			TypeColumnDefinition columnDef = new TypeColumnDefinition();
			columnDef.setColumnSortable(true);
			columnDef.setColumnTruncatable(false);
			columnDef.setPreferredColumnWidth(40);
			columnDef.setHeader(0, new HTML("Diagram type"));
			columnDef.setHeaderCount(1);
			columnDef.setHeaderTruncatable(false);
			tableDefinition.addColumnDefinition(columnDef);
		}
		// Action column
		{
			ActionColumnDefinition columnDef = new ActionColumnDefinition();
			columnDef.setColumnSortable(false);
			columnDef.setColumnTruncatable(false);
			columnDef.setMaximumColumnWidth(120);
			columnDef.setMinimumColumnWidth(120);
			columnDef.setHeader(0, new HTML("Action"));
			columnDef.setHeaderCount(1);
			columnDef.setHeaderTruncatable(false);
			tableDefinition.addColumnDefinition(columnDef);
		}
		return tableDefinition;
	}
	
	/**
	 * Create the {@link CachedTableModel}
	 * @param tableModel 
	 * @return
	 */
//	private CachedTableModel<DiagramDto> createCachedTableModel(DataSourceTableModel tableModel) {
//		CachedTableModel<DiagramDto> tm = new CachedTableModel<DiagramDto>(tableModel);
//		tm.setPreCachedRowCount(15);
//		tm.setPostCachedRowCount(15);
//		return tm;
//	}
	
	/**
	 * Model used by the paging scroll table to request the data.
	 */
	private class DataSourceTableModel extends MutableTableModel<DiagramDto> {
		
		private ArrayList<DiagramDto> cachedDiagrams;
		
		public void refreshData() {
			cachedDiagrams = null;
		}
		
		@Override
		protected boolean onRowInserted(int beforeRow) {
			cachedDiagrams = null;
			return true;
		}

		@Override
		protected boolean onRowRemoved(int row) {
			Log.debug("TableModel onRowRemoved row = " + row);
			cachedDiagrams = null;
			return true;
		}

		@Override
		protected boolean onSetRowValue(int row, DiagramDto rowValue) {
			cachedDiagrams = null;
			return true;
		}

		@Override
		public void requestRows(final Request request, final Callback<DiagramDto> callback) {
			final int startRow = request.getStartRow();
			final int numRows = request.getNumRows();
			
			// If we did not have cached the data, call the service and retrieve all diagrams
			if (cachedDiagrams == null) {
				cachedDiagrams = new ArrayList<DiagramDto>();
				
				diagramService.getDiagrams(new AsyncCallback<ArrayList<DiagramDto>>() {
					
					@Override
					public void onSuccess(final ArrayList<DiagramDto> result) {
						cachedDiagrams = result;
						
						// Use the TableCallback to return a sublist of the cached diagrams. 
						callback.onRowsReady(request, new Response<DiagramDto>() {
							@Override
							public Iterator<DiagramDto> getRowValues() {
								LoadingPopUp.getInstance().stopProcessing();
								return iteratorPagingList(startRow, numRows);
							}});
					}
					
					@Override
					public void onFailure(Throwable caught) {
						LoadingPopUp.getInstance().stopProcessing();
						Log.debug(caught.getLocalizedMessage());
						callback.onFailure(caught);
					}
				});
			}
			
			// If we have already cached the data, just return a sublist with the TableCallback.
			else {
				callback.onRowsReady(request, new Response<DiagramDto>() {
					@Override
					public Iterator<DiagramDto> getRowValues() {
						return iteratorPagingList(startRow, numRows);
					}
				});
			}
		}
		
		
		/**
		 * Return an iterator on the sublist of the cached diagram list. 
		 * The sublist is defined by the start row and the number of rows requested.
		 * If the startRow is superior of the cached diagrams list, return an iterator on an empty list.
		 * @param startRow
		 * @param numRows
		 * @return an iterator on the sublist.
		 */
		private Iterator<DiagramDto> iteratorPagingList(int startRow, int numRows) {
			if (startRow > cachedDiagrams.size() || startRow < 0)
				return new ArrayList<DiagramDto>().iterator();
				
			int toIndex;
			if (startRow + numRows > cachedDiagrams.size())
				toIndex = cachedDiagrams.size();
			else
				toIndex = startRow + numRows;
			
			return cachedDiagrams.subList(startRow, toIndex).iterator();
		}
	}
	
	/**
	 * Defines the column for {@link DiagramDto#getType()} 
	 */
	private final class TypeColumnDefinition extends AbstractColumnDefinition<DiagramDto, String> {

		@Override
		public String getCellValue(DiagramDto rowValue) {
			return rowValue.getType().toString().toLowerCase();
		}
		@Override
		public void setCellValue(DiagramDto rowValue, String cellValue) { }
	}
	
	
	/**
	 * Defines the column for {@link DiagramDto#getName()} 
	 */
	private final class NameColumnDefinition extends AbstractColumnDefinition<DiagramDto, String> {

		@Override
		public String getCellValue(DiagramDto rowValue) {
			return rowValue.getName();
		}
		@Override
		public void setCellValue(DiagramDto rowValue, String cellValue) { }
	}
	
	
	/**
	 * Defines the column for editing a diagram. 
	 */
	private final class ActionColumnDefinition extends AbstractColumnDefinition<DiagramDto, Widget> {

		@Override
		public Widget getCellValue(DiagramDto rowValue) {
			return createActionsButton(rowValue);
		}
		@Override
		public void setCellValue(DiagramDto rowValue, Widget cellValue) { }
	}
	
	private Widget createActionsButton(final DiagramDto diagram) {
		FlowPanel hpanel = new FlowPanel();
//		hpanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hpanel.addStyleName("actionsContainer");
		
		Image editIcon = new Image(ImageResources.INSTANCE.EditIcon());
		editIcon.addStyleDependentName("action");
		editIcon.setTitle("edit");
		editIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditDiagramEvent(diagram));
			}
		});
		
		hpanel.add(editIcon);
		
		Image deleteIcon = new Image(ImageResources.INSTANCE.deleteIcon());
		deleteIcon.addStyleDependentName("action");
		deleteIcon.setTitle("delete");
		deleteIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoadingPopUp.getInstance().startProcessing("Deleting the diagram, please wait...");
				diagramService.deleteDiagram(diagram.getKey(), new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						refreshCurrentPage();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						ErrorPopUp errorPopUp = new ErrorPopUp(caught);
						LoadingPopUp.getInstance().stopProcessing();
						errorPopUp.show();
					}
				});
			}
		});
		
		hpanel.add(deleteIcon);
		
		return hpanel;
	}
	

}
