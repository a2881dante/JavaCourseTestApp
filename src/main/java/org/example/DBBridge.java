package org.example;


import java.sql.*;

public class DBBridge {

    enum SqlOperation {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    private static final String url = "jdbc:mysql://localhost:3306/course_test_app_db";

    private static final String user = "root";

    private static final String password = "";

    public void list() {
        String query = "SELECT * FROM expressions";
        runQuery(query, SqlOperation.READ);
    }

    public void list(String operand, double result) {
        String query = String.format("SELECT * FROM expressions WHERE res %s %f", operand, result);
        runQuery(query, SqlOperation.READ);
    }

    public void create(String expression, double result) {
        String query = String.format("INSERT INTO expressions SET expr='%s', res=%f", expression, result);
        runQuery(query, SqlOperation.CREATE);
    }

    public void read(int id) {
        String query = String.format("SELECT * FROM expressions WHERE id=%d", id);
        runQuery(query, SqlOperation.READ);
    }

    public void update(int id, String expression, double result) {
        String query = String.format("UPDATE expressions SET expr='%s', res=%f WHERE id=%d", expression, result, id);
        runQuery(query, SqlOperation.UPDATE);
    }

    public void delete(int id) {
        String query = String.format("DELETE FROM expressions WHERE id=%d", id);
        runQuery(query, SqlOperation.DELETE);
    }

    private void runQuery(String query, SqlOperation operation) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            switch (operation) {
                case READ:
                    rs = stmt.executeQuery(query);
                    showRows(rs);
                    break;
                case UPDATE:
                    stmt.executeUpdate(query);
                    break;
                case CREATE:
                case DELETE:
                    stmt.execute(query);
                    break;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                assert stmt != null;
                stmt.close();
            } catch(SQLException ignored) {}
            try { con.close(); } catch(SQLException ignored) {}
            try {
                if(rs != null )
                    rs.close();
            } catch(SQLException ignored) {}
        }
    }

    public void showRows(ResultSet rs) {
        printDivider();

        System.out.printf("| %-5s| %-80s| %-50s|%n", "ID","EXPRESSION", "RESULT");

        printDivider();

        if(rs == null) {
            System.out.printf("| %-136s |%n", "NO RESULT");
        } else {
            try {
                while (rs.next()) {
                    System.out.printf("| %-5d| %-80s| %-50f|%n",
                            rs.getInt("id"),
                            rs.getString("expr"),
                            rs.getDouble("res")
                    );
                }
            } catch (SQLException sqlException) {
                System.out.printf("| %-136s |%n", "SQL ERROR");
            }
        }

        printDivider();
    }

    private static void printDivider() {
        System.out.print("+");
        for(int i=0; i<6; i++) { System.out.print("-"); }
        System.out.print("+");
        for(int i=0; i<81; i++) { System.out.print("-"); }
        System.out.print("+");
        for(int i=0; i<51; i++) { System.out.print("-"); }
        System.out.println("+");
    }
}
