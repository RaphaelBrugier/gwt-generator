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
package com.objetdirect.gwt.gen.shared.dto;

import java.io.Serializable;

import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;

/**
 * Data Transfert Object for the diagram entity.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class DiagramDto implements Serializable {
	public enum Type {
		CLASS, OBJECT, HYBRYD, SEQUENCE
	}
	
	private String key;
	
	private String name;
	
	private Type type;
	
	private String directoryKey;
	
	private UMLCanvas canvas;
	
	
	/** Default constructor. */
	public DiagramDto() {}
	
	
	/**
	 * Use this constructor to create a new diagram.
	 * @param directoryKey The key of the owner directory
	 * @param name the name of the diagram
	 * @param type the type of the diagram : CLASS or OBJECT or HYBRYD or SEQUENCE
	 */
	public DiagramDto(String directoryKey, String name, Type type) {
		this.name = name;
		this.type = type;
		this.directoryKey = directoryKey;
	}
	
	/**
	 * This constructor should only been used from diagramService after retrieving a diagram from the base.
	 * @param key the key of the diagram. Autogenerated by GAE.
	 * @param directoryKey The key of the owner directory
	 * @param name the name of the diagram
	 * @param type the type of the diagram : CLASS or OBJECT or HYBRYD or SEQUENCE
	 */
	public DiagramDto(String key, String directoryKey, String name, Type type) {
		this(directoryKey, name, type);
		this.key = key;
	}

	/**
	 * This constructor should only been used from diagramService after retrieving a diagram from the base.
	 * @param key the key of the diagram. Autogenerated by GAE.
	 * @param directoryKey The key of the owner directory
	 * @param name the name of the diagram
	 * @param type the type of the diagram : CLASS or OBJECT or HYBRYD or SEQUENCE
	 */
	public DiagramDto(String key, String directoryKey, String name, Type type, UMLCanvas umlCanvas) {
		this(key, directoryKey, name, type);
		this.canvas = umlCanvas;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
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
	 * @return the canvas
	 */
	public UMLCanvas getCanvas() {
		return canvas;
	}


	/**
	 * @param canvas the canvas to set
	 */
	public void setCanvas(UMLCanvas canvas) {
		this.canvas = canvas;
	}


	/**
	 * @return the directoryKey
	 */
	public String getDirectoryKey() {
		return directoryKey;
	}


	/**
	 * @param directoryKey the directoryKey to set
	 */
	public void setDirectoryKey(String directoryKey) {
		this.directoryKey = directoryKey;
	}
	
	@Override
	public String toString() {
		return "DiagramDto  \n" +
				"\t key = " + key + "\n" +
				"\t name = " + name + "\n" +
				"\t type = " + type +"\n" +
				"\t directoryKey = " + directoryKey;
	}
}
