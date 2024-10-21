/**
 * Automobil.java
 * 
 * Opis: Implementacija klase Automobil kao tip prevoznog sredstva.
 * Autor: Srđan Pešević
 * Datum: 12.07.2024
 */

package net.unibl.pj2.servisi.prevoznasredstva;
import java.io.Serializable;

import net.unibl.pj2.interfejsi.PrevozenjeViseLjudi;

/**
 * Klasa {@code Automobil} predstavlja automobil kao tip prevoznog sredstva.
 * Implementira interfejse {@code PrevozenjeViseLjudi} i {@code Kvar}.
 *  Ova klasa nasljeđuje {@link PrevoznoSredstvo} i dodaje specifične atribute i metode za automobil.
 */

public class Automobil extends PrevoznoSredstvo implements PrevozenjeViseLjudi, Serializable
{
	 /**
	 * Verzija serijalizacije za ovu klasu.
	 */
	private static final long serialVersionUID = 1L;

	/** Datum nabavke automobila. */
	private String datumNabavke;
	
	 /** Opis automobila. */
	private String opis;
	
	/**
     * Vraća datum nabavke automobila.
     * 
     * @return datum nabavke automobila
     */
	public String getDatumNabavke() 
	{
		return datumNabavke;
	}
	/**
     * Postavlja datum nabavke automobila.
     * 
     * @param datumNabavke novi datum nabavke automobila
     */
	public void setDatumNabavke(String datumNabavke) {
		this.datumNabavke = datumNabavke;
	}
	/**
     * Vraća opis automobila.
     * 
     * @return opis automobila
     */
	public String getOpis() {
		return opis;
	}

    /**
     * Postavlja opis automobila.
     * 
     * @param opis novi opis automobila
     */
	public void setOpis(String opis) {
		this.opis = opis;
	}
	/**
     * Konstruktor bez argumenata koji kreira novi {@code Automobil} objekat.
     */
	public Automobil()
	{
		super();
	}
	/**
    * Konstruktor sa argumentima koji kreira novi {@code Automobil} objekat sa zadatim vrednostima.
    * 
    * @param id identifikacioni broj automobila
    * @param proizvodjac proizvođač automobila
    * @param model model automobila
    * @param datumNabavke datum nabavke automobila
    * @param cijenaNabavke cena nabavke automobila
    * @param opis opis automobila
    * @param trenutniNivoBaterije trenutni nivo baterije automobila
    */
	public Automobil(String id, String proizvodjac,String model,String datumNabavke, double cijenaNabavke, 
	String opis,int trenutniNivoBaterije)
	{
		super(id, proizvodjac, model,cijenaNabavke,trenutniNivoBaterije);
		this.datumNabavke = datumNabavke;
		this.opis = opis;
	}
	/**
     * Implementacija metode iz interfejsa {@code PrevozenjeViseLjudi}.
     * 
     * @param brojPutnika broj putnika koji se prevoze
     * @return broj putnika
     */
	@Override
	public int prevozenjeViseLjudi(int brojPutnika)
	{
		return brojPutnika;
	}

	/**
     * Vraća string reprezentaciju objekta {@code Automobil}.
     * 
     * @return string reprezentacija objekta {@code Automobil}
     */
	@Override
	public String toString()
	{
		return "AUTOMOBIL: ID: " + id + ",PROIZVODJAC: " + proizvodjac +  ",MODEL: " + model + ",DATUM NABAVKE: " + datumNabavke + ",CIJENA NABAVKE: " +cijenaNabavke + ",OPIS: " + opis + ",NIVO BATERIJE: " + trenutniNivoBaterije + "%";
	}
}
