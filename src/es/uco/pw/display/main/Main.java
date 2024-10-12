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
        

    }
}
