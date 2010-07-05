package com.objetdirect.gwt.gen;
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
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;

/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestUtil {

	public static String packageName = "com.objetdirect";
	
	
	public static List<String> findLinesOfCode(String className, List<GeneratedCode> generatedClassesCode, CodeType codeType) {
		for(GeneratedCode genCode : generatedClassesCode) {
			if (genCode.getClassName().equalsIgnoreCase(className) && genCode.getCodeType() == codeType) {
				return genCode.getLinesOfCode();
			}
		}
		return null;
	}
	
	public static void assertText(String className, List<GeneratedCode> generatedClassesCode, CodeType codeType, String ... model) {
		List<String> linesOfCode = findLinesOfCode(className, generatedClassesCode, codeType);

		assertText(linesOfCode, model);
	}
	
	public static void assertExist(String className, List<GeneratedCode> generatedClassesCode, CodeType codeType, String ... model) {
		List<String> linesOfCode = findLinesOfCode(className, generatedClassesCode, codeType);

		assertExists(linesOfCode, model);
	}
	
	public static void assertText(List<String> linesOfCode, String ...model) {
		int length = model.length < linesOfCode.size() ? model.length : linesOfCode.size();
		for (int i=0; i<length; i++)
			Assert.assertEquals("Line not found ("+i+") :", model[i], linesOfCode.get(i));
	}
	
	public static void assertExists(String[] result, String ... model) {
		assertExists(Arrays.asList(result), model);
	}

	public static void assertExists(List<String> result, String ... model) {
		
		int lineIndex = 0;
		for (int i=0; i<result.size()-model.length+1; i++) {
			if (result.get(i).trim().equals(model[0].trim())) {
				int n = result.get(i).indexOf(result.get(i).trim());
				String indent = result.get(i).substring(0, n);
				int j = verifySubText(result, model, indent, i);
				if (j==-1)
					return;
				else if (j>lineIndex)
					lineIndex = j;
			}
		}
		
		Assert.fail("Line not found : "+model[lineIndex]);
	}

	public static void assertNotExisting(String[] result, String model) {
		for (String s : result)
			if (s.indexOf(model)!=-1)
				Assert.fail("Pattern '"+model+"' found in : "+s);
	}
	
	static int verifySubText(List<String> result, String[] model, String indent, int i) {
		for (int j=0; j<model.length; j++)
			if (!result.get(i+j).equals(indent+model[j])){
				return j;
			}
		return -1;
	}
	
	public static void println(List<String> text) {
		System.out.println("---------------------------------------------------------------------------------------\n");
		for (String line : text) {
			System.out.println(line);
		}
	}

	public static void println(String[] text) {
		System.out.println("---------------------------------------------------------------------------------------\n");
		for (String line : text) {
			System.out.println(line);
		}
	}
}
