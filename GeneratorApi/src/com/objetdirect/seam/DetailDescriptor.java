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

import java.util.List;

import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.PersistentList;

public abstract class DetailDescriptor
	extends BaseComponent
	implements EntityHolder, FormHolder 
{
	private LayoutDescriptor layout;

	public DetailDescriptor() {
		layout = new LayoutDescriptor();
		layout.setOwner(this);
	}
			
	public void buildJavaPart() {
		ClassDescriptor javaClass = getParent(PageDescriptor.class).getClassDescriptor();
		buildJavaElements(javaClass);
		layout.buildJavaPart();
		completeValidation();
	}
	
	public void buildFaceletPart() {
		buildFaceletFragment();
		layout.buildFaceletPart();
		getFragment().insertFragment("/// insert layout here", layout.getFragment());
	}

	protected boolean isComplex() {
		return layout.isComplex();
	}
		
	public EntityDescriptor getEntityType() {
		return getParent(EntityHolder.class).getEntityType();
	}

	public String getEntityAlias() {
		return getParent(DetailHolder.class).getEntityAlias();
	}
	
	public MethodDescriptor[] getCheckDirtyMethods() {
		return null;
	}

	public String getCheckDirtyTag() {
		return null;
	}
	
	protected void buildFaceletFragment() {
		setFragment(new FragmentDescriptor(getDetailPattern()));
	}
	
	protected abstract String[] getDetailPattern();
	
	public LayoutDescriptor getLayout() {
		return layout;
	}
	
	public DetailDescriptor addForm(FormDescriptor form) {
		layout.addForm(form);
		return this;
	}

	public String getNamePrefix() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}

	public String getNameSuffix() {
		return null;
	}
	
	public MethodDescriptor getFillFormMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getSetCurrentEntityMethod();
	}
	
	public DetailDescriptor addPersistentList(PersistentList persistentList) {
		layout.addPersistentList(persistentList);
		return this;
	}
	
	MethodDescriptor isEntityValid;
	
	public MethodDescriptor getIsEntityValidMethod() {
		return isEntityValid;
	}
	
	protected void buildJavaElements(ClassDescriptor javaClass) {
		isEntityValid = buildIsEntityValidMethod(javaClass);
	}
	
	protected void completeValidation() {
		List<FieldRendererDescriptor> fields = layout.getAllFields(); 
		if (fields.size()>0) {
			DetailHolder parent = getParent(DetailHolder.class);
			ConstraintManager constraintMngr = new ConstraintManager(
				parent.getCurrentEntityAttribute(), fields);
			String[] text = constraintMngr.getText();
			if (text.length>0) {
				isEntityValid.insertLines("/// insert validations here", "String message;");
				isEntityValid.insertLines("/// insert validations here", text);
			}
		}
	}
	
	protected MethodDescriptor buildIsEntityValidMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		String paramName = NamingUtil.toMember(entityType.getTypeName());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isCurrent", "valid", entityType));
		meth.setContent(
				"/// insert validations here",
				"return !FacesContext.getCurrentInstance().getMessages().hasNext();")
			.replace("FacesContext", JSF.FacesContext)
			.replace("entity", paramName);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor getOpenDetailMethod() {
		return null;
	}
	
}
