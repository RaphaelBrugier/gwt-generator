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

import com.google.gwt.junit.client.GWTTestCase;
import com.objetdirect.gwt.umlapi.client.engine.GeometryManager;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager.Theme;


public class TestUmlCanvasSerialization extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.objetdirect.gwt.gen.GwtGeneratorTestSuite";
	}
	
	/** Setup the gfx platform. */
	private void setUpPlatform() {
		OptionsManager.initialize();
		OptionsManager.set("DiagramType", 0);
		ThemeManager.setCurrentTheme((Theme.getThemeFromIndex(OptionsManager.get("Theme"))));
		GfxManager.setPlatform(OptionsManager.get("GraphicEngine"));
		GeometryManager.setPlatform(OptionsManager.get("GeometryStyle"));
	}
	
	@Deprecated
	public void testSerializeUmlCanvas() {
//		final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
//		setUpPlatform();
//		
//		UMLCanvasClassDiagram umlCanvas = (UMLCanvasClassDiagram)UMLCanvas.createUmlCanvas(CLASS);
//
//		final ClassArtifact class1 = new ClassArtifact(umlCanvas, 1, "Class1");
//		class1.setLocation(new Point( 2, 2));
//		umlCanvas.add(class1);
//		
//		final ClassArtifact class2 = new ClassArtifact(umlCanvas, 2, "Class2");
//		class2.setLocation(new Point( 20, 20));
//		umlCanvas.add(class2);
//		
//		final LinkArtifact classesRelation = new ClassRelationLinkArtifact(umlCanvas, 3, class1, class2, AGGREGATION_RELATION);
//		
//		umlCanvas.add(classesRelation);
//		
//		DiagramDto diagram = new  DiagramDto();
//		diagram.setCanvas(umlCanvas);
//		delayTestFinish(3000);
//
//		diagramService.saveDiagram(diagram, new AsyncCallback<Void>() {
//			@Override
//			public void onSuccess(Void result) {
////				System.out.println("service called with success");
//				finishTest();
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
////				System.out.println("service failed");
//				caught.printStackTrace();
//			}
//		});
	}
}
