import tkinter as tk
from tkinter import ttk


# Datos de ejemplo: ingresos semanales por método de pago (en moneda local)
INGRESOS_SEMANALES = {
    "Semana 1": {
        "Efectivo": 1250.50,
        "Tarjeta de crédito": 980.00,
        "Transferencia": 640.25,
        "Pago móvil": 410.00,
    },
    "Semana 2": {
        "Efectivo": 1420.75,
        "Tarjeta de crédito": 1130.40,
        "Transferencia": 720.30,
        "Pago móvil": 515.80,
    },
    "Semana 3": {
        "Efectivo": 1365.10,
        "Tarjeta de crédito": 1045.20,
        "Transferencia": 685.50,
        "Pago móvil": 455.60,
    },
    "Semana 4": {
        "Efectivo": 1520.90,
        "Tarjeta de crédito": 1185.00,
        "Transferencia": 755.75,
        "Pago móvil": 530.45,
    },
}


class IngresosApp(tk.Tk):
    def __init__(self) -> None:
        super().__init__()
        self.title("La tienda de Erick - Ingresos por método de pago")
        self.geometry("600x400")
        self.resizable(False, False)

        self._crear_widgets()
        self._cargar_semanas()

    def _crear_widgets(self) -> None:
        ttk.Label(
            self,
            text="Selecciona una semana:",
            font=("Segoe UI", 11, "bold"),
        ).pack(pady=(20, 10))

        self.combo_semana = ttk.Combobox(
            self,
            state="readonly",
            font=("Segoe UI", 10),
            width=25,
        )
        self.combo_semana.pack()
        self.combo_semana.bind("<<ComboboxSelected>>", self._on_semana_seleccionada)

        columnas = ("metodo", "ingreso")
        self.tabla = ttk.Treeview(
            self,
            columns=columnas,
            show="headings",
            height=8,
        )
        self.tabla.heading("metodo", text="Método de pago")
        self.tabla.heading("ingreso", text="Ingreso semanal")
        self.tabla.column("metodo", width=300, anchor=tk.W)
        self.tabla.column("ingreso", width=150, anchor=tk.E)
        self.tabla.pack(pady=20)

        self.total_var = tk.StringVar(value="Total semanal: 0.00")
        ttk.Label(
            self,
            textvariable=self.total_var,
            font=("Segoe UI", 11, "bold"),
        ).pack()

    def _cargar_semanas(self) -> None:
        semanas = list(INGRESOS_SEMANALES.keys())
        self.combo_semana["values"] = semanas
        if semanas:
            self.combo_semana.current(0)
            self._actualizar_tabla(semanas[0])

    def _on_semana_seleccionada(self, _event: tk.Event) -> None:
        semana = self.combo_semana.get()
        self._actualizar_tabla(semana)

    def _actualizar_tabla(self, semana: str) -> None:
        # Limpiar tabla
        for item in self.tabla.get_children():
            self.tabla.delete(item)

        ingresos = INGRESOS_SEMANALES.get(semana, {})
        total = 0.0

        for metodo, monto in ingresos.items():
            self.tabla.insert("", tk.END, values=(metodo, f"${monto:,.2f}"))
            total += monto

        self.total_var.set(f"Total semanal: ${total:,.2f}")


if __name__ == "__main__":
    app = IngresosApp()
    app.mainloop()

