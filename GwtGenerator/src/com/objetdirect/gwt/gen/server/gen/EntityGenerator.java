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

import static com.objetdirect.gwt.gen.server.helpers.ObjectDiagramBuilder.ENTITY_CLASS_NAME;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * Generate the entityDescriptor from a list of class.
 * Also set the relation between each entity from the list of uml relations.
 *
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class EntityGenerator {

	List<UMLClass> classes;
	List<UMLRelation> relations;
	String packageName;

	Map<UMLClass, EntityDescriptor> entities;
	Map<UMLClass, EnumDescriptor> enumerations;

	/**
	 * @param classes
	 * @param relations
	 * @param packageName
	 */
	public EntityGenerator(List<UMLClass> classes, List<UMLRelation> relations, String packageName) {
		this.classes = classes;
		this.relations = relations;
		this.packageName = packageName;
	}

	public Collection<EntityDescriptor> getGeneratedEntities() {
		processClasses();
		processRelations();
		return entities.values();
	}

	public Map<UMLClass, EntityDescriptor> getEntitiesMappedToCorrespondingUMLClass() {
		processClasses();
		processRelations();
		return entities;
	}

	private void processClasses() {
		entities = new HashMap<UMLClass, EntityDescriptor>();
		enumerations = new  HashMap<UMLClass, EnumDescriptor>();
		for (UMLClass umlClass : classes) {
			if (umlClass.isEnumeration()) {
				convertUMLClassToEnumeration(umlClass);
			} else {
				convertUMLClassToEntityDescriptor(umlClass);
			}
		}
	}

	private void processRelations() {
		for (UMLRelation relation : relations) {
			if (relation.getRightRole().equalsIgnoreCase(ENTITY_CLASS_NAME)) {
				return;
			}
			if (relation.getTarget().isEnumeration()) {
				createEnumerationRelation(relation);
			} else if (relation.isOneToOne())
				createOneToOneRelation(relation);
			else if (relation.isOneToMany()) {
				createOneToManyRelation(relation);
			} else if (relation.isManyToOne()) {
				createManyToOneRelation(relation);
			} else if (relation.isManyToMany()) {
				createManyToManyRelation(relation);
			} else {
				throw new UMLException("Unknown relation. Did you forget a property on the relation : " + relation + " ?");
			}
		}
	}


	/**
	 * Take an UML Class from GWTUml and convert it in an entity class, the based class for the generator.
	 * @param umlClass the class source
	 * @return An entity class used by the generator.
	 */
	public void convertUMLClassToEntityDescriptor(UMLClass umlClass) {
		EntityDescriptor entity = new EntityDescriptor(packageName, umlClass.getName());
		
		ArrayList<UMLClassAttribute> attributes = umlClass.getAttributes();
		
		for(UMLClassAttribute attribute : attributes) {
			addAttribute(entity, attribute);
		}
		
		entities.put(umlClass, entity);
	}
	
	/**
	 * Create an EnumDescriptor from the umlClass and add it to the list of enumerations. 
	 * 
	 * @param umlClass
	 */
	void convertUMLClassToEnumeration(UMLClass umlClass) {
		EnumDescriptor enumDesc = new EnumDescriptor(packageName, umlClass.getName());
		
		ArrayList<UMLClassAttribute> attributes = umlClass.getAttributes();
		
		for (UMLClassAttribute attribute : attributes) {
			String enumValue = attribute.getName();
			enumDesc.addConstant(enumValue.toUpperCase(), enumValue.toLowerCase());
		}
		
		enumerations.put(umlClass, enumDesc);
	}
	
	/** Create a one to one relationship between two entities from the given relation.
	 * @param relation the relation OneToOne between two entities
	 * @see com.objetdirect.gwt.gen.server.services#convertUMLClassToEntityDescriptor
	 */
	void createOneToOneRelation(UMLRelation relation) {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			if (relation.isLeftOwner()) {
				leftEntity.addOneToOne(rightEntity, relation.getRightRole(), false, false, false, relation.getLeftRole(), false, false, false);
			} else {
				 rightEntity.addOneToOne(leftEntity, relation.getLeftRole(), false, false, false, relation.getRightRole(), false, false, false);
			}
		} else {
			if (relation.isLeftOwner()) {
				leftEntity.addOneToOne(rightEntity, relation.getRightRole(), false, relation.isAComposition(), false);
			} else
			{
				rightEntity.addOneToOne(leftEntity, relation.getLeftRole(), false, relation.isAComposition(), false);
			}
		}
	}
	
	/** Create a one to many relationship between two entities from the given relation.
	 * @see com.objetdirect.gwt.gen.server.services#convertUMLClassToEntityDescriptor
	 * @param entities Is a map of couple {UmlClass, Entity counterPart} 
	 * @param relation the relation OneToOne between two entities
	 */
	void createOneToManyRelation(UMLRelation relation) throws UMLException {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			throw new UMLException("Bidirectional one-to-many relation is not supported.");
		} 
		// Unidirectional
		else {
			if (relation.isLeftOwner()) {
				leftEntity.addOneToMany(rightEntity, relation.getRightRole(), false);
			} else {
				rightEntity.addOneToMany(leftEntity, relation.getLeftRole(), false);
			}
		}
	}


	/** Create a many to one relationship between two entities from the given relation.
	 * @see com.objetdirect.gwt.gen.server.services#convertUMLClassToEntityDescriptor
	 * @param entities Is a map of couple {UmlClass, Entity counterPart} 
	 * @param relation the relation OneToOne between two entities
	 */
	void createManyToOneRelation(UMLRelation relation) throws UMLException {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			if (relation.isLeftOwner()) {
				leftEntity.addManyToOne(rightEntity, relation.getRightRole(), false, false, relation.getLeftRole(), false);
			} 
			else {
				rightEntity.addManyToOne(leftEntity, relation.getLeftRole(), false, false, relation.getRightRole(), false);
			}
		} 
		// Unidirectional
		else {
			if (relation.isLeftOwner()) {
				leftEntity.addManyToOne(rightEntity, relation.getRightRole(), false, false);
			} else {
				rightEntity.addManyToOne(leftEntity, relation.getLeftRole(), false, false);
			}
		}
	}

	/** Create a many to many relationship between two entities from the given relation.
	 * @see com.objetdirect.gwt.gen.server.services#convertUMLClassToEntityDescriptor
	 * @param entities Is a map of couple {UmlClass, Entity counterPart} 
	 * @param relation the relation OneToOne between two entities
	 */
	void createManyToManyRelation(UMLRelation relation) {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			if (relation.isLeftOwner()) {
				leftEntity.addManyToMany(rightEntity, relation.getRightRole(), false, relation.getLeftRole(), false);
			} else {
				rightEntity.addManyToMany(rightEntity, relation.getLeftRole(), false, relation.getRightRole(), false);
			}
		}
		// Unidirectional
		else {
			if (relation.isLeftOwner()) {
				leftEntity.addManyToMany(rightEntity, relation.getRightRole(), false);
			} else {
				rightEntity.addManyToMany(leftEntity, relation.getLeftRole(), false);
			}
		}
	}
	
	/**
	 * @param relation
	 */
	void createEnumerationRelation(UMLRelation relation) {
		EntityDescriptor entity  = entities.get(relation.getOwner());
		EnumDescriptor enumType = enumerations.get(relation.getTarget());
		String name = relation.getRightRole();
		
		entity.addEnumField(name, enumType, null);
	}
	
	/**
	 * Add an attribute to the entity depending of the type of the given UmlClassAttribute
	 * @param entity the target entity where the attribute will be add.
	 * @param attribute	the attribute to add to the entity
	 */
	void addAttribute(EntityDescriptor entity, UMLClassAttribute attribute) {
		String name = attribute.getName();
		String type = attribute.getType();
		
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
			
		case DATE :
			entity.addDateField(name, null);

		default:
			break;
		}
	}
}
