package com.teknoinn.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.teknoinn.exception.ModeloNotFoundException;
import com.teknoinn.model.Pago;
import com.teknoinn.model.Vendedor;
import com.teknoinn.service.IPagoService;
import com.teknoinn.service.IVendedorService;

@RestController
@RequestMapping("/pagos")
public class PagoController {
	
	@Autowired
	private IPagoService service;
	
	
	@PreAuthorize("@authServiceImpl.tieneAcceso('listar')")
	//@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('DBA')")
	@GetMapping
	public ResponseEntity<List<Pago>> listar() {
		List<Pago> lista = service.listar();
		return new ResponseEntity<List<Pago>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pago> listarPorId(@PathVariable("id") Integer id) {
		Pago obj = service.leerPorId(id);
		if (obj.getIdPago() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO" + id);
		}
		return new ResponseEntity<Pago>(obj, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Pago pago) {
		Pago obj = service.registrar(pago);
		//pago/4
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pago.getIdPago()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Pago> modificar(@Valid @RequestBody Pago pago) {
		Pago obj = service.modificar(pago);
		return new ResponseEntity<Pago>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		Pago obj = service.leerPorId(id);
		if (obj.getIdPago() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO" + id);
		}
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
