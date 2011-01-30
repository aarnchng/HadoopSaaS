<?php
$servicename = (isset($_REQUEST["j_svcname"])) ? $_REQUEST["j_svcname"] : "8087";
$serviceurl = "localhost:" . $servicename;
define("JAVA_HOSTS", $serviceurl);
define("JAVA_PREFER_VALUES", false);
require_once("Java.inc");

define("SERVICE_NAME", $servicename);
define("RUNNER_CLASS", "org.me.hadoop.wsclient.WSClientRunner");
?>
