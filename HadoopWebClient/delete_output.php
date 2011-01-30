<?php
ini_set("display_errors", 0);
include("WSClientRunner.inc");

$success = false;
$message = "";
if (isset($_POST["d_inputname"]) && isset($_POST["d_outputname"])) {
	try {
		$runner = new java(RUNNER_CLASS);
		$success = java_values($runner->removeInput($_POST["d_inputname"]));
		if (!$success) {
			$message = "Failure in deleting the data!";
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
