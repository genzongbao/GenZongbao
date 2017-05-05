package ydh.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 获取日志输出对象
 * @author tearslee
 *
 */
public class LogHelper {

	/**
	 * 日志输出获取
	 * @return
	 */
	public static Log getLogger(Class clazz){
		Log logger=null;
//		String clazzName = new Object()    {
//            public String getClassName()
//            {
//                String clazzName = this.getClass().getName();
//                return clazzName.substring(0, clazzName.lastIndexOf('$'));
//            }
//        }.getClassName();
		try {
			logger = LogFactory.getLog(clazz);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return logger;
	}
}
