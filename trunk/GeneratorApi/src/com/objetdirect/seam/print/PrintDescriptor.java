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

package com.objetdirect.seam.print;

import com.objetdirect.seam.DocumentDescriptor;
import com.objetdirect.seam.DocumentFeature;
import com.objetdirect.seam.FaceletDescriptor;

public class PrintDescriptor extends DocumentDescriptor {
	
	public PrintDescriptor(
		String classPackageName, String className, 
		String viewPackageName, String viewName) 
	{
		super(classPackageName, className, viewPackageName, viewName);
	}

	@Override
	protected FaceletDescriptor buildFacelet(String packageName, String name) {
		FaceletDescriptor facelet = new FaceletDescriptor(packageName, name, defaultContentFacelet) {
			public String[] getText() {
				removeLine("/// page content here");
				return super.getText();
			}
		};
		facelet.replace("beanName", getBeanName());
		return facelet;
	}

	public void setFeature(DocumentFeature feature) {
		if (!(feature instanceof PrintFeature))
			throw new ClassCastException();
		super.setFeature(feature);
	}
	
	protected String getScopeType() {
		return "ScopeType.EVENT";
	}
	
	String[] defaultContentFacelet={
		"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
		"		\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
		"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
		"		xmlns:s=\"http://jboss.com/products/seam/taglib\"",
		"		xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
		"		xmlns:f=\"http://java.sun.com/jsf/core\"",
		"		xmlns:p=\"http://jboss.com/products/seam/pdf\">",
		"",
		"<p:document>",
		"	<f:facet name=\"footer\">",
		"		<p:font size=\"10\">",
		"			<p:footer alignment=\"right\">",
		"				<p:pageNumber />",
		"			</p:footer>",
		"		</p:font>",
		"	</f:facet>",
		"	/// page content here",
		"</p:document>",
		"",
		"</ui:composition>"
	};

}
