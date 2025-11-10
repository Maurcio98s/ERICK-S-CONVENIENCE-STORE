// Archivo: Main.java
// Funcionalidad: Registrar un pago parcial o total de un cliente y actualizar su saldo.

import java.util.ArrayList;
import java.util.List;

// Clase que representa a un cliente
class Cliente {
    private String nombre;
    private double saldo;

    public Cliente(String nombre, double saldoInicial) {
        this.nombre = nombre;
        this.saldo = saldoInicial;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    // Método para aplicar un pago parcial o total
    public void registrarPago(double monto) {
        if (monto <= 0) {
            System.out.println("El monto del pago debe ser mayor que cero.");
            return;
        }
        if (monto > saldo) {
            System.out.println("El pago excede el saldo pendiente. Se registrará solo el saldo restante.");
            monto = saldo;
        }
        saldo -= monto;
        System.out.println("Pago de $" + monto + " registrado correctamente para " + nombre + ".");
        System.out.println("Saldo actualizado: $" + saldo);
    }
}

// Clase para almacenar información de pagos realizados
class RegistroPago {
    private Cliente cliente;
    private double monto;
    private String fecha;

    public RegistroPago(Cliente cliente, double monto, String fecha) {
        this.cliente = cliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Cliente: " + cliente.getNombre() +
               " | Monto pagado: $" + monto +
               " | Fecha: " + fecha;
    }
}

// Clase que gestiona los pagos
class SistemaPagos {
    private List<RegistroPago> historialPagos;

    public SistemaPagos() {
        historialPagos = new ArrayList<>();
    }

    public void registrarPago(Cliente c, double monto, String fecha) {
        c.registrarPago(monto);
        historialPagos.add(new RegistroPago(c, monto, fecha));
    }

    public void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL DE PAGOS ---");
        for (RegistroPago pago : historialPagos) {
            System.out.println(pago);
        }
    }
}

// Clase principal
public class PagoParcial {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente("Carlos López", 500.0);
        SistemaPagos sistema = new SistemaPagos();

        // Registrar pagos
        sistema.registrarPago(cliente1, 200.0, "2025-11-09"); // pago parcial
        sistema.registrarPago(cliente1, 300.0, "2025-11-10"); // pago total

        // Mostrar historial de pagos
        sistema.mostrarHistorial();
    }
}
