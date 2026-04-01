package sensores.estrategias;

public class EstrategiaMedia implements Estrategia {
	private int count = 0;
	private double media = 0;
	private double rango;
	
	public EstrategiaMedia(double rango, double valInicial) {
		this.rango = rango;
		this.media = valInicial;
	}

	public double getRango() {
		return rango;
	}

	public void setRango(double rango) {
		this.rango = rango;
	}

	@Override
	public double simularLectura() {
		double max = media + Math.abs(media)*rango;
		double min = media - Math.abs(media)*rango;
		double medicion = (Math.random() * (max - min) + min);
		media = ((media * count) + medicion)/(count + 1);
		count++;
		return medicion;
	}

}
