package com.succez.dengc.sqltree;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 上午10:01:38
 * @since jdk1.6 一个应答请求tree.do的控制器
 */
public class TreeController implements Controller {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GetAllInfo allInfo = new GetAllInfo("deng");
		allInfo.setId(1);
		allInfo.getAll();
		ArrayList<TreeBean> tree = allInfo.getList();
		return new ModelAndView("template", "tree", tree);
	}
}
