package com.objetdirect.gwt;

import junit.framework.TestCase;

import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ManyToManyReferenceListDescriptor;

public class GenerationTest extends TestCase {

	public void testGen() {
		EntityDescriptor e1 = new EntityDescriptor("com.objetdirect.domain", "Store");
		EntityDescriptor e2 = new EntityDescriptor("com.objetdirect.domain", "Customer");

		ManyToManyReferenceListDescriptor ref1 = 
			new ManyToManyReferenceListDescriptor(e1, e2, "customers", true);
		ManyToManyReferenceListDescriptor ref2 = 
			new ManyToManyReferenceListDescriptor(e2, e1, "stores", false);
		ref1.setReverse(ref2, true);
		ref2.setReverse(ref1, false);
		
		TestUtil.println(e1.getText());
		TestUtil.println(e2.getText());
	}
}
