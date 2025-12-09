import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


//HILO 2: SERVIDOR TCP
public class TCPServerThread extends Thread {
    @Override
    public void run() {
        // TODO:
        // 1. Crear ServerSocket en puerto TCP_PORT
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(ServidorCentral.TCP_PORT);

        // 2. Bucle infinito:
            while(true) {
                //    a. Aceptar conexión con accept()
                Socket cliente = serverSocket.accept();
                //    b. Crear ClientHandler para manejar la conexión
                ClientHandler clientHandler = new ClientHandler(cliente);
                //    c. Lanzar ClientHandler en nuevo hilo
                clientHandler.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}