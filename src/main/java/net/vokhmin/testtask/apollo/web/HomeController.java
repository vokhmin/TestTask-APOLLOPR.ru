package net.vokhmin.testtask.apollo.web;

import java.util.Date;

import javax.servlet.ServletException;

import net.vokhmin.testtask.apollo.model.FacebookStatus;
import net.vokhmin.testtask.apollo.model.FacebookUser;
import net.vokhmin.testtask.apollo.model.HomeModel;
import net.vokhmin.testtask.apollo.model.TwitterStatus;
import net.vokhmin.testtask.apollo.model.TwitterUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"home"})
public class HomeController {

	@ModelAttribute("home")
	public HomeModel populateHome() {
		return new HomeModel();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET) 
	ModelAndView getHome(
			@ModelAttribute("home") HomeModel hm) {
		return new ModelAndView("home", "home", hm);
	}
	
	@RequestMapping(value = "/login-twitter")
	String loginTwitter(
			@ModelAttribute("home") HomeModel hm) throws ServletException {
		TwitterUser user = new TwitterUser();
		user.setScreenName("Tw-User A");
		hm.getUsers().add(user);
		hm.addStatus(new TwitterStatus()
			.setText("Twitter status B").setUser(user).setPublishedAt(new Date()));
		hm.addStatus(new TwitterStatus()
			.setText("Twitter status A").setUser(user).setPublishedAt(new Date())); //(new Date().getTime() - 1))))
		hm.addStatus(new TwitterStatus()
			.setText("Twitter status C").setUser(user).setPublishedAt(new Date(new Date().getTime() - 1)));
		hm.addStatus(new TwitterStatus()
			.setText("Twitter status D").setUser(user).setPublishedAt(new Date((new Date().getTime() + 1))));
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login-facebook")
	String loginFacebok(
			@ModelAttribute("home") HomeModel hm) {
		FacebookUser user = new FacebookUser();
		user.setScreenName("FB-User A");
		hm.getUsers().add(user);
		hm.addStatus(new FacebookStatus()
			.setText("Facebook status").setUser(user).setPublishedAt(new Date()));
		return "redirect:/";
	}

}
