package it.uniroma3.siw.catering.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.catering.validator.IngredienteValidator;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
public class IngredienteController {
	
	@Autowired
	private IngredienteService ingredienteService;
	
    @Autowired
    private IngredienteValidator ingredienteValidator;
    
    @Autowired
    private PiattoService piattoService;
        
    @RequestMapping(value="/admin/ingrediente", method = RequestMethod.GET)
    public String addIngrediente(Model model) {
    	model.addAttribute("ingrediente", new Ingrediente());
        return "ingredienteForm";
    }

    @RequestMapping(value = "/ingrediente/{id}", method = RequestMethod.GET)
    public String getIngrediente(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("ingrediente", this.ingredienteService.ingredientePerId(id));
    	return "ingrediente";
    }

    @RequestMapping(value = "/ingrediente", method = RequestMethod.GET)
    public String getIngredienti(Model model) {
    		model.addAttribute("ingredienti", this.ingredienteService.tutti());
    		return "ingredienti";
    }
    
    @RequestMapping(value = "/admin/ingrediente", method = RequestMethod.POST)
    public String addIngrediente(@ModelAttribute("ingrediente") Ingrediente ingrediente, 
    									Model model, BindingResult bindingResult) {
    	this.ingredienteValidator.validate(ingrediente, bindingResult);
        if (!bindingResult.hasErrors()) {
        	this.ingredienteService.inserisci(ingrediente);
            model.addAttribute("ingrediente", ingrediente);
            return "ingrediente";
        }
        return "ingredienteForm";
    }
    
    @GetMapping("/admin/deleteIngrediente/{id}")
	public String deleteIngrediente(@PathVariable("id") Long id, Model model) {
    	Ingrediente ingrediente = ingredienteService.ingredientePerId(id);
		List<Piatto> piatti = piattoService.tutti();
		for(Piatto p : piatti) {
			if(p.getIngredienti().contains(ingrediente)) {
				p.getIngredienti().remove(ingrediente);
				piattoService.inserisci(p);
			}
		}
		ingredienteService.deleteIngredienteById(id);
		return "/admin/home";
	}
    
    @Transactional
	@PostMapping("/admin/ingredienteEdited/{id}")
	public String editedIngrediente(@PathVariable Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {

		if(!bindingResult.hasErrors()) {
		Ingrediente vecchioIngrediente = ingredienteService.ingredientePerId(id);

		vecchioIngrediente.setNome(ingrediente.getNome());
		vecchioIngrediente.setOrigine(ingrediente.getOrigine());
		vecchioIngrediente.setDescrizione(ingrediente.getDescrizione());

		ingredienteService.inserisci(vecchioIngrediente);
		model.addAttribute("ingrediente", ingrediente);
		return "ingrediente";
		}
		return "ingredienteEdit";
	}

	@GetMapping("/admin/ingredienteEdit/{id}")
	public String editIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = ingredienteService.ingredientePerId(id);
		model.addAttribute("ingrediente", ingrediente);
		return "ingredienteEdit";
	}
}