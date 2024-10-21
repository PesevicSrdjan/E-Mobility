package net.unibl.pj2.servisi.prevoznasredstva;

import java.io.Serializable;

import net.unibl.pj2.interfejsi.Kvar;

import net.unibl.pj2.interfejsi.PunjenjeBaterije;

/**
 * Apstraktna klasa {@code PrevoznoSredstvo} predstavlja osnovni model za različite tipove prevoznih sredstava.
 * Implementira interfejse {@link PunjenjeBaterije} i {@link Kvar} za punjenje baterije i prijavu kvara.
 * Ova klasa je serijalizabilna, omogućavajući da se njeni objekti serijalizuju i deserijalizuju.
 */
public abstract class PrevoznoSredstvo implements PunjenjeBaterije, Kvar, Serializable
{
	/**
	 * Verzija serijalizacije za klasu.
	 */
	private static final long serialVersionUID = 1L;
	 /**
     * Jedinstveni identifikator prevoznog sredstva.
     */
	protected String id;
	 /**
     * Trenutni nivo baterije prevoznog sredstva.
     * Ovaj atribut je označen kao `transient`, što znači da neće biti serijalizovan.
     */
	protected int trenutniNivoBaterije;

    /**
     * Proizvođač i model prevoznog sredstva.
     */
	protected String proizvodjac, model;
	/**
     * Cijena nabavke prevoznog sredstva.
     */
	protected double cijenaNabavke;
	
	 /**
     * Vraća jedinstveni identifikator prevoznog sredstva.
     * @return ID prevoznog sredstva.
     */
	public String getId() {
		return id;
	}

	 /**
     * Postavlja jedinstveni identifikator prevoznog sredstva.
     * @param id - ID prevoznog sredstva.
     */
	public void setId(String id) {
		this.id = id;
	}

	/**
     * Implementacija metode iz interfejsa {@link PunjenjeBaterije}.
     * Povećava trenutni nivo baterije za zadatu količinu.
     * @param kolicina - količina za koju se baterija puni.
     */
	@Override
	public void puniBateriju(int kolicina)
	{
		trenutniNivoBaterije += kolicina;
	}
	
	/**
     * Implementacija metode iz interfejsa {@link Kvar}.
     * Vraća opis kvara zajedno sa datumom i vremenom.
     * @param opis - opis kvara.
     * @param datumIVrijeme - datum i vrijeme kvara.
     * @return String koji sadrži opis kvara i vrijeme.
     */
	@Override
	public String kvar(String opis, String datumIVrijeme)
	{
		return "Opis Kvara: " + opis + " " + "Datum i Vrijeme: " + datumIVrijeme;
	}
	
	 /**
     * Vraća trenutni nivo baterije prevoznog sredstva.
     * @return trenutni nivo baterije.
     */
	public int getTrenutniNivoBaterije() {
		return trenutniNivoBaterije;
	}

	/**
     * Postavlja trenutni nivo baterije prevoznog sredstva.
     * @param trenutniNivoBaterije - trenutni nivo baterije.
     */
	public void setTrenutniNivoBaterije(int trenutniNivoBaterije) {
		this.trenutniNivoBaterije = trenutniNivoBaterije;
	}

	/**
     * Vraća naziv proizvođača prevoznog sredstva.
     * @return naziv proizvođača.
     */
	public String getProizvodjac() {
		return proizvodjac;
	}

	/**
     * Postavlja naziv proizvođača prevoznog sredstva.
     * @param proizvodjac - naziv proizvođača.
     */
	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}

	/**
     * Vraća naziv modela prevoznog sredstva.
     * @return naziv modela.
     */
	public String getModel() {
		return model;
	}

	/**
     * Postavlja naziv modela prevoznog sredstva.
     * @param model - naziv modela.
     */
	public void setModel(String model) {
		this.model = model;
	}

	/**
     * Vraća cijenu nabavke prevoznog sredstva.
     * @return cijena nabavke.
     */
	public double getCijenaNabavke() {
		return cijenaNabavke;
	}

	/**
     * Postavlja cijenu nabavke prevoznog sredstva.
     * @param cijenaNabavke - cijena nabavke.
     */
	public void setCijenaNabavke(double cijenaNabavke) {
		this.cijenaNabavke = cijenaNabavke;
	}

	/**
     * Podrazumijevani konstruktor.
     */
	public PrevoznoSredstvo()
	{
		super();
	}
	/**
     * Konstruktor za kreiranje prevoznog sredstva sa svim potrebnim parametrima.
     * @param id - jedinstveni identifikator prevoznog sredstva.
     * @param proizvodjac - naziv proizvođača prevoznog sredstva.
     * @param model - naziv modela prevoznog sredstva.
     * @param cijenaNabavke - cijena nabavke prevoznog sredstva.
     * @param trenutniNivoBaterije - trenutni nivo baterije prevoznog sredstva.
     */
	public PrevoznoSredstvo(String id,  String proizvodjac, String model,double cijenaNabavke, int trenutniNivoBaterije)
	{
		this.id = id;
		this.trenutniNivoBaterije = trenutniNivoBaterije;
		this.proizvodjac = proizvodjac;
		this.model = model;
		this.cijenaNabavke = cijenaNabavke;
	}
}
