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

package com.objetdirect.engine;

import java.util.ArrayList;
import java.util.List;

public class ConstructorSet {

	BasicClassDescriptor owner;
	List<ConstructorDescriptor> constructors = new ArrayList<ConstructorDescriptor>();
	
	public ConstructorSet(BasicClassDescriptor owner) {
		this.owner = owner;
	}
	
	public ConstructorSet addConstructor(ConstructorDescriptor constructor) {
		constructors.add(constructor);
		return this;
	}
	
	public String[] getText() {
		String[] text = {
			"/// constructors here"
		};
		boolean first = true;
		for (ConstructorDescriptor constructor : constructors) {
			if (first) 
				first = false;
			else
				text = Rewrite.insertLines(text, "/// constructors here", "");
			text = Rewrite.insertLines(text, "/// constructors here", constructor.getText());
		}
		return Rewrite.removeLine(text, "/// constructors here");
	}
}
