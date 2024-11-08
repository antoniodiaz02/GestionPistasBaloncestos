package es.uco.pw.display.main;

import es.uco.pw.common.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        // Crear una instancia de la clase DBConnection
        DBConnection dbConnection = new DBConnection();
        
        // Intentar obtener la conexión
        Connection connection = dbConnection.getConnection();
        
        if (connection != null) {
            try {
                // Si la conexión es exitosa, obtener y mostrar los metadatos de la base de datos
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
                System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());

                // Realizar una simple consulta para verificar que la base de datos está funcionando
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT 1");

                // Si la consulta se ejecuta correctamente, imprimir un mensaje
                if (resultSet.next()) {
                    System.out.println("Database connection is working correctly.");
                }

                // Cerrar la conexión después de la prueba
                dbConnection.closeConnection();

            } catch (SQLException e) {
                System.err.println("Error executing the database query.");
                e.printStackTrace();
            }
        } else {
            System.err.println("Connection could not be established.");
        }
    }
}
