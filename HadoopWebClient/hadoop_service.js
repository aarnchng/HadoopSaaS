var poll_interval = 15000;
function getModels() {
	$("#divSubmitJobMsg").html("&nbsp;");
	$("#models").empty();
	$("#models").load("get_models.php", {j_svcname: $("#services").val()}, 
		function() { enableSubmitJob(); });
}
function uploadFile() {
	$("#divSubmitJobMsg").html("&nbsp;");
	var filename = $("input[name=inputfile]").val();
	var extension = filename.substr(filename.lastIndexOf("."));
	if (extension == ".zip") {
		$("#divSubmitJobWait").html("<p>Uploading...</p>");
		$("#divSubmitJobWait").show();
		$("input[name=inputfile]").upload("add_input.php", 
			{j_svcname: $("#services").val()}, function(result) {
				if (result.success == true) {
					$("#divSubmitJobWait").hide();
					$("input[name=inputname]").val(result.inputname);
				} else {
					$("#divSubmitJobWait").hide();
					$("#divSubmitJobMsg").html(result.message);
				}
				enableSubmitJob();
			}, "json");
	} else {
		$("input[name=inputfile]").val("");
		$("#divSubmitJobMsg").html("Only zip file (*.zip) is " + 
			"allowed to be uploaded!");
	}
}
function enableSubmitJob() {
	if (($("#models").val() != null) && 
		($.trim($("#models").val()).length != 0) && 
		($.trim($("input[name=inputname]").val()).length != 0)) {
		$("#btnSubmitJob").removeAttr("disabled");
	} else {
		$("#btnSubmitJob").attr("disabled", "disabled");
	}
}
function clearSubmitJob() {
	$("#divSubmitJobMsg").html("&nbsp;");
	$("input[name=inputfile]").val("");
	$("#models").empty();
	$("input[name=arguments]").val("");
	$("input[name=description]").val("");
	$("input[name=inputname]").val("");
	enableSubmitJob();
	$("#divSubmitJob").hide();
}
function submitJob() {
	$("#divSubmitJobMsg").html("&nbsp;");
	$("#divSubmitJobWait").html("<p>Submitting...</p>");
	$("#divSubmitJobWait").show();
	$.post("submit_job.php", {j_svcname: $("#services").val(), 
		modelname: $("#models").val(), 
		inputname: $("input[name=inputname]").val(), 
		description: $("input[name=description]").val(), 
		arguments: $("input[name=arguments]").val()}, function(result) {
			if (result.success == true) {
				$("#divSubmitJobWait").hide();
				clearSubmitJob();
				/* Code For Tracking Of Job -- Start */
				$.jStorage.set(result.outputname, $.toJSON(result));
				addJob(result);
				$(document).everyTime(poll_interval, 
					"timer_" + result.outputname, function() {
						queryJob(result);
					});
				/* Code For Tracking Of Job -- End */
			} else {
				$("#divSubmitJobWait").hide();
				$("#divSubmitJobMsg").html(result.message);
			}
		}, "json");
}

function clearTrackJob() {
	$("#divTrackJobMsg").html("&nbsp;");
	$("input[name=t_inputname]").val("");
	$("input[name=t_outputname]").val("");
	$("#divTrackJob").hide();
}
function trackJob() {
	$("#divTrackJobMsg").html("&nbsp;");
	if ($.trim($("input[name=t_inputname]").val()).length == 0) {
		$("#divTrackJobMsg").html("Fill in the input name of the job!");
		return;
	}
	if ($.trim($("input[name=t_outputname]").val()).length == 0) {
		$("#divTrackJobMsg").html("Fill in the output name of the job!");
		return;
	}
	if ($.inArray($("input[name=t_outputname]").val(), jobs_outputnames) != -1) {
		$("#divTrackJobMsg").html("The job has already being tracked!");
		return;
	}
	$("#divTrackJobWait").show();
	$.post("query_job.php", {j_svcname: $("#services").val(), 
		q_inputname: $("input[name=t_inputname]").val(), 
		q_outputname: $("input[name=t_outputname]").val()}, function(result) {
			if (result.success == true) {
				$("#divTrackJobWait").hide();
				clearTrackJob();
				/* Code For Tracking Of Job -- Start */
				$.jStorage.set(result.outputname, $.toJSON(result));
				addJob(result);
				if ((result.status == "starting") || (result.status == "running")) {
					$(document).everyTime(poll_interval, 
						"timer_" + result.outputname, function() {
							queryJob(result);
						});
				}
				/* Code For Tracking Of Job -- End */
			} else {
				$("#divTrackJobWait").hide();
				$("#divTrackJobMsg").html(result.message);
			}
		}, "json");
}

function queryJob(job) {
	$("#divJobsMsg").html("&nbsp;");
	$.post("query_job.php", {j_svcname: job.servicename, 
		q_inputname: job.inputname, q_outputname: job.outputname}, 
		function(result) {
			if (result.success == true) {
				displayJob(result);
				if (job.status == undefined) {
					$.jStorage.set(job.outputname, $.toJSON(result));
				}
				if ((result.status != "starting") && (result.status != "running")) {
					$.jStorage.set(job.outputname, $.toJSON(result));
					$(document).stopTime("timer_" + job.outputname);
					var index = $.inArray(job.outputname, jobs_notracking);
					if (index != -1) {
						jobs_notracking.splice(index, 1);
					}
				}
			} else {
				$("#divJobsMsg").html("Failure in Checking " + job.outputname + 
					" : " + result.message);
			}
		}, "json");
}
function killJob(job) {
	$("#divJobsMsg").html("&nbsp;");
	$("#akill_" + job.outputname).unbind("click", function() {
			killJob(job);
		});
	$("#akill_" + job.outputname).removeAttr("href");
	$.post("kill_job.php", {j_svcname: job.servicename, 
		k_inputname: job.inputname, k_outputname: job.outputname}, 
		function(result) {
			if (result.success == true) {
				queryJob(job);
			} else {
				$("#divJobsMsg").html("Failure in Killing " + job.outputname + 
					" : " + result.message);
			}
		}, "json");
}

function deleteOutput(job) {
	$("#divJobsMsg").html("&nbsp;");
	$.post("delete_output.php", {j_svcname: job.servicename, 
		d_inputname: job.inputname, d_outputname: job.outputname}, 
		function(result) {
			if (result.success == true) {
				$("#" + job.outputname).remove();
				var index = $.inArray(job.outputname, jobs_outputnames);
				if (index != -1) {
					jobs_outputnames.splice(index, 1);
				}
				$.jStorage.deleteKey(job.outputname);
			} else {
				$("#divJobsMsg").html("Failure in Deleting " + job.outputname + 
					" : " + result.message);
			}
		}, "json");
}

var jobs_notracking = [];
var jobs_outputnames = [];
var header="<tr id=\"header\"><th>Job Information</th><th>Available Actions</th></tr>";
function addJob(job) {
	jobs_outputnames.push(job.outputname);
	$("#header").remove();
	$("#jobs").prepend("<tr id=\"" + job.outputname + "\"></tr>");
	$("#jobs").prepend(header);
	displayJob(job);
}
function getModelName(job) {
	var modelname = job.modelname;
	if ($.trim(job.modelversion).length != 0) {
		modelname = modelname + " " + job.modelversion;
	}
	if ($.trim(job.modelauthor).length != 0) {
		modelname = modelname + " BY " + job.modelauthor;
	}
	return modelname;
}
function getServiceName(job) {
	var servicename = "Production Cluster";
	if (job.servicename == "8086") {
		servicename = "Development Cluster";
	} else if (job.servicename == "8085") {
		servicename = "Amazon EC2 Cluster";
	}
	return servicename;
}
function displayJob(job) {
	$("#" + job.outputname).empty();
	row = "<td>";
	if (job.status == undefined) {
		row += "<table id=\"job\"><tr><td class=\"label\">Input Name:</td><td>" + job.inputname + 
			"</td></tr><tr><td class=\"label\">Output Name:</td><td>" + job.outputname + "</td></tr>";
		row += "<tr><td class=\"label\">Target Name:</td><td>" + getServiceName(job) + "</td></tr>";
		row += "<tr><td class=\"label\">Status:</td><td>CHECKING...</td></tr></table></td>" + 
			"<td class=\"alignc\">";
		row += "<a id=\"astop_" + job.outputname + "\" href=\"#\">";
		row += ($.inArray(job.outputname, jobs_notracking) == -1) ? "Stop Tracking" : "Start Tracking";
		row += "</a><br/><br/>";
		row += "<a id=\"acheck_" + job.outputname + "\" href=\"#\">Check Now</a><br/>";
	} else if ((job.status == "starting") || (job.status == "running")) {
		row += "<table id=\"job\"><tr><td class=\"label\">Input Name:</td><td>" + job.inputname + 
			"</td></tr><tr><td class=\"label\">Output Name:</td><td>" + job.outputname + "</td></tr>" + 
			"<tr><td class=\"label\">Model Name:</td><td>" + getModelName(job) + "</td></tr>" + 
			"<tr><td class=\"label\">Description:</td><td>" + job.description + "</td></tr>";
		row += "<tr><td class=\"label\">Target Name:</td><td>" + getServiceName(job) + "</td></tr>";
		row += "<tr><td class=\"label\">Status:</td><td>" + job.status.toUpperCase() + "...</td></tr>" + 
			"<tr><td class=\"label\">Hadoop Progress:</td><td>" + job.jobname + " / " + job.jobcount + 
			"</td></tr></table></td><td class=\"alignc\">";
		row += "<a id=\"astop_" + job.outputname + "\" href=\"#\">";
		row += ($.inArray(job.outputname, jobs_notracking) == -1) ? "Stop Tracking" : "Start Tracking";
		row += "</a><br/><br/>";
		row += "<a id=\"akill_" + job.outputname + "\" href=\"#\">Kill Job</a><br/><br/>";
		row += "<a id=\"acheck_" + job.outputname + "\" href=\"#\">Check Now</a><br/>";
	} else if (job.status == "success") {
		row += "<table id=\"job\"><tr><td class=\"label\">Input Name:</td><td>" + job.inputname + 
			"</td></tr><tr><td class=\"label\">Output Name:</td><td>" + job.outputname + "</td></tr>" + 
			"<tr><td class=\"label\">Model Name:</td><td>" + getModelName(job) + "</td></tr>" + 
			"<tr><td class=\"label\">Description:</td><td>" + job.description + "</td></tr>";
		row += "<tr><td class=\"label\">Target Name:</td><td>" + getServiceName(job) + "</td></tr>";
		row += "<tr><td class=\"label\">Start Date:</td><td>" + job.startdate + "</td></tr>" + 
			"<tr><td class=\"label\">End Date:</td><td>" + job.enddate + "</td></tr></table></td>" + 
			"<td class=\"alignc\">";
		row += "<a target=\"_blank\" href=\"get_output.php?j_svcname=" + job.servicename + "&g_inputname=" + 
			job.inputname + "&g_outputname=" + job.outputname + "\">Download Data</a><br/>";
		row += "<a id=\"adelete_" + job.outputname + "\" href=\"#\">Delete Data</a><br/>";
		row += "<a target=\"_blank\" href=\"view_log.php?j_svcname=" + job.servicename + "&v_inputname=" + 
			job.inputname + "&v_outputname=" + job.outputname + "\">View Log</a><br/><br/>";
	} else {
		row += "<table id=\"job\"><tr><td class=\"label\">Input Name:</td><td>" + job.inputname + 
			"</td></tr><tr><td class=\"label\">Output Name:</td><td>" + job.outputname + "</td></tr>" + 
			"<tr><td class=\"label\">Model Name:</td><td>" + getModelName(job) + "</td></tr>" + 
			"<tr><td class=\"label\">Description:</td><td>" + job.description + "</td></tr>";
		row += "<tr><td class=\"label\">Target Name:</td><td>" + getServiceName(job) + "</td></tr>";
		row += "<tr><td class=\"label\">Status:</td><td>" + job.status.toUpperCase() + "!</td></tr>" + 
			"</table></td><td class=\"alignc\">";
		row += "<a id=\"adelete_" + job.outputname + "\" href=\"#\">Delete Data</a><br/>";
		row += "<a target=\"_blank\" href=\"view_log.php?j_svcname=" + job.servicename + "&v_inputname=" + 
			job.inputname + "&v_outputname=" + job.outputname + "\">View Log</a><br/><br/>";
	}
	row += "<a id=\"aremove_" + job.outputname + "\" href=\"#\">Remove From List</a></td>";
	$("#" + job.outputname).html(row);
	$("#astop_" + job.outputname).click(function() {
			if ($("#astop_" + job.outputname).html() == "Start Tracking") {
				$(document).everyTime(poll_interval, "timer_" + job.outputname, 
					function() {
						queryJob(job);
					});
				var index = $.inArray(job.outputname, jobs_notracking);
				if (index != -1) {
					jobs_notracking.splice(index, 1);
				}
				$("#astop_" + job.outputname).html("Stop Tracking");
			} else {
				$(document).stopTime("timer_" + job.outputname);
				jobs_notracking.push(job.outputname);
				$("#astop_" + job.outputname).html("Start Tracking");
			}
		});
	$("#acheck_" + job.outputname).click(function() {
			queryJob(job);
		});
	$("#akill_" + job.outputname).click(function() {
			killJob(job);
		});
	$("#adelete_" + job.outputname).click(function() {
			deleteOutput(job);
		});
	$("#aremove_" + job.outputname).click(function() {
			$(document).stopTime("timer_" + job.outputname);
			$("#" + job.outputname).remove();
			var index = $.inArray(job.outputname, jobs_notracking);
			if (index != -1) {
				jobs_notracking.splice(index, 1);
			}
			index = $.inArray(job.outputname, jobs_outputnames);
			if (index != -1) {
				jobs_outputnames.splice(index, 1);
			}
			$.jStorage.deleteKey(job.outputname);
		});
}

function setVisible(main_id, hide_id) {
	if ($(main_id).is(":visible")) {
		$(main_id).hide();
	} else {
		$(main_id).show();
		$(hide_id).hide();
	}
}

$(document).ready(function() {
	$.each($.jStorage.index(), function(index, value) {
			var job = $.evalJSON($.jStorage.get(value));
			addJob(job);
			if ((job.status == undefined) || (job.status == "starting") || 
				(job.status == "running")) {
				$(document).everyTime(poll_interval, "timer_" + job.outputname, 
					function() {
						queryJob(job);
					});
			}
		});
	});
