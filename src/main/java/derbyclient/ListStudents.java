package derbyclient;

import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;

public class ListStudents {
    public static void main(String[] args) {
        String url = "jdbc:derby://localhost/studentdb";

        Driver d = new ClientDriver();
        try (Connection conn = d.connect(url, null);
                Statement stmt = conn.createStatement()) {

            String sql = "select * from STUDENT, DEPT where MajorId = DId";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("Name\tMajor\tGradYear");

            while (rs.next()) {
                String sname = rs.getString("SName");
                String dname = rs.getString("DName");
                int gradYear = rs.getInt("GradYear");
                System.out.println(sname + "\t" + dname + "\t" + gradYear);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
