package ParteA;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorCuadrado {

    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR CUADRADO ===");
        System.out.println("Iniciando servidor en el puerto " + PUERTO + "...");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado exitosamente!");
            System.out.println("Esperando conexiones de clientes...\n");

            while (true) {
                try {

                    Socket clienteSocket = serverSocket.accept();

                    String direccionCliente = clienteSocket.getInetAddress().getHostAddress();
                    int puertoCliente = clienteSocket.getPort();

                    System.out.println("Nueva conexión establecida desde: "
                            + direccionCliente + ":" + puertoCliente);

                    ManejadorCliente manejador = new ManejadorCliente(clienteSocket);
                    Thread hiloCliente = new Thread(manejador);
                    hiloCliente.start();

                } catch (IOException e) {
                    System.err.println("Error al aceptar conexión de cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
