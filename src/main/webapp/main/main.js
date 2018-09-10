$(document).ready(function() {
	$('#sidebarBtn').on('click', function() {
		$('#sidebar').toggleClass('active');
	});
});

$(document).ready(
		function() {
			$('a[href="#navbar-more-show"], .navbar-more-overlay').on(
					'click',
					function(event) {
						event.preventDefault();
						$('body').toggleClass('navbar-more-show');
						if ($('body').hasClass('navbar-more-show')) {
							$('a[href="#navbar-more-show"]').closest('li')
									.addClass('active');
						} else {
							$('a[href="#navbar-more-show"]').closest('li')
									.removeClass('active');
						}
						return false;
					});
		});

function waitAndRedirect() {
	setTimeout(function() {
		location.reload();
	}, 1000);
}
//
// $("a[href='#formEditcontrato:tblViewEditcontrato:tabTarefa']").on('click',
// function() {
// alert('Carregando tarefas...');
// carregarTarefas();
// });

function contratoCarregarTabelas(id) {
	if (id != null) {
		console.log("Contrato " + id);
		carregarTarefas();
		carregarPracas();
		carregarDocs();
		carregarPrevisao();
	}
}

function ordemServicoCarregarTabelas(id) {
	if (id != null) {
		console.log("OS " + id);

		carregarFuncionarios();
		carregarServicos();
		carregarProdutos();
		carregarDocs();
		
		carregarFuncionarios();
		carregarServicos();
		carregarProdutos();
		carregarDocs();
		PF('dlgResumo').show();
	}
}

function ordemServicoCarregarTabelasJustLoad(id) {
	if (id != null) {
		console.log("OS " + id);

		carregarFuncionarios();
		carregarServicos();
		carregarProdutos();
		carregarDocs();
	}
}

function adjustPositionning(dialog) {

	if ($(window).width() < 400)
		return;

	var y = 80;
	var padding = 200;
	dialog.offset({
		top : y,
		left : padding
	});
}

function preencheEnderecoCEP(cep, form) {

	var campoCep = document.getElementById(form + ":cep");
	var campoEndereco = document.getElementById(form + ":endereco");
	var campoUF = document.getElementById(form + ":uf");
	var campoCidade = document.getElementById(form + ":cidade");

	campoEndereco.value = "...";
	campoUF.value = "...";
	campoCidade.value = "...";

	cep.replace(/\D/g, '');

	if (cep != "") {

		// Expressão regular para validar o CEP.
		var validacep = /^[0-9]{8}$/;

		// Valida o formato do CEP.
		if (validacep.test(cep)) {
			// Consulta o webservice viacep.com.br/
			$.getJSON("//viacep.com.br/ws/" + cep + "/json/?callback=?",
					function(dados) {
						if (!("erro" in dados)) {
							// Atualiza os campos com os valores da consulta.
							campoEndereco.value = dados.logradouro
									+ ", <número e complemento>, "
									+ dados.bairro;
							campoUF.value = dados.uf;
							campoCidade.value = dados.localidade;
						} // end if.
						else {
							campoEndereco.value = "CEP não encontrado";
						}
					});
		} else {
			campoEndereco.value = "CEP não encontrado";
		}
	}
}

function preencheEnderecoCEPPraca(cep, form) {

	var campoCep = document.getElementById(form + ":cep");
	var campoEndereco = document.getElementById(form + ":endereco");

	campoEndereco.value = "...";

	cep.replace(/\D/g, '');

	if (cep != "") {

		// Expressão regular para validar o CEP.
		var validacep = /^[0-9]{8}$/;

		// Valida o formato do CEP.
		if (validacep.test(cep)) {
			// Consulta o webservice viacep.com.br/
			$.getJSON("//viacep.com.br/ws/" + cep + "/json/?callback=?",
					function(dados) {
						if (!("erro" in dados)) {
							// Atualiza os campos com os valores da consulta.
							campoEndereco.value = dados.logradouro + ", "
									+ dados.bairro + ", " + dados.localidade
									+ "/" + dados.uf;
							geocode(campoEndereco.value);
						} // end if.
						else {
							campoEndereco.value = "CEP não encontrado";
						}
					});
		} else {
			campoEndereco.value = "CEP não encontrado";
		}
	}
}

function abrePracaDialog() {
	PF('dlgPraca').show();
	var endereco = document
			.getElementById("formPraca:tblViewEditChamado:endereco").value;
	geocode(endereco);
}

$(document).ready(function() {
	$('.menu-anchor').on('click touchstart', function(e) {
		$('html').toggleClass('menu-active');
		e.preventDefault();
	});
})

function geocode(address) {
	PF('geoMap').geocode(address);
}

function reverseGeocode() {
	var lat = document.getElementById('lat').value, lng = document
			.getElementById('lng').value;

	PF('revGeoMap').reverseGeocode(lat, lng);
}