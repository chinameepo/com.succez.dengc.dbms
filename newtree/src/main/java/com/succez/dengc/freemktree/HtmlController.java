package com.succez.dengc.freemktree;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 下午04:21:42
 * @since jdk1.6 使用freemarker的模板生成指定的html文件。
 */

public class HtmlController implements Controller {
	 private ArrayList<TreeBean> list;
	/**
	 * 根据发过来的请求，获得表的内容，然后用输出流返回一个结果。
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = request.getRequestURI();
		TableContentProducer producer = new TableContentProducer(url);
		producer.produceContent();
		response.setContentType("text/html;charset=UTF-8 ");
		request.setAttribute("table",producer.getAllString().toString());
		TreeHandle handle = new TreeHandle();
		//静态的变量ID，你必须要设置成为1才可以。
		handle.setId(1);
		handle.genTree();
		list =handle.getList();
		request.setAttribute("tree", list);
		return new ModelAndView("template");
	}
}
