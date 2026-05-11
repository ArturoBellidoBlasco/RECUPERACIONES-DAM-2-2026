package cifrarydescifrar;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Herramientas {

    // El IV debe tener 16 bytes para AES
    byte[] iv = new byte[16];
    byte[] hashKey;
    String RUTA_BASE;
    String FICHERO_CIFRADO;

    public Herramientas(String RUTA_BASE) {
        this.RUTA_BASE = RUTA_BASE;
        FICHERO_CIFRADO = RUTA_BASE + "nombre_cifrado.dat";
    }

    /**
     * Genera el hash SHA-256 a partir de una frase para usarlo como clave AES.
     */
    public void prepararClave(String frase) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        this.hashKey = sha.digest(frase.getBytes());
        // SHA-256 nos da 32 bytes, AES usará los 32 (AES-256)
    }

    /**
     * Cifra los datos y guarda tanto el IV como el contenido en un archivo.
     */
    public void cifrar(byte[] datos) throws Exception {
        // 1. Generar un IV aleatorio (Fundamental para seguridad en modo CBC)
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 2. Configurar Cipher
        SecretKeySpec secretKey = new SecretKeySpec(hashKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // 3. Cifrar
        byte[] datosCifrados = cipher.doFinal(datos);

        // 4. Guardar en archivo: Primero el IV (16 bytes) y luego los datos
        try (FileOutputStream fos = new FileOutputStream(FICHERO_CIFRADO)) {
            fos.write(iv);
            fos.write(datosCifrados);
        }
        System.out.println("Archivo cifrado guardado con éxito.");
    }

    /**
     * Lee el archivo, recupera el IV y descifra el contenido.
     */
    public byte[] descifrar() throws Exception {
        if (hashKey == null) throw new IllegalStateException("¡Clave no inicializada!");

        // 1. Leer todo el archivo
        byte[] contenidoCompleto = Files.readAllBytes(Paths.get(FICHERO_CIFRADO));

        // 2. Extraer el IV (los primeros 16 bytes)
        System.arraycopy(contenidoCompleto, 0, iv, 0, 16);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 3. Extraer el mensaje cifrado (el resto)
        int longitudCifrado = contenidoCompleto.length - 16;
        byte[] datosCifrados = new byte[longitudCifrado];
        System.arraycopy(contenidoCompleto, 16, datosCifrados, 0, longitudCifrado);

        // 4. Configurar Cipher para descifrado
        SecretKeySpec secretKey = new SecretKeySpec(hashKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        return cipher.doFinal(datosCifrados);
    }
}
