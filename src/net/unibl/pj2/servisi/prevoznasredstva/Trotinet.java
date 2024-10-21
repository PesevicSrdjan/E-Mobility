package net.unibl.pj2.servisi.prevoznasredstva;

import java.io.Serializable;

/**
 * Klasa {@code Trotinet} predstavlja trotinet kao tip prevoznog sredstva.
 * Ova klasa nasljeđuje {@link PrevoznoSredstvo} i dodaje specifične atribute i metode za trotinet.
 */
public class Trotinet extends PrevoznoSredstvo implements Serializable
{
	/**
	 * Verzija serijalizacije za ovu klasu.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Maksmimalna brzina trotineta.
     */
	private int maksimalnaBrzina;
	
	/**
     * Vraća maksimalnu brzinu trotineta.
     * @return Maksimalna brzina trotineta.
     */
	public int getMaksimalnaBrzina() {
		return maksimalnaBrzina;
	}

	/**
     * Postavlja maksimalnu brzinu trotineta.
     * @param maksimalnaBrzina - Maksimalna brzina trotineta koja se postavlja.
     */
	public void setMaksimalnaBrzina(int maksimalnaBrzina) {
		this.maksimalnaBrzina = maksimalnaBrzina;
	}

	/**
     * Podrazumijevani konstruktor.
     */
	public Trotinet()
	{
		super();
	}
	 /**
     * Konstruktor za kreiranje trotineta sa svim potrebnim parametrima.
     * @param id - jedinstveni identifikator trotineta
     * @param proizvodjac - naziv proizvođača trotineta
     * @param model - model trotineta
     * @param cijenaNabavke - cena nabavke trotineta
     * @param maksimalnaBrzina - maksimalna brzina trotineta
     * @param trenutniNivoBaterije - trenutni nivo baterije trotineta u procentima
     */
	public Trotinet(String id, String proizvodjac , String model ,double cijenaNabavke, 
			int maksimalnaBrzina, int trenutniNivoBaterije)
			{
				super(id, proizvodjac, model,cijenaNabavke,trenutniNivoBaterije);
				this.maksimalnaBrzina = maksimalnaBrzina;
			}
	
	/**
     * Vraća tekstualni prikaz trotineta sa svim atributima.
     * @return tekstualni prikaz trotineta
     */
	@Override
	public String toString()
	{
		return "TROTINET: ID: " + id + ",PROIZVODJAC: " + proizvodjac +  ",MODEL: " + model + ",CIJENA NABAVKE: " +cijenaNabavke + ",MAKSIMALNA BRZINA: " + maksimalnaBrzina + ",NIVO BATERIJE: " + trenutniNivoBaterije + "%";
	}
	

}
