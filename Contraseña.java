// Archivo: Main.java
// Funcionalidad: Sistema de clientes con historial protegido por PIN.

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// -------------------- Clase Movimiento --------------------
class Movimiento {
    private String tipo;        // COMPRA o PAGO
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
               " (" + signo + "$" + monto + ")";
    }
}

// -------------------- Clase Cliente --------------------
class Cliente {
    private String nombre;
    private double saldo;
    private List<Movimiento> historial;

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

    public void registrarCompra(String descripcion, double monto, String fecha) {
        if (monto <= 0) {
            System.out.println("⚠️ Monto de compra inválido.");
            return;
        }
        saldo += monto;
        historial.add(new Movimiento("COMPRA", descripcion, monto, fecha));
        System.out.println("🛒 Compra registrada: " + descripcion + " por $" + monto);
        System.out.println("Saldo actual: $" + saldo);
    }

    public void registrarPago(double monto, String fecha) {
        if (monto <= 0) {
            System.out.println("⚠️ Monto de pago inválido.");
            return;
        }
        if (monto > saldo) monto = saldo;
        saldo -= monto;
        historial.add(new Movimiento("PAGO", "Abono a la deuda", monto, fecha));
        System.out.println("💰 Pago registrado: $" + monto);
        System.out.println("Saldo actualizado: $" + saldo);
    }

    public void mostrarHistorial() {
        System.out.println("\n📜 HISTORIAL DE " + nombre.toUpperCase());
        if (historial.isEmpty()) {
            System.out.println("Sin movimientos registrados.");
            return;
        }
        for (Movimiento m : historial) System.out.println(m);
        System.out.println("Saldo final: $" + saldo);
    }
}

// -------------------- Clase SistemaPagos (con PIN) --------------------
class SistemaPagos {
    private String pinPropietario;  // PIN de seguridad
    private List<Cliente> clientes;
    private Scanner sc;

    public SistemaPagos(String pinInicial) {
        this.pinPropietario = pinInicial;
        this.clientes = new ArrayList<>();
        this.sc = new Scanner(System.in);
    }

    // Verifica PIN antes de cualquier acción sensible
    private boolean autenticar() {
        System.out.print("\n🔑 Ingrese PIN de seguridad: ");
        String pinIngresado = sc.nextLine();
        if (!pinIngresado.equals(pinPropietario)) {
            System.out.println("❌ PIN incorrecto. Acceso denegado.");
            return false;
        }
        System.out.println("✅ Acceso concedido.");
        return true;
    }

    public void agregarCliente(Cliente c) {
        clientes.add(c);
    }

    public Cliente buscarCliente(String nombre) {
        for (Cliente c : clientes)
            if (c.getNombre().equalsIgnoreCase(nombre))
                return c;
        return null;
    }

    public void registrarCompra(String nombre, String descripcion, double monto, String fecha) {
        if (!autenticar()) return;
        Cliente c = buscarCliente(nombre);
        if (c != null) c.registrarCompra(descripcion, monto, fecha);
        else System.out.println("Cliente no encontrado.");
    }

    public void registrarPago(String nombre, double monto, String fecha) {
        if (!autenticar()) return;
        Cliente c = buscarCliente(nombre);
        if (c != null) c.registrarPago(monto, fecha);
        else System.out.println("Cliente no encontrado.");
    }

    public void mostrarHistorial(String nombre) {
        if (!autenticar()) return;
        Cliente c = buscarCliente(nombre);
        if (c != null) c.mostrarHistorial();
        else System.out.println("Cliente no encontrado.");
    }

    // Opción para cambiar el PIN del sistema
    public void cambiarPin() {
        if (!autenticar()) return;
        System.out.print("Ingrese nuevo PIN: ");
        String nuevoPin = sc.nextLine();
        pinPropietario = nuevoPin;
        System.out.println("🔒 PIN actualizado correctamente.");
    }
}

// -------------------- Clase Principal --------------------
public class Main {
    public static void main(String[] args) {
        SistemaPagos sistema = new SistemaPagos("1234");  // PIN inicial
        Cliente c1 = new Cliente("Camila Pérez");
        sistema.agregarCliente(c1);

        // Menú simple de demostración
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENÚ DEL SISTEMA ---");
            System.out.println("1. Registrar compra");
            System.out.println("2. Registrar pago");
            System.out.println("3. Ver historial");
            System.out.println("4. Cambiar PIN");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1:
                    sistema.registrarCompra("Camila Pérez", "Bolso de cuero", 200.0, "2025-11-09");
                    break;
                case 2:
                    sistema.registrarPago("Camila Pérez", 100.0, "2025-11-10");
                    break;
                case 3:
                    sistema.mostrarHistorial("Camila Pérez");
                    break;
                case 4:
                    sistema.cambiarPin();
                    break;
                case 0:
                    System.out.println("👋 Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

        sc.close();
    }
}
