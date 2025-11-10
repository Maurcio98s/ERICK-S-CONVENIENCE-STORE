// Archivo: Main.java
// Funcionalidad: Consultar la deuda actual de un cliente y registrar pagos parciales o totales.

import java.util.ArrayList;
import java.util.List;

// Clase que representa a un cliente con saldo pendiente
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

    // Registra un pago parcial o total
    public void registrarPago(double monto) {
        if (monto <= 0) {
            System.out.println("⚠️ El monto del pago debe ser mayor que cero.");
            return;
        }
        if (monto > saldo) {
            System.out.println("⚠️ El pago excede la deuda. Se registrará solo el saldo restante.");
            monto = saldo;
        }
        saldo -= monto;
        System.out.println("✅ Pago de $" + monto + " registrado para " + nombre + ".");
        System.out.println("Saldo actualizado: $" + saldo);
    }

    // Muestra la deuda actual
    public void mostrarDeudaActual() {
        if (saldo == 0) {
            System.out.println("💰 El cliente " + nombre + " no tiene deudas pendientes.");
        } else {
            System.out.println("💵 Deuda actual de " + nombre + ": $" + saldo);
        }
    }
}

// Clase que almacena pagos individuales
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

// Clase que gestiona pagos y consultas de deuda
class SistemaPagos {
    private List<RegistroPago> historialPagos;

    public SistemaPagos() {
        historialPagos = new ArrayList<>();
    }

    public void registrarPago(Cliente c, double monto, String fecha) {
        c.registrarPago(monto);
        historialPagos.add(new RegistroPago(c, monto, fecha));
    }

    public void consultarDeuda(Cliente c) {
        System.out.println("\n--- CONSULTA DE DEUDA ---");
        c.mostrarDeudaActual();
    }

    public void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL DE PAGOS ---");
        for (RegistroPago pago : historialPagos) {
            System.out.println(pago);
        }
    }
}

// Clase principal
public class Main {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente("Ana Martínez", 750.0);
        SistemaPagos sistema = new SistemaPagos();

        // Consultar deuda inicial
        sistema.consultarDeuda(cliente1);

        // Registrar un pago parcial
        sistema.registrarPago(cliente1, 250.0, "2025-11-09");

        // Consultar deuda al instante
        sistema.consultarDeuda(cliente1);

        // Registrar pago total
        sistema.registrarPago(cliente1, 500.0, "2025-11-10");

        // Consultar deuda final
        sistema.consultarDeuda(cliente1);

        // Mostrar historial de pagos
        sistema.mostrarHistorial();
    }
}
