import java.util.List;
import java.util.stream.Collectors;

// Archivo: ListadoDiario.java
public class ListadoDiario {
    public void generarListado(List<Cuenta> todasLasCuentas) {
        System.out.println("\n--- Listado Diario de Consumos ---");

        // Cuentas Pendientes (no liquidadas)
        List<Cuenta> pendientes = todasLasCuentas.stream()
            .filter(c -> !c.isPagada())
            .collect(Collectors.toList());

        System.out.println("\n[PENDIENTES DE PAGO (" + pendientes.size() + ")]");
        pendientes.forEach(c -> System.out.println(" - " + c.getNombre() + ": $" + c.getTotalConsumido()));

        // Cuentas Liquidadas (pagadas)
        List<Cuenta> liquidadas = todasLasCuentas.stream()
            .filter(c -> c.isPagada())
            .collect(Collectors.toList());

        System.out.println("\n[LIQUIDADOS (" + liquidadas.size() + ")]");
        liquidadas.forEach(c -> System.out.println(" - " + c.getNombre() + ": $" + c.getTotalConsumido()));

        System.out.println("---------------------------------");
    }
}