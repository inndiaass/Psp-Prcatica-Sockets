import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorCentral {
    public static final int UDP_PORT = 5000;
    public static final int TCP_PORT = 6000;
    public static final String MULTICAST_ADDRESS = "239.0.0.1";
    public static final int MULTICAST_PORT = 4446;

    // Almacena último valor de cada sensor: clave = "TIPO|ID", valor = último dato
    public static ConcurrentHashMap<String, String> ultimosDatos = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Iniciando Servidor Central...");
        System.out.println("Puerto UDP: " + UDP_PORT);
        System.out.println("Puerto TCP: " + TCP_PORT);
        System.out.println("Dirección Multicast: " + MULTICAST_ADDRESS + ":" + MULTICAST_PORT);

        // 1. Crear y lanzar UDPReceiverThread
        UDPReceiverThread udpReceiver = new UDPReceiverThread();
        udpReceiver.start();
        System.out.println("UDPReceiverThread iniciado.");

        // 2. Crear y lanzar TCPServerThread
        TCPServerThread tcpServer = new TCPServerThread();
        tcpServer.start();
        System.out.println("TCPServerThread iniciado.");

        System.out.println("Servidor Central en funcionamiento.");
    }
}




