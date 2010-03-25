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

package com.objetdirect.entities;

import com.objetdirect.engine.TestUtil;

import junit.framework.TestCase;

public class TestEasyAddRelationshipMethods extends TestCase {

	public void testAddOneToOneMethod() {
		EntityDescriptor author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		EntityDescriptor biography = new EntityDescriptor("com.objetdirect.domain", "Biography").
			addStringField("summary", null);
		author.addOneToOne(biography, "biography", true, true, false);
		String[] resultAuthor = author.getText();
		String[] resultBiography = biography.getText();
		author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		biography = new EntityDescriptor("com.objetdirect.domain", "Biography").
			addStringField("summary", null);
		RelationshipDescriptor rs = new OneToOneReferenceDescriptor(author, biography, "biography", true, true, false);
		author.addRelationship(rs);
		TestUtil.assertText(resultAuthor, author.getText());
		TestUtil.assertText(resultBiography, biography.getText());
	}
	
	public void testAddOneToOneBidirectionalMethod() {
		EntityDescriptor author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		EntityDescriptor biography = new EntityDescriptor("com.objetdirect.domain", "Biography").
			addStringField("summary", null);
		author.addOneToOne(biography, "biography", true, true, false, "author", false, false, false);
		String[] resultAuthor = author.getText();
		String[] resultBiography = biography.getText();
		author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		biography = new EntityDescriptor("com.objetdirect.domain", "Biography").
			addStringField("summary", null);
		RelationshipDescriptor rs = new OneToOneReferenceDescriptor(author, biography, "biography", true, true, false);
		author.addRelationship(rs);
		RelationshipDescriptor rrs = new OneToOneReferenceDescriptor(biography, author, "author", false, false, false);
		author.addRelationship(rrs);
		rs.setReverse(rrs, true);
		TestUtil.assertText(resultAuthor, author.getText());
		TestUtil.assertText(resultBiography, biography.getText());
	}

	public void testAddOneToManyMethod() {
		EntityDescriptor author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		EntityDescriptor book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		author.addOneToMany(book, "books", true);
		String[] resultAuthor = author.getText();
		String[] resultBook = book.getText();
		author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		RelationshipDescriptor rs = new OneToManyReferenceListDescriptor(author, book, "books", true);
		author.addRelationship(rs);
		TestUtil.assertText(resultAuthor, author.getText());
		TestUtil.assertText(resultBook, book.getText());
	}
	
	public void testAddOneToManyBidirectionalMethod() {
		EntityDescriptor author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		EntityDescriptor book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		author.addOneToMany(book, "books", true, "author", true, false);
		String[] resultAuthor = author.getText();
		String[] resultBook = book.getText();
		author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		RelationshipDescriptor rs = new OneToManyReferenceListDescriptor(author, book, "books", true);
		author.addRelationship(rs);
		RelationshipDescriptor rrs = new ManyToOneReferenceDescriptor(book, author, "author", true, false);
		author.addRelationship(rrs);
		rs.setReverse(rrs, true);
		TestUtil.assertText(resultAuthor, author.getText());
		TestUtil.assertText(resultBook, book.getText());
	}
	
	public void testAddManyToOneMethod() {
		EntityDescriptor author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		EntityDescriptor book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		book.addManyToOne(author, "author", true, false);
		String[] resultAuthor = author.getText();
		String[] resultBook = book.getText();
		author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		RelationshipDescriptor rs = new ManyToOneReferenceDescriptor(book, author, "author", true, false);
		author.addRelationship(rs);
		TestUtil.assertText(resultAuthor, author.getText());
		TestUtil.assertText(resultBook, book.getText());
	}
	
	public void testAddManyToOneBidirectionalMethod() {
		EntityDescriptor author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		EntityDescriptor book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		book.addManyToOne(author, "author", true, false, "books", true);
		String[] resultAuthor = author.getText();
		String[] resultBook = book.getText();
		author = new EntityDescriptor("com.objetdirect.domain", "Author").
			addStringField("name", null);
		book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		RelationshipDescriptor rs = new ManyToOneReferenceDescriptor(book, author, "author", true, false);
		author.addRelationship(rs);
		RelationshipDescriptor rrs = new OneToManyReferenceListDescriptor(author, book, "books", true);
		rs.setReverse(rrs, true);
		TestUtil.assertText(resultAuthor, author.getText());
		TestUtil.assertText(resultBook, book.getText());
	}

	public void testAddManyToManyMethod() {
		EntityDescriptor keyword = new EntityDescriptor("com.objetdirect.domain", "Keyword").
			addStringField("code", null);
		EntityDescriptor book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		book.addManyToMany(keyword, "keywords", false);
		String[] resultKeyword = keyword.getText();
		String[] resultBook = book.getText();
		keyword = new EntityDescriptor("com.objetdirect.domain", "Keyword").
			addStringField("code", null);
		book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		RelationshipDescriptor rs = new ManyToManyReferenceListDescriptor(book, keyword, "keywords", false);
		book.addRelationship(rs);
		TestUtil.assertText(resultKeyword, keyword.getText());
		TestUtil.assertText(resultBook, book.getText());
	}
	
	public void testAddManyToManyBidirectionalMethod() {
		EntityDescriptor keyword = new EntityDescriptor("com.objetdirect.domain", "Keyword").
			addStringField("code", null);
		EntityDescriptor book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		book.addManyToMany(keyword, "keywords", false, "books", false);
		String[] resultKeyword = keyword.getText();
		String[] resultBook = book.getText();
		keyword = new EntityDescriptor("com.objetdirect.domain", "Keyword").
			addStringField("code", null);
		book = new EntityDescriptor("com.objetdirect.domain", "Book").
			addStringField("title", null);
		RelationshipDescriptor rs = new ManyToManyReferenceListDescriptor(book, keyword, "keywords", false);
		book.addRelationship(rs);
		RelationshipDescriptor rrs = new ManyToManyReferenceListDescriptor(keyword, book, "books", false);
		keyword.addRelationship(rrs);
		rs.setReverse(rrs, true);
		TestUtil.assertText(resultKeyword, keyword.getText());
		TestUtil.assertText(resultBook, book.getText());
	}
}
