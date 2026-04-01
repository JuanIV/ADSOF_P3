package sensores.estrategias;

public class EstrategiaRango implements Estrategia {
	private double probabilidadFallo;
	private double min;
	private double max;
	
	public EstrategiaRango(double prob, double min, double max) {
		this.probabilidadFallo = prob;
		this.max = max;
		this.min = min;
	}
	
	public double getProbabilidadFallo() {
		return probabilidadFallo;
	}

	public void setProbabilidadFallo(double probabilidadFallo) {
		this.probabilidadFallo = probabilidadFallo;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	@Override
	public double simularLectura() {
		if((Math.random() * 100) > probabilidadFallo)
			return (Math.random() * (max - min) + min);
		else
			return (Math.random() * (max - min) + max);
	}
}
