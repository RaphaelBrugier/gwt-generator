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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.TypeDescriptor;

public class Seam {

	public static final TypeDescriptor Conversation = new TypeDescriptor("org.jboss.seam.core", "Conversation"); 
	public static final TypeDescriptor Name = new TypeDescriptor("org.jboss.seam.annotations", "Name"); 
	public static final TypeDescriptor Scope = new TypeDescriptor("org.jboss.seam.annotations", "Scope"); 
	public static final TypeDescriptor In = new TypeDescriptor("org.jboss.seam.annotations", "In"); 
	public static final TypeDescriptor ScopeType = new TypeDescriptor("org.jboss.seam", "ScopeType");
	public static final TypeDescriptor Component = new TypeDescriptor("org.jboss.seam", "Component");
	
	public static String getName(String packageName, String name) {
		name = NamingUtil.toMember(name);
		if (!names.contains(name)) {
			names.add(name);
			return name;
		}
		String candidate = name;
		if (packageName!=null) {
			StringTokenizer st = new StringTokenizer(packageName, "/");
			List<String> prefixes = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				prefixes.add(st.nextToken());
			}
			for (int i=prefixes.size()-1; i>=0; i--) {
				candidate = NamingUtil.toMember(prefixes.get(i))+NamingUtil.toProperty(candidate);
				if (!names.contains(candidate)) {
					names.add(candidate);
					return candidate;
				}
			}
		}
		int i=1;
		while (true) {
			if (!names.contains(candidate+i)) {
				names.add(candidate+i);
				return candidate+i;
			}
			i++;
		}
	}
	
	static Set<String> names = new HashSet<String>();
	
	public static final void clear() {
		names.clear();
	}
}
