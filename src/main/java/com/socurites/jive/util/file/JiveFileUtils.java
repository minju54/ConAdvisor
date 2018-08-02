package com.socurites.jive.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

/**
 * File Utils
 * 
 * @author socurites
 *
 */
public class JiveFileUtils {
	/**
	 * return directory file accordint to path
	 * 
	 * @param dirPath	absolute path or relative path
	 * @return
	 */
	public static File getDir(String dirPath) {
		Path path = Paths.get(dirPath);
		
		if (JiveFileUtils.class.getResource("/konal/rive") == null ) {
			//System.out.println("file is null");
		} else {
			//System.out.println("file is not null");
		}
		
		File templateDir = null;
		if ( path.isAbsolute() ) {
			templateDir = new File(dirPath);
		} else {
			templateDir = new File(JiveFileUtils.class.getClassLoader().getResource(dirPath).getFile());
			//templateDir = new File(dirPath + "/");
		//	System.out.println("---[JiveFileUtils] templateDir : " + templateDir);
//			InputStream input = JiveFileUtils.class.getResourceAsStream("konal/rive/0001.rive");
//			try {
//				templateDir = File.createTempFile("tempfile", ".tmp");
//				IOUtils.copy(input, new FileOutputStream(templateDir));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}
		
		return templateDir;
	}
}
