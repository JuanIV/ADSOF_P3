package sistema;

import sensores.*;
import java.time.*;
import java.util.*;

import excepciones.DuplicatedSensorIdException;

public class EstacionMetereologica {
	
	private class SensorInstalado {
		private Sensor<?> sensor;
		private LocalDate fechaInstalacion;
		
		public SensorInstalado(Sensor<?> sensor) {
			this.sensor = sensor;
			this.fechaInstalacion = LocalDate.now();
		}
		
		@Override
		public String toString() {
			return sensor.getId() + " (desde: " + fechaInstalacion + "): " + sensor.toString();
		}
	}
	
	private String nombre;
	private double longitud;
	private double latitud;
	private Map<String, SensorInstalado> sensores = new HashMap<String, SensorInstalado>();
	
	public EstacionMetereologica(String nombre, double longitud, double latitud) {
		this.nombre = nombre;
		this.longitud = longitud;
		this.latitud = latitud;
	}
	
	/************************ Métodos ****************************/
	
	public String getNombre() {
		return nombre;
	}
	
	public double getLongitud() {
		return longitud;
	}
	
	public double getLatitud() {
		return latitud;
	}
	
	public void anadirSensorTemperatura(UnidadTemperatura ud, double offset) throws DuplicatedSensorIdException {
		SensorInstalado sensor = new SensorInstalado(new SensorTemperatura(ud, offset));
		
		if(sensores.containsKey(sensor.sensor.getId())) {
			throw new DuplicatedSensorIdException("Ya existe un sensor con el mismo ID", sensores.get(sensor.sensor.getId()).sensor, sensor.sensor);
		} else {
			sensores.put(sensor.sensor.getId(), sensor);
		}
	}
	
	public void anadirSensorHumedad(UnidadHumedad ud, double offset) throws DuplicatedSensorIdException {
		SensorInstalado sensor = new SensorInstalado(new SensorHumedad(ud, offset));
		
		if(sensores.containsKey(sensor.sensor.getId())) {
			throw new DuplicatedSensorIdException("Ya existe un sensor con el mismo ID", sensores.get(sensor.sensor.getId()).sensor, sensor.sensor);
		} else {
			sensores.put(sensor.sensor.getId(), sensor);
		}
	}
	
	public void anadirSensorPresion(UnidadPresion ud, double offset) throws DuplicatedSensorIdException {
		SensorInstalado sensor = new SensorInstalado(new SensorPresion(ud, offset));
		
		if(sensores.containsKey(sensor.sensor.getId())) {
			throw new DuplicatedSensorIdException("Ya existe un sensor con el mismo ID", sensores.get(sensor.sensor.getId()).sensor, sensor.sensor);
		} else {
			sensores.put(sensor.sensor.getId(), sensor);
		}
	}
	
	@Override
	public String toString() {
		return sensores.values().toString();
	}
}
