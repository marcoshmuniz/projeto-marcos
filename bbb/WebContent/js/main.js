// The root URL for the RESTful services
var wsURL = "http://localhost:8080/bbb/rest/votacao";// "http://localhost:8080/bbb/rest";

// Register listeners
$('#voto1').click(function() {
	votar($('#voto1').val());
	return false;
});

$('#voto2').click(function() {
	votar($('#voto2').val());
	return false;
});

$('#resultado').click(function() {
	resultado();
	return false;
});

function votar(id) {
	console.log('votar');
	wsUrl = wsURL + '/votar/' + id;
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : wsUrl,
		dataType : "json",
		data : votoToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Voto realizado com sucesso');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('Erro ao realizar o voto: ' + textStatus);
		}
	});
}

// Helper function to serialize all the form fields into a JSON string
function votoToJSON(voto) {
	return JSON.stringify({
		"id" : voto,
	});
}
