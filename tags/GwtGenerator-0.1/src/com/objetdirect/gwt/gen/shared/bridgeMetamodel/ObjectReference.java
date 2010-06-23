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
 * Represents a reference from an object to an other.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ObjectReference {

	private final Object owner;
	private final Object target;

	private final String targetName;

	/**
	 * Construct a new reference
	 * 
	 * @param owner
	 *            The owner of the reference
	 * @param target
	 *            the target of the reference
	 * @param targetName
	 *            the name of the refence.
	 */
	public ObjectReference(Object owner, Object target, String targetName) {
		super();
		this.owner = owner;
		this.target = target;
		this.targetName = targetName;
	}

	/**
	 * @return the owner
	 */
	public Object getOwner() {
		return owner;
	}

	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

}
