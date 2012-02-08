package sk.project22.pmacko.translate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class XMLHelper
{
	private static final Logger logger = Logger.getLogger(Language.class.getName());
	
	public static Document openFile(String fileName) throws SAXException, IOException, ParserConfigurationException
	{
		File file = new File(fileName);
		
		if (!file.exists())
			throw new FileNotFoundException(fileName);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		return doc;
	}
	
	public static String getValueById(Document doc, String ID)
	{
		try
		{
			return doc.getElementById(ID).getAttribute("val");	
		}
		catch (NullPointerException e)
		{
			logger.warning("Cant find Language entry with ID " + ID);
			return "";
		}
	}

	public static Document createSettingsDocumnet() throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();

		DocumentType doctype = impl.createDocumentType(
			"settings",
			"System",
			"settings.dtd"
		);
       
       return impl.createDocument(null,null,doctype);
	}

	public static void setValueById(Document doc, String ID, String val)
	{
		if(doc.getDocumentElement() == null)
		{
			Element x = doc.createElement("settings");
			doc.appendChild(x);
		}
		if(doc.getElementById(ID) == null)
		{
			Element item = (Element)doc.createElement("item");
			item.setAttribute("ID", ID);
			item.setAttribute("val", val);
			doc.getDocumentElement().appendChild(item);
		}
	}

	public static void saveToFile(Document doc, String file) throws IOException, TransformerException
	{
		TransformerFactory transfac = TransformerFactory.newInstance();
	    Transformer trans = transfac.newTransformer();
	    
	    //generating string from xml tree
	    StringWriter sw = new StringWriter();
	    StreamResult result = new StreamResult(sw);
	    DOMSource source = new DOMSource(doc);
	    trans.transform(source, result);
	    String xmlString = sw.toString();
	 
	    //Saving the XML content to File
	    OutputStream f0;
	    byte buf[] = xmlString.getBytes();
	    f0 = new FileOutputStream(file);
	    
	    for(int i=0;i<buf .length;i++)
	    {
	    	f0.write(buf[i]);
	    }
	    
		f0.close();
		buf = null;
	}
}
