package com.example.springboot.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@PostMapping("/list")
	public ModelAndView produtosList2(@RequestParam("search") String src) {
		  ModelAndView mv = new ModelAndView("produtos/listar");
	        java.util.List<ProdutosModel> produtos = produtosRepository.findProdutosByNomeLike("%"+src+"%");
	        mv.addObject("produtos", produtos);
	        return mv;
	}
	
	@PostMapping("/insert")
	public String produtosInsert(@Valid @ModelAttribute ProdutosRecordDto dto, BindingResult result, @RequestParam("file") MultipartFile imagem, RedirectAttributes msg) {
		if(result.hasErrors())
			return "produtos/inserir";
		ProdutosModel produto = new ProdutosModel();
		BeanUtils.copyProperties(dto, produto);

		try {
			if(!imagem.isEmpty()) {
				byte[] bytes = imagem.getBytes();
				Path caminho = Paths.get("./src/main/resources/static/img/"+imagem.getOriginalFilename());
				Files.write(caminho, bytes);
				produto.setImagem(imagem.getOriginalFilename());
			}
		}catch(IOException e) {
			System.out.println("erro imagem");
		}
		
		produtosRepository.save(produto);
		msg.addFlashAttribute("mensagem", "Excluido com sucesso");
		return "redirect:/produtos/list";
	}
	
	@GetMapping("/list/{id}")
	@ResponseBody
	public ModelAndView getProduto(@PathVariable(value="id") int id) {
		ModelAndView mv = new ModelAndView("produtos/listarUm");
		Optional<ProdutosModel> produto = produtosRepository.findById(id);
		mv.addObject("nome", produto.get().getNome());
		mv.addObject("valor", produto.get().getValor());
		mv.addObject("imagem", produto.get().getImagem());
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
	
	@GetMapping(value = "/imagens/{img}")
	@ResponseBody
	public byte[] getImagens(@PathVariable("img") String imagem) throws IOException {
		File imagemArquivo = new File("./src/main/resources/static/img/"+imagem);
		System.out.println(imagem);
		if(imagem != null || imagem.trim().length()>0) {
			
			return Files.readAllBytes(imagemArquivo.toPath());
		}
		return null;
	}

}
