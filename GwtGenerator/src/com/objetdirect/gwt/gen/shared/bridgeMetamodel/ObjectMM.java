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
package com.objetdirect.gwt.gen.shared.bridgeMetamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ObjectMM {

	private final Clazz instanceOf;

	private final String name;

	private final List<ValueOfAttribute> valuesOfAttributes;

	public ObjectMM(Clazz instanceOf) {
		this(instanceOf, "");
	}

	public ObjectMM(Clazz instanceOf, String name) {
		this.instanceOf = instanceOf;
		this.name = name;
		valuesOfAttributes = new ArrayList<ValueOfAttribute>();
	}

	public ObjectMM addValueOfAttribute(String attributeName, String value) {
		Attribute attribute = getAttribute(attributeName);
		ValueOfAttribute<String> valueOfAttribute = new ValueOfAttribute<String>(attribute, value);

		valuesOfAttributes.add(valueOfAttribute);

		return this;
	}


	public ObjectMM addValueOfAttribute(String attributeName, int value) {
		Attribute attribute = getAttribute(attributeName);
		ValueOfAttribute<Integer> valueOfAttribute = new ValueOfAttribute<Integer>(attribute, value);

		valuesOfAttributes.add(valueOfAttribute);

		return this;
	}

	public ObjectMM addValueOfAttribute(String attributeName, boolean value) {
		Attribute attribute = getAttribute(attributeName);
		ValueOfAttribute<Boolean> valueOfAttribute = new ValueOfAttribute<Boolean>(attribute, value);

		valuesOfAttributes.add(valueOfAttribute);

		return this;
	}

	/**
	 * @param attributeName
	 * @return
	 */
	private Attribute getAttribute(String attributeName) {
		Attribute attribute = instanceOf.getAttribute(attributeName);
		if (attribute != null) {
			return attribute;
		} else {
			throw new RuntimeException("No attribute correspond to " + attributeName + " in the instantiated object.");
		}
	}

	/**
	 * @return the instanceOf
	 */
	public Clazz getInstanceOf() {
		return instanceOf;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the valuesOfAttributes
	 */
	public List<ValueOfAttribute> getValuesOfAttributes() {
		return valuesOfAttributes;
	}

	private ValueOfAttribute getValueOfAttribute(String attributeName) {
		Attribute attribute = getAttribute(attributeName);
		for (ValueOfAttribute valueOfAttribute : valuesOfAttributes) {
			if (valueOfAttribute.getAttribute().equals(attribute)) {
				return valueOfAttribute;
			}
		}

		return null;
	}

	public String getStringValueOfAttribute(String attributeName) {
		ValueOfAttribute<String> valueOfAtt = getValueOfAttribute(attributeName);

		if (valueOfAtt != null) {
			return valueOfAtt.getValue();
		}
		throw new RuntimeException("No String value found for the attribute " + attributeName + " on this object.");
	}
}
