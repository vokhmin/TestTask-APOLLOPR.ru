package net.vokhmin.testtask.apollo.model;

import java.util.Date;

public class TwitterStatus implements Status {

	private String text;
	private TwitterUser user;
	private Date publishedAt;

	public TwitterStatus setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
		return this;
	}

	public TwitterStatus setText(String text) {
		this.text = text;
		return this;
	}

	public TwitterStatus setUser(TwitterUser user) {
		this.user = user;
		return this;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public Date getPublishedAt() {
		return publishedAt;
	}

}
