<?php
ini_set("display_errors", 0);
include("WSClientRunner.inc");

$success = false;
$message = "";
if (isset($_POST["k_inputname"]) && isset($_POST["k_outputname"])) {
	try {
		$runner = new java(RUNNER_CLASS);
		$success = java_values($runner->killJob($_POST["k_inputname"], 
			$_POST["k_outputname"]));
		if (!$success) {
			$message = "Failure in killing the job!";
		}
	} catch (Exception $e) {
		$success = false;
		$message = "Service is unavailable!";
	}
} else {
	$success = false;
	$message = "Required parameters are missing!";
}
$data = array("success" => $success, "message" => $message);
echo json_encode($data);
?>
