// Clase base conceptual que se usará para todas las asignaciones.
class Cuenta {
    private String nombre;
    private double totalConsumido;
    private boolean pagada;

    public Cuenta(String nombre) {
        this.nombre = nombre;
        this.totalConsumido = 0.0;
        this.pagada = false;
    }

    public String getNombre() { return nombre; }
    public double getTotalConsumido() { return totalConsumido; }
    public boolean isPagada() { return pagada; }

    public void agregarConsumo(double monto) {
        this.totalConsumido += monto;
    }

    public void marcarComoPagada() {
        this.pagada = true;
    }
}