public class PanelOperador {
    private static final String MULTICAST_ADDRESS = "239.0.0.1";
    private static final int MULTICAST_PORT = 4446;
    private String nombreOperador;
    private int contadorAlertas = 0;
    // Códigos ANSI para colores (opcional)
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    public PanelOperador(String nombreOperador) {
// TODO: Inicializar nombre del operador
    }
    public void iniciar() {
        // TODO:
        // 1. Crear MulticastSocket en puerto MULTICAST_PORT
        // 2. Obtener InetAddress del grupo
        // 3. Unirse al grupo con joinGroup()
        // 4. Mostrar mensaje de conexión
        // 5. Bucle infinito:
        //    a. Crear DatagramPacket con buffer
        //    b. Recibir con socket.receive()
        //    c. Convertir a String
        //    d. Parsear mensaje (split "|")
        //    e. Incrementar contador
        //    f. Mostrar alerta con formato destacado

        // 6. En finally: abandonar grupo y cerrar socket
    }
    private void mostrarAlerta(String tipo, String id, double valor, String
            timestamp) {
// TODO: Mostrar alerta con formato visual
// Usar colores si está disponible
// Incluir contador de alertas
    }
    private String obtenerNivelAlerta(String tipo, double valor) {
// TODO: Determinar si es alerta MEDIA o CRÍTICA
// Retornar "MEDIA" o "CRÍTICA"
        return "";
    }
    public static void main(String[] args) {
// TODO:
// 1. Validar o solicitar nombre del operador
// 2. Crear instancia de PanelOperador
// 3. Llamar a iniciar()
    }
}