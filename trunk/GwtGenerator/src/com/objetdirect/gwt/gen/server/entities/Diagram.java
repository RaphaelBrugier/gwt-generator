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
package com.objetdirect.gwt.gen.server.entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;

/**
 * A diagram stored in the app egine.
 * A diagram contains the uml components and their positions on the canvas so they could be restore on the canvas. 
 * A diagram could be a class or an object or a sequence diagram.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@PersistenceCapable
public class Diagram {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	@Persistent
	private String name;
	
	@Persistent
	private Type type;
	
	@Persistent
	private Blob serializedCanvas;
	
	@Persistent
	private User user;

	@Persistent
	private String directoryKey; 
	
	
	/**
	 * Default constructor ONLY for gwt-rpc serialization
	 */
	protected Diagram() {
	}
	
	public Diagram(String directoryKey, Type type, String name, User user) {
		this.type = type;
		this.name = name;
		this.user = user;
		this.directoryKey = directoryKey;
	}

	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return the serializedCanvas
	 */
	public Blob getSerializedCanvas() {
		return serializedCanvas;
	}

	/**
	 * @param serializedCanvas the serializedCanvas to set
	 */
	public void setSerializedCanvas(Blob serializedCanvas) {
		this.serializedCanvas = serializedCanvas;
	}
	

	/**
	 * @return the directoryKey
	 */
	public String getDirectoryKey() {
		return directoryKey;
	}
	
	
	/**
	 * Copy all the fields of the given DiagramInformations the object.
	 * @param diagramToCopy The source diagram
	 */
	public void copyFromDiagramDto(DiagramDto diagramToCopy) {
		this.type = diagramToCopy.getType();
		this.name = diagramToCopy.getName();
		
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(byteOutput);
			oos.writeObject(diagramToCopy.getCanvas());
			this.serializedCanvas = new Blob(byteOutput.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** Copy the fields of the Diagram object into the given DiagramInformations object.
	 * @param diagramToCopy the diagram the targeted diagram
	 */
	public DiagramDto copyToDiagramDto() {
		DiagramDto diagramToCopy = new DiagramDto();
		diagramToCopy.setKey(this.key);
		diagramToCopy.setType(this.type);
		diagramToCopy.setName(this.name);
		
		UMLCanvas canvas = null;
		
		if (serializedCanvas!=null) {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedCanvas.getBytes());
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(byteInput);
				canvas = (UMLCanvas)ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			diagramToCopy.setCanvas(canvas);
		}
		return diagramToCopy;
	}

}
