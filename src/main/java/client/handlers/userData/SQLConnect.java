package client.handlers.userData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SQLConnect {

  private static Connection getConnection() throws Exception {
      Properties props = new Properties();
      props.setProperty("user", USERNAME);
      props.setProperty("password", PASSWD);
      return DriverManager.getConnection(URL, props);
  }

  private static final String URL = "jdbc:mysql://sql2.freemysqlhosting.net:3306";
//  private static final String URL = "jdbc:mysql://REDACTED";
  private static final String USERNAME = "sql2284965";
//  private static final String USERNAME = "REDACTED";
  private static final String PASSWD = "lU7*jV5%";
  // private static final String PASSWD = "REDACTED";

  public static String getUserdata() {
    return "";
  }

  public static String registerUser() {
    return "";
  }

  public static String saveData() {
    return "";
  }
}
