/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import quesheng.model.Mj;

public class SqliteOpenHelper {
    static String connectionString = "jdbc:sqlite:quesheng.db";
    private static Dao mjDao;

    public static Dao GetMjDao() throws SQLException {
        if (mjDao == null) {
            mjDao = DaoManager.createDao(SqliteOpenHelper.GetConnectionSource(), Mj.class);
        }
        return mjDao;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean exits(String table) throws ClassNotFoundException {
        boolean exits = false;
        String sql = "select count(*) as c from sqlite_master where name='" + table + "'";
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("c") <= 0) continue;
                exits = true;
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            }
            catch (SQLException e) {
                System.err.println(e);
            }
        }
        return exits;
    }

    public static void init() {
        try {
            if (!SqliteOpenHelper.exits("t_mj")) {
                TableUtils.createTable(SqliteOpenHelper.GetConnectionSource(), Mj.class);
            }
        }
        catch (Exception exception) {
            System.out.println("\u521b\u5efaCommodity\u8868\u5931\u8d25 : " + exception.getMessage());
        }
    }

    public static ConnectionSource GetConnectionSource() throws SQLException {
        return new JdbcConnectionSource(connectionString);
    }
}

