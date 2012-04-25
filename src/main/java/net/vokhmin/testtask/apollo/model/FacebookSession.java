package net.vokhmin.testtask.apollo.model;

import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;

import twitter4j.auth.RequestToken;

public class FacebookSession {
	
	private FacebookConnectionFactory connectionFactory;
	private Connection<Facebook> connection;
	private FacebookUser user;
	private OAuth2Operations oAuth;	
	
	public void setUser(FacebookUser user) {
		this.user = user;
	}

	public FacebookUser getUser() {
		return user;
	}

	public FacebookConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(FacebookConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Connection<Facebook> getConnection() {
		return connection;
	}

	public void setConnection(Connection<Facebook> connection) {
		this.connection = connection;
	}

	public void setOAuth(OAuth2Operations oauthOperations) {
		this.oAuth = oauthOperations;
	}

	public OAuth2Operations getOAuth() {
		return oAuth;
	}

}
