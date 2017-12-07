package com.boco.data.freamework.util;
/*
 * Created on 2008-7-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


/**
 * @author xqz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlManage {
	private static final String CONFIG_FILENAME = "/config/boco.xml";
	  /**
	   * XML properties to actually get and set the BOCO properties.
	   */
	  private static XmlManage xManage = null;

	  public static XMLProperties getBoco() {
	  		xManage = new XmlManage();
	  	String path = xManage.getClass().getResource(CONFIG_FILENAME).getPath();
	  	System.out.println("path:"+path);
		
	  	XMLProperties properties = new XMLProperties();
	  	properties.loadFile(path);
	    return properties;
	  }

	  public static XMLProperties getFile(String filePath)
	    {
	        System.out.println("read file:" + filePath);
	        xManage = new XmlManage();
	        String path = "";
	        String temp = filePath;
	        try
	        {
	            if(filePath.indexOf("/") == 0)
	                temp = filePath.substring(1);
	            path = StaticMethod.getFilePathForUrl("classpath:" + temp);
	            System.out.println("path=" + path);
	        }
	        catch(Exception err)
	        {
	            path = xManage.getClass().getResource(filePath).getPath();
	            System.out.println("path=" + path);
	        }
	        XMLProperties properties = new XMLProperties();
	        properties.loadFile(path);
	        return properties;
	    }
	  
	  public static XMLProperties getSchema(String xmlSchema) {
	  		xManage = new XmlManage();
	  	XMLProperties properties = new XMLProperties();
	  	properties.loadSchema(xmlSchema);
	    return properties;
	  }

	  private XmlManage() {

	  }
	  
	  
}
