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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeDescriptor {
	BasicJavaType owner;
	String[] pattern;
	SemanticDescriptor semantic;

	String[] content = new String[0];
	
	public CodeDescriptor() {
	}
			
	public CodeDescriptor init(BasicJavaType owner, String ... pattern) {
		this.owner = owner;
		setPattern(pattern);
		return this;
	}
	
	public CodeDescriptor setSemantic(SemanticDescriptor semantic) {
		this.semantic = semantic;
		return this;
	}
	
	public SemanticDescriptor getSemantic() {
		return semantic;
	}
	
	public boolean recognizes(SemanticDescriptor semantic) {
		if (this.semantic==null || semantic==null)
			return false;
		return this.semantic.equals(semantic);
	}
	
	public BasicJavaType getOwner() {
		return owner;
	}

	public String[] getPattern() {
		return pattern;
	}

	public void setPattern(String[] pattern) {
		this.pattern = pattern;
	}
	
	public CodeDescriptor addParameter(TypeDescriptor paramType, String paramName) {
		owner.getImportSet().addType(paramType);
		setPattern(Rewrite.replace(getPattern(), 
			"paramType paramName", paramType.getUsageName(owner)+" "+paramName+", paramType paramName"));
		parameters.add(new Parameter(paramName, paramType));
		return this;
	}
	
	public CodeDescriptor addVarargsParameter(TypeDescriptor paramType, String paramName) {
		owner.getImportSet().addType(paramType);
		setPattern(Rewrite.replace(getPattern(), 
			"paramType paramName", paramType.getUsageName(owner)+" ... "+paramName+", paramType paramName"));
		parameters.add(new Parameter(paramName, TypeDescriptor.array(paramType)));
		return this;
	}

	public CodeDescriptor setContent(String ... content) {
		this.content = content;
		return this;
	}
	
	public CodeDescriptor setContent(CodeDescriptor meth) {
		setContent(Rewrite.dup(meth.getContent()));
		this.actions = new ArrayList<Action>(meth.actions);
		this.lastActions = new ArrayList<Action>(meth.lastActions);
		return this;
	}

	public String[] getContent() {
		return content;
	}
	
	public String[] getText() {
		prepare();
		return getPattern();
	}

	protected void prepare() {
		for (AnnotationDescriptor annotation : annotations) {
			setPattern(Rewrite.insertLines(getPattern(), "/// annotations here", annotation.getText()));
		}
		for (Action action : actions) {
			action.execute(this);
		}
		for (Action action : lastActions) {
			action.execute(this);
		}
		if (getContent()!=null && getContent()!=getPattern()) {
			setPattern(Rewrite.insertLines(getPattern(), "/// content here", getContent()));
		}
		setPattern(Rewrite.remove(getPattern(), ", paramType paramName"));
		setPattern(Rewrite.remove(getPattern(), "paramType paramName"));
		setPattern(Rewrite.removeLine(getPattern(), "///"));
		setPattern(Rewrite.remove(getPattern(), "modifier "));
	}
	
	public CodeDescriptor addModifier(String modifier) {
		setPattern(Rewrite.replace(getPattern(), "modifier ", modifier+" modifier "));
		return this;
	}
	
	List<AnnotationDescriptor> annotations = new ArrayList<AnnotationDescriptor>();
	Map<TypeDescriptor, AnnotationDescriptor> annotationMap = new HashMap<TypeDescriptor, AnnotationDescriptor>();
	
	public CodeDescriptor addAnnotation(TypeDescriptor type, String ... params) {
		AnnotationDescriptor annotation = new AnnotationDescriptor(type);
		for (String param : params) {
			annotation.addParam(param);
		}
		annotation.setOwner(this);
		annotations.add(annotation);
		annotationMap.put(annotation.getType(), annotation);
		return this;
	}

	public CodeDescriptor addAnnotationParam(TypeDescriptor type, String param) {
		annotationMap.get(type).addParam(param);
		return this;
	}
	
	public CodeDescriptor addAnnotationParam(TypeDescriptor type, String param, TypeDescriptor paramType, String paramTypePattern) {
		getOwner().getImportSet().addType(paramType);
		param = Rewrite.replace(param, paramTypePattern, paramType.getUsageName(getOwner()));
		addAnnotationParam(type, param);
		return this;
	}
	
	static class Action {
		enum ActionType {REPLACE_TYPE, REPLACE_ATTR, REPLACE_METH, REPLACE_STRING, REPLACE_TEXT, INSERT_TEXT, REMOVE_STRING, REMOVE_LINE, REMOVE_TEXT};
		ActionType actionType;
		Object before;
		Object after;
		
		Action(ActionType actionType, Object before, Object after) {
			this.actionType = actionType;
			this.before = before;
			this.after = after;
		}
		
		void execute(CodeDescriptor meth) {
			switch (actionType) {
			case REPLACE_TYPE:
				meth.doReplace((String)before, (TypeDescriptor)after);
				break;
			case REPLACE_ATTR:
				meth.doReplace((String)before, (AttributeDescriptor)after);
				break;
			case REPLACE_METH:
				meth.doReplace((String)before, (MethodDescriptor)after);
				break;
			case REPLACE_STRING:
				meth.doReplace((String)before, (String)after);
				break;
			case REPLACE_TEXT:
				meth.doReplace((String)before, (String[])after);
				break;
			case INSERT_TEXT:
				meth.doInsertLines((String)before, (String[])after);
				break;
			case REMOVE_STRING:
				meth.doRemove((String)before);
				break;
			case REMOVE_LINE:
				meth.doRemoveLine((String)before);
				break;
			case REMOVE_TEXT:
				meth.doRemoveLines((String[])before);
				break;
			}
		}
	}
	
	List<Action> actions = new ArrayList<Action>();
	List<Action> lastActions = new ArrayList<Action>();
	
	public CodeDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		this.owner.getImportSet().addType(byThis);
		actions.add(new Action(Action.ActionType.REPLACE_TYPE, replaceThis, byThis));
		return this;
	}

	public CodeDescriptor replace(String replaceThis, AttributeDescriptor byThis) {
		actions.add(new Action(Action.ActionType.REPLACE_ATTR, replaceThis, byThis));
		return this;
	}
	
	public CodeDescriptor replace(String replaceThis, CodeDescriptor byThis) {
		actions.add(new Action(Action.ActionType.REPLACE_METH, replaceThis, byThis));
		return this;
	}

	public CodeDescriptor replace(String replaceThis, String byThis) {
		actions.add(new Action(Action.ActionType.REPLACE_STRING, replaceThis, byThis));
		return this;
	}

	public CodeDescriptor replace(String replaceThis, String[] byThis) {
		actions.add(new Action(Action.ActionType.REPLACE_TEXT, replaceThis, byThis));
		return this;
	}
	
	public CodeDescriptor insertLines(String beforeThis, String ... insertThis) {
		actions.add(new Action(Action.ActionType.INSERT_TEXT, beforeThis, insertThis));
		return this;
	}
	
	public CodeDescriptor remove(String removeThis) {
		actions.add(new Action(Action.ActionType.REMOVE_STRING, removeThis, null));
		return this;
	}
	
	public CodeDescriptor removeLine(String removeThis) {
		actions.add(new Action(Action.ActionType.REMOVE_LINE, removeThis, null));
		return this;
	}
	
	public CodeDescriptor removeLines(String ... removeThis) {
		actions.add(new Action(Action.ActionType.REMOVE_TEXT, removeThis, null));
		return this;
	}

	public CodeDescriptor doReplace(String replaceThis, TypeDescriptor byThis) {
		setContent(Rewrite.replace(getContent(), replaceThis, byThis.getUsageName(owner)));
		return this;
	}

	public CodeDescriptor lastReplace(String replaceThis, TypeDescriptor byThis) {
		this.owner.getImportSet().addType(byThis);
		lastActions.add(new Action(Action.ActionType.REPLACE_TYPE, replaceThis, byThis));
		return this;
	}

	public CodeDescriptor lastReplace(String replaceThis, AttributeDescriptor byThis) {
		lastActions.add(new Action(Action.ActionType.REPLACE_ATTR, replaceThis, byThis));
		return this;
	}
	
	public CodeDescriptor lastReplace(String replaceThis, CodeDescriptor byThis) {
		lastActions.add(new Action(Action.ActionType.REPLACE_METH, replaceThis, byThis));
		return this;
	}

	public CodeDescriptor lastReplace(String replaceThis, String byThis) {
		lastActions.add(new Action(Action.ActionType.REPLACE_STRING, replaceThis, byThis));
		return this;
	}

	public CodeDescriptor lastReplace(String replaceThis, String[] byThis) {
		lastActions.add(new Action(Action.ActionType.REPLACE_TEXT, replaceThis, byThis));
		return this;
	}
	
	public CodeDescriptor lastInsertLines(String beforeThis, String ... insertThis) {
		lastActions.add(new Action(Action.ActionType.INSERT_TEXT, beforeThis, insertThis));
		return this;
	}
	
	public CodeDescriptor lastRemove(String removeThis) {
		lastActions.add(new Action(Action.ActionType.REMOVE_STRING, removeThis, null));
		return this;
	}
	
	public CodeDescriptor lastRemoveLine(String removeThis) {
		lastActions.add(new Action(Action.ActionType.REMOVE_LINE, removeThis, null));
		return this;
	}
	
	public CodeDescriptor lastRemoveLines(String ... removeThis) {
		lastActions.add(new Action(Action.ActionType.REMOVE_TEXT, removeThis, null));
		return this;
	}

	public CodeDescriptor doReplace(String replaceThis, AttributeDescriptor byThis) {
		setContent(Rewrite.replace(getContent(), replaceThis, byThis.getName()));
		return this;
	}
	
	public CodeDescriptor doReplace(String replaceThis, MethodDescriptor byThis) {
		setContent(Rewrite.replace(getContent(), replaceThis, byThis.getName()));
		return this;
	}

	public CodeDescriptor doReplace(String replaceThis, String byThis) {
		setContent(Rewrite.replace(getContent(), replaceThis, byThis));
		return this;
	}

	public CodeDescriptor doReplace(String replaceThis, String[] byThis) {
		if (byThis.length>0)
			setContent(Rewrite.replaceAndInsertLines(getContent(), replaceThis, byThis));
		return this;
	}
	
	public CodeDescriptor doInsertLines(String beforeThis, String ... insertThis) {
		setContent(Rewrite.insertLines(getContent(), beforeThis, insertThis));
		return this;
	}
	
	public CodeDescriptor doRemove(String removeThis) {
		setContent(Rewrite.remove(getContent(), removeThis));
		return this;
	}
	
	public CodeDescriptor doRemoveLine(String removeThis) {
		setContent(Rewrite.removeLine(getContent(), removeThis));
		return this;
	}
	
	public CodeDescriptor doRemoveLines(String ... removeThis) {
		setContent(Rewrite.removeLines(getContent(), removeThis));
		return this;
	}

	public int getParameterCount() {
		return parameters.size();
	}
	
	public String getParameterName(int param) {
		return parameters.get(param).name;
	}


	public TypeDescriptor getParameterType(int param) {
		return parameters.get(param).type;
	}

	List<Parameter> parameters = new ArrayList<Parameter>();
	
	static class Parameter {
		Parameter(String name, TypeDescriptor type) {
			this.name = name;
			this.type = type;
		}
		String name;
		TypeDescriptor type;
	}
	
}
