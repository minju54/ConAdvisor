package com.socurites.jive.example.util.console;

import java.util.List;

import org.apache.log4j.Logger;

import com.socurites.jive.core.bot.JiveScriptBot;
import com.socurites.jive.core.engine.old.TopicManager;

/*-----------------------*/
/*-- Developer Methods --*/
/*-----------------------*/
public class DumpConsoleOut {
	/**
	 * DEVELOPER: Dump the trigger sort buffers to the terminal.
	 */
	private final static Logger logger = Logger.getLogger(DumpConsoleOut.class);
	public static void dumpSorted(TopicManager topics) {
		for ( String topicId : topics.getTopicIds() ) {
			String[] triggers = topics.addTopic(topicId).listTriggers();

			// Dump.
			logger.debug("Topic: " + topicId);
			for (int i = 0; i < triggers.length; i++) {
				logger.debug("       " + triggers[i]);
			}
		}
	}
	
	
	/**
	 * DEVELOPER: Dump the entire topic/trigger/reply structure to the terminal.
	 */
	public static void dumpTopics (TopicManager topics) {
		logger.debug("{");
		for ( String topicId : topics.getTopicIds() ) {
			String extra = "";

			// Includes? Inherits?
			String[] includes = topics.addTopic(topicId).includes();
			String[] inherits = topics.addTopic(topicId).inherits();
			if (includes.length > 0) {
				extra = "includes ";
				for (int i = 0; i < includes.length; i++) {
					extra += includes[i] + " ";
				}
			}
			if (inherits.length > 0) {
				extra += "inherits ";
				for (int i = 0; i < inherits.length; i++) {
					extra += inherits[i] + " ";
				}
			}
			logger.debug("  '" + topicId + "' " + extra + " => {");

			// Dump the trigger list.
			String[] trigList = topics.addTopic(topicId).listTriggers();
			for (int i = 0; i < trigList.length; i++) {
				String trig = trigList[i];
				logger.debug("    '" + trig + "' => {");

				// Dump the replies.
				List<String> replies = topics.addTopic(topicId).addTrigger(trig).getReplies();
				if (replies.size() > 0) {
					logger.debug("      'reply' => [");
					for ( String reply : replies ) {
						System.out.println("        '" + reply + "',");
					}
					logger.debug("      ],");
				}

				// Dump the conditions.
				List<String> conditionalReplies = topics.addTopic(topicId).addTrigger(trig).getConditionalReplies();
				if (conditionalReplies.size() > 0) {
					logger.debug("      'condition' => [");
					
					for ( String conditionalReply : conditionalReplies ) {
						logger.debug("        '" + conditionalReply + "',");
					}
					logger.debug("      ],");
				}

				// Dump the redirects.
				List<String> redirects = topics.addTopic(topicId).addTrigger(trig).getRedirects();
				if (redirects.size() > 0) {
					logger.debug("      'redirect' => [");
					
					for ( String redirect : redirects ) {
						logger.debug("        '" + redirect + "',");
					}
					logger.debug("      ],");
				}

				logger.debug("    },");
			}

			logger.debug("  },");
		}
	}
}
