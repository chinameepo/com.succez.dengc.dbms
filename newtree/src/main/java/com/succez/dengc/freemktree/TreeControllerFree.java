package com.succez.dengc.freemktree;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 下午02:44:32
 * @since   jdk1.6
 * 一个应答生成数据库树的应答类。
 */
public class TreeControllerFree implements Controller{
    private ArrayList<TreeBean> list;
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		TreeHandle handle = new TreeHandle();
		//静态的变量ID，你必须要设置成为1才可以。
		handle.setId(1);
		handle.genTree();
		request.setAttribute("table", "");
		list =handle.getList();
		request.setAttribute("tree", list);
		return new ModelAndView("template");
	}
}
