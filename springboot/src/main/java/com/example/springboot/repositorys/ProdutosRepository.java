package com.example.springboot.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.models.ProdutosModel;

@Repository
public interface ProdutosRepository extends JpaRepository<ProdutosModel, Integer> {
	
}
