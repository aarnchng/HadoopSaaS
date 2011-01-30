/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.reporter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Aaron
 */
public class Reporter {

    private static Connection conn = null;
    private PreparedStatement pstmtAddRes = null;
    private PreparedStatement pstmtModStat = null;
    private PreparedStatement pstmtGetAllStat = null;
    private PreparedStatement pstmtGetStat = null;
    private PreparedStatement pstmtGetRes = null;

    public Reporter() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
                    "hadoopservice", "ycyong", "pdcchadoop");
            pstmtAddRes = conn.prepareStatement("INSERT INTO hadoopreporter_logs " +
                    "(hostname, idlecpu, availmem) VALUES (?, ?, ?);");
            pstmtModStat = conn.prepareStatement("UPDATE hadoopreporter_nodes " +
                    "SET userin = ?, cpu = ? WHERE hostname = ?;");
            pstmtGetAllStat = conn.prepareStatement("SELECT hostname, userin, cpu FROM " +
                    "hadoopreporter_nodes order by hostname;");
            pstmtGetStat = conn.prepareStatement("SELECT userin, cpu FROM hadoopreporter_nodes " +
                    "WHERE hostname = ?;");
            pstmtGetRes = conn.prepareStatement("SELECT AVG(idlecpu) AS avg_idlecpu, " +
                    "AVG(availmem) AS avg_availmem FROM hadoopreporter_logs WHERE hostname = ? " +
                    "AND logdate <= CURRENT_TIMESTAMP AND logdate >= ?;");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void addResourceLog(String hostname, float idlecpu, float availmem) {
        if ((conn != null) && (pstmtAddRes != null)) {
            try {
                pstmtAddRes.setString(1, hostname);
                pstmtAddRes.setFloat(2, idlecpu);
                pstmtAddRes.setFloat(3, availmem);
                pstmtAddRes.executeUpdate();
                pstmtAddRes.clearParameters();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void updateStatus(String hostname, boolean using, int cpu) {
        if ((conn != null) && (pstmtModStat != null)) {
            try {
                pstmtModStat.setBoolean(1, using);
                pstmtModStat.setInt(2, cpu);
                pstmtModStat.setString(3, hostname);
                pstmtModStat.executeUpdate();
                pstmtModStat.clearParameters();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void printStatistics(PrintWriter out, String hostname) {
        if (hostname != null) {
            if (!hostname.equals("all_nodes")) {
                printOneStatistics(out, hostname);
            } else {
                printAllStatistics(out);
            }
        } else {
            printStatistics(out);
        }
    }

    private void printOneStatistics(PrintWriter out, String hostname) {
        if ((conn != null) && (pstmtGetStat != null) && (pstmtGetRes != null)) {
            boolean using = true;
            int cpu = 0;
            float[] idlecpu = new float[]{0.0f, 0.0f, 0.0f};
            float[] availmem = new float[]{0.0f, 0.0f, 0.0f};
            try {
                pstmtGetStat.setString(1, hostname);
                ResultSet rs = pstmtGetStat.executeQuery();
                try {
                    if (rs.next()) {
                        using = rs.getBoolean("userin");
                        cpu = rs.getInt("cpu");

                        pstmtGetRes.setString(1, hostname);
                        for (int i = 1, j = 0; i <= 4; i *= 2, j++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.HOUR_OF_DAY, -i);
                            pstmtGetRes.setTimestamp(2, new Timestamp(cal.getTime().getTime()));
                            ResultSet rs2 = pstmtGetRes.executeQuery();
                            try {
                                if (rs2.next()) {
                                    idlecpu[j] = rs2.getFloat("avg_idlecpu");
                                    availmem[j] = rs2.getFloat("avg_availmem");
                                }
                            } finally {
                                rs2.close();
                            }
                        }
                    }
                } finally {
                    rs.close();
                }
                pstmtGetRes.clearParameters();
                pstmtGetStat.clearParameters();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            out.print(String.valueOf(using) + "|");
            out.print(String.valueOf(cpu) + "|");
            for (int i = 0; i < idlecpu.length; i++) {
                out.print(String.valueOf(idlecpu[i]) + ";");
                out.print(String.valueOf(availmem[i]) + "|");
            }
        }
    }

    private void printAllStatistics(PrintWriter out) {
        if ((conn != null) && (pstmtGetAllStat != null) && (pstmtGetRes != null)) {
            try {
                ResultSet rs = pstmtGetAllStat.executeQuery();
                try {
                    while (rs.next()) {
                        String hostname = rs.getString("hostname");
                        boolean using = rs.getBoolean("userin");
                        int cpu = rs.getInt("cpu");
                        out.println(hostname + "|");
                        out.println(String.valueOf(using) + "|");
                        out.println(String.valueOf(cpu) + "|");

                        pstmtGetRes.setString(1, hostname);
                        for (int i = 1, j = 0; i <= 4; i *= 2, j++) {
                            float idlecpu = 0.0f;
                            float availmem = 0.0f;

                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.HOUR_OF_DAY, -i);
                            pstmtGetRes.setTimestamp(2, new Timestamp(cal.getTime().getTime()));
                            ResultSet rs2 = pstmtGetRes.executeQuery();
                            try {
                                if (rs2.next()) {
                                    idlecpu = rs2.getFloat("avg_idlecpu");
                                    availmem = rs2.getFloat("avg_availmem");
                                }
                            } finally {
                                rs2.close();
                            }

                            out.println(String.valueOf(idlecpu) + ";");
                            out.println(String.valueOf(availmem) + "|");
                        }
                        out.println("&&");
                    }
                } finally {
                    rs.close();
                }
                pstmtGetRes.clearParameters();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void printStatistics(PrintWriter out) {
        out.println("<html>");
        out.println("<head><title>Resources Statistics</title>");
        out.println("<link rel='stylesheet' type='text/css' href='styles.css'/></head>");
        out.println("<body><table>");
        out.println("<tr><th>Host Name</th><th>Being Used</th><th>CPU Cores</th>");
        out.println("<th>CPU Idleness (1hr)</th><th>Available Memory (1hr)</th>");
        out.println("<th>CPU Idleness (2hr)</th><th>Available Memory (2hr)</th>");
        out.println("<th>CPU Idleness (4hr)</th><th>Available Memory (4hr)</th></tr>");
        if ((conn != null) && (pstmtGetAllStat != null) && (pstmtGetRes != null)) {
            try {
                ResultSet rs = pstmtGetAllStat.executeQuery();
                try {
                    while (rs.next()) {
                        String hostname = rs.getString("hostname");
                        boolean using = rs.getBoolean("userin");
                        int cpu = rs.getInt("cpu");
                        out.println("<tr><td>" + hostname + "</td>");
                        out.println("<td>" + String.valueOf(using) + "</td>");
                        out.println("<td>" + String.valueOf(cpu) + "</td>");

                        pstmtGetRes.setString(1, hostname);
                        for (int i = 1, j = 0; i <= 4; i *= 2, j++) {
                            float idlecpu = 0.0f;
                            float availmem = 0.0f;

                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.HOUR_OF_DAY, -i);
                            pstmtGetRes.setTimestamp(2, new Timestamp(cal.getTime().getTime()));
                            ResultSet rs2 = pstmtGetRes.executeQuery();
                            try {
                                if (rs2.next()) {
                                    idlecpu = rs2.getFloat("avg_idlecpu");
                                    availmem = rs2.getFloat("avg_availmem");
                                }
                            } finally {
                                rs2.close();
                            }

                            out.println("<td>" + String.valueOf(idlecpu) + "</td>");
                            out.println("<td>" + String.valueOf(availmem) + "</td>");
                        }
                        out.println("</tr>");
                    }
                } finally {
                    rs.close();
                }
                pstmtGetRes.clearParameters();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        out.println("</table></body></html>");
    }

    public void compactLogs() {
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DELETE FROM hadoopreporter_logs WHERE " +
                        "logdate < DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -4 HOUR);");
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void removeAllLogs() {
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DELETE FROM hadoopreporter_logs;");
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        if (conn != null) {
            try {
                pstmtAddRes.close();
                pstmtModStat.close();
                pstmtGetAllStat.close();
                pstmtGetStat.close();
                pstmtGetRes.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                conn = null;
            }
        }
    }
}
