package edu.escuelaing.app.AppSvr.server;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.HashMap;
import edu.escuelaing.app.AppSvr.server.DefaultResponse;
import edu.escuelaing.app.AppSvr.EciBoot;

public class HttpServer {
    private static Map<String, BiFunction<Request, String, String>> servicios = new HashMap<>();
    private static String staticFilePath = "target/classes/archivesPractice"; // o se puede usar la ruta src/main/resources/archivesPractice
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("Servidor HTTP corriendo en el puerto 35000");

        EciBoot.loadComponents();

        boolean running = true;
        while (running) {
            Socket clientSocket = serverSocket.accept();
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            boolean isFirstLine = true;
            String file = "";
            String queryString = "";

            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] parts = inputLine.split(" ");
                    file = parts[1];
                    if (file.contains("?")) {
                        queryString = file.split("\\?")[1];
                        file = file.split("\\?")[0];
                    }
                    isFirstLine = false;
                }
                if (!in.ready()) break;
            }

            Request request = new Request(queryString);
            System.out.println("Solicitud recibida: " + file + " | Query: " + queryString);
            if (isStaticFile(file)) {
                System.out.println("Sirviendo archivo estático: " + file);
                serveStaticFiles(file, out);
            }
            else {
                String response;
                if (servicios.containsKey(file)) {
                    response = processRequest(file, request);
                    System.out.println("Respuesta desde servicio registrado: " + response);
                }
                else if (EciBoot.services.containsKey(file)) {
                    response = EciBoot.simulateRequest(file);
                    System.out.println("Respuesta desde EciBoot: " + response);
                }
                else {
                    response = "HTTP/1.1 404 Not Found\r\n\r\n{\"error\": \"Recurso no encontrado\"}";
                    System.out.println("Recurso no encontrado: " + file);
                }
                out.write(response.getBytes());
                out.close();
            }
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static boolean isStaticFile(String file) {
        return file.endsWith(".html") || file.endsWith(".css") || file.endsWith(".js") ||
                file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".jpeg");
    }

    public static void get(String route, BiFunction<Request, String, String> f) {
        servicios.put(route, f);
    }

    public static void staticfiles(String path) {
        staticFilePath = path;
    }

    private static void serveStaticFiles(String filePath, OutputStream out) throws IOException {
        String basePath = "target/classes/archivesPractice"; // o se puede usar la ruta src/main/resources/archivesPractice
        File requestedFile = new File(basePath + filePath);
        String contentType = determineContentType(filePath);

        if (requestedFile.exists() && requestedFile.isFile()) {
            String header = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "\r\n";
            out.write(header.getBytes());

            try (FileInputStream fileInputStream = new FileInputStream(requestedFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            DefaultResponse.generateFormResponse(out);
        }
    }


    private static String determineContentType(String file) {
        if (file.endsWith(".png")) {
            return "png";
        } else if (file.endsWith(".jpg") || file.endsWith(".jpeg")) {
            return "jpeg";
        } else if (file.endsWith(".html")) {
            return "html";
        } else if (file.endsWith(".css")) {
            return "css";
        } else if (file.endsWith(".js")) {
            return "javascript";
        } else {
            return "octet-stream";
        }
    }

    private static String processRequestA(String path, String query) {
        String responseBody = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n"
                + "{\"message\": \"Hello World!\"}";
        return responseBody;
    }

    private static String processRequest(String path, Request request) {
        BiFunction<Request, String, String> servicio = servicios.get(path);

        if (servicio != null) {
            String responseBody = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\"result\": \"" + servicio.apply(request, "") + "\"}"; // Corrected JSON
            return responseBody;
        } else {
            return "HTTP/1.1 404 Not Found\r\n\r\n{\"error\": \"Service not found\"}"; // 404 response
        }
    }
}