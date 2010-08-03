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
package com.objetdirect.gwt.gen.server.gen.processors.fields;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.gen.server.gen.processors.Processor;
import com.objetdirect.gwt.gen.server.gen.seamMM.EnumField;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class EnumFieldProcessor extends Processor {

	public static final String ENUM_FIELD = "EnumField";

	public EnumFieldProcessor(SeamGenerator seamGenerator) {
		super(seamGenerator);
	}
	
	@Override
	public void process(UMLObject object) {
		String fieldName = object.getValueOfAttribute("fieldName");
		String fieldTitle = object.getValueOfAttribute("fieldTitle");
		String length = object.getValueOfAttribute("length");
		
		EnumField enumField = new EnumField(fieldName, fieldTitle, length);
		
		seamGenerator.addBridgeObject(object, enumField);
	}

	@Override
	public String getProcessedClassName() {
		return ENUM_FIELD;
	}
}
