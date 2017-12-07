package net.devosmium.adhubbot;

import java.sql.*;

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

    public static void createServerSettingsTable(String tableName) {
        String url = "jdbc:sqlite:./data.db";

        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n " +
                " prefix text NOT NULL, \n" +
                " modRole text NOT NULL, \n" +
                " nickname text NOT NULL\n" +
                ");";
        try(Connection conn = DriverManager.getConnection(url)) {
            Statement stmt =  conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
