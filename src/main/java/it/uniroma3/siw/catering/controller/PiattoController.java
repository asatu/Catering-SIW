package it.uniroma3.siw.catering.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

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

import it.uniroma3.siw.catering.service.PiattoService;
import it.uniroma3.siw.catering.validator.PiattoValidator;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.model.Piatto;

@Controller
public class PiattoController {

	@Autowired
	private PiattoService piattoService;

	@Autowired
	private PiattoValidator piattoValidator;

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private BuffetService buffetService;

	@RequestMapping(value="/admin/piatto", method = RequestMethod.GET)
	public String addPiatto(Model model) {
		model.addAttribute("piatto", new Piatto());
		model.addAttribute("ingredienti", ingredienteService.tutti());
		return "piattoForm";
	}

	@RequestMapping(value = "/piatto/{id}", method = RequestMethod.GET)
	public String getPiatto(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", this.piattoService.piattoPerId(id));
		return "piatto";
	}

	@RequestMapping(value = "/piatto", method = RequestMethod.GET)
	public String getPiatti(Model model) {
		model.addAttribute("piatti", this.piattoService.tutti());
		return "piatti";
	}

	@RequestMapping(value = "/admin/piatto", method = RequestMethod.POST)
	public String addPiatto(@ModelAttribute("piatto") Piatto piatto, 
			Model model, BindingResult bindingResult) {
		this.piattoValidator.validate(piatto, bindingResult);
		if (!bindingResult.hasErrors()) {
			piattoService.inserisci(piatto);
			model.addAttribute("piatto", piatto);
			model.addAttribute("ingredienti", piatto.getIngredienti());
			return "piatto";
		}
		model.addAttribute("ingredienti", ingredienteService.tutti());
		return "piattoForm";
	}

	@GetMapping("/admin/deletePiatto/{id}")
	public String deletePiatto(@PathVariable("id") Long id, Model model) {
		
		Piatto piatto = piattoService.piattoPerId(id);
		List<Buffet> buffets = buffetService.tutti();
		for(Buffet b : buffets) {
			if(b.getPiattiProposti().contains(piatto)) {
				b.getPiattiProposti().remove(piatto);
				buffetService.inserisci(b);
			}
		}
		piattoService.deletePiattoById(id);
		return "/admin/home";
	}
	
	@Transactional
    @PostMapping("/admin/piattoEdited/{id}")
    public String editedPiatto(@PathVariable("id") Long id, @Valid @ModelAttribute Piatto piatto, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {

            Piatto vecchioPiatto = piattoService.piattoPerId(id);
            vecchioPiatto.setNome(vecchioPiatto.getNome());
            vecchioPiatto.setDescrizione(vecchioPiatto.getDescrizione());
            vecchioPiatto.setIngredienti(vecchioPiatto.getIngredienti());

            piattoService.inserisci(piatto);
            model.addAttribute("piatto", piatto);

            return "piatto";
        }
        return "piattoEdit";
    }


    @GetMapping("/admin/piattoEdit/{id}")
    public String editPiatto(@PathVariable("id") Long id, Model model) {
        Piatto piatto = piattoService.piattoPerId(id);
        model.addAttribute("piatto", piatto);
        model.addAttribute("ingredienti", ingredienteService.tutti());
        return "piattoEdit";
    }
}
