package net.devosmium.adhubbot;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class SQLiteUtils {
    public static void createDB() {
        String url = "jdbc:sqlite:./data.db";

        try(Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Created new DB with Driver Name" + meta.getDriverName());
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public static void createPartnerApplicationsTable() {
        String url = "jdbc:sqlite:./data.db";
        String sql = "CREATE TABLE IF NOT EXISTS partnerApplications (\n" +
                " id integer PRIMARY KEY, \n" +
                " serverName text NOT NULL, \n" +
                " applicantName text NOT NULL, \n" +
                " applicantId text NOT NULL, \n" +
                " serverCode text NOT NULL  \n" +
                ");";
        try(Connection conn = DriverManager.getConnection(url)) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Successfully Initialized the Partner Applications Database at" + Calendar.getInstance().getTime().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param id
     * @param serverName
     * @param applicant
     * @param applicantId
     * @param inviteCode
     */

    public static void applyForPartner(int id, String serverName, String applicant, String applicantId, String inviteCode) {
        String url = "jdbc:sqlite:./data.db";
        String sql = "INSERT INTO partnerApplications(id,serverName,applicantName,applicantId,serverCode) VALUES(?,?,?,?,?)";
        try (Connection conn =DriverManager.getConnection(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.setString(2, serverName);
            pstmt.setString(3, applicant);
            pstmt.setString(4, applicantId);
            pstmt.setString(5, inviteCode);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
