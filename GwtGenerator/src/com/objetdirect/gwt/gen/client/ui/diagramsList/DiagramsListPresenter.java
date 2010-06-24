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
package com.objetdirect.gwt.gen.client.ui.diagramsList;

import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.CLASS;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.OBJECT;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.NewProjectEvent;
import com.objetdirect.gwt.gen.client.event.NewProjectEvent.NewProjectEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.ProjectServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorToaster;
import com.objetdirect.gwt.gen.client.ui.popup.MessageToaster;
import com.objetdirect.gwt.gen.client.ui.resources.TreeProjectsResources;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.entities.Directory.DirType;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * Presenter for the explorer list of projects and directories on the explorer page.
 * Presenter and its associated view are inspired by the mvp pattern.
 * @see http://code.google.com/intl/fr/webtoolkit/articles/mvp-architecture.html
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class DiagramsListPresenter {

	public interface Display {
		/**
		 * @return The main container
		 */
		HasWidgets getContainer();

		/**
		 * @return A widget to display when loading project and directories.
		 */
		Widget getLoadingProjectsWidget();

		/**
		 * @return A widget to display a message when the use has no project
		 */
		Widget getNoProjectWidget();

		/**
		 * Create a new diagramTreeItem with the given diagram name
		 * @param diagramName the name displayed in the tree item
		 * @return the created TreeItem;
		 */
		DiagramTreeItem createDiagramTreeItem(String diagramName);

		/**
		 * @return The view as a widget.
		 */
		Widget asWidget();
	}

	private final HandlerManager eventBus;

	private final Display display;

	private final DiagramServiceAsync diagramService;

	private final ProjectServiceAsync projectService;

	private final Tree tree;

	public DiagramsListPresenter(HandlerManager eventBus, Display display, DiagramServiceAsync diagramService, ProjectServiceAsync projectService) {
		this.eventBus = eventBus;
		this.display = display;
		this.diagramService = diagramService;
		this.projectService = projectService;

		// TODO tree and treeItems creation should be delegate to the view.
		tree = new Tree(TreeProjectsResources.INSTANCE, true);
		display.getContainer().add(tree);

		addHandlers();
		doFectchProjects();
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}


	void addHandlers() {

		eventBus.addHandler(NewProjectEvent.TYPE, new NewProjectEventHandler() {
			@Override
			public void onNewProjectEvent(NewProjectEvent event) {
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
			bindDirectoryItem(directoryTreeItem);
			projectItem.addItem(directoryTreeItem);
			for (DiagramDto diagram : directory.getDiagrams()) {
				DiagramTreeItem diagramTreeItem = display.createDiagramTreeItem(diagram.getName());
				bindDiagramTreeItem(diagram, diagramTreeItem);
				directoryTreeItem.addItem(diagramTreeItem);
			}
		}

		return projectItem;
	}

	/**
	 * Bind a projectTreeItem buttons with the action : - delete the project
	 * 
	 * @param projectItem
	 *            The project tree item where the actions are attached.
	 */
	private void bindProjectTreeItem(final ProjectTreeItem projectItem) {
		projectItem.getDeleteProjectButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//TODO Display a popup to ask to delete or not the project ?
				doDeleteProject(projectItem.getProject());
			}
		});
	}

	/**
	 * Bind the directoryTreeItem buttons with the action : - create a new diagram
	 * 
	 * @param directoryTreeItem
	 *            The directory tree item where the actions are attached.
	 */
	private void bindDirectoryItem(final DirectoryTreeItem directoryTreeItem) {
		directoryTreeItem.getAddDiagramButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final CreateDiagramPopup createDiagramPopup = new CreateDiagramPopup();
				createDiagramPopup.getCreateButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String directoryKey = directoryTreeItem.getDirectory().getKey();
						DirType directoryType = directoryTreeItem.getDirectory().getDirType();
						doCreateDiagram(createDiagramPopup, directoryKey, directoryType, createDiagramPopup.getDiagramName());
					}
				});

				createDiagramPopup.show();
			}
		});
	}

	/**
	 * Bind the diagramTreeItem (view) with the actions :
	 *  - Edit the diagram.
	 *  - Delete the diagram.
	 * @param diagram The diagram on which the actions occurs.
	 * @param diagramTreeItem The view where the actions are attached.
	 */
	private void bindDiagramTreeItem(final DiagramDto diagram, DiagramTreeItem diagramTreeItem) {
		diagramTreeItem.getEditDiagramButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doEditDiagram(diagram);
			}
		});

		diagramTreeItem.getDeleteDiagramButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doDeleteDiagram(diagram);
			}
		});
	}

	/**
	 * Fill the tree with the user's projects and directories. If the user has no project, display a message.
	 */
	private void doFectchProjects() {
		display.getContainer().clear();
		display.getContainer().add(display.getLoadingProjectsWidget());

		tree.removeItems();
		projectService.getProjects(new AsyncCallback<List<Project>>() {

			@Override
			public void onSuccess(List<Project> projectsFound) {
				display.getContainer().clear();
				if (projectsFound.size() == 0) {
					display.getContainer().add(display.getNoProjectWidget());
				} else {
					for (Project project : projectsFound) {
						tree.addItem(createProjectItem(project));
					}
					display.getContainer().add(tree);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error while fetching the projects : " + caught.getMessage());
				ErrorToaster.show("Error while fetching the projects, please retry in few moments or contact the administrator.");
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
				MessageToaster.show("Project " + projectName + " created with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				createProjectPopup.hide();
				Log.error("Failed to create the project " + projectName);
				ErrorToaster.show("Failed to create the project " + projectName +", please retry in few moments or contact the administrator.");
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
				MessageToaster.show("Project " + projectToDelete.getName() + " deleted with success");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to delete the project " + projectToDelete);
				ErrorToaster.show("Failed to delete the project " + projectToDelete.getName() +", please retry in few moments or contact the administrator.");
			}
		});
	}


	/**
	 * Create a diagram in the given directory
	 * When the diagram is created, load the designer with it.
	 * @param createDiagramPopup The Popup to hide when the action is finished.
	 * @param directoryKey The directory key
	 * @param directoryType The type of directory to create
	 * @param diagramName diagramName The diagram name.
	 */
	private void doCreateDiagram(CreateDiagramPopup createDiagramPopup, String directoryKey, DirType directoryType, String diagramName) {
		createDiagramPopup.hide();
		
		DiagramType diagramType = getDiagramTypeFromDirectoryType(directoryType);
		
		final DiagramDto diagramDto = new DiagramDto(directoryKey, diagramName, diagramType);

		// Create a default canvas to save with the new diagram.
		UMLCanvas defaultCanvas = new UMLCanvas(CLASS);
		diagramDto.setCanvas(defaultCanvas);

		diagramService.createDiagram(diagramDto, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String key) {
				diagramDto.setKey(key);
				doEditDiagram(diagramDto);
				doFectchProjects();
				MessageToaster.show("Diagram " + diagramDto.getName() + " created with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error while creating the diagram : " + caught.getMessage());
				ErrorToaster.show("Error while creation the diagram, please retry in few moments or contact the administrator.");
			}
		});
	}

	/**
	 * @param directoryType
	 * @return a type of diagram from a type of directory.
	 */
	private DiagramType getDiagramTypeFromDirectoryType(DirType directoryType) {
		switch (directoryType) {
			case DOMAIN : return CLASS;
			case HCI : return OBJECT;
			case SERVICE : return CLASS;
		}
		return CLASS;
	}

	/**
	 * Request the edition of the given diagram
	 * @param diagram the diagram to edit in the modeler.
	 */
	private void doEditDiagram(final DiagramDto diagram) {
		eventBus.fireEvent(new EditDiagramEvent(diagram));
	}

	/**
	 * Request the deletion of the given diagram.
	 * @param diagram The diagram to delete.
	 */
	private void doDeleteDiagram(final DiagramDto diagram) {
		diagramService.deleteDiagram(diagram.getKey(), new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				doFectchProjects();
				MessageToaster.show("Diagram " + diagram.getName() + " deleted with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to delete the diagram " + diagram);
				ErrorToaster.show("Failed to delete the diagram " + diagram.getName() +", please retry in few moments or contact the administrator.");
			}
		});
	}
}
