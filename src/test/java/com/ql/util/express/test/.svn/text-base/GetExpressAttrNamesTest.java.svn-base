package com.ql.util.express.test;

import org.junit.Assert;
import org.junit.Test;

import com.ql.util.express.ExpressRunner;

public class GetExpressAttrNamesTest {

	@Test
	public void testABC() throws Exception {
		String express = "alias qh 100; exportAlias fff qh; int a = b; c = a;macro  惩罚    {100 + 100} 惩罚; qh ;fff;";
		ExpressRunner runner = new ExpressRunner(true,true);
		String[] names = runner.getOutVarNames(express);
		for(String s:names){
			System.out.println("var : " + s);
		}
		Assert.assertTrue("获取外部属性错误",names.length == 2);
		Assert.assertTrue("获取外部属性错误",names[0].equalsIgnoreCase("b"));
		Assert.assertTrue("获取外部属性错误",names[1].equalsIgnoreCase("c"));
	}
}
