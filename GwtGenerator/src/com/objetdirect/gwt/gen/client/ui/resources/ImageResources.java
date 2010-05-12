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
package com.objetdirect.gwt.gen.client.ui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Images bundle.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface ImageResources extends ClientBundle {

	public static final ImageResources INSTANCE = GWT.create(ImageResources.class);
	
	@Source("images/ajax-loader.gif")
	public ImageResource ajaxLoader();
	@Source("images/circleAjaxLoader.gif")
	public ImageResource circleAjaxLoader();
	
	@Source("images/odlabs.png")
	public ImageResource ODLabsLogo();
	
	@Source("images/GwtGeneratorLogo.png")
	public ImageResource GwtGeneratorLogo();
	
	@Source("images/delete.png")
	public ImageResource deleteIcon();
	
	@Source("images/EditIcon.png")
	public ImageResource EditIcon();
	
	@Source("images/background.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	public ImageResource background();
}
