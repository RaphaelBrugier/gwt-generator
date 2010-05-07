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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree; 

/**
 * Bundle the images used for the Tree panel of directories
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface TreeProjectsResources extends Tree.Resources {

		public static final TreeProjectsResources INSTANCE = GWT.create(TreeProjectsResources.class);
	
	    /**
	     * An image indicating a closed branch.
	     */
//		@Source("images/noimage.png")
//		@Source("images/projects_folder_badged.png")
//		public ImageResource treeClosed();

	    /**
	     * An image indicating a leaf.
	     */
//		@Source("images/noimage.png")
//	    public ImageResource treeLeaf();

	    /**
	     * An image indicating an open branch.
	     */
//		@Source("images/noimage.png")
//		@Source("images/projects_folder_badged.png")
//	    public ImageResource treeOpen();
		
		@Source("images/projects_folder_badged.png")
	    public ImageResource projectIcon();
		
		@Source("images/folder_open.png")
	    public ImageResource directoryIcon();
		
		@Source("images/addDiagram.png")
		public ImageResource createDiagramIcon();
		
		
		@Source("images/deleteIcon.png")
		public ImageResource deleteIcon();
		
		@Source("images/add.png")
		public ImageResource addIcon();
}
