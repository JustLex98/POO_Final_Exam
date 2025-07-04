package ParteB;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author frivera
 */

public class ClienteCuadrado {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    
    public static void main(String[] args) {
        System.out.println("=== CLIENTE CUADRADO ===");
        System.out.println("Conectando al servidor " + HOST + ":" + PUERTO + "...");
        
        Socket socket = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;
        Scanner scanner = new Scanner(System.in);
        
        try {
            socket = new Socket(HOST, PUERTO);
            System.out.println("Conexión establecida exitosamente!");
            
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.print("\nIngrese su nombre: ");
            String nombre = scanner.nextLine().trim();
            
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacio");
                return;
            }
            
            System.out.print("Ingrese un número entero: ");
            String numeroStr = scanner.nextLine().trim();
            
         
            int numero;
            try {
                numero = Integer.parseInt(numeroStr);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero valido");
                return;
            }
            
            System.out.println("\nEnviando datos al servidor...");
            salida.println(nombre);
            salida.println(numero);
            
            System.out.println("Esperando respuesta del servidor...\n");
            
            String linea;
            System.out.println("=== RESPUESTA DEL SERVIDOR ===");

            while ((linea = entrada.readLine()) != null) {
                System.out.println(linea);
                
         
                if (linea.contains("Fecha y hora del servidor:")) {
                    break;
                }
            }
            
            System.out.println("Operación completada");
            
        } catch (UnknownHostException e) {
            System.err.println("Error: No se pudo encontrar el host " + HOST);
            System.err.println("Verifique que el servidor esté ejecutándose");
        } catch (ConnectException e) {
            System.err.println("Error: No se pudo conectar al servidor en " + HOST + ":" + PUERTO);
            System.err.println("Verifique que el servidor esté ejecutándose y el puerto esté disponible");
        } catch (IOException e) {
            System.err.println("Error de comunicación: " + e.getMessage());
        } finally {
           
            try {
                if (entrada != null) {
                    entrada.close();
                    System.out.println("Flujo de entrada cerrado");
                }
                if (salida != null) {
                    salida.close();
                    System.out.println("Flujo de salida cerrado");
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Conexión cerrada");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
            scanner.close();
        }
        
        System.out.println("\nCliente terminado.");
    }
}