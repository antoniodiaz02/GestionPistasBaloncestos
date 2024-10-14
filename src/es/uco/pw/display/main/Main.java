package es.uco.pw.display.main;

import es.uco.pw.data.*;
import es.uco.pw.factory.*;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        // Crear una fecha de reserva
        Date fechaReserva = new Date();

        // Crear reservas usando la fábrica de reservas individuales
        ReservaFactory reservaFactory = new ReservaIndividualFactory();

        // Crear una reserva infantil
        ReservaInfantil reservaInfantil = reservaFactory.createReservaInfantil(
                "usuario1", fechaReserva, 60, "pista1", 10.0f, 2.0f, 3);
        System.out.println(reservaInfantil.toString());

        // Crear una reserva familiar
        ReservaFamiliar reservaFamiliar = reservaFactory.createReservaFamiliar(
                "usuario2", fechaReserva, 120, "pista2", 20.0f, 5.0f, 2, 2);
        System.out.println(reservaFamiliar.toString());

        // Crear una reserva de adultos
        ReservaAdultos reservaAdultos = reservaFactory.createReservaAdultos(
                "usuario3", fechaReserva, 90, "pista3", 30.0f, 0.0f, 4);
        System.out.println(reservaAdultos.toString());

        // Crear reservas usando la fábrica de reservas bono
        ReservaFactory reservaBonoFactory = new ReservaBonoFactory("bono123", 1);

        // Crear una reserva infantil con bono
        ReservaInfantil reservaInfantilBono = reservaBonoFactory.createReservaInfantil(
                "usuario4", fechaReserva, 60, "pista4", 12.0f, 3.0f, 2);
        System.out.println(reservaInfantilBono.toString());

        // Crear una reserva familiar con bono
        ReservaFamiliar reservaFamiliarBono = reservaBonoFactory.createReservaFamiliar(
                "usuario5", fechaReserva, 120, "pista5", 22.0f, 5.0f, 3, 1);
        System.out.println(reservaFamiliarBono.toString());

        // Crear una reserva de adultos con bono
        ReservaAdultos reservaAdultosBono = reservaBonoFactory.createReservaAdultos(
                "usuario6", fechaReserva, 90, "pista6", 35.0f, 0.0f, 5);
        System.out.println(reservaAdultosBono.toString());
    }
}
