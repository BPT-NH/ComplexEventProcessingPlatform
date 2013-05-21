var FORMISDIRTY = false;
var FORMCONFIRM = true;

$(window).on('beforeunload', function() {
		return warnOnPageExit("You did not save your changes!");
	}
);

function warnOnPageExit(formdirtymessage) {
	if (FORMCONFIRM && FORMISDIRTY) {
		return formdirtymessage;
	};
}
function setDirty() {
	FORMISDIRTY = true;
}
function dontConfirm() {
	FORMCONFIRM = false;
}

$("window").on('onload', function() {
	$("input[type='text']").change(
			function(){
				FORMISDIRTY = true;
			}
		);
	}
);