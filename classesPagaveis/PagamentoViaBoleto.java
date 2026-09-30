package classesPagaveis;

public class PagamentoViaBoleto implements EstrategiaPagavel {

	@Override
	public double aplicarTaxa(double valor) {
		return valor + (valor * 0.02);
	}

	@Override
	public String getInfo() {
		return "Boleto";
	}
}