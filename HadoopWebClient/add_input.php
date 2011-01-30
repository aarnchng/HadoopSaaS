<?php
ini_set("display_errors", 0);
set_time_limit(600);
include("WSClientRunner.inc");

function genRandStr($length=24, $list="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ") {
    mt_srand((double)microtime() * 1000000);
    $string = "";
    if ($length > 0){
        while (strlen($string) < $length){
            $string .= $list[mt_rand(0, strlen($list) - 1)];
        }
    }
    return $string;
}

$success = false;
$inputname = "";
$message = "";
if (isset($_FILES["inputfile"])) {
	$filename = $_FILES["inputfile"]["name"];
	$filename = substr($filename, strrpos($filename, "."));
	$filename = "tmp/" . genRandStr() . $filename;
	if (move_uploaded_file($_FILES["inputfile"]["tmp_name"], $filename)) {
		chmod($filename, 0755);
		if (SERVICE_NAME == "8087") {
			$filename = basename(realpath($filename));
			try {
				$runner = new java(RUNNER_CLASS);
				$input = $runner->addInData($filename);
				if (!java_is_null($input)) {
					$success = true;
					$inputname = java_values($input->getName());
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
			$filename = realpath($filename);
			try {
				$runner = new java(RUNNER_CLASS);
				$filename = $runner->putInData($filename);
				if (!java_is_null($filename)) {
					$input = $runner->addInData($filename);
					if (!java_is_null($input)) {
						$success = true;
						$inputname = java_values($input->getName());
					} else {
						$success = false;
						$message = "Service is unavailable!";
					}
				} else {
					$success = false;
					$message = "Failure in sending input file!";
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
		}
	} else {
		$success = false;
		$message = "Failure in uploading input file!";
	}
} else {
	$success = false;
	$message = "Required parameters are missing!";
}
$data = array("success" => $success, "servicename" => SERVICE_NAME, 
	"inputname" => $inputname, "message" => $message);
echo json_encode($data);
?>
