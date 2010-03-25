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

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ValueObjectDescriptor;

import junit.framework.TestCase;

public class TestDataAccessService extends TestCase {

	public void testEmptyService() {
		DataAccessServiceDescriptor service = 
			new DataAccessServiceDescriptor("com.objetdirect.services", "DataAccessService");
		TestUtil.assertText(service.getInterfaceText(),
			"package com.objetdirect.services;",
			"",
			"import javax.ejb.Remote;",
			"",
			"@Remote",
			"public interface DataAccessService {",
			"",
			"}"
		);
		TestUtil.assertText(service.getImplementationText(),
			"package com.objetdirect.services;",
			"",
			"import javax.ejb.Stateless;",
			"",
			"@Stateless",
			"public class DataAccessServiceImpl implements DataAccessService {",
			"",
			"",
			"",
			"",
			"}"
		);
	}
	
	public void testSimpleValueObjectGetter() {
		EntityDescriptor productMaker = new EntityDescriptor("com.myapp.domain", "Product", true);
		EntityDescriptor invoiceMaker = new EntityDescriptor("com.myapp.domain", "Invoice", true);
		EntityDescriptor invoiceLineMaker = new EntityDescriptor("com.myapp.domain", "InvoiceLine", true);
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true);
		entityMaker.addOneToMany(productMaker, "products", false);
		entityMaker.addOneToMany(invoiceMaker, "invoices", false);
		invoiceMaker.addOneToMany(invoiceLineMaker, "lines", true);
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
		new ValueObjectDescriptor("com.myapp.vo", invoiceMaker);
		DataAccessServiceDescriptor service = 
			new DataAccessServiceDescriptor("com.objetdirect.services", "DataAccessService");
		ValueObjectGetterDescriptor voGetter = new ValueObjectGetterDescriptor(valueObjectMaker,"products","invoices","invoices.lines");
		service.addElement(voGetter);
		TestUtil.assertText(service.getInterfaceText(),
			"package com.objetdirect.services;",
			"",
			"import com.myapp.vo.ClientVO;",
			"import javax.ejb.Remote;",
			"",
			"@Remote",
			"public interface DataAccessService {",
			"",
			"	ClientVO getClient(int clientId);",
			"}"
		);
		TestUtil.assertText(service.getImplementationText(),
			"package com.objetdirect.services;",
			"",
			"import com.myapp.domain.Client;",
			"import com.myapp.domain.Invoice;",
			"import com.myapp.vo.ClientVO;",
			"import com.objetdirect.ejbfrmk.Mapper;",
			"import javax.ejb.Stateless;",
			"import javax.persistence.EntityManager;",
			"import javax.persistence.EntityNotFoundException;",
			"import javax.persistence.PersistenceContext;",
			"",
			"@Stateless",
			"public class DataAccessServiceImpl implements DataAccessService {",
			"",
			"	@PersistenceContext",
			"	EntityManager entityManager;",
			"",
			"",
			"	public ClientVO getClient(int clientId) {",
			"		Client client = entityManager.find(Client.class, clientId);",
			"		if (client == null)",
			"			throw new EntityNotFoundException();",
			"		client.loadProducts();",
			"		client.loadInvoices();",
			"		for (Invoice invoice : client.getInvoices() {",
			"			invoice.loadLines();",
			"		}",
			"		return (ClientVO)Mapper.toValueObject(client, ClientVO.class);",
			"	}",
			"",
			"}"
		);
	}
	
	public void testComplexValueObjectGetter() {
		EntityDescriptor productMaker = new EntityDescriptor("com.myapp.domain", "Product", true);
		EntityDescriptor productDetailMaker = new EntityDescriptor("com.myapp.domain", "ProductDetail", true);
		EntityDescriptor invoiceMaker = new EntityDescriptor("com.myapp.domain", "Invoice", true);
		EntityDescriptor invoiceLineMaker = new EntityDescriptor("com.myapp.domain", "InvoiceLine", true);
		EntityDescriptor invoiceLineDetailMaker = new EntityDescriptor("com.myapp.domain", "InvoiceLineDetail", true);
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true);
		entityMaker.addOneToMany(productMaker, "products", false);
		productMaker.addOneToMany(productDetailMaker, "details", true);
		entityMaker.addOneToMany(invoiceMaker, "invoices", false);
		invoiceMaker.addOneToMany(invoiceLineMaker, "lines", true);
		invoiceLineMaker.addOneToMany(invoiceLineDetailMaker, "details", true);
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
		new ValueObjectDescriptor("com.myapp.vo", invoiceMaker);
		new ValueObjectDescriptor("com.myapp.vo", invoiceLineMaker);
		new ValueObjectDescriptor("com.myapp.vo", productMaker);
		DataAccessServiceDescriptor service = 
			new DataAccessServiceDescriptor("com.objetdirect.services", "DataAccessService");
		ValueObjectGetterDescriptor voGetter = new ValueObjectGetterDescriptor(valueObjectMaker,
			"products","products.details","invoices","invoices.lines","invoices.lines.details");
		service.addElement(voGetter);
		TestUtil.assertText(service.getImplementationText(),
			"package com.objetdirect.services;",
			"",
			"import com.myapp.domain.Client;",
			"import com.myapp.domain.Invoice;",
			"import com.myapp.domain.InvoiceLine;",
			"import com.myapp.domain.Product;",
			"import com.myapp.vo.ClientVO;",
			"import com.objetdirect.ejbfrmk.Mapper;",
			"import javax.ejb.Stateless;",
			"import javax.persistence.EntityManager;",
			"import javax.persistence.EntityNotFoundException;",
			"import javax.persistence.PersistenceContext;",
			"",
			"@Stateless",
			"public class DataAccessServiceImpl implements DataAccessService {",
			"",
			"	@PersistenceContext",
			"	EntityManager entityManager;",
			"",
			"",
			"	public ClientVO getClient(int clientId) {",
			"		Client client = entityManager.find(Client.class, clientId);",
			"		if (client == null)",
			"			throw new EntityNotFoundException();",
			"		client.loadProducts();",
			"		for (Product product : client.getProducts() {",
			"			product.loadDetails();",
			"		}",
			"		client.loadInvoices();",
			"		for (Invoice invoice : client.getInvoices() {",
			"			invoice.loadLines();",
			"			for (InvoiceLine invoiceLine : invoice.getLines() {",
			"				invoiceLine.loadDetails();",
			"			}",
			"		}",
			"		return (ClientVO)Mapper.toValueObject(client, ClientVO.class);",
			"	}",
			"",
			"}"
		);
	}
	
	public void testSimpleEntityCreator() {
//		EntityDescriptor productMaker = new EntityDescriptor("com.myapp.domain", "Product", true);
//		EntityDescriptor invoiceMaker = new EntityDescriptor("com.myapp.domain", "Invoice", true);
//		EntityDescriptor invoiceLineMaker = new EntityDescriptor("com.myapp.domain", "InvoiceLine", true);
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "Client", true);
//		entityMaker.addOneToMany(productMaker, "products", false);
//		entityMaker.addOneToMany(invoiceMaker, "invoices", false);
//		invoiceMaker.addOneToMany(invoiceLineMaker, "lines", true);
		ValueObjectDescriptor valueObjectMaker = new ValueObjectDescriptor("com.myapp.vo", entityMaker);
//		new ValueObjectDescriptor("com.myapp.vo", invoiceMaker);
		DataAccessServiceDescriptor service = 
			new DataAccessServiceDescriptor("com.objetdirect.services", "DataAccessService");
		EntityCreatorrDescriptor entityCreator = new EntityCreatorrDescriptor(valueObjectMaker/*,"products","invoices","invoices.lines"*/);
		service.addElement(entityCreator);
		TestUtil.println(service.getInterfaceText());
		TestUtil.println(service.getImplementationText());
	}
}
