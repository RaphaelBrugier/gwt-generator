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
package com.objetdirect.gwt.gen.client.ui.explorer.projectList;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent;
import com.objetdirect.gwt.gen.client.event.DisplayDiagramsEvent;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.client.services.ProjectServiceAsync;
import com.objetdirect.gwt.gen.client.ui.explorer.projectList.DirectoryListView.CreateDiagramPopup;
import com.objetdirect.gwt.gen.client.ui.explorer.projectList.DirectoryListView.CreateDirectoryPopup;
import com.objetdirect.gwt.gen.client.ui.explorer.projectList.DirectoryListView.CreateProjectPopup;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorPopUp;
import com.objetdirect.gwt.gen.client.ui.popup.MessagePopUp;
import com.objetdirect.gwt.gen.client.ui.resources.TreeProjectsResources;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * Presenter for the explorer list of projects and directories on the explorer page.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class DirectoryListPresenter {
	
	public interface Display {
		/**
		 * @return a button for creating a new project 
		 */
		HasClickHandlers getCreateProjectButton();
		
		/**
		 * @return The main container
		 */
		HasWidgets getContainer();
		
		/**
		 * @return A widget to display when loading project and directories.
		 */
		Widget getLoadingWidget();
		
		/**
		 * @return A widget to display a message when the use has no project
		 */
		Widget getNoProjectWidget();
		
		/**
		 * @return The view as a widget.
		 */
		Widget asWidget();
	}


	private final ProjectServiceAsync projectService = GWT.create(ProjectService.class);
	
	private final HandlerManager eventBus;
	
	private final Tree tree;
	
	private final Display display;
	
	/**
	 * We need to save the previous selected item to get a workaround for this bug :
	 * http://code.google.com/p/google-web-toolkit/issues/detail?id=3660
	 */
	private TreeItem previousSelectedItem;
	int comingFromSetState = 0;
	boolean prevOpenState = true;
	
	public DirectoryListPresenter(HandlerManager eventBus, Display display) {
		
		this.eventBus = eventBus;
		this.display = display;
		
		this.tree = new Tree(TreeProjectsResources.INSTANCE, true);
		display.getContainer().add(tree);
		bindCreateProjectButton();
		addTreeHandlers();
		doFectchProjects();
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	
	/** Add the handler on the items of the tree.*/
	private void addTreeHandlers() {
		
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				
				// Dirty workaround for this bug 
				//http://code.google.com/p/google-web-toolkit/issues/detail?id=3660
				//TODO find a better workaround
				TreeItem item = event.getSelectedItem();

				if (! item.equals(previousSelectedItem )) {
					item.setState(!item.getState());
					previousSelectedItem = item;
					prevOpenState = !item.getState();
				} else {
					if (comingFromSetState == 1 && prevOpenState) {
						comingFromSetState++;
					}
					if (comingFromSetState != 2) {
						comingFromSetState++;
						item.setState(!item.getState());
						prevOpenState = !item.getState();
					} else {
						comingFromSetState = 0;
						prevOpenState = true;
					}
				}
			}
		});
	}
	
	
	void bindCreateProjectButton() {
		display.getCreateProjectButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				final CreateProjectPopup createProjectPopUp = new CreateProjectPopup();
				createProjectPopUp.getCreateButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent e) {
						doCreateProject(createProjectPopUp, createProjectPopUp.getProjectName());
					}
				});
				createProjectPopUp.show();
				
			}
		});
	}


	/**
	 * Create the items on the tree for the project.
	 * @param project
	 * @return The item representing the project with its directory children.
	 */
	private TreeItem createProjectItem(final Project project) {
		ProjectTreeItem projectItem = new ProjectTreeItem(project);
		bindProjectTreeItem(projectItem);
		
		for (Directory directory : project.getDirectories()) {
			DirectoryTreeItem directoryTreeItem = new DirectoryTreeItem(directory);
			projectItem.addItem(directoryTreeItem);
			bindDirectoryItem(project, directoryTreeItem);
		}
		
		return projectItem;
	}

	/**
	 * Bind a projectTreeItem buttons with the actions.
	 * Actions are :
	 *  - add a directory
	 *  - delete the project
	 * @param projectItem The project tree item where the actions are added.
	 */
	private void bindProjectTreeItem(final ProjectTreeItem projectItem) {
		// Bind the add directory button
		projectItem.getAddDirectoryButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final CreateDirectoryPopup createDirectoryPopup = new CreateDirectoryPopup();
				
				createDirectoryPopup.getCreateButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						doCreateDirectory(createDirectoryPopup, projectItem.getProject(), createDirectoryPopup.getDirectoryName());
					}
				});
				
				createDirectoryPopup.show();
			}
		});

		// Bind the delete button
		projectItem.getDeleteProjectButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Display a popup to ask to delete or not the project ?
				doDeleteProject(projectItem.getProject());
			}
		});
	}

	/**
	 * Bind the directoryTreeItem buttons with the actions :
	 *  - display the diagrams of the directory when the a click occurs on the directory icon or the directory name
	 *  - create a new diagram
	 *  - delete the directory 
	 * @param directoryTreeItem The directory tree item where the actions are added.
	 */
	private void bindDirectoryItem(final Project project, final DirectoryTreeItem directoryTreeItem) {

		// Bind the directory icon and directory name
		final ClickHandler diplayDiagramsClickHandlers = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doDisplayDiagrams(directoryTreeItem.getDirectory());
			}
		};
		directoryTreeItem.getDirectoryIcon().addClickHandler(diplayDiagramsClickHandlers);
		directoryTreeItem.getDirectoryName().addClickHandler(diplayDiagramsClickHandlers);
		
		// Bind the add diagram button
		directoryTreeItem.getAddDiagramButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final CreateDiagramPopup createDiagramPopup = new CreateDiagramPopup();
				createDiagramPopup.getCreateButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String directoryKey = directoryTreeItem.getDirectory().getKey();
						doCreateDiagram(createDiagramPopup, directoryKey, createDiagramPopup.getDiagramName(), createDiagramPopup.getDiagramType());
					}
				});
				
				createDiagramPopup.show();
			}
		});

		// Bind the delete directory button.
		directoryTreeItem.getDeleteDirectoryButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// Display a popup to ask to delete or not the directory ?
				doDeleteDirectory(project, directoryTreeItem.getDirectory());
			}
		});
	}	


	/**
	 * Create a project.
	 * When the action is finish (with success or not), close the popup and display a message.
	 * @param createProjectPopup The popup to hide when the action of creation is finished.
	 * @param projectName The name of the project to create.
	 */
	private void doCreateProject(final CreateProjectPopup createProjectPopup, final String projectName) {
		projectService.createProject(projectName, new AsyncCallback<Long>() {
			
			@Override
			public void onSuccess(Long result) {
				doFectchProjects();
				createProjectPopup.hide();
				MessagePopUp messagePopUp = new MessagePopUp("Project " + projectName + " created with success.");
				messagePopUp.show();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				createProjectPopup.hide();
				Log.error("Failed to create the project " + projectName);
				ErrorPopUp errorPopUp = new ErrorPopUp("Failed to create the project " + projectName);
				errorPopUp.show();
			}
		});
	}
	
	/**
	 * Delete a project.
	 * When the project is deleted with success, display a message and reload the tree of projects.
	 * @param projectToDelete The project to delete
	 */
	private void doDeleteProject(final Project projectToDelete) {
		projectService.deleteProject(projectToDelete, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				doFectchProjects();
				MessagePopUp messagePopUp = new MessagePopUp("Project " + projectToDelete.getName() + " deleted with success");
				messagePopUp.show();
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to delete the project " + projectToDelete);
				ErrorPopUp errorPopUp = new ErrorPopUp("Failed to delete the project");
				errorPopUp.show();
			}
		});
	}
	
	
	/**
	 * Create a directory in the given project.
	 * When the directory is created with success, display a message and reload the tree of projects.
	 * @param createDirectoryPopup The popup to hide when the action is finished.
	 * @param project The project where the directory will be created
	 * @param directoryName The name of the directory to create.
	 */
	private void doCreateDirectory(final CreateDirectoryPopup createDirectoryPopup, Project project, final String directoryName) {
		projectService.addDirectory(project, directoryName, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				doFectchProjects();
				createDirectoryPopup.hide();
				MessagePopUp messagePopUp = new MessagePopUp("Directory " + directoryName + " created with success.");
				messagePopUp.show();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				createDirectoryPopup.hide();
				Log.error("Failed to create the directory " + directoryName);
				ErrorPopUp errorPopUp = new ErrorPopUp("Failed to create the directory");
				errorPopUp.show();
			}
		});
	}

	
	/**
	 * Delete a directory
	 * When the directory is deleted with success, display a message and reload the tree of projects.
	 * @param directory The directory to delete.
	 */
	private void doDeleteDirectory(final Project project, final Directory directory) {

		projectService.deleteDirectory(project, directory, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				doFectchProjects();
				MessagePopUp messagePopUp = new MessagePopUp("Directory " + directory.getName() + " deleted with success.");
				messagePopUp.show();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to delete the directory " + directory.getName());
				ErrorPopUp errorPopUp = new ErrorPopUp("Failed to delete the directory " + directory.getName());
				errorPopUp.show();
			}
		});
	}
	
	/**
	 * Create a diagram in the given directory
	 * When the diagram is created, load the designer with it.
	 * Else display an error message.
	 * @param createDiagramPopup the Popup to hide when the action is finished.
	 * @param diagramName The diagram name.
	 * @param diagramType The diagram type.
	 */
	private void doCreateDiagram(CreateDiagramPopup createDiagramPopup, String directoryKey, String diagramName, String diagramType) {
		Type type = Type.valueOf(diagramType.toUpperCase());
		DiagramDto diagramInformations = new DiagramDto(directoryKey, diagramName, type);
		createDiagramPopup.hide();
		eventBus.fireEvent(new CreateDiagramEvent(diagramInformations));
	}


	/** Fetch the tree with the projects and directories of the user.
	 * If the user has no project, display a message.
	 */
	private void doFectchProjects() {
		display.getContainer().clear();
		display.getContainer().add(display.getLoadingWidget());
		
		tree.removeItems();
		projectService.getProjects(new AsyncCallback<Collection<Project>>() {
			
			@Override
			public void onSuccess(Collection<Project> projectsFound) {
				if (projectsFound.size() == 0) {
					display.getContainer().clear();
					display.getContainer().add(display.getNoProjectWidget());
				} else {
					for (Project project : projectsFound) {
						tree.addItem(createProjectItem(project));
					}
					display.getContainer().clear();
					display.getContainer().add(tree);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error while fetching the projects : " + caught.getMessage());
			}
		});
	}


	/**
	 * Request the display of the list of diagrams contains in the selected directory
	 * @param directory The selected directory
	 */
	private void doDisplayDiagrams(Directory directory) {
		eventBus.fireEvent(new DisplayDiagramsEvent(directory));
	}
}
