package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.catering.model.Ingrediente;

@Repository
public interface IngredienteRepository extends CrudRepository<Ingrediente,Long> {
	public List<Ingrediente> findByNome (String nome);
	
	//public List<Ingrediente> findByNomeOrOrigine (String nome, String origine);
	//public List<Ingrediente> findByOrigine (String origine);
	//public boolean existsByNomeAndOrigine (String nome, String origine);
}