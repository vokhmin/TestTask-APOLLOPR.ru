package net.vokhmin.testtask.apollo.web;

import java.io.IOException;
import java.util.Date;
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
import org.springframework.social.facebook.api.impl.FacebookTemplate;
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
    		if (facebookSession.getConnection() != null) {
    			log.debug("there is facebook connection already");
    			response.sendRedirect(request.getContextPath()+ "/");
    		}
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            facebookSession.setOAuth(facebookSession.getConnectionFactory().getOAuthOperations());
            OAuth2Parameters params = new OAuth2Parameters();
            params.setRedirectUri(callbackURL.toString());
            params.setScope("read_stream,offline_access");
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
        	Facebook facebook = connection.getApi();
        	
        	//facebookSession.getConnection().
            FacebookUser user = new FacebookUser()
        		.setScreenName(facebook.userOperations().getUserProfile().getName());
	        facebookSession.setUser(user);
	        hm.getUsers().add(user);
	        log.debug("access grant is '{}' and access token is '{}'", accessGrant, accessGrant.getAccessToken());
	        FacebookTemplate template = new FacebookTemplate(accessGrant.getAccessToken());
	        List<Post> feed = template.feedOperations().getFeed();
	        log.debug("was getting {} feeds", feed.size());
	        //List<String> friends = facebookSession.getConnection().getApi().friendOperations().getFriendIds();
	        //for (String f : friends) {
			//	hm.addStatus(new FacebookStatus().setUser(user).setPublishedAt(new Date()).setText(f));
			//}
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
		facebookSession.reset();
        response.sendRedirect(request.getContextPath()+ "/");
	}
}
