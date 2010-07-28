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

public class Rewrite {

	public static String[] replace(String[] text, String replaceThis, String byThis) {
		String[] result = new String[text.length];
		for (int i=0; i<result.length; i++)
			result[i] = replace(text[i], replaceThis, byThis);
		return result;
	}

	public static String[] replace(String[] text, String ... replacements) {
		String[] result = new String[text.length];
		for (int i=0; i<result.length; i++)
			result[i] = replace(text[i], replacements);
		return result;
	}
	
	public static String replace(String text, String replaceThis, String byThis) {
		int i = 0;
		int n = text.indexOf(replaceThis, i);
		while (n!=-1) {
			text = text.substring(0, n)+byThis+text.substring(n+replaceThis.length());
			i=n+byThis.length();
			n = text.indexOf(replaceThis, i);
		}
		return text;
	}
	
	public static String replace(String text, String ... replacements) {
		for (int i=0; i<replacements.length; i+=2) {
			text = replace(text, replacements[i], replacements[i+1]);
		}
		return text;
	}
	
	public static String remove(String text, String removeThis) {
		return replace(text, removeThis, "");
	}

	public static String[] remove(String[] text, String removeThis) {
		return replace(text, removeThis, "");
	}
	
	public static String[] indent(String[] text, int tabCount) {
		String tabs="";
		for (int i=0; i<tabCount; i++)
			tabs+='\t';
		
		String[] result = new String[text.length];
		for (int i=0; i<result.length; i++)
			result[i] = tabs+text[i];
		return result;
	}

	public static String[] insertLines(String[] text, String beforeThis, String ... insertThis) {
		List<String> result = new ArrayList<String>();
		for (String s : text) {
			if (s.trim().equals(beforeThis)) {
				int n = s.indexOf(beforeThis);
				for (int i=0; i<insertThis.length; i++)
					result.add(s.substring(0, n)+insertThis[i]);
			}
			result.add(s);
		}
		return toArray(result);
	}

	public static String[] replaceAndInsertLines(String text, String replaceThis, String ... byThis) {
		List<String> result = new ArrayList<String>();
		String indent = text.substring(0, text.indexOf(text.trim())); 
		int n = text.indexOf(replaceThis, 0);
		String segment=null;
		while (n!=-1) {
			if (segment==null)
				segment = text.substring(0, n)+byThis[0];
			else
				segment += text.substring(0, n)+byThis[0];
			if(byThis.length>1) {
				result.add(segment);
				for (int l=1; l<byThis.length-1; l++)
					result.add(indent+byThis[l]);
				segment = indent+byThis[byThis.length-1];
			}
			text = text.substring(n+replaceThis.length());
			n = text.indexOf(replaceThis, 0);
		}
		if (segment==null)
			result.add(text);
		else
			result.add(segment+text);
		return toArray(result);
	}
	
	public static String[] replaceAndInsertLines(String[] text, String replaceThis, String ... byThis) {
		List<String> result = new ArrayList<String>();
		for (String line : text) {
			String[] lines = replaceAndInsertLines(line, replaceThis, byThis);
			for (String s : lines) {
				result.add(s);
			}
		}
		return toArray(result);
	}
	
	public static String[] removeLine(String[] text, String removeThis) {
		List<String> result = new ArrayList<String>();
		for (String s : text) {
			if (s.indexOf(removeThis)==-1) {
				result.add(s);
			}
		}
		return toArray(result);
	}

	public static String[] toArray(List<String> result) {
		String[] newText = new String[result.size()];
		int i=0;
		for (String s : result) {
			newText[i++]=s;
		}
		return newText;
	}

	public static String[] array(String ... lines) {
		return lines;
	}

	public static String[] removeLines(String[] text, String ... toRemove) {
		List<String> result = new ArrayList<String>();
		for (int i=0; i<text.length; i++) {
			if (matches(text, toRemove, i)) {
				i=i+toRemove.length-1;
			}
			else result.add(text[i]);
		}
		return toArray(result);
	}
	
	static boolean matches(String[] text, String[] pattern, int r) {
		if (r+pattern.length>=text.length)
			return false;
		for (int i=0; i<pattern.length; i++) {
			if (!pattern[i].trim().equals(text[r+i].trim()))
				return false;
		}
		return true;
	}

	public static String[] dup(String[] content) {
		String[] result = new String[content.length];
		for (int i=0; i<content.length; i++)
			result[i] = content[i];
		return result;
	}
	
}
