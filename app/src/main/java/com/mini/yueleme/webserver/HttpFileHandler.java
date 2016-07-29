package com.mini.yueleme.webserver;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

public class HttpFileHandler implements HttpRequestHandler {

	private static final String TAG = "HttpFileHandler";
	private String webRoot;
	private List<String> allFiles;

	private Context context;
	public HttpFileHandler(Context context, final String webRoot) {
		this.context = context;
		this.webRoot = webRoot;
		try {
			String[] files = context.getAssets().list("");
			if(files!=null){
				allFiles = Arrays.asList(files);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {

		try {
			String target = URLDecoder.decode(request.getRequestLine().getUri(),
					"UTF-8");
			Log.i(TAG, "target="+target);
			if("/".equals(target)){
				response.setStatusCode(HttpStatus.SC_OK);
				HttpEntity entity = null;
				InputStream is = this.context.getAssets().open("download.html");
				entity = new InputStreamEntity(is, is.available());
				response.setHeader("Content-Type", "text/html");
				response.setEntity(entity);
			}else if("/Android".equals(target)){
				File file = new File(this.context.getPackageManager().getApplicationInfo(
						this.context.getPackageName(), 0).sourceDir);
				String contentType = URLConnection
						.guessContentTypeFromName(file.getAbsolutePath());
				contentType = null == contentType ? "charset=UTF-8"
						: contentType + "; charset=UTF-8";
				HttpEntity entity = new FileEntity(file, contentType);
				response.setHeader("Content-Type", contentType);
				response.setHeader("Content-Type", "application/octet-stream");
				response.addHeader("Content-Disposition",
						"attachment;filename=" + file.getName());
				response.setEntity(entity);
			}else{
				String fileName = target.substring(target.indexOf("/")+1);
				if(allFiles!=null&&allFiles.contains(fileName)){
					response.setStatusCode(HttpStatus.SC_OK);
					HttpEntity entity = null;
					InputStream is = this.context.getAssets().open(fileName);
					entity = new InputStreamEntity(is, is.available());
					response.setHeader("Content-Type", "text/html");
					response.setEntity(entity);
				}else{
				response.setStatusCode(HttpStatus.SC_NOT_FOUND);
				StringEntity entity = new StringEntity(
						"<html><body><h1>Error 404, file not found.</h1></body></html>",
						"UTF-8");
				response.setHeader("Content-Type", "text/html");
				response.setEntity(entity);
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean hasWfsDir(File f) {
		String path = f.isDirectory() ? f.getAbsolutePath() + "/" : f
				.getAbsolutePath();
		return path.indexOf("/.wfs/") != -1;
	}

}
