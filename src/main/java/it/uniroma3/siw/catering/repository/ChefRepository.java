package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.catering.model.Chef;

@Repository
public interface ChefRepository extends CrudRepository<Chef, Long>
{
	public List<Chef> findByNome(String nome);
	
	public boolean existsByNomeAndCognomeAndNazionalita(String nome, String cognome, String nazionalita);
	
}
