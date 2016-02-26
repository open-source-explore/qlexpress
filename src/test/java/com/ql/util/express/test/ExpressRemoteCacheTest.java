package com.ql.util.express.test;

import com.ql.util.express.*;
import org.junit.Assert;

public class ExpressRemoteCacheTest {
	
	@org.junit.Test
	public void testcache(){
		ExpressRunner runner =new ExpressRunner();		
		ExpressRemoteCacheRunner cacheRunner = new LocalExpressCacheRunner(runner);
		cacheRunner.loadCache("�ӷ�����", "a+b");
		cacheRunner.loadCache("��������", "a-b");
		
		
		IExpressContext<String, Object> context = new DefaultContext<String, Object>();
		context.put("a", 1);
		context.put("b", 2);
		
		if(cacheRunner.getCache("�ӷ�����")!=null){
			Object result = cacheRunner.execute("�ӷ�����", context, null, false, true, null);
			Assert.assertTrue("�ӷ�����", result.toString().equalsIgnoreCase("3"));		
			System.out.println(result);
		}
		if(cacheRunner.getCache("�ӷ�����")!=null){
			Object result = cacheRunner.execute("��������", context, null, false, true, null);
			Assert.assertTrue("��������", result.toString().equalsIgnoreCase("-1"));
			System.out.println(result);
		}
		if(cacheRunner.getCache("�˷�����")!=null){
			Object result = cacheRunner.execute("�˷�����", context, null, false, true, null);
			Assert.assertTrue("�˷�����", result.toString().equalsIgnoreCase("2"));
			System.out.println(result);
		}else{
			System.out.println("û�ж���˷�������.");
		}
	}

}
