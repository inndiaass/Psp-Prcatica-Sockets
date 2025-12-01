import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
        double valor;
        String tipo = this.tipo;
// TODO: Generar valor aleatorio según el tipo de sensor
// TEMPERATURA: 15.0 - 35.0
        if (tipo.equalsIgnoreCase("temperatura")){
            valor = 15.0 + (35.0 - 15.0) * random.nextDouble();
        } else if (tipo.equalsIgnoreCase("presion")) {
            valor = 980.0 + (1050.0 - 980.0) * random.nextDouble();
        }else {
            valor = 30.0 + (90.0 - 30.0) * random.nextDouble();
        }
// PRESION: 980.0 - 1050.0
// HUMEDAD: 30.0 - 90.0
        return valor;
    }
    private String construirMensaje(double valor) {
// TODO: Construir mensaje con formato TIPO|ID|VALOR|TIMESTAMP
        return "TIPO | " + tipo + " | ID | " + id + " | VALOR | " + valor + " | TIMESTAMP " + puerto;
    }
    public void iniciar() {
        DatagramSocket datagramSocket = null;
// TODO:
        try {
// 1. Crear DatagramSocket
            datagramSocket = new DatagramSocket();
// 2. Obtener InetAddress del servidor
            InetAddress inetAddress = InetAddress.getByName(servidor);
// 3. Bucle infinito:
            while (true) {

//    a. Generar valor
                double valor = generarValor();
//    b. Construir mensaje
                String mensaje = construirMensaje(valor);
//    c. Convertir a bytes
                byte[] buffer = mensaje.getBytes();
//    d. Crear DatagramPacket con destino (servidor, puerto)
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, puerto);
//    e. Enviar con socket.send()
                datagramSocket.send(datagramPacket);
//    f. Mostrar mensaje enviado
                System.out.println("Mensaje" + mensaje + "enviado al servidor");
//    g. Thread.sleep(1000)
                Thread.sleep(3000);
            }

// 4. Cerrar socket en finally
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }finally {
            if(datagramSocket.isClosed() || datagramSocket!= null){
                datagramSocket.close();
            }
        }
    }
    public static void main(String[] args) {
// TODO:
// 1. Validar que args.length == 4
        if(args.length != 4){
            System.out.println("Error, hay menos de 4 argumentos");
        }
// 2. Extraer parámetros: tipo, id, servidor, puerto
        String tipo = args[0];
        String id = args[1];
        String servidor = args[2];
        int puerto = Integer.parseInt(args[3]);
// 3. Crear instancia de Sensor
        Sensor sensor = new Sensor(tipo, id, servidor, puerto);
// 4. Llamar a iniciar()
        sensor.iniciar();
    }
}