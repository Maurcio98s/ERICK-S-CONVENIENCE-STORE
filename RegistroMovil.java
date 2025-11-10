// Archivo: RegistroMovil.java
public class RegistroMovil {
    public void registrarVenta(Cuenta cuenta, String producto, double precio) {
        // Lógica para simular la entrada de datos (ej. desde un JSON/API)
        System.out.println("-> [Celular] Ingresando venta: " + producto + " por $" + precio + " a cuenta " + cuenta.getNombre());
        cuenta.agregarConsumo(precio);
    }
}