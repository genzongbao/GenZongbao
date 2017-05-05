package ydh.cicada.dict;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;

public class DictContext implements ServletContextAware {
	private static final Logger logger = LoggerFactory.getLogger(DictContext.class);

    private String[] packagesToScan;
    
    private Map<String, Dict> dicts = new HashMap<String, Dict>();

    private Dict installDict(Dict dict) {
        Dict old = dicts.put(dict.getDictName(), dict);
        Assert.isNull(old, "Duplicate dict name: " + dict.getDictName());
        return dict;
    }

    /**
     * 将符合条件的Bean以Class集合的形式返回
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void scanEnumDict() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        if (this.packagesToScan != null && this.packagesToScan.length > 0) {
            AnnotationTypeFilter dictEnumFilter = new AnnotationTypeFilter(DictEnum.class);
            for (String pkg : this.packagesToScan) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + "/**/*.class";
                Resource[] resources = resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        if (dictEnumFilter.match(reader, readerFactory)) {
                            try {
                                String className = reader.getClassMetadata().getClassName();
                                Class<?> clazz = Class.forName(className);
                                installDict(new Dict(clazz));
                            } catch (ClassNotFoundException e) {
                                if (logger.isErrorEnabled()) {
                                    logger.error("dict class no found.", e);
                                }
                                continue;
                            } catch (Exception e) {
                                if (logger.isErrorEnabled()) {
                                	
                                    logger.error("dict init method invoke fail.", e);
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        try {
            this.scanEnumDict();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("scan enum dict fail.", e);
            }
        }
        servletContext.setAttribute("dict", Collections.unmodifiableMap(dicts));
    }

    public String[] getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public class Dict implements Map<String, Enum<?>> {
    	private Class<?> enumClass;
    	private LinkedHashMap<String, Enum<?>> map = new LinkedHashMap<String, Enum<?>>();
    	
    	public Dict(Class<?> enumClass) {
    		this.enumClass = enumClass;
    		Enum<?>[] enums = (Enum<?>[])this.enumClass.getEnumConstants();
    		for (Enum<?> e : enums) {
    			map.put(e.name(), e);
    		}
    	}
    	
    	public String getDictName() {
    		return enumClass.getSimpleName();
    	}
    	
		@Override
		public boolean containsKey(Object key) {
			return map.containsKey(key);
		}
	
		@Override
		public boolean containsValue(Object value) {
			return map.containsValue(value);
		}
	
		@Override
		public Set<Map.Entry<String, Enum<?>>> entrySet() {
			return map.entrySet();
		}
	
		@Override
		public boolean isEmpty() {
			return map.isEmpty();
		}
	
		@Override
		public Set<String> keySet() {
			return map.keySet();
		}
	
		@Override
		public void clear() {
			throw new UnsupportedOperationException("Cannot modify dict");
		}
	
		@Override
		public int size() {
			return dicts.size();
		}
	
		@Override
		public Collection<Enum<?>> values() {
			return map.values();
		}
		
		@Override
		public Enum<?> put(String key, Enum<?> value) {
			throw new UnsupportedOperationException("Cannot modify dict");
		}

		@Override
		public void putAll(Map<? extends String, ? extends Enum<?>> m) {
			throw new UnsupportedOperationException("Cannot modify dict");
		}

		@Override
		public Enum<?> get(Object key) {
			if (key instanceof Integer) {
				return (Enum<?>)enumClass.getEnumConstants()[(Integer)key];
			} else {
				return map.get(key);
			}
		}

		@Override
		public Enum<?> remove(Object key) {
			throw new UnsupportedOperationException("Cannot modify dict");
		}
		
    }
}
