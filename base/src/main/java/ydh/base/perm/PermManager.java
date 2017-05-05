package ydh.base.perm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class PermManager implements ServletContextAware {
	private static final Logger logger = LoggerFactory.getLogger(PermManager.class);
	
	private String[] packagesToScan;
	private Class<?>[] permClasses;
	private Map<String, PermSys> systems;
	private Map<String, PermRes> resources;

    /**
     * 将符合条件的Bean以Class集合的形式返回
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Class<?>[] scanPermClasses() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        if (this.packagesToScan != null && this.packagesToScan.length > 0) {
        	List<Class<?>> classes = new ArrayList<Class<?>>();
            AnnotationTypeFilter dictEnumFilter = new AnnotationTypeFilter(Perm.class);
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
                                classes.add(clazz);
                            } catch (ClassNotFoundException e) {
                                if (logger.isErrorEnabled()) {
                                    logger.error("perm class no found.", e);
                                }
                                continue;
                            }
                        }
                    }
                }
            }
            Class<?>[] array = new Class<?>[classes.size()];
            return classes.toArray(array);
        } else {
        	throw new NullPointerException("packagesToScan must be not null.");
        }
    }

	@Override
	public void setServletContext(ServletContext servletContext) {
		try {
			this.permClasses = this.scanPermClasses();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Map<String, PermSys> permSysMap = new LinkedHashMap<String,PermSys>();
		Map<String,PermRes> resourceMap = new LinkedHashMap<String,PermRes>();
		for (Class<?> permClass : permClasses) {
			PermSys permSys = loadPermSys(permClass, permSysMap);
			for (PermRes res : permSys.resourceMap.values()) {
				PermRes oldRes = resourceMap.put(res.code, res);
				Assert.isNull(oldRes, "Duplicate Resource Code: "+ res.code);
			}
		}
		this.systems = Collections.unmodifiableMap(permSysMap);
		this.resources = Collections.unmodifiableMap(resourceMap);
	}
	
	private PermSys loadPermSys(Class<?> permClass, Map<String,PermSys> permSysMap) {
		PermSys permSys = new PermSys(permClass);
		PermSys oldSys = permSysMap.put(permSys.type, permSys);
		Assert.isNull(oldSys, "Duplicate Permission System Type:"+permSys.type);
		return permSys;
	}
	
	public static class PermSys {
		public final Class<?> permClass;
		public final Perm perm;
		public final String type;
		public final String name;
		public final Map<String, PermRes> resourceMap;
		
		private PermSys(Class<?> permClass) {
			this.permClass = permClass;
			this.perm = permClass.getAnnotation(Perm.class);
			Assert.notNull(this.perm);
			this.type = this.perm.type();
			this.name = this.perm.name();
			LinkedHashMap<String, PermRes> resourceMap = new LinkedHashMap<String,PermRes>();
			this.loadResources(resourceMap);
			this.resourceMap = Collections.unmodifiableMap(resourceMap);
		}
		
		private void loadResources(LinkedHashMap<String, PermRes> resourceMap) {
			Class<?>[] resourceClasses = permClass.getDeclaredClasses();
			for (Class<?> resourceClass : resourceClasses) {
				this.loadResources(resourceClass, resourceMap);
			}
		}
		
		private void loadResources(Class<?> resourceClass, LinkedHashMap<String, PermRes> resourceMap) {
			PermRes permRes = new PermRes(resourceClass);
			resourceMap.put(permRes.name, permRes);
		}

		public String getName() {
			return name;
		}

		public Map<String, PermRes> getResourceMap() {
			return resourceMap;
		}
		
	}
	
	public static class PermRes {
		public final Class<?> resourceClass;
		public final PermResource permResource;
		public final String code;
		public final String name;
		public final Map<String, Permission> permissionMap;
		
		public PermRes(Class<?> resourceClass) {
			this.resourceClass = resourceClass;
			this.permResource = resourceClass.getAnnotation(PermResource.class);
			Assert.notNull(this.permResource);
			this.code = resourceClass.getSimpleName();
			this.name = this.permResource.name();
			LinkedHashMap<String, Permission> permissionMap = new LinkedHashMap<String,Permission>();
			this.loadPermissions(permissionMap);
			this.permissionMap = Collections.unmodifiableMap(permissionMap);
		}
		
		private void loadPermissions(LinkedHashMap<String, Permission> permissionMap) {
			Field[] fields = resourceClass.getDeclaredFields();
			for (Field field : fields) {
				this.loadPermission(field, permissionMap);
			}
		}
		
		private void loadPermission(Field field, LinkedHashMap<String, Permission> permissionMap) {
			Permission permission = new Permission(field);
			Permission oldPermission = permissionMap.put(permission.code, permission);
			Assert.isNull(oldPermission, "Duplicate permission code:" + permission.code);
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}

		public Map<String, Permission> getPermissionMap() {
			return permissionMap;
		}
		
	}
	
	public static class Permission {
		public final Field field;
		public final String code;
		public final String name;
		
		public Permission(Field field) {
			PermOperator oper = field.getAnnotation(PermOperator.class);
			Assert.isTrue(Modifier.isStatic(field.getModifiers()));
			Assert.isTrue(Modifier.isFinal(field.getModifiers()));
			Assert.isTrue(Modifier.isPublic(field.getModifiers()));
			Assert.isTrue(field.getType() == String.class);
			Assert.notNull(oper);

			this.field = field;
			this.name = oper.name();
			this.code = getPermissionCode(field);
		}

		private static String getPermissionCode(Field field) {
			try {
				return (String)field.get(field.getDeclaringClass());
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		public Field getField() {
			return field;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
		
	}

	public Map<String, PermRes> getResources() {
		return resources;
	}

	public Map<String, PermSys> getSystems() {
		return systems;
	}

	public Class<?>[] getPermClasses() {
		return permClasses;
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

}
