import java.io.*;
import java.net.Socket;

// HILO 3: MANEJADOR DE CLIENTE TCP
public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            // Streams de entrada y salida
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);

            // 2. Enviar mensaje de bienvenida
            pw.println("Bienvenido al Servidor Central.");
            pw.println("Comandos disponibles: ESTADO | SENSOR <tipo> <id> | SALIR");

            String comando;

            // 3. Bucle de comandos
            while ((comando = br.readLine()) != null) {
                comando = comando.trim().toUpperCase();

                if (comando.equals("SALIR")) {
                    pw.println("Cerrando la conexión. ¡Hasta luego!");
                    break; // Cierra el bucle, luego los streams
                }

                if (comando.equals("ESTADO")) {
                    pw.println(generarResumen());
                    continue;
                }

                // SENSOR tipo id
                if (comando.startsWith("SENSOR")) {
                    String[] partes = comando.split(" ");
                    if (partes.length == 3) {
                        String tipo = partes[1];
                        String id = partes[2];

                        String clave = tipo + "-" + id;

                        if (ServidorCentral.ultimosDatos.containsKey(clave)) {
                            pw.println("Último dato del sensor " + clave + ": " +
                                    ServidorCentral.ultimosDatos.get(clave));
                        } else {
                            pw.println("No existe un sensor con clave: " + clave);
                        }
                    } else {
                        pw.println("Formato incorrecto. Use: SENSOR <tipo> <id>");
                    }
                    continue;
                }

                // Si llega algo desconocido
                pw.println("Comando no reconocido: " + comando);
            }

        } catch (Exception e) {
            System.out.println("Error en ClientHandler: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    private String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADO DEL SISTEMA ===\n");
        sb.append("Total sensores: ").append(ServidorCentral.ultimosDatos.size()).append("\n\n");

        for (String clave : ServidorCentral.ultimosDatos.keySet()) {
            sb.append("Sensor ").append(clave)
                    .append(" => ")
                    .append(ServidorCentral.ultimosDatos.get(clave))
                    .append("\n");
        }

        return sb.toString();
    }
}
