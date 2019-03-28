package client.handlers.accountHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Contains all methods to send and retrieve data to an SQL server for the game
 */
public class SQLConnect {

  //  private static final String URL = "jdbc:mysql://REDACTED";
  private static final String USERNAME = "sql2284965";

  private static final String URL = "jdbc:mysql://sql2.freemysqlhosting.net:3306";
  //  private static final String USERNAME = "REDACTED";
  private static final String PASSWD = "lU7*jV5%";

  /**
   * Sets up the connection
   */
  private static Connection getConnection() throws SQLException {
    Properties props = new Properties();
    props.setProperty("user", USERNAME);
    props.setProperty("password", PASSWD);
    return DriverManager.getConnection(URL, props);
  }

  // private static final String PASSWD = "REDACTED";
  private static final String GET_DATA_STATEMENT = "SELECT uuid, achievements, skins, lootbox, money FROM sql2284965.userData WHERE name = ? AND password = ?";
  private static final String CHECK_USERNAME = "SELECT name FROM sql2284965.userData WHERE name = ?";
  private static final String REGISTER_USER = "INSERT INTO sql2284965.userData (uuid, name, password, achievements, skins, lootbox, money) VALUES (?,?,?,?,?,?,?)";
  private static final String SAVE_DATA = "UPDATE sql2284965.userData SET name = ?, achievements = ?, skins = ?, lootbox = ?, money = ? WHERE uuid = ?";

  /**
   * Obtains user data in String format when given a username and a password
   * @param username Desired username to connect to
   * @param password Password of the user stored in the databse
   * @return AccountData constructor in String format, or a fail string if no result was returned
   */
  public static String getUserdata(String username, String password) {
    String toRet = "";
    try {
      Connection conn = getConnection();
      PreparedStatement pst = conn.prepareStatement(GET_DATA_STATEMENT);
      pst.setString(1, username);
      pst.setString(2, password);

      ResultSet results = pst.executeQuery();
      if (results.isBeforeFirst()) {
        results.next();
        toRet = results.getString(1) + "//x/s" + username + "//x/s" + results.getInt(2) + "//x/s"
            + results.getInt(3) + "//x/s" +
            results.getInt(4) + "//x/s" + results.getInt(5);
      } else {
        toRet = "fail";
      }
    } catch (SQLException e) {
      toRet = "failed error: " + e;
    } finally {
      return toRet;
    }
  }

  /**
   * Registers a new unique user
   * @param data AccountData from settings containing the data of the new user
   * @param password The password to store alongside the user to protect it
   * @return success/fail/exists
   */
  public static String registerUser(AccountData data, String password) {
    String toRet = "";
    try {
      Connection conn = getConnection();

      PreparedStatement pst = conn.prepareStatement(CHECK_USERNAME);
      pst.setString(1, data.getUsername());
      ResultSet results = pst.executeQuery();

      if (results.isBeforeFirst()) {
        toRet = "exists";
      } else {
        pst = conn.prepareStatement(REGISTER_USER);

        String[] args = data.registerAccountQuery(password);
        pst.setString(1, args[0]);
        pst.setString(2, args[1]);
        pst.setString(3, args[2]);
        pst.setInt(4, Integer.parseInt(args[3]));
        pst.setInt(5, Integer.parseInt(args[4]));
        pst.setInt(6, Integer.parseInt(args[5]));
        pst.setInt(7, Integer.parseInt(args[6]));

        int result = pst.executeUpdate();
        if (result == 1) {
          toRet = "success";
        } else {
          toRet = "fail";
        }
      }

    } catch (SQLException e) {
      toRet = "failed error: " + e;
      System.out.println(e);
    } finally {
      return toRet;
    }
  }

  /**
   * Saves the current local AccountData onto the database
   * @param data The AccountData to save
   * @return success/fail
   */
  public static String saveData(AccountData data) {
    if (data.getUsername().equals("NEWUSER")) {
      return "new";
    }
    String toRet = "";
    try {
      Connection conn = getConnection();

      PreparedStatement pst = conn.prepareStatement(CHECK_USERNAME);
      pst.setString(1, data.getUsername());
      ResultSet results = pst.executeQuery();

      if (!results.isBeforeFirst()) {
        toRet = "new";
      } else {
        pst = conn.prepareStatement(SAVE_DATA);
        String[] args = data.saveQuery();

        pst.setString(1, args[1]);
        pst.setInt(2, Integer.parseInt(args[2]));
        pst.setInt(3, Integer.parseInt(args[3]));
        pst.setInt(4, Integer.parseInt(args[4]));
        pst.setInt(5, Integer.parseInt(args[5]));
        pst.setString(6, args[0]);

        pst.executeUpdate();
        toRet = "success";
      }
    } catch (SQLException e) {
      toRet = "failed error: " + e;
    } finally {
      return toRet;
    }
  }
}
