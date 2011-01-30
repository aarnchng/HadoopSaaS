<%@ page
    contentType="text/html;charset=UTF-8"
    import="java.io.IOException"
    import="java.io.InputStream"
    import="java.net.InetSocketAddress"
    import="org.apache.hadoop.conf.Configuration"
    import="org.apache.hadoop.mapred.JobClient"
    import="org.apache.hadoop.net.NetUtils"
    %>

    <%!
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
    %>

    <%JobClient client = getJobClient(request);
    out.println(client.getClusterStatus().getTaskTrackers());%>
