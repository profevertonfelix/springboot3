package com.example.springboot.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboot.dtos.ProdutosRecordDto;
import com.example.springboot.models.ProdutosModel;
import com.example.springboot.repositorys.ProdutosRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class ProdutosController {
	
	@Autowired
	ProdutosRepository produtosRepository;
	
	@PostMapping("/produtos")
	public ResponseEntity<ProdutosModel> salvarProduto(@RequestBody @Valid ProdutosRecordDto produtosRecordDto) {
		var produtosModel = new ProdutosModel();
		BeanUtils.copyProperties(produtosRecordDto, produtosModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtosRepository.save(produtosModel));
	}
	
	@GetMapping("/produtos")
	public ResponseEntity<List<ProdutosModel>> getAllProdutos(){
		return ResponseEntity.status(HttpStatus.OK).body(produtosRepository.findAll());	
	}
	
	@GetMapping("/produtos/{id}")
	public ResponseEntity<Object> getOneProduto(@PathVariable(value="id") int id){
		Optional<ProdutosModel> produto = produtosRepository.findById(id); 
		if(produto.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto Não encontrado");
		}
		return ResponseEntity.status(HttpStatus.OK).body(produto.get());	

	}
	@PutMapping("/produtos/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value="id") int id, 
			@RequestBody @Valid ProdutosRecordDto productRecordDto) {
		Optional<ProdutosModel> productO = produtosRepository.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		var productModel = productO.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(produtosRepository.save(productModel));
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") int id) {
		Optional<ProdutosModel> productO = produtosRepository.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		produtosRepository.delete(productO.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
	}
	



}
