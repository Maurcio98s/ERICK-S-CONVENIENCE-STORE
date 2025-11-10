
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un cliente de la tienda
 * Issue #2: Acumular compras al saldo
 */
public class Cliente {
    private int id;
    private String nombre;
    private String cedula;
    private String telefono;
    private double saldoDeuda;
    private List<Compra> historialCompras;
    private Date fechaRegistro;

    public Cliente(int id, String nombre, String cedula, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.saldoDeuda = 0.0;
        this.historialCompras = new ArrayList<>();
        this.fechaRegistro = new Date();
    }

    /**
     * Issue #2: Acumula una compra al saldo del cliente
     * 
     * @param monto El monto de la compra a acumular
     * @throws IllegalArgumentException si el monto es negativo o cero
     */
    public void agregarCompra(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        this.saldoDeuda += monto;
        this.historialCompras.add(new Compra(monto, new Date(), "Compra a crédito"));
    }

    /**
     * Realiza un pago que reduce el saldo de deuda
     * 
     * @param monto El monto del pago
     * @return El saldo restante después del pago
     */
    public double registrarPago(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }
        if (monto > this.saldoDeuda) {
            throw new IllegalArgumentException("El pago no puede ser mayor a la deuda actual");
        }
        this.saldoDeuda -= monto;
        return this.saldoDeuda;
    }

    /**
     * Issue #7: Marca la deuda como saldada (pone el saldo en 0)
     */
    public void saldarDeuda() {
        this.saldoDeuda = 0.0;
    }

    /**
     * Verifica si el cliente tiene deuda pendiente
     */
    public boolean tieneDeuda() {
        return this.saldoDeuda > 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getSaldoDeuda() {
        return saldoDeuda;
    }

    public List<Compra> getHistorialCompras() {
        return new ArrayList<>(historialCompras);
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", telefono='" + telefono + '\'' +
                ", saldoDeuda=" + saldoDeuda +
                ", cantidadCompras=" + historialCompras.size() +
                '}';
    }

    /**
     * Clase interna para representar una compra
     */
    public static class Compra {
        private double monto;
        private Date fecha;
        private String descripcion;

        public Compra(double monto, Date fecha, String descripcion) {
            this.monto = monto;
            this.fecha = fecha;
            this.descripcion = descripcion;
        }

        public double getMonto() {
            return monto;
        }

        public Date getFecha() {
            return fecha;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return "Compra{" +
                    "monto=" + monto +
                    ", fecha=" + fecha +
                    ", descripcion='" + descripcion + '\'' +
                    '}';
        }
    }
}