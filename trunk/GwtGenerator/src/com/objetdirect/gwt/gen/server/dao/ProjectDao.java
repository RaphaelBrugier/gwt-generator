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

import static com.objetdirect.gwt.gen.server.ServerHelper.getCurrentUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.server.entities.Diagram;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;

/**
 * Data access object for the project entities.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class ProjectDao {
	
	public interface Action {
		void run(PersistenceManager pm);
	}
	
	public void execute(Action a) {
		PersistenceManager pm = ServerHelper.getPM();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			a.run(pm);
			tx.commit();
		} finally  {
			if (tx.isActive()) {
                tx.rollback();
            }
			pm.close();
		}
	}
	
	/**
	 * @param name
	 * @param email
	 * @return
	 */
	public Project createProject(String name, String email) {
		
		final Project persistedProject[] = new Project[1];
		persistedProject[0] = new Project(name, email);

		execute(new Action(){

			@Override
			public void run(PersistenceManager pm) {
				persistedProject[0] = pm.makePersistent(persistedProject[0]);
			}
		});
		return persistedProject[0];
	}
	
	public Project createProject(Project project) {
		
		final Project persistedProject[] = new Project[1];
		persistedProject[0] = project;

		execute(new Action(){

			@Override
			public void run(PersistenceManager pm) {
				persistedProject[0] = pm.makePersistent(persistedProject[0]);
			}
		});
		return persistedProject[0];
	}
	
	public Project getProjectById(Long id){
		Project projectFound = null;
		
		PersistenceManager pm = ServerHelper.getPM();
		pm.getFetchPlan().addGroup("directory");
		try {
			projectFound = pm.getObjectById(Project.class, id);
		} finally {
			pm.close();
		}
		
		return projectFound;
	}
	
	@SuppressWarnings("unchecked")
	public Project getSeamProject() {
		PersistenceManager pm = ServerHelper.getPM();
		pm.getFetchPlan().addGroup("directory");
		
		Project projectFound = null;
		List<Project> projectsFound;
		try {
			Query q = pm.newQuery(Project.class, "seamProject == b");
		    q.declareParameters("boolean b");
		    projectsFound = (List<Project>)q.execute(true);
		    if (projectsFound.size() == 1) {
		    	projectFound = (Project) pm.detachCopyAll(projectsFound).toArray()[0];
		    }
		} finally {
			pm.close();
		}
		
		return projectFound;
	}

	/**
	 * Get all the projects of the logged user
	 * 
	 * @return a list of project.
	 */
	@SuppressWarnings("unchecked")
	public List<Project> getProjects(String email) {
		PersistenceManager pm = ServerHelper.getPM();
		
		// Adding the "directory" group will force the detach of the "directories" field of the Project class
		// The directory group is configure in the top of the project class.
		// See this post for more informations about detaching children :
		// http://groups.google.com/group/google-appengine-java/browse_thread/thread/4a55b8ec08343229/8dace46252053dd8?show_docid=8dace46252053dd8&fwc=1
		pm.getFetchPlan().addGroup("directory"); 
		
		List<Project> queryResult;
		Collection<Project> projectsFound = null;
		try {
			Query q = pm.newQuery(Project.class, "email == e");
		    q.declareParameters("String e");
		    queryResult = (List<Project>) q.execute(email);
		    projectsFound = pm.detachCopyAll(queryResult);
		} finally {
			pm.close();
		}

		return new ArrayList<Project>(projectsFound);
	}
	
	/**
	 * Update a project in the base;
	 * @param project
	 */
	public Project updateProject(Project project) {
		PersistenceManager pm = ServerHelper.getPM();
		pm.getFetchPlan().addGroup("directory");
		Project persistedProject = null;
		try {
			persistedProject = pm.makePersistent(project);
		} finally {
			pm.close();
		}
		
		return persistedProject;
	}
	
	
	/**
	 * Delete the given project.
	 * The project to delete must be owned by the logged user.
 	 * @param project The project to delete
	 */
	public void deleteProject(final Project project) {
	execute(new Action() { @SuppressWarnings("unchecked")
	public void run(PersistenceManager pm) {
		
		List<Diagram> queryResult;
		Project projectFound = pm.getObjectById(Project.class, project.getKey());

		if (projectFound == null) 
			throw new GWTGeneratorException("The Project to delete was not found.");
		
		// Delete all the diagrams in all the directories of the project to delete before deleting the project.
		for (Directory directory : projectFound.getDirectories()) {
			Query q = pm.newQuery(Diagram.class, "user == u && directoryKey == d");
		    q.declareParameters("com.google.appengine.api.users.User u, " + "String d");
		    queryResult = (List<Diagram>) q.execute(getCurrentUser(), directory.getKey());
	    	pm.deletePersistentAll(queryResult);
		}

		pm.deletePersistent(projectFound);
		
	}});}
}
