<?php ini_set("display_errors", 0); ?>
<html>
<head>
	<title>Hadoop Service Web Client</title>
	<link rel="stylesheet" type="text/css" href="hadoop_service.css"/>
	<script type="text/javascript" src="jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="jquery.timers-1.2.js"></script>
	<script type="text/javascript" src="jquery.json-2.2.min.js"></script>
	<script type="text/javascript" src="jquery.upload-1.0.2.js"></script>
	<script type="text/javascript" src="jstorage.min.js"></script>
	<script type="text/javascript" src="hadoop_service.js"></script>
</head>
<body>
	<div id="divSubmitJobWait"><p>Uploading...</p></div>
	<div id="divSubmitJob">
		<table>
			<tr><td colspan="2"><div id="divSubmitJobMsg">&nbsp;</div></td></tr>
<?php if (!isset($_GET["advanced"])) { ?>
			<form><tr><td class="label">Input File<br/>(&#60; 20M):</td><td><input type="file" name="inputfile" accept="application/zip" onchange="uploadFile();"/></td></tr></form>
<?php } ?>
			<form><tr><td class="label">Model Name:</td><td><select id="models" name="modelname"/></td></tr>
			<tr><td>&nbsp;</td><td><input type="button" value="Refresh Models" onclick="getModels();"/></td></tr>
<?php if (!isset($_GET["advanced"])) { ?>
			<tr><td colspan="2"><input type="hidden" name="inputname" value=""/></td></tr>
<?php } else { ?>
			<tr><td class="label">Input Name:</td><td><input type="text" name="inputname" size="35" maxlength="100" onchange="enableSubmitJob();"/></td></tr>
<?php } ?>
			<tr><td class="label">Arguments:</td><td><input type="text" name="arguments" size="35" maxlength="100"/></td></tr>
			<tr><td class="label">Description:</td><td><input type="text" name="description" size="35" maxlength="100"/></td></tr>
			<tr><td colspan="2" class="alignr"><input id="btnSubmitJob" type="button" value="Submit Job" disabled="disabled" onclick="submitJob();"/></td></tr></form>
		</table>
	</div>
	<div id="divTrackJobWait"><p>Tracking...</p></div>
	<div id="divTrackJob">
		<table>
			<tr><td colspan="2"><div id="divTrackJobMsg">&nbsp;</div></td></tr>
			<form><tr><td class="label">Input Name:</td><td><input type="text" name="t_inputname" size="25" maxlength="50"/></td></tr>
			<tr><td class="label">Output Name:</td><td><input type="text" name="t_outputname" size="25" maxlength="50"/></td></tr>
			<tr><td colspan="2" class="alignr"><input id="btnTrackJob" type="button" value="Track Job" onclick="trackJob();"/></td></tr></form>
		</table>
	</div>
	<div id="divTitle">
		<h2>Hadoop Service Web Client<br/>
		<span>For Hadoop Cluster @NTU</span></h2>
	</div>
	<div id="divServices">
		<select id="services" name="servicename">
			<option value="8087" selected="selected">Production</option>
			<option value="8086">Development</option>
			<option value="8085">Amazon EC2</option>
		</select>
	</div>
	<div id="divNavigationBar">
		<a id="aSubmitJob" href="#" onclick="setVisible('#divSubmitJob', '#divTrackJob');">Submit A Job</a>
		&nbsp;&nbsp;
		<a id="aTrackJob" href="#" onclick="setVisible('#divTrackJob', '#divSubmitJob');">Track A Job</a>
	</div>
	<div id="divJobs">
		<div id="divJobsMsg">&nbsp;</div>
		<table id="jobs">
			<col/><col id="actions"/>
			<tr id="header"><th>Job Information</th><th>Available Actions</th></tr>
		</table>
	</div>
</body>
</html>
