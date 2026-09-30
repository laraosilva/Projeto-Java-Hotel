package classesPagaveis;

public class PagamentoViaPix implements EstrategiaPagavel{

	@Override
	public double aplicarTaxa(double valor) {
		return valor - (valor * 0.05);
	}

	@Override
	public String getInfo() {
		return "Pix";
	}
}
