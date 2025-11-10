// Archivo: PagoConsumo.java
public class PagoConsumo {
    public void liquidarConsumo(Cuenta cuenta, String metodoPago) {
        if (!cuenta.isPagada()) {
            cuenta.marcarComoPagada();
            System.out.println("-> [Pago] Cuenta '" + cuenta.getNombre() + "' liquidada con éxito mediante " + metodoPago + ".");
        } else {
            System.out.println("-> [Pago] Error: La cuenta '" + cuenta.getNombre() + "' ya estaba pagada.");
        }
    }
}