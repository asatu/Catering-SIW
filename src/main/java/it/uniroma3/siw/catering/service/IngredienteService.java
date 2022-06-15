package it.uniroma3.siw.catering.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.repository.IngredienteRepository;

@Service
public class IngredienteService {

	@Autowired
	private IngredienteRepository ingredienteRepository;

	@Transactional
	public Ingrediente inserisci(Ingrediente ingrediente) {
		return ingredienteRepository.save(ingrediente);
	}

	@Transactional
	public List<Ingrediente> tutti() {
		return (List<Ingrediente>) ingredienteRepository.findAll();
	}

	@Transactional
	public Ingrediente ingredientePerId(Long id) {
		Optional<Ingrediente> optional = ingredienteRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Ingrediente ingrediente) {
		List<Ingrediente> ingredienti = this.ingredienteRepository.findByNome(ingrediente.getNome());
		if (ingredienti.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void deleteIngredienteById (Long id) {
		ingredienteRepository.deleteById(id);
	}
}