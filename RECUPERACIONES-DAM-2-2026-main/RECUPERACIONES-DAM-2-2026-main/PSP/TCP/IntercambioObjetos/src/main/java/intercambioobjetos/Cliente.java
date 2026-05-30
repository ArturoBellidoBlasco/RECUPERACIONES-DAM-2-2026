package intercambioobjetos;

import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {

        int port = 5555;
        String host = "localhost";

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());){

            ObjetoSerializable mensagem = new ObjetoSerializable(1, "Hola servidor");

            out.writeObject(mensagem);

           ObjetoSerializable objetoSerializableRecibido = (ObjetoSerializable) in.readObject();
            System.out.println("El servidor dice: "+objetoSerializableRecibido.getMensaje());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
