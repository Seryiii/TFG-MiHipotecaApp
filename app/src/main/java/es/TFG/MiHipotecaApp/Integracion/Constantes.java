package es.TFG.MiHipotecaApp.Integracion;

public class Constantes {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    //private static final String PUERTO_HOST = "63343";

    /**
     * Dirección IP de AVD
     */
    private static final String IP = "http://10.0.2.2:/MiHipotecaApp";

    /**
     * URLs del Web Service
     */

    public static final String GET_ALL_USERS = IP + "/obtener_usuarios.php";
    //public static final String UPDATE = IP  + "/dbcitas_android/actualizar_usuario.php";

    //public static final String DELETE = IP  + "/dbcitas_android/eliminar_usuario.php";
    //public static final String CREACION_USUARIO = IP + "/creacion_usuario.php";
    //public static final String GET_BY_ID_USUARIO = IP + "/obtener_usuario_por_id.php";
    //public static final String GET_BY_NOMBRE_USUARIO = IP + "/obtener_usuario_por_nombre_usuario.php";
    //public static final String GET_BY_PROVINCIA = IP + "/obtener_usuarios_por_provincia.php";
    //public static final String UPDATE_NOMBRE_USUARIO = IP + "/actualizar_nombre_por_id.php";
    //public static final String UPDATE_SEXO_USUARIO = IP + "/actualizar_sexo_por_id.php";
    //public static final String UPDATE_BUSCA_USUARIO = IP + "/actualizar_busca_por_id.php";

    /**
     * Clave para el valor extra que representa al identificador de un usuario
     */
    public static final String EXTRA_ID = "IDEXTRA";

}
