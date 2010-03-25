/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.entities;

import com.objetdirect.engine.TypeDescriptor;

public class JavaxPersistence {
	
	public static final String JAVAX_PERSISTENCE="javax.persistence";
	
	public static final TypeDescriptor Entity = new TypeDescriptor(JAVAX_PERSISTENCE, "Entity");
	public static final TypeDescriptor Id = new TypeDescriptor(JAVAX_PERSISTENCE, "Id");
	public static final TypeDescriptor GeneratedValue = new TypeDescriptor(JAVAX_PERSISTENCE, "GeneratedValue");
	public static final TypeDescriptor Version = new TypeDescriptor(JAVAX_PERSISTENCE, "Version");
	
	public static final TypeDescriptor OneToOne = new TypeDescriptor(JAVAX_PERSISTENCE, "OneToOne");
	public static final TypeDescriptor OneToMany = new TypeDescriptor(JAVAX_PERSISTENCE, "OneToMany");
	public static final TypeDescriptor ManyToOne = new TypeDescriptor(JAVAX_PERSISTENCE, "ManyToOne");
	public static final TypeDescriptor ManyToMany = new TypeDescriptor(JAVAX_PERSISTENCE, "ManyToMany");
	public static final TypeDescriptor CascadeType = new TypeDescriptor(JAVAX_PERSISTENCE, "CascadeType");
	public static final TypeDescriptor PreRemove = new TypeDescriptor(JAVAX_PERSISTENCE, "PreRemove");
	public static final TypeDescriptor Transient = new TypeDescriptor(JAVAX_PERSISTENCE, "Transient");
	public static final TypeDescriptor EntityManager = new TypeDescriptor(JAVAX_PERSISTENCE, "EntityManager");
	public static final TypeDescriptor PersistenceContext = new TypeDescriptor(JAVAX_PERSISTENCE, "PersistenceContext");
	public static final TypeDescriptor EntityNotFoundException = new TypeDescriptor(JAVAX_PERSISTENCE, "EntityNotFoundException");
}
//javax.persistence.EntityNotFoundException