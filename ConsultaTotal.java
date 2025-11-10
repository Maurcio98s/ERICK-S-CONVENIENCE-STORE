// Archivo: ConsultaTotal.java
public class ConsultaTotal {
    public void mostrarTotal(Cuenta cuenta) {
        System.out.println("-> [Consulta] Total consumido por la cuenta '" + cuenta.getNombre() + "': $" + cuenta.getTotalConsumido());
    }
}