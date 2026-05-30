package intercambiodatos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Servidor {
    public static void main(String[] args) {
        int port = 5555;

        try(DatagramSocket socket = new DatagramSocket(port)) {
            socket.setSoTimeout(10000);

            while (true) {
                byte[] bufferPeticion = new byte[1024];
                DatagramPacket peticion = new DatagramPacket(bufferPeticion, bufferPeticion.length);

                socket.receive(peticion);

                System.out.println("El Cliente dice: " + new String(peticion.getData()).trim());

                String mensaje = "WAZAAAAAAAAAAAAAAAAAAAAAAAAAA";
                byte[] bufferRespuesta = mensaje.getBytes();
                DatagramPacket respuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length, peticion.getAddress(), peticion.getPort());

                socket.send(respuesta);

                System.out.println("Respuesta enviada");

            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
