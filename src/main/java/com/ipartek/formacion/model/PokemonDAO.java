package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;

public class PokemonDAO implements IDAO<Pokemon> {

	private static PokemonDAO INSTANCE;

	private PokemonDAO() {
		super();
	}

	public static synchronized PokemonDAO getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new PokemonDAO();
		}
		return INSTANCE;

	}

	@Override
	public List<Pokemon> getAll() {

		String sql = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' "
				+ "FROM pokemon p " + "LEFT JOIN pokemon_habilidades ph " + "ON p.id=ph.id_pokemon "
				+ "LEFT JOIN habilidad h " + "ON h.id=ph.id_habilidad " + "ORDER BY p.id ASC LIMIT 500;";

		HashMap<Integer, Pokemon> hm = new HashMap<>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {

				mapper(rs, hm);
			}

		} catch (Exception e) {
			// TODO: LOG
			e.printStackTrace();
		}

		return new ArrayList<Pokemon>(hm.values());
	}

	@Override
	public Pokemon getById(int id) {
		String sql = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' "
				+ "FROM pokemon p " + "JOIN pokemon_habilidades ph " + "ON p.id=ph.id_pokemon " + "JOIN habilidad h "
				+ "ON h.id=ph.id_habilidad " + "WHERE p.id=? " + "ORDER BY p.id DESC LIMIT 500";
		HashMap<Integer, Pokemon> hm = new HashMap<>();
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {

			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {

				mapper(rs, hm);

			}

		} catch (Exception e) {
			// TODO: LOG
			e.printStackTrace();
		}

		return (hm.get(id));
	}

	@Override
	public Pokemon delete(int id) throws Exception {

		Pokemon resultado = getById(id);
		String sql = "DELETE FROM pokemon WHERE id=?";
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {

			pst.setInt(1, id);

			int afectedRows = pst.executeUpdate();
			if (afectedRows != 1) {
				throw new Exception();
			}
		}

		return resultado;
	}

	@Override
	public Pokemon update(int id, Pokemon pojo) throws Exception {
		String sql = "UPDATE pokemon SET nombre=? WHERE id=?;";
		String sql_delete = "DELETE FROM pokemon_habilidades WHERE id_pokemon=?;";
		String sql_insert = "INSERT INTO pokemon_habilidades (id_pokemon, id_habilidad) VALUES (?,?);";
		
		Pokemon resultado = null;

		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {
			pst.setString(1, pojo.getNombre());
			pst.setInt(2, id);

			PreparedStatement pstd = con.prepareStatement(sql_delete);
			PreparedStatement psti = con.prepareStatement(sql_insert);
			
			pstd.setInt(1, pojo.getId());
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				pstd.executeUpdate();
				
				for (Habilidad habilidad : pojo.getHabilidades()) {
					psti.setInt(1, pojo.getId());
					psti.setInt(2, habilidad.getId());
					psti.executeUpdate();
				}
				
				resultado = pojo;
			} else {
				throw new Exception("No se encontro registro para id=" + id);
			}
		}

		return resultado;
	}

	@Override
	public Pokemon create(Pokemon pojo) throws Exception {
		String sql_pokemon = "INSERT INTO pokemon (nombre) VALUES (?);";
		String sql_habilidad = "INSERT INTO pokemon_habilidades (id_pokemon, id_habilidad) VALUES (?,?);";
		
		Connection con = null;
		Pokemon resultado = null;
		try {
			con = ConnectionManager.getConnection();
			con.setAutoCommit(false);
			PreparedStatement pst = con.prepareStatement(sql_pokemon, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement psth = con.prepareStatement(sql_habilidad);

			pst.setString(1, pojo.getNombre());

			int afectedRows = pst.executeUpdate();
			if (afectedRows == 1) {
				ResultSet rs= pst.getGeneratedKeys();
				
				if(rs.next()) {
					pojo.setId(rs.getInt(1));
					resultado = pojo;
				}	
				
				if(resultado.getHabilidades().size()>0) {
					
					for (Habilidad habilidad : resultado.getHabilidades()) {
						psth.setInt(1, resultado.getId());
						psth.setInt(2, habilidad.getId());
						psth.executeUpdate();
					}
				}
			}
			
			con.commit();
		} catch (Exception e) {

			con.rollback();
		} finally {

			if (con != null) {
				con.close();
			}
		}

		return resultado;
	}

	private void mapper(ResultSet rs, HashMap<Integer, Pokemon> hm) throws SQLException {

		int idPokemon = rs.getInt("id_pokemon");
		Pokemon p = hm.get(idPokemon);

		if (p == null) {
			p = new Pokemon();
			p.setId(idPokemon);
			p.setNombre(rs.getString("nombre_pokemon"));
		} else {

		}
		Habilidad h = new Habilidad();
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));

		p.getHabilidades().add(h);

		hm.put(idPokemon, p);
	}

	public List<Pokemon> getAllbyName(String nombre) {

		String sql = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' "
				+ "FROM pokemon p " + "JOIN pokemon_habilidades ph " + "ON p.id=ph.id_pokemon " + "JOIN habilidad h "
				+ "ON h.id=ph.id_habilidad " + "WHERE p.nombre LIKE ?" + "ORDER BY p.id DESC LIMIT 500";

		HashMap<Integer, Pokemon> hm = new HashMap<>();
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {

			pst.setString(1, "%" + nombre + "%");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {

				mapper(rs, hm);

				// registros.add(p);
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return new ArrayList<Pokemon>(hm.values());
	}

}
