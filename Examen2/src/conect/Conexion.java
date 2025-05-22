package conect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion implements AutoCloseable {
    private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/bd_examen2";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    private Connection conexion;

    static {
        try {
            // Cargar el controlador MySQL JDBC
            Class.forName(CONTROLADOR);
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador");
            e.printStackTrace();
        }
    }

    public Conexion() {
    	try {
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexión establecida correctamente.");  // Descomentar esta línea
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión");
            e.printStackTrace();
            throw new RuntimeException("Error al establecer la conexión", e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("Conexión cerrada correctamente.");
        }
    }

    public Connection obtenerConexion() {
        return conexion;
    }
}



