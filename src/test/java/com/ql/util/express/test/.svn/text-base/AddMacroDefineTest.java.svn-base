package com.ql.util.express.test;

import org.junit.Test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

public class AddMacroDefineTest {
	@Test
	public void test2Java() throws Exception {
		ExpressRunner runner = new ExpressRunner(false,true);	
		runner.addFunctionOfClassMethod("abc", BeanExample.class.getName(),
				"testLong", new String[] { "long" }, null);	
		runner.addMacro("ÐþÄÑ", "abc(100);");
		String express = "ÐþÄÑ + \" - Test\";";
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		Object r =   runner.execute(express, context, null, false,true);
		System.out.println(r);		
	}
}
