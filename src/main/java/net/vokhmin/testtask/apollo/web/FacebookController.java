package net.vokhmin.testtask.apollo.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.vokhmin.testtask.apollo.model.FacebookSession;
import net.vokhmin.testtask.apollo.model.FacebookStatus;
import net.vokhmin.testtask.apollo.model.FacebookUser;
import net.vokhmin.testtask.apollo.model.HomeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "/facebook")
@SessionAttributes({"facebook", "home"})
public class FacebookController {
	protected static Logger log = LoggerFactory.getLogger(FacebookController.class); 
	
	@Autowired
	FacebookConnectionFactory factory;
	
	@ModelAttribute("facebook")
	public FacebookSession populateFacebook() {
	       FacebookSession facebookSession = new FacebookSession(); 
	       facebookSession.setConnectionFactory(factory);
	       return facebookSession;
	}
	
	@RequestMapping(value = "/signin")
	void signin(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("facebook") FacebookSession facebookSession
			) throws ServletException {
        try {
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            facebookSession.setOAuth(facebookSession.getConnectionFactory().getOAuthOperations());
            OAuth2Parameters params = new OAuth2Parameters();
            params.setRedirectUri(callbackURL.toString());
            params.setScope("offline_access");
            String authorizationUrl = facebookSession.getOAuth().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);
            log.debug("return redirect to {}", authorizationUrl);
            response.sendRedirect(authorizationUrl);
        } catch (Exception e) {
        	log.error("Unsuccessful facebook signin - " + e.getMessage(), e);
        	throw new ServletException(e);
        }
	}
	
	@RequestMapping(value = "/callback")
	void callback(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("home") HomeModel hm,
			@ModelAttribute("facebook") FacebookSession facebookSession,
			@RequestParam("code") String authorizationCode
			) throws ServletException {
        try {
        	StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");;
            
        	AccessGrant accessGrant = facebookSession.getOAuth().exchangeForAccess(authorizationCode, callbackURL.toString(), null);
        	Connection<Facebook> connection = facebookSession.getConnectionFactory().createConnection(accessGrant);
        	facebookSession.setConnection(connection);
        	
        	//facebookSession.getConnection().
            FacebookUser user = new FacebookUser()
        		.setScreenName(facebookSession.getConnection().getApi().userOperations().getUserProfile().getUsername());
	        facebookSession.setUser(user);
	        hm.getUsers().add(user);
	        List<Post> feed = facebookSession.getConnection().getApi().feedOperations().getFeed();
	        for (Post s : feed) {
				hm.addStatus(
						new FacebookStatus()
							.setUser(user)
							.setText(s.getMessage())
							.setPublishedAt(s.getCreatedTime())
				);
			}
        	
        	response.sendRedirect(request.getContextPath()+ "/");
        } catch (Exception e) {
        	log.error("Unsuccessful facebook signin - " + e.getMessage(), e);
        	throw new ServletException(e);
        }
    }

	@RequestMapping(value = "/logout")
	void logout(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("home") HomeModel hm,
			@ModelAttribute("facebook") FacebookSession facebookSession) throws IOException {
        //request.getSession().invalidate();
		if (facebookSession.getUser() != null)
			hm.removeUser(facebookSession.getUser());
		facebookSession.getConnection();
		facebookSession = null;
        response.sendRedirect(request.getContextPath()+ "/");
	}
}
