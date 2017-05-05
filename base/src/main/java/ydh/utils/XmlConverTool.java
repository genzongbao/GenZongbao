package ydh.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlConverTool {
    @SuppressWarnings("unchecked")
	 public static Map<String,String> xmltoMap(String xml) {  
        try {  
            Map<String,String> map = new HashMap<String,String>();  
            Document document = DocumentHelper.parseText(xml);  
            Element nodeElement = document.getRootElement();  
			List<Element> node = nodeElement.elements();
            for (Iterator<Element> it = node.iterator(); it.hasNext();) {  
                Element elm = it.next();  
                map.put(elm.getName(), elm.getText());  
            }
            node = null;  
            nodeElement = null;  
            document = null;  
            return map;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
}
