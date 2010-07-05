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
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulate a class name and its generated source code.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class GeneratedCode implements Serializable {

	public enum CodeType {
		JAVA, FACELET;
	}
	
	/** Name of the encapsulated class. */
	private String className;
	
	/** Generated code of the encapsulated class. */
	private List<String> linesOfCode;
	
	private CodeType codeType;
	

	/**
	 * ONLY FOR GWT-RPC Serialization !
	 * DO NOT USE
	 */
	@SuppressWarnings("unused")
	private GeneratedCode() {
	}
	
	/**
	 * Constructor
	 * @param className The name of the encapsulated class
	 * @param linesOfCode The generated lines of code.
	 * @param codeType type of code, ie : java or facelet.
	 */
	public GeneratedCode(String className, String[] linesOfCode, CodeType codeType) {
		this.className = className;
		this.linesOfCode = Arrays.asList(linesOfCode);
		this.codeType = codeType;
	}
	

	/**
	 * Get the name of the class
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Get the lines of code of the generated class.
	 * @return the linesOfCode
	 */
	public List<String> getLinesOfCode() {
		return linesOfCode;
	}
	
	/**
	 * @return the codeType
	 */
	public CodeType getCodeType() {
		return codeType;
	}
}
