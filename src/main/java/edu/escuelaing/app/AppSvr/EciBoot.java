package edu.escuelaing.app.AppSvr;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class EciBoot {
    public static Map<String, Method> services = new HashMap<>();
    private static final Logger logger = Logger.getLogger(EciBoot.class.getName());

    //Si se desea solo probar el ECIBoot
    /*
    public static void main(String[] args){
        System.out.println("Iniciando EciBoot...");

        if (args.length == 0) {
            System.out.println("No se proporcionaron componentes para cargar.");
            return;
        }

        loadComponents(args);

        System.out.println("Simulando petición a '/greeting'...");
        String response = simulateRequest("/greeting");
        System.out.println(response);
    }
    public static void loadComponents(String[] args) {
        try {
            System.out.println("Cargando componente: " + args[0]);

            Class<?> c = Class.forName(args[0]);

            if (!c.isAnnotationPresent(RestController.class)) {
                System.out.println("La clase " + args[0] + " no es un RestController. Saliendo...");
                return;
            }

            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    GetMapping a = m.getAnnotation(GetMapping.class);
                    services.put(a.value(), m);
                    System.out.println("Método registrado: " + m.getName() + " en ruta " + a.value());
                }
            }

            System.out.println("Componentes cargados correctamente.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: No se encontró la clase " + args[0]);
        } catch (Exception ex) {
            System.out.println("Error inesperado en loadComponents(): " + ex.getMessage());
        }
    }
    */

    public static void loadComponents() {
        try {
            String controllerClass = "edu.escuelaing.app.AppSvr.controllers.GreetingController";
            System.out.println("Cargando componente: " + controllerClass);

            Class<?> c = Class.forName(controllerClass);

            if (!c.isAnnotationPresent(RestController.class)) {
                System.out.println("La clase " + controllerClass + " no es un RestController.");
                return;
            }

            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    GetMapping annotation = m.getAnnotation(GetMapping.class);
                    services.put(annotation.value(), m);
                    System.out.println("Método registrado: " + m.getName() + " en ruta " + annotation.value());
                }
            }

            System.out.println("Componentes cargados correctamente.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: No se encontró la clase.");
        } catch (Exception ex) {
            System.out.println("Error inesperado en loadComponents(): " + ex.getMessage());
        }
    }

    public static String simulateRequest(String route) {
        try {
            if (!services.containsKey(route)) {
                return "HTTP/1.1 404 Not Found\r\n\r\n{\"error\": \"Ruta no encontrada\"}";
            }
            Method method = services.get(route);
            if (method == null) {
                return "HTTP/1.1 500 Internal Server Error\r\n\r\n{\"error\": \"Método no encontrado\"}";
            }
            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\"resp\":\"" + (String) method.invoke(null, "World") + "\"}";
        } catch (IllegalAccessException | InvocationTargetException e) {
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
