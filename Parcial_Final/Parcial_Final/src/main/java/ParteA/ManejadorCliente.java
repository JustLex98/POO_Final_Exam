package ParteA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManejadorCliente implements Runnable {

    private final Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String nombre = null;

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            nombre = in.readLine();
            int numero = Integer.parseInt(in.readLine());

            System.out.println("Cliente " + nombre + " conectado");

            int cuadrado = numero * numero;
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHoraFormateada = ahora.format(formatter);

            out.println("¡Bienvenido, " + nombre + "!");
            out.println("El cuadrado de " + numero + " es: " + cuadrado);
            out.println("Fecha y hora del servidor: " + fechaHoraFormateada);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error de comunicación con el cliente: " + e.getMessage());
        } finally {
            if (nombre != null) {
                System.out.println("Cliente " + nombre + " desconectado");
            } else {
                System.out.println("Cliente anónimo desconectado");
            }

            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}