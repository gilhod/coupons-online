package com.gil.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;

public class FilesUtil {
	
	
	public static void uploadFile(InputStream uploadedInputStream, String fileLocation) throws ApplicationException {
		try {
			OutputStream out = new FileOutputStream(new File(fileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(fileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		}
		
	}
	
	
	public static void deleteFile(String fileLoaction){
		File file = new File(fileLoaction);
		if(file.exists()){
			file.delete();
		}
		
	}

}
