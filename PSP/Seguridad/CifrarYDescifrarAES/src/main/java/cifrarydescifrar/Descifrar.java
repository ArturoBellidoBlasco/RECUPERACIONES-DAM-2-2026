package cifrarydescifrar;

import java.io.File;

public class Descifrar {

    private static final String RUTA_BASE = "archivos" + File.separator;

    public static void main(String[] args) {

        Herramientas app = new Herramientas(RUTA_BASE);
        String password = "mi_clave_secreta";
        try {
            app.prepararClave(password);
            byte[] desencriptado = app.descifrar();
            System.out.println("Resultado: " + new String(desencriptado));

        } catch (Exception e) {
            System.err.println("Error crítico: " + e.getMessage());
        }

    }
}
