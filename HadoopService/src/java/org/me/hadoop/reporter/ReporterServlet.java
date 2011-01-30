package org.me.hadoop.reporter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Aaron
 */
public class ReporterServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        Reporter reporter = new Reporter();
        getServletContext().setAttribute("HadoopReporter", reporter);
    }

    @Override
    public void destroy() {
        Reporter reporter = (Reporter) getServletContext().getAttribute("HadoopReporter");
        if (reporter != null) {
            reporter.close();
            getServletContext().removeAttribute("HadoopReporter");
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Reporter reporter = (Reporter) getServletContext().getAttribute("HadoopReporter");
        if (reporter == null) {
            return;
        }

        String operation = request.getParameter("operation");
        String hostname = request.getParameter("hostname");
        if (operation != null) {
            if (operation.equals("report_resources") && (hostname != null)) {
                float idlecpu = Float.parseFloat(request.getParameter("cpu_idle"));
                float availmem = Float.parseFloat(request.getParameter("available_memory"));
                reporter.addResourceLog(hostname, idlecpu, availmem);
            } else if (operation.equals("report_status") && (hostname != null)) {
                boolean userin = request.getParameter("user_in").equals("yes");
                int cpu = Integer.parseInt(request.getParameter("cpu_count"));
                reporter.updateStatus(hostname, userin, cpu);
            } else if (operation.equals("compact")) {
                reporter.compactLogs();
            } else if (operation.equals("reset")) {
                reporter.removeAllLogs();
            }
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                /* TODO output your page here
                 */
                reporter.printStatistics(out, hostname);
            } finally {
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "ReporterServlet";
    }// </editor-fold>
}
