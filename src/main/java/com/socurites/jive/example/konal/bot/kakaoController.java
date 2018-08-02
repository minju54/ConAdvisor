package com.socurites.jive.example.konal.bot;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import com.socurites.jive.core.bot.JiveScriptBot;
import com.socurites.jive.core.bot.builder.JiveScriptReplyBuilder;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtBotBuilder;

@RestController
public class kakaoController {
	private final static Logger logger = Logger.getLogger(kakaoController.class);
	
	/** template file directory path. */
	private static final String TEMPLATE_DIR_PATH = "konal/rive";
	private static final String KEYWORD_DIR_PATH = null;
	
	/** analyzing user request. */
	private static final boolean ENABLE_ANALYZE = true;
	JiveScriptBot bot = (new JiveScriptExtBotBuilder())
			.analyze(ENABLE_ANALYZE)
			.parse(TEMPLATE_DIR_PATH, KEYWORD_DIR_PATH)
			.build()
	;
	
	// 기본 테스트
	@RequestMapping(value = "/", method =RequestMethod.GET)
	public String test() {
		return "test success!!";
	}
	
	// 키보드
    @RequestMapping(value = "/keyboard", method = RequestMethod.GET)
    public String keyboard() {
        JSONObject jobjBtn = new JSONObject();
        jobjBtn.put("type", "text");

        return jobjBtn.toJSONString();
    }
    
    // 메시지
    @RequestMapping(value = "/message", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public String message(@RequestBody JSONObject resObj) {
    	String input_msg;
    	input_msg = (String)resObj.get("content");

    	// input_msg -> 의도 결과
    	MessageModel m_msg = new MessageModel();
    	m_msg = PythonModelController.getResult(input_msg);
    	logger.info(">>> [KakaoController] category : " + m_msg.getCategory());
    	logger.info(">>> [KakaoController] tokens : " + m_msg.getTokens());
    	
    	JiveScriptReplyBuilder reply = bot.reply("테스트고객", m_msg.getTokens(),  m_msg.getCategory());

    	JSONObject jobjRes = new JSONObject();
    	JSONObject jobjText = new JSONObject();
    	
    	jobjText.put("text", reply.getReplyAsText());
    	jobjRes.put("message", jobjText);
    	
    	return jobjRes.toJSONString();
    }
}