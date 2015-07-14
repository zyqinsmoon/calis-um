package com.cnebula.um.service.tests;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import com.cnebula.common.remote.ws.EasyServiceClient;
import com.cnebula.common.security.auth.ILoginService;
import com.cnebula.common.security.auth.LoginStatus;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;

public class LoginPerfomanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	int c = 100;
	int sleep = 0;
	volatile long lastStart = 0;
	volatile long lastEnd = 0;
	volatile boolean finished = false;
	
	public void startCall() {
		lastStart = System.currentTimeMillis();
		lastEnd = 0;
	}
	
	
	public void endCall() {
		lastEnd = System.currentTimeMillis();
	}
	
	
	public void testLogin(ILoginService ls, String name, String password){
		try {
			
			
			UMPrincipal u = (UMPrincipal)ls.loginByNamePassword(name, password) ;//"NL10000001", "999999");
			System.out.println(u.getName());
			long s = System.currentTimeMillis();
			for (int i = 0; i < c; i++){
				startCall();
				LoginStatus status = ls.loginByNamePasswordAndGetStatus(name, password, false);
//				ls.loginByCredential(status.getArtifact());
				if (sleep > 0){
					Thread.sleep(sleep);
				}
				endCall();
//				System.out.println(i);
			}
			finished = true;
			System.out.println(System.currentTimeMillis() - s);
			System.out.println("end : " + ((UMPrincipal)ls.getUser()).getName());
			ls.logout();
		} catch (Throwable e) {
			e.printStackTrace();
		} 
	}
	
	
	
	@Test
	public void testEasyServiceLogin() {
		try{
		String host = "162.105.206.120";
//		host = "162.105.139.43";
		host = "192.168.2.250";
//		host = "192.168.2.105";
		host = "uas.jslib.org.cn";
		final ILoginService ls = EasyServiceClient.lookup(host, 80, ILoginService.class);
		Thread t = new Thread(){
			@Override
			public void run() {
				
				testLogin(ls, "NL10000001", "999999");
			}
		};
		t.start();
//		final ILoginService ls2 = EasyServiceClient.lookup(host, 80, ILoginService.class);
//		Thread t2 = new Thread(){
//			@Override
//			public void run() {
//				testLogin(ls2, "NL10000002", "999999");
//			}
//		};
//		t2.start();

			while (!finished) {
				
				Thread.sleep(5000);
				if (lastEnd == 0) {
					System.out.println( "not return cost : " + (System.currentTimeMillis() - lastStart));
				}
			}
			t.join();
//			t2.join();
		}catch (Exception e) {
			
		}
		
		
		
	}
}
