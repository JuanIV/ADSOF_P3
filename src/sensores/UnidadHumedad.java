package sensores;

public enum UnidadHumedad {
	HUMEDAD(0, 100, "%");
	
	private double min;
	private double max;
	private String simbolo;
	
	private UnidadHumedad(double min, double max, String simbolo) {
		this.min = min;
		this.max = max;
		this.simbolo = simbolo;
	}
	
	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
	
	public String getSimbolo() {
		return simbolo;
	}
	
	public boolean inRange(double value) {
		return (value >= min)&&(value <= max);
	}
}
