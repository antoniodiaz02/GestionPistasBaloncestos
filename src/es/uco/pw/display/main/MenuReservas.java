package es.uco.pw.display.main;


import es.uco.pw.gestores.GestorReservas;
import es.uco.pw.data.Pista.TamanoPista;
import es.uco.pw.data.Reserva;
import es.uco.pw.data.ReservaAdultos;
import es.uco.pw.data.ReservaFamiliar;
import es.uco.pw.data.ReservaInfantil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;

/**
 * Menu principal para gestionar resevas.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

public class MenuReservas {
    private static final GestorReservas gestorReservas = new GestorReservas();
    private static final Scanner scanner = new Scanner(System.in); // Definir Scanner como estático

    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú de Gestión de Reservas ---");
            System.out.println("1. Hacer reserva individual");
            System.out.println("2. Hacer reserva con bono");
            System.out.println("3. Generar nuevo bono");
            System.out.println("4. Actualizar sesiones de bono");
            System.out.println("5. Verificar si el plazo de 24 horas ha sido excedido");
            System.out.println("6. Listar reservas futuras");
            System.out.println("7. Modificar una reserva");
            System.out.println("8. Listar reservas por fecha y pista");
            System.out.println("9. Cancelar una reserva");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer de entrada

            switch (opcion) {
                case 1:
                    hacerReservaIndividual(scanner);
                    break;
                case 2:
                    hacerReservaBono(scanner);
                    break;
                case 3:
                    hacerNuevoBono(scanner);
                    break;
                case 4:
                    actualizarSesionesBono();
                    break;
                case 5:
                    verificarPlazoExcedido();
                    break;
                case 6:
                    listarReservasFuturas();
                    break;
                case 7:
                    modificarReserva();
                    break;
                case 8:
                    listarReservasPorFechaYPista();
                    break;
                case 9:
                    cancelarReserva();
                    break;    
                case 0:
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }

        scanner.close();
    }

    private static void hacerReservaIndividual(Scanner scanner) {
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();
        System.out.print("Nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        System.out.print("Fecha y hora de la reserva (dd-MM-yyyy HH:mm): ");
        Date fechaHora = leerFechaHora(scanner);
        System.out.print("Duración (60, 90, 120): ");
        int duracion = scanner.nextInt();
        System.out.print("Número de adultos: ");
        int numeroAdultos = scanner.nextInt();
        System.out.print("Número de niños: ");
        int numeroNinos = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        System.out.println("Tipo de reserva: 1. Infantil, 2. Familiar, 3. Adultos");
        int tipo = scanner.nextInt();
        Class<? extends Reserva> tipoReserva;
        switch (tipo) {
            case 1 -> tipoReserva = ReservaInfantil.class;
            case 2 -> tipoReserva = ReservaFamiliar.class;
            case 3 -> tipoReserva = ReservaAdultos.class;
            default -> {
                System.out.println("Tipo no válido.");
                return;
            }
        }

        boolean resultado = gestorReservas.hacerReservaIndividual(correo, nombrePista, fechaHora, duracion, numeroAdultos, numeroNinos, tipoReserva);
        System.out.println(resultado ? "Reserva realizada con éxito." : "Error al realizar la reserva.");
    }

    private static void hacerReservaBono(Scanner scanner) {
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();
        System.out.print("Nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        System.out.print("Fecha y hora de la reserva (dd-MM-yyyy HH:mm): ");
        Date fechaHora = leerFechaHora(scanner);
        System.out.print("Duración (60, 90, 120): ");
        int duracion = scanner.nextInt();
        System.out.print("Número de adultos: ");
        int numeroAdultos = scanner.nextInt();
        System.out.print("Número de niños: ");
        int numeroNinos = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        System.out.print("ID del bono: ");
        String bonoId = scanner.nextLine();
        System.out.print("Sesiones restantes en el bono: ");
        int sesion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        System.out.println("Tipo de reserva: 1. Infantil, 2. Familiar, 3. Adultos");
        int tipo = scanner.nextInt();
        Class<? extends Reserva> tipoReserva;
        switch (tipo) {
            case 1 -> tipoReserva = ReservaInfantil.class;
            case 2 -> tipoReserva = ReservaFamiliar.class;
            case 3 -> tipoReserva = ReservaAdultos.class;
            default -> {
                System.out.println("Tipo no válido.");
                return;
            }
        }

        boolean resultado = gestorReservas.hacerReservaBono(correo, nombrePista, fechaHora, duracion, numeroAdultos, numeroNinos, tipoReserva, bonoId, sesion);
        System.out.println(resultado ? "Reserva realizada con éxito." : "Error al realizar la reserva con bono.");
    }

    private static void hacerNuevoBono(Scanner scanner) {
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();

        System.out.println("Seleccione el tamaño de la pista para el bono: 1. MINIBASKET, 2. TRES_VS_TRES, 3. ADULTOS");
        int opcionTamano = scanner.nextInt();
        TamanoPista tamano;
        switch (opcionTamano) {
            case 1 -> tamano = TamanoPista.MINIBASKET;
            case 2 -> tamano = TamanoPista.TRES_VS_TRES;
            case 3 -> tamano = TamanoPista.ADULTOS;
            default -> {
                System.out.println("Tamaño de pista no válido.");
                return;
            }
        }

        boolean resultado = gestorReservas.hacerNuevoBono(correo, tamano);
        System.out.println(resultado ? "Bono generado con éxito." : "Error al generar el bono.");
    }

    private static Date leerFechaHora(Scanner scanner) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        while (true) {
            try {
                return sdf.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.print("Formato incorrecto. Intente de nuevo (dd-MM-yyyy HH:mm): ");
            }
        }
    }

    private static void actualizarSesionesBono() {
        System.out.print("Ingrese el ID del bono: ");
        String bonoId = scanner.nextLine();  // Usar el scanner definido como estático
        if (gestorReservas.actualizarSesionesBono(bonoId)) {
            System.out.println("Sesiones de bono actualizadas correctamente.");
        } else {
            System.out.println("Error al actualizar sesiones del bono.");
        }
    }

    private static void verificarPlazoExcedido() {
        try {
            System.out.print("Ingrese la fecha de la reserva (dd-MM-yyyy HH:mm:ss): ");
            String fechaInput = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date fechaReserva = sdf.parse(fechaInput);

            // Llamar al método de forma estática
            if (GestorReservas.plazoExcedido(fechaReserva)) {
                System.out.println("El plazo de 24 horas ha sido excedido.");
            } else {
                System.out.println("El plazo de 24 horas no ha sido excedido.");
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto.");
        }
    }
    private static void listarReservasFuturas() {
        int resultado = gestorReservas.listarReservasFuturas();
        if (resultado == -1) {
            System.out.println("No hay reservas futuras registradas.");
        } else if (resultado == -2) {
            System.out.println("Error al listar reservas futuras.");
        }
    }

    private static void modificarReserva() {
        System.out.print("Ingrese el ID de la reserva a modificar: ");
        String idReserva = scanner.nextLine();

        // Pedir al usuario que seleccione el tipo de reserva
        System.out.println("Seleccione el tipo de reserva a modificar: 1. Infantil, 2. Familiar, 3. Adultos");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        // Crear la instancia de la subclase correcta de Reserva según la elección
        Reserva nuevaReserva;
        switch (tipo) {
            case 1:
                nuevaReserva = new ReservaInfantil();
                break;
            case 2:
                nuevaReserva = new ReservaFamiliar();
                break;
            case 3:
                nuevaReserva = new ReservaAdultos();
                break;
            default:
                System.out.println("Tipo de reserva no válido.");
                return;
        }

        // Intentar modificar la reserva
        try {
            int resultado = gestorReservas.modificarReserva(idReserva, nuevaReserva);
            switch (resultado) {
                case -1:
                    System.out.println("No se puede modificar la reserva, ha pasado el plazo de 24 horas.");
                    break;
                case 0:
                    System.out.println("Reserva no encontrada.");
                    break;
                case 1:
                    System.out.println("Reserva modificada correctamente.");
                    break;
                default:
                    System.out.println("Error inesperado al modificar la reserva.");
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error al modificar la reserva: " + e.getMessage());
        }
    }

    private static void listarReservasPorFechaYPista() {
        try {
            System.out.print("Ingrese la fecha de la reserva (dd-MM-yyyy): ");
            String fechaInput = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date fechaBuscada = sdf.parse(fechaInput);

            System.out.print("Ingrese el ID de la pista: ");
            String idPista = scanner.nextLine();

            int resultado = gestorReservas.listarReservasPorFechaYPista(fechaBuscada, idPista);
            if (resultado == -1) {
                System.out.println("No hay reservas registradas para la fecha y pista indicadas.");
            } else if (resultado == -2) {
                System.out.println("Error al listar reservas para la fecha y pista.");
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto.");
        }
    }

    private static void cancelarReserva() {
        System.out.print("Ingrese el ID de la reserva a cancelar: ");
        String idReserva = scanner.nextLine();
        if (gestorReservas.cancelarReserva(idReserva)) {
            System.out.println("Reserva cancelada correctamente.");
        } else {
            System.out.println("Error al cancelar la reserva o no se puede cancelar porque ha excedido el plazo de 24 horas.");
        }
    }
}