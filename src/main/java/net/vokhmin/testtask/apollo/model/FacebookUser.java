package net.vokhmin.testtask.apollo.model;

public class FacebookUser implements User{
	public static final String SERVICE_NAME = "Facebook";
	
	public FacebookUser setScreenName(String screenName) {
		this.screenName = screenName;
		return this;
	}

	String screenName;

	@Override
	public String getScreenName() {
		return screenName;
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

}
