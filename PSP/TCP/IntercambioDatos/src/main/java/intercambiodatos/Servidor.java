package intercambiodatos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Servidor {
    public static void main(String[] args) {

        int port = 5555;

        try (ServerSocket socket = new ServerSocket(port)){

            socket.setSoTimeout(10000);

            while (true) {
                try (Socket cliente = socket.accept();
                     PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));){

                    System.out.println("El cliente dice: " + in.readLine());
                    String mensagem = "Hola cliente!";
                    out.println(mensagem);
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
