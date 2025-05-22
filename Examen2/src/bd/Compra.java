package bd;

public class Compra {

    private int id;
    private int proveedorID;
    private int productoID;
    private int cantidad;

    public Compra(int id, int proveedorID, int productoID, int cantidad) {
        this.id = id;
        this.proveedorID = proveedorID;
        this.productoID = productoID;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public int getProveedorID() {
        return proveedorID;
    }

    public int getProductoID() {
        return productoID;
    }

    public int getCantidad() {
        return cantidad;
    }
}

