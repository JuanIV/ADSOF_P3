package sistema;

import sensores.*;
import unidades.*;
import conversores.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import excepciones.*;
import sensores.estrategias.*;

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
			StringBuilder str = new StringBuilder();
			str.append(sensor.getId() + "("+sensor.getUnidadLectura().getSimbolo()+")");
			if(!sensor.getUnidadEscritura().equals(sensor.getUnidadLectura()))
				str.append(" con conversor a "+sensor.getUnidadEscritura().getSimbolo());
			str.append(": "+sensor.getHistorial());
			if(!sensor.getHistorial().isEmpty())
				str.append(String.format(" -- MIN: %.2f MAX: %.2f AVG: %.2f", sensor.getMinimo(), sensor.getMaximo(), sensor.getMedia()));
			return str.toString();
		}
	}
	
	private String nombre;
	private double longitud;
	private double latitud;
	private double cambioBrusco;
	private Map<String, SensorInstalado> sensores = new HashMap<String, SensorInstalado>();
	
	public EstacionMetereologica(String nombre, double longitud, double latitud, double cambioBrusco) {
		this.nombre = nombre;
		this.longitud = longitud;
		this.latitud = latitud;
		if(cambioBrusco > 100) cambioBrusco = 100;
		if(cambioBrusco < 0) cambioBrusco = 0;
		this.cambioBrusco = cambioBrusco;
	}
	
	public EstacionMetereologica(String nombre, double longitud, double latitud) {
		this.nombre = nombre;
		this.longitud = longitud;
		this.latitud = latitud;
		this.cambioBrusco = 50.0;
	}
	
	/************************ Setters y Getters ****************************/
	
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
	
	public void setCambioBrusco(double cambioBrusco) {
		if(cambioBrusco > 100) cambioBrusco = 100;
		if(cambioBrusco < 0) cambioBrusco = 0;
		this.cambioBrusco = cambioBrusco;
	}
	
	/**************************Métodos **********************************/
	
	private void medir(SensorInstalado si) throws SensorDescalibrado, CambioBruscoException {
		double ultima = si.sensor.getValorUltimaLectura();
		si.sensor.tomarMedicion();
		double actual = si.sensor.getValorUltimaLectura();
		if((ultima != 0) && Math.abs((actual-ultima)/ultima) > cambioBrusco) {
			throw new CambioBruscoException("Ha ocurrido un cambio brusco", si.sensor);
		}
	}
	
	public void tomarMediciones() throws SensorDescalibrado, CambioBruscoException {
		for(SensorInstalado si : sensores.values()) {
			medir(si);
		}
	}
	
	public void iniciarLecturasPeriodicas(long periodoSegundos, int maxLecturas) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		int[] count = {0};  // array para poder modificarlo desde la lambda

		scheduler.scheduleAtFixedRate(() -> {
			try {
				if (count[0] >= maxLecturas) {
					scheduler.shutdown();
					return;
				}
				tomarMediciones();
				count[0]++;
			} catch(SensorDescalibrado | CambioBruscoException e) {
				scheduler.shutdown();
				throw new RuntimeException(e);
			}
			
		}, 0, periodoSegundos, TimeUnit.SECONDS);
	}

	public void detenerLecturasPeriodicas() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}
	
	public void anadirSensor(Unidad ud, double offset, Estrategia estrategia, Conversor conv) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		Sensor sensor;
		if(ud instanceof UnidadHumedad) {
			sensor = new SensorHumedad((UnidadHumedad)ud, offset, estrategia, conv);
		} else if(ud instanceof UnidadTemperatura) {
			sensor = new SensorTemperatura((UnidadTemperatura)ud, offset, estrategia, conv);
		} else if(ud instanceof UnidadPresion) {
			sensor = new SensorPresion((UnidadPresion)ud, offset, estrategia, conv);
		} else {
			throw new IncompatibleUnitsException("Tipo de unidad sin sensor específico");
		}
		
		SensorInstalado si = new SensorInstalado(sensor);
		if (sensores.containsKey(si.sensor.getId())) {
			throw new DuplicatedSensorIdException("Ya existe un sensor con el mismo ID", sensores.get(si.sensor.getId()).sensor, si.sensor);
		}
		sensores.put(si.sensor.getId(), si);
	}
	
	public void anadirSensor(Unidad ud, double offset, Estrategia estrategia) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		anadirSensor(ud, offset, estrategia, Conversor.identidad(ud));
	}
	
	public void anadirSensor(Unidad ud, double offset, Conversor conv) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		anadirSensor(ud, offset, new EstrategiaAnterior(0, (ud.getMax()+ud.getMin())/2), conv);
	}
	
	public void anadirSensor(Unidad ud, double offset) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		anadirSensor(ud, offset, new EstrategiaAnterior(0, (ud.getMax()+ud.getMin())/2), Conversor.identidad(ud));
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Estación metereológica: "+ nombre);
		str.append(String.format("\nUbicación: %.4f, %.4f", longitud, latitud));
		str.append("\n----------------------------");
		str.append("\nSensores instalados: "+sensores.size());
		//str.append("\nUltima lectura: "+ultimaLectura);
		for(SensorInstalado sens : sensores.values()) {
			str.append("\n"+sens);
		}
		return str.toString();
	}
}
