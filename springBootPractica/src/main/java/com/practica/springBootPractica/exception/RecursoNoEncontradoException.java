package com.practica.springBootPractica.exception;

public class RecursoNoEncontradoException extends RuntimeException {
	
	public RecursoNoEncontradoException(String mensaje) {
		super(mensaje);
	}
}
