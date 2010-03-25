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

public class ConstructorDescriptor extends CodeDescriptor {

	static final String[] defaultPattern = new String[] {
		"/// annotations here",
		"modifier className(paramType paramName) {",
		"	/// content here",
		"}"
	};
	
	public ConstructorDescriptor() {	
	}
	
	public ConstructorDescriptor init(BasicClassDescriptor owner, String ... pattern) {
		super.init(owner, (pattern==null || pattern.length==0) ? defaultPattern : pattern);
		setPattern(Rewrite.replace(getPattern(), "className", owner.getTypeName()));
		return this;
	}

}
