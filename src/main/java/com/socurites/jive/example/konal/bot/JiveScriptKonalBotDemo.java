package com.socurites.jive.example.konal.bot;

//import org.python.util.PythonInterpreter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.socurites.jive.example.util.bot.JiveScriptStandaloneBot;


@SpringBootApplication
public class JiveScriptKonalBotDemo {
	/** template file directory path. */
	private static final String TEMPLATE_DIR_PATH = "konal/rive";
	private static final String KEYWORD_DIR_PATH = null;
	
	/** analyzing user request. */
	private static final boolean ENABLE_ANALYZE = true;
	
	public static void main(String[] args) {
		JiveScriptStandaloneBot botDemo = new JiveScriptStandaloneBot();
		SpringApplication.run(JiveScriptKonalBotDemo.class, args);
		botDemo.run(TEMPLATE_DIR_PATH, KEYWORD_DIR_PATH, ENABLE_ANALYZE);
	}
}
