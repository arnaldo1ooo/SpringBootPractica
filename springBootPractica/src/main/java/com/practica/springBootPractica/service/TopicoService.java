package com.practica.springBootPractica.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.practica.springBootPractica.controller.dto.TopicoDTO;
import com.practica.springBootPractica.controller.dto.TopicoDetalleDTO;
import com.practica.springBootPractica.controller.form.ActualizaTopicoForm;
import com.practica.springBootPractica.controller.form.TopicoForm;
import com.practica.springBootPractica.exception.RecursoNoEncontradoException;
import com.practica.springBootPractica.model.Curso;
import com.practica.springBootPractica.model.Topico;
import com.practica.springBootPractica.model.Usuario;
import com.practica.springBootPractica.repository.CursoRepository;
import com.practica.springBootPractica.repository.TopicoRepository;
import com.practica.springBootPractica.repository.UsuarioRepository;

@Service
public class TopicoService {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	public List<TopicoDTO> listado(String cursoNombre) {
		List<Topico> resultado;
		
		if (cursoNombre == null) {
			resultado = topicoRepository.findAll();
		}
		else {
			resultado = topicoRepository.findByCurso_Nombre(cursoNombre);
		}
		
		return TopicoDTO.convertir(resultado);
	}

	public Topico registrar(TopicoForm topicoForm) {
		Optional<Usuario> usuario= usuarioRepository.findById(topicoForm.getIdUsuario());
		Optional<Curso> curso = cursoRepository.findByNombre(topicoForm.getCursoNombre());
	
		Topico topico = topicoForm.convertir(usuario, curso);
		
		return topicoRepository.save(topico);
	}

	public TopicoDetalleDTO detalle(Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		
		if (!topico.isPresent()) {
			throw new RecursoNoEncontradoException(String
					.format("El topico de id %s no fue encontrado", id));
		} 
		
		return new TopicoDetalleDTO(topico.get());
	}

	@Transactional
	public Topico actualizar(Long id, @Valid ActualizaTopicoForm actualizaTopicoForm) {
		Optional<Topico> optTopico = topicoRepository.findById(id);
		
		if (!optTopico.isPresent()) {
			throw new RecursoNoEncontradoException(String
					.format("El topico de id %s no fue encontrado", id));
		}
		
		Topico topico = optTopico.get();	
		topico.setTitulo(actualizaTopicoForm.getTitulo());
		topico.setMensaje(actualizaTopicoForm.getMensaje());
		
		return topico;
	}

	public void borrar(Long id) {
		Optional<Topico> optTopico = topicoRepository.findById(id);
		
		if (!optTopico.isPresent()) {
			throw new RecursoNoEncontradoException(String
					.format("El topico de id %s no fue encontrado", id));
		}
		
		topicoRepository.deleteById(id);
	}
	
	
	
}
