package it.uniroma3.siw.catering.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(
				name = "UniqueNameAndNationality",
				columnNames = {"nome", "cognome", "nazionalita"})})
public class Chef {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	@NotBlank
	private String nazionalita;

	@OneToMany(mappedBy = "chef")
	private List<Buffet> buffetsProposti;

	@Override
	public String toString () {
		return id + " " + nome + " " + cognome + ", " + nazionalita;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public List<Buffet> getBuffetsProposti() {
		return buffetsProposti;
	}

	public void setBuffetsProposti(List<Buffet> buffetsProposti) {
		this.buffetsProposti = buffetsProposti;
	}
}