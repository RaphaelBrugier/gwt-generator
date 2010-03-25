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

import com.objetdirect.engine.TestUtil;

import junit.framework.TestCase;

public class TestConfirmPopup extends TestCase {

	public void testSimpleConfirmPopup() {
	
		Seam.clear();
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "Processor", "views", "processor");
		page.build();
		ConfirmPopupDescriptor popup = new ConfirmPopupDescriptor();
		popup.setOwner(page);
		popup.addOperation("DOTHIS", new String[] {"doThis(target);"}, "Voulez-vous effectuer l'operation dothis : #{processor.target.name} ?");
		popup.addOperation("DOTHAT", new String[] {"doThat();"}, "Voulez-vous effectuer l'operation dothat ?");
		popup.buildJavaPart();
		popup.buildFaceletPart();
		TestUtil.assertExists(page.getJavaText(), 
			"static final int OP_NONE = 0;",
			"static final int OP_DOTHIS = 1;",
			"static final int OP_DOTHAT = 2;",
			"int operation = OP_NONE;"
		);
		TestUtil.assertExists(page.getJavaText(), 
			"public int getOperation() {",
			"	return operation;",
			"}",
			"",
			"public void setOperation(int operation) {",
			"	this.operation = operation;",
			"}",
			"",
			"public void cancelOperation() {",
			"	operation = OP_NONE;",
			"}",
			"",
			"public void continueOperation() {",
			"	if (operation == OP_DOTHIS) {",
			"		doThis(target);",
			"	}",
			"	else if (operation == OP_DOTHAT) {",
			"		doThat();",
			"	}",
			"	operation = OP_NONE;",
			"}",
			"",
			"public boolean isConfirmRequested() {",
			"	return operation != OP_NONE;",
			"}",
			"",
			"public void setConfirmRequested(boolean confirmRequested) {",
			"	if (!confirmRequested) {",
			"		cancelOperation();",
			"	}",
			"}"
		);
		TestUtil.assertText(popup.getFragment().getText(), 
			"<ice:panelPopup modal=\"true\" draggable=\"true\" styleClass=\"popup\" rendered=\"#{processor.confirmRequested}\">",
			"	<f:facet name=\"header\">",
			"		<ice:panelGrid styleClass=\"title\" cellpadding=\"0\" cellspacing=\"0\" columns=\"2\">",
			"			<ice:outputText value=\"Confirmation\"/>",
			"		</ice:panelGrid>",
			"	</f:facet>",
			"	<f:facet name=\"body\">",
			"		<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
			"			<ice:outputText value=\"Voulez-vous effectuer l'operation dothis : #{processor.target.name} ?\" rendered=\"#{processor.operation==1}\"/>",
			"			<ice:outputText value=\"Voulez-vous effectuer l'operation dothat ?\" rendered=\"#{processor.operation==2}\"/>",
			"			<div class=\"actionButtons\">",
			"				<h:commandButton value=\"valider\" action=\"#{processor.continueOperation}\"/>",
			"				<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{processor.cancelOperation}\"/>",
			"			</div>",
			"		</ice:panelGrid>",
			"	</f:facet>",
			"</ice:panelPopup>"
		);
	}
	
}
