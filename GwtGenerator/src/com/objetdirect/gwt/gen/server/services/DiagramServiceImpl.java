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

import java.util.ArrayList;
import java.util.Collection;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.server.dao.DiagramDao;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;
import com.objetdirect.gwt.gen.shared.exceptions.DiagramAlreadyExistException;
import com.objetdirect.gwt.gen.shared.exceptions.NotLoggedInException;

/**
 * Real implementation of DiagramService.
 * @see com.objetdirect.gwt.gen.client.services.DiagramService
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
@SuppressWarnings("serial")
public class DiagramServiceImpl extends RemoteServiceServlet implements DiagramService {

	
	private DiagramDao diagramDao = new DiagramDao();
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#createDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramInformations.Type, java.lang.String)
	 */
	@Override
	public Long createDiagram(Type type, String name) throws DiagramAlreadyExistException {
		checkLoggedIn();
		if (diagramDao.getDiagram(type, name) != null) {
			throw new DiagramAlreadyExistException();
		}
		return diagramDao.createDiagram(type, name);
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagrams()
	 */
	@Override
	public ArrayList<DiagramDto> getDiagrams() {
		checkLoggedIn();
		return diagramDao.getDiagrams();
	}
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#deleteDiagram(java.lang.Long)
	 */
	@Override
	public void deleteDiagram(Long key) {
		checkLoggedIn();
		diagramDao.deleteDiagram(key);
	}
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagram(java.lang.Long)
	 */
	@Override
	public DiagramDto getDiagram(Long key) {
		checkLoggedIn();
		return diagramDao.getDiagram(key);
	}
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#saveDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramInformations)
	 */
	@Override
	public void saveDiagram(DiagramDto diagramToSave) {
		checkLoggedIn();
		diagramDao.saveDiagram(diagramToSave);
	}
	
	/** Get the current logged user on GAE or null if the user is not logged.
	 * @return the logged User object.
	 */
	private static User getCurrentUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}
	
	/** Check if the user is logged in.
	 *  Throw a NotLoggedInException if not.
	 */
	private void checkLoggedIn()  {
		    if (getCurrentUser() == null) {
		      throw new NotLoggedInException("Not logged in.");
		    }
	 }
}
