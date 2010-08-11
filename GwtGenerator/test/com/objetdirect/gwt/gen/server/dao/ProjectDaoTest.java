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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ProjectDaoTest {

	private static final String PROJECT_NAME = "projectname";

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig());
	
	private static final String EMAIL_TESTVALUE = "test@gmail.com";
	ProjectDao projectDao;
	
	@Before
	public void setUp() throws Exception {
		helper.setUp();
		helper.setEnvIsLoggedIn(true)
		  .setEnvEmail("MyEmail@gmail.com")
	      .setEnvAuthDomain("google.com");
		projectDao = new ProjectDao();
	}

	@Test
	public void createOneProject_FromNameAndEmail_Success() {
		projectDao.createProject(PROJECT_NAME, EMAIL_TESTVALUE);
		
		List<Project> projects = projectDao.getProjects(EMAIL_TESTVALUE);
		
		assertEquals("expected to find one project ", 1, projects.size());
	}
	
	@Test
	public void createOneProject_Success() {
		Project p = new Project(PROJECT_NAME, EMAIL_TESTVALUE);
		projectDao.createProject(p);
		
		List<Project> projects = projectDao.getProjects(EMAIL_TESTVALUE);
		
		assertEquals("expected to find one project ", 1, projects.size());
	}
	
	@Test
	public void createAdminProject_Success() {
		Project p = new Project(PROJECT_NAME, null);
		p.setSeamProject(true);
		projectDao.createProject(p);
		
		Project projectSeam = projectDao.getSeamProject();
		
		assertNotNull(projectSeam);
	}
	
	@Test
	public void createOneUserProject_and_AdminProject_returnJustUserProject_succes() {
		projectDao.createProject(PROJECT_NAME, EMAIL_TESTVALUE);
		
		List<Project> projects = projectDao.getProjects(EMAIL_TESTVALUE);
		
		assertEquals("expected to find one project ", 1, projects.size());
	}
}
