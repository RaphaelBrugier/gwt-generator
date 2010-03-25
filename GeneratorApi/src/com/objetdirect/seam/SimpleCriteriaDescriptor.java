/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.seam;

import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.entities.EntityDescriptor;

public class SimpleCriteriaDescriptor implements CriteriaDescriptor {

	Component owner;
	
	public SimpleCriteriaDescriptor() {
	}

	public EntityDescriptor getEntity() {
		return getParent(EntityHolder.class).getEntityType();
	}
	
	public void buildJavaPart() {	
	}
	
	public void buildFaceletPart() {	
	}

	public String[] getInitText() {
		String[] pattern = {"entityManager.createQuery(\"from EntityClass\").getResultList()"};
		pattern = Rewrite.replace(pattern, "entityManager", getDocument().getEntityManager().getName());
		pattern = Rewrite.replace(pattern,"EntityClass", getEntity().getClassDescriptor().getTypeName());
		return pattern;
	}

	public void setOwner(Component owner) {
		this.owner = owner;
	}

	public ClassDescriptor getClassDescriptor() {
		return owner.getClassDescriptor();
	}

	public DocumentDescriptor getDocument() {
		return owner.getDocument();
	}

	@SuppressWarnings("unchecked")
	public <T> T getParent(Class<T> type) {
		if (type.isInstance(owner))
			return (T)owner;
		else
			return owner.getParent(type);
	}
	
	public FragmentDescriptor getFragment() {
		return null;
	}
}
