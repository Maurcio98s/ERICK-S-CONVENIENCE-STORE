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


DEUDAS_PROVEEDORES = {
    "Distribuidora Andina": {
        "Enero 2025": 3210.75,
        "Febrero 2025": 2985.40,
        "Marzo 2025": 3420.10,
    },
    "Sabores Latinos": {
        "Enero 2025": 1875.00,
        "Febrero 2025": 2130.55,
        "Marzo 2025": 2050.00,
    },
    "Frutas Selectas": {
        "Enero 2025": 950.80,
        "Febrero 2025": 1105.25,
        "Marzo 2025": 985.60,
    },
    "Lácteos del Valle": {
        "Enero 2025": 1640.30,
        "Febrero 2025": 1525.45,
        "Marzo 2025": 1710.90,
    },
    "Panadería Santa Ana": {
        "Enero 2025": 730.25,
        "Febrero 2025": 840.00,
        "Marzo 2025": 795.50,
    },
}


class IngresosApp(tk.Tk):
    def __init__(self) -> None:
        super().__init__()
        self.title("La tienda de Erick - Panel financiero")
        self.geometry("640x460")
        self.resizable(False, False)

        self._crear_widgets()
        self._cargar_semanas()
        self._cargar_proveedores()

    def _crear_widgets(self) -> None:
        self.notebook = ttk.Notebook(self)
        self.notebook.pack(fill=tk.BOTH, expand=True, padx=12, pady=12)

        self.tab_ingresos = ttk.Frame(self.notebook, padding=10)
        self.tab_deudas = ttk.Frame(self.notebook, padding=10)

        self.notebook.add(self.tab_ingresos, text="Ingresos semanales")
        self.notebook.add(self.tab_deudas, text="Deudas con proveedores")

        self._crear_tab_ingresos()
        self._crear_tab_deudas()

    def _crear_tab_ingresos(self) -> None:
        ttk.Label(
            self.tab_ingresos,
            text="Selecciona una semana:",
            font=("Segoe UI", 11, "bold"),
        ).pack(pady=(10, 8))

        self.combo_semana = ttk.Combobox(
            self.tab_ingresos,
            state="readonly",
            font=("Segoe UI", 10),
            width=25,
        )
        self.combo_semana.pack()
        self.combo_semana.bind("<<ComboboxSelected>>", self._on_semana_seleccionada)

        columnas = ("metodo", "ingreso")
        self.tabla = ttk.Treeview(
            self.tab_ingresos,
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
            self.tab_ingresos,
            textvariable=self.total_var,
            font=("Segoe UI", 11, "bold"),
        ).pack(pady=(0, 10))

    def _crear_tab_deudas(self) -> None:
        instrucciones = (
            "Selecciona un proveedor y un mes para consultar la deuda registrada."
        )
        ttk.Label(
            self.tab_deudas,
            text=instrucciones,
            font=("Segoe UI", 10),
            wraplength=520,
            justify=tk.LEFT,
        ).grid(row=0, column=0, columnspan=2, sticky="w", pady=(0, 15))

        ttk.Label(
            self.tab_deudas,
            text="Proveedor:",
            font=("Segoe UI", 10, "bold"),
        ).grid(row=1, column=0, sticky="w")
        self.combo_proveedor = ttk.Combobox(
            self.tab_deudas,
            state="readonly",
            font=("Segoe UI", 10),
            width=30,
        )
        self.combo_proveedor.grid(row=1, column=1, sticky="ew", padx=(10, 0))

        ttk.Label(
            self.tab_deudas,
            text="Mes:",
            font=("Segoe UI", 10, "bold"),
        ).grid(row=2, column=0, sticky="w", pady=(10, 0))
        self.combo_mes = ttk.Combobox(
            self.tab_deudas,
            state="readonly",
            font=("Segoe UI", 10),
            width=20,
        )
        self.combo_mes.grid(row=2, column=1, sticky="ew", padx=(10, 0), pady=(10, 0))

        self.combo_proveedor.bind(
            "<<ComboboxSelected>>", self._on_proveedor_mes_cambiado
        )
        self.combo_mes.bind("<<ComboboxSelected>>", self._on_proveedor_mes_cambiado)

        self.deuda_var = tk.StringVar(value="Selecciona un proveedor y un mes.")
        ttk.Label(
            self.tab_deudas,
            textvariable=self.deuda_var,
            font=("Segoe UI", 11, "bold"),
        ).grid(row=3, column=0, columnspan=2, sticky="w", pady=(20, 0))

        self.tab_deudas.grid_columnconfigure(0, weight=0)
        self.tab_deudas.grid_columnconfigure(1, weight=1)

    def _cargar_semanas(self) -> None:
        semanas = list(INGRESOS_SEMANALES.keys())
        self.combo_semana["values"] = semanas
        if semanas:
            self.combo_semana.current(0)
            self._actualizar_tabla(semanas[0])

    def _cargar_proveedores(self) -> None:
        proveedores = list(DEUDAS_PROVEEDORES.keys())
        meses = sorted(
            {mes for datos in DEUDAS_PROVEEDORES.values() for mes in datos.keys()}
        )
        self.combo_proveedor["values"] = proveedores
        self.combo_mes["values"] = meses

        if proveedores:
            self.combo_proveedor.current(0)
        if meses:
            self.combo_mes.current(0)

        if proveedores and meses:
            self._on_proveedor_mes_cambiado()

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

    def _on_proveedor_mes_cambiado(self, _event: tk.Event | None = None) -> None:
        proveedor = self.combo_proveedor.get()
        mes = self.combo_mes.get()

        if not proveedor or not mes:
            self.deuda_var.set("Selecciona un proveedor y un mes.")
            return

        monto = DEUDAS_PROVEEDORES.get(proveedor, {}).get(mes)

        if monto is None:
            self.deuda_var.set("Sin registros de deuda para esta combinación.")
        else:
            self.deuda_var.set(f"Deuda del mes: ${monto:,.2f}")


if __name__ == "__main__":
    app = IngresosApp()
    app.mainloop()