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

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.server.entities.SeamDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * DAO for the seam diagram entity.
 *
 * The seam diagram should normally be a single entity.
 *
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class SeamDiagramDao {
	
	public interface Action {
		void run(PersistenceManager pm);
	}
	
	public void execute(Action a) {
		PersistenceManager pm = ServerHelper.getPM();
		try {
			a.run(pm);
		} finally  {
			pm.close();
		}
	}

	
	public SeamDiagram createSeamDiagram() {
		final SeamDiagram persistedDiagram[] = new SeamDiagram[1];
		persistedDiagram[0] = new SeamDiagram(null);
		
		execute(new Action() {
			@Override
			public void run(PersistenceManager pm) {
				persistedDiagram[0] = pm.makePersistent(persistedDiagram[0]);
			}
		});
		
		return persistedDiagram[0];
	}

	
	@SuppressWarnings("unchecked")
	public SeamDiagram getSeamDiagram() {
		final SeamDiagram diagram[] = new SeamDiagram[1];
		diagram[0] = null;

		execute(new Action() {
			@Override
			public void run(PersistenceManager pm) {
				Query query = pm.newQuery(SeamDiagram.class);
				List<SeamDiagram> seamDiagrams = (List<SeamDiagram>) query.execute();
				
				if (seamDiagrams != null && seamDiagrams.size() != 0) {
					diagram[0] = seamDiagrams.get(0);
				}
			}
		});
		return diagram[0];
	}

	
	@SuppressWarnings("unchecked")
	public void updateCanvasInSeamDiagram(final UMLCanvas seamCanvas) {
		execute(new Action() {
			@Override
			public void run(PersistenceManager pm) {
				Query query = pm.newQuery(SeamDiagram.class);
				List<SeamDiagram> seamDiagrams = (List<SeamDiagram>) query.execute();
				
				if (seamDiagrams != null && seamDiagrams.size() != 0) {
					SeamDiagram seamDiagram = seamDiagrams.get(0);
					seamDiagram.setCanvas(seamCanvas);
				}
			}
		});
	}

	
	@SuppressWarnings("unchecked")
	public void deleteSeamDiagram() {
		execute(new Action() {
			@Override
			public void run(PersistenceManager pm) {
				SeamDiagram seamDiagram;
				Query query = pm.newQuery(SeamDiagram.class);
				List<SeamDiagram> seamDiagrams = (List<SeamDiagram>) query.execute();
				
				if (seamDiagrams != null && seamDiagrams.size() != 0) {
					seamDiagram = seamDiagrams.get(0);
					pm.deletePersistent(seamDiagram);
				}
			}
		});
	}
}
