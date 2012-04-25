package net.vokhmin.testtask.apollo.model;

public class TwitterUser implements User{	
	public static final String SERVICE_NAME = "Twitter";
	
	String screenName;

	public TwitterUser setScreenName(String screenName) {
		this.screenName = screenName;
		return this;
	}

	@Override
	public String getDisplayName() {
		return screenName;
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

}
