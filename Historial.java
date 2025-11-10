// Archivo: Main.java
// Funcionalidad: Mantener historial de compras y pagos por cliente.

import java.util.ArrayList;
import java.util.List;

// Clase que representa a un cliente
class Cliente {
    private String nombre;
    private double saldo;
    private List<Movimiento> historial;  // Almacena compras y pagos

    public Cliente(String nombre) {
        this.nombre = nombre;
        this.saldo = 0.0;
        this.historial = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Movimiento> getHistorial() {
        return historial;
    }

    // Registrar una compra (aumenta deuda)
    public void registrarCompra(String descripcion, double monto, String fecha) {
        if (monto <= 0) {
            System.out.println("⚠️ El monto de la compra debe ser mayor que cero.");
            return;
        }
        saldo += monto;
        Movimiento mov = new Movimiento("COMPRA", descripcion, monto, fecha);
        historial.add(mov);
        System.out.println("🛒 Compra registrada: " + descripcion + " por $" + monto);
        System.out.println("Saldo actual: $" + saldo);
    }

    // Registrar un pago (disminuye deuda)
    public void registrarPago(double monto, String fecha) {
        if (monto <= 0) {
            System.out.println("⚠️ El monto del pago debe ser mayor que cero.");
            return;
        }
        if (monto > saldo) {
            System.out.println("⚠️ El pago excede la deuda. Se aplicará solo el saldo pendiente.");
            monto = saldo;
        }
        saldo -= monto;
        Movimiento mov = new Movimiento("PAGO", "Abono a la deuda", monto, fecha);
        historial.add(mov);
        System.out.println("💰 Pago registrado: $" + monto);
        System.out.println("Saldo actualizado: $" + saldo);
    }

    // Mostrar historial de movimientos
    public void mostrarHistorial() {
        System.out.println("\n📜 HISTORIAL DE " + nombre.toUpperCase());
        if (historial.isEmpty()) {
            System.out.println("Sin movimientos registrados.");
            return;
        }
        for (Movimiento m : historial) {
            System.out.println(m);
        }
        System.out.println("Saldo final: $" + saldo);
    }
}

// Clase que representa un movimiento (compra o pago)
class Movimiento {
    private String tipo;       // "COMPRA" o "PAGO"
    private String descripcion;
    private double monto;
    private String fecha;

    public Movimiento(String tipo, String descripcion, double monto, String fecha) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        String signo = tipo.equals("COMPRA") ? "+" : "-";
        return "[" + fecha + "] " + tipo + ": " + descripcion +
               "  (" + signo + "$" + monto + ")";
    }
}

// Clase principal
public class Main {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente("Laura Gómez");

        // Registrar algunas compras y pagos
        cliente1.registrarCompra("Zapatos deportivos", 300.0, "2025-11-01");
        cliente1.registrarCompra("Chaqueta impermeable", 250.0, "2025-11-03");
        cliente1.registrarPago(200.0, "2025-11-05");
        cliente1.registrarCompra("Bufanda de lana", 100.0, "2025-11-07");
        cliente1.registrarPago(150.0, "2025-11-09");

        // Consultar deuda actual
        System.out.println("\n💵 Deuda actual de " + cliente1.getNombre() + ": $" + cliente1.getSaldo());

        // Mostrar historial completo
        cliente1.mostrarHistorial();
    }
}
