<?php
/**
 * Insertar un nuevo usuario en la base de datos
 */

require 'Usuarios.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);

    // Insertar usuario
    $retorno = Usuarios::insert(
        $body['nombre'],
        $body['correo'],
        $body['contrasenia'],
        $body['avatar']);

    
    if($retorno['result']) {
        // Código de éxito
        print json_encode(
            array(
                'estado' => '1',
                'mensaje' => 'Usuario registrado correctamente',
                'id' => $retorno['id']
            )
        );


    }else{
        // Código de falla
        print json_encode(
            array(
                'estado' => '2',
                'mensaje' => 'Registro fallido')
        );
    }
}

?>