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

package com.objetdirect.seam.lists;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.BaseComponent;
import com.objetdirect.seam.DetailDescriptor;
import com.objetdirect.seam.FieldRendererDescriptor;
import com.objetdirect.seam.PageDescriptor;
import com.objetdirect.seam.fieldrenderers.LabelFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintBooleanFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintDateFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintEntityFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintEnumFieldRendererDescriptor;
import com.objetdirect.seam.fieldrenderers.PrintNumberFieldRendererDescriptor;

public abstract class AbstractListDescriptor extends BaseComponent {

	static final int DEFAULT_LIST_SIZE = 10;
	
	private String[] tablePattern = defaultTablePattern;

	private String[] inlineDetailPattern = defaultInlineDetailPattern;
	private String[] popupDetailPattern = defaultPopupDetailPattern;
	
	private MethodDescriptor listGetter;
	private int listSize = DEFAULT_LIST_SIZE;
	
	private DetailDescriptor detail;

	private FragmentDescriptor detailFragment;	

	public AttributeDescriptor formCentered;
	
	private MethodDescriptor isFormCentered;	
	private String detailTitle;
	
	private List<FieldRendererDescriptor> columns = new ArrayList<FieldRendererDescriptor>();
	private String[] columnPattern = defaultColumnPattern;

	public AbstractListDescriptor showField(FieldRendererDescriptor field) {
		field.setOwner(this);
		columns.add(field);
		return this;
	}

	public AbstractListDescriptor showField(String fieldName, String fieldTitle, int length) {
		showField(new LabelFieldRendererDescriptor(fieldName, fieldName, fieldTitle, length));
		return this;
	}

	public AbstractListDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		showField(new PrintNumberFieldRendererDescriptor(fieldName, fieldName, fieldTitle, pattern, length));
		return this;
	}

	public AbstractListDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		showField(new PrintDateFieldRendererDescriptor(fieldName, fieldName, fieldTitle, pattern));
		return this;
	}

	public AbstractListDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		showField(new PrintBooleanFieldRendererDescriptor(fieldName, fieldName, fieldTitle, trueValue, falseValue));
		return this;
	}

	public AbstractListDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		showField(new PrintEnumFieldRendererDescriptor(fieldName, fieldName, fieldTitle, length));
		return this;
	}

	public AbstractListDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		showField(new PrintEntityFieldRendererDescriptor(fieldName, fieldName, fieldTitle, labels, length));
		return this;
	}
	
	protected void addColumnsToFragment() {
		boolean first = true;
		for (FieldRendererDescriptor column : columns) {
			FragmentDescriptor columnFragment = new FragmentDescriptor(columnPattern);
			columnFragment.replace("Title", column.getFieldTitle());
			manageSelection(column, columnFragment, first);
			first = false;
			column.setFragmentFiller(new FieldRendererDescriptor.FragmentFiller() {
				public void fillFragment(
						FragmentDescriptor fragment,
						FieldRendererDescriptor field) 
				{
					fragment.replace("entity", getEntityAlias());
				}
				
			});
			column.buildFaceletPart();
			FragmentDescriptor fieldFragment = column.getFragment();
			columnFragment.insertFragment("/// insert field here", fieldFragment);
			getFragment().insertFragment("/// insert columns here", columnFragment);
			getFragment().replace("colWidth", column.getWidth()+"px,colWidth");
		}
	}

	protected abstract void manageSelection(FieldRendererDescriptor column, FragmentDescriptor columnFragment, boolean first);

	public void buildFaceletPart() {
		buildListPart();
		if (detail!=null) {
			buildDetailPart();
		}
	}
	
	protected void buildDetailPart() {
		detail.buildFaceletPart();
		detailFragment.insertFragment("/// detail content here", detail.getFragment());
		detailFragment.removeLine("/// detail content here");
	}
	
	protected void buildListPart() {
		prepareFragment();
		addColumnsToFragment();
	}

	protected void prepareFragment() {
		setFragment(new FragmentDescriptor(tablePattern) {
			public String[] getText() {
				remove(",colWidth");
				remove(" columnWidths=\"colWidth\"");
				removeLines(
					"<h:panelGroup rendered=\"rendering condition here\">",
					"	/// insert commands here",
					"</h:panelGroup>"
				);
				remove(" rendered=\"rendering condition here\"");
				return super.getText();
			}
		});
		String listName = getListName();
		String listId = getDocument().makeId(listName);
		if (detail!=null) {
			if (detailTitle!=null)
				insertPopupDetail();
			else
				insertInlineDetail();
		}	
		getFragment().removeLine("/// detail here");
		getFragment().replace(
			"beanName", getDocument().getBeanName(),
			"rowCount", ""+listSize,
			"entityList", listName,
			"tableId", listId,
			"line", getEntityAlias());
	}

	protected void insertInlineDetail() {
		getFragment().insertLines("/// detail here", inlineDetailPattern);
		getFragment().replaceProperty("currentEntityVisible", getIsDetailVisibleMethod());
		detailFragment = getFragment();
	}

	protected void insertPopupDetail() {
		detailFragment = new FragmentDescriptor(popupDetailPattern);
		detailFragment.replaceProperty("currentEntityFormCentered", isFormCentered);
		detailFragment.replace("popup title here", detailTitle);
		detailFragment.replaceProperty("currentEntityVisible", getIsDetailVisibleMethod());
		detailFragment.replaceMethod("cancelCurrentEntity", getCloseDetailMethod());
		detailFragment.replace("beanName", getDocument().getBeanName());
		detailFragment.replaceProperty("entity", getEntityShownInDetailMethod());
		PageDescriptor page = getParent(PageDescriptor.class);
		page.addPopup(detailFragment);
	}
	
	protected abstract MethodDescriptor getIsDetailVisibleMethod();
	
	protected abstract MethodDescriptor getCloseDetailMethod();
	
	protected abstract MethodDescriptor getEntityShownInDetailMethod();
	
	protected DetailDescriptor getDetail() {
		return detail;
	}
	
	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
		if (detail!=null) {
			buildDetailJavaPart(javaClass);
		}
	}

	public AbstractListDescriptor setDetail(DetailDescriptor detail) {
		this.detail = detail;
		this.detail.setOwner(this);
		return this;
	}

	public void buildJavaElements(ClassDescriptor javaClass) {
		listGetter = buildListGetterMethod(); 
		listGetter.addAnnotation(TypeDescriptor.SuppressWarnings, "\"unchecked\"");
		getClassDescriptor().addMethod(listGetter);
	}
	
	protected void buildDetailJavaPart(ClassDescriptor javaClass) {
		detail.buildJavaPart();
		if (detailTitle!=null) {
			formCentered = buildFormCenteredAttribute(javaClass);
			isFormCentered = buildIsFormCenteredMethod(javaClass);
		}
	}
	
	protected abstract MethodDescriptor buildListGetterMethod();
	
	public MethodDescriptor getListGetterMethod() {
		return listGetter;
	}
	
	public String getEntityAlias() {
		String lineName = NamingUtil.toSingular(getListName());
		return lineName;
	}

	public String getListName() {
		String listName = NamingUtil.extractMember(listGetter);
		return listName;
	}

	protected AttributeDescriptor buildFormCenteredAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("current"+NamingUtil.toProperty(getEntityAlias()), "formCentered", null))
				.initWithPattern("true");
		javaClass.addAttribute(attr);
		return attr;
	}
		
	protected MethodDescriptor buildIsFormCenteredMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean,
			new StandardNameMaker("is", null, formCentered))
			.addModifier("public")
			.setContent(
				"boolean result = formCentered;",
				"formCentered = false;",
				"return result;");
		meth.replace("formCentered", formCentered);
		javaClass.addMethod(meth);
		return meth;
	}
	
	public AbstractListDescriptor setDetailInPopup(String detailTitle) {
		this.detailTitle = detailTitle;
		return this;
	}
	
	static final String[] defaultColumnPattern = {
		"<ice:column>",
		"	<f:facet name=\"header\">Title</f:facet>",
		"	/// insert field here",
		"</ice:column>",
	};
	
	static final String[] defaultTablePattern = {
		"/// criteria content here",
		"<h:panelGroup>",
		"	<ice:dataTable id=\"tableId\" value=\"#{beanName.entityList}\" var=\"line\"", 
		"		rows=\"rowCount\" resizable=\"true\" columnWidths=\"colWidth\">",
		"		/// insert columns here",
		"	</ice:dataTable>",
		"	<ice:dataPaginator for=\"tableId\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
		"		<f:facet name=\"first\">",
		"			<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
		"		</f:facet>",
		"		<f:facet name=\"last\">",
		"			<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
		"		</f:facet>",
		"		<f:facet name=\"previous\">",
		"			<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
		"		</f:facet>",
		"		<f:facet name=\"next\">",
		"			<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
		"		</f:facet>",
		"		<f:facet name=\"fastforward\">",
		"			<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
		"		</f:facet>",
		"		<f:facet name=\"fastrewind\">",
		"			<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
		"		</f:facet>",
		"	</ice:dataPaginator>",
		"	<h:panelGroup rendered=\"rendering condition here\">",
		"		/// insert commands here",
		"	</h:panelGroup>",
		"</h:panelGroup>",
		"/// detail here"
	};
	
	static final String[] defaultInlineDetailPattern = {
		"<h:panelGroup rendered=\"#{beanName.currentEntityVisible}\">",
		"	<div class=\"mask\"/>",
		"	<h:panelGroup style=\"position:relative\">",
    	"		/// detail content here",
    	"	</h:panelGroup>",
		"</h:panelGroup>"
	};

	static final String[] defaultPopupDetailPattern = {
		"<h:panelGroup rendered=\"#{beanName.currentEntityVisible}\">",
		"	<div class=\"modal\"/>",
		"	<ice:panelPopup draggable=\"true\"",
		"		autoCentre=\"#{beanName.currentEntityFormCentered}\"",
		"		styleClass=\"corePopup\">",
		"		<f:facet name=\"header\">",
		"			<ice:panelGroup styleClass=\"popupHeaderWrapper\">",
		"				<ice:outputText value=\"popup title here\" styleClass=\"popupHeaderText\"/>",
		"				<ice:commandButton type=\"button\" image=\"/img/cancel.png\"",
		"					actionListener=\"#{beanName.cancelCurrentEntity}\"",
		"					styleClass=\"popupHeaderImage\" title=\"Close Popup\" alt=\"Close\"/>",
		"			</ice:panelGroup>",
		"		</f:facet>",
		"		<f:facet name=\"body\">",
		"			<ice:panelGroup styleClass=\"popupBody\">",
		"				/// detail content here",
		"			</ice:panelGroup>",
		"		</f:facet>",
		"	</ice:panelPopup>",
		"</h:panelGroup>"
	};
}
