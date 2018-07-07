new function() {
	var ws = null;
	var connected = false;

	var serverUrl;
	var connectionStatus;
	var sendMessage;
	
	var connectButton;
	var disconnectButton; 
	var sendButton;

	var open = function() {
		var url = serverUrl.val();
		ws = new WebSocket(url);
		ws.onopen = onOpen;
		ws.onclose = onClose;
		ws.onmessage = onMessage;
		ws.onerror = onError;

		connectionStatus.text('OPENING ...');
		serverUrl.attr('disabled', 'disabled');
		connectButton.hide();
		disconnectButton.show();
	}
	
	var close = function() {
		if (ws) {
			console.log('CLOSING ...');
			ws.close();
		}
		connected = false;
		connectionStatus.text('CLOSED');

		serverUrl.removeAttr('disabled');
		connectButton.show();
		disconnectButton.hide();
		sendMessage.attr('disabled', 'disabled');
		sendButton.attr('disabled', 'disabled');
	}
	
	var clearLog = function() {
		$('#messages').html('');
	}

	var clearUser = function(){
		$('#usuarios').html('');
	}

	var onOpen = function() {
		console.log('OPENED: ' + serverUrl.val());
		connected = true;
		connectionStatus.text('OPENED');
		sendMessage.removeAttr('disabled');
		sendButton.removeAttr('disabled');
		clearUser();
	};
	
	var onClose = function() {
		console.log('CLOSED: ' + serverUrl.val());
		ws = null;
		clearUser();
	};
	
	var onMessage = function(event) {
  		var data = JSON.parse(event.data);
  		switch(data.type) {
			case "usuario":
				clearUser();
				var usuarios = data.name.split(",");
				usuarios.sort();
				for (var i=1; i < usuarios.length; i++) {
					addUsuarios(usuarios[i]);
				}
			    break;
			case "mensagem":
			    addMessage(data.text);
			    break;
		}
	};
	
	var onError = function(event) {
		alert(event.data);
	}
	
	var addMessage = function(data, type) {
		var msg = '<li class="list-group-item list-group-item-primary"> '+data+'</li>';
		if (type === 'SENT') {
			msg='<li class="list-group-item list-group-item-success"> Eu: '+data+'</li>'
		}
		var messages = $('#messages');
		messages.append(msg);
		
		var msgBox = messages.get(0);
		while (msgBox.childNodes.length > 1000) {
			msgBox.removeChild(msgBox.firstChild);
		}
		msgBox.scrollTop = msgBox.scrollHeight;
	}

	var addUsuarios = function(data) {
		var msg = '<li class="list-group-item list-group-item-info"> '+data+'</li>';
		var usuarios = $('#usuarios');
		usuarios.append(msg);
		
		var msgBox = usuarios.get(0);
		while (msgBox.childNodes.length > 1000) {
			msgBox.removeChild(msgBox.firstChild);
		}
		msgBox.scrollTop = msgBox.scrollHeight;
	}

	WebSocketClient = {
		init: function() {
			serverUrl = $('#serverUrl');
			connectionStatus = $('#connectionStatus');
			sendMessage = $('#sendMessage');
			
			connectButton = $('#connectButton');
			disconnectButton = $('#disconnectButton'); 
			sendButton = $('#sendButton');
			
			connectButton.click(function(e) {
				close();
				open();
			});
		
			disconnectButton.click(function(e) {
				close();
			});
			
			sendButton.click(function(e) {
				var msg = $('#sendMessage').val();
				addMessage(msg, 'SENT');
				ws.send(msg);

			});
			
			$('#clearMessage').click(function(e) {
				clearLog();
			});
			
			var isCtrl;
			sendMessage.keyup(function (e) {
				if(e.which == 17) isCtrl=false;
			}).keydown(function (e) {
				if(e.which == 17) isCtrl=true;
				if(e.which == 13 && isCtrl == true) {
					sendButton.click();
					return false;
				}
			});
		}
	};
}

$(function() {
	WebSocketClient.init();
});