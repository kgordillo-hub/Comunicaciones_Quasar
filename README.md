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
