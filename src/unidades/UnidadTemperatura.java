package unidades;

public enum UnidadTemperatura implements Unidad {
	CELSIUS(-273.15, 1000, "ºC"),
	FAHRENHEIT(-459.67, 1832, "ºF"),
	KELVIN(0, 1273.15, " K");
	
	private double min;
	private double max;
	private String simbolo;
	
	private UnidadTemperatura(double min, double max, String simbolo) {
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
