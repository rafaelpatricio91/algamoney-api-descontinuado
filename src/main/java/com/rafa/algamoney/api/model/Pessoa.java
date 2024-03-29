package com.rafa.algamoney.api.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="pessoa")
public class Pessoa
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	private String nome;
	private boolean ativo;
	@Embedded
	private Endereco endereco;
	
	public Long getCodigo()
	{
		return codigo;
	}
	public void setCodigo(Long id)
	{
		this.codigo = id;
	}
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public boolean isAtivo()
	{
		return ativo;
	}
	public void setAtivo(boolean ativo)
	{
		this.ativo = ativo;
	}
	public Endereco getEndereco()
	{
		return endereco;
	}
	public void setEndereco(Endereco endereco)
	{
		this.endereco = endereco;
	}
	
	@JsonIgnore @Transient
	public Boolean isInativo()
	{
		return !this.ativo;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (codigo == null)
		{
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}
