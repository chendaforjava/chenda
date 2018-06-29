package com.vein.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Corporation;
import com.vein.service.LoginService;



@Controller
@Cacheable
@RequestMapping("/login")
@SuppressWarnings("unchecked")
public class LoginAction {
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	EnterpriseCacheSessionDAO enterpriseCacheSessionDAO;
	
	
	@ResponseBody
	@RequestMapping(value = "/corporationInfo", method = RequestMethod.POST)
	public Map<String,Object> getCorporationInfo(HttpServletRequest request){
		Map<String,Object> corporationMap = new HashMap<String,Object>();
		try {
			List<Corporation> corporationList =  loginService.getCorporationInfo();
			corporationMap.put("corporationList", corporationList);
			corporationMap.put("accessType","1");
		} catch (Exception e) {
			e.printStackTrace();
			corporationMap.put("accessType","2");
		}
		return corporationMap;
	}
	
	
	
	// 登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord,
			@RequestParam("corporation") String corporation,HttpServletRequest request, HttpServletResponse response) {
		
		Corporation cor = loginService.getCorporationByName(corporation);
		if(cor==null){
			return "redirect:/login.html?accessType=4";
		}else{
			corporation = cor.getCorId();
		}
		
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.setAttribute("corporation", corporation);
		session.setAttribute("userName", userName);
		session.setAttribute("passWord", passWord);
		userName = userName+"#"+corporation;
		UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord);
		token.setRememberMe(true);
		try {
			/*Collection<Session> sessions = enterpriseCacheSessionDAO.getActiveSessions();
			 for (Session s : sessions) {
		            System.out.println("登录用户" + session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
		            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
		                return "redirect:/login.html";
		            }
		        }*/
			currentUser.login(token);
			return "redirect:/departmentManage.html";
			//return "departmentManage";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			token.clear();
		}
		return "redirect:/login.html?accessType=4";
	}
	
	
	/*private List<Session> getLoginedSession(Subject currentUser) {
        Collection<Session> list = ((DefaultSessionManager) ((DefaultSecurityManager) SecurityUtils
                .getSecurityManager()).getSessionManager()).getSessionDAO()
                .getActiveSessions();
        List<Session> loginedList = new ArrayList<Session>();
        SysUserEntity loginUser = (SysUserEntity) currentUser.getPrincipal();
        for (Session session : list) {

            Subject s = new Subject.Builder().session(session).buildSubject();

            if (s.isAuthenticated()) {
                SysUserEntity user = (SysUserEntity) s.getPrincipal();

                if (user.getUsercode().equalsIgnoreCase(loginUser.getUsercode())) {
                    if (!session.getId().equals(
                            currentUser.getSession().getId())) {
                        loginedList.add(session);
                    }
                }
            }
        }
        return loginedList;
    }*/
	
}
