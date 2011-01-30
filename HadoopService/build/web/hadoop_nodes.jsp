<%@ page
    contentType="text/html;charset=UTF-8"
    import="java.io.IOException"
    import="java.io.InputStream"
    import="java.net.URL"
    import="java.util.*"
    import="org.apache.hadoop.conf.Configuration"
    import="net.htmlparser.jericho.*"
    %>

    <%!    private static String[] names = {
        "pdc1", "pdc2", "pdc3", "pdc4", "pdc5", "pdc6", "pdc7", "pdc8",
        "pdc9", "pdc10", "pdc11", "pdc12", "pdc13", "pdc14", "pdc15",
        "pdc16", "pdc17", "pdc18", "pdc19", "pdc20", "pdc21", "pdc22",
        "pdc23", "pdc24", "pdc25", "pdc26", "pdc27", "pdc28", "pdc29",
        "pdc33", "fypyyc1", "fypyyc2", "fypyyc3", "fypyyc4", "fypyyc5",
        "fypyyc6", "fypyyc7", "fypyyc8", "fypyyc9", "fypyyc10"
    };

    public static class Node {

        private String name;
        private String runningTasks;
        private String maxMapTasks;
        private String maxReduceTasks;
        private String failures;

        public Node(String name, String runningTasks, String maxMapTasks,
                String maxReduceTasks, String failures) {
            this.name = name;
            this.runningTasks = runningTasks;
            this.maxMapTasks = maxMapTasks;
            this.maxReduceTasks = maxReduceTasks;
            this.failures = failures;
        }

        public void printNode(JspWriter out) throws IOException {
            out.println("<tr><td>" + name + "</td><td>" + runningTasks + "</td>" +
                    "<td>" + maxMapTasks + "</td><td>" + maxReduceTasks + "</td>" +
                    "<td>" + failures + "</td></tr>");
        }
    }

    public void generateNodes(JspWriter out, HttpServletRequest request) throws IOException {
        Source src = getSource(getURL(request));
        Map<String, Node> nodes = getNodes(src);
        for (String name : names) {
            if (nodes.containsKey(name)) {
                nodes.get(name).printNode(out);
            } else {
                out.println("<tr><td>" + name + "</td><td colspan=\"4\">OFFLINE</td></tr>");
            }
        }
    }

    private String getURL(HttpServletRequest request) {
        Configuration conf = initialise(request);
        String address = conf.get("mapred.job.tracker");
        if ((address != null) && (!address.equals("local"))) {
            String port = conf.get("mapred.job.tracker.http.address");
            String url = "http://" + address.substring(0, address.indexOf(":"));
            url += port.substring(port.indexOf(":")) + "/machines.jsp";
            return url;
        }
        return null;
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

    private Source getSource(String url) {
        if (url != null) {
            try {
                return (new Source(new URL(url)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private Map<String, Node> getNodes(Source src) throws IOException {
        Map<String, Node> nodes = new HashMap<String, Node>();
        if (src != null) {
            List<Element> trs = src.getAllElements(HTMLElementName.TR);
            for (Element tr : trs) {
                List<Element> tds = tr.getContent().getAllElements(HTMLElementName.TD);
                String name = tds.get(0).getContent().getTextExtractor().toString().trim();
                if ((tds.size() < 7) || (name.equals("Name"))) {
                    continue;
                }
                name = tds.get(1).getContent().getTextExtractor().toString().trim();
                if (name.endsWith(".pdccs.ntu.edu.sg")) {
                    name = name.substring(0, name.indexOf(".pdccs.ntu.edu.sg"));
                }
                nodes.put(name, new Node(name, tds.get(2).getContent().getTextExtractor().toString(),
                        tds.get(3).getContent().getTextExtractor().toString(),
                        tds.get(4).getContent().getTextExtractor().toString(),
                        tds.get(5).getContent().getTextExtractor().toString()));
            }
        }
        return nodes;
    }

    public String generateMainUrl(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null) {
            return "hadoop_cluster.jsp?id=" + id;
        }
        return "hadoop_cluster.jsp";
    }
    %>

    <html>
        <head>
            <title>Available Nodes in Hadoop Cluster @NTU</title>
            <link rel="stylesheet" type="text/css" href="styles.css"/>
        </head>
        <body>
            <h2>Available Nodes in Hadoop Cluster @NTU</h2>

            <a href="<%=generateMainUrl(request)%>">Back To Main</a><br/>
            <br/>
            <table>
                <tr>
                    <th>Host Name</th>
                    <th>Running Tasks</th>
                    <th>Maximum<br/>Map Tasks</th>
                    <th>Maximum<br/>Reduce Tasks</th>
                    <th>Failures</th>
                </tr>
                <%generateNodes(out, request);%>
            </table><br/>
            <a href="<%=generateMainUrl(request)%>">Back To Main</a>
        </body>
    </html>
