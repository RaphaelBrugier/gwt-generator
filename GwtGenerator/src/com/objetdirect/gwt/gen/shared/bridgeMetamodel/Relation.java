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

/**
 * Represent a relation between two classes. By default the association is simple and not an aggregation or a
 * composition.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class Relation {

	private final Clazz ownerClass;
	private final Clazz targetClass;

	private final String ownerRole;
	private final String targetRole;

	private final String ownerCardinality;
	private final String targetCardinality;

	private boolean composition;
	private boolean aggregation;

	/**
	 * Construct a simple unidirectional association between two classes. The cardinality of the owner class is set to 1
	 * 
	 * @param ownerClass
	 *            the class owning the association
	 * @param targetClass
	 *            the class targeted by the association
	 * @param targetCardinality
	 *            the cardinality of the association in the target side
	 * @param targetRole
	 *            the role of the target in the association
	 */
	public Relation(Clazz ownerClass, Clazz targetClass, String targetCardinality, String targetRole) {
		this(ownerClass, targetClass, targetCardinality, targetRole, "1");
	}

	/**
	 * Construct an unidirectional association between two classes.
	 * 
	 * @param ownerClass
	 *            the class owning the association
	 * @param targetClass
	 *            the class targeted by the association
	 * @param targetCardinality
	 *            the cardinality of the association in the target side
	 * @param targetRole
	 *            the role of the target in the association
	 * @param ownerCardinality
	 *            the cardinality of the owner class.
	 */
	public Relation(Clazz ownerClass, Clazz targetClass, String targetCardinality, String targetRole, String ownerCardinality) {
		this(ownerClass, targetClass, targetCardinality, targetRole, ownerCardinality, "");
	}

	/**
	 * Construct a bidirectionnal association between two classes.
	 * 
	 * @param ownerClass
	 *            the class owning the association
	 * @param targetClass
	 *            the class targeted by the association
	 * @param targetCardinality
	 *            the cardinality of the association in the target side
	 * @param targetRole
	 *            the role of the target in the association
	 * @param ownerCardinality
	 *            the cardinality of the owner class.
	 * @param ownerRole
	 */
	public Relation(Clazz ownerClass, Clazz targetClass, String targetCardinality, String targetRole, String ownerCardinality, String ownerRole) {
		this.ownerClass = ownerClass;
		this.targetClass = targetClass;

		this.targetRole = targetRole;
		this.ownerRole = ownerRole;

		this.ownerCardinality = ownerCardinality;
		this.targetCardinality = targetCardinality;

		setComposition(false);
		setAggregation(false);
	}

	/**
	 * @return the composition
	 */
	public boolean isComposition() {
		return composition;
	}

	/**
	 * @param composition
	 *            the composition to set
	 */
	public Relation setComposition(boolean composition) {
		this.composition = composition;
		if (composition = true) {
			aggregation = false;
		}
		return this;
	}

	/**
	 * @return the aggregation
	 */
	public boolean isAggregation() {
		return aggregation;
	}

	/**
	 * @param aggregation
	 *            the aggregation to set
	 */
	public Relation setAggregation(boolean aggregation) {
		this.aggregation = aggregation;
		if (aggregation = true) {
			composition = false;
		}
		return this;
	}

	/**
	 * @return the ownerClass
	 */
	public Clazz getOwnerClass() {
		return ownerClass;
	}

	/**
	 * @return the targetClass
	 */
	public Clazz getTargetClass() {
		return targetClass;
	}

	/**
	 * @return the ownerRole
	 */
	public String getOwnerRole() {
		return ownerRole;
	}

	/**
	 * @return the targetRole
	 */
	public String getTargetRole() {
		return targetRole;
	}

	/**
	 * @return the ownerCardinality
	 */
	public String getOwnerCardinality() {
		return ownerCardinality;
	}

	/**
	 * @return the targetCardinality
	 */
	public String getTargetCardinality() {
		return targetCardinality;
	}
}
