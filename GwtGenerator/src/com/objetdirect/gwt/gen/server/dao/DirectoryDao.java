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

import javax.jdo.PersistenceManager;

import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.shared.entities.Directory;


/**
 * Data access object for the directory entities.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class DirectoryDao {

//	public String createDirectory(Directory directory) {
//		PersistenceManager pm = ServerHelper.getPM();
//		try {
//			System.out.println("Key : " + directory.getProjectParent().getKey().toString());
//			directory = pm.makePersistent(directory);
//		} finally  {
//			pm.close();
//		}
//		return directory.getKey();
//	}
	
	
	public Directory getDirectory(Long id) {
		PersistenceManager pm = ServerHelper.getPM();
		Directory result;
		try {
			result = pm.getObjectById(Directory.class, id);
			result = pm.detachCopy(result);
		} finally {
			pm.close();
		}

		return result;
	}

	/**
	 * Delete the given Directory.
	 * The directory to delete must be owned by the logged user.
 	 * @param directory The directory to delete
	 */
	public void deleteDirectory(Directory directory) {
		PersistenceManager pm = ServerHelper.getPM();
		pm.currentTransaction().begin();
		try {
			Directory directoryFound = pm.getObjectById(Directory.class, directory.getKey());
			pm.deletePersistent(directoryFound);
			
			pm.currentTransaction().commit();
		} finally {
			 if (pm.currentTransaction().isActive()) {
				 pm.currentTransaction().rollback();
	         }
		}
	}
}
