package com.ipartek.formacion.model.pojo;

public class Usuario {

	private int id;
	private String nombre;
	private String pass;
	
	
	public Usuario() {
		super();
		this.id=0;
		this.nombre="";
		this.pass="";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", pass=" + pass + "]";
	}	
}
