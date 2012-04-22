package net.vokhmin.testtask.apollo.model;

import java.util.Date;

public abstract class AbstractStatus implements Status {

	private String text;
	private TwitterUser user;
	private Date publishedAt;

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
