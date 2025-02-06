# Proyecto Final Aplicación Android en Compose

## Funcionalidad de la Aplicación

La aplicación es una plataforma que permite gestionar usuarios registrados, realizar consultas a una API externa, y mostrar notificaciones periódicamente. La pantalla principal incluye el registro de nuevos usuarios, inicio de sesión, y la visualización de información obtenida desde una API.

La aplicación también ofrece opciones de navegación entre pantallas, gestión de datos Room y Retrofit, y un sistema de autenticación con almacenamiento local para recordar los accesos del usuario.

## Pasos para Ejecutar el Proyecto

1. **Clonar el repositorio**:
```bash
git clone https://github.com/chri13Hd/MiProyectoCompose.git
```
2. **Abrir el proyecto en Android Studio**:
    - Abre Android Studio
    - Selecciona **Open** y navega hasta la carpeta del proyecto clonado.

3. **Configurar el entorno**:
   - Asegúrate de tener instalado Android Studio con soporte para Jetpack Compose.
   - Verifica que las dependencias estén correctamente configuradas en el archivo `build.gradle`.

4. **Ejecutar la alicación**:
    - Conecta un dispositivo Android o utiliza un emulador.
    - Haz clic en el botón "Run" para compilar y ejecutar la aplicación.

## Análisis de Decisiones Tomadas

Durante el desarrollo de la aplicación, se tomaron las siguientes decisiones:

1. **Uso de Jetpack Compose**:
       Se eligió **Compose** para la implementación moderna y eficiente de interfaces de usuario en Android, permitiendo un mayor control sobre el diseño y mejor rendimiento.

2. **Persistencia de Datos con Room y SharedPreferences**:
       Se utilizó **Room** para almacenar datos de usuarios de forma persistente en la base de datos local. Además, se usó **SharedPreferences** para manejar datos sencillos como el número de accesos y la fecha del último acceso.

3. **Uso de Retrofit para la Consulta de API**:
   Se implementó **Retrofit** para realizar consultas a una API externa, lo que facilita la comunicación con servidores remotos y la gestión de respuestas en formato JSON.

4. **Navegación con Jetpack Navigation**:
   Se utilizó **NavController** de Jetpack Navigation para manejar las transiciones entre las distintas pantallas de la aplicación de forma eficiente y modular.

5. **Manejo de Notificaciones**:
   Se implementaron notificaciones periódicas utilizando hilos (`Runnable`), lo que permite recordar al usuario consultar la API y realizar otras acciones en momentos específicos.

## Resumen de las Búsquedas Realizadas

A lo largo del desarrollo, se realizaron varias búsquedas para implementar las funcionalidades de la aplicación:

1. **Implementación de Retrofit**:
   Busqué documentación y tutoriales sobre cómo integrar Retrofit en un proyecto Android, especialmente para hacer solicitudes de red y manejar respuestas en formato JSON.

2. **Persistencia con Room**:
   Investigé sobre el uso de **Room** como solución de base de datos local, cómo crear entidades y acceder a los datos con DAO (Data Access Objects).

3. **Navegación en Compose**:
   Realicé búsquedas sobre cómo implementar la navegación entre pantallas usando **NavController** en Jetpack Compose, y cómo manejar el ciclo de vida de la navegación.

4. **Notificaciones peródicas**:
   Leí sobre la implementación de notificaciones periódicas en Android, utilizando hilos y alarmas para enviar recordatorios a los usuarios.

## Commits Descriptivos

Los siguientes commits describen el progreso del desarrollo de la aplicación:

- **`Estructura inicial del proyecto creada`**:  
  Configuración inicial del proyecto con dependencias y estructura básica de carpetas.
  
- **`Implementación del sistema de registro de usuarios`**:  
  Se añadió el formulario de registro y almacenamiento de usuarios en la base de datos utilizando **Room**.
  
- **`Autenticación y manejo de accesos`**:  
  Implementación del sistema de inicio de sesión y control de accesos con incremento del contador de accesos.

- **`Implementación de la consulta de API`**:  
  Integración de **Retrofit** para realizar consultas a una API externa y mostrar los resultados en la app.
  
- **`Notificaciones periódicas implementadas`**:  
  Creación de recordatorios periódicos para que el usuario consulte la API.

## Organización del Proyecto

El proyecto sigue buenas prácticas de desarrollo de Android y se organiza de la siguiente manera:

- **Jetpack Compose** se utiliza para la construcción de la interfaz de usuario, lo que proporciona un diseño reactivo y flexible.
- **Room** se encarga de la persistencia de datos locales, permitiendo almacenar la información de los usuarios y accesos.
- **Retrofit** facilita la comunicación con la API externa para obtener y procesar datos.
- **Navigation** se usa para gestionar la navegación entre las pantallas de la aplicación.
- **ViewModel** y **LiveData** permiten separar la lógica de negocio de la interfaz de usuario, siguiendo el patrón MVVM (Model-View-ViewModel).

Además, el código se organiza en capas, siguiendo el principio de **separación de responsabilidades (SoC)**, lo que hace el proyecto más limpio y mantenible.
