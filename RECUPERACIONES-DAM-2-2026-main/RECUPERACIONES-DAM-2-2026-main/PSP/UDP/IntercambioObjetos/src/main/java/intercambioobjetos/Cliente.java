package intercambioobjetos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Cliente {
    public static void main(String[] args) {
        int port = 5555;
        String host = "localhost";

        try(DatagramSocket socket = new DatagramSocket()) {

                String mensajeServidor = "TElEkE";
                byte[] bufferRespuesta = mensajeServidor.getBytes();
                DatagramPacket respuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length, InetAddress.getByName(host), port);
                socket.send(respuesta);

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String mensaje = new String(packet.getData(), 0, packet.getLength());
                System.out.println(mensaje.trim());


        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
