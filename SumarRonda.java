// Archivo: SumarRonda.java
public class SumarRonda {
    public void agregarRonda(Cuenta cuenta, double montoRonda) {
        System.out.println("-> [Ronda] Sumando nueva ronda de $" + montoRonda + " a la cuenta " + cuenta.getNombre());
        cuenta.agregarConsumo(montoRonda);
    }
}