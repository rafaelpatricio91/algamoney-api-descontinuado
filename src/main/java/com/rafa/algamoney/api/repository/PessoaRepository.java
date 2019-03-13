package com.rafa.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafa.algamoney.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>
{}
