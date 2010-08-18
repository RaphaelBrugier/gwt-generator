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

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.print.PrintDescriptor;
import com.objetdirect.seam.print.PrintListDescriptor;

public class TestPrintList extends TestCase {

	public static String[] testSimpleListJavaText = {
		"package com.objetdirect.actions;",
		"",
		"import com.objetdirect.domain.Agency;",
		"import java.util.List;",
		"import org.jboss.seam.annotations.Name;",
		"import org.jboss.seam.annotations.Scope;",
		"import org.jboss.seam.ScopeType;",
		"",
		"@Name(\"printAgencies\")",
		"@Scope(ScopeType.EVENT)",
		"public class PrintAgenciesAnimator {",
		"",
		"	List<Agency> agencies = null;",
		"",
		"",
		"	public List<Agency> getAgencies() {",
		"		return agencies;",
		"	}",
		"	",
		"	public void setAgencies(List<Agency> agencies) {",
		"		this.agencies = agencies;",
		"	}",
		"",
		"}"
	};
	
	public static String[] testSimpleListFaceletText = {
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
		"	<p:table columns=\"3\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Name\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Phone\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"E-mail\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<ui:repeat value=\"#{printAgencies.agencies}\" var=\"agency\">",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{agency.name}\" />",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{agency.phone}\" />",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{agency.email}\" />",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"		</ui:repeat>",
		"	</p:table>",
		"</p:document>",
		"",
		"</ui:composition>"
	};
	
	public void testSimpleList() {	
		Seam.clear();
		EntityDescriptor agency = new EntityDescriptor("com.objetdirect.domain", "Agency").
			addStringField("name", null).
			addStringField("phone", null).
			addStringField("email", null);
		PrintDescriptor page = 
			new PrintDescriptor("com.objetdirect.actions", "PrintAgencies", "views", "print-agencies");
		PrintListDescriptor feature = new PrintListDescriptor(agency).
			showField("name", "Name", 20).
			showField("phone", "Phone", 10).
			showField("email", "E-mail", 20);
		page.setFeature(feature);
		page.build();
		
		TestUtil.assertText(page.getJavaText(), testSimpleListJavaText);
		TestUtil.assertText(page.getFaceletText(), testSimpleListFaceletText);
	}

	
	public static String[] testReadOnlyFieldsFaceletText = {
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
		"	<p:table columns=\"6\" headerRows=\"1\"  spacingBefore=\"1\" spacingAfter=\"10\">",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"String field\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Int field\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Date field\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Boolean field\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Enum field\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<p:cell>",
		"			<p:font name=\"times\" style=\"bold\" size=\"8\">",
		"				<p:paragraph>",
		"					<p:text value=\"Link field\" />",
		"				</p:paragraph>",
		"			</p:font>",
		"		</p:cell>",
		"		<ui:repeat value=\"#{printAgencies.dummies}\" var=\"dummy\">",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{dummy.stringField}\" />",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{dummy.intField}\">",
		"							<f:convertNumber pattern=\"####\"/>",
		"						</p:text>",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{dummy.dateField}\">",
		"							<f:convertDateTime pattern=\"dd/MM/yyyy\"/>",
		"						</p:text>",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{dummy.boolField?'TRUE':'FALSE'}\"/>",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{dummy.enumField.label}\"/>",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"			<p:cell colspan=\"1\">",
		"				<p:font name=\"times\" size=\"10\" style=\"bold\">",
		"					<p:paragraph>",
		"						<p:text value=\"#{dummy.linkField.name}\"/>",
		"					</p:paragraph>",
		"				</p:font>",
		"			</p:cell>",
		"		</ui:repeat>",
		"	</p:table>",
		"</p:document>",
		"",
		"</ui:composition>"	
	};
	
	
	public void testReadOnlyFields() {
		Seam.clear();
		EnumDescriptor enumDesc = new EnumDescriptor("com.objetdirect.domain", "DummyEnum").
			addConstant("A", "first letter").
			addConstant("B", "second letter").
			addConstant("Z", "last letter");
		EntityDescriptor linkedDummy = new EntityDescriptor("com.objetdirect.domain", "LinkedDummy").
			addStringField("name", null);
		EntityDescriptor dummy = new EntityDescriptor("com.objetdirect.domain", "Dummy").
			addStringField("stringField", null).
			addIntField("intField", null).
			addDateField("dateField", null).
			addBooleanField("boolField", null).
			addEnumField("enumField", enumDesc, null).
			addManyToOne(linkedDummy, "linkField", false, false);
		PrintDescriptor page = 
			new PrintDescriptor("com.objetdirect.actions", "PrintAgencies", "views", "print-agencies");
		PrintListDescriptor feature = new PrintListDescriptor(dummy);
		feature.
			showField("stringField", "String field", 20).
			showNumberField("intField", "Int field", "####", 20).
			showDateField("dateField", "Date field", "dd/MM/yyyy").
			showBooleanField("boolField", "Boolean field", "TRUE", "FALSE").
			showEnumField("enumField", "Enum field", 15).
			showEntityField("linkField", "Link field", "name", 15);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getFaceletText(), testReadOnlyFieldsFaceletText);
	}
	
}

