package cifrarydescifrar;

import java.io.*;

public class Cifrar {

        // Usamos el separador del sistema para evitar errores en Linux/Windows
        private static final String RUTA_BASE = "archivos" + File.separator;


        public static void main(String[] args) {
            Herramientas app = new Herramientas(RUTA_BASE);

            try {
                String password = "mi_clave_secreta";
                String mensaje = "Este es un mensaje secreto para el examen de PSP.";

                app.prepararClave(password);
                app.cifrar(mensaje.getBytes());

            } catch (Exception e) {
                System.err.println("Error crítico: " + e.getMessage());
            }
        }



}
