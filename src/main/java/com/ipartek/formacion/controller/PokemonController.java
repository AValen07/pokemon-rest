package com.ipartek.formacion.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ipartek.formacion.model.PokemonDAO;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.ipartek.formacion.utilidades.Utilidades;

/**
 * Servlet implementation class PokemonController
 */
@WebServlet("/api/pokemon/*")
public class PokemonController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static PokemonDAO dao;

	private static String pathInfo;
	private static PrintWriter out;
	private static String jsonResponseBody;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = PokemonDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		dao = null;
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
		
		super.service(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pNombre = request.getParameter("nombre");
		String pathInfo = request.getPathInfo();
		int id = 0;
		if (pNombre == null) {
			try {
				id = Utilidades.obtenerId(pathInfo);

				if (id == -1) {

					ArrayList<Pokemon> pokemons = (ArrayList<Pokemon>) dao.getAll();

					if (pokemons.size() != 0) {
						try (PrintWriter out = response.getWriter()) {

							Gson json = new Gson();
							out.print(json.toJson(pokemons));
							out.flush();

						}

						response.setStatus(HttpServletResponse.SC_OK);
					} else {
						response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					}

				} else {
					Pokemon pokemon = dao.getById(id);
					if (pokemon != null) {
						try (PrintWriter out = response.getWriter()) {

							Gson json = new Gson();
							out.print(json.toJson(pokemon));
							out.flush();

						}
						response.setStatus(HttpServletResponse.SC_OK);
					} else {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}
					// LOG.debug(pokemon);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ArrayList<Pokemon> pokemons = (ArrayList<Pokemon>) dao.getAllbyName(pNombre);

			if (pokemons.size() != 0) {
				try (PrintWriter out = response.getWriter()) {

					Gson json = new Gson();
					out.print(json.toJson(pokemons));
					out.flush();

				}

				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		Pokemon pokemon = gson.fromJson(reader, Pokemon.class);
		
		try {
			
			dao.create(pokemon);
			response.setStatus(HttpServletResponse.SC_CREATED);
			
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		catch(Exception e){
			response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);

		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		Pokemon pokemon = gson.fromJson(reader, Pokemon.class);
		
		String pathInfo = request.getPathInfo();
		int id = 0;
		
		try {
			id = Utilidades.obtenerId(pathInfo);
			dao.update(id, pokemon);
			response.setStatus(HttpServletResponse.SC_CREATED);
			
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			
		}catch(Exception e){
			response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		int id = 0;
		try {

			id = Utilidades.obtenerId(pathInfo);

			if (id != -1) {

				Pokemon pokemon = dao.delete(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
