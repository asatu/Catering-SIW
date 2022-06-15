package it.uniroma3.siw.catering.controller;

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

import it.uniroma3.siw.catering.service.ChefService;
import it.uniroma3.siw.catering.validator.ChefValidator;
import it.uniroma3.siw.catering.model.Chef;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private ChefValidator chefValidator;

	@RequestMapping(value="/admin/chef", method = RequestMethod.GET)
	public String addChef(Model model) {
		model.addAttribute("chef", new Chef());
		return "chefForm";
	}

	@RequestMapping(value = "/chef/{id}", method = RequestMethod.GET)
	public String getChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.chefPerId(id));
		return "chef";
	}

	@RequestMapping(value = "/chef", method = RequestMethod.GET)
	public String getChefs(Model model) {
		model.addAttribute("chefs", this.chefService.tutti());
		return "chefs";
	}

	@RequestMapping(value = "/admin/chef", method = RequestMethod.POST)
	public String addChef(@ModelAttribute("chef") Chef chef, 
			Model model, BindingResult bindingResult) {
		this.chefValidator.validate(chef, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.chefService.inserisci(chef);
			model.addAttribute("chef", this.chefService.chefPerId(chef.getId()));
			return "chef";
		}
		return "chefForm";
	}

	@GetMapping("/admin/deleteChef/{id}")
	public String deleteChef(@PathVariable("id") Long id, Model model) {
		chefService.deleteChefById(id);
		model.addAttribute("chefs", chefService.tutti());
		return "/admin/home";
	}

	@Transactional
	@PostMapping("/admin/chefEdited/{id}")
	public String editedChef(@PathVariable Long id, @Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResults, Model model) {

		if(!bindingResults.hasErrors()) {
			Chef vecchioChef = chefService.chefPerId(id);
			vecchioChef.setId(chef.getId());
			vecchioChef.setNome(chef.getNome());
			vecchioChef.setCognome(chef.getCognome());
			vecchioChef.setNazionalita(chef.getNazionalita());

			this.chefService.inserisci(vecchioChef);
			model.addAttribute("chef", chef);
			return "chef";
		}
		return "chefEdit";
	}

	@GetMapping("/admin/chefEdit/{id}")
	public String editChef(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.chefPerId(id);
		model.addAttribute("chef", chef);
		return "chefEdit";
	}
}
