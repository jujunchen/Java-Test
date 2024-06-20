package util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class DeployUtils {
	/**
	 * 读取jar包中所有类文件
	 */
	public static Set<String> readJarFile(String jarAddress) {
	    Set<String> classNameSet = new HashSet<>();
	    
	    try(JarFile jarFile = new JarFile(jarAddress)) {
	    	Enumeration<JarEntry> entries = jarFile.entries();//遍历整个jar文件
		    while (entries.hasMoreElements()) {
		        JarEntry jarEntry = entries.nextElement();
		        String name = jarEntry.getName();
		        if (name.endsWith(".class")) {
		            String className = name.replace(".class", "").replaceAll("/", ".");
		            classNameSet.add(className);
		        }
		    }
		} catch (Exception e) {
			log.warn("加载jar包失败", e);
		}
	    return classNameSet;
	}

//	public static InputStream readPomJarFile(File jarAddress) {
//		try(JarFile jarFile = new JarFile(jarAddress)) {
//			//遍历整个jar文件
//			Enumeration<JarEntry> entries = jarFile.entries();
//			while (entries.hasMoreElements()) {
//				JarEntry jarEntry = entries.nextElement();
//				String name = jarEntry.getName();
//				if (PluginConstants.POM.equals(name)) {
//					return jarFile.getInputStream(jarEntry);
//				}
//			}
//		} catch (Exception e) {
//			log.warn("加载jar包失败", e);
//		}
//		return null;
//	}

	/**
	 * 方法描述 判断class对象是否带有spring的注解
	 */
//	public static boolean isSpringBeanClass(Class<?> cls) {
//	    if (cls == null) {
//	        return false;
//	    }
//	    //是否是接口
//	    if (cls.isInterface()) {
//	        return false;
//	    }
//	    //是否是抽象类
//	    if (Modifier.isAbstract(cls.getModifiers())) {
//	        return false;
//	    }
//	    //自定义注解
//	    if (cls.getAnnotation(Supplier.class) != null) {
//	        return true;
//	    }
//	    if (cls.getAnnotation(Component.class) != null) {
//	        return true;
//	    }
//	    if (cls.getAnnotation(Mapper.class) != null) {
//	        return true;
//	    }
//	    if (cls.getAnnotation(Service.class) != null) {
//	        return true;
//	    }
//	    return false;
//	}
	
	
//	public static boolean isController(Class<?> cls) {
//		if (cls.getAnnotation(Controller.class) != null) {
//			return true;
//		}
//		if (cls.getAnnotation(RestController.class) != null) {
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 类名首字母小写 作为spring容器beanMap的key
	 */
	public static String transformName(String className) {
	    String tmpstr = className.substring(className.lastIndexOf(".") + 1);
	    return tmpstr.substring(0, 1).toLowerCase() + tmpstr.substring(1);
	}

	public static Set<String> readClassFile(String path) {
		if (path.endsWith(".class")) {
			return readJarFile(path);
		} else {
			List<File> pomFiles =  FileUtil.loopFiles(path.toString(), file -> file.getName().endsWith(".class"));
			Set<String> classNameSet = new HashSet<>();
			for (File file : pomFiles) {
				String className = StrUtil.subBetween(file.getPath(), "classes" + File.separator, ".class").replace(File.separator, ".");
				classNameSet.add(className);
			}
			return classNameSet;
		}
	}

	public static void main(String[] args) {
		readClassFile("D:\\javaprojects\\Java-Test\\target");
	}
}

