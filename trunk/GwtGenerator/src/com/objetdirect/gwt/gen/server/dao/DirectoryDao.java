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
package com.objetdirect.gwt.gen.server.dao;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.shared.entities.Directory;

/**
 * Data access object for the directory entities.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class DirectoryDao {
	/**
	 * Get all the projects of the logged user
	 * @return a list of project.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Directory> getDirectories() {
		PersistenceManager pm = ServerHelper.getPM();
		List<Directory> queryResult;
		Collection<Directory> directoryFound = null;
		try {
			Query q = pm.newQuery(Directory.class, "email == e");
		    q.declareParameters("String e");
		    queryResult = (List<Directory>) q.execute(ServerHelper.getCurrentUser().getEmail());
		    directoryFound = pm.detachCopyAll(queryResult);
		    
		} finally {
			pm.close();
		}

		return directoryFound;
	}
}
