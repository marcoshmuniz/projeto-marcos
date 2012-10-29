var wsURL = "http://localhost:8080/bbb/rest/votacao/resultado";
recuperarPercentual();
function recuperarPercentual() {
	console.log('recuperarPercentual');
	$.ajax({
		type: 'GET',
		url: wsURL,
		dataType: "json", // data type of response
		success: function(data) {
			console.log('renderizar a tabela');
			var list = data.voto;
			
			$('#resultado1').html('<td>'+list[0].percentual+'</td>');
			$('#resultado2').html('<td>'+list[1].percentual+'</td>');
		}
	});
}

