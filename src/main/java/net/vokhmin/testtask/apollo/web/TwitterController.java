package net.vokhmin.testtask.apollo.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.vokhmin.testtask.apollo.model.HomeModel;
import net.vokhmin.testtask.apollo.model.TwitterSession;
import net.vokhmin.testtask.apollo.model.TwitterStatus;
import net.vokhmin.testtask.apollo.model.TwitterUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "/twitter")
@SessionAttributes({ "twitter", "home" })
public class TwitterController {
	protected static Logger log = LoggerFactory
			.getLogger(TwitterController.class);

	@Autowired
	TwitterConnectionFactory factory;

	@ModelAttribute("twitter")
	public TwitterSession populateTwitter() {
		TwitterSession twitterSession = new TwitterSession();
		twitterSession.setConnectionFactory(factory);
		return twitterSession;
	}

	@RequestMapping(value = "/signin")
	void signin(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("twitter") TwitterSession twitterSession)
			throws ServletException {
		try {
    		if (twitterSession.getConnection() != null) {
    			log.debug("there is facebook connection already");
    			response.sendRedirect(request.getContextPath()+ "/");
    		}			StringBuffer callbackURL = request.getRequestURL();
			int index = callbackURL.lastIndexOf("/");
			callbackURL.replace(index, callbackURL.length(), "").append(
					"/callback");

			twitterSession.setOAuth(twitterSession.getConnectionFactory()
					.getOAuthOperations());
			OAuthToken requestToken = twitterSession.getOAuth()
					.fetchRequestToken(callbackURL.toString(), null);
			twitterSession.setRequestToken(requestToken);
			String authorizeUrl = twitterSession.getOAuth().buildAuthorizeUrl(
					requestToken.getValue(), OAuth1Parameters.NONE);
			log.debug("return redirect to " + authorizeUrl);
			response.sendRedirect(authorizeUrl);
		} catch (Exception e) {
			log.error("Unsuccessful twitter signin - " + e.getMessage(), e);
			throw new ServletException(e);
		}
	}

	@RequestMapping(value = "/callback")
	void callback(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("home") HomeModel hm,
			@ModelAttribute("twitter") TwitterSession twitterSession,
			@RequestParam("oauth_verifier") String oauthVerifier)
			throws ServletException {
		try {
			OAuthToken accessToken = twitterSession.getOAuth()
					.exchangeForAccessToken(
							new AuthorizedRequestToken(
									twitterSession.getRequestToken(),
									oauthVerifier), null);
			Connection<Twitter> connection = twitterSession
					.getConnectionFactory().createConnection(accessToken);
			twitterSession.setConnection(connection);
			Twitter twitter = connection.getApi();
			twitterSession.setRequestToken(null);

			TwitterUser user = new TwitterUser().setScreenName(twitter
					.userOperations().getUserProfile().getName());
			twitterSession.setUser(user);
			hm.getUsers().add(user);
			List<Tweet> timeline = twitter.timelineOperations()
					.getHomeTimeline();
			log.debug("was getting {} tweets", timeline.size());
			for (Tweet t : timeline) {
				hm.addStatus(new TwitterStatus().setUser(user)
						.setText(t.getText()).setPublishedAt(t.getCreatedAt()));
			}

			response.sendRedirect(request.getContextPath() + "/");
		} catch (Exception e) {
			log.error("Unsuccessful twitter signin - " + e.getMessage(), e);
			throw new ServletException(e);
		}
	}

	@RequestMapping(value = "/logout")
	void logout(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("home") HomeModel hm,
			@ModelAttribute("twitter") TwitterSession twitterSession)
			throws IOException {
		// request.getSession().invalidate();
		if (twitterSession.getUser() != null)
			hm.removeUser(twitterSession.getUser());
		twitterSession.reset();
		response.sendRedirect(request.getContextPath() + "/");
	}
}
