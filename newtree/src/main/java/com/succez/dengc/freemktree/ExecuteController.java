package com.succez.dengc.freemktree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-19 下午03:22:57
 * @since   jdk1.6
 * 应答要求执行SQL语句的请求，生成一个输入框给用户。
 */
public class ExecuteController implements Controller {
	 private ArrayList<TreeBean> list;
	 private Logger logger =LoggerFactory.getLogger(getClass());
		public ModelAndView handleRequest(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			response.setCharacterEncoding("UTF-8");
			TreeHandle handle = new TreeHandle();
			//静态的变量ID，你必须要设置成为1才可以。
			handle.setId(1);
			handle.genTree();
			list =handle.getList();
			String form =getFileContent("./inputText");
			request.setAttribute("table",form);
			request.setAttribute("tree", list);
			return new ModelAndView("template");
		}
		public String getFileContent(String path) {
			InputStream inputStream  =null;
			StringBuilder requestStr = new StringBuilder();
			try {
				inputStream =(InputStream)(new FileInputStream(path));
				int len=-1;
				byte[] buff =new byte[1024];
				do{
					len=inputStream.read(buff);
					if(len!=-1)
					{
						requestStr.append(new String(buff,0,len,"UTF-8")).append('\n');
					}
				}while(len==1024);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("读取文件出错！");
				return "";
			}finally{
				try {
					inputStream.close();
				} catch (Exception e2) {
					logger.error(e2.toString());
				}
			}
			return requestStr.toString();
		}
}


