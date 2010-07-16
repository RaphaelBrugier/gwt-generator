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
package com.objetdirect.gwt.gen.shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.objetdirect.gwt.gen.shared.entities.Directory.DirectoryType;

/**
 * Represent a Project of a user.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
@PersistenceCapable(detachable="true")
@FetchGroup(name="directory", members={@Persistent(name="directories")})
public class Project implements Serializable {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	@Persistent
	private String name;

	@Persistent
	private String email;
	
	@Element(dependent = "true")
	private List<Directory> directories;


	/** Default constructor ONLY for gwt-rpc serialization. */
	@SuppressWarnings("unused")
	private Project() {}
	
	public Project(String name, String email) {
		this.name = name;
		this.email = email;
		directories = new ArrayList<Directory>();
	}
	
	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return the directories
	 */
	public List<Directory> getDirectories() {
		return directories;
	}
	

	public void addDirectory(Directory directory) {
		directories.add(directory);
	}

	public void removeDirectory(Directory directory) {
		directories.remove(directory);
	}
	
	public Directory getDirectory(DirectoryType type) {
		for (Directory directory : directories) {
			if (directory.getDirType() == type)
				return directory;
		}
		return null;
	}

	@Override
	public String toString() {
		return "Project key = " + key + " name = " + name + " email = " + email;  
	}
}
