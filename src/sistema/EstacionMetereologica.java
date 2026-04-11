package sistema;

import sensores.*;
import unidades.*;
import conversores.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.*;
import excepciones.*;
import sensores.estrategias.*;
import visualizacion.*;

/**
 * Clase Class EstacionMetereologica.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstacionMetereologica implements IDocumento {
	
	/** scheduler para ejecutar las mediciones periódicas. */
	private ScheduledExecutorService scheduler;
	
	/** Periodo en segundos de las mediciones periódicas. */
	private long periodoSegundos;
	
	/** Máximo de lecturas de las mediciones actuales. */
	private int maxLecturas;
	
	/** Número de lecturas realizadas en las mediciones actuales. */
	private int lecturasRealizadas;
	
	/**Indica si hay lecturas periódicas activas en el momento. */
	private boolean lecturasPeriodicasActivas = false;
	
	/** Nombre de la estación. */
	private String nombre;
	
	/** Longitud de la estación. */
	private double longitud;
	
	/** Latitud de la estación. */
	private double latitud;
	
	/** Porcentaje de diferencia a partir del cual se considera cambio brusco una medición. */
	private double cambioBrusco;
	
	/** Mapa de sensores instalados en la estación. */
	private Map<String, SensorInstalado> sensores = new HashMap<String, SensorInstalado>();
	
	/** Mapa de alertas activas en la estación. */
	private Map<Sensor, ArrayList<AlertaSensor>> alertas = new HashMap<Sensor, ArrayList<AlertaSensor>>();
	 
	/**
	 * Clase SensorInstalado.
	 *
	 * @author Juan Ibáñez y Tiago Oselka
	 * @version 1.0
	 */
	private class SensorInstalado {
		
		/** sensor. */
		private Sensor sensor;
		
		/** fecha de instalacion. */
		private LocalDate fechaInstalacion;
		
		/**
		 * Inicializa un nuevo objeto de la clase sensor instalado.
		 *
		 * @param sensor Sensor
		 */
		public SensorInstalado(Sensor sensor) {
			this.sensor = sensor;
			this.fechaInstalacion = LocalDate.now();
		}
		
		/**
		 * To string.
		 *
		 * @return String formateado imprimiendo la información completa de un sensor de la estación
		 */
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

		@SuppressWarnings("unused")
		public LocalDate getFechaInstalacion() {
			return fechaInstalacion;
		}
	}
	
	/**
	 * Inicializa un nuevo objeto de la clase estacion metereologica.
	 *
	 * @param nombre Nombre de la estación
	 * @param longitud longitud de la estación
	 * @param latitud latitud de la estación
	 * @param cambioBrusco Porcentaje mínimo que se considera cambio brusco
	 */
	public EstacionMetereologica(String nombre, double longitud, double latitud, double cambioBrusco) {
		this.nombre = nombre;
		this.longitud = longitud;
		this.latitud = latitud;
		if(cambioBrusco > 100) cambioBrusco = 100;
		if(cambioBrusco < 0) cambioBrusco = 0;
		this.cambioBrusco = cambioBrusco;
	}
	
	/**
	 * Inicializa un nuevo objeto de la clase estacion metereologica.
	 *
	 * @param nombre Nombre de la estación
	 * @param longitud longitud de la estación
	 * @param latitud latitud de la estación
	 */
	public EstacionMetereologica(String nombre, double longitud, double latitud) {
		this.nombre = nombre;
		this.longitud = longitud;
		this.latitud = latitud;
		this.cambioBrusco = 50.0;
	}
	
	/************************* Setters y Getters ***************************/
	
	/**
	 * Getter del nombre.
	 * 
	 * @return nombre Nombre de la estación
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Getter de longitud.
	 *
	 * @return longitud Longitud de la estación
	 */
	public double getLongitud() {
		return longitud;
	}
	
	/**
	 * Getter de latitud.
	 *
	 * @return latitud Latitud de la estación
	 */
	public double getLatitud() {
		return latitud;
	}
	
	/**
	 * Getter de sensores.
	 *
	 * @return sensores Sensores de la estación
	 */
	public List<Sensor> getSensores() {
		return sensores.values().stream().map(si -> si.sensor).toList();
	}
	
	/**
	 * Getter de sensor por id.
	 *
	 * @param id ID del sensor
	 * @return sensor Sensor con la id
	 */
	public Sensor getSensor(String id) {
		if(sensores.containsKey(id))
			return sensores.get(id).sensor;
		else return null;
	}
	
	/**
	 * Getter de sensores por tipo.
	 *
	 * @param <T> tipo genérico
	 * @param tipo Tipo de sensor
	 * @return sensores por tipo
	 */
	public <T extends Sensor> List<T> getSensoresPorTipo(Class<T> tipo) {
		return sensores.values().stream()
	    		.map(si -> si.sensor)
	    		.filter(s -> tipo.isInstance(s))
	    		.map(s -> tipo.cast(s))
	    		.collect(Collectors.toList());
	}
	
	/**
	 * Setter de cambio brusco.
	 *
	 * @param cambioBrusco nuevo cambio brusco
	 */
	public void setCambioBrusco(double cambioBrusco) {
		if(cambioBrusco > 100) cambioBrusco = 100;
		if(cambioBrusco < 0) cambioBrusco = 0;
		this.cambioBrusco = cambioBrusco;
	}
	
	/**
	 * Getter de fecha ultima lectura.
	 *
	 * @return fecha ultima lectura
	 */
	public LocalDateTime getFechaUltimaLectura() {
		if(sensores.isEmpty()) return null;
		SensorInstalado min = (SensorInstalado)sensores.values().toArray()[0];
		for(SensorInstalado si : sensores.values()) {
			if(si.sensor.getFechaUltimaLectura().isAfter(min.sensor.getFechaUltimaLectura()))
				min = si;
		}
		return min.sensor.getFechaUltimaLectura();
	}
	
	/**
	 * Getter de alertas.
	 *
	 * @return alertas
	 */
	public List<AlertaSensor> getAlertas(){
		ArrayList<AlertaSensor> lista = new ArrayList<AlertaSensor>();
		for(ArrayList<AlertaSensor> l : alertas.values()) {
			lista.addAll(l);
		}
		return lista;
	}
	
	/**************************Métodos *********************************/
	
	
	/**
	 * Método para realizar la acción de medir con un sensor
	 * 
	 * @param si Sensor instalado en la estación
	 */
	private void medir(SensorInstalado si) {
		if(alertas.containsKey(si.sensor) && alertas.get(si.sensor).stream().anyMatch(a -> a instanceof SensorDescalibrado)) {
			return;
		}
		
		double ultima = si.sensor.getValorUltimaLectura();
		try {
			si.sensor.tomarMedicion();
		} catch(SensorDescalibrado e) {
			if(!alertas.containsKey(e.getSensor())) {
				alertas.put(e.getSensor(), new ArrayList<AlertaSensor>());
			}
			alertas.get(e.getSensor()).add(e);
			
			return;
		}
		double actual = si.sensor.getValorUltimaLectura();
		
		if((ultima != 0) && Math.abs((actual-ultima)/ultima)*100.0 > cambioBrusco) {
			AlertaSensor e = new CambioBruscoException(si.sensor, actual, ultima);
			if(!alertas.containsKey(e.getSensor())) {
				alertas.put(e.getSensor(), new ArrayList<AlertaSensor>());
			}
			alertas.get(e.getSensor()).add(e);
		} else {
			/*Limpiamos las alertas de cambio brusco asociadas a este vector si no ha ocurrido ahora*/
			limpiarAlertasCambioBrusco(si.sensor);
		}
	}
	
	/**
	 * Tomar mediciones con todos los sensores de la estación.
	 */
	public void tomarMediciones() {
		for(SensorInstalado si : sensores.values()) {
			medir(si);
		}
	}
	
	/**
	 * Iniciar lecturas periodicas.
	 *
	 * @param periodoSegundos Periodo en segundos
	 * @param maxLecturas Maximas lecturas realizadas
	 */
	public void iniciarLecturasPeriodicas(long periodoSegundos, int maxLecturas) {
		this.scheduler = Executors.newSingleThreadScheduledExecutor();
		this.lecturasPeriodicasActivas = true;
		this.periodoSegundos = periodoSegundos;
		this.maxLecturas = maxLecturas;
		this.lecturasRealizadas = 0;
		scheduler.scheduleAtFixedRate(() -> {
			if (this.lecturasRealizadas >= maxLecturas) {
				scheduler.shutdown();
				return;
			}
			tomarMediciones();
			this.lecturasRealizadas++;
		}, 0, periodoSegundos, TimeUnit.SECONDS);
	}

	/**
	 * Detener lecturas periodicas.
	 */
	public void detenerLecturasPeriodicas() {
		this.lecturasPeriodicasActivas = false;
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}
	
	/**
	 * Anadir sensor.
	 *
	 * @param ud Unidades de entrada del sensor
	 * @param offset Offset inicial del sensor
	 * @param estrategia Estretegia del sensor
	 * @param conv Conversor del sensor
	 * @throws IncompatibleUnitsException si el sensor tiene un tipo de unidad desconocido
	 * @throws DuplicatedSensorIdException si ya existe un sensor con la misma id
	 */
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
	
	/**
	 * Anadir sensor.
	 *
	 * @param ud Unidades de entrada del sensor
	 * @param offset Offset inicial del sensor
	 * @param estrategia Estretegia del sensor
	 * @param conv Conversor del sensor
	 * @throws IncompatibleUnitsException si el sensor tiene un tipo de unidad desconocido
	 * @throws DuplicatedSensorIdException si ya existe un sensor con la misma id
	 */
	public void anadirSensor(Unidad ud, double offset, Estrategia estrategia) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		anadirSensor(ud, offset, estrategia, Conversor.identidad(ud));
	}
	
	/**
	 * Anadir sensor.
	 *
	 * @param ud Unidades de entrada del sensor
	 * @param offset Offset inicial del sensor
	 * @param conv Conversor del sensor
	 * @throws IncompatibleUnitsException si el sensor tiene un tipo de unidad desconocido
	 * @throws DuplicatedSensorIdException si ya existe un sensor con la misma id
	 */
	public void anadirSensor(Unidad ud, double offset, Conversor conv) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		anadirSensor(ud, offset, new EstrategiaAnterior(0, (ud.getMax()+ud.getMin())/2), conv);
	}
	
	/**
	 * Anadir sensor.
	 *
	 * @param ud Unidades de entrada del sensor
	 * @param offset Offset inicial del sensor
	 * @throws IncompatibleUnitsException si el sensor tiene un tipo de unidad desconocido
	 * @throws DuplicatedSensorIdException si ya existe un sensor con la misma id
	 */
	public void anadirSensor(Unidad ud, double offset) throws IncompatibleUnitsException, DuplicatedSensorIdException {
		anadirSensor(ud, offset, new EstrategiaAnterior(0, (ud.getMax()+ud.getMin())/2), Conversor.identidad(ud));
	}
	
	/**
	 * Limpiar alertas por descalibrado sobre un sensor
	 *
	 * @param sensor Sensor del que se limpian las alertas
	 */
	private void limpiarAlertasDescalibrado(Sensor sensor) {
		if(alertas.containsKey(sensor))
			alertas.get(sensor).removeIf(a -> a instanceof SensorDescalibrado);
	}
	
	/**
	 * Limpiar alertas por cambio brusco sobre un sensor
	 *
	 * @param sensor Sensor del que se limpian las alertas
	 */
	private void limpiarAlertasCambioBrusco(Sensor sensor) {
		if(alertas.containsKey(sensor))
			alertas.get(sensor).removeIf(a -> a instanceof CambioBruscoException);
	}
	
	/**
	 * Calibrar sensor (método auxiliar).
	 *
	 * @param id ID del sensor a calibrar
	 * @param accion Acción que se realiza (calibrado) sobre el sensor
	 */
	private void calibrarSensorInterno(String id, Consumer<Sensor> accion) {
	    if(!sensores.containsKey(id)) return;
	    boolean retomar = false;
	    if(lecturasPeriodicasActivas) {
	    	retomar = true;
	    	detenerLecturasPeriodicas();
	    }
	    
	    Sensor s = sensores.get(id).sensor;
	    accion.accept(s);
	    
	    limpiarAlertasDescalibrado(s);
	    
	    if(retomar) {
	    	iniciarLecturasPeriodicas(periodoSegundos, maxLecturas - lecturasRealizadas);
	    }
	}
	
	/**
	 * Calibrar sensor.
	 *
	 * @param id ID del sensor a calibrar
	 * @param offset Nuevo offset del sensor tras el calibrado
	 * @param duracionCalibrado Duracion del calibrado hasta que caduque
	 */
	public void calibrarSensor(String id, double offset, Duration duracionCalibrado) {
	    calibrarSensorInterno(id, s -> s.calibrar(offset, duracionCalibrado));
	}

	/**
	 * Calibrar sensor.
	 *
	 * @param id ID del sensor a calibrar
	 * @param duracionCalibrado Duracion del calibrado hasta que caduque
	 */
	public void calibrarSensor(String id, Duration duracionCalibrado) {
	    calibrarSensorInterno(id, s -> s.calibrar(duracionCalibrado));
	}

	/**
	 * Calibrar sensor.
	 *
	 * @param id ID del sensor a calibrar
	 * @param offset Nuevo offset del sensor tras el calibrado
	 */
	public void calibrarSensor(String id, double offset) {
	    calibrarSensorInterno(id, s -> s.calibrar(offset));
	}

	/**
	 * Calibrar sensor.
	 *
	 * @param id ID del sensor a calibrar
	 */
	public void calibrarSensor(String id) {
	    calibrarSensorInterno(id, s -> s.calibrar());
	}
	
	/**
	 * To string de la estación metereológica.
	 *
	 * @return String con la información de la estación
	 */
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

	/***************************** Métodos de visualizacion *************************************/
	 
	/**
	 * Método para obtener el título del documento que se genera sobre la estación
	 * 
	 * @return titulo del documento soobre la estacion
	 */
	@Override
	public String getTitulo() {
		return "Estacion Metereológica: "+nombre;
	}

	/**
	 * Getter del header de la seccion principal del documento de la estacion
	 *
	 * @return header de la seccion principal
	 */
	@Override
	public String getHeaderSeccionPrincipal() {
		return nombre;
	}

	/**
	 * Getter de los parrafos del documento de la estacion
	 *
	 * @return parrafos del documento
	 */
	@Override
	public String[] getParrafos() {
		ArrayList<String> parrafos = new ArrayList<String>();
		parrafos.add("Ubicacion: "+longitud+", "+latitud);
		parrafos.add("Sensores instalados: "+this.sensores.size());
		LocalDateTime ultima = getFechaUltimaLectura();
		parrafos.add("Última lectura: "+(ultima != null ? ultima : "Sin lecturas"));
		
		return parrafos.toArray(new String[0]);
	}

	/**
	 * Getter de listas para el documento sobre la estacion
	 *
	 * @return listas que se mostrarán en el documento
	 */
	@Override
	public ListaConTitulo[] getListas() {
		ArrayList<ListaConTitulo> listas = new ArrayList<ListaConTitulo>();
		listas.add(new ListaConTitulo("Sensores Activos", new ArrayList<Object>(sensores.values())));
		List<AlertaSensor> listaAlertas = getAlertas();
		listas.add(new ListaConTitulo("Alertas Activas: "+listaAlertas.size(), new ArrayList<Object>(listaAlertas)));
		return listas.toArray(new ListaConTitulo[0]);
	}
}
