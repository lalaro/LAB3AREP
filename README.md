# Lab3 AREP

Para este taller los estudiantes deberán construir un servidor Web (tipo Apache) en Java. El servidor debe ser capaz de entregar páginas html e imágenes tipo PNG. Igualmente el servidor debe proveer un framework IoC para la construcción de aplicaciones web a partir de POJOS. Usando el servidor se debe construir una aplicación Web de ejemplo. El servidor debe atender múltiples solicitudes no concurrentes.

Para este taller desarrolle un prototipo mínimo que demuestre las capacidades reflexivas de JAVA y permita por lo menos cargar un bean (POJO) y derivar una aplicación Web a partir de él.

Debe entregar su trabajo al final del laboratorio. Luego puede complementar para entregarlo en 8 días. Se verificara y compararán el commit del día de inicio del laboratorio y el dela entrega final.

## Comenzando

Se debe clonar el proyecto localmente con el comando:

` git clone https://github.com/lalaro/LAB3AREP.git`

Y luego revisar las intrucciones a continuación para el manejo de soluciones del proyecto.

### Prerrequisitos

Se necesita de Maven (La versión más reciente) y Java 19, la instalación debe realizarse desde las paginas oficiales de cada programa.


### Instalación

Para Maven debe irse a https://maven.apache.org/download.cgi, descargar la versión más nueva que allá de Maven (En este caso tenemos la versión 3.9.6) y agregarse en la carpeta de Program Files, luego se hace la respectiva configuración de variables de entorno según la ubicación que tenemos para el archivo de instalación, tanto de MAVEN_HOME y de Path.
Luego revisamos que haya quedado bien configurado con el comando para Windows:

` mvn - v `
o
` mvn -version `

Para Java debe irse a https://www.oracle.com/java/technologies/downloads/?er=221886, descargar la versión 19 de Java y agregarse en la carpeta de Program Files, luego se hace la respectiva configuración de variables de entorno según la ubicación que tenemos para el archivo de instalación, tanto de JAVA_HOME y de Path.
Luego revisamos que haya quedado bien configurado con el comando para Windows:

` java -version `

Si no tenemos la versión solicitada podemos hacer lo siguiente, para el caso de Windows:

Ir al Windows PowerShell y ejecutar como administrador los siguientes codigos:

` [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-19.0.2", [System.EnvironmentVariableTarget]::Machine) ` 

Revisar las rutas de la máquina

`  $env:JAVA_HOME = "C:\Program Files\Java\jdk-19.0.2" `

`  $env:Path = "C:\Program Files\Java\jdk-19.0.2\bin;" + $env:Path `

`  echo $env:JAVA_HOME `

`  javac -version `

`  java -version `

Así se debe ver:

![image5.jpeg](src/main/resources/image5.jpeg)

## Solución del lab 

El desarrollo del Laboratorio es el siguiente:

Como arquitectura tenemos:

![image1.jpeg](src%2Fmain%2Fresources%2Fimage1.jpeg)

Explicación de arquitectura:

Este diagrama representa una arquitectura de sistema distribuido que involucra un cliente web, un servidor HTTP y un servidor backend, todos comunicándose a través de una red local. El cliente web realiza solicitudes al servidor HTTP, quien a su vez puede solicitar datos JSON al servidor backend. Además, el servidor HTTP sirve archivos estáticos (HTML, CSS, JS, PNG, JPEG) directamente al cliente.
En el diagrama se establece el puerto 35000 utilizado para la comunicación y la especificación de la ruta GET. El Local Server indica que todos los componentes residen en el mismo entorno local.

Desarrollo del lab:

1. Para su primera versión cargue el POJO desde la línea de comandos , de manera similar al framework de TEST. Es decir pásela como parámetro cuando invoque el framework. Ejemplo de invocación:

java -cp target/classes co.edu.escuelaing.reflexionlab.MicroSpringBoot co.edu.escuelaing.reflexionlab.FirstWebService

Debemos tener en cuenta que este comando cambiara porque se va implementar en el servidor y todo va estar sobre la clase de **WebApplication**

![image2.jpeg](src/main/resources/image2.jpeg)

Entonces tendremos

` java -version `
` mvn dependency:copy-dependencies `
` java -cp "target/classes;target/dependency/*" edu.escuelaing.app.AppSvr.server.WebApplication `

Para probar las clases que usan las anotaciones de forma independiente se puede:

` java -cp "target/classes;target/dependency/*" edu.escuelaing.app.AppSvr.server.WebApplication edu.escuelaing.appAppSvr.controller.GreetingController ` o ` java -cp "target/classes;target/dependency/*" edu.escuelaing.app.AppSvr.server.WebApplication edu.escuelaing.appAppSvr.controller.MathController `

![image3.jpeg](src/main/resources/image3.jpeg)

Es necesario traer las dependencias, ya que si no se traen no puede explorar el directorio raiz, con las respectivas anotaciones

![image3.1.jpeg](src/main/resources/image3.1.jpeg)

2. Atienda la anotación @GetMapping publicando el servicio en la URI indicada, limítelo a tipos de retorno String,  ejemplo:

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}

Nuestro servicio será la clase **GreetingController**
![image4.jpeg](src/main/resources/image4.jpeg)

3. En su versión final el framework debe explorar el directorio raiz (o classpath) buscando classes con una anotación que indique que son componentes, por ejemplo @RestController y cargar todos los que tengan dicha anotación. Así no tendrá que especificarlos siempre en la línea de comandos.

Se agregan las dependencias en el pom.xml para poder explorar el directorio raiz.

![image6.jpeg](src/main/resources/image6.jpeg)
![image7.jpeg](src/main/resources/image7.jpeg)

Se configura el EciBoot, para que no realice la búsqueda en una clase específica, sino sobre todas.

![image8.jpeg](src/main/resources/image8.jpeg)

Se configura el HttpServer

![image9.jpeg](src/main/resources/image9.jpeg)

Se implementan más clases en el directorio de controllers, para poder verificar que si se haga la busqueda sobre todos.
![image10.jpeg](src/main/resources/image10.jpeg)
![image11.jpeg](src/main/resources/image11.jpeg)

Logra identificar lo que hay en las dos clases, usando las anotaciones correspondientes.

![image12.jpeg](src/main/resources/image12.jpeg)

4.  Debe soportar también @GetMapping y debe soportar @RequestParam.

Configuración:

![image13.jpeg](src/main/resources/image13.jpeg)
![image14.jpeg](src/main/resources/image14.jpeg)

Implementación para @GetMapping y @RequestParam.

![image15.jpeg](src/main/resources/image15.jpeg)
![image16.jpeg](src/main/resources/image16.jpeg)

Directorio de Controllers:

![image18.jpeg](src/main/resources/image18.jpeg)
![image19.jpeg](src/main/resources/image19.jpeg)

Acá se verán las respuestas del directorio controllers, como debería traer la respuesta a su petición:

![image17.jpeg](src/main/resources/image17.jpeg)

5. Debe ser posible impelmentar el siguiente componente:

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hola " + name;
	}
}

![image20.jpeg](src/main/resources/image20.jpeg)

Para probar todo el funcionamiento podemos traer las rutas del WebApplication, HttpServer, GreetingController y MathController.
http://localhost:35000/greeting?name=Laura

![image21.jpeg](src/main/resources/image21.jpeg)

http://localhost:35000/greeting

![image22.jpeg](src/main/resources/image22.jpeg)

http://localhost:35000/pi

![image23.jpeg](src/main/resources/image23.jpeg)

http://localhost:35000/square?n=88

![image24.jpeg](src/main/resources/image24.jpeg)

http://localhost:35000/index.html

![image25.jpeg](src/main/resources/image25.jpeg)

http://localhost:35000/app/pi

![image26.jpeg](src/main/resources/image26.jpeg)

http://localhost:35000/app/e

![image27.jpeg](src/main/resources/image27.jpeg)

http://localhost:35000/hello

![image28.jpeg](src/main/resources/image28.jpeg)


El resumen en nuestra consola de nuestras busquedas se verá así:
![image29.jpeg](src/main/resources/image29.jpeg)
![image30.jpeg](src/main/resources/image30.jpeg)

## Ejecutando las pruebas

Podemos Abrir en terminal el proyecto y ejecutar las pruebas desde el PowerShell, en el caso de Windows. Y ejecutamos el comando:

` mvn test `

O de igual forma en el ID que deseemos.

Así se vera:



### Desglose en pruebas de extremo a extremo


### Y pruebas de estilo de código


## Despliegue

Podemos Abrir en terminal el proyecto y compilar y empaquetar el proyecto desde el PowerShell, en el caso de Windows. Y ejecutamos los comandos:

` mvn clean `

` mvn compile `

` mvn package `

O de igual forma en el ID que deseemos.

Así se vera:


## Construido con

* [Maven](https://maven.apache.org/) - Gestión de dependencias.
* [Java](https://www.java.com/es/) - Versionamiento en Java.

## Contribuyendo

Por favor, lee [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) para detalles sobre nuestro código de conducta y el proceso para enviarnos solicitudes de cambios (*pull requests*).

## Versionado

Usamos [SemVer](http://semver.org/) para el versionado.

## Autores

* **Laura Valentina Rodríguez Ortegón** - *Lab2 AREP* - [Repositorio](https://github.com/lalaro/Laboratorio-2AREP.git)

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo [LICENSE.md](LICENSE.md) para más detalles.

## Reconocimientos

* Agradecimientos a la Escuela Colombiana de Ingeniería
* La documentación de Git Hub
* Al profesor Luis Daniel Benavides
