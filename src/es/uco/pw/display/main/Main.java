package es.uco.pw.display.main;

import es.uco.pw.data.*;
import es.uco.pw.factory.*;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Crear jugadores
        Jugador jugador1 = new Jugador("Carlos Pérez", new Date(2000 - 1900, 5 - 1, 15), "carlos.perez@example.com");
        Jugador jugador2 = new Jugador("Ana García", new Date(1998 - 1900, 8 - 1, 25), "ana.garcia@example.com");
        
        // Mostrar información de los jugadores
        System.out.println(jugador1);
        System.out.println(jugador2);
        
        // Crear materiales
        Material material1 = new Material(1, Material.TipoMaterial.PELOTAS, true, Material.EstadoMaterial.DISPONIBLE);
        Material material2 = new Material(2, Material.TipoMaterial.CANASTAS, false, Material.EstadoMaterial.DISPONIBLE);
        
        // Mostrar información de los materiales
        System.out.println(material1);
        System.out.println(material2);
        
        // Crear pistas
        Pista pista1 = new Pista("Pista Central", true, true, Pista.TamanoPista.ADULTOS, 10);
        Pista pista2 = new Pista("Pista Exterior", true, false, Pista.TamanoPista.MINIBASKET, 6);
        
        // Asociar materiales a las pistas
        pista1.asociarMaterial(material1);
        pista2.asociarMaterial(material2);
        
        // Mostrar información de las pistas
        System.out.println(pista1);
        System.out.println(pista2);
        
     // Crear una fecha para las reservas
        Date fechaReserva = new Date();

        // Prueba de Reserva Individual de Adultos
        ReservaFactory reservaAdultosFactory = new ReservaIndividualFactory("usuario1", fechaReserva, 60, "pista1", 50.0f, 5.0f, "adultos", 4);
        Reserva reservaAdultos = reservaAdultosFactory.crearReserva();
        System.out.println(reservaAdultos);

        // Prueba de Reserva Individual Familiar
        ReservaFactory reservaFamiliarFactory = new ReservaIndividualFactory("usuario2", fechaReserva, 90, "pista2", 70.0f, 10.0f, "familia", 3); // 2 adultos, 1 niño
        Reserva reservaFamiliar = reservaFamiliarFactory.crearReserva();
        System.out.println(reservaFamiliar);

        // Prueba de Reserva Individual Infantil
        ReservaFactory reservaInfantilFactory = new ReservaIndividualFactory("usuario3", fechaReserva, 30, "pista3", 30.0f, 0.0f, "infantil", 5); // 5 niños
        Reserva reservaInfantil = reservaInfantilFactory.crearReserva();
        System.out.println(reservaInfantil);

        // Prueba de Reserva Bono de Adultos
        ReservaFactory reservaBonoAdultosFactory = new ReservaBonoFactory("usuario4", fechaReserva, 60, "pista1", 50.0f, 5.0f, "adultos", 2, "bono123", 1);
        Reserva reservaBonoAdultos = reservaBonoAdultosFactory.crearReserva();
        System.out.println(reservaBonoAdultos);

        // Prueba de Reserva Bono Familiar
        ReservaFactory reservaBonoFamiliarFactory = new ReservaBonoFactory("usuario5", fechaReserva, 120, "pista2", 80.0f, 15.0f, "familia", 4, "bono456", 2);
        Reserva reservaBonoFamiliar = reservaBonoFamiliarFactory.crearReserva();
        System.out.println(reservaBonoFamiliar);

        // Prueba de Reserva Bono Infantil
        ReservaFactory reservaBonoInfantilFactory = new ReservaBonoFactory("usuario6", fechaReserva, 45, "pista3", 25.0f, 0.0f, "infantil", 10, "bono789", 1);
        Reserva reservaBonoInfantil = reservaBonoInfantilFactory.crearReserva();
        System.out.println(reservaBonoInfantil);
        

    }
}
