import java.util.Random;

public class Sensor {
    private String tipo;
    private String id;
    private String servidor;
    private int puerto;
    private Random random;
    public Sensor(String tipo, String id, String servidor, int puerto) {
        this.tipo = tipo;
        this.id = id;
        this.servidor = servidor;
        this.puerto = puerto;
    }
    private double generarValor() {
// TODO: Generar valor aleatorio según el tipo de sensor
// TEMPERATURA: 15.0 - 35.0
// PRESION: 980.0 - 1050.0
// HUMEDAD: 30.0 - 90.0
        return 0.0;
    }
    private String construirMensaje(double valor) {
// TODO: Construir mensaje con formato TIPO|ID|VALOR|TIMESTAMP
        return "";
    }
    public void iniciar() {
// TODO:
// 1. Crear DatagramSocket
// 2. Obtener InetAddress del servidor
// 3. Bucle infinito:
//    a. Generar valor
//    b. Construir mensaje
//    c. Convertir a bytes
//    d. Crear DatagramPacket con destino (servidor, puerto)
//    e. Enviar con socket.send()
//    f. Mostrar mensaje enviado
//    g. Thread.sleep(1000)
// 4. Cerrar socket en finally
    }
    public static void main(String[] args) {
// TODO:
// 1. Validar que args.length == 4
// 2. Extraer parámetros: tipo, id, servidor, puerto
// 3. Crear instancia de Sensor
// 4. Llamar a iniciar()
    }
}