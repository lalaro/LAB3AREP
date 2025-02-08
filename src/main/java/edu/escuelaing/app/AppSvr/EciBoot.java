package edu.escuelaing.app.AppSvr;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class EciBoot {

    public static Map<String, Method> services = new HashMap();

    public static void main(String[] args){
        loadComponents(args);
        System.out.println(simulateRequest("/greeting"));
    }


    private static String simulateRequest(String route) {
        try {
            Method s = services.get(route);
            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\"resp\":\"" + (String) s.invoke(null, "Pedro") + "\"}";
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n" + "Error: " + e.getMessage();
        }
    }

    public static void loadComponents(String[] args) {
        try {
            Class c = Class.forName(args[0]);
            if (!c.isAnnotationPresent(RestController.class)){
                System.exit(0);
            }

            for (Method m: c.getDeclaredMethods()) {
                if(m.isAnnotationPresent(GetMapping.class)){
                    GetMapping a = m.getAnnotation(GetMapping.class);
                    services.put(a.value(), m);

                }
            }
        } catch (Exception ex){
            Logger.getLogger(EciBoot.class.getName()).log(Level.SEVERE, null, ex );

        }
    }

}
