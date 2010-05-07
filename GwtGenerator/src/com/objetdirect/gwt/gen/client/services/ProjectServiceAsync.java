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
package com.objetdirect.gwt.gen.client.services;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * Async counter part of ProjectService
 * @see com.objetdirect.gwt.gen.client.services.ProjectService
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface ProjectServiceAsync {

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#createProject(java.lang.String)
	 */
	void createProject(String name, AsyncCallback<Long> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#getProjects()
	 */
	void getProjects(AsyncCallback<Collection<Project>> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#updateProject(com.objetdirect.gwt.gen.shared.entities.Project)
	 */
	void updateProject(Project project, AsyncCallback<Void> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#addDirectory(com.objetdirect.gwt.gen.shared.entities.Project, java.lang.String)
	 */
	void addDirectory(Project project, String directoryName,
			AsyncCallback<Void> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#deleteProject(com.objetdirect.gwt.gen.shared.entities.Project)
	 */
	void deleteProject(Project projectToDelete, AsyncCallback<Void> callback);
}
