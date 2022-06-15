package it.uniroma3.siw.catering.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;
import it.uniroma3.siw.catering.service.PiattoService;
import it.uniroma3.siw.catering.validator.BuffetValidator;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.model.Buffet;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private PiattoService piattoService;

	@Autowired
	private ChefService chefService;

	@Autowired
	private BuffetValidator buffetValidator;

	@RequestMapping(value="/admin/buffet", method = RequestMethod.GET)
    public String addBuffet(Model model) {
		List<Piatto> piatti = piattoService.tutti();
		model.addAttribute("piattiProposti", piatti);
		model.addAttribute("chefs", chefService.tutti());
		model.addAttribute("buffet", new Buffet());
        return "buffetForm";
    }

    @RequestMapping(value = "/buffet/{id}", method = RequestMethod.GET)
    public String getBuffet(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("buffet", this.buffetService.buffetPerId(id));
    	return "buffet";
    }

    @RequestMapping(value = "/buffet", method = RequestMethod.GET)
    public String getBuffets(Model model) {
    		model.addAttribute("buffets", this.buffetService.tutti());
    		return "buffets";
    }
    
    @RequestMapping(value = "/admin/buffet", method = RequestMethod.POST)
    public String addBuffet(@ModelAttribute("buffet") Buffet buffet, 
    									Model model, BindingResult bindingResult) {
    	this.buffetValidator.validate(buffet, bindingResult);
    	
        if (!bindingResult.hasErrors()) {
        	this.buffetService.inserisci(buffet);
            model.addAttribute("buffet", buffet);
            return "buffet";
        }
        List<Piatto> piatti = piattoService.tutti();
		model.addAttribute("piattiProposti", piatti);
		model.addAttribute("chefs", chefService.tutti());
        return "buffetForm";
    }
    
    @GetMapping("/admin/deleteBuffet/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
    	Buffet buffet = buffetService.buffetPerId(id);
		List<Chef> chef = chefService.tutti();
		for(Chef c : chef) {
			if(c.getBuffetsProposti().contains(buffet)) {
				c.getBuffetsProposti().remove(buffet);
				chefService.inserisci(c);
			}
		}
		buffetService.deleteBuffetById(id);
		return "/admin/home";
	}
    
    @Transactional
	@PostMapping("/admin/buffetEdited/{id}")
	public String editChef(@PathVariable Long id, @Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResults, Model model) {

		if(!bindingResults.hasErrors()) {
			Buffet vecchioBuffet = buffetService.buffetPerId(id);
			vecchioBuffet.setId(buffet.getId());
			vecchioBuffet.setNome(buffet.getNome());
			vecchioBuffet.setDescrizione(buffet.getDescrizione());
			vecchioBuffet.setChef(buffet.getChef());
			vecchioBuffet.setPiattiProposti(buffet.getPiattiProposti());

			this.buffetService.inserisci(vecchioBuffet);
			model.addAttribute("buffet", buffet);
			return "buffet";
		}
		return "buffetEdit";

	}

	@GetMapping("/admin/buffetEdit/{id}")
	public String modifyBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = buffetService.buffetPerId(id);
		model.addAttribute("buffet", buffet);
		model.addAttribute("chefList", chefService.tutti());
		model.addAttribute("piattoList", piattoService.tutti());
		return "buffetEdit";
	}
}