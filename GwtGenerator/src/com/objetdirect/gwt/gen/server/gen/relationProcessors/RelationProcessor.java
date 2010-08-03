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

import java.util.List;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;


/**
 * The RelationProcessor is responsible to add a relation between two objects.
 * The RelationProcessor class is parameterized with two type, the types of the objects where the relation is established.
 * 
 * The type F designed the object's type where the relation is added (From)
 * The type T designed the object's type where the relation point to. (To)
 * 
 * Typically, the process method will do :
 * process {
 * 		F fromObject = getOwner();
 * 		T toObject = getTargert();
 * 
 * 		fromObject.setT(toObject);
 * }
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 *
 * @param <F> The object's type where the relation is added
 * @param <T> The object's type where the relation point to.
 */
public abstract class RelationProcessor<F, T> {
	
	protected final SeamGenerator seamGenerator;

	/**
	 * @param seamGenerator
	 */
	public RelationProcessor(SeamGenerator seamGenerator) {
		this.seamGenerator = seamGenerator;
	}

	/**
	 * Process a relation between two objects.
	 * 
	 * We assume that the leftObject of the relation is of the type or extends F
	 * We assume that the righObject of the relation is of the type or extends T
	 * 
	 * The typical implementation of the process method is :
	 * public void process {
	 * 		F fromObject = getOwner();
	 * 		T toObject = getTargert();
	 * 
	 * 		fromObject.setT(toObject);
	 * }
	 * 
	 * @param objectRelation the relation to process.
	 */
	public abstract void process(ObjectRelation objectRelation);
	
	/**
	 * Get the types of all the relation's owner supported by the processor.
	 * 
	 * For example, if the processor can process two kind of relation : 
	 * 1/ a relation from an object of type A to an object of type Z
	 * 2/ a relation from an object of type B to an object of type Z
	 * 
	 * Then the list returned is {"A", "B"}
	 * 
	 * We assume that the types returned is or extends the parameter F of the RelationProcessor.
	 * 
	 * @return the owner's name.
	 */
	public abstract List<String> getOwnerClassNames();
	
	
	/**
	 * Return the types of all the relation's target supported by the relation processor.
	 * 
	 * For example : if the processor can process two relations :
	 * 1/ a relation from an object of type A to an object of type B
	 * 2/ a relation from an object of type A to an object of type C
	 * 
	 * Then getTargetClassNames will return the list {"B", "C"}
	 * 
	 * We assume that the types B and C extends the type T of the RelationProcessor.
	 * 
	 * @return the list of the types 
	 */
	public abstract List<String> getTargetClassNames();
	
	/**
	 * Get the owner object of a relation
	 * 
	 * @param objectRelation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public F getOwner(ObjectRelation objectRelation) {
		return (F) seamGenerator.getGenObjectCounterPartOf(objectRelation.getLeftObject());
	}

	/**
	 * Get the targeted object of a relation.
	 * 
	 * @param objectRelation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getTarget(ObjectRelation objectRelation) {
		return (T) seamGenerator.getGenObjectCounterPartOf(objectRelation.getRightObject());
	}
}
