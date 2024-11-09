package es.uco.pw.display.main;

import es.uco.pw.common.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

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
                
                // Ejecutamos una consulta para obtener todos los registros de la tabla Pistas
                String sql = "SELECT * FROM Pistas;";
                ResultSet resultSet = statement.executeQuery(sql);

                // Obtenemos la metadata para saber cuántas columnas tiene la tabla
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Imprimir encabezados de las columnas
                System.out.println("Datos de la tabla Pistas:");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + "\t");
                }
                System.out.println();

                // Iterar sobre cada fila en el ResultSet e imprimir sus valores
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                    System.out.println(); // Salto de línea entre registros
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
