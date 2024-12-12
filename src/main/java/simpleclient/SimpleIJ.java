package simpleclient;

import java.sql.*;
import java.util.Scanner;
import simpledb.jdbc.embedded.EmbeddedDriver;
import simpledb.jdbc.network.NetworkDriver;

public class SimpleIJ {
    private static final int MAX_COMMAND_LENGTH = 1024;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Connect> ");
        String s = sc.nextLine();
        Driver d = (s.contains("//")) ? new NetworkDriver() : new EmbeddedDriver();

        try (Connection conn = d.connect(s, null);
                Statement stmt = conn.createStatement()) {
            System.out.print("\nSQL> ");
            String cmd = "";
            while (sc.hasNextLine()) {
                // process one line of input
                String line = sc.nextLine().trim();
                cmd += line;
                cmd = cmd.trim();

                if (cmd.length() > MAX_COMMAND_LENGTH) {
                    System.out.println("Command too long");
                    cmd = "";
                    continue;
                }

                if (cmd.startsWith("exit")) break;

                if (!cmd.endsWith(";")) {
                    cmd += " ";
                    System.out.print("> ");
                    System.out.flush();
                    continue;
                }

                if (cmd.startsWith("select")) doQuery(stmt, cmd);
                else doUpdate(stmt, cmd);

                cmd = "";
                System.out.print("\nSQL> ");
                System.out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sc.close();
    }

    private static void doQuery(Statement stmt, String cmd) {
        try (ResultSet rs = stmt.executeQuery(cmd)) {
            ResultSetMetaData md = rs.getMetaData();
            int numcols = md.getColumnCount();
            int totalwidth = 0;

            // print header
            for (int i = 1; i <= numcols; i++) {
                String fldname = md.getColumnName(i);
                int width = md.getColumnDisplaySize(i);
                totalwidth += width;
                String fmt = "%" + width + "s";
                System.out.format(fmt, fldname);
            }
            System.out.println();
            for (int i = 0; i < totalwidth; i++) System.out.print("-");
            System.out.println();

            // print records
            while (rs.next()) {
                for (int i = 1; i <= numcols; i++) {
                    String fldname = md.getColumnName(i);
                    int fldtype = md.getColumnType(i);
                    String fmt = "%" + md.getColumnDisplaySize(i);
                    if (fldtype == Types.INTEGER) {
                        int ival = rs.getInt(fldname);
                        System.out.format(fmt + "d", ival);
                    } else {
                        String sval = rs.getString(fldname);
                        System.out.format(fmt + "s", sval);
                    }
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    private static void doUpdate(Statement stmt, String cmd) {
        try {
            int howmany = stmt.executeUpdate(cmd);
            System.out.println(howmany + " records processed");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
}
