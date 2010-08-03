/*
 * This file is part of the Gwt-Generator project and was written by Raphaël Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.server.gen.seamMM;

/**
 * A wrapper for a Date field in the Seam generation metamodel.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class DateField {
	
	public final String fieldName;
	public final String fieldTitle;
	public final String pattern;
	
	/**
	 * @param fieldName
	 * @param fieldTitle
	 * @param pattern
	 */
	public DateField(String fieldName, String fieldTitle, String pattern) {
		this.fieldName = fieldName;
		this.fieldTitle = fieldTitle;
		this.pattern = pattern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((fieldTitle == null) ? 0 : fieldTitle.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
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
		DateField other = (DateField) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (fieldTitle == null) {
			if (other.fieldTitle != null)
				return false;
		} else if (!fieldTitle.equals(other.fieldTitle))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DateField [fieldName=" + fieldName + ", fieldTitle=" + fieldTitle + ", pattern=" + pattern + "]";
	}
}
