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
package com.objetdirect.gwt.gen.server.services;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility;

/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class GeneratorServiceImpl extends RemoteServiceServlet implements GeneratorService {

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.GeneratorService#generateClassCode(com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass, java.lang.String)
	 */
	@Override
	public String[] generateClassCode(UMLClass clazz, String packageName) {
		EntityDescriptor entity = new EntityDescriptor(packageName, clazz.getName());
		
		ArrayList<UMLClassAttribute> attributes = clazz.getAttributes();
		
		for(UMLClassAttribute attribute : attributes) {
			addAttribute(entity, attribute);
		}
		
		return entity.getText();
	}
	
	private void addAttribute(EntityDescriptor entity, UMLClassAttribute attribute) {
		String name = attribute.getName();
		String type = attribute.getType();
		UMLVisibility visibility = attribute.getVisibility();
		
		switch (visibility) {
		case PRIVATE:
			break;
		default:
			throw new InvalidParameterException("Pojo generator only supports private field");
		}
		
		switch (UMLType.getUMLTypeFromString(type)) {
		case STRING :
			entity.addStringField(name, null);
			break;
		case INT :
			entity.addIntField(name, null);
			break;
			
		case INTEGER :
			entity.addWrapperIntField(name, null);
			break;
			
		case LONG : 
			entity.addLongField(name, null);
			break;
			
		case WRAPPED_LONG : 
			entity.addWrapperLongField(name, null);
			break;
			
		case BYTE : 
			entity.addByteField(name, null);
			break;
			
		case WRAPPED_BYTE : 
			entity.addWrapperByteField(name, null);
			break;
			
		case SHORT  : 
			entity.addShortField(name, null);
			break;
			
		case WRAPPED_SHORT :
			entity.addWrapperShortField(name, null);
			break;
			
		case BOOLEAN :
			entity.addBooleanField(name, null);
			break;
			
		case WRAPPED_BOOLEAN :
			entity.addWrapperBooleanField(name, null);
			break;
			
		case CHAR :
			entity.addCharField(name, null);
			break;
			
		case CHARACTER :
			entity.addWrapperCharField(name, null);
			break;
			
		case FLOAT  :
			entity.addFloatField(name, null);
			break;
			
		case WRAPPED_FLOAT :
			entity.addWrapperFloatField(name, null);
			break;
			
		case DOUBLE :
			entity.addDoubleField(name, null);
			break;
			
		case WRAPPED_DOUBLE  :
			entity.addWrapperDoubleField(name, null);
			break;

		default:
			break;
		}
	}
}
