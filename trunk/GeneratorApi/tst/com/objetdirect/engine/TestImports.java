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

public class TestImports extends TestCase {

	public void testSimpleImports() {
		ImportSet is= new ImportSet("com.objetdirect.engine");
		is.addType(TypeDescriptor.Date);
		TestUtil.assertText(is.getText(), "import java.util.Date;");
		TypeDescriptor date = TypeDescriptor.type("java.sql", "Date");
		is.addType(date);
		assertFalse(is.accept(date));
		TestUtil.assertText(is.getText(), "import java.util.Date;");
		is.addType(TypeDescriptor.List(null));
		TestUtil.assertText(is.getText(), "import java.util.Date;", "import java.util.List;");
	}
	
	public void testUselessImports() {
		ImportSet is= new ImportSet("com.objetdirect.engine");
		is.addType(TypeDescriptor.List(null));
		is.addType(TypeDescriptor.Boolean);
		TypeDescriptor processor = TypeDescriptor.type("com.objetdirect.engine", "Processor"); 
		is.addType(processor);
		assertTrue(is.accept(processor));
		TestUtil.assertText(is.getText(), "import java.util.List;");		
	}
	
	public void testComplexTypeImport() {
		ImportSet is= new ImportSet("com.objetdirect.engine");
		is.addType(TypeDescriptor.Map(
			TypeDescriptor.type("com.objetdirect.domain", "User"), 
			TypeDescriptor.type("com.objetdirect.domain", "Product")));
		TestUtil.assertText(is.getText(), 
			"import com.objetdirect.domain.Product;",
			"import com.objetdirect.domain.User;", 
			"import java.util.Map;"
		);
	}
	
	public void testImportRawType() {
		ImportSet is= new ImportSet("com.objetdirect.engine");
		is.addType(TypeDescriptor.rDouble);
		assertTrue(is.accept(TypeDescriptor.rDouble));
		TestUtil.assertText(is.getText());
	}
	
}
