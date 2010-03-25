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

import java.util.StringTokenizer;

public class NamingUtil {

	public static String toProperty(String fieldName) {
		if (fieldName.length()==0)
			throw new GeneratorException("A name must contain at least one character");
		if (fieldName.length()==1)
			return fieldName.toUpperCase();
		else
			return Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
	}

	public static String toMember(String propertyName) {
		if (propertyName.length()==0)
			throw new GeneratorException("A name must contain at least one character");
		if (propertyName.length()==1)
			return propertyName.toLowerCase();
		else
			return Character.toLowerCase(propertyName.charAt(0))+propertyName.substring(1);
	}

	public static String toSingular(String name) {
		if (name.endsWith("ies")) {
			if (name.length()>3)
				return name.substring(0, name.length()-3)+"y";
			else
				return "ie";
		}
		else if (name.endsWith("xes")) {
			if (name.length()>3)
				return name.substring(0, name.length()-2);
			else
				return "xe";
		}
		else if (name.endsWith("s")) {
			if (name.length()>1)
				return name.substring(0, name.length()-1);
			else
				return "s";
		}
		return name;
	}

	public static String toPlural(String name) {
		if (name.endsWith("y")) {
			if (name.length()>1)
				return name.substring(0, name.length()-1)+"ies";
			else
				return "ys";
		}
		else if (name.endsWith("x")) {
			if (name.length()>1)
				return name+"es";
			else
				return "xs";
		}
		else if (!name.endsWith("s"))
			return name+"s";
		else
			return name;
	}
	
	public static String extractMember(MethodDescriptor getter) {
		if (getter.getName().length()>3 && getter.getName().startsWith("get"))
			return toMember(getter.getName().substring(3));
		else if (getter.getName().length()>2 && getter.getName().startsWith("is"))
			return toMember(getter.getName().substring(2));
		else
			throw new GeneratorException(getter.getName()+" is not a property getter");
	}

	public static String pathToName(String path) {
		StringTokenizer st = new StringTokenizer(path, ".");
		String result = null;
		while (st.hasMoreTokens()) {
			if (result==null)
				result = toProperty(st.nextToken());
			else
				result+= toProperty(st.nextToken());
		}
		return result;
	}

	public static String toConst(String attrName) {
		String result = "";
		String token = "";
		boolean isUpper = false;
		Character lastUpper = null;
		for (int i=0; i<attrName.length(); i++) {
			char c = attrName.charAt(i);
			if (Character.isUpperCase(c) || Character.isDigit(c)) {
				if (isUpper) {
					token+=lastUpper;
					lastUpper = c;
				}
				else {
					if (result.length()>0)
						result+="_";
					result+=token.toUpperCase();
					token="";
					lastUpper = c;
					isUpper = true;
				}
			}
			else {
				if (isUpper) {
					if (token.length()>0) {
						if (result.length()>0)
							result+="_";
						result+=token.toUpperCase();
						token = ""+lastUpper;
					}
					else {
						token=""+lastUpper;
						isUpper=false;
					}
				}
				isUpper = false;
				token+=c;
			}
		}
		if (isUpper)
			token+=lastUpper;
		if (token.length()>0) {
			if (result.length()>0)
				result+="_";
			result+=token.toUpperCase();
		}
		return result;
	}
	
}
