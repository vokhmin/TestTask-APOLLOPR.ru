package net.vokhmin.testtask.apollo.model;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

public class TwitterSession {
	
	private OAuthToken requestToken;
	private TwitterUser user;
	private TwitterConnectionFactory connectionFactory;
	private OAuth1Operations oAuth;
	private Connection<Twitter> connection;	
	
	public void reset() {
		user = null;
		connection = null;
		oAuth = null;
	}
	
	public OAuthToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(OAuthToken requestToken) {
		this.requestToken = requestToken;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}

	public TwitterUser getUser() {
		return user;
	}

	public void setConnectionFactory(TwitterConnectionFactory factory) {
		this.connectionFactory = factory;
	}

	public OAuth1ConnectionFactory<Twitter> getConnectionFactory() {
		return connectionFactory;
	}

	public void setOAuth(OAuth1Operations oAuthOperations) {
		this.oAuth = oAuthOperations;
	}

	public OAuth1Operations getOAuth() {
		return oAuth;
	}

	public void setConnection(Connection<Twitter> connection) {
		this.connection = connection;
	}

	public Connection<Twitter> getConnection() {
		return connection;
	}
	
}
