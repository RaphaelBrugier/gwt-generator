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

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.exceptions.CreateDiagramException;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;

/**
 * Service to operate on the stored diagrams.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@RemoteServiceRelativePath("diagram")
public interface DiagramService extends RemoteService {
	
	/**
	 * Create a new diagram
	 * @param diagramDto The dto containing all the informations about the diagram to create.
	 * @return the generated key
	 * @throws CreateDiagramException
	 */
	public String createDiagram(DiagramDto diagramDto) throws CreateDiagramException;
	
	/**
	 * Get all the diagrams of the logged user on a directory
	 *
	 * @param directory Directory where the diagrams are stored.
	 * @return A list of the diagrams.
	 */
	public ArrayList<DiagramDto> getDiagrams(String directory) throws GWTGeneratorException;
	
	/**
	 * Delete a diagram in the base.
	 * @param key the key of the diagram to delete
	 */
	public void deleteDiagram(String key) throws GWTGeneratorException;
	
	/**
	 * Get a Diagram from its key
	 * @param key the key
	 * @return the diagram found or null.
	 */
	public DiagramDto getDiagram(String key) throws GWTGeneratorException;
	
	/**
	 * Save the diagram in the base.
	 * @param diagram the diagram to save
	 */
	public void saveDiagram(DiagramDto diagram) throws GWTGeneratorException;
	
}
