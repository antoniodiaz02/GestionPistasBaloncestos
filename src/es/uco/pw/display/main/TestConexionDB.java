package es.uco.pw.display.main;

import es.uco.pw.common.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConexionDB {

    public static void main(String[] args) {
        // Instancia de la clase DBConnection
        DBConnection dbConnection = new DBConnection();

        // Intentar establecer la conexión a la base de datos
        Connection connection = dbConnection.getConnection();

        // Verificar si la conexión fue exitosa
        if (connection != null) {
            System.out.println("¡Conexión a la base de datos exitosa!");

            // Intentamos obtener las tablas de la base de datos
            try {
                // Creamos un Statement para ejecutar consultas SQL
                Statement statement = connection.createStatement();
                
                // Ejecutamos una consulta para obtener las tablas de la base de datos
                String sql = " select * from Material"; // Para MySQL. Si usas otro DBMS, cambia la consulta
                ResultSet resultSet = statement.executeQuery(sql);

                // Imprimimos las tablas
                System.out.println("Tablas en la base de datos:");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1)); // Imprime el nombre de cada tabla
                }
                
                // Cerramos el ResultSet y Statement
                resultSet.close();
                statement.close();

            } catch (Exception e) {
                System.out.println("Error al obtener las tablas: " + e.getMessage());
            } finally {
                // Finalmente, cerramos la conexión
                dbConnection.closeConnection();
            }
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
        }
    }
}
