package util;

import java.sql.*;
import java.util.List;

import com.xxl.job.executor.exception.JobHandlerException;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

public class DbUtil {

	private String driver;
	private String jdbcUrl;
	private String username;
	private String password;

	public DbUtil(String driver, String jdbcUrl, String username, String password) {
		super();
		this.driver = driver;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}

	public void executeSql(String sql) throws JobHandlerException {
		List<net.sf.jsqlparser.statement.Statement> stmtList=null;
		try {
			stmtList = CCJSqlParserUtil.parseStatements(sql).getStatements();
		} catch (Exception e1) {
			throw new JobHandlerException(-2,"Sql parsed failed.");
		}
		Connection connection = null;
		Statement stat = null;
		try {
			Class.forName(this.driver);
			connection = DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
//			connection.setAutoCommit(false);
			stat = connection.createStatement();
			for (net.sf.jsqlparser.statement.Statement statement : stmtList) {
				stat.execute(statement.toString());
			}
//			connection.commit();
		} catch (Exception e) {
			throw new JobHandlerException(-1, e.getMessage());
		} finally {
			try {
				stat.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection(){
		try {
			Class.forName(this.driver);
			return DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void close(ResultSet rs, Statement st, Connection conn){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				if(st != null){
					try {
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally{
						if(conn != null){
							try {
								conn.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}


}
