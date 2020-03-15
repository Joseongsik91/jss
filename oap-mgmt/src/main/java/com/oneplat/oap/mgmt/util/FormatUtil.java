package com.oneplat.oap.mgmt.util;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Map;

public class FormatUtil {
	
	protected FormatUtil() {
	}
	
	public static final String INDENT      = "    ";
	public static final String GT          = ">";
	public static final String NEW_LINE    = "\n";
	
	
	private static String setPrettyJsonString(String jsonString) {
		
		StringBuffer prettyJsonSb = new StringBuffer();
		int indentDepth = 0;
		String targetString = null;
		for(int i=0; i<jsonString.length(); i++) {
			targetString = jsonString.substring(i, i+1);
			if(targetString.equals("{")||targetString.equals("[")) {
				prettyJsonSb.append(targetString).append(NEW_LINE);
				indentDepth++;
				for(int j=0; j<indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			}else if(targetString.equals("}")||targetString.equals("]")) {
				prettyJsonSb.append(NEW_LINE);
				indentDepth--;
				for(int j=0; j<indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
				prettyJsonSb.append(targetString);
			}else if(targetString.equals(",")) {
				prettyJsonSb.append(targetString);
				prettyJsonSb.append(NEW_LINE);
				for(int j=0; j<indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			}else {
				prettyJsonSb.append(targetString);
			}
		}
		return prettyJsonSb.toString();
	}

    /** json string pretty format convert
     * @param jsonString
     * @return
     */
	public static String getPrettyJson(String jsonString) {
		
		if(jsonString == null) {
			return null;
		}else {
			return setPrettyJsonString(jsonString);
		}
    }
	
	/** replace all String
	 * @param xml
	 * @return
	 */
	private static String replaceString(String xml) {
		
		String str = xml;
		
		String asisType1 = "\t";
		String asisType2 = ">[ ]*\r\n";
		String asisType3 = ">[ ]*\r";
		String asisType4 = ">[ ]*\n";
		String asisType5 = ">[ ]*<";
		
		str = str.replaceAll(asisType1 , INDENT);
		str = str.replaceAll(asisType2 , GT);
		str = str.replaceAll(asisType3 , GT);
		str = str.replaceAll(asisType4 , GT);
		str = str.replaceAll(asisType5 , "\\>\\<");
		
		return str;
	}
    
    /** xml string pretty format convert
     * @param xml
     * @return
     */
    public static String getPrettyXml(String xml) throws TransformerException, ParserConfigurationException, SAXException, IOException {
    	
    	String xmlString = xml;
    	if(xmlString == null) {
    		return null;
    	}else {
    		xmlString = replaceString(xmlString);
    		Document document          = transformString2Document(xmlString);
    		TransformerFactory factory = TransformerFactory.newInstance();
    		Transformer transformer    = factory.newTransformer();
    		OutputStream out           = new ByteArrayOutputStream();
    		
    		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    		transformer.transform(new DOMSource(document), new StreamResult(out));
    		
    		xmlString = out.toString();
    		xmlString = xmlString.replace("\"?>", "\"?>\r\n");
    		
    		return xmlString;
    	}
    }
    
    /** transform string to document
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    protected static Document transformString2Document(String xml) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder        = factory.newDocumentBuilder();
        Document document              = builder.parse(new InputSource(new StringReader(xml)));
		return document;
	}
    
    /**
	 * getRestUrlReplace
	 * @param url
	 * @param map
	 * @return
     * @throws UnsupportedEncodingException 
	 */
	public static String getRestUrlReplace(String url, Map<String, String> map, String encodingType) throws Exception{
		String convertedUrl = url;
		if(map != null) {
			for(Map.Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();
//				try {
					if (!"".equals("")) {
						value = URLEncoder.encode(value, encodingType);
					}
//				} catch (UnsupportedEncodingException e) {
//					log.debug("url encode error ", e);
//				}
				convertedUrl = StringUtils.replace(convertedUrl, "{" + entry.getKey() + "}", value);
			}
		}
		return convertedUrl;
	}	
	
	/** replace 1,000
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String replaceComma(long val) {
		
		String returnVal = "0";
		DecimalFormat df = new DecimalFormat("#,##0");
		if(val > 0){
			returnVal = df.format(val);
		}
		return returnVal;
	}
	
    public static boolean isNull( String str ){
        if (str != null) {
            str = str.trim();
        }
        return (str == null || "".equals(str));
    }	
	
    public static String toPhoneFormat( String str ){
        String returnStr = "";
        if (isNull(str) || "".equals(str)) {
            returnStr = "";
        } else {
            if (str.length() == 11) {
                returnStr = str.substring(0,3)+"-"+str.substring(3,7)+"-"+str.substring(7);
            } else if (str.length() == 10) {
                returnStr = str.substring(0,3)+"-"+str.substring(3,6)+"-"+str.substring(6);
            } 
        }
        return returnStr;
    }	
	
}