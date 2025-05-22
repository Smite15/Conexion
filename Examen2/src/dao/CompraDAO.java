package dao;

import bd.Compra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraDAO implements AutoCloseable {

    private Connection conexion;

    // Constructor que toma la conexión como parámetro
    public CompraDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para cerrar la conexión
    @Override
    public void close() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }

    public ObservableList<Compra> consultarCompras() throws SQLException {
        ObservableList<Compra> listaCompras = FXCollections.observableArrayList();

        try (PreparedStatement statement = this.conexion.prepareStatement("SELECT * FROM Compras")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Compra compra = new Compra(
                            resultSet.getInt("ID"),
                            resultSet.getInt("proveedorID"),
                            resultSet.getInt("productoID"),
                            resultSet.getInt("cantidad")
                    );
                    listaCompras.add(compra);
                }
            }
        }

        return listaCompras;
    }

    public void insertarCompra(Compra compra) throws SQLException {
        String query = "INSERT INTO Compras (proveedorID, productoID, cantidad) VALUES (?, ?, ?)";

        try (PreparedStatement statement = this.conexion.prepareStatement(query)) {
            statement.setInt(1, compra.getProveedorID());
            statement.setInt(2, compra.getProductoID());
            statement.setInt(3, compra.getCantidad());

            statement.executeUpdate();
        }
    }
}
