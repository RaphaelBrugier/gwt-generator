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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.entities.EntityDescriptor;

public class CriteriaProcessor {

	public Slot process(
		EntityDescriptor entity, 
		List<FieldRendererDescriptor> fields,
		List<AttributeDescriptor> attributes) 
	{
		List<CriteriaElement> elements = new ArrayList<CriteriaElement>();
		for (int i=0; i<fields.size(); i++) {
			elements.add(new CriteriaElement(fields.get(i).getFieldPath(), attributes.get(i)));
		}
		return process(entity, elements);
	}
	
	public Slot process(EntityDescriptor entity, List<CriteriaElement> elements) {
		Slot root = new Slot(entity);
		int c=1;
		for (CriteriaElement element : elements) {
			Slot parent = root;
			StringTokenizer st = new StringTokenizer(element.path, ".");
			int nb = st.countTokens();
			for (int i=0; i<nb-1; i++) {
				String segment = st.nextToken();
				if (parent.getSlot(segment)!=null)
					parent = parent.getSlot(segment);
				else {
					Slot slot = new Slot(c++);
					parent.addSlot(slot, segment);
					parent = slot;
				}
			}
			parent.addAttribute(element.attribute, st.nextToken());
		}
		return root;
	}
	
	public static class Slot {
		
		public Slot(EntityDescriptor entity) {
			this.entity = entity;
		}
		
		public Slot(int number) {
			this.number = number;
		}
		
		public Slot getSlot(String segment) {
			return slots.get(segment);
		}
		
		public void addSlot(Slot slot, String segment) {
			slots.put(segment, slot);
		}
		
		public void addAttribute(AttributeDescriptor attr, String segment) {
			attributes.put(segment, attr);
		}
		
		public List<AttributeDescriptor> getInvolvedAttributes() {
			List<AttributeDescriptor> result = new ArrayList<AttributeDescriptor>();
			result.addAll(collectInvolvedAttributes());
			Collections.sort(result, new Comparator<AttributeDescriptor>() {
				public int compare(
						AttributeDescriptor o1,
						AttributeDescriptor o2) 
				{
					return o1.getName().compareTo(o2.getName());
				}
			});
			return result;
		}
		
		protected Set<AttributeDescriptor> collectInvolvedAttributes() {
			Set<AttributeDescriptor> result = new HashSet<AttributeDescriptor>();
			result.addAll(attributes.values());
			for (Slot slot : slots.values()) {
				result.addAll(slot.collectInvolvedAttributes());
			}
			return result;
		}
		
		public int number;
		public EntityDescriptor entity;
		public Map<String, AttributeDescriptor> attributes = new HashMap<String, AttributeDescriptor>();
		public Map<String, Slot> slots = new HashMap<String, Slot>(); 
	}
	
	public static class CriteriaElement {
		
		public CriteriaElement(String path, AttributeDescriptor attribute) {
			this.path = path;
			this.attribute = attribute;
		}
		
		public String path;
		public AttributeDescriptor attribute;
	}
	
}
