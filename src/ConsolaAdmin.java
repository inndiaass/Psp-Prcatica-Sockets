import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConsolaAdmin {
    private String servidor;
    private int puerto;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private Scanner scanner;

    public ConsolaAdmin(String servidor, int puerto) {
        // Inicializar variables de conexión
        this.servidor = servidor;
        this.puerto = puerto;
    }

    public void conectar() throws IOException {
        System.out.println("Conectando a " + servidor + ":" + puerto + "...");

        // 1. Crear Socket conectando a servidor:puerto
        socket = new Socket(servidor, puerto);

        // 2. Crear BufferedReader del InputStream
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 3. Crear PrintWriter del OutputStream con autoFlush (Importante: true)
        pw = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Conectado exitosamente!\n");

        // 4. Leer mensaje de bienvenida del servidor
        // Nota: Asumimos que el servidor envía una línea de bienvenida al conectar.
        // Si el servidor envía más líneas al inicio, esto debería estar en un bucle similar a leerRespuesta().
        if (br.ready()) {
            String bienvenida = br.readLine();
            if (bienvenida != null) {
                System.out.println(bienvenida);
            }
        }
    }

    public void mostrarMenu() {
        System.out.println("\nComandos disponibles:");
        System.out.println("  ESTADO              - Ver resumen de todos los sensores");
        System.out.println("  SENSOR <tipo> <id>  - Ver datos de sensor específico");
        System.out.println("  AYUDA               - Mostrar esta ayuda");
        System.out.println("  SALIR               - Cerrar conexión\n");
    }

    public void ejecutar() {
        // 1. Crear Scanner para leer del usuario
        scanner = new Scanner(System.in);

        // 2. Bucle infinito
        try {
            while (true) {
                // a. Mostrar prompt
                System.out.print("admin> ");

                // b. Leer comando del usuario
                String comando = scanner.nextLine().trim();

                // Validación básica para no enviar líneas vacías
                if (comando.isEmpty()) continue;

                // c. Si es "SALIR", enviar al servidor y break
                if (comando.equalsIgnoreCase("SALIR")) {
                    pw.println(comando);
                    break;
                }

                // d. Si es "AYUDA", mostrar menú (no se envía al servidor)
                if (comando.equalsIgnoreCase("AYUDA")) {
                    mostrarMenu();
                    continue; // Vuelve al inicio del bucle sin contactar al servidor
                }

                // e. Si es otro comando, enviarlo al servidor
                pw.println(comando);

                // f. Leer respuesta del servidor (puede ser multilínea)
                String respuesta = leerRespuesta();

                // g. Mostrar respuesta
                System.out.println(respuesta);
            }
        } catch (IOException e) {
            System.err.println("Error durante la comunicación: " + e.getMessage());
        } finally {
            // 3. Cerrar recursos
            cerrar();
        }
    }

    private String leerRespuesta() throws IOException {
        StringBuilder respuesta = new StringBuilder();
        String linea;

        // Leemos línea por línea
        while ((linea = br.readLine()) != null) {
            // Condición de parada según el enunciado: línea vacía o "---FIN---"
            // Nota: Si el protocolo del servidor usa una línea vacía real como separador de datos, 
            // la lógica aquí debería ajustarse para detectar solo el final de la transmisión.
            // Basado en el TODO:
            if (linea.isEmpty() || linea.equals("---FIN---")) {
                break;
            }
            respuesta.append(linea).append("\n");
        }
        return respuesta.toString();
    }

    public void cerrar() {
        try {
            System.out.println("Cerrando conexión...");
            if (pw != null) pw.close();
            if (br != null) br.close();
            if (socket != null && !socket.isClosed()) socket.close();
            if (scanner != null) scanner.close();
            System.out.println("¡Hasta pronto!");
        } catch (IOException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // 1. Validar args (servidor y puerto)
        if (args.length < 2) {
            System.out.println("Uso: java ConsolaAdmin <servidor> <puerto>");
            System.out.println("Ejemplo: java ConsolaAdmin 192.168.16.10 6000");
            return;
        }

        String host = args[0];
        int port;

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("El puerto debe ser un número entero.");
            return;
        }

        // 2. Crear instancia de ConsolaAdmin
        ConsolaAdmin consola = new ConsolaAdmin(host, port);

        try {
            System.out.println("=== CONSOLA DE ADMINISTRACIÓN ===");
            // 3. Conectar al servidor
            consola.conectar();

            // 4. Mostrar menú
            consola.mostrarMenu();

            // 5. Ejecutar bucle de comandos
            consola.ejecutar();

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            System.err.println("Asegúrate de que el servidor esté encendido.");
        }
        // El método ejecutar() ya llama a cerrar() en su bloque finally, 
        // pero si falla conectar(), no entra a ejecutar.
    }
}