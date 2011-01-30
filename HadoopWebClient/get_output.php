<?php
ini_set("display_errors", 0);
include("WSClientRunner.inc");

$destination = "tmp/";
$path = realpath($destination);
$success = false;
$message = "";
if (isset($_GET["g_inputname"]) && isset($_GET["g_outputname"])) {
	try {
		$runner = new java(RUNNER_CLASS);
		$output = $runner->getCompressedWCOutput($_GET["g_inputname"], 
			$_GET["g_outputname"], "*");
		if (!java_is_null($output)) {
			if (SERVICE_NAME == "8087") {
				$success = true;
				$filename = $output->getFileName();
				header("Content-type: application/zip");
				header("Content-Disposition: attachment; filename=\"" . 
					$_GET["g_outputname"] . ".zip\"");
				readfile($destination . $filename);
			} else {
				$filename = $output->getFileName();
				$filename = $runner->getOutData($path, $filename);
				if (!java_is_null($filename)) {
					$success = true;
					header("Content-type: application/zip");
					header("Content-Disposition: attachment; filename=\"" . 
						$_GET["g_outputname"] . ".zip\"");
					readfile($destination . basename($filename));
				} else {
					$success = false;
					$message = "Failure in receiving output file!";
				}
			}
		} else {
			$success = false;
			$message = "Service is unavailable!";
		}
	} catch (JavaException $e) {
		$success = false;
		$ex = $runner->getException();
		if (!java_is_null($ex)) {
			$message = $ex->getFault()->getFaultString();
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

<?php if ($success == false) { ?>
<html>
<head>
	<title>Error in Downloading Data</title>
	<link rel="stylesheet" type="text/css" href="hadoop_service.css"/>
</head>
<body>
	<p><?php echo $message; ?></p>
</body>
</html>
<?php } ?>
