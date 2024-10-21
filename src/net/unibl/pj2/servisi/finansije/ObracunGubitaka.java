package net.unibl.pj2.servisi.finansije;

import java.util.List;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import net.unibl.pj2.izuzeci.NepostojecePrevoznoSredstvoException;
import net.unibl.pj2.servisi.iznajmljivanje.Iznajmljivanje;
import net.unibl.pj2.servisi.prevoznasredstva.Automobil;
import net.unibl.pj2.servisi.prevoznasredstva.Bicikl;
import net.unibl.pj2.servisi.prevoznasredstva.PrevoznoSredstvo;
import net.unibl.pj2.servisi.prevoznasredstva.Trotinet;
import net.unibl.pj2.servisi.serijalizacija.Serijalizacija;

/**
 * Klasa koja predstavlja obračun gubitaka za prevozna sredstva.
 * Ova klasa se koristi za izračunavanje gubitaka, prihoda, i troškova
 * održavanja i popravke, kao i za serijalizaciju najvećih gubitaka.
 */
public class ObracunGubitaka implements Serializable
{
    /**
	 * Verzija serijalizacije za ovu klasu.
	 */
	private static final long serialVersionUID = 1L;
	private PrevoznoSredstvo prevoznoSredstvo;
    private double gubitak, prihod;
    private double troskoviOdrzavanja;
    private double troskoviPopravke;
    
    /**
     * Statičke liste koje čuvaju obračune gubitaka za različite tipove prevoznih sredstava.
     */
    private static List<ObracunGubitaka> listaAutomobila = new ArrayList<>();
    private static List<ObracunGubitaka> listaBicikala  = new ArrayList<>();
    private static List<ObracunGubitaka> listaTrotineta = new ArrayList<>();
    
    /**
     * Vraća prihod za obračun gubitaka.
     * @return prihod
     */
    public double getPrihod() {
		return prihod;
	}
    
    /**
     * Postavlja prihod za obračun gubitaka.
     * @param prihod - prihod koji se postavlja
     */
	public void setPrihod(double prihod) {
		this.prihod = prihod;
	}
	/**
     * Konstruktor za kreiranje obračuna gubitaka sa svim potrebnim parametrima.
     * @param prevoznoSredstvo - prevozno sredstvo koje se obračunava
     * @param prihod - prihod od iznajmljivanja
     * @param troskoviOdrzavanja - troškovi održavanja
     * @param troskoviPopravke - troškovi popravke
     * @param gubitak - gubitak na osnovu izračunatih troškova
     */
    public ObracunGubitaka(PrevoznoSredstvo prevoznoSredstvo,double prihod,double troskoviOdrzavanja, double troskoviPopravke, double gubitak) 
    {
        this.prevoznoSredstvo = prevoznoSredstvo;
        this.prihod = prihod;
        this.gubitak = gubitak;
        this.troskoviOdrzavanja = troskoviOdrzavanja;
        this.troskoviPopravke = troskoviPopravke;
    }

    /**
     * Vraća prevozno sredstvo za koje se vrši obračun gubitaka.
     * @return prevoznoSredstvo
     */
    public PrevoznoSredstvo getPrevoznoSredstvo() {
        return prevoznoSredstvo;
    }

    /**
     * Postavlja prevozno sredstvo za koje se vrši obračun gubitaka.
     * @param prevoznoSredstvo - prevozno sredstvo koje se postavlja
     */
    public void setPrevoznoSredstvo(PrevoznoSredstvo prevoznoSredstvo) {
        this.prevoznoSredstvo = prevoznoSredstvo;
    }

    /**
     * Vraća gubitak za prilikom obračuna gubitaka.
     * @return gubitak
     */
    public double getGubitak() {
        return gubitak;
    }

    /**
     * Postavlja gubitak za obračun gubitaka.
     * @param gubitak - gubitak koji se postavlja
     */
    public void setGubitak(double gubitak) {
        this.gubitak = gubitak;
    }

    /**
     * Vraća troškove održavanja obračuna gubitaka.
     * @return troskoviOdrzavanja
     */
    public double getTroskoviOdrzavanja() {
        return troskoviOdrzavanja;
    }

    /**
     * Postavlja troškove održavanja obračuna gubitaka.
     * @param troskoviOdrzavanja - troškovi održavanja koji se postavljaju
     */
    public void setTroskoviOdrzavanja(double troskoviOdrzavanja) {
        this.troskoviOdrzavanja = troskoviOdrzavanja;
    }

    /**
     * Vraća troškove popravke obračuna gubitaka.
     * @return troskoviPopravke
     */
    public double getTroskoviPopravke() {
        return troskoviPopravke;
    }

    /**
     * Postavlja troškove popravke obračuna gubitaka.
     * @param troskoviPopravke - troškovi popravke koji se postavljaju
     */
    public void setTroskoviPopravke(double troskoviPopravke) {
        this.troskoviPopravke = troskoviPopravke;
    }

    /**
     * Vraća listu obračuna gubitaka za automobile.
     * @return listaAutomobila
     */
    public static List<ObracunGubitaka> getListaAutomobila() {
        return listaAutomobila;
    }

    /**
     * Vraća listu obračuna gubitaka za bicikle.
     * @return listaBicikala
     */
    public static List<ObracunGubitaka> getListaBicikala() {
        return listaBicikala;
    }

    /**
     * Vraća listu obračuna gubitaka za trotinete.
     * @return listaTrotineta
     */
    public static List<ObracunGubitaka> getListaTrotineta() {
        return listaTrotineta;
    }

    /**
     * Izračunava trošak popravke na osnovu tipa prevoznog sredstva.
     * @param iznajmljivanje - objekat iznajmljivanja na osnovu kojeg se izračunava trošak popravke
     * @return trošak popravke
     */
    private static double izracunajTrosakPopravke(Iznajmljivanje iznajmljivanje) throws NepostojecePrevoznoSredstvoException
    {	
        if (iznajmljivanje.getPrevoznoSredstvo() instanceof Automobil) 
        {
            return iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.07;
        } 
        else if (iznajmljivanje.getPrevoznoSredstvo() instanceof Bicikl) 
        {
            return iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.04;
            
        } 
        else if (iznajmljivanje.getPrevoznoSredstvo() instanceof Trotinet) 
        {
            return iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.02;
        }
        else 
        {
        	throw new NepostojecePrevoznoSredstvoException("Nepoznata vrsta prevoznog sredstva!");
		}
    }

    /**
     * Dodaje obračun gubitaka u odgovarajuću listu na osnovu tipa prevoznog sredstva.
     * @param obracun - objekat obračuna gubitaka koji se dodaje
     */
    private static void dodajUListu(ObracunGubitaka obracun) throws NepostojecePrevoznoSredstvoException
    {	
        if (obracun.getPrevoznoSredstvo() instanceof Automobil) 
        {
            listaAutomobila.add(obracun);
        } 
        else if (obracun.getPrevoznoSredstvo() instanceof Bicikl) 
        {
            listaBicikala.add(obracun);
        } 
        else if (obracun.getPrevoznoSredstvo() instanceof Trotinet) 
        {
            listaTrotineta.add(obracun);
        }
        else 
        {
        	throw new NepostojecePrevoznoSredstvoException("Nepoznata vrsta prevoznog sredstva");
		}
    }
    /**
     * Izračunava gubitke za iznajmljivanje i dodaje obračun gubitaka u odgovarajuću listu.
     * @param iznajmljivanje - objekat iznajmljivanja za koji se vrši obračun gubitaka
     * @param ukupnoZaPlacanje - ukupni iznos koji treba da se plati
     */
    public static void izracunajGubitke(Iznajmljivanje iznajmljivanje, double ukupnoZaPlacanje) throws NepostojecePrevoznoSredstvoException
    {
        double troskoviOdrzavanja = ukupnoZaPlacanje * 0.2;
        double troskoviPopravke = iznajmljivanje.isKvar() ? izracunajTrosakPopravke(iznajmljivanje) : 0.0;
        double gubitak = ukupnoZaPlacanje - troskoviOdrzavanja - troskoviPopravke;

        ObracunGubitaka obracun = new ObracunGubitaka(iznajmljivanje.getPrevoznoSredstvo(),ukupnoZaPlacanje,troskoviOdrzavanja, troskoviPopravke, gubitak);
        dodajUListu(obracun);
    }

    /**
     * Pronalazi najveći gubitak iz liste obračuna gubitaka.
     * @param lista - lista obračuna gubitaka iz koje se pronalazi najveći gubitak
     * @return lista obračuna gubitaka sa najvećim gubitkom
     */
    private static List<ObracunGubitaka> nadjiNajveciGubitak(List<ObracunGubitaka> lista)
    {
    	// Kreiranje nove liste, u koju će biti smještena prevozna sredstva koja su donijela najveći gubitak.
    	List<ObracunGubitaka> listaGubitaka = new ArrayList<>();
    	
    	// Ako je parametar metode prazan, vraćamo null.
        if (lista == null || lista.isEmpty()) 
        {
            return null;
        }
        
        // Pronalazak prevoznog sredstva sa najvećim obračunatim gubitkom.
        ObracunGubitaka najveciGubitak = lista.get(0);
        for (ObracunGubitaka obracun : lista) 
        {
            if (obracun.getGubitak() < najveciGubitak.getGubitak()) 
            {   	
                najveciGubitak = obracun;
            }
        }
        
        // Još jedan prolazak kroz petlju, kako bi se dodala u listu i ona prevozna sredstva koja imaju
        // jednak (najveci) gubitak.
        for (ObracunGubitaka obracun : lista) 
        {
            if (obracun.getGubitak() == najveciGubitak.getGubitak()) 
            {
                listaGubitaka.add(obracun);
            }
        }
        return listaGubitaka;
    }

    /**
     * Serijalizuje najveće gubitke za svaki tip prevoznog sredstva u zadati folder.
     * @param folder - putanja do foldera u kojem će biti sačuvani serijalizovani objekti.
     * NAPOMENA: Ovdje se serijalizuje objekat ObracunGubitaka, koji sadrži atribut klase PrevoznoSredstvo.
     * Razlog iz kojeg se serijalizuje objekat klase ObracunGubitaka, jeste taj, kako bi imali uvid u iznos gubitka, troškova, prihoda itd, tj.
     * kako bi se to takođe prikazalo na GUI prozoru.
     */
    public static void serijalizujNajveceGubitke(String folder) 
    {
        // Pronalaze se prevozna sredstva koja su donijela najveće gubitke za različite tipove prevoznih sredstava.
        List<ObracunGubitaka> najveciGubiciAutomobila = nadjiNajveciGubitak(listaAutomobila);
        List <ObracunGubitaka> najveciGubiciBicikala = nadjiNajveciGubitak(listaBicikala);
        List <ObracunGubitaka> najveciGubiciTrotineta = nadjiNajveciGubitak(listaTrotineta);

        // Prolazi se kroz listu automobila koji su donijeli najviše gubitaka i vrši se serijalizacija objekata.
        for(ObracunGubitaka automobil: najveciGubiciAutomobila)
        {
        	if (automobil != null) 
        	{
        		// Formiranje imena datoteke za serijalizaciju.
                String imeDatoteke = folder + File.separator + 
                    ((Automobil) automobil.getPrevoznoSredstvo()).getId() + ".ser";
                
                // Serijalizacija objekta.
                Serijalizacija.serijalizujObjekat(automobil, imeDatoteke);
            }
        	
        }
        // Prolazi se kroz listu bicikala koji su donijeli najviše gubitaka i vrši se serijalizacija objekata.
        for(ObracunGubitaka bicikl : najveciGubiciBicikala)
        {
        	if (bicikl != null) 
        	{
        		// Formiranje imena datoteke za serijalizaciju.
                String imeDatoteke = folder + File.separator + 
                    ((Bicikl) bicikl.getPrevoznoSredstvo()).getId() + ".ser";
                
                // Serijalizacija objekta.
                Serijalizacija.serijalizujObjekat(bicikl, imeDatoteke);
            }
        }
        // Prolazi se kroz listu bicikala koji su donijeli najviše gubitaka i vrši se serijalizacija objekata.
        for(ObracunGubitaka trotinet : najveciGubiciTrotineta)
        {
	        if (trotinet != null) 
	        {
	        	// Formiranje imena datoteke za serijalizaciju.
	            String imeDatoteke = folder + File.separator + 
	                ((Trotinet) trotinet.getPrevoznoSredstvo()).getId() + ".ser";
	         // Serijalizacija objekta.
	            Serijalizacija.serijalizujObjekat(trotinet, imeDatoteke);
	        }
        }
    }   
}