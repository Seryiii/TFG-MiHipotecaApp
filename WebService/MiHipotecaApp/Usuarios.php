<?php

/**
 * Representa la estructura de los usuarios
 * almacenados en la base de datos
 */
require 'Database.php';

class Usuarios
{
    function __construct()
    {
    }

    /**
     * Retorna en la fila especificada de la tabla 'usuario'
     *
     * @param $id Identificador del registro
     * @return array Datos del registro
     */
    public static function getAll()
    {
        $consulta = "SELECT * FROM usuarios";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
    }


    /**
     * Obtiene los campos de un usuario con un identificador
     * determinado
     *
     * @param $idUsuario Identificador del usuario
     * @return mixed
     */
    public static function getById($idUsuario)
    {
        // Consulta del usuario
        $consulta = "SELECT * FROM usuarios WHERE id = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idUsuario));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }


     /**
     * Obtiene los campos de un usuario con un identificador
     * determinado
     *
     * @param $idUsuario correo del usuario
     * @return mixed
     */
    public static function getByCorreo($correo)
    {
        // Consulta del usuario
        $consulta = "SELECT * FROM usuarios WHERE correo = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($correo));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

    /**
     * Insertar un nuevo usuario
     *
     * @param $nombre           nombre de usuario del nuevo registro
     * @param $correo           correo del nuevo registro
     * @param $contrasenia      contrasenia del nuevo registro
     * @param $avatar           avatar del nuevo registro
     * @return PDOStatement
     */
    public static function insert($nombre, $correo, $contrasenia, $avatar)
    {
        // Sentencia INSERT
        $comando = "INSERT INTO usuarios (nombre, correo, password, premium, avatar) VALUES(?,?,?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        $result = $sentencia->execute(array($nombre,$correo,$contrasenia,false,$avatar));

        $id = Database::getInstance()->getDb()->lastInsertId();

        return array(
            'result' => $result,
            'id' => $id
        );
        
    }

}
?>