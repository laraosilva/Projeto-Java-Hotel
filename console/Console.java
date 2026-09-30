package console;

import java.util.Scanner;

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

public class Console {
	private Scanner leitor = new Scanner(System.in);
	private ControladorSistemaHotel controlador = new ControladorSistemaHotel();

	public String lerTexto(String mensagem) {
		System.out.print(mensagem);
		return leitor.nextLine();
	}

	public int lerInteiro(String mensagem) {
		System.out.print(mensagem);
		int valor = leitor.nextInt();
		leitor.nextLine();
		return valor;
	}

	public double lerDouble(String mensagem) {
		System.out.print(mensagem);
		double valorDouble = leitor.nextDouble();
		leitor.nextLine();
		return valorDouble;
	}

	public void iniciar() throws Exception {
		int opcao;

		do {
			exibirMenu();
			opcao = lerInteiro("Opcao: ");

			executarOpcao(opcao);

			if (opcao != 0) {
				System.out.println();
			}

		} while (opcao != 0);

		leitor.close();
	}

	private void exibirMenu() {
		System.out.println("\n=== Menu do Hotel ===");
		System.out.println("1 - Cadastrar reserva: ");
		System.out.println("2 - Adicionar quarto a uma reserva: ");
		System.out.println("3 - Alterar forma de pagamento de uma reserva: ");
		System.out.println("4 - Exibir reservas: ");
		System.out.println("5 - Remover reserva: ");
		System.out.println("6 - Buscar reserva por código: ");
		System.out.println("7 - Valor do patrimônio do hotel: ");
		System.out.println("0 - Sair");
	}

	private void executarOpcao(int opcao) throws Exception {
		switch (opcao) {
		case 1:
			opcaoAnotarNovaReserva();
			break;

		case 2:
			opcaoAdicionarQuarto();
			break;

		case 3:
			opcaoAlterarFormaPagamento();
			break;

		case 4:
			opcaoExibirReservas();
			break;

		case 5:
			opcaoApagarReserva();
			break;

		case 6:
			opcaoBuscarReservaPorCodigo();
			break;

		case 7:
			opcaoPatrimonioHotel();
			break;

		case 0:
			System.out.println("Encerrando sistema...");
			break;

		default:
			System.out.println("Opção inválida!! ");
		}
	}
	
	public void opcaoAnotarNovaReserva() throws Exception {

		System.out.println("======= Cadastrando nova reserva =======");

		if (controlador.estaCheio()) {
			System.out.println("Nao foi possivel cadastrar: capacidade maxima de reservas atingida.");
			return;
		}

		String codigo = lerTexto("Codigo: ");
		String nomeHospede = lerTexto("\nNome Hospede: ");
		EstrategiaPagavel estrategiaPagamento = escolherFormaPagamento();
		DiaSemana diaEntrada = escolherDiaEntrada();
		int quantidadeDias = lerInteiro("\nQuantidade de dias: ");

		boolean resultado = controlador.cadastrarReserva(codigo, nomeHospede, estrategiaPagamento, diaEntrada, quantidadeDias);

		if (resultado == false) {
			System.out.println("\nNao foi possivel cadastrar reserva!!!");
			return;
		}

		System.out.println("Reserva cadastrada com sucesso!");
		adicionarQuartoNaReserva(codigo);

		String resposta = lerTexto("\nDeseja adicionar outro quarto? (Sim/Nao): ");
		if ((resposta.equalsIgnoreCase("Sim")))
			do {
				adicionarQuartoNaReserva(codigo);
				resposta = lerTexto("\nDeseja adicionar outro quarto? (Sim/Nao): ");
			} while (resposta.equalsIgnoreCase("Sim"));

		if (resposta.equalsIgnoreCase("Nao")) {
			return;
		} else {
			System.out.println("opcao invalida.");
		}
	}
	
	private EstrategiaPagavel escolherFormaPagamento() {
		System.out.println("\nForma de pagamento");
		System.out.println("1 - Cartao");
		System.out.println("2 - Boleto");
		System.out.println("3 - Pix");

		EstrategiaPagavel estrategiaPagamento = null;
		
		while (estrategiaPagamento == null) {
			int opcao = lerInteiro("\nPagamento: ");
			switch (opcao) {
			case 1:
				estrategiaPagamento = new PagamentoViaCartao();
				break;

			case 2:
				estrategiaPagamento = new PagamentoViaBoleto();
				break;

			case 3:
				estrategiaPagamento = new PagamentoViaPix();
				break;

			default:
				System.out.println("tipo invalido, tente novamente");
			}
		}

		return estrategiaPagamento;
	}
	
	private DiaSemana escolherDiaEntrada() {
		System.out.println("\nDia da Entrada");
		System.out.println("1 - Domingo");
		System.out.println("2 - Segunda");
		System.out.println("3 - Terca");
		System.out.println("4 - Quarta");
		System.out.println("5 - Quinta");
		System.out.println("6 - Sexta");
		System.out.println("7 - Sabado");

		DiaSemana diaEntrada = null;

		while (diaEntrada == null) {
		int opcao = lerInteiro("\nDia: ");
			switch (opcao) {
			case 1:
				diaEntrada = DiaSemana.DOMINGO;
				break;

			case 2:
				diaEntrada = DiaSemana.SEGUNDA;
				break;

			case 3:
				diaEntrada = DiaSemana.TERCA;
				break;

			case 4:
				diaEntrada = DiaSemana.QUARTA;
				break;

			case 5:
				diaEntrada = DiaSemana.QUINTA;
				break;

			case 6:
				diaEntrada = DiaSemana.SEXTA;
				break;

			case 7:
				diaEntrada = DiaSemana.SABADO;
				break;

			default:
				System.out.println("Dia invalido, tente novamente");
			}
		}
		return diaEntrada;
	}
	
	public void opcaoAdicionarQuarto() throws Exception {
		if (naoHaReservas()) {
			return;
		}

		String codigo = lerTexto("Código da reserva: ");

		if (controlador.buscarReservasPorCodigo(codigo) == null) {
			System.out.println("Reserva não encontrada.");
			return;
		}

		adicionarQuartoNaReserva(codigo);
	}
	
	private void adicionarQuartoNaReserva(String codigoReserva) throws Exception {
		Quarto quarto;
		try {
			quarto = criarQuarto();
		} catch (Exception e) {
			System.out.println("Não foi possível criar o quarto: " + e.getMessage());
			return;
		}

		boolean resultado = controlador.adicionarQuartoNaReserva(codigoReserva, quarto);

		if (resultado) {
			System.out.println("Quarto adicionado com sucesso!");
		} else {
			System.out.println("Número de quarto indisponível: já existe uma reserva no sistema.");
		}
	}
	
	private Quarto criarQuarto() throws Exception {
		System.out.println("\nTipo do Quarto");
		System.out.println("1 - Luxo");
		System.out.println("2 - Comum");

		int opcao = lerInteiro("\nQuarto: ");

		String numeroQuarto = lerTexto("\nNumero do quarto: ");
		double valorDiaria = lerDouble("\nValor diaria: ");

		switch (opcao) {
		case 1:
			return new QuartoLuxo("Quarto Luxo", numeroQuarto, valorDiaria);

		case 2:
			return new QuartoComum("Quarto Comum", numeroQuarto, valorDiaria);

		default:
			throw new Exception("Opção de quarto inválida!");
		}
	}
	
	public void opcaoAlterarFormaPagamento() throws Exception {
		if (naoHaReservas()) {
			return;
		}

		String codigo = lerTexto("Código da reserva: ");

		if (controlador.buscarReservasPorCodigo(codigo) == null) {
			System.out.println("Reserva não encontrada.");
			return;
		}

		System.out.println("\nNova forma de pagamento:");
		EstrategiaPagavel novaEstrategia = escolherFormaPagamento();

		boolean resultado = controlador.alterarFormaPagamento(codigo, novaEstrategia);

		if (resultado) {
			System.out.println("Forma de pagamento alterada com sucesso!");
		} else {
			System.out.println("Não foi possível alterar a forma de pagamento.");
		}
	}
	
	public void opcaoExibirReservas() {
		if (naoHaReservas()) {
			return;
		}

		System.out.print("\n=== Caderno de Reservas do Hotel " + controlador.getNomeHotel() + " ===\n"
				+ "\nQuantidade de Reservas: " + controlador.contarReservas()
				+ controlador.exibirRelatorioDeReservas());
	}
	
	public void opcaoApagarReserva() {
		if (naoHaReservas()) {
			return;
		}

		String codigo = lerTexto("Digite o código para remover: ");

		if (controlador.removerReservaPorCodigo(codigo)) {
			System.out.println("Reserva removida com sucesso.");
		} else {
			System.out.println("Reserva não encontrada.");
		}
	}

	public void opcaoBuscarReservaPorCodigo() {
		if (naoHaReservas()) {
			return;
		}

		System.out.println("\n-- Buscar reserva Por Codigo --");

		String codigo = lerTexto("Digite o código da reserva que deseja buscar: ");
		Reserva reserva = controlador.buscarReservasPorCodigo(codigo);

		if (reserva != null) {
			System.out.println(reserva);
		} else {
			System.out.println("Reserva não encontrada.");
		}
	}

	public void opcaoPatrimonioHotel() {
		System.out.println("\n-- Valor do patrimônio do Hotel --");
		System.out.println("R$ " + controlador.calcularPatrimonioTotal());
	}
	
	private boolean naoHaReservas() {
		if (controlador.estaVazio()) {
			System.out.println("\nNenhum hóspede cadastrado!!");
			return true;
		}
		return false;
	}

}