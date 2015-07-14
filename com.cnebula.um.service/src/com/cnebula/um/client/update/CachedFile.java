package com.cnebula.um.client.update;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class CachedFile {
	
	public URL file;
	public long lastModified;
	public byte[] content;
	public String contentType;
	
	public CachedFile(URL file, String contentType) {
		this.file = file;
		this.contentType = contentType;
	}
	
	public synchronized boolean refreshWhenModify() throws IOException{
		URLConnection con = file.openConnection();
		long curLastModified = con.getLastModified();
		if (curLastModified > lastModified){
			lastModified = curLastModified;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
            OutputStream out =  bout;
            byte[] buf = new byte[8192];
            InputStream fin = con.getInputStream();
            try{
            	int c = 0;
            	while ( (c = fin.read(buf)) > 0 ){
            		out.write(buf, 0 ,c);
            	}
            	out.close();
            }finally{
            	if (fin != null){
            		fin.close();
            	}
            }
            content = bout.toByteArray();
		}
		return false;
	}
	
}
