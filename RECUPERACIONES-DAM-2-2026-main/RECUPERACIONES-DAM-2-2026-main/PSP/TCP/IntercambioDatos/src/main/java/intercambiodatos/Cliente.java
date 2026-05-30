package intercambiodatos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public static void main(String[] args) {

        int port = 5555;
        String host = "localhost";

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){

            String mensagem = "Hola servidor!";
            out.println(mensagem);

            System.out.println("El servidor dice: ");
            System.out.println(in.readLine());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
