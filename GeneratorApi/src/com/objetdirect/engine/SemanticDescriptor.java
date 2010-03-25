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

public class SemanticDescriptor {

	String request;
	Object[] params;
	
	public SemanticDescriptor(String request, Object ... params) {
		this.request = request;
		this.params = params;
	}
	
	@Override
	public int hashCode() {
		int result = request.hashCode()^params.length;
		for (Object param : params) {
			result ^= param.hashCode();
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SemanticDescriptor semantic = (SemanticDescriptor) obj;
		if (!request.equals(semantic.request))
			return false;
		if (params.length != semantic.params.length)
			return false;
		for (int i=0; i<params.length; i++) {
			if (params[i]==semantic.params[i])
				continue;
			if (params[i]==null || semantic.params[i]==null)
				return false;
			if (!params[i].equals(semantic.params[i]))
				return false;
		}
		return true;
	}
	
	public String getRequest() {
		return request;
	}
	
	public Object getParam(int param) {
		return params[param];
	}
}
