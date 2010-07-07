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
package com.objetdirect.gwt.gen.server.gen.relationProcessors;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import com.objetdirect.gwt.gen.server.gen.seamMM.StringField;
import com.objetdirect.seam.print.PrintDescriptor;
import com.objetdirect.seam.print.PrintEntityDescriptor;
import com.objetdirect.seam.print.PrintFormDescriptor;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPrintFormToStringField extends TestRelationProcessor{
	private static final String FIELD_TITLE = "fieldTitle";
	private static final String FIELD_NAME = "fieldName";
	private static final String LENGTH = "20";

	@Mock
	PrintFormDescriptor printForm;
	
	StringField stringField;
	
	@Test
	public void process() {
		PrintFormToStringField processor = new PrintFormToStringField(seamGenerator);
		stringField  = new StringField(FIELD_NAME, FIELD_TITLE, LENGTH);
		setReturnedGenObject(printForm, stringField, null);

		processor.process(objectRelation);
		
		verify(seamGenerator).getObjectGenCounterPartOf(umlObjectOwner);
		verify(seamGenerator).getObjectGenCounterPartOf(umlObjectTarget);
		verify(printForm).showField(FIELD_NAME, FIELD_TITLE, 20);
	}
}
