package com.socurites.jive.example.konal.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.socurites.jive.core.bot.JiveScriptBot;
import com.socurites.jive.core.bot.builder.JiveScriptReplyBuilder;
import com.socurites.jive.example.konal.web.domain.KonalDistrict;
import com.socurites.jive.example.konal.web.repository.KonalDistrictRepository;
import com.socurites.jive.ext.analyze.entity.JiveExtDomainEntity;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtBot;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtReplyBuilder;

@Controller
public class KonalWebController {
	
	private final static Logger logger = Logger.getLogger(KonalWebController.class);
	@Autowired
	private JiveScriptBot bot;
	
	@Autowired
	private KonalDistrictRepository disctrictRepository;
	
	@Autowired
	private RestTemplate restTemplate;
			
	@Autowired 
	private HttpEntity<String> httpEntity;
	
	@RequestMapping("/iCnetro")
    @ResponseBody
    String listen(@RequestParam("message")String message, @RequestParam("name")String name) {
		logger.debug("message=" + message);
		logger.debug("name=" + name);
		
		JiveScriptReplyBuilder reply = bot.reply(name, message, null);
		
		String weatherResult = ""; 
		logger.debug("===========================>  START");
		if ( bot instanceof JiveScriptExtBot ) {
			JiveScriptExtBot extBot = (JiveScriptExtBot)bot;
			reply.getReplyAsText();
			JiveExtDomainEntity extDomain = ((JiveScriptExtReplyBuilder)reply).getDomainEntity();
			logger.debug(extDomain);
			
			if ( extDomain.isEmpty() ) {
				return reply.getReplyAsText();
			}
			
			try {
				String sido = (extDomain.hasProp("sido") ) ? extDomain.getProp("sido") : "";
				String sigungu = (extDomain.hasProp("sigungu") ) ? extDomain.getProp("sigungu") : "";
				String town = (extDomain.hasProp("town") ) ? extDomain.getProp("town") : "";
				
				KonalDistrict standardDistrcit = disctrictRepository.getStandardDistrcit(sido, sigungu, town);
				logger.debug("standardDistrcit=" + standardDistrcit);

//				ResponseEntity<String>  responseEntity = restTemplate.exchange("http://apis.skplanetx.com/weather/current/hourly?lon=&village=" + standardDistrcit.getTown() + "&county=" + standardDistrcit.getSigungu() + "&lat=&city=" + standardDistrcit.getSido() + "&version=1", HttpMethod.GET, httpEntity, String.class);
//				String responseBody = responseEntity.getBody();
				String responseBody = "해당기능은 제공되지 않습니다.";
				
				JsonObject jsonObj = new JsonParser().parse(responseBody).getAsJsonObject();
				JsonArray hourlyWeatherArray = jsonObj.get("weather").getAsJsonObject()
						.get("hourly").getAsJsonArray()
				;
				
				if ( hourlyWeatherArray != null ) {
					JsonObject hourlyWeatherObject =  hourlyWeatherArray.get(0).getAsJsonObject();
					
					String sky = hourlyWeatherObject
							.get("sky").getAsJsonObject()
							.get("name")
							.getAsString()
							.replaceAll("\"", "");
				
					String tempCurr = hourlyWeatherObject
							.get("temperature").getAsJsonObject()
							.get("tc")
							.getAsString()
							.replaceAll("\"", "");
					
					String tempMax = hourlyWeatherObject
							.get("temperature").getAsJsonObject()
							.get("tmax")
							.getAsString()
							.replaceAll("\"", "");
					
					String tempMin = hourlyWeatherObject
							.get("temperature").getAsJsonObject()
							.get("tmin")
							.getAsString()
							.replaceAll("\"", "");
					
					String humidity = hourlyWeatherObject
							.get("humidity")
							.getAsString()
							.replaceAll("\"", "");
					
					weatherResult += "\n";
					weatherResult += sky + ", 현재 온도는 " + tempCurr + "(최저: " + tempMin + ", 최고: " + tempMax + "), 습도는 " + humidity + "%입니다.";
				} else {
					weatherResult += "\n";
					weatherResult += "내가 알고 있는 지역이 아닌 듯.."; 
				}
			} catch ( Exception e) {
				e.printStackTrace();
				return reply.getReplyAsText();
			}
		} else {
			weatherResult = message;
		}
		
		logger.debug("응답 : "+reply.getReplyAsText() + weatherResult);
        return reply.getReplyAsText() + weatherResult;
    }

}
