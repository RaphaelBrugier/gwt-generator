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
package com.objetdirect.gwt.gen.client.helpers;

import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.CLASS;

import com.objetdirect.gwt.umlapi.client.artifacts.clazz.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.engine.Point;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * This class is responsible to construct the class diagram of the classes supported by the seam generator.
 * 
 * Usage : SeamDiagramBuilder.getSeamDiagram();
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class SeamDiagramBuilder {

	private static SeamDiagramBuilder instance;
	
	public static final String SEAM_DIAGRAM_NAME = "seamClasses";

	static SeamDiagramBuilder getInstance() {
		if (instance == null) {
			instance = new SeamDiagramBuilder();
		}

		return instance;
	}
	
	public static UMLCanvas getSeamDiagram() {
		return getInstance().getSeamClassDiagram();
	}
	
	SeamDiagramBuilder() {
	}
	
	private UMLCanvas getSeamClassDiagram() {
		UMLCanvas canvasClassDiagram = UMLCanvas.createUmlCanvas(CLASS);
		
		final ClassArtifact classPerso = new ClassArtifact(canvasClassDiagram, 0, "classe perso");
		classPerso.setLocation(new Point(500, 85));
		canvasClassDiagram.add(classPerso);
		
		return canvasClassDiagram;
	}
}
