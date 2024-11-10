package es.uco.pw.display.main;

import es.uco.pw.data.DAOs.*;
import es.uco.pw.business.DTOs.*;
import es.uco.pw.common.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestFindMaterialById {
    public static void main(String[] args) {
        // Crea una instancia de la clase que contiene findMaterialById
        MaterialDAO materialDAO = new MaterialDAO();

        // ID de material para la prueba
        int idMaterial = 1;  // Cambia este valor para probar con distintos IDs

        // Invoca el método findMaterialById
        MaterialDTO material = materialDAO.findMaterialById(idMaterial);

        // Comprueba si se obtuvo el material y muestra los resultados
        if (material != null) {
            System.out.println("Material encontrado:");
            System.out.println("ID: " + material.getIdMaterial());
            System.out.println("Tipo: " + material.getTipoMaterial());
            System.out.println("Uso Interior: " + material.getEstadoMaterial());
            System.out.println("Estado: " + material.getEstadoMaterial());
        } else {
            System.out.println("No se encontró material con el ID: " + idMaterial);
        }
    }
}

