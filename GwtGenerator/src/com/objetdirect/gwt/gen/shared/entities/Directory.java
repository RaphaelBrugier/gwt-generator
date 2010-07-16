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
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.objetdirect.gwt.gen.shared.dto.DiagramDto;

/**
 * Represent a Directory of a user.
 * A directory is owned by a project.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
@PersistenceCapable(detachable="true")
public class Directory implements Serializable {
	
	public enum DirectoryType {
		DOMAIN, SEAM
	}
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String email;
	
	@Persistent
	private DirectoryType dirType;
	
	@NotPersistent
	private List<DiagramDto> diagrams;
	
	/** Default constructor ONLY for gwt-rpc serialization. */
	@SuppressWarnings("unused")
	private Directory() {}
	
	public Directory(String name, String email, DirectoryType dirType) {
		this.name = name;
		this.email = email;
		this.dirType= dirType;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
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
	 * @return the dirType
	 */
	public DirectoryType getDirType() {
		return dirType;
	}
	
	/**
	 * @return the diagrams
	 */
	public List<DiagramDto> getDiagrams() {
		return diagrams;
	}

	/**
	 * @param diagrams the diagrams to set
	 */
	public void setDiagrams(List<DiagramDto> diagrams) {
		this.diagrams = diagrams;
	}

	@Override
	public String toString() {
		return "Directory  key = " + key + "  name = " + name + "  email = " + email;
	}
}
