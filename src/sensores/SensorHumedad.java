package sensores;

public class SensorHumedad extends Sensor<UnidadHumedad> {
	private static int count = 0;
	
	public SensorHumedad(UnidadHumedad ud, double offset) {
		super(String.format("HUM-%04d", (count++)), ud, offset);
	}
	
	@Override
	public String toString() {
		return "Sensor Humedad (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
