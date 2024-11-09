package es.uco.pw.business.Gestores;

import java.io.FileReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import es.uco.pw.business.DTOs.JugadorDTO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Clase pricipal para gestionar usuarios.
 */
public class GestorUsuarios {
	
    /**
     * Lista que almacena los usuarios registrados.
     */
    private List<JugadorDTO> usuarios;

    /**
     * Constructor de la clase GestorUsuarios. Inicializa la lista de usuarios.
     */
    public GestorUsuarios() {
        usuarios = new ArrayList<>();
    }

	/**
	 * Añade un nuevo usuario
	 * @param newJugador Jugador a añadir
	 * @return codigo Codigo de salida
	 */
    public int insertarUsuario(JugadorDTO newJugador) {
        int codigo = 0;

        // Comprobar si el usuario ya existe mediante el correo electrónico
        JugadorDTO jugadorExistente = buscarUsuarioPorCorreo(newJugador.getCorreoElectronico());
        if (jugadorExistente != null) {
            codigo = -2;
            return codigo; // El usuario ya está registrado
        }

        try {
            // Añadir el nuevo jugador a la lista en memoria
            usuarios.add(newJugador);

            // Guardar el nuevo jugador en el archivo users.txt
            String rutaArchivo = "src/es/uco/pw/files/users.txt"; // Ruta al archivo

            // Abrimos el archivo en modo append para añadir al final
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true));

            // Escribimos la información del usuario en una nueva línea
            writer.write(newJugador.getNombreCompleto() + ";" + 
                         new SimpleDateFormat("dd/MM/yyyy").format(newJugador.getFechaNacimiento()) + ";" +
                         newJugador.getCorreoElectronico());
            writer.newLine(); // Añadir salto de línea
            writer.close(); // Cerrar el archivo

            codigo = 1; // Usuario añadido correctamente
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
            codigo = -1; // Error al añadir el usuario
        }

        return codigo;
    }
	
    /**
     * Modifica la información de un usuario existente identificado por su correo electrónico.
     * 
     * @param correoElectronico Correo del usuario a modificar
     * @param nuevoJugador Nuevos datos del jugador
     * @return 1 si se modifica correctamente, 0 si no se encuentra el usuario, -1 si ocurre un error
     * @throws IOException Si ocurre un error al guardar el archivo
     */
    public int modificarUsuario(String correoElectronico, JugadorDTO nuevoJugador) throws IOException {
        int codigo = 0;
        String rutaArchivo = "src/es/uco/pw/files/users.txt"; // Ruta al archivo
        List<String> lineas = new ArrayList<>();
        
        try {
            // Abrimos el archivo en modo lectura
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            boolean usuarioModificado = false;

            // Leemos cada línea del archivo
            while ((linea = reader.readLine()) != null) {
                // Cada línea tiene el formato: nombreCompleto, dd/MM/yyyy, correoElectronico
                String[] datos = linea.split(";");

                if (datos.length == 3) {
                    String correo = datos[2];

                    // Si encontramos el usuario, lo actualizamos
                    if (correo.equals(correoElectronico)) {
                        String nuevaLinea = nuevoJugador.getNombreCompleto() + ";" +
                                            new SimpleDateFormat("dd/MM/yyyy").format(nuevoJugador.getFechaNacimiento()) + ";" +
                                            nuevoJugador.getCorreoElectronico();
                        lineas.add(nuevaLinea);
                        usuarioModificado = true;
                    } else {
                        // Si no es el usuario, simplemente añadimos la línea tal como está
                        lineas.add(linea);
                    }
                }
            }

            reader.close(); // Cerramos el archivo de lectura

            if (!usuarioModificado) {
                return 0; // Usuario no encontrado
            }

            // Reescribimos todo el archivo con las líneas actualizadas
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, false)); // Sobrescribimos el archivo

            for (String nuevaLinea : lineas) {
                writer.write(nuevaLinea);
                writer.newLine();
            }

            writer.close(); // Cerramos el archivo de escritura
            codigo = 1; // Usuario modificado correctamente
        } catch (IOException e) {
            System.out.println("Error al modificar el archivo: " + e.getMessage());
            codigo = -1; // Error durante la modificación
        }

        return codigo;
    }
   
    /**
     * Lista todos los usuarios actualmente registrados en el archivo users.txt.
     * 
     * @return 1 si se listan correctamente, -1 si no hay usuarios registrados, -2 si ocurre un error
     */
    public int listarUsuarios() {
        int codigo = 0;
        String rutaArchivo = "src/es/uco/pw/files/users.txt"; // Ruta al archivo

        try {
            // Abrimos el archivo en modo lectura
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            boolean hayUsuarios = false;

            // Verificamos si el archivo contiene usuarios
            System.out.println("\n Usuarios registrados:");
            System.out.println("----------------------------");
            // Leemos cada línea del archivo
            while ((linea = reader.readLine()) != null) {
                // Cada línea tiene el formato: nombreCompleto, dd/MM/yyyy, correoElectronico
                String[] datos = linea.split(";");

                if (datos.length == 3) {
                    hayUsuarios = true; // Se encontró al menos un usuario

                    String nombreCompleto = datos[0];
                    String fechaNacimiento = datos[1];
                    String correoElectronico = datos[2];

                    // Mostramos la información del usuario
                    System.out.println("Nombre completo: " + nombreCompleto);
                    System.out.println("Fecha de nacimiento: " + fechaNacimiento);
                    System.out.println("Correo electrónico: " + correoElectronico);
                    System.out.println("----------------------------");
                }
            }

            reader.close(); // Cerramos el archivo

            if (!hayUsuarios) {
                codigo = -1; // No hay usuarios registrados
                System.out.println("No hay usuarios registrados.");
            } else {
                codigo = 1; // Usuarios listados correctamente
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            codigo = -2; // Error al intentar listar usuarios
        }

        return codigo;
    }
    
    /**
     * Busca un usuario por su correo electrónico en la lista de usuarios.
     * 
     * @param correoElectronico Correo del jugador a buscar
     * @return Jugador si se encuentra, null si no existe
     */
	public JugadorDTO buscarUsuarioPorCorreo(String correoElectronico) {
	    String rutaArchivo = "src/es/uco/pw/files/users.txt"; // Ruta al archivo

	    try {
	        // Abrimos el archivo en modo lectura
	        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
	        String linea;

	        // Leemos cada línea del archivo
	        while ((linea = reader.readLine()) != null) {
	            // Cada línea tiene el formato: nombreCompleto, dd/MM/yyyy, correoElectronico
	            String[] datos = linea.split(";");

	            if (datos.length == 3) {
	                String nombreCompleto = datos[0];
	                String fechaNacimientoStr = datos[1];
	                String correo = datos[2];

	                // Si el correo electrónico coincide
	                if (correo.equals(correoElectronico)) {
	                    // Convertir la fecha de nacimiento de String a Date
	                    Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimientoStr);

	                    // Crear y devolver el objeto Jugador
	                    JugadorDTO jugador = new JugadorDTO(nombreCompleto, fechaNacimiento, correo);
	                    reader.close();
	                    return jugador;
	                }
	            }
	        }

	        reader.close(); // Cerramos el archivo
	    } catch (IOException | ParseException e) {
	        System.out.println("Error al leer el archivo o procesar los datos: " + e.getMessage());
	    }
	    // Si no se encuentra el usuario
	    return null;
	}
}