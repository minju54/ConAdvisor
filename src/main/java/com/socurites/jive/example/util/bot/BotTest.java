package com.socurites.jive.example.util.bot;

import java.util.Scanner;

import com.socurites.jive.core.analyze.entity.JiveTokenModel;
import com.socurites.jive.core.bot.JiveScriptBot;
import com.socurites.jive.core.bot.builder.JiveScriptReplyBuilder;
import com.socurites.jive.example.util.console.DumpConsoleOut;
import com.socurites.jive.example.util.console.PrettyConsoleOut;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtBot;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtBotBuilder;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtReplyBuilder;

public class BotTest {
	public void run(String templateDirPath, String keywordDirPath, boolean enableAnalyze) {
//		PrettyConsoleOut.printBanner();
//		PrettyConsoleOut.printInfo("building bot");
//
//		JiveScriptBot bot = (new JiveScriptExtBotBuilder())
//				.analyze(enableAnalyze)
//				.parse(templateDirPath, keywordDirPath)
//				.build()
//		;
//		
//
//			PrettyConsoleOut.printInputScreen("송준이");
//			String message = "똑똑하네";
//
//
//				JiveScriptReplyBuilder reply = bot.reply("송준이", message);
//				System.out.println("Bot> " + reply.getReplyAsText());
//				if ( bot instanceof JiveScriptExtBot ) {
//					JiveScriptExtBot extBot = (JiveScriptExtBot)bot;
//					System.out.println(((JiveScriptExtReplyBuilder)reply).getDomainEntity());
//				}
//				if ( enableAnalyze ) {
//					System.out.println(reply);
//					System.out.println(reply.getRequestBuilder());
//					JiveTokenModel jiveTokenModel = reply.getRequestBuilder().getJiveTokenModel();
//					System.out.println(jiveTokenModel);
//				}
//			
		}
	
	/** template file directory path. */
	private static final String TEMPLATE_DIR_PATH = "konal/rive";
	private static final String KEYWORD_DIR_PATH = null;
	
	/** analyzing user request. */
	private static final boolean ENABLE_ANALYZE = true;
	
	public static void main(String[] args) {
		BotTest botDemo = new BotTest();
		botDemo.run(TEMPLATE_DIR_PATH, KEYWORD_DIR_PATH, ENABLE_ANALYZE);
	}
	
}
