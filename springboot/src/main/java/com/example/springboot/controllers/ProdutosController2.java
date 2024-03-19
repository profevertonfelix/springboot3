package com.example.springboot.controllers;

import java.util.Optional;

import org.hibernate.mapping.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboot.dtos.ProdutosRecordDto;
import com.example.springboot.models.ProdutosModel;
import com.example.springboot.repositorys.ProdutosRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutosController2 {

	@Autowired
	ProdutosRepository produtosRepository;
	
	@GetMapping("/insert")
	public String produtosInser() {
		return "produtos/inserir";
	}
	
	@GetMapping("/list")
	public ModelAndView produtosList() {
		  ModelAndView mv = new ModelAndView("produtos/listar");
	        java.util.List<ProdutosModel> produtos = produtosRepository.findAll();
	        mv.addObject("produtos", produtos);
	        return mv;
	}
	
	@PostMapping("/insert")
	public String produtosInsert(@Valid @ModelAttribute ProdutosRecordDto dto, BindingResult result) {
		if(result.hasErrors())
			return "produtos/inserir";
		
		ProdutosModel produto = new ProdutosModel();
		BeanUtils.copyProperties(dto, produto);
		produtosRepository.save(produto);
		
		return "redirect:/produtos/list";
	}
	
	@GetMapping("/list/{id}")
	public ModelAndView getProduto(@PathVariable(value="id") int id) {
		ModelAndView mv = new ModelAndView("produtos/listarUm");
		Optional<ProdutosModel> produto = produtosRepository.findById(id);
		mv.addObject("nome", produto.get().getNome());
		mv.addObject("valor", produto.get().getValor());
		return mv;
	}

	@GetMapping("/delete/{id}")
	public String deleteProduto(@PathVariable(value="id") int id){

		Optional<ProdutosModel> productO = produtosRepository.findById(id);
		if(productO.isEmpty()) {
			return "redirect:/produtos/list";
		}
		produtosRepository.delete(productO.get());
		return "redirect:/produtos/list";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView updateProdutos(@PathVariable(value="id") int id) {
		ModelAndView mv = new ModelAndView("produtos/editar");
		Optional<ProdutosModel> productO = produtosRepository.findById(id);
		System.out.println(id);
		mv.addObject("nome", productO.get().getNome());
		mv.addObject("valor", productO.get().getValor());
		return mv;
	}

	@PostMapping(value="/editar/{id}")
	public String updateProduto(@Valid @ModelAttribute ProdutosRecordDto dto, BindingResult result, @PathVariable(value="id") int id) {
		Optional<ProdutosModel> productO = produtosRepository.findById(id);
		if(productO.isEmpty()) {
			return "redirect:/produtos/list";
		}

		if(result.hasErrors())
			return "produtos/inserir";

		var productModel = productO.get();
		BeanUtils.copyProperties(dto, productModel);

		produtosRepository.save(productModel);
		return "redirect:/produtos/list";
	}

}
