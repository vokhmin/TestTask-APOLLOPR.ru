package net.vokhmin.testtask.apollo.model;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

public class TwitterSession {
	
	private Twitter twitter;
	private RequestToken requestToken;
	private TwitterUser user;	
	
	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	public RequestToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}

	public TwitterUser getUser() {
		return user;
	}
	
}
