/*
 * This file is part of the GWTUML project and was written by Mounier Florian <mounier-dot-florian.at.gmail'dot'com> for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct Contact: gwtuml@googlegroups.com
 * 
 * GWTUML is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * GWTUML is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GWTUML. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
import com.objetdirect.gwt.gen.client.ui.Main;
import com.objetdirect.gwt.umlapi.client.helpers.HotKeyManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.Session;

/**
 * Main class for gwtuml application. This class does some initialization and calls the start panel.
 * 
 * @author Henri Darmet
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class GwtGenerator implements EntryPoint {
	private final static DockPanel	appRootPanel	= new DockPanel();
	static HorizontalPanel			southBar;
	
	private final static GeneratorServiceAsync generatorService = GWT.create(GeneratorService.class);

	/**
	 * Entry point of the application This class make a StartPanel and manage the history for it
	 */
	public void gwt_main() {

		GwtGenerator.southBar = new HorizontalPanel();
		GwtGenerator.southBar.setVisible(false);
		GwtGenerator.southBar.setSpacing(5);
		final Button toUrl = new Button("Export to url");
		toUrl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				HistoryManager.upgradeDiagramURL(Session.getActiveCanvas().toUrl());
			}
		});
		GwtGenerator.southBar.add(toUrl);
		final Button clearUrl = new Button("Clear diagram in url");
		clearUrl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				HistoryManager.upgradeDiagramURL("");
			}
		});
		GwtGenerator.southBar.add(clearUrl);
		final Button exportToSvg = new Button("Export to SVG (Experimental)");
		exportToSvg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				String svg = "<?xml version='1.0' standalone='no'?><!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>";
				Session.getActiveCanvas().clearArrows();
				svg += DOM.getInnerHTML((Element) Session.getActiveCanvas().getElement().getFirstChildElement());
				Window.open("data:image/xml+svg," + svg, "SVG export", "top");
				Session.getActiveCanvas().makeArrows();
			}
		});
		GwtGenerator.southBar.add(exportToSvg);
		
		OptionsManager.initialize();
		HotKeyManager.forceStaticInit();
		final HistoryManager historyManager = new HistoryManager();
		historyManager.initApplication(GwtGenerator.appRootPanel);
		
		DOM.setInnerHTML(RootPanel.get("loading-screen").getElement(), "");
		

		GwtGenerator.appRootPanel.setSize("100%", "100%");

		GwtGenerator.appRootPanel.add(GwtGenerator.southBar, DockPanel.SOUTH);
		RootLayoutPanel.get().add(GwtGenerator.appRootPanel);
	}

	
	/*
	 * Real gwt app entry point, this code allow GWT Log to catch exception and display it (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		Log.setUncaughtExceptionHandler();
		
		Main mainWindow = new Main();
		DOM.setInnerHTML(RootPanel.get("loading-screen").getElement(), "");
		RootLayoutPanel.get().add(mainWindow);
		
	}
}
