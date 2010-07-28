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

import junit.framework.TestCase;

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.print.PrintDescriptor;
import com.objetdirect.seam.print.PrintEntityDescriptor;
import com.objetdirect.seam.print.PrintFormDescriptor;
import com.objetdirect.seam.print.PrintInternalListDescriptor;

public class TestPrintEntity extends TestCase {

	public static String[] testSimpleEntityJavaText = {
		"package com.objetdirect.actions;",
		"",
		"import com.objetdirect.domain.Agency;",
		"import org.jboss.seam.annotations.Name;",
		"import org.jboss.seam.annotations.Scope;",
		"import org.jboss.seam.ScopeType;",
		"",
		"@Name(\"printAgency\")",
		"@Scope(ScopeType.EVENT)",
		"public class PrintAgencyAnimator {",
		"",
		"	Agency currentAgency = null;",
		"",
		"",
		"	public Agency getCurrentAgency() {",
		"		return currentAgency;",
		"	}",
		"	",
		"	public void setCurrentAgency(Agency currentAgency) {",
		"		this.currentAgency = currentAgency;",
		"	}",
		"",
		"}"};
	
	public static String[] testSimpleEntityFaceletText = {
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
		"	<p:table columns=\"4\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Name\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"				<p:paragraph>",
		"					<p:text value=\"#{printAgency.currentAgency.name}\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Phone\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"				<p:paragraph>",
		"					<p:text value=\"#{printAgency.currentAgency.phone}\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"E-mail\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"				<p:paragraph>",
		"					<p:text value=\"#{printAgency.currentAgency.email}\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"	</p:table>",
		"</p:document>",
		"",
		"</ui:composition>"
	};
	
	public void testSimpleEntity() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		PrintDescriptor page = 
			new PrintDescriptor("com.objetdirect.actions", "PrintAgency", "views", "print-agency");
		PrintEntityDescriptor feature = new PrintEntityDescriptor(agency).
			addElement(new PrintFormDescriptor().
				showField("name", "Name", 20).
				showField("phone", "Phone", 10).
				showField("email", "E-mail", 20)
		);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
				testSimpleEntityJavaText
		);
		TestUtil.assertText(page.getFaceletText(),
				testSimpleEntityFaceletText
		);
	}
	
	public static String[] testFullFeaturedEntityFaceletText = {
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
		"	<p:table columns=\"4\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Name\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"				<p:paragraph>",
		"					<p:text value=\"#{printAgency.currentAgency.name}\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Phone\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"				<p:paragraph>",
		"					<p:text value=\"#{printAgency.currentAgency.phone}\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"E-mail\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell border=\"0\">",
		"			<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"				<p:paragraph>",
		"					<p:text value=\"#{printAgency.currentAgency.email}\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"	</p:table>",
		"	<p:table columns=\"2\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"First Name\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Last Name\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<ui:repeat value=\"#{printAgency.employees}\" var=\"employee\">",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{employee.firstName}\" />",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{employee.lastName}\" />",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"		</ui:repeat>",
		"	</p:table>",
		"</p:document>",
		"",
		"</ui:composition>"
	};

	public void testFullFeaturedEntity() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		EntityDescriptor employee = new EntityDescriptor("com.objetdirect.domain", "Employee").
			addStringField("firstName", null).
			addStringField("lastName", null);
		
		agency.addOneToMany(employee, "employees", false);
		PrintDescriptor page = 
			new PrintDescriptor("com.objetdirect.actions", "PrintAgency", "views", "print-agency");
		PrintEntityDescriptor feature = new PrintEntityDescriptor(agency)
		.addElement(new PrintFormDescriptor().
			showField("name", "Name", 20).
			showField("phone", "Phone", 10).
			showField("email", "E-mail", 20))
		.addElement(new PrintInternalListDescriptor("employees").
			showField("firstName", "First Name", 20).
			showField("lastName", "Last Name", 10)
		);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getFaceletText(), testFullFeaturedEntityFaceletText
		);
	}	
}

