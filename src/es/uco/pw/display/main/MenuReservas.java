package es.uco.pw.display.main;


import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;
import es.uco.pw.business.Gestores.GestorReservas;

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
 * Menu principal para gestionar resevas.
 */
public class MenuReservas {
	
	/**
     * Constructor por defecto de la clase MenuReservas.
     */
    public MenuReservas() {
        // Constructor por defecto
    }
	
    private static final GestorReservas gestorReservas = new GestorReservas();
    private static final Scanner scanner = new Scanner(System.in); // Definir Scanner como estático

    /**
     * Muestra el menú de gestión de reservas al usuario y permite seleccionar 
     * diferentes opciones para realizar acciones relacionadas con las reservas.
     */
    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
        	System.out.println("\n───────────────────────────────────────");
            System.out.println("----- Menú de Gestión de Reservas -----");
            System.out.println("───────────────────────────────────────");
            System.out.println("  1. Crear nuevo bono");
            System.out.println("  2. Hacer reserva individual");
            System.out.println("  3. Hacer reserva con bono");
            System.out.println("  4. Modificar una reserva");
            System.out.println("  5. Cancelar una reserva");
            System.out.println("  6. Listar reservas futuras");
            System.out.println("  7. Listar reservas por fecha y pista");
            System.out.println("───────────────────────────────────────");
            System.out.println("  8. Salir");
            System.out.println("───────────────────────────────────────");
            System.out.print("\n Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer de entrada

            switch (opcion) {
                case 1:
                	hacerNuevoBono(scanner);
                    break;
                case 2:
                	hacerReservaIndividual(scanner);
                    break;
                case 3:
                	hacerReservaBono(scanner);
                    break;
                case 4:
                	modificarReserva();
                    break;
                case 5:
                	cancelarReserva();
                    break;
                case 6:
                    listarReservasFuturas();
                    break;
                case 7:
                	listarReservasPorFechaYPista();
                    break;
                case 8:
                	salir = true;
                	System.out.println("Saliendo del programa...");
                	break;
                default:
                    System.out.println(" ERROR! Opción no válida. Por favor intente de nuevo.");
            }
        }
    }

    private static void hacerReservaIndividual(Scanner scanner) {
    	
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();
        if (correo == null || correo.isEmpty()) {
            System.out.println(" ERROR! El correo no puede estar vacío.");
            return;
        }
        
        System.out.print("Nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        if (nombrePista == null || nombrePista.isEmpty()) {
            System.out.println(" ERROR! El nombre de la pista no puede estar vacío.");
            return;
        }
        
        System.out.print("Fecha y hora de la reserva (dd/MM/yyyy HH:mm): ");
        Date fechaHora;
        try {
            fechaHora = leerFechaHora(scanner);
        } catch (Exception e) {
            System.out.println(" ERROR! Formato de fecha y hora inválido. Use el formato dd/MM/yyyy HH:mm.");
            return;
        }
        
        System.out.print("Duración en minutos (60, 90, 120): ");
        int duracion;

        duracion = scanner.nextInt();
        if (duracion != 60 && duracion != 90 && duracion != 120) {
        	System.out.println(" ERROR! Duración no válida. Solo se permiten 60, 90 o 120 minutos.");
            return;
        }


        System.out.println("Tipo de reserva: 1. Infantil, 2. Familiar, 3. Adultos");
        int tipo = scanner.nextInt();
        Class<? extends ReservaDTO> tipoReserva;
        switch (tipo) {
            case 1 -> tipoReserva = ReservaInfantilDTO.class;
            case 2 -> tipoReserva = ReservaFamiliarDTO.class;
            case 3 -> tipoReserva = ReservaAdultosDTO.class;
            default -> {
                System.out.println(" ERROR! Tipo no válido.");
                return;
            }
        }
        
        int numeroNinos=0;
        int numeroAdultos=0;
        
        if(tipo==1 || tipo==2) {
        	System.out.print("Número de niños: ");
        	numeroNinos= scanner.nextInt();        	
        }
        
        if(tipo>=2) {
        	System.out.print("Número de adultos: ");
        	numeroAdultos = scanner.nextInt();	
        }
        
        scanner.nextLine(); // Limpiar el buffer de entrada

        boolean resultado = gestorReservas.hacerReservaIndividual(correo, nombrePista, fechaHora, duracion, numeroAdultos, numeroNinos, tipoReserva);
        System.out.println(resultado ? "Reserva realizada con éxito." : " ERROR! Error al realizar la reserva.");
        
    }

    private static void hacerReservaBono(Scanner scanner) {    	
    	System.out.print("ID del bono: ");
    	String bonoId = scanner.nextLine();
        if (bonoId == null || bonoId.isEmpty()) {
            System.out.println("ERROR! El ID del bono no puede estar vacío.");
            return;
        }
        
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();
        if (correo == null || correo.isEmpty()) {
            System.out.println("ERROR! El correo no puede estar vacío.");
            return;
        }
        
        System.out.print("Nombre de la pista: ");
        String nombrePista = scanner.nextLine();
        if (nombrePista == null || nombrePista.isEmpty()) {
            System.out.println("ERROR! El nombre de la pista no puede estar vacío.");
            return;
        }
        
        System.out.print("Fecha y hora de la reserva (dd/MM/yyyy HH:mm): ");
        Date fechaHora;
        try {
            fechaHora = leerFechaHora(scanner);
            if (fechaHora == null) {
                throw new IllegalArgumentException("La fecha y hora no pueden ser nulas.");
            }
        } catch (Exception e) {
            System.out.println("ERROR! Formato de fecha y hora inválido. Use el formato dd/MM/yyyy HH:mm.");
            return;
        }
        
        System.out.print("Duración en minutos (60, 90, 120): ");
        int duracion;

        duracion = scanner.nextInt();
        if (duracion != 60 && duracion != 90 && duracion != 120) {
        	System.out.println(" ERROR! Duración no válida. Solo se permiten 60, 90 o 120 minutos.");
            return;
        }


        System.out.println("Tipo de reserva: 1. Infantil, 2. Familiar, 3. Adultos");
        int tipo = scanner.nextInt();
        Class<? extends ReservaDTO> tipoReserva;
        switch (tipo) {
            case 1 -> tipoReserva = ReservaInfantilDTO.class;
            case 2 -> tipoReserva = ReservaFamiliarDTO.class;
            case 3 -> tipoReserva = ReservaAdultosDTO.class;
            default -> {
                System.out.println(" ERROR! Tipo no válido.");
                return;
            }
        }
        
        int numeroNinos=0;
        int numeroAdultos=0;
        
        if(tipo==1 || tipo==2) {
        	System.out.print("Número de niños: ");
        	numeroNinos= scanner.nextInt(); 
            if (numeroNinos < 0) {
                System.out.println("ERROR! El número de niños no puede ser negativo.");
                return;
            }
        }
        
        if(tipo>=2) {
        	System.out.print("Número de adultos: ");
        	numeroAdultos = scanner.nextInt();	
            if (numeroAdultos < 0) {
                System.out.println("ERROR! El número de adultos no puede ser negativo.");
                return;
            }
        }

        scanner.nextLine(); // Limpiar el buffer de entrada


        boolean resultado = gestorReservas.hacerReservaBono(correo, nombrePista, fechaHora, duracion, numeroAdultos, numeroNinos, tipoReserva, bonoId);
        System.out.println(resultado ? "Reserva realizada con éxito." : " ERROR! Error al realizar la reserva con bono.");
    }

    private static void hacerNuevoBono(Scanner scanner) {
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();
        if (correo == null || correo.isEmpty()) {
            System.out.println("ERROR! El correo no puede estar vacío.");
            return;
        }

        System.out.println("Seleccione el tamaño de la pista para el bono: 1. MINIBASKET, 2. TRES_VS_TRES, 3. ADULTOS");
        int opcionTamano = scanner.nextInt();
        TamanoPista tamano;
        switch (opcionTamano) {
            case 1 -> tamano = TamanoPista.MINIBASKET;
            case 2 -> tamano = TamanoPista.TRES_VS_TRES;
            case 3 -> tamano = TamanoPista.ADULTOS;
            default -> {
                System.out.println(" ERROR! Tamaño de pista no válido.");
                return;
            }
        }

        boolean resultado = gestorReservas.hacerNuevoBono(correo, tamano);
        System.out.println(resultado ? "Bono generado con éxito." : " ERROR! Error al generar el bono.");
    }

    private static Date leerFechaHora(Scanner scanner) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        while (true) {
            try {
                return sdf.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.print(" ERROR! Formato incorrecto. Intente de nuevo (dd/MM/yyyy HH:mm): ");
            }
        }
    }

    private static void listarReservasFuturas() {
        int resultado = gestorReservas.listarReservasFuturas();
        if (resultado == -1) {
            System.out.println("No hay reservas futuras registradas.");
        } else if (resultado == -2) {
            System.out.println(" ERROR! Error al listar reservas futuras.");
        }
    }

    private static void modificarReserva() {    	
        System.out.print("Ingrese el ID de la reserva a modificar: ");
        String idReserva = scanner.nextLine();
        
        // Pedir al usuario que seleccione el tipo de reserva
        ReservaDTO nuevaReserva= gestorReservas.obtenerReservaPorId(idReserva);
        
        boolean modificable= true;
        
        System.out.println("\n  ¿Qué deseas modificar?");
        System.out.println("    1. Correo del reservante");
        System.out.println("    2. Fecha de reserva");
        System.out.println("    3. Duracion de reserva");
        System.out.println("    4. Pista");
        System.out.print("Elige una opción: ");
        int opcionModificar = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        switch (opcionModificar) {
            case 1:
                System.out.print("\n Introduce el nuevo correo: ");
                String correoUsuario = scanner.nextLine();
                if (correoUsuario == null || correoUsuario.isEmpty()) {
                    System.out.println("ERROR! El correo no puede estar vacío.");
                    break;  // Salir del caso si el correo es vacío
                }

                JugadorDTO user = gestorReservas.buscarJugador(correoUsuario);
                if (user == null) {
                    modificable = false;
                    System.out.println("ERROR! El usuario no existe.");
                } else {
                    nuevaReserva.setUsuarioId(correoUsuario);
                }
                break;

            case 2:
                System.out.print("\n Introduce la nueva fecha de reserva (dd/MM/yyyy HH:mm): ");
                Date nuevaFecha = leerFechaHora(scanner);
                if (nuevaFecha == null) {
                    System.out.println("ERROR! La fecha no puede ser nula.");
                    break;  // Salir si la fecha es inválida
                }
                nuevaReserva.setFechaHora(nuevaFecha);
                break;

            case 3:
                System.out.print("\n Introduce la nueva duracion de reserva: ");
                int duracion = scanner.nextInt();
                if (duracion <= 0) {
                    System.out.println("ERROR! La duración debe ser un número positivo.");
                    break;  // Salir si la duración es inválida
                }
                nuevaReserva.setDuracion(duracion);
                break;
            case 4:
                System.out.print("\n Introduce la nueva pista: ");
                String pistaString = scanner.nextLine();
                
                if (pistaString == null || pistaString.isEmpty()) {
                    System.out.println("ERROR! El nombre de la pista no puede estar vacío.");
                    break;  // Salir si el nombre de la pista es vacío
                }
                
                PistaDTO pista= gestorReservas.buscarPista(pistaString);
                
                if(pista==null) {
                	System.out.print(" ERROR! No existe la pista que se quiere introducir.");
                }
                TamanoPista tamano= pista.getTamanoPista();
                
//                if((tipo== 1 && tamano==TamanoPista.TRES_VS_TRES) || (tipo== 1 && tamano==TamanoPista.ADULTOS)) {
//                	System.out.print(" ERROR! Pista no compatible con el tipo de reserva.");
//                	modificable= false;
//                }
//                
//                else if((tipo== 2 && tamano==TamanoPista.ADULTOS)) {
//                	System.out.print(" ERROR! Pista no compatible con el tipo de reserva.");
//                	modificable= false;
//                }
//                
//                else if((tipo== 3 && tamano==TamanoPista.MINIBASKET) || (tipo== 3 && tamano==TamanoPista.TRES_VS_TRES)) {
//                	System.out.print(" ERROR! Pista no compatible con el tipo de reserva.");
//                	modificable= false;
//                }
                
                nuevaReserva.setPistaId(pistaString);
                break;

            default:
                System.out.println("\n ERROR! Opción no válida.");
                return;
        }


        // Intentar modificar la reserva
        if(modificable) {
	        try {
	            int resultado = gestorReservas.modificarReserva(idReserva, nuevaReserva);
	            switch (resultado) {
	                case -1:
	                    System.out.println(" ERROR! No se puede modificar la reserva, ha pasado el plazo de 24 horas.");
	                    break;
	                case 0:
	                    System.out.println(" ERROR! Reserva no encontrada.");
	                    break;
	                case 1:
	                    System.out.println("Reserva modificada correctamente.");
	                    break;
	                default:
	                    System.out.println(" ERROR! Error inesperado al modificar la reserva.");
	                    break;
	            }
	        } catch (IOException e) {
	            System.out.println(" ERROR! Error al modificar la reserva: " + e.getMessage());
	        }
        }
    }

    private static void listarReservasPorFechaYPista() {
        try {
            System.out.print("Ingrese la fecha de la reserva (dd/MM/yyyy): ");
            String fechaInput = scanner.nextLine();
            if (fechaInput == null || fechaInput.trim().isEmpty()) {
                System.out.println("ERROR! La fecha no puede estar vacía.");
                return; // Salir si la fecha es vacía
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaBuscada = sdf.parse(fechaInput);

            System.out.print("Ingrese el ID de la pista: ");
            String idPista = scanner.nextLine();
            if (idPista == null || idPista.trim().isEmpty()) {
                System.out.println("ERROR! El ID de la pista no puede estar vacío.");
                return; // Salir si el ID de la pista es vacío
            }

            int resultado = gestorReservas.listarReservasPorFechaYPista(fechaBuscada, idPista);
            if (resultado == -1) {
                System.out.println("No hay reservas registradas para la fecha y pista indicadas.");
            } else if (resultado == -2) {
                System.out.println(" ERROR! Error al listar reservas para la fecha y pista.");
            }
        } catch (ParseException e) {
            System.out.println(" ERROR! Formato de fecha incorrecto.");
        }
    }

    private static void cancelarReserva() {
        System.out.print("Ingrese el ID de la reserva a cancelar: ");
        String idReserva = scanner.nextLine();
        if (idReserva == null || idReserva.trim().isEmpty()) {
            System.out.println("ERROR! El ID de la reserva no puede estar vacío.");
            return; // Salir si el ID de la reserva es vacío
        }
        
        if (gestorReservas.cancelarReserva(idReserva)) {
            System.out.println("Reserva cancelada correctamente.");
        } else {
            System.out.println(" ERROR! Error al cancelar la reserva o no se puede cancelar porque ha excedido el plazo de 24 horas.");
        }
    }
}