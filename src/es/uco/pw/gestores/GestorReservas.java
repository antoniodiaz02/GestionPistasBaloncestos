package es.uco.pw.gestores;

import es.uco.pw.data.Jugador;

import es.uco.pw.data.Pista;
import es.uco.pw.data.Reserva;
import es.uco.pw.data.ReservaAdultos;
import es.uco.pw.data.ReservaFamiliar;
import es.uco.pw.data.ReservaInfantil;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.Date;

public class GestorReservas {
	
    private final String rutaArchivoJugadores = "src/es/uco/pw/files/users.txt";
    private final String rutaArchivoPistas = "src/es/uco/pw/files/pistas.txt";
    private final String rutaArchivoReservas = "src/es/uco/pw/files/reservas.txt";

    public GestorReservas() {
        // Constructor vacío si no tenemos nada que inicializar
    }
	
    public boolean hacerReservaIndividual(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numParticipantes, int numeroAdultos, int numeroNinos, Class<? extends Reserva> tipoReserva) {
        Jugador jugador = buscarJugador(correoUsuario);
        Pista pista = buscarPista(nombrePista);

        if (jugador == null || pista == null || !pista.isDisponible()) return false;

        Reserva reserva = null;
        float precio = calcularPrecio(duracion);
        float descuento = jugador.calcularAntiguedad() > 2 ? precio * 0.1f : 0;

        // Crear la instancia de la reserva de acuerdo con el tipo
        if (tipoReserva == ReservaInfantil.class && pista.getTamanoPista() == Pista.TamanoPista.MINIBASKET) {
            reserva = new ReservaInfantil(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numParticipantes);
        } else if (tipoReserva == ReservaFamiliar.class && (pista.getTamanoPista() == Pista.TamanoPista.MINIBASKET || pista.getTamanoPista() == Pista.TamanoPista.TRES_VS_TRES)) {
            reserva = new ReservaFamiliar(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numeroAdultos, numeroNinos);
        } else if (tipoReserva == ReservaAdultos.class) {
            reserva = new ReservaAdultos(correoUsuario, fechaHora, duracion, nombrePista, precio, descuento, numParticipantes);
        }

        if (reserva != null) {
            guardarReservaEnArchivo(reserva);
            pista.setDisponible(false);
            actualizarPistaEnArchivo(pista);
            return true;
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
                String[] datos = linea.split(",");
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
                String[] datos = linea.split(",");
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

            String linea = tipoReserva + "," + reserva.getUsuarioId() + "," + reserva.getPistaId() + "," 
                           + reserva.getFechaHora().getTime() + "," + reserva.getDuracion() + "," 
                           + reserva.getPrecio() + "," + reserva.getDescuento() + "," 
                           + ((reserva instanceof ReservaInfantil) ? ((ReservaInfantil) reserva).getNumNinos()
                           : (reserva instanceof ReservaFamiliar) ? ((ReservaFamiliar) reserva).getNumNinos() + ((ReservaFamiliar) reserva).getNumAdultos()
                           : ((ReservaAdultos) reserva).getNumAdultos());

            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar reserva en el archivo: " + e.getMessage());
        }
    }
}


