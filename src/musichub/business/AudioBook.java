package musichub.business;

import java.io.*;
import org.w3c.dom.*;

public class AudioBook extends AudioElement {
	private Language language;
	private Category category;
	
	public AudioBook (String title, String artist, int lengthInSeconds, String uid, String content, String language, String category) {
		super (title, artist, lengthInSeconds, uid, content);
		this.setLanguage(language);
		this.setCategory(category);
	}
	public AudioBook (String title, String artist, int lengthInSeconds, String content, String language, String category) {
		super (title, artist, lengthInSeconds, content);
		this.setLanguage(language);
		this.setCategory(category);
	}
	
	public AudioBook (Element xmlElement) throws Exception {
		super(xmlElement);
		try {
			this.setLanguage(xmlElement.getElementsByTagName("language").item(0).getTextContent());
			this.setCategory(xmlElement.getElementsByTagName("category").item(0).getTextContent());
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Language getLanguage() {
		return this.language;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public void setLanguage (String language) {	
		switch (language.toLowerCase()) {
			case "english":
			default:
				this.language = Language.ENGLISH;
				break;
			case "french":
				this.language = Language.FRENCH;
				break;
			case "german":
				this.language = Language.GERMAN;
				break;
			case "spanish":
				this.language = Language.SPANISH;
				break;
			case "italian":
				this.language = Language.ITALIAN;
				break;
				
		}
	}
	
	public void setCategory (String category) {	
		switch (category.toLowerCase()) {
			case "youth":
			default:
				this.category = Category.YOUTH;
				break;
			case "novel":
				this.category = Category.NOVEL;
				break;
			case "theater":
				this.category = Category.THEATER;
				break;
			case "documentary":
				this.category = Category.DOCUMENTARY;
				break;
			case "speech":
				this.category = Category.SPEECH;
				break;
		}
	}
	
	
	public String toString() {
		return super.toString() + ", Language = " + getLanguage() + ", Category = " + getCategory() + "\n";
	}
	

	public void createXMLElement(Document document, Element parentElement) {
		// audiobook element
        Element audioBook = document.createElement("audiobook");

		super.createXMLElement(document, audioBook);
		
		Element languageElement = document.createElement("language");
        languageElement.appendChild(document.createTextNode(language.getLanguage()));
        audioBook.appendChild(languageElement);
		
		Element categoryElement = document.createElement("category");
        categoryElement.appendChild(document.createTextNode(category.getCategory()));
        audioBook.appendChild(categoryElement);
		
		parentElement.appendChild(audioBook);
		return;
	}
}