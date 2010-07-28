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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.GeneratorException;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;

public class ConfirmPopupDescriptor extends BaseComponent {

	String[] popupPattern = defaultPopupPattern;
	String[] messagePattern = defaultMessagePattern;
	
	List<Operation> operations = new ArrayList<Operation>();
	
	public ConfirmPopupDescriptor addOperation(String operationConst, String operationCall[], String operationMessage) {
		operations.add(new Operation(operationConst, operationCall, operationMessage));
		return this;
	}

	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
	}

	public void buildFaceletPart() {
		setFragment(new FragmentDescriptor(popupPattern) {
			public String[] getText() {
				for (int i=0; i<operations.size(); i++) {
					insertLines("/// insert messages here", messagePattern);
					replace("OpValue", ""+numbers.get(operations.get(i).getOperationConst()));
					replaceProperty("operAttr", operationGetter);
					replace("put the message here", operations.get(i).operationMessage);
				}
				replace("beanName", getDocument().getBeanName());
				replaceProperty("opened", confirmRequestedGetter);
				replaceMethod("continue", confirmOperation);
				replaceMethod("cancel", cancelOperation);
				removeLine("/// insert messages here");
				return super.getText();
			}
		});
	}
	
	public AttributeDescriptor getOperation() {
		return operation;
	}

	AttributeDescriptor operation;
	MethodDescriptor operationGetter;
	MethodDescriptor operationSetter;
	MethodDescriptor confirmRequestedGetter;
	MethodDescriptor confirmRequestedSetter;
	MethodDescriptor confirmOperation;
	MethodDescriptor cancelOperation;
	
	protected void buildJavaElements(ClassDescriptor javaClass) {
		noneAttr = buildConstsAttributes(javaClass);
		operation = buildOperationAttribute(javaClass);
		operationGetter = buildOperationGetterMethod(javaClass);
		operationSetter = buildOperationSetterMethod(javaClass);
		cancelOperation = buildCancelOperationMethod(javaClass);
		confirmOperation = buildContinueOperationMethod(javaClass);
		confirmRequestedGetter = buildConfirmRequestedGetterMethod(javaClass);
		confirmRequestedSetter = buildConfirmRequestedSetterMethod(javaClass);
	}

	Map<String, Integer> numbers = new HashMap<String, Integer>();
	AttributeDescriptor noneAttr;
	
	protected AttributeDescriptor buildConstsAttributes(
			ClassDescriptor javaClass) {
		AttributeDescriptor noneConst = new AttributeDescriptor();
		noneConst.init(javaClass, TypeDescriptor.rInt, "OP_NONE")
			.addModifier("static final").initWithPattern("0");
		javaClass.addAttribute(noneConst);
		Map<String, AttributeDescriptor> consts = 
			new HashMap<String, AttributeDescriptor>();
		for (int i=0; i<operations.size(); i++) {
			AttributeDescriptor aConst = consts.get(operations.get(i).getOperationConst());
			if (aConst==null) {
				int number = i+1;
				numbers.put(operations.get(i).getOperationConst(), number);
				aConst = new AttributeDescriptor();
				aConst.init(javaClass, TypeDescriptor.rInt, "OP_"+operations.get(i).getOperationConst())
					.addModifier("static final").initWithPattern(""+number);
				consts.put(operations.get(i).getOperationConst(), aConst);
				javaClass.addAttribute(aConst);
			}
			operations.get(i).operationAttr = aConst;
		}
		return noneConst;
	}

	protected AttributeDescriptor buildOperationAttribute(
			ClassDescriptor javaClass) {
		AttributeDescriptor oper = new AttributeDescriptor();
		oper.init(
			javaClass, TypeDescriptor.rInt, "operation")
			.initWithPattern("NONE")
			.replace("NONE", noneAttr);
		javaClass.addAttribute(oper);
		return oper;
	}

	protected MethodDescriptor buildOperationGetterMethod(
			ClassDescriptor javaClass) {
		MethodDescriptor getter = StandardMethods.getter(operation, "public");
		javaClass.addMethod(getter);
		return getter;
	}

	protected MethodDescriptor buildOperationSetterMethod(
			ClassDescriptor javaClass) {
		MethodDescriptor setter = StandardMethods.setter(operation, "public");
		javaClass.addMethod(setter);
		return setter;
	}

	protected MethodDescriptor buildConfirmRequestedGetterMethod(
			ClassDescriptor javaClass) {
		MethodDescriptor confirmGetter = new MethodDescriptor();
		confirmGetter.init(javaClass, TypeDescriptor.rBoolean, new StandardNameMaker("isConfirm", "Requested", null))
			.addModifier("public")
			.setContent("return operAttr != NONE;")
			.replace("operAttr", operation)
			.replace("NONE", noneAttr);
		javaClass.addMethod(confirmGetter);
		return confirmGetter;
	}

	protected MethodDescriptor buildConfirmRequestedSetterMethod(
			ClassDescriptor javaClass) {
		MethodDescriptor confirmSetter = new MethodDescriptor();
		confirmSetter.init(javaClass, TypeDescriptor.rVoid, new StandardNameMaker("setConfirm", "Requested", null))
			.addModifier("public")
			.addParameter(TypeDescriptor.rBoolean, "confirmRequested")
			.setContent(
				"if (!confirmRequested) {",
				"	cancelOp();",
				"}"
			)
			.replace("cancelOp", cancelOperation);
		javaClass.addMethod(confirmSetter);
		return confirmSetter;
	}

	protected MethodDescriptor buildCancelOperationMethod(
			ClassDescriptor javaClass) {
		MethodDescriptor cancel = new MethodDescriptor();
		cancel.init(javaClass, TypeDescriptor.rVoid, new StandardNameMaker("cancel", null, operation))
			.addModifier("public")
			.setContent("operAttr = NONE;")
			.replace("operAttr", operation)
			.replace("NONE", noneAttr);
		javaClass.addMethod(cancel);
		return cancel;
	}

	protected MethodDescriptor buildContinueOperationMethod(
			ClassDescriptor javaClass) {
		List<String> opConsts = getOperationConsts();
		MethodDescriptor confirm = new MethodDescriptor();
		confirm.init(javaClass,TypeDescriptor.rVoid, new StandardNameMaker("continue", null, operation))
			.addModifier("public")
			.setContent(
				"if (operAttr == CONST) {",
				"	call();",
				"}",
				"/// insert elseif here",
				"operAttr = NONE;"
			);
		List<Operation> ops = getOperations(opConsts.get(0));
		confirm.replace("CONST", ops.get(0).operationAttr);
		confirm.replace("call();", getCall(ops));
		for (int i=1; i<opConsts.size(); i++) {
			ops = getOperations(opConsts.get(i));
			confirm.insertLines("/// insert elseif here", 
				"else if (operAttr == CONST) {",
				"	call();",
				"}"
			);
			confirm.replace("CONST", ops.get(0).operationAttr);
			confirm.replace("call();", getCall(ops));
		}
		confirm.replace("operAttr", operation)
			.replace("NONE", noneAttr)
			.removeLine("/// insert elseif here");
		javaClass.addMethod(confirm);
		return confirm;
	}
	
	String[] getCall(List<Operation> operations) {
		String[] text = {"/// insert calls here"};
		for (Operation op : operations) {
			text = Rewrite.insertLines(text, "/// insert calls here", op.operationCall);
		}
		text = Rewrite.removeLine(text, "/// insert calls here");
		return text;	
	}
	
	List<String> getOperationConsts() {
		List<String> opers = new ArrayList<String>();
		for (Operation op : operations) {
			if (!opers.contains(op.getOperationConst()))
				opers.add(op.getOperationConst());
		}
		return opers;
	}
	
	List<Operation> getOperations(String operationConst) {
		List<Operation> opers = new ArrayList<Operation>();
		for (Operation op : operations) {
			if (op.getOperationConst().equals(operationConst))
				opers.add(op);
		}
		return opers;
	}
	
	static class Operation {
		
		public Operation(String operationConst, String[] operationCall,
				String operationMessage) {
			super();
			this.operationConst = operationConst;
			this.operationCall = operationCall;
			this.operationMessage = operationMessage;
		}
		
		String getOperationConst() {
			return operationConst;
		}

		private String operationConst;
		String[] operationCall;
		String operationMessage;
		AttributeDescriptor operationAttr;
	}
	
	static String[] defaultPopupPattern = {
		"<ice:panelPopup modal=\"true\" draggable=\"true\" styleClass=\"popup\" rendered=\"#{beanName.opened}\">",
		"	<f:facet name=\"header\">",
		"		<ice:panelGrid styleClass=\"title\" cellpadding=\"0\" cellspacing=\"0\" columns=\"2\">",
		"			<ice:outputText value=\"Confirmation\"/>",
		"		</ice:panelGrid>",
		"	</f:facet>",
		"	<f:facet name=\"body\">",
		"		<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
		"			/// insert messages here",
		"			<div class=\"actionButtons\">",
		"				<h:commandButton value=\"valider\" action=\"#{beanName.continue}\"/>",
		"				<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{beanName.cancel}\"/>",
		"			</div>",
		"		</ice:panelGrid>",
		"	</f:facet>",
		"</ice:panelPopup>"
	};

	static String[] defaultMessagePattern = {
 		"<ice:outputText value=\"put the message here\" rendered=\"#{beanName.operAttr==OpValue}\"/>"
	};

	public AttributeDescriptor getConst(String constLabel) {
		if (constLabel.equals("NONE"))
			return noneAttr; //getConsts().get(0);
		for (int i=0; i<operations.size(); i++) {
			if (operations.get(i).getOperationConst().equals(constLabel))
				return operations.get(i).operationAttr; //getConsts().get(i+1);
		}
		throw new GeneratorException("Unknown operation label : "+constLabel);
	}

}
