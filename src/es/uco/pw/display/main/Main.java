package es.uco.pw.display.main;

import es.uco.pw.data.Jugador;
import es.uco.pw.gestores.GestorUsuarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorUsuarios gestor = new GestorUsuarios();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        boolean salir = false;

        while (!salir) {
            System.out.println("---- Menú Gestión de Usuarios ----");
            System.out.println("1. Insertar nuevo usuario");
            System.out.println("2. Listar usuarios");
            System.out.println("3. Modificar usuario");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    // Insertar usuario
                    try {
                        System.out.println("Introduce los datos del nuevo usuario:");
                        System.out.print("Nombre completo: ");
                        String nombre = scanner.nextLine();

                        System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
                        String fechaStr = scanner.nextLine();
                        Date fechaNacimiento = sdf.parse(fechaStr);

                        System.out.print("Correo electrónico: ");
                        String correo = scanner.nextLine();

                        Jugador nuevoJugador = new Jugador(nombre, fechaNacimiento, correo);
                        int codigo = gestor.insertarUsuario(nuevoJugador);

                        if (codigo == 1) {
                            System.out.println("Usuario añadido correctamente.");
                        } else if (codigo == -2) {
                            System.out.println("El usuario ya está registrado.");
                        } else {
                            System.out.println("Error al añadir el usuario.");
                        }
                    } catch (ParseException e) {
                        System.out.println("Formato de fecha incorrecto.");
                    }
                    break;

                case 2:
                    // Listar usuarios
                    int resultadoListar = gestor.listarUsuarios();
                    if (resultadoListar == -1) {
                        System.out.println("No hay usuarios registrados.");
                    } else if (resultadoListar == -2) {
                        System.out.println("Error al listar usuarios.");
                    }
                    break;

                case 3:
                    // Modificar usuario
                    try {
                        System.out.print("Introduce el correo del usuario a modificar: ");
                        String correoModificar = scanner.nextLine();
                        Jugador jugadorExistente = gestor.buscarUsuarioPorCorreo(correoModificar);

                        if (jugadorExistente == null) {
                            System.out.println("Usuario no encontrado.");
                            break;
                        }

                        System.out.println("¿Qué deseas modificar?");
                        System.out.println("1. Nombre completo");
                        System.out.println("2. Fecha de nacimiento");
                        System.out.println("3. Correo electrónico");
                        System.out.print("Elige una opción: ");
                        int opcionModificar = scanner.nextInt();
                        scanner.nextLine(); // Consumir el salto de línea

                        switch (opcionModificar) {
                            case 1:
                                // Modificar nombre
                                System.out.print("Introduce el nuevo nombre completo: ");
                                String nuevoNombre = scanner.nextLine();
                                jugadorExistente.setNombreCompleto(nuevoNombre);
                                break;

                            case 2:
                                // Modificar fecha de nacimiento
                                System.out.print("Introduce la nueva fecha de nacimiento (dd/MM/yyyy): ");
                                String nuevaFechaStr = scanner.nextLine();
                                Date nuevaFecha = sdf.parse(nuevaFechaStr);
                                jugadorExistente.setFechaNacimiento(nuevaFecha);
                                break;

                            case 3:
                                // Modificar correo electrónico
                                System.out.print("Introduce el nuevo correo electrónico: ");
                                String nuevoCorreo = scanner.nextLine();
                                jugadorExistente.setCorreoElectronico(nuevoCorreo);
                                break;

                            default:
                                System.out.println("Opción no válida.");
                                continue;
                        }

                        // Llamar al método de modificación
                        int resultadoModificar = gestor.modificarUsuario(correoModificar, jugadorExistente);
                        if (resultadoModificar == 1) {
                            System.out.println("Usuario modificado correctamente.");
                        } else if (resultadoModificar == 0) {
                            System.out.println("Usuario no encontrado.");
                        } else {
                            System.out.println("Error al modificar el usuario.");
                        }
                    } catch (ParseException e) {
                        System.out.println("Formato de fecha incorrecto.");
                    } catch (IOException e) {
                        System.out.println("Error al modificar el archivo.");
                    }
                    break;

                case 4:
                    // Salir
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println(); // Salto de línea para separar las acciones
        }

        scanner.close();
    }
}