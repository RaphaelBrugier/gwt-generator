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
import java.util.StringTokenizer;

public class PathUtils {

	public static MethodDescriptor[] getGetters(ClassDescriptor clazz, String path) {
		StringTokenizer st = new StringTokenizer(path, ".");
		List<MethodDescriptor> getters = new ArrayList<MethodDescriptor>();
		while (st.hasMoreTokens()) {
			String field = st.nextToken();
			MethodDescriptor getter = clazz.getMethod("get"+NamingUtil.toProperty(field));
			if (getter==null)
				getter = clazz.getMethod("is"+NamingUtil.toProperty(field));
			if (getter == null)
				throw new GeneratorException("No getter for \""+field+"\" in path : "+path);
			if (getter.getReturnType() instanceof ClassDescriptor)
				clazz = (ClassDescriptor)getter.getReturnType();
			else if (st.hasMoreTokens())
				throw new GeneratorException("Invalid getter for \""+field+"\" in path : "+path);
			getters.add(getter);
		}
		return toArray(getters);
	}

	public static MethodDescriptor[] getSetters(ClassDescriptor clazz, String path) {
		StringTokenizer st = new StringTokenizer(path, ".");
		List<MethodDescriptor> accessors = new ArrayList<MethodDescriptor>();
		while (st.hasMoreTokens()) {
			String field = st.nextToken();
			if (st.hasMoreTokens()) {
				MethodDescriptor getter = clazz.getMethod("get"+NamingUtil.toProperty(field));
				if (getter==null)
					getter = clazz.getMethod("is"+NamingUtil.toProperty(field));
				if (getter == null)
					throw new GeneratorException("No getter for \""+field+"\" in path : "+path);
				if (getter.getReturnType() instanceof ClassDescriptor)
					clazz = (ClassDescriptor)getter.getReturnType();
				else
					throw new GeneratorException("Invalid getter for \""+field+"\" in path : "+path);
				accessors.add(getter);
			}
			else {
				MethodDescriptor setter = clazz.getMethod("set"+NamingUtil.toProperty(field));
				if (setter == null)
					throw new GeneratorException("No setter for \""+field+"\" in path : "+path);
				accessors.add(setter);
			}
		}
		return toArray(accessors);
	}

	static MethodDescriptor[] toArray(List<MethodDescriptor> getters) {
		int i=0;
		MethodDescriptor[] result = new MethodDescriptor[getters.size()];
		for (MethodDescriptor getter : getters) {
			result[i++]=getter;
		}
		return result;
	}

	public static String getGetterCalls(ClassDescriptor clazz, String path) {
		MethodDescriptor[] getters = getGetters(clazz, path);
		String result = null;
		for (MethodDescriptor getter : getters) {
			if (result==null)
				result = getter.getName()+"()";
			else
				result += "."+getter.getName()+"()";
		}
		return result;
	}

	public static String getSetterCalls(ClassDescriptor clazz, String path) {
		MethodDescriptor[] accessors = getSetters(clazz, path);
		String result = null;
		for (int i=0; i<accessors.length-1; i++) {
			if (result==null)
				result = accessors[i].getName()+"()";
			else
				result += "."+accessors[i].getName()+"()";
		}
		if (result!=null)
			result+="." + accessors[accessors.length-1].getName();
		else
			result = accessors[accessors.length-1].getName();
		return result;
	}
	
	public static MethodDescriptor getGetter(ClassDescriptor clazz, String path) {
		MethodDescriptor[] getters = getGetters(clazz, path);
		return getters[getters.length-1];
	}

	public static String getAccessPart(String path) {
		if (path.indexOf(".")==-1)
			return "";
		return path.substring(0, path.lastIndexOf("."));
	}

	public static AttributeDescriptor getAttribute(ClassDescriptor clazz, String path) {
		SemanticDescriptor semantic = getGetter(clazz, path).getSemantic();
		return semantic==null ? null : (AttributeDescriptor)semantic.getParam(0);
	}

	public static TypeDescriptor getType(ClassDescriptor clazz, String path) {
		AttributeDescriptor attr = getAttribute(clazz, path);
		return attr.getType();
	}
	
	public static String[] splitPath(String path) {
		StringTokenizer st = new StringTokenizer(path, ".");
		String[] pathes = new String[st.countTokens()];
		String currentPath = "";
		int t=0;
		while (st.hasMoreTokens()) {
			currentPath+=st.nextToken();
			pathes[t++]=currentPath;
			currentPath+=".";
		}
		return pathes;
	}
	
	public static class PathStore {
		public PathStore(String label) {
			this.label = label;
		}
		
		String label;
		List<PathStore> stores = new ArrayList<PathStore>();
		List<Object> content = new ArrayList<Object>();
		
		public String getLabel() {
			return label;
		}
		
		public Object getContent(int i) {
			return content.get(i);
		}
		
		public List<PathStore> getStores(){
			return stores;
		}
		
		public void store(String[] segments, int i, Object contained) {
			if (segments.length==i) {
				content.add(contained);
			}
			else {
				PathStore child = null;
				for (PathStore c : stores) {
					if (c.label.equals(segments[i]))
						child = c;
				}
				if (child==null) {
					child = new PathStore(segments[i]);
					stores.add(child);
				}
				child.store(segments, i+1, contained);
			}
		}
		
		public void simplify() {
			while (content.size()==0 && stores.size()==1) {
				PathStore child = stores.get(0);
				this.stores = child.stores;
				this.content = child.content;
				this.label = this.label+"."+child.label;
			}
			for (PathStore child : stores) {
				child.simplify();
			}
		}
		
		public String toString(String indent) {
			String result = indent+"["+label+"] {\n";
			for (Object c : content) {
				result+=indent+"\t"+c+"\n";
			}
			for (PathStore s : stores) {
				result+=s.toString(indent+"\t");
			}
			result+=indent+"}\n";
			return result;
		}
		
		public String toString() {
			return toString("");
		}
	}
	
	public static PathStore storePathes(Object ... pathes) {
		PathStore root = new PathStore("");
		for (int i=0; i<pathes.length; i+=2) {
			StringTokenizer st = new StringTokenizer((String)pathes[i], ".");
			String[] segments = new String[st.countTokens()];
			int t=0;
			while (st.hasMoreTokens()) {
				segments[t++] = st.nextToken();
			}
			root.store(segments, 0, pathes[i+1]);
		}
		root.simplify();
		return root;
	}

	public static PathStore storeOnlyPathes(String ... pathes) {
		PathStore root = new PathStore("");
		for (int i=0; i<pathes.length; i++) {
			StringTokenizer st = new StringTokenizer((String)pathes[i], ".");
			String[] segments = new String[st.countTokens()];
			int t=0;
			while (st.hasMoreTokens()) {
				segments[t++] = st.nextToken();
			}
			root.store(segments, 0, pathes[i]);
		}
		root.simplify();
		return root;
	}

	public static String getNotNullCondition(String currentInstance, ClassDescriptor currentClass, String path) {
		String[] pathes = splitPath(path);
		String pattern="condition";
		for (String apath : pathes) {
			String call = currentInstance+"."+getGetterCalls(currentClass, apath)+" != null && condition";
			pattern = Rewrite.replace(pattern, "condition", call);
		}
		pattern = Rewrite.remove(pattern, " && condition");
		return pattern;
	}
	
	static String[] getText(
		ClassDescriptor owner, 
		ClassDescriptor currentClass, 
		String currentInstance, 
		String basePath, String path,
		Object[] result,
		StoreFiller filler) 
	{
		String[] pattern = {
			"/// insert processing here",
			"/// insert sub text here",
		};
		result[0] = currentClass;
		result[1] = currentInstance;
		if (path.length()>0) {
			pattern = new String[] {
				"if (condition) {",
				"	TargetClass instanceName = currentInstance.getValue();",
				"	/// insert processing here",
				"	/// insert sub text here",
				"}"
			};
			String condition = getNotNullCondition(currentInstance, currentClass, path);
			pattern = Rewrite.replace(pattern, "condition", condition);
			TypeDescriptor type = getType(currentClass, path);
			owner.getImportSet().addType(type);
			String instanceName = currentInstance + NamingUtil.toProperty(type.getTypeName());
			pattern = Rewrite.replace(pattern, "TargetClass", type.getUsageName(owner));
			pattern = Rewrite.replace(pattern, "currentInstance", currentInstance);
			pattern = Rewrite.replace(pattern, "instanceName", instanceName);
			pattern = Rewrite.replace(pattern, "getValue()", getGetterCalls(currentClass, path));
			result[0] = type;
			result[1] = instanceName;
		}
		String[] op = filler.getText(owner, basePath, (ClassDescriptor)result[0], (String)result[1]);
		pattern = Rewrite.insertLines(pattern, "/// insert processing here", op);
		pattern = Rewrite.removeLine(pattern, "/// insert processing here");
		return pattern;
	}
	
	public static String[] getText(
		ClassDescriptor owner,
		String basePath,
		ClassDescriptor currentClass, 
		String currentInstance,
		PathUtils.PathStore store,
		StoreFiller filler) 
	{
		Object[] result = new Object[2];
		String[] pattern = getText(owner, currentClass, currentInstance, basePath, store.label, result, filler);
		for (PathUtils.PathStore child : store.stores) {
			
			String[] childPattern = getText(owner, 
				basePath.length()==0 ? child.label : basePath+"."+child.label, 
				(ClassDescriptor)result[0], (String)result[1], child, filler);
			pattern = Rewrite.insertLines(pattern, "/// insert sub text here", childPattern);
		}
		pattern = Rewrite.removeLine(pattern, "/// insert sub text here");
		return pattern;
	}
	
	public interface StoreFiller {
		String[] getText(
			ClassDescriptor owner, 
			String path,
			ClassDescriptor currentClass,
			String currentInstance);
	}
	
}
