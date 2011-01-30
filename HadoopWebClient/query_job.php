<?php
ini_set("display_errors", 0);
include("WSClientRunner.inc");

$success = false;
$inputname = "";
$outputname = "";
$description = "";
$status = "";
$jobname = "";
$jobcount = "";
$startdate = "";
$enddate = "";
$modelname = "";
$modelversion = "";
$modelauthor = "";
$stderr = "";
$stdout = "";
$message = "";
if (isset($_POST["q_inputname"]) && isset($_POST["q_outputname"])) {
	try {
		$runner = new java(RUNNER_CLASS);
		$output = $runner->getOutput($_POST["q_inputname"], $_POST["q_outputname"]);
		if (!java_is_null($output)) {
			$success = true;
			$inputname = $_POST["q_inputname"];
			$outputname = $_POST["q_outputname"];
			$description = java_values($output->getDescription());
			$status = java_values($output->getStatus());
			$jobname = java_values($output->getJobName());
			$jobcount = java_values($output->getJobCount());
			$startdate = java_values($output->getStartDate());
			$enddate = java_values($output->getEndDate());
			$modelname = java_values($output->getModelName());
			$modelversion = java_values($output->getModelVersion());
			$modelauthor = java_values($output->getModelAuthor());
			$stderr = java_values($output->getStdErr());
			$stdout = java_values($output->getStdOut());
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
	"inputname" => $inputname, "outputname" => $outputname, 
	"description" => $description, "status" => $status, "jobname" => $jobname, 
	"jobcount" => $jobcount, "startdate" => $startdate, "enddate" => $enddate, 
	"modelname" => $modelname, "modelversion" => $modelversion, 
	"modelauthor" => $modelauthor, "stderr" => $stderr, "stdout" => $stdout, 
	"message" => $message);
echo json_encode($data);
?>
