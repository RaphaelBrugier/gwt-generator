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
package com.objetdirect.gwt.gen.server.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.server.entities.Diagram;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations.Type;
import com.objetdirect.gwt.gen.shared.exceptions.NotLoggedInException;

/**
 * Real implementation of DiagramService.
 * @see com.objetdirect.gwt.gen.client.services.DiagramService
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
@SuppressWarnings("serial")
public class DiagramServiceImpl extends RemoteServiceServlet implements DiagramService {

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#createDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramInformations.Type, java.lang.String)
	 */
	@Override
	public Long createDiagram(Type type, String name) {
		checkLoggedIn();
		PersistenceManager pm = PMF.getPM();
		
		Diagram persistedDiagram = new Diagram(type, name, getCurrentUser());
		try {
			persistedDiagram = pm.makePersistent(persistedDiagram);
		} finally  {
			pm.close();
		}
		return persistedDiagram.getKey();
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagrams()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<DiagramInformations> getDiagrams() {
		checkLoggedIn();
		
		PersistenceManager pm = PMF.getPM();
		List<Diagram> queryResult = new LinkedList<Diagram>();
		Collection<DiagramInformations> results = new LinkedList<DiagramInformations>();
		try {
			Query q = pm.newQuery(Diagram.class, "user == u");
		      q.declareParameters("com.google.appengine.api.users.User u");
		      queryResult = (List<Diagram>) q.execute(getCurrentUser());
		      
		      for (Diagram diagram : queryResult) {
		    	  DiagramInformations diagramInformation = new DiagramInformations(diagram.getKey(), diagram.getName(), diagram.getType());
		    	  results.add(diagramInformation);
		      } //TODO move this after the pm.close (if possible)
		} finally {
			pm.close();
		}
		
		return results;
	}
	
	
	/** Get the current logged user on GAE or null if the user is not logged.
	 * @return
	 */
	private static User getCurrentUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}
	
	/** Check if the user is logged in.
	 * Throw a NotLoggedInException if not.
	 */
	private void checkLoggedIn()  {
		    if (getCurrentUser() == null) {
		      throw new NotLoggedInException("Not logged in.");
		    }
	 }
}
