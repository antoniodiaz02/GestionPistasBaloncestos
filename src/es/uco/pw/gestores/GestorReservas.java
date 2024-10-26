package es.uco.pw.gestores;

import es.uco.pw.data.Jugador;


import es.uco.pw.data.Pista;
import es.uco.pw.data.Pista.TamanoPista;
import es.uco.pw.data.Reserva;
import es.uco.pw.data.ReservaAdultos;
import es.uco.pw.data.ReservaFamiliar;
import es.uco.pw.data.ReservaInfantil;
import es.uco.pw.factory.ReservaBonoFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestorReservas {
	
    private final String rutaArchivoJugadores = "src/es/uco/pw/files/users.txt";
    private final String rutaArchivoPistas = "src/es/uco/pw/files/pistas.txt";
    private final String rutaArchivoReservas = "src/es/uco/pw/files/reservas.txt";
    private final String rutaArchivoBonos = "src/es/uco/pw/files/bonos.txt";

    public GestorReservas() {
        // Constructor vacío si no tenemos nada que inicializar
    }
    
    public boolean hacerNuevoBono(String correoUsuario, TamanoPista tamano){
    	Jugador jugador = buscarJugador(correoUsuario);
    	if (jugador == null) return false;
    	
        // Valor por defecto para las sesiones de un bono nuevo
        int sesiones = 5;

        // La fecha de la primera sesión será un valor vacío
        String fechaPrimeraSesion = "";
        
        // Generar identificador único para el bono
        String bonoId = generarIdentificadorUnico();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoBonos, true))) {
            writer.write(bonoId + ";" + correoUsuario + ";" + tamano.toString() + ";" + sesiones + ";" + fechaPrimeraSesion);
            writer.newLine();  // Añadir una nueva línea al final
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Si todo fue bien, retornamos true
        return true;
    	
    }
    
    // Función para generar el identificador único del bono
    private String generarIdentificadorUnico() {
        String ultimoId = "B_0000";  // Identificador inicial si el archivo está vacío o no existe
        String patron = "B_(\\d{4})"; // Expresión regular para identificar el formato B_XXXX

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoBonos))) {
            String linea;
            String ultimoBono = null;

            while ((linea = reader.readLine()) != null) {
                ultimoBono = linea;
            }

            if (ultimoBono != null) {

            	String[] partes = ultimoBono.split(";");
                String idBonoActual = partes[0];

               
                Pattern pattern = Pattern.compile(patron);
                Matcher matcher = pattern.matcher(idBonoActual);

                if (matcher.matches()) {
                    // Extraer el número actual y convertirlo a entero
                    int numero = Integer.parseInt(matcher.group(1));
                    // Incrementar en 1 el número y formatearlo como B_XXXX
                    ultimoId = String.format("B_%04d", numero + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ultimoId;
    }
	
    public boolean hacerReservaIndividual(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends Reserva> tipoReserva) {
        Jugador jugador = buscarJugador(correoUsuario);
        Pista pista = buscarPista(nombrePista);

        if (jugador == null || pista == null || !pista.isDisponible()) return false;

        Reserva reserva = null;
        float precio = calcularPrecio(duracion);
        float descuento = jugador.calcularAntiguedad() > 2 ? precio * 0.1f : 0;

        // Crear la instancia de la reserva de acuerdo con el tipo
        if (tipoReserva == ReservaInfantil.class && pista.getTamanoPista() == Pista.TamanoPista.MINIBASKET) {
            reserva = new ReservaInfantil(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroNinos);
        } else if (tipoReserva == ReservaFamiliar.class && (pista.getTamanoPista() == Pista.TamanoPista.MINIBASKET || pista.getTamanoPista() == Pista.TamanoPista.TRES_VS_TRES)) {
            reserva = new ReservaFamiliar(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos, numeroNinos);
        } else if (tipoReserva == ReservaAdultos.class) {
            reserva = new ReservaAdultos(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos);
        }

        if (reserva != null) {
            guardarReservaEnArchivo(reserva);
            pista.setDisponible(false);
            actualizarPistaEnArchivo(pista);
            return true;
        }
        return false;
    }
    
    public boolean hacerReservaBono(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends Reserva> tipoReserva, String bonoId, int sesion) {
    	Jugador jugador = buscarJugador(correoUsuario);
        Pista pista = buscarPista(nombrePista);

        if (jugador == null || pista == null || !pista.isDisponible()) return false;
        
        // 1. Comprobacion del bono para poder hacer reservas. 
        if(!comprobarBono(bonoId)) return false;
        
        // 2. Realización de la reserva.
        Reserva reserva = null;
        ReservaBonoFactory reservaBono = new ReservaBonoFactory(bonoId, sesion);
    	
        float precio = calcularPrecio(duracion);
        float descuento = 0.05f;
        
        // Crear la instancia de la reserva de acuerdo con el tipo
        if (tipoReserva == ReservaInfantil.class && pista.getTamanoPista() == Pista.TamanoPista.MINIBASKET) {
        	reserva= reservaBono.createReservaInfantil(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroNinos);
        } else if (tipoReserva == ReservaFamiliar.class && (pista.getTamanoPista() == Pista.TamanoPista.MINIBASKET || pista.getTamanoPista() == Pista.TamanoPista.TRES_VS_TRES)) {
            reserva= reservaBono.createReservaFamiliar(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos, numeroNinos);
        } else if (tipoReserva == ReservaAdultos.class) {
            reserva= reservaBono.createReservaAdultos(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos);
        }
        
        
        // 3. Modificacion del numero de reservas de bono y adición de la fecha si es su primera reserva.
        if (reserva != null) {                
            guardarReservaEnArchivo(reserva);
            pista.setDisponible(false);
            actualizarPistaEnArchivo(pista);
            return actualizarSesionesBono(bonoId);
        }
        return false;
        
    }	
    
    private void actualizarPistaEnArchivo(Pista pista) {
        // Método para actualizar una pista en el archivo (implementación puede variar)
    }
    
    private float calcularPrecio(int duracion) {
        switch (duracion) {
            case 60:
                return 20.0f;
            case 90:
                return 30.0f;
            case 120:
                return 40.0f;
            default:
                throw new IllegalArgumentException("Duración no permitida. Use 60, 90 o 120 minutos.");
        }
    }

    private Jugador buscarJugador(String correoElectronico) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoJugadores))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].equals(correoElectronico)) {
                    // Ajustamos la conversión de fecha
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Ajusta el formato según cómo esté almacenada la fecha en el archivo
                    Date fechaNacimiento;
                    
                    try {
                        fechaNacimiento = sdf.parse(datos[1]);  // Convertimos la fecha de nacimiento a Date
                    } catch (ParseException e) {
                        System.out.println("Error al parsear la fecha de nacimiento: " + e.getMessage());
                        return null;
                    }
                    
                    // Creamos el objeto Jugador con los datos adaptados
                    return new Jugador(datos[2], fechaNacimiento, datos[0]); // nombreCompleto, fechaNacimiento, correoElectronico
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar jugador: " + e.getMessage());
        }
        return null;
    }

    private Pista buscarPista(String nombre) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoPistas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].equals(nombre)) {
                    return new Pista(datos[0], Boolean.parseBoolean(datos[2]), Boolean.parseBoolean(datos[3]), Pista.TamanoPista.valueOf(datos[1]), Integer.parseInt(datos[4]));

                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar pista: " + e.getMessage());
        }
        return null;
    }
    
    private void guardarReservaEnArchivo(Reserva reserva) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoReservas, true))) {
            String tipoReserva;
            if (reserva instanceof ReservaInfantil) {
                tipoReserva = "INFANTIL";
            } else if (reserva instanceof ReservaFamiliar) {
                tipoReserva = "FAMILIAR";
            } else if (reserva instanceof ReservaAdultos) {
                tipoReserva = "ADULTOS";
            } else {
                throw new IllegalArgumentException("Tipo de reserva desconocido");
            }

            String linea = tipoReserva + ";" + reserva.getUsuarioId() + ";" + reserva.getPistaId() + ";" 
                           + reserva.getFechaHora().getTime() + ";" + reserva.getDuracion() + ";" 
                           + reserva.getPrecio() + ";" + reserva.getDescuento() + ";" 
                           + ((reserva instanceof ReservaInfantil) ? ((ReservaInfantil) reserva).getNumNinos()
                           : (reserva instanceof ReservaFamiliar) ? ((ReservaFamiliar) reserva).getNumNinos() + ((ReservaFamiliar) reserva).getNumAdultos()
                           : ((ReservaAdultos) reserva).getNumAdultos());

            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar reserva en el archivo: " + e.getMessage());
        }
    }
    
    public boolean comprobarBono(String bonoId) {
    	File archivoBonos = new File(rutaArchivoBonos);
    	
    	boolean bonoFound = false;
    	int sesiones = 0;
    	Date fechaBono = null;
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(archivoBonos))) {
    		String line;
    		
    		while ((line = reader.readLine()) != null) {
    			String[] fields = line.split(";");
    			if (fields.length >= 4 && fields[0].equals(bonoId)) {
    				bonoFound = true;
    				sesiones = Integer.parseInt(fields[3].trim());
    				
    				//Si hay fecha se guarda
    				if (fields.length >= 5) {
    					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    					fechaBono = sdf.parse(fields[4].trim());
    				}
    				break;
    			}
    			
    		}
    	} catch (IOException | ParseException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	// Comprobar que el bono existe, y tiene sesiones disponibles.
    	if (!bonoFound || sesiones <= 0) return false;
    	
    	// Verificar que la fecha del bono no exceda en un año la fecha actual
    	if (fechaBono != null) {
    	    Calendar cal = Calendar.getInstance();
    	    cal.setTime(fechaBono);
    	    cal.add(Calendar.YEAR, 1);
    	    if (new Date().after(cal.getTime())) return false;
    	}
    	
    	return true; // El bono es válido
    }

	public boolean actualizarSesionesBono(String bonoId) {
	    File archivoBonos = new File(rutaArchivoBonos);
	    File tempFile = new File(archivoBonos.getAbsolutePath() + ".tmp");
	
	    try (BufferedReader reader = new BufferedReader(new FileReader(archivoBonos));
	         PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
	
	        String line;
	        boolean bonoFound = false;
	
	        while ((line = reader.readLine()) != null) {
	            String[] fields = line.split(";");
	            if (fields.length >= 4 && fields[0].equals(bonoId)) {
	                // Se encontró el bono
	                int sesiones = Integer.parseInt(fields[3].trim());
	                bonoFound = true;
	
	                // Verificar si es la primera reserva que realiza el bono.
	                if (sesiones == 5) {
	                	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	                    String fechaActual = sdf.format(new Date());
	                    writer.println(fields[0] + ";" + fields[1] + ";" + fields[2] + ";" + (sesiones - 1) + ";" + fechaActual);
	                } else {
	                    // Decrementar el número de sesiones
	                    writer.println(fields[0] + ";" + fields[1] + ";" + fields[2] + ";" + (sesiones - 1) + ";" + fields[4]);
	                }
	            } else {
	                // Escribir la línea original si no es la del bono
	                writer.println(line);
	            }
	        }
	
	        if (bonoFound) {
	            // Renombrar el archivo original y el temporal
	            archivoBonos.delete();
	            tempFile.renameTo(archivoBonos);
	        } else {
	            // Si no se encontró el bono, eliminar el archivo temporal
	            tempFile.delete();
	        }
	
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false; // Manejo de errores
	    }
	
	    return true; // Indica que la operación fue exitosa
	}
	
}


