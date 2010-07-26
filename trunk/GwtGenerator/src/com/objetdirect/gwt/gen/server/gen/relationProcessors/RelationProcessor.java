/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
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

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public abstract class RelationProcessor<L, R> {
	
	protected final SeamGenerator seamGenerator;

	/**
	 * @param seamGenerator
	 */
	public RelationProcessor(SeamGenerator seamGenerator) {
		this.seamGenerator = seamGenerator;
	}

	public abstract void process(ObjectRelation objectRelation);
	
	/**
	 * Get the owner object of a relation
	 * 
	 * @param objectRelation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L getOwner(ObjectRelation objectRelation) {
		return (L) seamGenerator.getGenObjectCounterPartOf(objectRelation.getLeftObject());
	}

	/**
	 * Get the targeted object of a relation.
	 * 
	 * @param objectRelation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public R getTarget(ObjectRelation objectRelation) {
		return (R) seamGenerator.getGenObjectCounterPartOf(objectRelation.getRightObject());
	}
}
