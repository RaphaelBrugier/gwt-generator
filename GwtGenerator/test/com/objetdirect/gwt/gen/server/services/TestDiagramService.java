/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassRelationLinkArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.LinkArtifact;
import com.objetdirect.gwt.umlapi.client.engine.GeometryManager;
import com.objetdirect.gwt.umlapi.client.engine.Point;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.helpers.HotKeyManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager.Theme;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram.Type;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLLink.LinkKind;


public class TestDiagramService extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.objetdirect.gwt.gen.GwtGeneratorTestSuite";
	}
	
	/** Setup the gfx platform. */
	private void setUpPlatform() {
		OptionsManager.initialize();
		HotKeyManager.forceStaticInit();
		OptionsManager.set("DiagramType", 0);
		ThemeManager.setCurrentTheme((Theme.getThemeFromIndex(OptionsManager.get("Theme"))));
		GfxManager.setPlatform(OptionsManager.get("GraphicEngine"));
		GeometryManager.setPlatform(OptionsManager.get("GeometryStyle"));
	}
	
	public void testSerializeUmlCanvas() {
		final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
		setUpPlatform();
		
		UMLCanvas umlCanvas = new UMLCanvas(new UMLDiagram(Type.CLASS));

		final ClassArtifact class1 = new ClassArtifact(umlCanvas, "Class1");
		class1.setLocation(new Point( 2, 2));
		umlCanvas.add(class1);
		
		final ClassArtifact class2 = new ClassArtifact(umlCanvas, "Class2");
		class2.setLocation(new Point( 20, 20));
		umlCanvas.add(class2);
		
		final LinkArtifact classesRelation = new ClassRelationLinkArtifact(umlCanvas, class1, class2, LinkKind.AGGREGATION_RELATION);
		
		umlCanvas.add(classesRelation);
		
		DiagramInformations diagram = new  DiagramInformations();
		diagram.setCanvas(umlCanvas);
		delayTestFinish(3000);

		diagramService.saveDiagram(diagram, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
//				System.out.println("service called with success");
				finishTest();
			}
			
			@Override
			public void onFailure(Throwable caught) {
//				System.out.println("service failed");
				caught.printStackTrace();
			}
		});
	}
}
