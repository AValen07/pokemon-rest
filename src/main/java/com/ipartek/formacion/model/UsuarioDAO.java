package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ipartek.formacion.model.pojo.Usuario;

public class UsuarioDAO implements IUsuarioDAO {

	@Override
	public List<Usuario> getAll() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Usuario getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario update(int id, Usuario pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario create(Usuario pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario exist(String nombre, String contrasenia) {
		Usuario resul = null;
		String sql = "SELECT id, usuario, pass FROM usuario WHERE usuario=? AND pass=?";
		try (Connection con = ConnectionManager.getConnection(); 
			 PreparedStatement pst = con.prepareStatement(sql);){
			
			pst.setString(1, nombre);
			pst.setString(2, contrasenia);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
