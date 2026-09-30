package hotelUepb;

import java.util.ArrayList;
import java.util.List;

import classesDeQuartos.Quarto;
import classesPagaveis.EstrategiaPagavel;

public class SistemaHotel {
	private List<Reserva> reservasAtivas;
	private String nomeHotel;
	private int capacidadeMaxima;

	public SistemaHotel() {
		this.reservasAtivas = new ArrayList<Reserva>();
		this.nomeHotel = "Raio de Sol";
		this.capacidadeMaxima = 3;
	}

	public String getNomeHotel() {
		return nomeHotel;
	}

	public boolean cadastrarReserva(String codigo, String nomeHospede, EstrategiaPagavel estrategiaPagamento, DiaSemana diaEntrada, int quantidadeDias) {
		if(estaCheio()) {
			return false;
		}
		
		for (Reserva reserva : reservasAtivas) {
			if (reserva.getCodigo().equals(codigo)) {
				return false;
			}
		}

		try {
			Reserva novaReserva = new Reserva(codigo, nomeHospede, estrategiaPagamento, diaEntrada, quantidadeDias);
			reservasAtivas.add(novaReserva);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean adicionarQuartoNaReserva(String codigoReserva, Quarto quarto) {
		Reserva reserva = buscarReservasPorCodigo(codigoReserva);
		if (reserva == null) {
			return false;
		}
		if (quartoJaCadastradoEmOutraReserva(quarto.getNumeroQuarto())) {
			return false;
		}
		return reserva.adicionarQuarto(quarto);
		
	}

	private boolean quartoJaCadastradoEmOutraReserva(String numeroQuarto) {
		boolean encontrado = false;
		for (Reserva reservaExistente : reservasAtivas) {
			if (reservaExistente.getQuartos().containsKey(numeroQuarto)) {
				encontrado = true;
			}
		}
		return encontrado;
	}
	
	public boolean alterarFormaPagamento(String codigoReserva, EstrategiaPagavel novaEstrategia) throws Exception {
		Reserva reserva = buscarReservasPorCodigo(codigoReserva);
		if (reserva == null) {
			return false;
		}
		try {
			reserva.setEstrategiaPagamento(novaEstrategia);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean removerReservaPorCodigo(String codigo) {
		for (int i = 0; i < reservasAtivas.size(); i++) {
			if (reservasAtivas.get(i).getCodigo().equals(codigo)) {
				reservasAtivas.remove(i);
				return true;
			}
		}
		return false;
	}

	public Reserva buscarReservasPorCodigo(String codigo) {
		for (Reserva minhaReserva : reservasAtivas) {
			if (minhaReserva.getCodigo().equals(codigo)) {
				return minhaReserva;
			}
		}
		return null;
	}

	public String exibirRelatorioDeReservas() {
		String info = "";
		for (Reserva minhaReserva : reservasAtivas) {
			info += minhaReserva.toString();
		}
		return info;
	}

	public double calcularPatrimonioTotal() {
		double soma = 0;
		for (Reserva minhaReserva : reservasAtivas) {
			soma += minhaReserva.calcularDiariaTotal();
		}
		return soma;
	}

	public int contarReservas() {
		return reservasAtivas.size();
	}

	public boolean estaCheio() {
		return reservasAtivas.size() >= this.capacidadeMaxima;
	}

	public boolean estaVazio() {
		return reservasAtivas.isEmpty();
	}
}