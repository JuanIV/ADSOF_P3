package sensores;

public class SensorTemperatura extends Sensor<UnidadTemperatura> {
	private static int count  = 0;
	
	public SensorTemperatura(UnidadTemperatura ud, double offset) {
		super(String.format("TEMP-%04d", (count++)), ud, offset);
	}
	
	@Override
	public String toString() {
		return "Sensor Temperatura (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
