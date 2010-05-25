/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.client.ui.content;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;

/**
 * View : Manage the content panel
 * The content panel displays the modeler and the generated code.
 * It also displays a navigation bar to do some simple actions on the current diagram displayed.
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class ContentView extends ResizeComposite implements ContentPresenter.Display {

	private static ContentViewUiBinder uiBinder = GWT
			.create(ContentViewUiBinder.class);

	interface ContentViewUiBinder extends UiBinder<Widget, ContentView> {
	}

	public interface ContentStyle extends CssResource {
		String horizontalAlignCenter();
		String loadingMessage();
		String generatedCodeWrapper();
		String codePanel();
	}
	
	public interface ContentResources extends ClientBundle {
		public ContentResources INSTANCE = GWT.create(ContentResources.class);
		
		@Source("ContentStyle.css")
		ContentStyle css();
	}
	
	@UiField
	LayoutPanel contentPanel;
	
	@UiField
	Button saveButton;
	
	@UiField
	Button generateButton;
	
	private TabLayoutPanel tabPanel;

	public ContentView() {
		initWidget(uiBinder.createAndBindUi(this));
		ContentResources.INSTANCE.css().ensureInjected();
		tabPanel = new TabLayoutPanel(2, Unit.EM);
	}


	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public LayoutPanel getMainContainer() {
		return contentPanel;
	}
	
	@Override
	public void setInMainContainer(Widget widget) {
		contentPanel.clear();
		contentPanel.add(widget);
	}


	@Override
	public Button getSaveButton() {
		return saveButton;
	}
	
	@Override
	public Button getSwitchModeButton() {
		return generateButton;
	}

	@Override
	public Widget buildLoadingWidget(String message) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(css().horizontalAlignCenter());
		
		Image ajaxLoader = new Image(ImageResources.INSTANCE.ajaxLoader());
		ajaxLoader.addStyleName(css().loadingMessage());
		
		Label loadingMessage = new Label(message);
		
		panel.add(ajaxLoader);
		panel.add(loadingMessage);
		return panel;
	}
	
	@Override
	public Widget buildInformationWidget(String message) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(css().horizontalAlignCenter());
		
		Label messageLabel = new Label(message);
		messageLabel.addStyleName(css().loadingMessage());
		
		panel.add(messageLabel);
		return panel;
	}
	
	@Override
	public DrawerPanel buildDrawer(UMLCanvas umlCanvas) {
		return new DrawerPanel(umlCanvas);
	}
	
	@Override
	public void addClassCode(String className, List<String> codeLines) {
		StringBuilder lines = new StringBuilder();
		for (String line : codeLines) {
			lines.append(line).append("\n");
		}
		TextArea ta = new TextArea();
		ta.setText(lines.toString());
		ta.setReadOnly(false);
		ta.setHeight("100%");
		ta.setWidth("100%");
		ta.addStyleName(css().codePanel());
		
		// Wrap the textArea, it allows to display the scrollbar
		SimplePanel wrapper = new SimplePanel();
		wrapper.addStyleName(css().generatedCodeWrapper());
		wrapper.add(ta);
		
		tabPanel.add(wrapper, className);
		//Remove the default css style
		wrapper.removeStyleName("gwt-TabLayoutPanelContent");
	}
	
	@Override
	public void cleanAllCode() {
		tabPanel.clear();
	}

	@Override
	public void goToFirstClass() {
		// Not sure if this is a control method and should be moved in the presenter ?
		contentPanel.clear();
		contentPanel.add(tabPanel);
		tabPanel.selectTab(0);
	}
	
	/**
	 * Helper Method to access to the css resources
	 * @return
	 */
	private ContentStyle css() {
		return ContentResources.INSTANCE.css();
	}
	
}
