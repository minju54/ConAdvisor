package com.socurites.jive.core.core.analyze;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import scala.collection.Seq;

import com.socurites.jive.core.analyze.entity.JiveToken;
import com.socurites.jive.core.analyze.entity.JiveTokenModel;
import com.socurites.jive.core.bot.JiveScriptBot;
import com.twitter.penguin.korean.KoreanPosJava;
import com.twitter.penguin.korean.KoreanTokenJava;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer.KoreanToken;

import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.eunjeon.*;

public class JiveScriptKoreanDefaultAnalyzer extends JiveScriptKoreanAnalyzer {
	private final static Logger logger = Logger.getLogger(JiveScriptKoreanDefaultAnalyzer.class);
	
	public JiveScriptKoreanDefaultAnalyzer() {
		this.loadAnalyzer();
	}
	
	/**
	 * loading static TwitterKoreanProcessorJava class when creating a bot
	 */
	private void loadAnalyzer() {
//		String text = "우울한 노래 들려줘";
		String text = "알려줘 알려줄래 보여줄래 보여줘";
		
	    // Normalize
	    CharSequence normalized = TwitterKoreanProcessorJava.normalize(text);
	    logger.info("#normalized: " + normalized);
	    // 한국어를 처리하는 예시입니다ㅋㅋ #한국어


	    // Tokenize
	    Seq<KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized);
	    logger.info("#tokenize: " + TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(tokens));
	    // [한국어(Noun: 0, 3), 를(Josa: 3, 1),  (Space: 4, 1), 처리(Noun: 5, 2), 하는(Verb: 7, 2),  (Space: 9, 1), 예시(Noun: 10, 2), 입니(Adjective: 12, 2), 다(Eomi: 14, 1), ㅋㅋ(KoreanParticle: 15, 2),  (Space: 17, 1), #한국어(Hashtag: 18, 4)]


	    // Stemming
	    Seq<KoreanToken> stemmed = TwitterKoreanProcessorJava.stem(tokens);
	    logger.info("#stem: " + TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(stemmed));
	    // [한국어(Noun: 0, 3), 를(Josa: 3, 1),  (Space: 4, 1), 처리(Noun: 5, 2), 하다(Verb: 7, 2),  (Space: 9, 1), 예시(Noun: 10, 2), 이다(Adjective: 12, 3), ㅋㅋ(KoreanParticle: 15, 2),  (Space: 17, 1), #한국어(Hashtag: 18, 4)]
	    

	    // Phrase extraction
	    List<KoreanPhrase> phrases = TwitterKoreanProcessorJava.extractPhrases(tokens, true, true);
	    logger.info("#phrases: " + phrases);
	    // [한국어(Noun: 0, 3), 처리(Noun: 5, 2), 처리하는 예시(Noun: 5, 7), 예시(Noun: 10, 2), #한국어(Hashtag: 18, 4)]
	}
	
	@Override
	public void addKewords(List<String> keywords) {
		TwitterKoreanProcessorJava.addNounsToDictionary(keywords);
	}
	
	// 여기서 message 처리한다!!
	@Override
	public JiveTokenModel analyze(String text) {
		String mecab_text = text;
		text = text.replaceAll(" ", ""); // 띄어쓰기 다 지우고
		
		List<JiveToken> jiveTokens = new ArrayList<JiveToken>();
		List<String> tags = new ArrayList<String>();
		
		// TwitterKoreanProcessorJava 라이브러리 참조~!
	    CharSequence normalized = TwitterKoreanProcessorJava.normalize(text); // 정규화 : 입니닼ㅋㅋ -> 입니다 ㅋㅋ
	    Seq<KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized); // 토큰화 : 한국어를 -> 한국어 Noun, 를 Josa
	    Seq<KoreanToken> stemmedTokens = TwitterKoreanProcessorJava.stem(tokens); // 어근화 : 입니다 -> 이다 
	    List<KoreanTokenJava> koreanTokens = TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(stemmedTokens);
	    List<KoreanPhrase> phrases = TwitterKoreanProcessorJava.extractPhrases(tokens, true, false); // 어구 추출 : 한국어를 처리하는 -> 한국어, 처리
	    
	    // 이 자리에 twitter 대신 mecab 넣고 토큰화 시켜봐!!!
	    List<String> nouns = new ArrayList<String>();
	    List<String> verbs = new ArrayList<String>();
	    List<String> token_m = new ArrayList<String>();
	    List<String> pos_m = new ArrayList<String>();
	    Tagger tagger = new Tagger(); // mecab 형태소 분석의 준비

	    Sentence analyzed = tagger.tagSentence(mecab_text); 
	    logger.debug(">>> [JiveScriptKoreanDefaultAnalyzer] analyzed 출력: " + analyzed);
	 
	    for (int i = 0; i < analyzed.jNouns().size(); i++) {
	    	String word = analyzed.jNouns().get(i).toString().split("=")[1];
	    	int num_word = StringUtils.countMatches(word, "/");
		    for (int j = 0; j < num_word; j++) {
				String temp1 = word.split("/")[0];
				//System.out.println(">> token : " + temp1);
				token_m.add(temp1);
				word = StringUtils.replaceOnce(word, temp1 + "/", "");
				String temp2 = word.split("\\)")[0]; 
				temp2 = temp2 + ")";
				//System.out.println(">> pos : " + temp2);
				pos_m.add(temp2);
				word = StringUtils.replaceOnce(word, temp2, "");
			}
	    } 
	    
	    for (int i = 0; i < analyzed.jModifiers().size(); i++) {
	    	String word = analyzed.jModifiers().get(i).toString().split("=")[1];
	    	int num_word = StringUtils.countMatches(word, "/");
		    for (int j = 0; j < num_word; j++) {
				String temp1 = word.split("/")[0];
				//System.out.println(">> token : " + temp1);
				token_m.add(temp1);
				word = StringUtils.replaceOnce(word, temp1 + "/", "");
				String temp2 = word.split("\\)")[0]; 
				temp2 = temp2 + ")";
				//System.out.println(">> pos : " + temp2);
				pos_m.add(temp2);
				word = StringUtils.replaceOnce(word, temp2, "");
			}
		}
	    for (int i = 0; i < analyzed.jVerbs().size(); i++) {
	    	String word = analyzed.jVerbs().get(i).toString().split("=")[1];
	    	int num_word = StringUtils.countMatches(word, "/");
		    for (int j = 0; j < num_word; j++) {
				String temp1 = word.split("/")[0];
				//System.out.println(">> token : " + temp1);
				token_m.add(temp1);
				word = StringUtils.replaceOnce(word, temp1 + "/", "");
				String temp2 = word.split("\\)")[0]; 
				temp2 = temp2 + ")";
				//System.out.println(">> pos : " + temp2);
				pos_m.add(temp2);
				word = StringUtils.replaceOnce(word, temp2, "");
			}
		}
	    
	   

	    logger.debug(">>>[JiveScriptKoreanDefaultAnalyzer] token + pos");
	    for (int i = 0; i < token_m.size(); i++) {
	    	logger.debug("token[" + i + "]: " + token_m.get(i));
	    	logger.debug("pos[" + i + "]: " + pos_m.get(i));
	    }

	    // twitter 형태소 분석기로 만든 Token
	    JiveToken jiveToken = null;
	    for ( int i = 0; i < koreanTokens.size(); i++) {
	    	// JiveToken params : (String text, String pos)
	    	jiveToken = new JiveToken(koreanTokens.get(i).getText(), convertPos(koreanTokens.get(i).getPos()));
	    	jiveTokens.add(jiveToken);
	    }

	    // Mecab 형태소 분석기로 만든 Token
		JiveToken jiveToken_mecab = null;
		List<JiveToken> jiveTokens_mecab = new ArrayList<JiveToken>();
	    for (int i = 0; i < token_m.size(); i++) {
	    	jiveToken_mecab = new JiveToken(token_m.get(i), convertMecabPos(pos_m.get(i)));
	    	jiveTokens_mecab.add(jiveToken_mecab);
		}
	    
	    for ( int i = 0; i < phrases.size(); i++) {
	    	tags.add(phrases.get(i).text());
	    }
	    
	    // JiveTokenModel params : List<JiveToken> tokens, List<String> tags
	    // tags : 내 말 이해해 ? -> 내말 
	    JiveTokenModel jiveTokenModel = new JiveTokenModel(jiveTokens, tags);
	    JiveTokenModel jiveTokenModel_mecab = new JiveTokenModel(jiveTokens_mecab, tags);
	    //return jiveTokenModel; // twitter
	    return jiveTokenModel_mecab; // mecab 
	}
	
	public String convertPos(KoreanPosJava koreanPosJava) {
		if ( KoreanPosJava.Noun == koreanPosJava ) {
			return "noun";
		} else if ( KoreanPosJava.ProperNoun == koreanPosJava ) {
			return "noun";
		} else if ( KoreanPosJava.Adjective == koreanPosJava ) {
			return "adj";
		} else if ( KoreanPosJava.Verb == koreanPosJava ) {
			return "verb";
		} else if ( KoreanPosJava.Adverb == koreanPosJava ) {
			return "adv";
		} else if ( KoreanPosJava.Punctuation == koreanPosJava ) {
			return "punct";
		} else if ( KoreanPosJava.Alpha == koreanPosJava ) {
			return "alpha";
		} else {
			return "etc";
		}
	}
	
	public String convertMecabPos(String pos) {
		if (pos.contains("NNG") || pos.contains("NNP") || pos.contains("NP") || pos.contains("NNB") || pos.contains("NNBC") || pos.contains("NR")) {
			return "noun";
		} else if (pos.contains("VV") || pos.contains("VX") || pos.contains("VCP") || pos.contains("VCN")) {
			return "verb";
		} else if (pos.contains("VA") || pos.contains("MAG")) {
			return "adj";
		} else if (pos.contains("SL") || pos.contains("SN")) {
			return "alpha";
		} else {
			return "etc";		
		}
	}
	
	public static void main(String[] args) {
		JiveScriptKoreanDefaultAnalyzer test = new JiveScriptKoreanDefaultAnalyzer();
	}
}
