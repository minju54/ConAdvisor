package com.socurites.jive.example.konal.bot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.socurites.jive.core.bot.JiveScriptBot;


public class PythonModelController {
	private final static Logger logger = Logger.getLogger(PythonModelController.class);
	
	public static MessageModel getResult(String msg) {
		HttpURLConnection connection = null;
		String send_msg = msg;
		MessageModel m_msg = new MessageModel();
		try {
			//URL url = new URL("http://192.168.202.33:5000/api");
			URL url = new URL("http://54.94.181.203:5000/api");
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(20000); // 타임아웃 20초
			connection.setRequestProperty("Content-type", "application/json");

			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			// send request
//			JSONObject jsonParam = new JSONObject();
//			jsonParam.put("input", send_msg);
//			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//			wr.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
//		    logger.debug(">>> wr : " + jsonParam.getString("input"));
//			wr.flush();
//			wr.close();
			
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("input", send_msg);
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			wr.write(jsonParam.toString());
			wr.flush();
			wr.close();
			
			// get responses
			int statusCode = connection.getResponseCode();
			//InputStream in = connection.getInputStream();
			InputStream in = null;
			if (statusCode == 200) {
				in = connection.getInputStream();
				logger.info(">>> statusCode 200");
				
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(in, "UTF-8"));
				
				//m_msg.setCategory((String)jsonObject.get("category"));
				m_msg.setCategory((List<String>)jsonObject.get("category"));
				m_msg.setTokens((String)jsonObject.get("tokens"));
				
//				for (int i = 0; i < m_msg.getCategory().size(); i++) {
//					logger.debug(">>> m_msg category : " + m_msg.getCategory().get(i));
//				}
//				logger.debug(">>> msg tokens :" + m_msg.getTokens());
				
//				BufferedReader rd = new BufferedReader(new InputStreamReader(in));
//				String line;
//		        StringBuffer response = new StringBuffer(); 
//		        while((line = rd.readLine()) != null) {
//		        	response.append(line);
//		            response.append('\r');
//		            //logger.debug(">>> line : " + line);
//		        }
//		        rd.close();
			} else {
				in = connection.getErrorStream();
				logger.info(">>> statusCode : " + statusCode);
				BufferedReader rd = new BufferedReader(new InputStreamReader(in));
				String line;
		        StringBuffer response = new StringBuffer(); 
		        while((line = rd.readLine()) != null) {
		        	response.append(line);
		            response.append('\r');
		            //logger.debug(">>> line : " + line);
		        }
		        rd.close();
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return m_msg;
	}
}


