package musichub.business;

import java.util.*;
import org.w3c.dom.*;

public abstract class AudioElement {
	protected String  	title;
	protected String 	artist;
	protected int    	lengthInSeconds;
	protected UUID    	uuid;
	protected String	content;
	
	public AudioElement (String title, String artist, int lengthInSeconds, String id, String content) {
		this.title = title;
		this.artist = artist;
		this.lengthInSeconds = lengthInSeconds;
		this.uuid = UUID.fromString(id);
		this.content = content;
	}

	public AudioElement (String title, String artist, int lengthInSeconds, String content) {
		this.title = title;
		this.artist = artist;
		this.lengthInSeconds = lengthInSeconds;
		this.content = content;
		this.uuid =  UUID.randomUUID();
	}
	
	public AudioElement (Element xmlElement)  throws Exception
	{
		try {
			title = xmlElement.getElementsByTagName("title").item(0).getTextContent();
			artist = xmlElement.getElementsByTagName("artist").item(0).getTextContent();
			lengthInSeconds = Integer.parseInt(xmlElement.getElementsByTagName("length").item(0).getTextContent());
			content = xmlElement.getElementsByTagName("content").item(0).getTextContent();
			String uuid = null;
			try {
				uuid = xmlElement.getElementsByTagName("UUID").item(0).getTextContent();
			}
			catch (Exception ex) {
				System.out.println ("Empty element UUID, will create a new one");
			}
			if ((uuid == null)  || (uuid.isEmpty()))
				this.uuid = UUID.randomUUID();
			else this.uuid = UUID.fromString(uuid);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String getArtist() {
		return this.artist;
	}

	public String getTitle() {
		return this.title;
	}
	
	public String toString() {
		return "Title = " + this.title + ", Artist = " + this.artist + ", Length = " + this.lengthInSeconds + ", Content = " + this.content;
	}

	public void createXMLElement(Document document, Element parentElement)
	{
		Element nameElement = document.createElement("title");
        nameElement.appendChild(document.createTextNode(title));
        parentElement.appendChild(nameElement);
		
		Element artistElement = document.createElement("artist");
        artistElement.appendChild(document.createTextNode(artist));
        parentElement.appendChild(artistElement);
		
		Element lengthElement = document.createElement("length");
        lengthElement.appendChild(document.createTextNode(Integer.valueOf(lengthInSeconds).toString()));
        parentElement.appendChild(lengthElement);
		
		Element UUIDElement = document.createElement("UUID");
        UUIDElement.appendChild(document.createTextNode(uuid.toString()));
        parentElement.appendChild(UUIDElement);
		
		Element contentElement = document.createElement("content");
        contentElement.appendChild(document.createTextNode(content));
        parentElement.appendChild(contentElement);

	}
	
}