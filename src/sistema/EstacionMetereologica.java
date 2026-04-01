package sistema;

import sensores.*;
import sensores.unidades.UnidadHumedad;
import sensores.unidades.UnidadPresion;
import sensores.unidades.UnidadTemperatura;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import excepciones.*;

public class EstacionMetereologica {
	private ScheduledExecutorService scheduler;
	
	private class SensorInstalado {
		private Sensor sensor;
		private LocalDate fechaInstalacion;
		
		public SensorInstalado(Sensor sensor) {
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
	
	public List<Sensor> getSensores() {
		return sensores.values().stream().map(si -> si.sensor).toList();
	}
	
	public Sensor getSensor(String id) {
		if(sensores.containsKey(id))
			return sensores.get(id).sensor;
		else return null;
	}
	
	public <T extends Sensor> List<T> getSensoresPorTipo(Class<T> tipo) {
		return sensores.values().stream()
	    		.map(si -> si.sensor)
	    		.filter(s -> tipo.isInstance(s))
	    		.map(s -> tipo.cast(s))
	    		.collect(Collectors.toList());
	}
	
	public void tomarMediciones() {
		sensores.values().forEach(si -> si.sensor.tomarMedicion());
	}
	
	public void iniciarLecturasPeriodicas(long periodoSegundos, int maxLecturas) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		int[] count = {0};  // array para poder modificarlo desde la lambda

		scheduler.scheduleAtFixedRate(() -> {
			if (count[0] >= maxLecturas) {
				scheduler.shutdown();
				return;
			}
			tomarMediciones();
			count[0]++;
		}, 0, periodoSegundos, TimeUnit.SECONDS);
	}

	public void detenerLecturasPeriodicas() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}
	
	private void anadirSensor(Sensor sensor) throws DuplicatedSensorIdException {
		SensorInstalado si = new SensorInstalado(sensor);
		if (sensores.containsKey(si.sensor.getId())) {
			throw new DuplicatedSensorIdException("Ya existe un sensor con el mismo ID", sensores.get(si.sensor.getId()).sensor, si.sensor);
		}
		sensores.put(si.sensor.getId(), si);
	}
	
	public void anadirSensorTemperatura(UnidadTemperatura ud, double offset) throws DuplicatedSensorIdException {
		anadirSensor(new SensorTemperatura(ud, offset));
	}
	
	public void anadirSensorHumedad(UnidadHumedad ud, double offset) throws DuplicatedSensorIdException {
		anadirSensor(new SensorHumedad(ud, offset));
	}
	
	public void anadirSensorPresion(UnidadPresion ud, double offset) throws DuplicatedSensorIdException {
		anadirSensor(new SensorPresion(ud, offset));
	}
	
	@Override
	public String toString() {
		return sensores.values().toString();
	}
}
