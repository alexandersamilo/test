var showChangePassDialog = function () {
	$("#changePassDialog").modal("show");
	changePassViewModel.errors.showAllMessages(false);
};

$(function () {
	getNotifications(true);
});

var getNotifications = function (showNoties) {
	var pathname = location.pathname;
	var inNotifications = pathname.indexOf("notification") != -1;
	if (inNotifications) {
		pathname = pathname + "/new";
	} else {
		pathname = pathname + "/notification/new";
	}

	if (!inNotifications && showNoties) {
		$.ajax({
			url: pathname,
			type: 'POST',
			success: function (notificationList) {
				if (notificationList.length > 0) {
					$("#notiesCount").text(notificationList.length);
				}
				notificationList.forEach(function (notification) {
					showNotification(notification);
				});
			}
		});
	} else {
		$.ajax({
			url: pathname + "/count",
			type: 'POST',
			success: function (count) {
				if (count > 0) {
					$("#notiesCount").text(count);
				}
			}
		});
	}
};

var DocumentLight = function () {
	var self = this;
	self.updateFrom = function (data) {
		ko.mapping.fromJS(data, {}, self);
	};
	self.updateFrom(documentLightInitData);
};

var DocumentLightViewModel = function () {
	var self = this;
	self.document = new DocumentLight();

	self.init = function () {
		ko.applyBindings(self.document, $('#documentLightForm')[0]);
		ko.applyBindings(self.document, $("#filesTabLight")[0]);
	};
};

var showDocumentDetailsDialog = function (id) {
	$.ajax({
		url: 'document/details',
		data: {
			'id': id
		},
		type: 'POST',
		success: function (data) {
			$('#content').html(data);
			if (documentId != -1) {
				var model = new DocumentLightViewModel();
				model.init();
				$('#docDialog').modal("show");
			}
		}
	});
};