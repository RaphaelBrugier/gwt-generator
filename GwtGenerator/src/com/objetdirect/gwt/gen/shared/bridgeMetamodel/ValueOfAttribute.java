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

/**
 * Represent the value for an attribute in an instantiate object
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ValueOfAttribute<T> {

	private final Attribute attribute;
	private final T value;

	/**
	 * 
	 */
	public ValueOfAttribute(Attribute attribute, T value) {
		this.attribute = attribute;
		this.value = value;
	}

	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
}
