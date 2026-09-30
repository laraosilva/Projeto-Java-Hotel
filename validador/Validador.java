package validador;

import classesPagaveis.EstrategiaPagavel;

public class Validador {
	public static void validaValorNegativo(double valor, String mensagem) throws Exception {
		if (valor < 0) {
			throw new Exception(mensagem);
		}
	}
	
	public static void validaValorMenorIgualZero(double valor, String mensagem) throws Exception {
		if (valor <= 0) {
			throw new Exception(mensagem);
		}
	}

	public static void validaNomeNuloOuVazio(String nome, String mensagem) throws Exception {
		if (nome == null || nome.isBlank()) {
			throw new Exception(mensagem);
		}
	}
	
	public static void validaEstrategia(EstrategiaPagavel estrategiaPagavel, String mensagem) throws Exception {
		if(estrategiaPagavel == null) {
			throw new Exception(mensagem);
		}
	}
}
