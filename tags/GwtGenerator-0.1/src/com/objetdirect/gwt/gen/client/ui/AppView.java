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
package com.objetdirect.gwt.gen.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.ui.resources.BaseCss;


/**
 * View : Application. position for all the main widgets.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class AppView extends LayoutPanel implements AppPresenter.Display  {

	private static AppViewUiBinder uiBinder = GWT
			.create(AppViewUiBinder.class);

	interface AppViewUiBinder extends UiBinder<Widget, AppView> {}
	
	@UiField
	Anchor newProject;
	
	@UiField
	Anchor save;
	
	@UiField
	Anchor generate;
	
	@UiField
	Anchor signOut;
	
	@UiField
	SplitLayoutPanel splitPanel;
	
	@UiField
	FlowPanel westPanel;
	
	@UiField
	LayoutPanel content;
	
	@UiField
	BaseCss baseStyle;
	
	public AppView() {
		this.add(uiBinder.createAndBindUi(this));
		setDiagramButtonsEnabled(false);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasWidgets getContentContainer() {
		return content;
	}

	@Override
	public HasWidgets getDiagramsListContainer() {
		return westPanel;
	}

	@Override
	public HasClickHandlers getNewProjectButton() {
		return newProject;
	}
	
	@Override
	public HasClickHandlers getGenerateButton() {
		return generate;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	@Override
	public void setSignOutHref(String href) {
		signOut.setHref(href);
	}

	@Override
	public void setDiagramButtonsEnabled(boolean enabled) {
		if (enabled) {
			save.removeStyleName(baseStyle.buttonInactive());
			save.addStyleName(baseStyle.button());
			generate.removeStyleName(baseStyle.buttonInactive());
			generate.addStyleName(baseStyle.button());
		} else {
			save.removeStyleName(baseStyle.button());
			save.addStyleName(baseStyle.buttonInactive());
			generate.removeStyleName(baseStyle.button());
			generate.addStyleName(baseStyle.buttonInactive());
		}
		save.setEnabled(enabled);
		generate.setEnabled(enabled);
	}
}
