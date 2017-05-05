
package ydh.utils;

import java.lang.reflect.Type;
import java.util.Arrays;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


public class GsonUtil {

    /**
     * 将对象转换成json字符串。
     * 
     * <pre>
     * 如果传入的为数组、List、Set，并且内容不含复杂数据对象，如String [] arr = {"a","b"};
     * 转后的数据格式为:["a","b"],与javascript的数组格式相同。
     * </pre>
     * 
     * <pre>
     * 如果传入的对象的引用属性与其他的实体具有懒加载lazy=true的关系，会报session关闭异常。
     * 如果需要对该对象进行序列化，请将lazy设置为false即可解决。
     * </pre>
     * 
     * @param obj
     *            传入的对象:集合，map，对象都可以
     * @return 转换成功的JSON字符串,{key:value}
     */
    public static String toJSONString(Object object) {
        String jsonString = null;
        try {			
        	Gson gson = new GsonBuilder()
        			//过滤密码
        			.setExclusionStrategies(new ExclusionStrategy() {  
        	              
        	            @Override  
        	            public boolean shouldSkipField(FieldAttributes f) {  
        	                 //过滤掉字段名包含"age"  
        	                return f.getName().contains("cusPassword");  
        	            }  
        	              
        	            @Override  
        	            public boolean shouldSkipClass(Class<?> clazz) {  
        	                //过滤掉 类名包含 Bean的类  
        	                return clazz.getName().contains("Bean");  
        	            }  
        	        })
        			.setDateFormat("yyyy-MM-dd HH:mm:ss")
        			.create();
        	jsonString = gson.toJson(object);
		} catch (Exception e) {
			Gson gson = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {  
      	              
        	            @Override  
        	            public boolean shouldSkipField(FieldAttributes f) {  
        	                 //过滤掉字段名包含"age"  
        	                return f.getName().contains("cusPassword");  
        	            }  
        	              
        	            @Override  
        	            public boolean shouldSkipClass(Class<?> clazz) {  
        	                //过滤掉 类名包含 Bean的类  
        	                return clazz.getName().contains("anyBean");  
        	            }  
        	        })
					.setDateFormat("MMM d, yyyy hh:mm:ss").create();
        	jsonString = gson.toJson(object);
		}
        return jsonString;
    }

    /**
     * <pre>将指定的对象转换成JSON字符串，同时可以设置不需要过滤的属性的全名集合
     * 如果filedFullNames为null，则说明不需要任何过滤，此时建议直接使用toJSONString(Object obj)即可; 
     * e.g.  
     * String filedFullNames = {"java.util.List<com.abc.User>"};
     * toJSONString(new UserType(),types)
     * </pre>
     * @param object 需要转换的对象
     * @param sikpFieldNames 需要过滤的属性全名 e.g. {"java.util.List<com.abc.User>"}
     * @return 转换成功的JSON字符串,{key:value}
     */
    public static String toJSONString(Object object, String[] sikpFieldNames) {
        final String[] types = sikpFieldNames;
        new GsonBuilder().enableComplexMapKeySerialization();
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (types != null && Arrays.asList(types).contains(f.getDeclaredType().toString())) {
                    return true;
                }
                return false;
            }
        }).create();
        return gson.toJson(object);
    }

    /**
     * 将JSON字符串转成对应的java对象，若传入的对象不满足转换的条件，则返回null
     * 实现:调用alibba.fastJson的JSON.parseObject，传入jsonString，clazz即可。 对该方法进行try
     * catch,若发生异常，则返回null
     * 
     * @param JSONString
     *            传入的JSON字符串
     * @param obj
     *            需要将JSON字符串已经该对象进行绑定才能转换
     * @return
     * @return 转换成功的java对象
     */
    public static <T> T toObject(String jsonString, Class<T> clazz) {
        T t = null;
        Gson gson =null;
        try {
        	gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        	t = gson.fromJson(jsonString, clazz);
		} catch (JsonSyntaxException e) {
			gson=new GsonBuilder().setDateFormat("MMM d, yyyy hh:mm:ss").create();
        	t = gson.fromJson(jsonString, clazz);
		}
        return t;
    }
    
    /**
     * 将JSON字符串转成对应的java对象，若传入的对象不满足转换的条件，则返回null
     * 实现:调用com.google.gson.reflect.TypeToken.TypeToken<List<T>>()获取java.lang.reflect.Type传入即可
     * @param jsonString
     * @param type
     * @return
     */
    public static <T> T toObjectChange(String jsonString,Type type){
		T t=null;
		//默认日期格式.GBK为
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		try {			
			t=gson.fromJson(jsonString, type);
		} catch (JsonSyntaxException e) {
			gson=new GsonBuilder().setDateFormat("MMM d, yyyy hh:mm:ss").create();
			t=gson.fromJson(jsonString, type);
//			e.printStackTrace();
		}
    	return t;
    }
}
