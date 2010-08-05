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

import static com.objetdirect.gwt.gen.server.gen.processors.PrintFormProcessor.PRINT_FORM;
import static com.objetdirect.gwt.gen.server.gen.processors.PrintInternalListDescriptorProcessor.PRINT_INTERNAL_LIST;
import static com.objetdirect.gwt.gen.server.gen.processors.PrintListDescriptorProcessor.PRINT_LIST_DESCRIPTOR;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.BooleanFieldProcessor.BOOLEAN_FIELD;

import java.util.Arrays;
import java.util.List;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.gen.server.gen.seamMM.BooleanField;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.seam.fieldrenderers.HasFields;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class HasFieldsToBooleanField extends RelationProcessor<HasFields, BooleanField> {

	private static final String FALSE_VALUE = "FALSE";
	private static final String TRUE_VALUE = "TRUE";

	public HasFieldsToBooleanField(SeamGenerator seamGenerator) {
		super(seamGenerator);
	}

	@Override
	public void process(ObjectRelation objectRelation) {
		HasFields printFormDescriptor = getOwner(objectRelation);
		BooleanField booleanField  = getTarget(objectRelation);
		printFormDescriptor.addBooleanField(booleanField.fieldName, booleanField.fieldTitle, TRUE_VALUE, FALSE_VALUE);
	}

	@Override
	public List<String> getOwnerClassNames() {
		return Arrays.asList(PRINT_FORM, PRINT_INTERNAL_LIST, PRINT_LIST_DESCRIPTOR);
	}

	@Override
	public List<String> getTargetClassNames() {
		return Arrays.asList(BOOLEAN_FIELD);
	}
}
