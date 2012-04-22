package net.vokhmin.testtask.apollo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.vokhmin.testtask.apollo.model.HomeModel;
import net.vokhmin.testtask.apollo.model.TwitterSession;
import net.vokhmin.testtask.apollo.model.TwitterStatus;
import net.vokhmin.testtask.apollo.model.TwitterUser;
import net.vokhmin.testtask.apollo.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

@Controller
@RequestMapping(value = "/twitter")
@SessionAttributes({"twitter", "home"})
public class TwitterController {
	protected static Logger log = LoggerFactory.getLogger(TwitterController.class); 
	
	@ModelAttribute("twitter")
	public TwitterSession populateTwitter() {
	       TwitterSession twitterSession = new TwitterSession(); 
	       twitterSession.setTwitter(new TwitterFactory().getInstance());
	       return twitterSession;
	}
	
	@RequestMapping(value = "/signin")
	void signin(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("twitter") TwitterSession twitterSession
			) throws ServletException {
        //Twitter twitter = new TwitterFactory().getInstance();
        //request.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            RequestToken requestToken = twitterSession.getTwitter().getOAuthRequestToken(callbackURL.toString());
            twitterSession.setRequestToken(requestToken);
            //request.getSession().setAttribute("requestToken", requestToken);
            log.debug("return redirect to " + requestToken.getAuthenticationURL());
            response.sendRedirect(requestToken.getAuthenticationURL());

        } catch (TwitterException e) {
            log.error("Failed to retrieve access token - "
                + e.getMessage(), e);
            throw new ServletException(e);
        } catch (Exception e) {
        	log.error("Unsuccessful twitter signin - " + e.getMessage(), e);
        	throw new ServletException(e);
        }
	}
	
	@RequestMapping(value = "/callback")
	void callback(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("home") HomeModel hm,
			@ModelAttribute("twitter") TwitterSession twitterSession,
			@RequestParam("oauth_verifier") String oauthVerifier
			) throws ServletException {
        //Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        //RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        //String verifier = request.getParameter("oauth_verifier");
        try {
            twitterSession.getTwitter().getOAuthAccessToken(twitterSession.getRequestToken(), oauthVerifier);
            //request.getSession().removeAttribute("requestToken");
            twitterSession.setRequestToken(null);
            TwitterUser user = new TwitterUser()
            	.setScreenName(twitterSession.getTwitter().getScreenName());
            twitterSession.setUser(user);
            hm.getUsers().add(user);
            ResponseList<Status> tl = twitterSession.getTwitter().getHomeTimeline();
            for (Status s : tl) {
				hm.addStatus(
						new TwitterStatus()
							.setUser(user)
							.setText(s.getText())
							.setPublishedAt(s.getCreatedAt())
				);
			}
            response.sendRedirect(request.getContextPath() + "/");
        } catch (TwitterException e) {
            throw new ServletException(e);
        } catch (Exception e) {
        	log.error("Unsuccessful twitter signin - " + e.getMessage(), e);
        	throw new ServletException(e);
        }
    }

	@RequestMapping(value = "/logout")
	void logout(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("home") HomeModel hm,
			@ModelAttribute("twitter") TwitterSession twitterSession) throws IOException {
        //request.getSession().invalidate();
		if (twitterSession.getUser() != null)
			hm.removeUser(twitterSession.getUser());
		twitterSession.getTwitter().shutdown();
		twitterSession = null;
        response.sendRedirect(request.getContextPath()+ "/");
	}
}
