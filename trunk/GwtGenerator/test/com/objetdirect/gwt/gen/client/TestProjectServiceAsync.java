package com.objetdirect.gwt.gen.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.client.services.ProjectServiceAsync;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

public class TestProjectServiceAsync extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.objetdirect.gwt.gen.GwtGeneratorTestSuite";
	}

	public void testCreateProject() throws Exception {
		final ProjectServiceAsync projectService = GWT.create(ProjectService.class);
		
		delayTestFinish(1500);
		
		projectService.createProject("name", new AsyncCallback<Long>() {
			
			@Override
			public void onSuccess(Long result) {
				finishTest();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail();
			}
		});
	}
	
	
	public void testGetProject() throws Exception {
		final ProjectServiceAsync projectService = GWT.create(ProjectService.class);
		
		delayTestFinish(1500);
		
		projectService.createProject("name", new AsyncCallback<Long>() {
			
			@Override
			public void onSuccess(final Long key) {
				projectService.getProjects(new AsyncCallback<List<Project>>() {
					
					@Override
					public void onSuccess(List<Project> projectsFound) {
						assertEquals(key, projectsFound.get(0));
						finishTest();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						fail();
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail();
			}
		});
	}
}
