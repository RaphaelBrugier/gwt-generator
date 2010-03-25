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

public class ScriptDescriptor {

	String[] script;
	ClassDescriptor owner;
	List<Object[]> replacements = new ArrayList<Object[]>();
	
	public ScriptDescriptor setOwner(ClassDescriptor owner) {
		this.owner = owner;
		return this;
	}
	
	public ScriptDescriptor(String ... script) {
		this.script = script;
	}
	
	public ScriptDescriptor replace(String ... replacements) {
		for (int i=0; i<replacements.length; i+=2){
			this.replacements.add(new Object[] {replacements[i], replacements[i+1]});
		}
		return this;
	}
	
	public ScriptDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		replacements.add(new Object[] {replaceThis, byThis});
		return this;
	}
	
	public String[] getText() {
		for (Object[] replacement : replacements) {
			if (replacement[1] instanceof TypeDescriptor) {
				owner.getImportSet().addType((TypeDescriptor)replacement[1]);
				script = Rewrite.replace(script, (String)replacement[0], 
					((TypeDescriptor)replacement[1]).getUsageName(owner));
			}
			else {
				script = Rewrite.replace(script, (String)replacement[0], 
					replacement[1]==null ? "null" : replacement[1].toString());
			}
		}
		return script;
	}
}
