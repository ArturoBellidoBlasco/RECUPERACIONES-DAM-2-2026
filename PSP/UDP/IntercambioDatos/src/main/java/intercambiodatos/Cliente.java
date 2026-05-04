package intercambiodatos;

import java.io.IOException;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        int puerto=5555;
        String host = "localhost";

        try(DatagramSocket socket = new DatagramSocket()){

            String mensaje = "METEREKETENGE";
            byte[] bufferPeticion = mensaje.getBytes();
            DatagramPacket peticion = new DatagramPacket(bufferPeticion, bufferPeticion.length, InetAddress.getByName(host),puerto);

            socket.send(peticion);

            System.out.println("Peticion enviada");

            byte[] bufferRespuesta = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);

            socket.receive(respuesta);

            System.out.println("El Servidor dice: " + new String(respuesta.getData()).trim());

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
