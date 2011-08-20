package com.succez.dengc.sqltree;

import java.util.ArrayList;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 上午11:56:25
 * @since   jdk1.6
 * 缺什么我就测试什么
 * 
 */
public class Test {
	public static void main(String[] args) {
		System.out.println("开始了");
		GetAllInfo allInfo = new GetAllInfo("deng");
		allInfo.getAll();
		ArrayList<TreeBean> tree = allInfo.getList();
		System.out.println("size =" + tree.size());
		for (int i = 0; i < 91; i++) {
			TreeBean bean = tree.get(i);
			System.out.println("inex=" + i + " ,bean id=" + bean.getId() + ","
					+ "parentId:" + bean.getPrentid() + "," + bean.getName());
		}
	}

}


