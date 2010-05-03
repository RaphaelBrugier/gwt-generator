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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.objetdirect.gwt.gen.server.entities.Diagram;
import com.objetdirect.gwt.gen.server.services.PMF;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;

/**
 * DAO for the diagram entities.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DiagramDao {
	
	/**
	 * Create a diagram and return its generated id.
	 * @param type the type of the diagram
	 * @param name the diagram name.
	 * @return the generated id for the diagram.
	 */
	public Long createDiagram(Type type, String name) {
		PersistenceManager pm = PMF.getPM();
		Diagram persistedDiagram = new Diagram(type, name, getCurrentUser());
		try {
			persistedDiagram = pm.makePersistent(persistedDiagram);
		} finally  {
			pm.close();
		}
		return persistedDiagram.getKey();
	}
	
	/**
	 * Get a diagram for the logged user from its key.
	 * @param key
	 * @return
	 */
	public DiagramDto getDiagram(Long key) {
		PersistenceManager pm = PMF.getPM();
		DiagramDto diagramFound = null;
		try {
			Diagram diagram = pm.getObjectById(Diagram.class, key);
			if(diagram!=null) {
				if (! diagram.getUser().equals(getCurrentUser())) {
					throw new GWTGeneratorException("Get a diagram not owned by the user logged.");
				}
				diagramFound = diagram.copyToDiagramDto();
			}
		} finally {
			pm.close();
		}
		
		return diagramFound;
	}
	
	@SuppressWarnings("unchecked")
	public DiagramDto getDiagram(Type type, String name) {
		PersistenceManager pm = PMF.getPM();
		DiagramDto diagramFound = null;
		List<Diagram> queryResult;
		try {
			Query q = pm.newQuery(Diagram.class, "user == u && type == t && name == n");
			q.declareParameters(
					"com.google.appengine.api.users.User u, " +
					"String t, "+
					"String n");
			queryResult = (List<Diagram>) q.execute(getCurrentUser(), type, name);

			if (queryResult.size() == 1) {
				diagramFound = queryResult.get(0).copyToDiagramDto();
			}
		} finally {
			pm.close();
		}

		return diagramFound;
	}
	
	/**
	 * Return the diagrams of the logged user.
	 * Please note that returned dto will NOT contained the serialized umlCanvas.
	 * Use getDiagram(Long key) to get a dto with the serialized field.
	 * @return a collection of all the user's diagrams.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DiagramDto> getDiagrams() {
		PersistenceManager pm = PMF.getPM();
		List<Diagram> queryResult;
		ArrayList<DiagramDto> results = new ArrayList<DiagramDto>();
		try {
			Query q = pm.newQuery(Diagram.class, "user == u");
		    q.declareParameters("com.google.appengine.api.users.User u");
		    queryResult = (List<Diagram>) q.execute(getCurrentUser());
		      
		    for (Diagram diagram : queryResult) {
		    	DiagramDto diagramInformation = new DiagramDto(diagram.getKey(), diagram.getName(), diagram.getType());
		    	results.add(diagramInformation);
		    } //TODO move this after the pm.close (if possible)
		} finally {
			pm.close();
		}

		return results;
	}
	
	/**
	 * Delete a diagram from its key
	 * @param key the id of the diagram.
	 */
	public void deleteDiagram(Long key) {
		PersistenceManager pm = PMF.getPM();
		try {
			Diagram diagram = pm.getObjectById(Diagram.class, key);
			if (! diagram.getUser().equals(getCurrentUser())) {
				throw new GWTGeneratorException("Trying to delete a diagram not owned by the user logged.");
			}
			if (diagram == null) 
				throw new GWTGeneratorException("The diagram to delete was not found.");
			
			pm.deletePersistent(diagram);
		} finally {
			pm.close();
		}
	}
	
	
	/**
	 * Save a diagram from a dto.
	 * @param diagramToSave dto to copy in base.
	 */
	public void saveDiagram(DiagramDto diagramToSave) {
		PersistenceManager pm = PMF.getPM();
		try {
			Diagram diagram = pm.getObjectById(Diagram.class, diagramToSave.getKey());
			if (diagram == null) 
				throw new GWTGeneratorException("The diagram to save was not found.");
			if (! diagram.getUser().equals(getCurrentUser())) {
				throw new GWTGeneratorException("Trying to save a diagram not owned by the user logged.");
			}
			
			diagram.copyFromDiagramDto(diagramToSave);
			
		} finally {
			pm.close();
		}
	}
	
	/** Get the current logged user on GAE or null if the user is not logged.
	 * @return the logged User object.
	 */
	private static User getCurrentUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}
}
