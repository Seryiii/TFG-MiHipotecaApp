<?php
/**
 * Obtiene el detalle de un usuario especificado por
 * su correo "correo"
 */

require 'Usuarios.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    if (isset($_GET['correoUsuario'])) {

        // Obtener parámetro correoUsuario
        $parametro = $_GET['correoUsuario'];

        // Tratar retorno
        $retorno = Usuarios::getByCorreo($parametro);


        if ($retorno) {

            $usuario["estado"] = "1";
            $usuario["usuario"] = $retorno;
            // Enviar objeto json del usuario
            print json_encode($usuario);
        } else {
            // Enviar respuesta de error general
            print json_encode(
                array(
                    'estado' => '2',
                    'mensaje' => 'No se obtuvo el usuario'
                )
            );
        }

    } else {
        // Enviar respuesta de error
        print json_encode(
            array(
                'estado' => '3',
                'mensaje' => 'Se necesita un correo'
            )
        );
    }
}

?>