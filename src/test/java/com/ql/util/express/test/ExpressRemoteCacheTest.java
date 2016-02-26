package com.ql.util.express.test;

import com.ql.util.express.*;
import org.junit.Assert;

public class ExpressRemoteCacheTest {
	
	@org.junit.Test
	public void testcache(){
		ExpressRunner runner =new ExpressRunner();		
		ExpressRemoteCacheRunner cacheRunner = new LocalExpressCacheRunner(runner);
		cacheRunner.loadCache("加法计算", "a+b");
		cacheRunner.loadCache("减法计算", "a-b");
		
		
		IExpressContext<String, Object> context = new DefaultContext<String, Object>();
		context.put("a", 1);
		context.put("b", 2);
		
		if(cacheRunner.getCache("加法计算")!=null){
			Object result = cacheRunner.execute("加法计算", context, null, false, true, null);
			Assert.assertTrue("加法计算", result.toString().equalsIgnoreCase("3"));		
			System.out.println(result);
		}
		if(cacheRunner.getCache("加法计算")!=null){
			Object result = cacheRunner.execute("减法计算", context, null, false, true, null);
			Assert.assertTrue("减法计算", result.toString().equalsIgnoreCase("-1"));
			System.out.println(result);
		}
		if(cacheRunner.getCache("乘法计算")!=null){
			Object result = cacheRunner.execute("乘法计算", context, null, false, true, null);
			Assert.assertTrue("乘法计算", result.toString().equalsIgnoreCase("2"));
			System.out.println(result);
		}else{
			System.out.println("没有定义乘法计算器.");
		}
	}

}
