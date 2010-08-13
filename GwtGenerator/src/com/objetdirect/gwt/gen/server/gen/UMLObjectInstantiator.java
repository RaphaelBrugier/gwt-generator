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
package com.objetdirect.gwt.gen.server.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.objetdirect.gwt.gen.shared.exceptions.InstantiateObjectDiagramException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObjectAttribute;

/**
 * This class is responsible to instantiate generator object from UmlObject using reflection.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class UMLObjectInstantiator {

	private static final String PACKAGE_NAME = "com.objetdirect.";

	public Object instantiate(UMLObject umlObject) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String className = umlObject.getClassName();

		Class<?> classToInstantiate = Class.forName(PACKAGE_NAME + className);

		Object object = null;

		int numberOfAttributes = umlObject.getObjectAttributes().size();
		if (numberOfAttributes == 0) {
			object = classToInstantiate.newInstance();
		} else {
			object = instantiateWithParameters(umlObject.getObjectAttributes(), classToInstantiate);
		}

		return object;
	}

	/**
	 * @param umlAttributes
	 * @param classToInstantiate
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	private Object instantiateWithParameters(List<UMLObjectAttribute> umlAttributes, Class<?> classToInstantiate) throws InstantiationException, IllegalAccessException {
		int numberOfAttributes = umlAttributes.size();
		Class<String>[] paramTypes = new Class[numberOfAttributes];
		Object[] attributesValues = new Object[numberOfAttributes];
		for (int i =0; i<numberOfAttributes; i++) {
			paramTypes[i] = String.class;
			attributesValues[i] = umlAttributes.get(i).getValue();
		}
		
		String className = classToInstantiate.getSimpleName();
		Constructor<?> constructor;
		try {
			constructor = classToInstantiate.getConstructor(paramTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new InstantiateObjectDiagramException("Unable to instantiate the class " + className + " due to security restrictions");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InstantiateObjectDiagramException(
					"Unable to instantiate the class " + className + " because there is no constructor for the given parameters");
		}
		
		Object object = null;
		try {
			object = constructor.newInstance(attributesValues);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new InstantiateObjectDiagramException(
					"Unable to instantiate the class " + className + " because wrong parameters has been passed to the constructor " + constructor);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new InstantiateObjectDiagramException(
					"Unable to instantiate the class " + className + " because wrong parameters has been passed to the constructor " + constructor);
		}
		return object;
	}
}
