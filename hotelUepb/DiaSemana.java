package hotelUepb;

public enum DiaSemana {
	SEGUNDA(0), 
	TERCA(0), 
	QUARTA(0), 
	QUINTA(0), 
	SEXTA(0.25), 
	SABADO(0.5), 
	DOMINGO(0.5);
	
	private double taxa;
	
	private DiaSemana(double taxa){
		this.taxa = taxa;
	}

	public double getTaxa() {
		return taxa;
	}
}