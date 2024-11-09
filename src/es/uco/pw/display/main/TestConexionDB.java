package es.uco.pw.display.main;

import es.uco.pw.common.DBConnection;
import java.sql.Connection;

public class TestConexionDB {

    public static void main(String[] args) {
        // Instancia de la clase DBConnection
        DBConnection dbConnection = new DBConnection();

        // Intentar establecer la conexión a la base de datos
        Connection connection = dbConnection.getConnection();

        // Verificar si la conexión fue exitosa
        if (connection != null) {
            System.out.println("¡Conexión a la base de datos exitosa!");
            
            // Aquí puedes realizar consultas a la base de datos o interacciones adicionales
            
            // Finalmente, cerrar la conexión
            dbConnection.closeConnection();
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
        }
    }
}
