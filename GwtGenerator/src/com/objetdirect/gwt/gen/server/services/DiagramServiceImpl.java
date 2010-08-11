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

import static com.objetdirect.gwt.gen.server.ServerHelper.checkLoggedIn;
import static com.objetdirect.gwt.gen.server.ServerHelper.getCurrentUser;
import static com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper.isNotBlank;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.OBJECT;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.server.dao.DiagramDao;
import com.objetdirect.gwt.gen.server.entities.Diagram;
import com.objetdirect.gwt.gen.server.helpers.ObjectDiagramBuilder;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.exceptions.CreateDiagramException;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;

/**
 * Real implementation of DiagramService.
 * @see com.objetdirect.gwt.gen.client.services.DiagramService
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
@SuppressWarnings("serial")
public class DiagramServiceImpl extends RemoteServiceServlet implements DiagramService {
	
	private DiagramDao diagramDao = new DiagramDao();
	
	/**
	 * @param diagramDao the diagramDao to set
	 */
	public void setDiagramDao(DiagramDao diagramDao) {
		this.diagramDao = diagramDao;
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#createDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramDto)
	 */
	@Override
	public String createDiagram(DiagramDto diagramDto) throws CreateDiagramException {
		checkLoggedIn();
		if (! isNotBlank(diagramDto.getName())) {
			throw new CreateDiagramException("You must specify a name for your diagram.");
		}
		
		if (diagramDto.isSeamDiagram()) {
			if (! UserServiceFactory.getUserService().isUserAdmin()) {
				throw new CreateDiagramException("Only administrators can create a new diagram in the Seam project.");
			}
		}
		
		if (diagramDao.getDiagram(diagramDto.getType(), diagramDto.getName(), diagramDto.getDirectoryKey()) != null) {
			throw new CreateDiagramException("A diagram of this type and with this name already exist. Please use an other name.");
		}
		
		Diagram newDiagram = new Diagram(diagramDto.getDirectoryKey(), diagramDto.getType(), diagramDto.getName(), getCurrentUser());
		if (diagramDto.getCanvas()!= null) {
			newDiagram.setCanvas(diagramDto.getCanvas());
		}
		newDiagram.setClassDiagramKey(diagramDto.classDiagramKey);
		newDiagram.setSeamDiagram(diagramDto.isSeamDiagram());
		
		return diagramDao.createDiagram(newDiagram);
	}
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#deleteDiagram(java.lang.String)
	 */
	@Override
	public void deleteDiagram(String key) {
		checkLoggedIn();
		diagramDao.deleteDiagram(key);
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagram(java.lang.String, boolean)
	 */
	@Override
	public DiagramDto getDiagram(String key) {
		checkLoggedIn();
		
		Diagram diagram = diagramDao.getDiagram(key);
		
		DiagramDto diagramFound = diagram.copyToDiagramDto();
		
		setSeamDiagramNonEditableForNonAdmin(diagram, diagramFound);
		
		if (diagramFound.getType()==OBJECT) {
			ObjectDiagramBuilder objectDiagramBuilder = new ObjectDiagramBuilder(diagramFound, diagramDao);
			diagramFound = objectDiagramBuilder.getObjectDiagramTransformed();
		}
		
		return diagramFound;
	}

	private void setSeamDiagramNonEditableForNonAdmin(Diagram diagram, DiagramDto diagramFound) {
		if (diagram.isSeamDiagram()) {
			if (! UserServiceFactory.getUserService().isUserAdmin()) {
				diagramFound.setEditable(false);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#saveDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramInformations)
	 */
	@Override
	public void saveDiagram(DiagramDto diagramToSave) {
		checkLoggedIn();
		if (diagramToSave.isSeamDiagram()) {
			checkUserLoggedIsAdmin();
		}
		diagramDao.saveDiagram(diagramToSave);
	}

	private void checkUserLoggedIsAdmin() {
		if (! UserServiceFactory.getUserService().isUserAdmin()) {
			throw new GWTGeneratorException("You are not allowed to edit this diagram.");
		}
	}
}
