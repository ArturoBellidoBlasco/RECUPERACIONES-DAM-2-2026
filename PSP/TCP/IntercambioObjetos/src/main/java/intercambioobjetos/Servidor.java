package intercambioobjetos;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Servidor {
    public static void main(String[] args) {
        int port = 5555;

        try (ServerSocket socket = new ServerSocket(port)){

            socket.setSoTimeout(10000);

            while (true) {
                try (Socket cliente = socket.accept();
                     ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());){


                    ObjetoSerializable objetoBytesRecibir = (ObjetoSerializable) in.readObject();

                    System.out.println("El cliente dice: " + objetoBytesRecibir.getMensaje());

                    ObjetoSerializable mensagem = new ObjetoSerializable(2, "Hola Cliente");
                    out.writeObject(mensagem);

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Tiempo de espera terminado");
            System.out.println("Apagando...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
