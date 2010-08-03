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
package com.objetdirect.gwt.gen.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.objetdirect.gwt.gen.shared.exceptions.NotLoggedInException;

/**
 * Helper class to access to GAE services.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public final class ServerHelper {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");
    
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
    
    /**
	 * Get the persistence manager used on GAE.
     * @return
     */
    public static PersistenceManager getPM() {
    	return ServerHelper.get().getPersistenceManager();
    }
        
    /** Get the current logged user on GAE or null if the user is not logged.
	 * @return the logged User object.
	 */
	public static User getCurrentUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}
	
	/** Check if the user is logged in.
	 *  Throw a NotLoggedInException if not.
	 */
	public static void checkLoggedIn()  {
		    if (getCurrentUser() == null) {
		      throw new NotLoggedInException("Not logged in.");
		    }
	 }
}