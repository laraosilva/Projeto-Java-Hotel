package controlador;

import classesDeQuartos.Quarto;
import classesPagaveis.EstrategiaPagavel;
import hotelUepb.DiaSemana;
import hotelUepb.Reserva;
import hotelUepb.SistemaHotel;

public class ControladorSistemaHotel {
	private SistemaHotel sistema;

	public ControladorSistemaHotel() {
		this.sistema = new SistemaHotel();
	}

	public boolean cadastrarReserva(String codigo, String nomeHospede, EstrategiaPagavel estrategiaPagamento, DiaSemana diaEntrada, int quantidadeDias) {
		return sistema.cadastrarReserva(codigo, nomeHospede, estrategiaPagamento, diaEntrada, quantidadeDias);
	}

	public boolean adicionarQuartoNaReserva(String codigoReserva, Quarto quarto) {
		return sistema.adicionarQuartoNaReserva(codigoReserva, quarto);
	}

	public boolean alterarFormaPagamento(String codigoReserva, EstrategiaPagavel novaEstrategia) throws Exception {
		return sistema.alterarFormaPagamento(codigoReserva, novaEstrategia);
	}

	public boolean removerReservaPorCodigo(String codigo) {
		return sistema.removerReservaPorCodigo(codigo);
	}

	public Reserva buscarReservasPorCodigo(String codigo) {
		return sistema.buscarReservasPorCodigo(codigo);
	}

	public String exibirRelatorioDeReservas() {
		return sistema.exibirRelatorioDeReservas();
	}

	public double calcularPatrimonioTotal() {
		return sistema.calcularPatrimonioTotal();
	}

	public int contarReservas() {
		return sistema.contarReservas();
	}

	public boolean estaCheio() {
		return sistema.estaCheio();
	}

	public boolean estaVazio() {
		return sistema.estaVazio();
	}

	public String getNomeHotel() {
		return sistema.getNomeHotel();
	}
}