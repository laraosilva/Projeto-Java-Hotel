package hotelUepb;

import java.util.HashMap;
import java.util.Map;

import classesDeQuartos.Quarto;
import classesPagaveis.EstrategiaPagavel;
import validador.Validador;

public class Reserva {
	private String codigo;
	private String nomeHospede;
	private EstrategiaPagavel estrategiaPagamento;
	private DiaSemana diaEntrada;
	private int quantidadeDias;
	private Map<String, Quarto> quartos;

	public Reserva(String codigo, String nomeHospede, EstrategiaPagavel estrategiaPagamento, DiaSemana diaEntrada, int quantidadeDias) throws Exception {
		Validador.validaNomeNuloOuVazio(nomeHospede, "Nenhum nome foi digitado.");
		Validador.validaValorMenorIgualZero(quantidadeDias, "quantidade de dias invalido.");

		this.codigo = codigo;
		this.nomeHospede = nomeHospede;
		this.estrategiaPagamento = estrategiaPagamento;
		this.diaEntrada = diaEntrada;
		this.quantidadeDias = quantidadeDias;
		this.quartos = new HashMap<String, Quarto>();
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getNomeHospede() {
		return nomeHospede;
	}

	public EstrategiaPagavel getEstrategiaPagamento() {
		return estrategiaPagamento;
	}

	public DiaSemana getDiaEntrada() {
		return diaEntrada;
	}

	public int getQuantidadeDias() {
		return quantidadeDias;
	}

	public Map<String, Quarto> getQuartos() {
		return quartos;
	}

	public boolean adicionarQuarto(Quarto quarto) {
		if (quartos.containsKey(quarto.getNumeroQuarto())) {
			return false;
			
		}
		quartos.put(quarto.getNumeroQuarto(), quarto);
		return true;
	}

	public void setEstrategiaPagamento(EstrategiaPagavel novaEstrategia) throws Exception {
		Validador.validaEstrategia(novaEstrategia, "estrategia invalida.");
		this.estrategiaPagamento = novaEstrategia;
	}

	public double calcularDiariaTotal() {
		double total = 0;
		int entradaReserva = diaEntrada.ordinal();

		for (Quarto quarto : quartos.values()) {
			double diariaQuarto = quarto.calcularValorBase(quarto.getValorDiaria());

			for (int i = 0; i < quantidadeDias; i++) {
				DiaSemana diaAtual = DiaSemana.values()[(entradaReserva + i) % 7];
				total += diariaQuarto * (1+diaAtual.getTaxa());
			}
		}

		return estrategiaPagamento.aplicarTaxa(total);
	}

	public String toString() {
		String infoQuartos = "";
		for (Quarto quarto : quartos.values()) {
			infoQuartos += "\n - " + quarto.getNomeDoQuarto() + " | Nº: " + quarto.getNumeroQuarto();
		}
		return "\n\n============================================================" + "\n DADOS DA RESERVA"
				+ "\n============================================================" + "\nCódigo : " + codigo
				+ "\nHóspede : " + nomeHospede + "\nForma de pagamento : " + estrategiaPagamento.getInfo()
				+ "\nQuantidade de dias : " + quantidadeDias + "\n"
				+ "\n------------------------- QUARTOS -------------------------" + infoQuartos + "\n"
				+ "\n------------------------- RESUMO --------------------------" + "\nTotal a pagar : R$ "
				+ calcularDiariaTotal() + "\n============================================================\n\n";
	}
}
