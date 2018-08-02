package com.socurites.jive.example.util.bot;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.socurites.jive.core.bot.JiveScriptBot;
import com.socurites.jive.example.util.console.DumpConsoleOut;
import com.socurites.jive.example.util.console.PrettyConsoleOut;
import com.socurites.jive.ext.analyze.entity.JiveScriptExtBotBuilder;

public class JiveScriptStandaloneBot {
	private final static Logger logger = Logger.getLogger(JiveScriptStandaloneBot.class);
	public void run(String templateDirPath, String keywordDirPath, boolean enableAnalyze) {
		PrettyConsoleOut.printBanner();
		PrettyConsoleOut.printInfo("building bot");

		JiveScriptBot bot = (new JiveScriptExtBotBuilder())
				.analyze(enableAnalyze)
				.parse(templateDirPath, keywordDirPath)
				.build()
		;
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			PrettyConsoleOut.printInputScreen("송준이");
			String message = scanner.nextLine();
			
			if (message.equals("/quit")) {
				scanner.close();
				System.exit(0);
			} else if (message.equals("/dump topics")) {
				DumpConsoleOut.dumpTopics(bot.getTopics());
			} else if (message.equals("/dump sorted")) {
				DumpConsoleOut.dumpSorted(bot.getTopics());
			} else if (message.equals("/last")) {
				logger.debug("You last matched: " + bot.lastMatch("localuser"));
			} else if (message.equals("/help")) {
				logger.debug("Available commands:\n"
								+ "  /last           Print the last matched trigger.\n"
								+ "  /dump topics    Pretty-print the topic structure.\n"
								+ "  /dump sorted    Pretty-print the sorted trigger structure.\n"
								+ "  /help           Show this message.\n"
								+ "  /quit           Exit the program.\n");
			} else {
				/*
				JiveScriptReplyBuilder reply = bot.reply("송준이", message);
				System.out.println("Bot> " + reply.getReplyAsText());
				if ( bot instanceof JiveScriptExtBot ) {
					JiveScriptExtBot extBot = (JiveScriptExtBot)bot;
					System.out.println(((JiveScriptExtReplyBuilder)reply).getDomainEntity());
				}
				if ( enableAnalyze ) {
					System.out.println(reply);
					System.out.println(reply.getRequestBuilder());
					JiveTokenModel jiveTokenModel = reply.getRequestBuilder().getJiveTokenModel();
					System.out.println(jiveTokenModel);
				}
				*/
			}
		}
	}
}
