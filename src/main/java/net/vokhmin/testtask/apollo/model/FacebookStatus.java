package net.vokhmin.testtask.apollo.model;

import java.util.Date;

public class FacebookStatus implements Status {

	private String text;
	private FacebookUser user;
	private Date publishedAt;

	public FacebookStatus setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
		return this;
	}

	public FacebookStatus setText(String text) {
		this.text = text;
		return this;
	}

	public FacebookStatus setUser(FacebookUser user) {
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
