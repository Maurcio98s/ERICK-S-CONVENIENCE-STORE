// Archivo: RegistroRapido.java
public class Registro {
    public void registrarConsumoRapido(Cuenta cuenta, double monto, boolean esGrupal) {
        String tipo = esGrupal ? "Grupal" : "Individual";
        System.out.println("-> [Rápido] Registrando consumo " + tipo + " de $" + monto + " a la cuenta " + cuenta.getNombre());
        cuenta.agregarConsumo(monto);
    }
}
