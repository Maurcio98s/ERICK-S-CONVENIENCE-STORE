def main() -> None:
    try:
        numero_1 = float(input("Ingresa el primer número: "))
        numero_2 = float(input("Ingresa el segundo número "))
    except ValueError:
        print("Entrada inválida. Asegúrate de ingresar números.")
        return

    resultado = numero_1 - numero_2
    print(f"La suma es: {resultado}")


if __name__ == "__main__":
    main()