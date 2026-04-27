package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

public class ServidorHTTP {
    public static void main(String[] args) {
        int puerto = 8080;

        try {
            HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);

            servidor.createContext("/", new HandlerBienvenida());
            servidor.createContext("/hora", new HandlerHora());
            servidor.createContext("/info", new HandlerInfo());

            //Configurar un Executor para atender peticiones de forma concurrente
            //Esto permite que el servidor maneje múltiples clientes a la vez
            servidor.setExecutor(Executors.newFixedThreadPool(10));

            System.out.println("Servidor iniciado en el puerto " + puerto);
            servidor.start();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // --- Manejador para la Ruta Raíz (/) ---
    static class HandlerBienvenida implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/")) {
                // Si es la raíz, enviamos el OK
                String respuesta = "<html><body><h1>Bienvenido al Servidor Java</h1></body></html>";
                enviarRespuesta(exchange, respuesta, 200);
            } else {
                String respuesta = "<h1>404 Not Found</h1><p>La ruta " + path + " no existe.</p>";
                enviarRespuesta(exchange, respuesta, 404);
                System.out.println("Petición fallida a: " + path + " (404 enviado)");
            }
        }
    }

    // --- Manejador para la Ruta /hora ---
    static class HandlerHora implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/hora")) {
                String horaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                String respuesta = "Fecha y hora actual: " + horaActual;
                enviarRespuesta(exchange, respuesta, 200);
            }else {
                String respuesta = "<h1>404 Not Found</h1><p>La ruta " + path + " no existe.</p>";
                enviarRespuesta(exchange, respuesta, 404);
                System.out.println("Petición fallida a: " + path + " (404 enviado)");
            }
        }
    }

    // --- Manejador para la Ruta /info ---
    static class HandlerInfo implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/info")) {

                String os = System.getProperty("os.name");
                String nombreEquipo = InetAddress.getLocalHost().getHostName();
                String respuesta = "Informacion del sistema:\n- Equipo: " + nombreEquipo + "\n- SO: " + os;
                enviarRespuesta(exchange, respuesta, 200);

            }else {
                String respuesta = "<h1>404 Not Found</h1><p>La ruta " + path + " no existe.</p>";
                enviarRespuesta(exchange, respuesta, 404);
                System.out.println("Petición fallida a: " + path + " (404 enviado)");
            }
        }
    }

    // Met odo auxiliar para enviar respuestas y cerrar el flujo
    private static void enviarRespuesta(HttpExchange exchange, String respuesta, int codigo) throws IOException {
        byte[] bytes = respuesta.getBytes();
        exchange.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
