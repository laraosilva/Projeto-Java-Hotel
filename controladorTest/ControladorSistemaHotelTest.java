package controladorTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import classesDeQuartos.Quarto;
import classesDeQuartos.QuartoComum;
import classesDeQuartos.QuartoLuxo;
import classesPagaveis.EstrategiaPagavel;
import classesPagaveis.PagamentoViaBoleto;
import classesPagaveis.PagamentoViaCartao;
import classesPagaveis.PagamentoViaPix;
import controlador.ControladorSistemaHotel;
import hotelUepb.DiaSemana;
import hotelUepb.Reserva;

import org.junit.Test;

public class ControladorSistemaHotelTest {

	private ControladorSistemaHotel controlador = new ControladorSistemaHotel();


	@Test
	public void deveCadastrarReserva() throws Exception {

		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		assertTrue(controlador.cadastrarReserva("100", "1", formaDePagamento, DiaSemana.SEGUNDA, 1));
		assertEquals(1, controlador.contarReservas());
	}

	@Test
	public void naoDeveCadastrarDuasReservasComMesmoCodigo() throws Exception {

		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		assertTrue(controlador.cadastrarReserva("100", "Lara", formaDePagamento, DiaSemana.SEGUNDA, 1));

		EstrategiaPagavel formaDePagamento2 = new PagamentoViaCartao();
		assertFalse(controlador.cadastrarReserva("100", "Maria", formaDePagamento2, DiaSemana.TERCA, 1));
	}

	@Test
	public void deveAdicionarQuartoNaReserva() throws Exception {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
		assertTrue(controlador.adicionarQuartoNaReserva("100", quarto));

		Map<String, Quarto> quartos = controlador.buscarReservasPorCodigo("100").getQuartos();
		assertEquals(1, quartos.size());
		assertTrue(quartos.containsKey("1"));
	}

	@Test
	public void naoDeveAdicionarQuartoComMesmoNumeroNaMesmaReserva() throws Exception {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		controlador.adicionarQuartoNaReserva("100", new QuartoComum("Quarto Comum", "1", 100.0));
		boolean resultado = controlador.adicionarQuartoNaReserva("100", new QuartoLuxo("Luxo", "1", 150.0));

		assertFalse(resultado);
		assertEquals(1, controlador.buscarReservasPorCodigo("100").getQuartos().size());
	}

	@Test
	public void deveRetornarCalculoDiariaTotalComDoisQuartos() throws Exception {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);
		controlador.adicionarQuartoNaReserva("100", new QuartoComum("Quarto Comum", "1", 100.0));
		controlador.adicionarQuartoNaReserva("100", new QuartoComum("Quarto Comum", "2", 100.0));

		Reserva reserva = controlador.buscarReservasPorCodigo("100");

		assertEquals(210.0, reserva.calcularDiariaTotal(), 0.001);
	}

	@Test
	public void deveCalcularTaxaDeSabado() throws Exception {
		Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();

		controlador.cadastrarReserva("100", "Lara", formaDePagamento, DiaSemana.SABADO, 1);

		controlador.adicionarQuartoNaReserva("100", quarto);

		Reserva reserva = controlador.buscarReservasPorCodigo("100");

		assertEquals(157.50, reserva.calcularDiariaTotal(), 0.001);
	}

	@Test
	public void deveCalcularValorDoQuartoDeLuxo() throws Exception {
	    Quarto quarto = new QuartoLuxo("Quarto Luxo", "1", 100.0);
	    double valorEsperado = 130.0; 
	    assertEquals(valorEsperado, quarto.calcularValorBase(quarto.getValorDiaria()), 0.001);
	}

	@Test
	public void deveAplicarDescontoNoPix() {
		EstrategiaPagavel formaDePagamento = new PagamentoViaPix();
		assertEquals(95.0, formaDePagamento.aplicarTaxa(100), 0.001);
	}

	@Test
	public void deveAlterarFormaPagamento() throws Exception {
	    Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
	    EstrategiaPagavel cartao = new PagamentoViaCartao();
	    EstrategiaPagavel boleto = new PagamentoViaBoleto();

	    controlador.cadastrarReserva("100", "Maria", cartao, DiaSemana.SEGUNDA, 1);
	    controlador.adicionarQuartoNaReserva("100", quarto);

	    Reserva reserva = controlador.buscarReservasPorCodigo("100");

	    assertEquals(105.0, reserva.calcularDiariaTotal(), 0.001);

	    boolean resultado = controlador.alterarFormaPagamento("100", boleto);

	    assertTrue(resultado);
	    assertEquals(102.0, reserva.calcularDiariaTotal(), 0.001);
	}

	@Test
	public void deveExibirRelatorioComDadosDasReservas() throws Exception {
	    Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
	    EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();

	    controlador.cadastrarReserva("100", "Maria", formaDePagamento, DiaSemana.SEGUNDA, 1);
	    controlador.adicionarQuartoNaReserva("100", quarto);

	    Reserva reserva = controlador.buscarReservasPorCodigo("100");

	    String relatorio = reserva.toString();

	    assertTrue(relatorio.contains("Código : 100"));
	    assertTrue(relatorio.contains("Hóspede : Maria"));
	    assertTrue(relatorio.contains("Forma de pagamento : Cartao"));
	    assertTrue(relatorio.contains("Quantidade de dias : 1"));
	    assertTrue(relatorio.contains("Quarto Comum"));
	    assertTrue(relatorio.contains("Nº: 1"));
	    assertTrue(relatorio.contains("Total a pagar : R$ 105.0"));
	}
	@Test
	public void deveBuscarReservaPorCodigo() {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		Reserva reserva = controlador.buscarReservasPorCodigo("100");

		assertNotNull(reserva);
		assertEquals("100", reserva.getCodigo());
		assertEquals("Maria", reserva.getNomeHospede());
		assertEquals("Cartao", reserva.getEstrategiaPagamento().getInfo());
		assertEquals(DiaSemana.SEGUNDA, reserva.getDiaEntrada());
		assertEquals(1, reserva.getQuantidadeDias());
	}

	@Test
	public void deveRetornarNullAoBuscarCodigoInexistente() {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		assertNull(controlador.buscarReservasPorCodigo("999"));
	}

	@Test
	public void naoDeveAdicionarQuartoEmReservaInexistente() throws Exception {
		Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
		assertFalse(controlador.adicionarQuartoNaReserva("999", quarto));
	}

	@Test
	public void naoDeveCadastrarQuandoQuantidadeDiasInvalida() throws Exception {
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		boolean resultado = controlador.cadastrarReserva("100", "Fabiola", formaDePagamento, DiaSemana.SEGUNDA, -1);
		assertFalse(resultado);
		assertEquals(0, controlador.contarReservas());
	}

	@Test
	public void naoDeveCadastrarQuandoNomeVazio() throws Exception {
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "", formaDePagamento, DiaSemana.SEGUNDA, 1);

		assertEquals(0, controlador.contarReservas());
	}

	@Test
	public void retornaFalsoQuandoNomeVazio() throws Exception {
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		assertFalse(controlador.cadastrarReserva("100", "", formaDePagamento, DiaSemana.SEGUNDA, 1));
	}

	@Test
	public void retornaFalsoQuandoQuantidadeDiasInvalida() throws Exception {
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		assertFalse(controlador.cadastrarReserva("100", "Maria", formaDePagamento, DiaSemana.SEGUNDA, -1));
	}

	@Test
	public void deveRetornarCalculoDiariaTotalComUmQuarto() throws Exception {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);
		controlador.adicionarQuartoNaReserva("100", new QuartoComum("Quarto Comum", "1", 100.0));

		Reserva reserva = controlador.buscarReservasPorCodigo("100");

		assertEquals(105.0, reserva.calcularDiariaTotal(), 0.001);
	}

	@Test
	public void deveRemoverReservaPorCodigo() {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		assertTrue(controlador.removerReservaPorCodigo("100"));
		assertEquals(0, controlador.contarReservas());
	}

	@Test
	public void deveRetornarFalseSeCodigoNaoEncontrado() {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		assertFalse(controlador.removerReservaPorCodigo("999"));
		assertEquals(1, controlador.contarReservas());
	}

	@Test
	public void deveCalcularPatrimonioTotal() throws Exception {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);
		controlador.adicionarQuartoNaReserva("100", new QuartoComum("Quarto Comum", "1", 100.0));

		EstrategiaPagavel estrategia2 = new PagamentoViaCartao();
		controlador.cadastrarReserva("101", "Amalia", estrategia2, DiaSemana.TERCA, 1);
		controlador.adicionarQuartoNaReserva("101", new QuartoComum("Quarto Comum", "12", 100.0));

		double esperado = controlador.buscarReservasPorCodigo("100").calcularDiariaTotal()
				+ controlador.buscarReservasPorCodigo("101").calcularDiariaTotal();
		assertEquals(esperado, controlador.calcularPatrimonioTotal(), 0.001);
	}

	@Test
	public void deveContarReservas() {
		EstrategiaPagavel estrategia = new PagamentoViaCartao();
		controlador.cadastrarReserva("100", "Maria", estrategia, DiaSemana.SEGUNDA, 1);

		EstrategiaPagavel estrategia2 = new PagamentoViaCartao();
		controlador.cadastrarReserva("101", "Amalia", estrategia2, DiaSemana.TERCA, 1);

		assertEquals(2, controlador.contarReservas());
	}

	@Test
	public void deveIniciarComNenhumaReserva() {
		assertEquals(0, controlador.contarReservas());
	}

	@Test
	public void deveEstarVazioQuandoNaoPossuiReservas() {
		assertTrue(controlador.estaVazio());
	}

	@Test
	public void deveAplicarTaxaNoCartao() {
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();
		assertEquals(105.0, formaDePagamento.aplicarTaxa(100), 0.001);
	}

	@Test
	public void deveAplicarTaxaNoBoleto() {
		EstrategiaPagavel formaDePagamento = new PagamentoViaBoleto();

		assertEquals(102.0, formaDePagamento.aplicarTaxa(100.0), 0.001);
	}

	@Test
	public void deveCalcularTaxaDeSexta() throws Exception {
		Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();

		controlador.cadastrarReserva("105", "1", formaDePagamento, DiaSemana.SEXTA, 1);

		controlador.adicionarQuartoNaReserva("105", quarto);

		Reserva reserva = controlador.buscarReservasPorCodigo("105");

		assertEquals(131.25, reserva.calcularDiariaTotal(), 0.001);
	}

	@Test
	public void deveCalcularTaxaDeDomingo() throws Exception {
		Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
		EstrategiaPagavel formaDePagamento = new PagamentoViaCartao();

		controlador.cadastrarReserva("200", "Maria", formaDePagamento, DiaSemana.DOMINGO, 1);

		controlador.adicionarQuartoNaReserva("200", quarto);

		Reserva reserva = controlador.buscarReservasPorCodigo("200");

		assertEquals(157.50, reserva.calcularDiariaTotal(), 0.001);
	}

	@Test
	public void deveCalcularOValorDoQuartoComum() throws Exception {
	    Quarto quarto = new QuartoComum("Quarto Comum", "1", 100.0);
	    double valorEsperado = 100.0;
	    assertEquals(valorEsperado, quarto.calcularValorBase(quarto.getValorDiaria()), 0.001);
	}

	@Test
	public void naoDeveAlterarFormaPagamentoQuandoReservaNaoExiste() throws Exception {
	    EstrategiaPagavel boleto = new PagamentoViaBoleto();

	    boolean resultado = controlador.alterarFormaPagamento("999", boleto);

	    assertFalse(resultado);
	}

}