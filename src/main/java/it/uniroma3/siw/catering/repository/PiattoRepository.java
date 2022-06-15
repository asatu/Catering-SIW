package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;
//import it.uniroma3.siw.catering.model.Ingrediente;

public interface PiattoRepository extends CrudRepository<Piatto,Long>
{
	public List<Piatto> findByNome(String nome);

	public List<Piatto> findByIngredientiContaining(Ingrediente i);
	public boolean existsByNome(String nome);
	
}
