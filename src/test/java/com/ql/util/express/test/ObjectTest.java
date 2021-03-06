package com.ql.util.express.test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.junit.Assert;
import org.junit.Test;

public class ObjectTest {
	@Test
	public void testABC() throws Exception {
		String express = "object.amount*2+object.volume";
		ExpressRunner runner = new ExpressRunner();
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		ObjectBean tempObject= new ObjectBean(100,60);
		context.put("object", tempObject);
		Object r =  runner.execute(express, context, null, false,
				true);
		System.out.println(r);
		Assert.assertTrue("����ִ�д���", r.toString().equals(260) == false);
	}

}
