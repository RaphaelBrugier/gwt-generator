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

import static com.objetdirect.gwt.gen.server.gen.UMLObjectInstantiator.getJavaClassFromUmlObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.StringTokenizer;

import com.objetdirect.gwt.gen.shared.exceptions.DiagramGenerationException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ObjectRelationMaker {

	SeamGenerator seamGenerator;
	
	public ObjectRelationMaker(SeamGenerator seamGenerator) {
		this.seamGenerator = seamGenerator;
	}


	/**
	 * @param objectRelation
	 * @return
	 */
	private Object getOwnerObjectFromRelation(ObjectRelation objectRelation) {
		UMLObject umlObjectOwner = objectRelation.getLeftObject();
		Object ownerObject = seamGenerator.getGenObjectCounterPartOf(umlObjectOwner);
		return ownerObject;
	}


	/**
	 * @param objectRelation
	 * @return
	 */
	private Object getTargetObjectFromRelation(ObjectRelation objectRelation) {
		UMLObject umlObjectTarget = objectRelation.getRightObject();
		Object targetObject = seamGenerator.getGenObjectCounterPartOf(umlObjectTarget);
		return targetObject;
	}


	/**
	 * @param objectRelation
	 */
	public void createRelationFromUml(ObjectRelation objectRelation) {
		UMLObject umlObjectOwner = objectRelation.getLeftObject();
		
		Class<?> ownerJavaClass = getJavaClassFromUmlObject(umlObjectOwner);

		Object ownerObject = getOwnerObjectFromRelation(objectRelation);
		Object targetObject = getTargetObjectFromRelation(objectRelation);

		String setMethodName = makeSetMethodFromRelation(objectRelation);

		Method setMethod = null;
		try {
			Method[] methods = ownerJavaClass.getMethods();
			for (Method method : methods) {
				if (method.getName().equals(setMethodName)) {
					setMethod = method;
				}
			}
			if (setMethod == null) {
				throw new DiagramGenerationException("unable to find the method " + setMethodName);
			}
			setMethod.invoke(ownerObject, targetObject);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new DiagramGenerationException("unable to get the method " + setMethodName + "due to security exception");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new DiagramGenerationException("unable to invoke the method " + setMethodName + "due to illegal argument");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new DiagramGenerationException("unable to get the method " + setMethodName + "due to security exception");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new DiagramGenerationException("unable to get the method " + setMethodName);
		}
	}

	/**
	 * @param objectRelation
	 */
	String makeSetMethodFromRelation(ObjectRelation objectRelation) {
		String fieldName = objectRelation.getRightRole();
		if (fieldName == null || fieldName.isEmpty()) {
			fieldName = extractFieldNameFromClassName(objectRelation);
		}

		String fieldNameCapitalized = fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
		String setMethod = "set".concat(fieldNameCapitalized);
		return setMethod;
	}


	/**
	 * @param objectRelation
	 * @return
	 */
	private String extractFieldNameFromClassName(ObjectRelation objectRelation) {
		String className = objectRelation.getRightObject().getClassName();
		String[] path = className.split("\\.");
		String fieldName = null;
		for (String s : path) {
			fieldName = s;
		}
		
		return fieldName;
	}
}
