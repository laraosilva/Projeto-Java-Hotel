package classesDeQuartos;

import validador.Validador;

public abstract class Quarto {  
	private String nomeDoQuarto;
	private String numeroQuarto;
	private double valorDiaria;

	public Quarto(String nomeDoQuarto, String numeroQuarto, double valorDiaria) throws Exception {
		Validador.validaValorNegativo(valorDiaria, "valor da diaria invalido.");
		
		this.nomeDoQuarto = nomeDoQuarto;
		this.numeroQuarto = numeroQuarto;
		this.valorDiaria = valorDiaria;
	}

	public abstract double calcularValorBase(double valor);
	
	public String getNomeDoQuarto() {
		return nomeDoQuarto;
	}

	public String getNumeroQuarto() {
		return numeroQuarto;
	}

	public double getValorDiaria() {
		return valorDiaria;
	}
}




