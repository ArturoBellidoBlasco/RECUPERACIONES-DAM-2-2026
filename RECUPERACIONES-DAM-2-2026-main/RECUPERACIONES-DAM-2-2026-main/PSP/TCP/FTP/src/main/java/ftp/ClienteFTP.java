package ftp;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

public class ClienteFTP {
    public static void main(String[] args) {
        FTPSClient ftpsClient = new FTPSClient("TLS", false);

        try {
            ftpsClient.setTrustManager(new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            });

            //ftpsClient.connect("192.168.50.244");
            ftpsClient.connect("eu-central-1.sftpcloud.io");
            if (!FTPReply.isPositiveCompletion(ftpsClient.getReplyCode())) {
                ftpsClient.disconnect();
                System.out.println("No se pudo conectar al servidor");
                return;
            }

            System.out.println("Codigo de respuesta del servidor esperado: 234");
            System.out.println("Codigo de respuesta del servidor recibido: " + ftpsClient.getReplyCode());

            //ftpsClient.login("smr1","VonNeumann1945");
            //ftpsClient.login("anonymous","anonymous@");
            String username = "2d5325ccb84b4723b8958d1d29c73908";
            String password = "msar0MbLksE1Cv9qnGGtxZTDQ1qtGTQp";
            ftpsClient.login(username, password);



            ftpsClient.execPBSZ(0);
            ftpsClient.execPROT("P");
            ftpsClient.enterLocalPassiveMode();

            System.out.println("¿Sesión iniciada con exito? ");
            System.out.println(ftpsClient.isConnected());

            System.out.println("Carpetas ubicadas en el servidor...");
            FTPFile[] files = ftpsClient.listFiles();
            for (FTPFile file : files) {
                System.out.println(file.getName());
            }

            Scanner sc = new Scanner(System.in);

            // Cambiar de directorio
            System.out.println("¿A qué directorio quieres moverte?");
            String path = sc.nextLine().trim();
            if (!path.isEmpty()) {
                ftpsClient.changeWorkingDirectory(path);
                files = ftpsClient.listFiles();
                for (FTPFile file : files) {
                    System.out.println(file.getName());
                }
            }

            System.out.println("1 - Descargar un fichero");
            System.out.println("2 - Subir un fichero");
            System.out.println("Otro - Cerrar conexión");

            int opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1: // DESCARGAR Y DESCIFRAR
                    System.out.println("¿Qué fichero quieres descargar?");
                    String fileDescargar = sc.nextLine().trim();
                    System.out.print("Introduce la contraseña de descifrado: ");
                    char[] passDescarga = System.console().readPassword();

                    File destino = new File(System.getProperty("user.home") + "/Downloads/" + fileDescargar);

                    try (FileOutputStream fos = new FileOutputStream(destino)) {
                        ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);

                        // 1. Necesitamos obtener el IV del servidor primero
                        // En este ejemplo simplificado, descargamos y luego desciframos
                        // Para hacerlo "al vuelo" es más complejo, así que bajaremos el stream

                        ftpsClient.retrieveFile(fileDescargar, Files.newOutputStream(Paths.get(destino + ".tmp")));

                        // Proceso de descifrado local del temporal
                        try (FileInputStream fisTmp = new FileInputStream(destino + ".tmp");
                             FileOutputStream fosFinal = new FileOutputStream(destino)) {

                            byte[] iv = new byte[16];
                            fisTmp.read(iv); // Leer los primeros 16 bytes (el IV)

                            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                            cipher.init(Cipher.DECRYPT_MODE, prepararClave(passDescarga), new IvParameterSpec(iv));

                            CipherInputStream cis = new CipherInputStream(fisTmp, cipher);
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = cis.read(buffer)) != -1) {
                                fosFinal.write(buffer, 0, bytesRead);
                            }
                        }
                        new File(destino + ".tmp").delete();
                        System.out.println("DESCARGA Y DESCIFRADO OK.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2: // CIFRAR Y SUBIR
                    System.out.println("Escriba la ruta del archivo a subir:");
                    String rutaSubida = sc.nextLine().trim();
                    File fSubir = new File(rutaSubida);

                    if (fSubir.exists()) {
                        System.out.print("Introduce una contraseña para cifrar el archivo: ");
                        char[] passSubida = System.console().readPassword();

                        try {
                            // 1. Configurar Cifrado
                            byte[] iv = new byte[16];
                            new SecureRandom().nextBytes(iv); // Generar IV aleatorio (Pág. 21)
                            IvParameterSpec ivSpec = new IvParameterSpec(iv);

                            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                            cipher.init(Cipher.ENCRYPT_MODE, prepararClave(passSubida), ivSpec);

                            // 2. Crear un archivo temporal cifrado para subir
                            File fCifrado = new File(fSubir.getAbsolutePath() + ".cif");
                            try (FileInputStream fis = new FileInputStream(fSubir);
                                 FileOutputStream fos = new FileOutputStream(fCifrado)) {

                                fos.write(iv); // Guardamos el IV al principio
                                CipherOutputStream cos = new CipherOutputStream(fos, cipher);

                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = fis.read(buffer)) != -1) {
                                    cos.write(buffer, 0, bytesRead);
                                }
                                cos.flush();
                                cos.close();
                            }

                            // 3. Subir el archivo ya cifrado
                            try (FileInputStream fisCifrado = new FileInputStream(fCifrado)) {
                                ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
                                if (ftpsClient.storeFile(fSubir.getName() + ".enc", fisCifrado)) {
                                    System.out.println("SUBIDA CIFRADA OK.");
                                }
                            }
                            fCifrado.delete(); // Borrar temporal
                            java.util.Arrays.fill(passSubida, ' '); // Limpiar RAM (Pág. 25)

                        } catch (Exception e) {
                            System.out.println("ERROR EN CIFRADO/SUBIDA: " + e.getMessage());
                        }
                    }
                    break;

                default:
                    System.out.println("Opción salir seleccionada.");
                    break;
            }

            System.out.println("Finalizando protocolo FTP...");
            ftpsClient.logout();
            if (ftpsClient.isConnected()) {
                ftpsClient.disconnect();
            }
            System.out.println("Desconectado.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKeySpec prepararClave(char[] password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(new String(password).getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, "AES");
    }
}
