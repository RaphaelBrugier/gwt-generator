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

package com.objetdirect.engine;

import junit.framework.TestCase;

public class TestRewrite extends TestCase {

	public void testSimpleReplace() {
		String result = Rewrite.replace("Type var = new Type(param);", "Type", "Date");
		assertEquals("Date var = new Date(param);", result);
		result = Rewrite.replace("Type var = new Type(param);", "Type", "StringTokenizer");
		assertEquals("StringTokenizer var = new StringTokenizer(param);", result);
	}
	
	public void testMultipleReplace() {
		String result = Rewrite.replace("import package.class;", "package", "com.objetdirect.steelman", "class", "Engine");
		assertEquals("import com.objetdirect.steelman.Engine;", result);
	}
	
	public void testTextReplace() {
		String[] result = Rewrite.replace(new String[] {
			"Type var = new Type(param);",
			"Type var2 = var;",
			"var.equals(var);"
		}, "Type", "Date");
		TestUtil.assertText(result,
			"Date var = new Date(param);",
			"Date var2 = var;",
			"var.equals(var);"			
		);
	}
	
	public void testMultipleReplaceWithMultiplePatterns() {
		String[] result = Rewrite.replace(
			new String[] {
				"Type method() {",
				"	if (attribute==null)",
				"		attribute = new Type();",
				"	return attribute;",
				"}"
			}, 
			"Type", "Client", "attribute", "client", "method", "getClient");
		TestUtil.assertText(result,
			"Client getClient() {",
			"	if (client==null)",
			"		client = new Client();",
			"	return client;",
			"}"
		);
	}

	public void testTextRemove() {
		String[] result = Rewrite.remove(new String[] {
			"if (x==1 and condition) {",
			"	System.out.println(x);",
			"}"
		}, " and condition");		
		TestUtil.assertText(result,
			"if (x==1) {",
			"	System.out.println(x);",
			"}"
		);
	}
	
	public void testTextIndent() {
		String[] result = Rewrite.indent(new String[] {
				"Date var = new Date(param);",
				"Date var2 = var;",
				"var.equals(var);"		
			}, 2);
			TestUtil.assertText(result,
				"		Date var = new Date(param);",
				"		Date var2 = var;",
				"		var.equals(var);"				
			);
	}
	
	public void testInsertLine() {
		String[] text = new String[] {
			"// insert init here",
			"if (processIt()) {",
			"	// insert instructions here",
			"}"
		};
		text = Rewrite.insertLines(text, "// insert init here", new String[] {"String s = null;"});
		TestUtil.assertText(text, new String[] {
			"String s = null;",
			"// insert init here",
			"if (processIt()) {",
			"	// insert instructions here",
			"}"
		});
		text = Rewrite.insertLines(text, "// insert instructions here", new String[] {"s = \"Hello world\";", "s.toUpperCase();"});
		TestUtil.assertText(text, new String[] {
			"String s = null;",
			"// insert init here",
			"if (processIt()) {",
			"	s = \"Hello world\";",
			"	s.toUpperCase();",	
			"	// insert instructions here",
			"}"
		});		
	}
	
	public void testRemoveLine() {
		String[] text = Rewrite.array(
			"String s = null;",
			"// insert init here",
			"if (processIt()) {",
			"	s = \"Hello world\";",
			"	s.toUpperCase();",	
			"	// insert instructions here",
			"}");
		text = Rewrite.removeLine(text, "// insert init here");
		text = Rewrite.removeLine(text, "// insert instructions here");
		TestUtil.assertText(text, Rewrite.array(
			"String s = null;",
			"if (processIt()) {",
			"	s = \"Hello world\";",
			"	s.toUpperCase();",	
			"}")
		);		
	}
	
	public void testRemoveLines() {
		String[] text = {
			"void setCurrentClient(Client client) {",
			"	currentClient = client;",
			"	if (client!=null) {",
			"		// insert init here",
			"	}",
			"}"
		};
		text = Rewrite.removeLines(text,
			"if (client!=null) {",
			"	// insert init here",
			"}"
		);
		TestUtil.assertText(text, Rewrite.array(
			"void setCurrentClient(Client client) {",
			"	currentClient = client;",
			"}")
		);		
	}
	
	public void testReplaceAndInsertLines() {
		String line = "current = initialization;";
		String[] text = Rewrite.replaceAndInsertLines(line, "initialization",
			"new Thread() {",
			"	public void run(){",
			"		// insert code here",
			"	}",
			"}"
		);
		TestUtil.assertText(text, Rewrite.array(
			"current = new Thread() {",
			"	public void run(){",
			"		// insert code here",
			"	}",
			"};")
		);
		line = "		new Process(){}.task = new Task(){}.setPriority(HIGH);";
		text = Rewrite.replaceAndInsertLines(line, "{}",
			"{",
			"	public void run(){",
			"		// insert code here",
			"	}",
			"}"
		);
		TestUtil.assertText(text, Rewrite.array(
			"		new Process(){",
			"			public void run(){",
			"				// insert code here",
			"			}",
			"		}.task = new Task(){",
			"			public void run(){",
			"				// insert code here",
			"			}",
			"		}.setPriority(HIGH);")
		);
	}
	
	public void testSpecialReplaceAndInsertLines() {
		String line = "current = initialization;";
		String[] text = Rewrite.replaceAndInsertLines(line, "initialization", "new Thread()");
		TestUtil.assertText(text, Rewrite.array("current = new Thread();"));
		line = "current = new Task();";
		text = Rewrite.replaceAndInsertLines(line, "initialization", "new Thread()");
		TestUtil.assertText(text, Rewrite.array("current = new Task();"));
		line = "";
		text = Rewrite.replaceAndInsertLines(line, "initialization", "new Thread()");
		TestUtil.assertText(text, Rewrite.array(""));
		line = "		";
		text = Rewrite.replaceAndInsertLines(line, "initialization", "new Thread()");
		TestUtil.assertText(text, Rewrite.array("		"));
	}

	public void testTextReplaceAndInsertLines() {
		String[] text = Rewrite.array(
			"Task ti = new InitTask() {};",
			"Task tf = new FinalTask() {};"
		);
		text = Rewrite.replaceAndInsertLines(text, "{}",
			"{",
			"	public void run(){",
			"		// insert code here",
			"	}",
			"}"
		);
		TestUtil.assertText(text, Rewrite.array(
			"Task ti = new InitTask() {",
			"	public void run(){",
			"		// insert code here",
			"	}",
			"};",
			"Task tf = new FinalTask() {",
			"	public void run(){",
			"		// insert code here",
			"	}",
			"};"
		));
	}
	
}
