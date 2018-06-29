package com.vein.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vein.service.ReportDownLoadService;

@Controller
@RequestMapping("/report")
public class ReportDownLoadAction {
	
	@Autowired
	ReportDownLoadService reportDownLoadService;
	
	@RequestMapping("/currentReport")
	public void getCurrentReport(HttpServletRequest request,HttpServletResponse response){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		reportDownLoadService.downLoadCurrentReport(request, response,corId);
	}
	
	@RequestMapping("/historyreport")
	public void getHistoryReport(HttpServletRequest request,HttpServletResponse response){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		reportDownLoadService.getHistoryReport(request, response,corId);
	}
}
