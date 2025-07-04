package ParteA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ManejadorCliente implements Runnable {

    private Socket clienteSocket;
    private String direccionCliente;
    private int puertoCliente;

    public ManejadorCliente(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
        this.direccionCliente = clienteSocket.getInetAddress().getHostAddress();
        this.puertoCliente = clienteSocket.getPort();
    }

    @Override
    public void run() {
        BufferedReader entrada = null;
        PrintWriter salida = null;

        try {

            entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            salida = new PrintWriter(clienteSocket.getOutputStream(), true);

            System.out.println("Procesando cliente: " + direccionCliente + ":" + puertoCliente);

            String nombre = entrada.readLine();
            if (nombre == null || nombre.trim().isEmpty()) {
                salida.println("ERROR: Nombre no válido");
                return;
            }

            String numeroStr = entrada.readLine();
            if (numeroStr == null) {
                salida.println("ERROR: Número no recibido");
                return;
            }

            int numero;
            try {
                numero = Integer.parseInt(numeroStr.trim());
            } catch (NumberFormatException e) {
                salida.println("ERROR: El número debe ser un entero válido");
                return;
            }

            int cuadrado = numero * numero;

            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaHora = ahora.format(formatter);

            StringBuilder respuesta = new StringBuilder();
            respuesta.append("¡Bienvenido, ").append(nombre).append("!\n");
            respuesta.append("El cuadrado de ").append(numero).append(" es: ").append(cuadrado).append("\n");
            respuesta.append("Fecha y hora del servidor: ").append(fechaHora);

            salida.println(respuesta.toString());

            System.out.println("Cliente " + direccionCliente + ":" + puertoCliente
                    + " - Nombre: " + nombre + ", Número: " + numero
                    + ", Cuadrado: " + cuadrado);

        } catch (IOException e) {
            System.err.println("Error al procesar cliente " + direccionCliente + ":"
                    + puertoCliente + " - " + e.getMessage());
        } finally {

            try {
                if (entrada != null) {
                    entrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
                if (clienteSocket != null && !clienteSocket.isClosed()) {
                    clienteSocket.close();
                }
                System.out.println("Conexión cerrada con cliente: "
                        + direccionCliente + ":" + puertoCliente + "\n");
            } catch (IOException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
}
