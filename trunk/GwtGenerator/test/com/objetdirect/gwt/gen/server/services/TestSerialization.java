package com.objetdirect.gwt.gen.server.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import com.allen_sauer.gwt.log.client.Log;

public class TestSerialization extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testSimpleSerialization() {
		byte[] serializedString = getSerializedUMLCanvas();
		
//		String result = deserializeByteArray(String.class, serializedString);
//		System.out.println(result);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T deserializeByteArray(Class<T> clazz, byte[] bytes) {
		ObjectInputStream in;
		T object = null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(bytes));
			object = (T)in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
	public byte[] getSerializedUMLCanvas() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeBytes(new String("test"));
		} catch (IOException e) {
			Log.debug(e.getMessage());
		}
		
		return bos.toByteArray();
	}

}
