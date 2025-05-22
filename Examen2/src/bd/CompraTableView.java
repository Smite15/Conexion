package bd;

import dao.CompraDAO;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import conect.Conexion;

import java.sql.SQLException;

public class CompraTableView extends Application {

    private TableView<Compra> tableView;
    private CompraDAO compraDAO;
    private TextField tfProveedorID;
    private TextField tfProductoID;
    private TextField tfCantidad;
    private Conexion conexion; // Agregado

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Quita el try-with-resources de la creación de la conexión
        conexion = new Conexion();
        compraDAO = new CompraDAO(conexion.obtenerConexion());

        TableColumn<Compra, Integer> columnaID = new TableColumn<>("ID");
        TableColumn<Compra, Integer> columnaProveedorID = new TableColumn<>("Proveedor ID");
        TableColumn<Compra, Integer> columnaProductoID = new TableColumn<>("Producto ID");
        TableColumn<Compra, Integer> columnaCantidad = new TableColumn<>("Cantidad");

        columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaProveedorID.setCellValueFactory(new PropertyValueFactory<>("proveedorID"));
        columnaProductoID.setCellValueFactory(new PropertyValueFactory<>("productoID"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tableView = new TableView<>(); // Inicializar el TableView

        tableView.getColumns().addAll(columnaID, columnaProveedorID, columnaProductoID, columnaCantidad);

        tfProveedorID = new TextField();
        tfProductoID = new TextField();
        tfCantidad = new TextField();

        tfProveedorID.setPromptText("Proveedor ID");
        tfProductoID.setPromptText("Producto ID");
        tfCantidad.setPromptText("Cantidad");

        Button ingresarButton = new Button("Ingresar");
        ingresarButton.setOnAction(event -> ingresarCompra());

        HBox inputBox = new HBox(tfProveedorID, tfProductoID, tfCantidad, ingresarButton);
        inputBox.setSpacing(10);

        VBox vbox = new VBox(tableView, inputBox);
        Scene scene = new Scene(vbox, 600, 400);

        primaryStage.setTitle("Compras TableView");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Agrega un nuevo evento para cerrar la conexión cuando se cierra la aplicación
        primaryStage.setOnCloseRequest(event -> {
            try {
                // Cierra la conexión al cerrar la aplicación
                if (compraDAO != null) {
                    compraDAO.close();
                    System.out.println("CompraDAO cerrado correctamente.");
                }
                if (conexion != null) {
                    conexion.close(); // Método que deberías agregar en la clase Conexion
                    System.out.println("Conexión cerrada correctamente.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cargarComprasDesdeBD();
    }

    private void cargarComprasDesdeBD() {
        try {
            ObservableList<Compra> listaCompras = compraDAO.consultarCompras();
            tableView.setItems(listaCompras);
            System.out.println("Compras cargadas desde la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ingresarCompra() {
        try {
            int proveedorID = Integer.parseInt(tfProveedorID.getText());
            int productoID = Integer.parseInt(tfProductoID.getText());
            int cantidad = Integer.parseInt(tfCantidad.getText());

            Compra nuevaCompra = new Compra(0, proveedorID, productoID, cantidad);
            compraDAO.insertarCompra(nuevaCompra);

            System.out.println("Compra ingresada correctamente.");

            // Actualizar la tabla después de ingresar la compra
            cargarComprasDesdeBD();
        } catch (NumberFormatException e) {
            mostrarError("Error al convertir texto a número.");
            e.printStackTrace();
        } catch (SQLException e) {
            mostrarError("Error al ingresar la compra en la base de datos.");
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

