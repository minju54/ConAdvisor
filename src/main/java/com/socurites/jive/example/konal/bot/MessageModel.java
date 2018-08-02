package com.socurites.jive.example.konal.bot;

import java.util.List;

public class MessageModel {
	private List<String> category;
	private String tokens;
	
	public void setCategory(List<String> category) {
		this.category = category;
	}
	
	public void setTokens(String tokens) {
		this.tokens = tokens;
	}
	public List<String> getCategory() {
		return category;
	}
	
	public String getTokens() {
		return tokens;
	}
}
