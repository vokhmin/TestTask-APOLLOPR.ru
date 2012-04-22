package net.vokhmin.testtask.apollo.model;

public class FacebookUser implements User{
	public static final String SERVICE_NAME = "Facebook";
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
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
