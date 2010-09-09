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
package com.objetdirect.seam.fieldrenderers;

import com.objetdirect.seam.fields.BooleanField;
import com.objetdirect.seam.fields.DateField;
import com.objetdirect.seam.fields.EntityField;
import com.objetdirect.seam.fields.EnumField;
import com.objetdirect.seam.fields.NumberField;
import com.objetdirect.seam.fields.StringField;

/**
 * @author Raphael Brugier <raphael dot brugier at gmail dot com >
 */
public interface HasFields {

	public void setStringField(StringField stringField);
	
	public void setNumberField(NumberField numberField);
	
	public void setDateField(DateField dateField);
	
	public void setBooleanField(BooleanField booleanField);
	
	public void setEnumField(EnumField enumField);
	
	public void setEntityField(EntityField entityField);
}
