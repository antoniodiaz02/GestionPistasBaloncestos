package es.uco.pw.display.main;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.Gestores.GestorUsuarios;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;

/**
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
* Menu principal para gestionar usuarios.
*  */
public class MenuUsuarios {
    
    private GestorUsuarios gestor;
    private SimpleDateFormat sdf;
    private Scanner scanner;

    /**
    * Constructor de la clase MenuUsuarios
    *  */
    public MenuUsuarios() {
        this.gestor = new GestorUsuarios();
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra el Menú usuario.
     */
    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n---- Menú Gestión de Usuarios ----");
            System.out.println("  1. Insertar nuevo usuario");
            System.out.println("  2. Listar usuarios");
            System.out.println("  3. Modificar usuario");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    insertarUsuario();
                    break;

                case 2:
                    listarUsuarios();
                    break;

                case 3:
                    modificarUsuario();
                    break;

                case 4:
                    salir = true;
                    System.out.println("Saliendo del menú...");
                    break;

                default:
                    System.out.println(" ERROR! Opción no válida. Por favor intente de nuevo.");
            }

            System.out.println(); // Salto de línea para separar las acciones
        }
    }

    /**
     * Inserta un usuario al fichero
     * @throws ParseException Si ocurre un error en la fecha
     */
    private void insertarUsuario() {
        try {
            System.out.println("Introduce los datos del nuevo usuario:");
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine();

            System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();
            Date fechaNacimiento = sdf.parse(fechaStr);

            System.out.print("Correo electrónico: ");
            String correo = scanner.nextLine();

            JugadorDTO nuevoJugador = new JugadorDTO(nombre, fechaNacimiento, correo);

            int codigo = gestor.insertarUsuario(nuevoJugador);

            if (codigo == 1) {
                System.out.println("\n Usuario añadido correctamente.");
            } else if (codigo == -2) {
                System.out.println("\n ERROR! El usuario ya está registrado.");
            } else {
                System.out.println("\n ERROR! Error al añadir el usuario.");
            }
        } catch (ParseException e) {
            System.out.println("\n ERROR! Formato de fecha incorrecto.");
        }
    }



    /**
     * Lista los usuarios del fichero usuarios
     */
    private void listarUsuarios() {
        int resultadoListar = gestor.listarUsuarios();
        if (resultadoListar == -1) {
            System.out.println("\n ERROR! No hay usuarios registrados.");
        } else if (resultadoListar == -2) {
            System.out.println("\n ERROR! Error al listar usuarios.");
        }
    }

    /**
     * Modifica los datos del usuario a elegir.
     */
    private void modificarUsuario() {
        try {
            System.out.print("\n Introduce el correo del usuario a modificar: ");
            String correoModificar = scanner.nextLine();
            JugadorDTO jugadorExistente = gestor.buscarUsuarioPorCorreo(correoModificar);

            if (jugadorExistente == null) {
                System.out.println("\n ERROR! Usuario no encontrado.");
                return;
            }

            System.out.println("\n  ¿Qué deseas modificar?");
            System.out.println("    1. Nombre completo");
            System.out.println("    2. Fecha de nacimiento");
            System.out.println("    3. Correo electrónico");
            System.out.print("Elige una opción: ");
            int opcionModificar = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcionModificar) {
                case 1:
                    System.out.print("\n Introduce el nuevo nombre completo: ");
                    String nuevoNombre = scanner.nextLine();
                    jugadorExistente.setNombreCompleto(nuevoNombre);
                    break;

                case 2:
                    System.out.print("\n Introduce la nueva fecha de nacimiento (dd/MM/yyyy): ");
                    String nuevaFechaStr = scanner.nextLine();
                    Date nuevaFecha = sdf.parse(nuevaFechaStr);
                    jugadorExistente.setFechaNacimiento(nuevaFecha);
                    break;

                case 3:
                    System.out.print("\n Introduce el nuevo correo electrónico: ");
                    String nuevoCorreo = scanner.nextLine();
                    jugadorExistente.setCorreoElectronico(nuevoCorreo);
                    break;

                default:
                    System.out.println("\n ERROR! Opción no válida.");
                    return;
            }

            // Llamar al método de modificación
            int resultadoModificar = gestor.modificarUsuario(correoModificar, jugadorExistente);
            if (resultadoModificar == 1) {
                System.out.println("\n Usuario modificado correctamente.");
            } else if (resultadoModificar == 0) {
                System.out.println("\n Usuario no encontrado.");
            } else {
                System.out.println("\n ERROR! Error al modificar el usuario.");
            }
        } catch (ParseException e) {
            System.out.println("\n ERROR! Formato de fecha incorrecto.");
        } catch (IOException e) {
            System.out.println("\n ERROR! Error al modificar el archivo.");
        }
    }
}