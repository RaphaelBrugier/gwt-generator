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

package com.objetdirect.services;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.InterfaceDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.SemanticDescriptor;
import com.objetdirect.engine.SignatureDescriptor;
import com.objetdirect.entities.JavaxPersistence;

public class DataAccessServiceDescriptor {

	String packageName;
	String className;
	boolean prepared = false;
	
	public DataAccessServiceDescriptor(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
		init();
	}
	
	ClassDescriptor implementationDescriptor;
	InterfaceDescriptor interfaceDescriptor;
	
	void init() {
		interfaceDescriptor = new InterfaceDescriptor(packageName, className);
		interfaceDescriptor.addSemantic(new SemanticDescriptor("defines", this));
		implementationDescriptor = new ClassDescriptor(packageName, className+"Impl");
		implementationDescriptor.addSemantic(new SemanticDescriptor("implements", this));
		implementationDescriptor.addInterface(interfaceDescriptor);
		interfaceDescriptor.addAnnotation(JavaxEJB.Remote);
		implementationDescriptor.addAnnotation(JavaxEJB.Stateless);
	}
	
	public InterfaceDescriptor getInterfaceDescriptor() {
		return interfaceDescriptor;
	}

	public ClassDescriptor getImplementationDescriptor() {
		return implementationDescriptor;
	}

	public String[] getInterfaceText() {
		prepare();
		return interfaceDescriptor.getText();
	}

	public String[] getImplementationText() {
		prepare();
		return implementationDescriptor.getText();
	}

	public void publishMethod(MethodDescriptor method) {
		implementationDescriptor.addMethod(method);
		SignatureDescriptor signature = new SignatureDescriptor();
		signature.init(interfaceDescriptor, method.getReturnType(), method.getName());
		for (int i=0; i<method.getParameterCount(); i++) {
			signature.addParameter(method.getParameterType(i), method.getParameterName(i));
		}
		interfaceDescriptor.addMethod(signature);
	}
	
	void prepare() {
		if (!prepared) { 
			for (DataAccessServiceElement element : elements) {
				element.prepare();
			}
			prepared = true;
		}
	}
	
	List<DataAccessServiceElement> elements = new ArrayList<DataAccessServiceElement>();

	public DataAccessServiceDescriptor addElement(DataAccessServiceElement element) {
		element.setOwner(this);
		elements.add(element);
		return this;
	}
	
	AttributeDescriptor entityManager;
	
	public void declareEntityManager() {
		if (entityManager==null) {
			entityManager = new AttributeDescriptor();
			entityManager.init(implementationDescriptor, JavaxPersistence.EntityManager, "entityManager");
			entityManager.addAnnotation(JavaxPersistence.PersistenceContext);
			implementationDescriptor.addAttribute(entityManager);
		}
	}
	
	public AttributeDescriptor getEntityManager() {
		declareEntityManager();
		return entityManager;
	}
	
}
