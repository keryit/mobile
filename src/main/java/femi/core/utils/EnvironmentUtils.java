package femi.core.utils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentUtils {
   static Document xmlDocument = null;
	/**
	 * Call this function to replace envKey_ value defined in the scenario step with value from testResouces file.
	 * If the input value does not contain "envKey_" prefix, it is returned as is, without changes.
	 * @param stepValue - the value from the scenario step that may contain "envKey_" prefix
	 * @return value from testResouces file
	 * @throws Exception
	 */
	public static String getEnvironmentDependentValue(String stepValue){
		return getValueByXMLKeyFromTestResources(stepValue);
	}
		
	/**
	 * Call this function to replace envKey_ value(s) defined in the table row in scenario step with value(s) from testResouces file.
	 * If the input value does not contain "envKey_" prefix, it is returned as is, without changes.
	 * @param row from ExampleMap defined in story file
	 * @return new row where all values prefixed with "envKey_" are replaced with values from testResouces file
	 * @throws Exception
	 */
	public static Map<String,String> getEnvironmentDependentExampleTableRow(Map<String,String> row) throws Exception {
		Map<String,String> updatedExampleMapRow = new HashMap<String, String>();
		
		for (Map.Entry<String, String> exampleMapEntry : row.entrySet()) {
			String columnName = exampleMapEntry.getKey();
			String columnValue = exampleMapEntry.getValue();
			String updatedColumnValue=getEnvironmentDependentValue(columnValue);
			updatedExampleMapRow.put(columnName, updatedColumnValue);
		}
		
		return updatedExampleMapRow;
	}
	
	/**
	 * Looks for the unique xml tag in the file defined by test.resources property and return its text value
	 * @param xmlKey- xml tag name
	 * @return value from testResouces file
	 * @throws Exception if the input xml key is not found or found more than once, exception is produced
	 */
	public static String getValueByXMLKeyFromTestResources(String xmlKey){
		String value = null;
		try {
			String configPath = System.getProperty("test.resources");
			if (configPath == null) {
				initXMLDocument("environments/qa/testResources.xml");
			} else {
				initXMLDocument(System.getProperty("test.resources"));
			}

			NodeList nList = xmlDocument.getElementsByTagName(xmlKey);

			if (nList.getLength() == 0) {
				throw new Exception("envKey with name: " + xmlKey + " not found in the test resource file "
						+ System.getProperty("test.resources"));
			} else if (nList.getLength() > 1) {
				throw new Exception("Several envKey with name: " + xmlKey + " are found in the test resource file " + System.getProperty("test.resources")
						+ " Xml key name must be unique.");
			}
			value = nList.item(0).getTextContent();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return value;
	}
	
	private static void initXMLDocument(String pathToFile) throws SAXException, IOException, ParserConfigurationException {
		if (xmlDocument != null) {
			return;
		}
		else {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream(pathToFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			xmlDocument = dBuilder.parse(is);
			xmlDocument.getDocumentElement().normalize();
		}
	}
}
