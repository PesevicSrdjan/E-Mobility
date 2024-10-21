package net.unibl.pj2.servisi.prevoznasredstva;

import java.io.Serializable;

/**
 * Klasa {@code Bicikl} predstavlja bicikl kao tip prevoznog sredstva.
 * Ova klasa nasljeđuje {@link PrevoznoSredstvo} i dodaje specifične atribute i metode za bicikl.
 */
public class Bicikl extends PrevoznoSredstvo implements Serializable
{
	/**
	 * Verzija serijalizacije za ovu klasu.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Autonomija (domet) bicikla.
     */
	private int autonomija;
	
	/**
     * Vraća autonomiju (domet) bicikla.
     * @return autonomija - autonomija bicikla.
     */
	public int getAutonomija() {
		return autonomija;
	}

	/**
     * Postavlja autonomiju bicikla.
     * @param autonomija - autonomija bicikla koja se postavlja.
     */
	public void setAutonomija(int autonomija) {
		this.autonomija = autonomija;
	}

	/**
     * Podrazumijevani konstruktor.
     */
	public Bicikl()
	{
		super();
	}
	/**
     * Konstruktor za kreiranje bicikla sa svim potrebnim parametrima.
     * @param id - jedinstveni identifikator bicikla
     * @param proizvodjac - naziv proizvođača bicikla
     * @param model - model bicikla
     * @param cijenaNabavke - cena nabavke bicikla
     * @param autonomija - autonomija bicikla
     * @param trenutniNivoBaterije - trenutni nivo baterije bicikla u procentima
     */
	public Bicikl(String id, String proizvodjac, String model,double cijenaNabavke, 
			int autonomija, int trenutniNivoBaterije)
			{
				super(id, proizvodjac, model,cijenaNabavke,trenutniNivoBaterije);
				this.autonomija = autonomija;
			}
	
	/**
     * Vraća tekstualni prikaz bicikla sa svim atributima.
     * @return tekstualni prikaz bicikla
     */
	@Override
	public String toString()
	{
		return "BICIKL: ID: " + id + 
				",PROIZVODJAC: " + proizvodjac +  
				",MODEL: " + model + 
				",CIJENA NABAVKE: " + cijenaNabavke + 
				",AUTONOMIJA: " + autonomija + 
				",NIVO BATERIJE: " + trenutniNivoBaterije + "%";
	}
}