package net.unibl.pj2.ekrani.mapa;

import java.awt.Color;

/**
 * Klasa <code>PoljeInfo</code> proširuje klasu <code>Polje</code> i dodaje dodatne informacije 
 * koje su korisne za rad sa mapom u kontekstu višestrukih niti (threadova).
 * Ova klasa omogucava pracenje stanja svakog polja, ukljucujuci boju, labelu, 
 * i da li je polje posljednje ili indeksirano.
 */
public class PoljeInfo extends Polje
{
	private Color boja; // Boja koja se koristi za prikaz polja na mapi
	private String labela;  // Labela koja opisuje polje
	private boolean posljednje, indeksirano; // Da li je polje poslednje u nizu ili je indeksirano.

	/**
     * Vraca da li je polje posljednje u nizu.
     * 
     * @return <code>true</code> ako je polje posljednje, <code>false</code> inace
     */
    public boolean isPosljednje() {
		return posljednje;
	}

    /**
    * Postavlja da li je polje posljednje u nizu.
    * 
    * @param posljednje <code>true</code> ako je polje posljednje, <code>false</code> inace
    */
	public void setPosljednje(boolean posljednje) {
		this.posljednje = posljednje;
	}

	 /**
     * Vraca da li je polje trenutno indeksirano.
     * 
     * @return <code>true</code> ako je polje indeksirano, <code>false</code> inace
     */
	public boolean isIndeksirano() {
		return indeksirano;
	}

	 /**
     * Postavlja da li je polje trenutno indeksirano.
     * 
     * @param indeksirano <code>true</code> ako je polje indeksirano, <code>false</code> inace
     */
	public void setIndeksirano(boolean indeksirano) {
		this.indeksirano = indeksirano;
	}
	
	/**
     * Konstruktor koji inicijalizuje sve atribute polja sa datim vrijednostima.
     * 
     * @param boja Boja polja
     * @param labela Labela polja
     * @param posljednje Da li je polje posljednje u nizu
     * @param zauzeto Da li je polje indeksirano
     */
	public PoljeInfo(Color boja, String labela, boolean posljednje, boolean indeksirano) {
        this.boja = boja;
        this.labela = labela;
        this.posljednje = posljednje;
        this.indeksirano = indeksirano;   
    }

	/**
     * Vraca boju polja.
     * 
     * @return Boja polja
     */
    public Color getBoja() {
        return boja;
    }

    /**
     * Postavlja boju polja.
     * 
     * @param boja Boja polja
     */
    public void setBoja(Color boja) {
        this.boja = boja;
    }
    /**
     * Vraca labelu polja.
     * 
     * @return Labela polja
     */
    public String getLabela() {
        return labela;
    }
    /**
     * Postavlja labelu polja.
     * 
     * @param labela Labela polja
     */
    public void setLabela(String labela) {
        this.labela = labela;
    }
}