<%@ page
    contentType="text/html;charset=UTF-8"
    import="java.io.IOException"
    import="java.io.InputStream"
    import="java.net.InetSocketAddress"
    import="java.text.DecimalFormat"
    import="org.apache.hadoop.conf.Configuration"
    import="org.apache.hadoop.mapred.ClusterStatus"
    import="org.apache.hadoop.mapred.JobClient"
    import="org.apache.hadoop.mapred.JobStatus"
    import="org.apache.hadoop.net.NetUtils"
    import="org.apache.hadoop.util.VersionInfo"
    %>

    <%!    private static DecimalFormat percentage = new DecimalFormat("##0.00");

    public JobClient getJobClient(HttpServletRequest request) {
        Configuration conf = initialise(request);
        JobClient client = null;
        String address = conf.get("mapred.job.tracker");
        if ((address == null) || address.equals("local")) {
            client = new JobClient();
        } else {
            InetSocketAddress inetaddress = NetUtils.createSocketAddr(address);
            try {
                client = new JobClient(inetaddress, conf);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return client;
    }

    private Configuration initialise(HttpServletRequest request) {
        ClassLoader loader = this.getClass().getClassLoader();
        Configuration conf = new Configuration(false);
        conf.addResource(loader.getResourceAsStream("/hadoop-default.xml"));
        conf.addResource(loader.getResourceAsStream("/hadoop-site.xml"));

        String id = request.getParameter("id");
        if (id != null) {
            InputStream is = loader.getResourceAsStream("/hadoop-site-" + id + ".xml");
            if (is != null) {
                conf.addResource(is);
            }
        }
        return conf;
    }

    public String generateNodesUrl(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null) {
            return "hadoop_nodes.jsp?id=" + id;
        }
        return "hadoop_nodes.jsp";
    }

    public void generateClusterInfo(JspWriter out, HttpServletRequest request, JobClient client) throws IOException {
        if (client != null) {
            ClusterStatus cluster = client.getClusterStatus();
            out.println("<br/>");
            out.println("<b>State: </b>" + cluster.getJobTrackerState() + "<br/>");
            out.print("<b>Online Nodes: </b><a href=\"" + generateNodesUrl(request) + "\">");
            out.println(cluster.getTaskTrackers() + "</a><br/>");
            out.println("<b>Map Task Capacity: </b>" + cluster.getMaxMapTasks() + "<br/>");
            out.println("<b>Reduce Task Capacity: </b>" + cluster.getMaxReduceTasks() + "<br/>");
            if (cluster.getTaskTrackers() > 0) {
                double totalTasks = (double) (cluster.getMaxMapTasks() + cluster.getMaxReduceTasks());
                double tasksPerNode = totalTasks / cluster.getTaskTrackers();
                out.println("<b>Average Tasks/Node: </b>" + percentage.format(tasksPerNode) + "<br/>");
            } else {
                out.println("<b>Average Tasks/Node: </b>0<br/>");
            }
            out.println("<br/>");
            out.println("<table>");
            out.println("<tr><th>Running<br/>Map Tasks</th><th>Running<br/>Reduce Tasks</th>" +
                    "<th>Idle<br/>Map Tasks</th><th>Idle<br/>Reduce Tasks</th></tr>");
            int idleMapTasks = cluster.getMaxMapTasks() - cluster.getMapTasks();
            int idleReduceTasks = cluster.getMaxReduceTasks() - cluster.getReduceTasks();
            out.println("<tr><td>" + cluster.getMapTasks() + "</td><td>" + cluster.getReduceTasks() + "</td>" +
                    "<td>" + idleMapTasks + "</td><td>" + idleReduceTasks + "</td></tr>");
            out.println("</table>");
        } else {
            out.println("<br/>");
            out.println("<b>State: </b>OFFLINE<br/>");
            out.println("<b>Online Nodes: </b><a href=\"hadoop_nodes.jsp\">0</a><br/>");
            out.println("<b>Map Task Capacity: </b>0<br/>");
            out.println("<b>Reduce Task Capacity: </b>0<br/>");
            out.println("<b>Average Tasks/Node: </b>0<br/>");
            out.println("<br/>");
            out.println("<table>");
            out.println("<tr><th>Running<br/>Map Tasks</th><th>Running<br/>Reduce Tasks</th>" +
                    "<th>Idle<br/>Map Tasks</th><th>Idle<br/>Reduce Tasks</th></tr>");
            out.println("<tr><td>0</td><td>0</td><td>0</td><td>0</td></tr>");
            out.println("</table>");
        }
    }

    public void generateRunningJobs(JspWriter out, JobClient client) throws IOException {
        out.println("<table>");
        out.println("<tr><th>Job ID</th><th>Status</th>" +
                "<th>Setup %<br/>Complete</th>" +
                "<th>Map %<br/>Complete</th><th>Total<br/>Map Tasks</th>" +
                "<th>Reduce %<br/>Complete</th><th>Total<br/>Reduce Tasks</th>" +
                "</tr>");
        if (client != null) {
            JobStatus[] jobs = client.jobsToComplete();
            for (JobStatus job : jobs) {
                out.println("<tr><td>" + job.getJobID() + "</td><td>" + getStatusMessage(job) + "</td>" +
                        "<td>" + percentage.format(job.setupProgress() * 100) + "%</td>" +
                        "<td>" + percentage.format(job.mapProgress() * 100) + "%</td>" +
                        "<td>" + client.getMapTaskReports(job.getJobID()).length + "</td>" +
                        "<td>" + percentage.format(job.reduceProgress() * 100) + "%</td>" +
                        "<td>" + client.getReduceTaskReports(job.getJobID()).length + "</td>" +
                        "</tr>");
            }
        }
        out.println("</table>");
    }

    public void generateCompletedJobs(JspWriter out, JobClient client) throws IOException {
        out.println("<table>");
        out.println("<tr><th>Job ID</th>" +
                "<th>Total<br/>Map Tasks</th><th>Total<br/>Reduce Tasks</th>" +
                "</tr>");
        if (client != null) {
            JobStatus[] jobs = client.getAllJobs();
            for (int i = jobs.length - 1; ((i >= 0) && (i >= jobs.length - 20)); i--) {
                JobStatus job = jobs[i];
                if ((job.getRunState() == JobStatus.PREP) || (job.getRunState() == JobStatus.RUNNING)) {
                    continue;
                }
                if ((job.getRunState() == JobStatus.FAILED) || (job.getRunState() == JobStatus.KILLED)) {
                    continue;
                }
                out.println("<tr><td>" + job.getJobID() + "</td>" +
                        "<td>" + client.getMapTaskReports(job.getJobID()).length + "</td>" +
                        "<td>" + client.getReduceTaskReports(job.getJobID()).length + "</td>" +
                        "</tr>");
            }
        }
        out.println("</table>");
    }

    public void generateFailedJobs(JspWriter out, JobClient client) throws IOException {
        out.println("<table>");
        out.println("<tr><th>Job ID</th>" +
                "<th>Setup %<br/>Complete</th>" +
                "<th>Map %<br/>Complete</th><th>Total<br/>Map Tasks</th>" +
                "<th>Reduce %<br/>Complete</th><th>Total<br/>Reduce Tasks</th>" +
                "</tr>");
        if (client != null) {
            JobStatus[] jobs = client.getAllJobs();
            for (int i = jobs.length - 1; ((i >= 0) && (i >= jobs.length - 20)); i--) {
                JobStatus job = jobs[i];
                if ((job.getRunState() == JobStatus.FAILED) || (job.getRunState() == JobStatus.KILLED)) {
                    out.println("<tr><td>" + job.getJobID() + "</td>" +
                            "<td>" + percentage.format(job.setupProgress() * 100) + "%</td>" +
                            "<td>" + percentage.format(job.mapProgress() * 100) + "%</td>" +
                            "<td>" + client.getMapTaskReports(job.getJobID()).length + "</td>" +
                            "<td>" + percentage.format(job.reduceProgress() * 100) + "%</td>" +
                            "<td>" + client.getReduceTaskReports(job.getJobID()).length + "</td>" +
                            "</tr>");
                }
            }
        }
        out.println("</table>");
    }

    private String getStatusMessage(JobStatus job) {
        switch (job.getRunState()) {
            case JobStatus.PREP:
                return "Preparing";
            case JobStatus.RUNNING:
                return "Running";
            case JobStatus.SUCCEEDED:
                return "Success";
            case JobStatus.FAILED:
            case JobStatus.KILLED:
                return "Failure";
            default:
                return "Unknown";
        }
    }
    %>

    <%JobClient client = getJobClient(request);%>
    <html>
        <head>
            <title>Hadoop Cluster @NTU</title>
            <link rel="stylesheet" type="text/css" href="styles.css"/>
        </head>
        <body>
            <h1>Hadoop Cluster @NTU</h1>

            <div id="quick_links"><b>Quick Links</b>
                <ul>
                    <li><a href="#cluster_info">Cluster Information</a></li>
                    <li><a href="<%=generateNodesUrl(request)%>">Available Nodes</a></li>
                    <li><a href="#running_jobs">Running Jobs</a></li>
                    <li><a href="#completed_jobs">Completed Jobs</a></li>
                    <li><a href="#failed_jobs">Failed Jobs</a></li>
                </ul>
            </div>
            <hr/>

            <h2 id="cluster_info">Cluster Information</h2>
            <b>Version: </b><%=VersionInfo.getVersion()%>, r<%=VersionInfo.getRevision()%><br/>
            <%generateClusterInfo(out, request, client);%><br/>
            <a href="#quick_links">Back To Top</a>
            <hr/>

            <h2 id="running_jobs">Running Jobs</h2>
            <%generateRunningJobs(out, client);%><br/>
            <a href="#quick_links">Back To Top</a>
            <hr/>

            <h2 id="completed_jobs">Completed Jobs</h2>
            <%generateCompletedJobs(out, client);%><br/>
            <a href="#quick_links">Back To Top</a>
            <hr/>

            <h2 id="failed_jobs">Failed Jobs</h2>
            <%generateFailedJobs(out, client);%><br/>
            <a href="#quick_links">Back To Top</a>
            <hr/>
        </body>
    </html>
