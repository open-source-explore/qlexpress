package com.ql.util.express.test;

import org.junit.Test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

public class ATempTest {
	@Test
	public void test2Java() throws Exception {
		//String express = "2 in (1,2,3)";
		String express = "include Test; max(1,2,3)";
		//String express = "(1+2)*3";
		ExpressRunner runner = new ExpressRunner(false,true);	
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		Object r =   runner.execute(express, context, null, false,false);
		System.out.println(r);		
	}
	
	
	@Test
	public void testLoadFromFile() throws Exception{	
		ExpressRunner runner = new ExpressRunner(false,true);
		runner.loadExpress("includeRoot");
		DefaultContext<String, Object>  context = new DefaultContext<String, Object>();	
		Object r = runner.executeByExpressName("includeRoot", context, null, false,false,null);
		System.out.println(r );
		System.out.println(context);
	}	
}
