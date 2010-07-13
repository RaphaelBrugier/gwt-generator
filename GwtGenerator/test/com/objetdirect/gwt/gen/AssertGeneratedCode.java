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
package com.objetdirect.gwt.gen;

import static com.objetdirect.gwt.gen.TestUtil.findLinesOfCode;
import static com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType.JAVA;
import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;

/**
 * A simple DSL to simplified the test of the generated code.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class AssertGeneratedCode {
	public static AssertGeneratedCode In(List<GeneratedCode> generatedClassesCode) {
		return new AssertGeneratedCode(generatedClassesCode);
	}
	
	private final List<GeneratedCode> generatedClassesCode;
	private String className;
	private String packageName;
	private CodeType codeType;
	private List<String[]> contained;

	private AssertGeneratedCode(List<GeneratedCode> generatedClassesCode) {
		this.generatedClassesCode = generatedClassesCode;
		contained = new ArrayList<String[]>();
		packageName = TestUtil.packageName;
		codeType = JAVA;
		className = null;
	}
	
	public AssertGeneratedCode theCodeOfClass(String className) {
		this.className = className;
		return  this;
	}
	
	/**
	 * Default to "com.od.test"
	 */
	public AssertGeneratedCode ofPackage(String packageName) {
		this.packageName = packageName;
		return this;
	}

	/**
	 * Default to JAVA
	 */
	public AssertGeneratedCode ofType(CodeType codeType) {
		this.codeType = codeType;
		return this;
	}
	
	public AssertGeneratedCode contains (String ...model) {
		contained.add(model);
		return this;
	}
	
	/**
     * Assert that all the code of the class exactly equals the given model.
	 */
	public void equals(String ... model) {
		assert className != null : "you should give a class name";
		
		List<String> linesOfCode = findLinesOfCode(className, generatedClassesCode, codeType);
		
		assertNotNull("Found no lines of Code", linesOfCode);
		TestUtil.assertText(linesOfCode, model);
	}
	
	/**
	 * Verify that all the models given in the contains method exist in the code of the class. 
	 */
	public void verify() {
		List<String> linesOfCode = findLinesOfCode(className, generatedClassesCode, codeType);
		for(String[] model : contained) {
			TestUtil.assertExists(linesOfCode, model);
		}
	}

}
