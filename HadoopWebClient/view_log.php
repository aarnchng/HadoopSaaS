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
if (isset($_GET["v_inputname"]) && isset($_GET["v_outputname"])) {
	try {
		$runner = new java(RUNNER_CLASS);
		$output = $runner->getOutput($_GET["v_inputname"], $_GET["v_outputname"]);
		if (!java_is_null($output)) {
			$success = true;
			$inputname = $_GET["v_inputname"];
			$outputname = $_GET["v_outputname"];
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
?>

<?php if ($success == true) { ?>
<html>
<head>
	<title>View Log</title>
	<link rel="stylesheet" type="text/css" href="hadoop_service.css"/>
</head>
<body>
	<h3>Input Name</h3>
	<p><?php echo $inputname; ?></p>
	<h3>Output Name</h3>
	<p><?php echo $outputname; ?></p>
	<h3>Standard Error</h3>
	<p><?php echo str_replace("\n", "<br/>", $stderr); ?></p>
	<h3>Standard Output</h3>
	<p><?php echo str_replace("\n", "<br/>", $stdout); ?></p>
</body>
</html>
<?php } else { ?>
<html>
<head>
	<title>Error in Viewing Log</title>
	<link rel="stylesheet" type="text/css" href="hadoop_service.css"/>
</head>
<body>
	<p><?php echo $message; ?></p>
</body>
</html>
<?php } ?>
