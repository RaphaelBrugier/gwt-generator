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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;


/**
 * Async counter part of DiagramService
 * @see com.objetdirect.gwt.gen.client.services.DiagramService
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface DiagramServiceAsync {

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#createDiagram(java.lang.String, com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type, java.lang.String)
	 */
	void createDiagram(String directoryKey, Type type, String name, AsyncCallback<Long> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagrams(java.lang.String)
	 */
	void getDiagrams(String directoryKey, AsyncCallback<ArrayList<DiagramDto>> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#deleteDiagram(java.lang.Long)
	 */
	void deleteDiagram(Long key, AsyncCallback<Void> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagram(java.lang.Long)
	 */
	void getDiagram(Long key, AsyncCallback<DiagramDto> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#saveDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramInformations)
	 */
	void saveDiagram(DiagramDto diagram, AsyncCallback<Void> callback);
}
