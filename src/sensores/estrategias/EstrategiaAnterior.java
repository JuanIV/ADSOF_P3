package sensores.estrategias;

public class EstrategiaAnterior implements Estrategia{
	private double rango;
	private double anterior;
	
	public EstrategiaAnterior(double rango, double valInicial) {
		this.rango = rango;
		this.anterior = valInicial;
	}
	
	public double getRango() {
		return rango;
	}

	public void setRango(double rango) {
		this.rango = rango;
	}

	@Override
	public double simularLectura() {
		double max = anterior + Math.abs(anterior)*rango;
		double min = anterior - Math.abs(anterior)*rango;
		double medicion = (Math.random() * (max - min) + min);
		anterior = medicion;
		return medicion;
	}
}
