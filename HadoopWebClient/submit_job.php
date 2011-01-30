<?php
ini_set("display_errors", 0);
include("WSClientRunner.inc");

$success = false;
$inputname = "";
$outputname = "";
$message = "";
if (isset($_POST["modelname"]) && isset($_POST["inputname"]) && 
	isset($_POST["description"]) && isset($_POST["arguments"])) {
	try {
		$runner = new java(RUNNER_CLASS);
		$run = $runner->submitJob($_POST["modelname"], $_POST["inputname"], 
			$_POST["description"], $_POST["arguments"]);
		if (!java_is_null($run)) {
			$success = true;
			$inputname = $_POST["inputname"];
			$output = $run->getOutput();
			$outputname = java_values($output->getName());
		} else {
			$success = false;
			$message = "Service is unavailable!";
		}
	} catch (JavaException $e) {
		$success = false;
		$ex = $runner->getException();
		if (!java_is_null($ex)) {
			$message = java_values($ex->getFault()->getFaultString());
		} else {
			$message = "Service is unavailable!";
		}
	} catch (Exception $e) {
		$success = false;
		$message = "Service is unavailable!";
	}
} else {
	$success = false;
	$message = "Required parameters are missing!";
}
$data = array("success" => $success, "servicename" => SERVICE_NAME, 
	"inputname" => $inputname, "outputname" => $outputname, "message" => $message);
echo json_encode($data);
?>
