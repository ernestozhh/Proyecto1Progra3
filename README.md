# Proyecto 1 Progra 3 - Consulta de Padron Electoral

## Integrantes

German Benitez Mendez
Carlos Olaso Lizano
Christopher Orozco Zamora
Lorenzo Rojas Solano
Ernesto Zawadzki Hernandez

## Compilacion y ejecucion

El programa se debe compilar en ant dentro del IDE de NetBeans 25 con el JDK 23.
Es necesario  descargar del siguiente link https://miulatinaac-my.sharepoint.com/:f:/g/personal/ernesto_zawadzki_ulatina_net/IgC_ejljg_ElT5ohYwE4kE_GAUtJ-UgT6T0PwpokENywGEM?e=bxnmmU  los archivos de PADRON.txt y distelec.txt y colocarlos dentro del folder del programa al mismo nivel que este README.

Al ejecutar, se observara un menu con las siguientes opciones:

1. Levantar servidores
2. Consultar persona por TCP
3. Consultar persona por HTTP
4. Salir

Para poder realizar consultas primero se deben levantar los servidores. 
Posteriormente se elige por cual servidor se hara la consulta (TCP/HTTP), se elige el formato (JSON/XML), y se indica una cedula. 
Al terminar se debe elegir la opcion de salir para cerrar los servidores y el programa.

## Protocolo TCP

El servidor TCP escucha en el puerto `5000`.

Formato del request:

```text
GET|cedula|JSON
GET|cedula|XML
```

Comando especial:

```text
BYE
```

Ejemplos validos:

```text
GET|119000588|JSON
GET|119000588|XML
```

Ejemplos invalidos:

```text
GET||JSON
BUSCAR|119000588|JSON
GET|119000588|TXT
```

## Endpoints HTTP

El servidor HTTP escucha en el puerto `8080`.

Se soportan dos estilos de endpoint:

```text
GET /padron?cedula=119000588&format=json
GET /padron?cedula=119000588&format=xml
GET /padron/119000588?format=json
GET /padron/119000588?format=xml
```

Estados HTTP esperados:

- `200 OK`: consulta exitosa
- `400 Bad Request`: cedula vacia, cedula invalida o formato invalido
- `404 Not Found`: cedula no encontrada
- `405 Method Not Allowed`: metodo distinto de `GET`
- `500 Internal Server Error`: error interno controlado

## Ejemplos de requests y responses

### TCP JSON

Request:

```text
GET|119000588|JSON
```

Response:

```json
{"recibido":true,"respuesta":"Consulta exitosa","persona":{"cedula":"119000588","nombre":"ERNESTO","apellido1":"ZAWADZKI","apellido2":"HERNANDEZ","codElectoral":"118003","provincia":"SAN JOSE","canton":"CURRIDABAT","distrito":"SANCHEZ"}}
```

### TCP XML

Request:

```text
GET|119000588|XML
```

Response:

```xml
<respuesta><recibido>true</recibido><mensaje>Consulta exitosa</mensaje><persona><cedula>119000588</cedula><nombre>ERNESTO</nombre><apellido1>ZAWADZKI</apellido1><apellido2>HERNANDEZ</apellido2><codElectoral>118003</codElectoral><provincia>SAN JOSE</provincia><canton>CURRIDABAT</canton><distrito>SANCHEZ</distrito></persona></respuesta>
```

### HTTP JSON

Request:

```http
GET /padron?cedula=119000588&format=json HTTP/1.1
Host: localhost:8080
```

Response:

```json
{"recibido":true,"respuesta":"Consulta exitosa","persona":{"cedula":"119000588","nombre":"ERNESTO","apellido1":"ZAWADZKI","apellido2":"HERNANDEZ","codElectoral":"118003","provincia":"SAN JOSE","canton":"CURRIDABAT","distrito":"SANCHEZ"}}
```

### HTTP XML

Request:

```http
GET /padron/119000588?format=xml HTTP/1.1
Host: localhost:8080
```

Response:

```xml
<respuesta><recibido>true</recibido><mensaje>Consulta exitosa</mensaje><persona><cedula>119000588</cedula><nombre>ERNESTO</nombre><apellido1>ZAWADZKI</apellido1><apellido2>HERNANDEZ</apellido2><codElectoral>118003</codElectoral><provincia>SAN JOSE</provincia><canton>CURRIDABAT</canton><distrito>SANCHEZ</distrito></persona></respuesta>
```

## Ejemplos de logs TCP

Al levantar los servicios y hacer una consulta TCP, es normal ver algo parecido a esto en consola:

```text
Servidor TCP escuchando en el puerto 5000
Protocolo: GET|cedula|JSON o GET|cedula|XML
Comando de cierre: BYE
Servidor TCP levantado en puerto 5000.
Servidor HTTP levantado en puerto 8080.
Cliente conectado.
```

Si se consulta desde el menu, tambien se muestra la respuesta serializada:

```text
===== RESPUESTA TCP =====
{"recibido":true,"respuesta":"Consulta exitosa","persona":{"cedula":"119000588","nombre":"ERNESTO","apellido1":"ZAWADZKI","apellido2":"HERNANDEZ","codElectoral":"118003","provincia":"SAN JOSE","canton":"CURRIDABAT","distrito":"SANCHEZ"}}
```

## Ejemplos con curl HTTP

Consulta JSON por query string:

```bash
curl "http://localhost:8080/padron?cedula=119000588&format=json"
```

Consulta XML por path parameter:

```bash
curl "http://localhost:8080/padron/119000588?format=xml"
```

Error por cedula invalida:

```bash
curl "http://localhost:8080/padron?cedula=ABC&format=json"
```

Error por formato invalido:

```bash
curl "http://localhost:8080/padron?cedula=119000588&format=txt"
```

Metodo no permitido:

```bash
curl -X POST "http://localhost:8080/padron?cedula=119000588&format=json"
```
