package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;

public class HabilidadesDAO implements IDAO<Habilidad> {

	private static HabilidadesDAO INSTANCE;

	private HabilidadesDAO() {
		super();
	}

	public static synchronized HabilidadesDAO getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new HabilidadesDAO();
		}
		return INSTANCE;
	}

	
	@Override
	public List<Habilidad> getAll() {
		String sql = "SELECT id, nombre " + 
				"FROM habilidad " + 
				"ORDER BY id ASC " + 
				"LIMIT 500;";

		ArrayList<Habilidad> resultado=new ArrayList<Habilidad>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {

				resultado.add(mapper(rs));
			}

		} catch (Exception e) {
			// TODO: LOG
			e.printStackTrace();
		}

		return resultado;
	}

	private Habilidad mapper(ResultSet rs) throws SQLException {
		
		Habilidad h= new Habilidad();
		
		h.setId(rs.getInt("id"));
		h.setNombre(rs.getString("nombre"));
		
		return h;
		
	}

	@Override
	public Habilidad getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad update(int id, Habilidad pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad create(Habilidad pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
