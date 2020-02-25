package com.ipartek.formacion.model;

import com.ipartek.formacion.model.pojo.Usuario;

public interface IUsuarioDAO extends IDAO<Usuario>{

	/**
	 * Comprueba si existe el usuario en la base datos
	 * @param nombre
	 * @param contrasenia
	 * @return Usuario con datos si lo encuentra, <b>null</b> en caso contrario
	 */
	Usuario exist( String nombre, String contrasenia);
}
