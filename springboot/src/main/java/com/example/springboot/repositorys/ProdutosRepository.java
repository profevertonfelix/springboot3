package com.example.springboot.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.models.ProdutosModel;

@Repository
public interface ProdutosRepository extends JpaRepository<ProdutosModel, Integer> {
	List<ProdutosModel> findProdutosByNomeLike(String nome);
}
