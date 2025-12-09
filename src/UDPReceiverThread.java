import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//HILO 1: RECEPTOR UDP
public class UDPReceiverThread extends Thread {
    @Override
    public void run() {
        try {
            // 1. Crear DatagramSocket en puerto UDP_PORT
            DatagramSocket datagramSocket = new DatagramSocket(ServidorCentral.UDP_PORT);
            System.out.println("Socket UDP escuchando en puerto " + ServidorCentral.UDP_PORT);

            // 2. Crear MulticastSocket para enviar alertas
            MulticastSocket multicastSocket = new MulticastSocket();

            // 3. Obtener InetAddress del grupo multicast
            InetAddress grupoMulticast = InetAddress.getByName(ServidorCentral.MULTICAST_ADDRESS);

            // 4. Bucle infinito:
            byte[] buffer = new byte[1024];
            while (true) {
                // a. Recibir DatagramPacket
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);

                // b. Convertir a String y parsear (split "|")
                String mensaje = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                String[] partes = mensaje.split("\\|");

                if (partes.length >= 3) {
                    String tipo = partes[0];
                    String id = partes[1];
                    String valorStr = partes[2];
                    double valor = Double.parseDouble(valorStr);

                    // c. Guardar en ultimosDatos
                    String clave = tipo + "|" + id;
                    ServidorCentral.ultimosDatos.put(clave, mensaje);

                    // d. Verificar si hay alerta
                    if (esAlerta(tipo, valor)) {
                        // e. Si hay alerta, enviar por multicast
                        enviarAlerta(multicastSocket, grupoMulticast, tipo, id, valor);
                    }

                    // f. Mostrar dato recibido en consola
                    System.out.println("Dato recibido: " + mensaje);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en UDPReceiverThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean esAlerta(String tipo, double valor) {
        // Verificar umbrales según tipo
        switch (tipo.toUpperCase()) {
            case "TEMPERATURA":
                return valor > 35.0 || valor < 0.0;
            case "HUMEDAD":
                return valor > 80.0 || valor < 20.0;
            case "PRESION":
                return valor > 1050.0 || valor < 950.0;
            case "CO2":
                return valor > 1000.0;
            case "LUZ":
                return valor > 10000.0 || valor < 100.0;
            default:
                return false;
        }
    }

    private void enviarAlerta(MulticastSocket mSocket, InetAddress group, String tipo, String id, double valor) {
        try {
            // Formato: ALERTA|TIPO|ID|VALOR|TIMESTAMP
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String mensajeAlerta = "ALERTA|" + tipo + "|" + id + "|" + valor + "|" + timestamp;

            byte[] datos = mensajeAlerta.getBytes();
            DatagramPacket paquete = new DatagramPacket(datos, datos.length, group, ServidorCentral.MULTICAST_PORT);
            mSocket.send(paquete);

            System.out.println("ALERTA ENVIADA: " + mensajeAlerta);
        } catch (Exception e) {
            System.err.println("Error al enviar alerta: " + e.getMessage());
        }
    }
}
