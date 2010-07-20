/*
 * This file is part of the Gwt-Generator project and was written by Raphaël Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.server.entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * 
 * The special class diagram of the classes and relations supported by the seam Generator.
 *
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@PersistenceCapable
public class SeamDiagram {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String key;
	
	@Persistent
	private Blob serializedCanvas;
	
	protected SeamDiagram() {
	}
	
	public SeamDiagram(UMLCanvas umlCanvas) {
		setCanvas(umlCanvas);
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	
	public void setCanvas(UMLCanvas umlCanvas) {
		if (umlCanvas == null)
			return;
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(byteOutput);
			oos.writeObject(umlCanvas);
			this.serializedCanvas = new Blob(byteOutput.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UMLCanvas getCanvas() {
		UMLCanvas canvas = null;
		
		if (serializedCanvas!=null) {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedCanvas.getBytes());
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(byteInput);
				canvas = (UMLCanvas)ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return canvas;
	}
}
