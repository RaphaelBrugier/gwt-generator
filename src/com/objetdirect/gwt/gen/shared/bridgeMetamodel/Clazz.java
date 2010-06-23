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
package com.objetdirect.gwt.gen.shared.bridgeMetamodel;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType;

/**
 * Represents a class in the bridge meta model
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class Clazz {

	private final String name;

	private final List<Attribute> attributes;

	public Clazz(String name) {
		this.name = name;
		attributes = new ArrayList<Attribute>();
	}

	/**
	 * Add an attribute to the class
	 * 
	 * @param name
	 *            the name of the attribute
	 * @param type
	 *            the type of the attribute
	 */
	void addAttribute(String name, UMLType type) {
		if (getAttribute(name) != null) {
			throw new RuntimeException("An attribute with this name already exist in the class");
		}
		Attribute attribute = new Attribute(name, type);
		attributes.add(attribute);
	}

	/**
	 * Return the attribute attribute in the class from its name or null if no attribute correspond to this name.
	 * 
	 * @param attributeName
	 * @return the attribute found or null either.
	 */
	Attribute getAttribute(String attributeName) {
		List<Attribute> attributes = getAttributes();
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(attributeName)) {
				return attribute;
			}
		}

		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}
}
