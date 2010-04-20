package com.objetdirect.gwt;
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
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;

/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestUtil {

	public static String packageName = "com.objetdirect";
	
	public static void assertText(String className, List<GeneratedCode> generatedClassesCode, String ... model) {
		List<String> code = new LinkedList<String>();
		for(GeneratedCode genCode : generatedClassesCode) {
			if (genCode.getClassName().equalsIgnoreCase(className)) {
				code = genCode.getLinesOfCode();
			}
		}

		int length = model.length < code.size() ? model.length : code.size();
		for (int i=0; i<length; i++)
			Assert.assertEquals("Line not found ("+i+") :", model[i], code.get(i));
	}
	
	public static void assertExist(String className, List<GeneratedCode> generatedClassesCode, String ... model) {
		List<String> linesOfCode = new LinkedList<String>();
		for(GeneratedCode genCode : generatedClassesCode) {
			if (genCode.getClassName().equalsIgnoreCase(className)) {
				linesOfCode = genCode.getLinesOfCode();
			}
		}
		
		String[] lines = new String[linesOfCode.size()];
		System.arraycopy(linesOfCode.toArray(), 0, lines, 0, linesOfCode.size());
		assertExists(lines, model);
	}
	
	public static void assertText(String[] result, String ... model) {
		int length = model.length < result.length ? model.length : result.length;
		for (int i=0; i<length; i++)
			Assert.assertEquals("Line not found ("+i+") :", model[i], result[i]);
	}

	public static void assertExists(String[] result, String ... model) {
		
		int lineIndex = 0;
		for (int i=0; i<result.length-model.length+1; i++) {
			if (result[i].trim().equals(model[0].trim())) {
				int n = result[i].indexOf(result[i].trim());
				String indent = result[i].substring(0, n);
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
	
	static int verifySubText(String[] result, String[] model, String indent, int i) {
		for (int j=0; j<model.length; j++)
			if (!result[i+j].equals(indent+model[j])){
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
