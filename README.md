<img align="left" width="90" height="90" src="https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/rebel.png" alt="Resume application project app icon">

# MELI - Operación fuego de Quasar


## Tabla de contenido

* [Introducción](#introducción)
  * [Reglas de negocio](#reglas-de-negocio)
* [Arquitectura de la aplicación](#arquitectura-de-la-aplicación)
  * [Modelo funcional](#modelo-funcional)
  * [Modelo estructural](#modelo-estructural)
  * [Diseño general](#diseño-general)
* [Pruebas](#pruebas)
* [Servicio web](#servicio-web)
  * [Endpoints](#endpoints)
  * [Mensajes de error](#mensajes-de-error)
* [Ejecución](#ejecución)

## Introducción

El problema básicamente plantea un problema de [trilateración](https://es.wikipedia.org/wiki/Trilateraci%C3%B3n). Es decir, determinar la posición relativa de un objeto dadas las coordenadas de otros tres elementos y sus distancias (radios) a dicho objeto. En este caso, las coordenadas del elemento al calcular son de una nave rebelde y, los objetos de referencia, para calcular la trilateración, son satélites de la alianza rebelde.

Se define que la nave espacial enviará a cada uno de los satélites un mensaje y su distancia relativa. A continuación, se enumeran ciertas reglas de negocio que se consideraron pertinentes para abordar el problema.

### Reglas de negocio

1. El mensaje que recibe cada satélite debe tener la misma longitud. Por ejemplo, si el satélite 1 (s1) recibe: "este","es","un","mensaje","secreto" (longitud 5). El satélite 2 (s2) recibe: "este","es","un","mensaje" (longitud 4). Y, el satélite 3 (s3) recibe: "esto", "", "mensaje" (longitud 3). Se considera una violación de regla de negocio, ya que no es posible determinar cuál de los 3 es el mensaje original emitido por la nave.
2. La palabra del mensaje no puede ser vacía en la misma posición para todos los mensajes recibidos. Por ejemplo, s1 recibe: "","es","un","mensaje","". S2 recibe: "","","un","","secreto". S3 recibe: "","","un","","". Aunque todos tengan la misma longitud, en la posición 1 de todos los mensajes la palabra está vacía ("").
3. La palabra del mensaje no puede ser diferente en la misma (si no es vacía) posición para todos los mensajes recibidos. Por ejemplo, s1 recibe: "hola","es","un","mensaje","". S2 recibe: "este","","un","","secreto". S3 recibe: "","","un","","". Aunque todos tengan la misma longitud, en la posición 1 de los mensajes 1 y 2, la palabra es diferente ('hola' y 'este', respectivamente).
4. El nombre del satélite no puede ser nulo y debe estar previamente configurado en el servidor. La actual configuración sólo admite satélites con los nombres: sato, kenobi y skywalker. Los nombres son 'case sensitive', por lo que se deben enviar en minúsculas.
5. Para el servicio que recibe los mensajes por separado. Sólo retornará una respuesta válida cuando tenga al menos 3 mensajes en memoria. Para las peticiones recibidas por GET, el delimitador será la comma (,).
6. Para el servicio que recibe los mensajes por separado. El nombre del satélite, que se provee al final del path del endpoint, debe coincidir con alguno de los satélites previamente configurados: sato, kenobi o skywalker.

## Arquitectura de la aplicación

Ya que se trata de una aplicación sencilla, sólamente se provee el modelo funcional: casos de uso y, el modelo estructural: diagrama de clases, diagrama de paquetes y diagrama de despliegue.

### Modelo funcional

En el siguiente diagrama se ilustran las funcionalidades a las que tiene acceso el usuario (nave):

![Screenshot](https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/Casos_uso_Quasar.PNG)

### Modelo estructural

A continuación se presenta el diagrama de clases.

![Screenshot](https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/Clases_Quasar.PNG)

El diagrama de paquetes se muestra a continuación:

![Screenshot](https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/Paquetes_Quasar.PNG)

Por otro lado, el diagrama de despliegue es muy sencillo ya que se decidió desplegar en una aplicación web de Azure.

![Screenshot](https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/Despliegue_Quasar.PNG)

### Diseño general

La aplicación fue desplegada en Azure utilizando el servicio de integración continua de Azure. Conectando con este repositorio de Github.

![Screenshot](https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/Design-Quasar.png)

## Pruebas

Se realizaron pruebas unitarias a todo el código y se verificó el cubrimiento de la mayoría de casos de prueba utilizando Jacoco para generar el reporte.

![Screenshot](https://github.com/kgordillo-hub/Comunicaciones_Quasar/blob/master/images/Coverage_testing.PNG)

Como se observa, se logró un 92% del cubrimiento del código. El otro 8%, corresponde a casos de prueba que involucran operaciones de IO y que por su naturaleza son complicados de replicar utilizando Spring testing.

## Servicio web

La aplicación fue desplegada en Azure, a continuación se presentan los endpoint:

### Endpoints

Endpoint de auto descripción del servicio. En este mismo endpoint se pueden realizar pruebas:
```
https://communications-quasar-meli.azurewebsites.net/api/swagger-ui/index.html
```

Endpoint para obtener posición de la nave y mensaje. Recibe la lista completa de mensajes y distancias:
```
https://communications-quasar-meli.azurewebsites.net/api/topsecret
```
Ejemplo de petición:
```
POST: https://communications-quasar-meli.azurewebsites.net/api/topsecret

{
	"satellites": [
		{
			"name": "kenobi",
			"distance": 100.0,
			"message": [
				"",
				"",
				"",
				"mensaje",
				""
			]
		},
		{
			"name": "skywalker",
			"distance": 115.5,
			"message": [
				"",
				"es",
				"",
				"",
				"secreto"
			]
		},
		{
			"name": "sato",
			"distance": 142.7,
			"message": [
				"este",
				"",
				"un",
				"",
				""
			]
		}
	]
}
```



Endpoint para recibir por separado la información (POST):
```
https://communications-quasar-meli.azurewebsites.net/api/topsecret_split/{satellineName}
```

Ejemplo de petición:
```
POST: https://communications-quasar-meli.azurewebsites.net/api/topsecret_split/sato
{
	"distance": 142.7,
	"message": [
		"este",
		"",
		"un",
		"",
		""
	]
}
```

Endpoint para recibir por separado la información (GET):
```
https://communications-quasar-meli.azurewebsites.net/api/topsecret_split/{satellineName}
```

Ejemplo de petición:
```
GET: https://communications-quasar-meli.azurewebsites.net/api/topsecret_split/sato?distance=100.0&message=,,un,,

```
### Mensajes de error

A continuación se presenta una tabla con los códigos HTTP y el mensajes de respuesta a las diferentes peticiones. Si existe alguna violación a una regla de negocio, se retornará un código de error 404 (not found) con alguno de los mensajes mencionados.

HTTP code|Message
:---:|---
200|Ok
404|Distances to Satellite are null or not equal to the number of satellites to hit.|
404|Satellite with name '{satellite}' was not found in server's configuration.|
404|The Satellite name can not be null, please provide all the satellite names in the request.|
404|You are calling the end point to '{satellite}' satellite. Please remove the satellite name from the request.|
404|Is not possible to determine the message. The message from each Satellite does not have the same length.|
404|Is not possible to determine the message. There is one word that is different in at least two arrays at position: {pos}.|
404|Is not possible to determine the message. The word is empty in all arrays at position: {pos}.|
404|Please provide the message in the correct format (comma separated). Example: message=,es,un,mensaje.|
404|There can not be null words in the message. If null, please replace by empty string.|
404|Is not possible to reconstruct the message or get the spaceship's position as this service has not received enough information regarding the other satellites.|
500|System error, please contact the administrator.|

## Ejecución

Para ejecutar la aplicación localmente se deben seguir los siguientes pasos:

### Requisitos

* Tener [gradle](https://gradle.org/)
* Tener [JDK 11](https://www.oracle.com/co/java/technologies/javase/jdk11-archive-downloads.html) o superior

### Corriendo el programa

Primero, clonar el repositorio en un folder local:
```shell
git clone https://github.com/kgordillo-hub/Comunicaciones_Quasar.git
```

Segundo, entrar al folder: Comunicaciones_Quasar, abrir una terminal (cmd) y ejecutar:

```shell
./gradlew clean build
```

Tercero, una vez haya terminado la ejecución de la instrucción anterior, ejecutar:

```shell
./gradlew bootRun
```

Una vez hecho esto, ya se puede probar la aplicación. Por defecto, la aplicación corre sobre el puerto 8080, para revisar la auto descripción del servicio y probar, se puede acceder a:

```
http://localhost:8080/api/swagger-ui/index.html
```
