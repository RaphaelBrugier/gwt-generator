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
package com.objetdirect.gwt.gen.shared.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulate a class name and its generated source code.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class GeneratedCode implements Serializable {

	/** Name of the encapsulated class. */
	private String className;
	
	/** Generated code of the encapsulated class. */
	private List<String> linesOfCode;
	
	/**
	 * Default constructor ONLY for gwt-rpc serialization. 
	 */
	public GeneratedCode() {
		linesOfCode = new LinkedList<String>();
	}
	
	/**
	 * Constructor
	 * @param className The name of the encapsulated class
	 * @param linesOfCode The generated lines of code.
	 */
	public GeneratedCode(String className, List<String> linesOfCode) {
		this();
		this.className = className;
		this.linesOfCode = linesOfCode;
	}

	/**
	 * Get the name of the class
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the name of the class.
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Get the lines of code of the generated class.
	 * @return the linesOfCode
	 */
	public List<String> getLinesOfCode() {
		return linesOfCode;
	}

	/**
	 * Set the generated lines of code for the class. 
	 * @param linesOfCode the linesOfCode to set
	 */
	public void setLinesOfCode(List<String> linesOfCode) {
		this.linesOfCode = linesOfCode;
	}
}
