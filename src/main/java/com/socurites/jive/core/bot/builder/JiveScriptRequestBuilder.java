package com.socurites.jive.core.bot.builder;

import java.util.List;

import org.apache.log4j.Logger;

import com.socurites.jive.core.analyze.entity.JiveToken;
import com.socurites.jive.core.analyze.entity.JiveTokenModel;
import com.socurites.jive.core.core.analyze.JiveScriptKoreanAnalyzer;
import com.socurites.jive.core.engine.old.Util;
import com.socurites.jive.core.parser.entity.JiveScriptEntity;

public class JiveScriptRequestBuilder {
	private final static Logger logger = Logger.getLogger(JiveScriptRequestBuilder.class);
	
	private JiveScriptEntity entityBuilder;
	private boolean enableAnalyze = false;
	private JiveScriptKoreanAnalyzer analyzer;
	private JiveTokenModel jiveTokenModel;
	
	public JiveScriptRequestBuilder analyze(boolean enableAnalyze) {
		this.enableAnalyze = enableAnalyze;
		
		return this;
	}
	
	public JiveScriptRequestBuilder analyzer(JiveScriptKoreanAnalyzer analyzer) {
		this.analyzer = analyzer;
		
		return this;
	}
	

	public JiveScriptRequestBuilder entityBuilder(JiveScriptEntity entityBuilder) {
		this.entityBuilder = entityBuilder;
		
		return this;
	}
	
	public String build(String message){
		message = message.toLowerCase(); // 영어에서 적용되는 소문자로 바꾸기
		/**
		 * Run substitutions on a string.
		 *
		 * @param entityBuilder.getSortedSubs() -> sorted : The sorted list of substitution patterns to process.
		 * @param entityBuilder.getSubs() -> hash : A hash that pairs the sorted list with the replacement texts.
		 * @param message -> text : The text to apply the substitutions to.
		 */
		message = Util.substitute(entityBuilder.getSortedSubs(), entityBuilder.getSubs(), message);
		logger.info(">> [JiveScriptRequestBuilder] msg substitute: " + message); // 얘 뭐하는 애일까?? 차이가 없음
		
		if ( this.enableAnalyze ) {
			message = getAnalyzedMessage(message);
		}
		logger.info(">> [JiveScriptRequestBuilder] msg getAnalyzedMessage: " + message); // 정규화를 거쳐서 나온애 같다 --> 얘를 살펴보자!
	
		message = message.replaceAll("[^a-z0-9가-힣 \\?]", "");
		logger.info(">> [JiveScriptRequestBuilder] msg replaceAll: " + message); // 특수문자 처리하는거 같은데, 전 함수에서 다 처리되는거 같음
		return message;
	}
	
	
	/**
	 * @return the jiveTokenModel
	 */
	public JiveTokenModel getJiveTokenModel() {
		return jiveTokenModel;
	}

	// 뭐하는애니
	private String getAnalyzedMessage(String message) {
		// analyze함수 안으로 들어가서 몰 할까?? -> JiveTokenModel 얘 호출함
		this.jiveTokenModel = this.analyzer.analyze(message); 
		logger.info(">> [JiveScriptRequestBuilder] msg analyze after: " + message);
		
		List<JiveToken> tokens = this.jiveTokenModel.getTokens();
		StringBuffer sb = new StringBuffer();
		
		for ( JiveToken token : tokens ) {
			if ( isMeaningfulPos(token.getText(), token.getPos()) )
				sb.append(token.getText() + " ");
		}
		message = sb.toString().trim();
		
		logger.info(">> [JiveScriptRequestBuilder] msg token after: " + message);
		return message;
	}
	
	private boolean isMeaningfulPos(String text, String pos) {
		if ( "noun".equals(pos)
			|| "verb".equals(pos)
			|| "adj".equals(pos)
			|| "alpha".equals(pos)) {
			return true;
		} 
		/*
		else if ( "punct".equals(pos) && "?".equals(text) ) {
			return true;
		}
		*/
		return false;
			
	}
}
