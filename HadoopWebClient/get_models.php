<?php
ini_set("display_errors", 0);
include("WSClientRunner.inc");

$runner = new java(RUNNER_CLASS);
$models = $runner->listModels();
if (!java_is_null($models)) {
	foreach ($models as $model) {
		$name = $model->getName();
		if (strlen(trim($model->getVersion())) != 0) {
			$name = $name . " " . $model->getVersion();
		}
		if (strlen(trim($model->getAuthor())) != 0) {
			$name = $name . " BY " . $model->getAuthor();
		}
		echo "<option value=\"" . $model->getKey() . "\">" . $name . "</option>";
	}
}
?>
