package com.cnebula.um.aas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.log.ILog;


@EasyService
public class OraXapoolTest {

	@ESRef(target="(name=java:comp/env/jdbc/xatest)")
	protected DataSource ds;
	
	@ESRef
	protected UserTransaction tx;
	
	@ESRef
	protected ILog log;
	
	public void testSimpeConnection() throws Exception {

		int count = 5;
		log.info("testSimpeConnection");
		Set<PreparedStatement> stmts = new HashSet<PreparedStatement>();
		for (int i = 0; i < count; i++){
			
			Connection c = ds.getConnection();
			c.setAutoCommit(true);
			System.out.println(c.getAutoCommit());
			PreparedStatement ps = c.prepareStatement("select * from testxp where id>?");
			ps.setString(1, "1228805252486");
			stmts.add(ps);
			ResultSet rs = ps.executeQuery();
			System.out.println(rs.getFetchSize());
			ps.close();
			rs.close();
			
			PreparedStatement ps1 = c.prepareStatement("select id from testxp where name=?");
			ps1.setString(1, "good");
			
			ResultSet rs2 = ps1.executeQuery();
			System.out.println(rs2.getFetchSize());
			rs2.close();
			ps1.close();
			
			c.close();
			
		}
		System.out.println("-----------------------------------------");
		System.out.println("finish testSimpeConnection");
	
	}
	
	public Connection testCommit() throws Exception {
		int count = 5;
		log.info("testCommit");
		tx.begin();
		Set<PreparedStatement> stmts = new HashSet<PreparedStatement>();
		for (int i = 0; i < count; i++){
			
			Connection c = ds.getConnection();
			c.setAutoCommit(false);
			System.out.println(c.getAutoCommit());
			PreparedStatement ps = c.prepareStatement("select * from testxp where id>?");
			ps.setString(1, "1228805252486");
			stmts.add(ps);
			ResultSet rs = ps.executeQuery();
			System.out.println(rs.getFetchSize());
			ps.close();
			rs.close();
			
			PreparedStatement ps1 = c.prepareStatement("select id from testxp where name=?");
			ps1.setString(1, "good");
			
			ResultSet rs2 = ps1.executeQuery();
			System.out.println(rs2.getFetchSize());
			rs2.close();
			ps1.close();
			
			c.close();
			
		}
		System.out.println("-----------------------------------------");
		tx.commit();
		System.out.println("finish commit");
		return null;
	}
	
	protected void activate(ComponentContext ctx) {
		try {
			testSimpeConnection();
			testCommit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
