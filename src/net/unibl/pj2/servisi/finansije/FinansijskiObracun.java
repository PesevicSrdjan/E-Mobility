package net.unibl.pj2.servisi.finansije;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import net.unibl.pj2.ekrani.mapa.Polje;
import net.unibl.pj2.izuzeci.NeispravanPronalazakPutanjeException;
import net.unibl.pj2.izuzeci.NepostojecePrevoznoSredstvoException;
import net.unibl.pj2.servisi.iznajmljivanje.CitacKonfiguracioneDatoteke;
import net.unibl.pj2.servisi.iznajmljivanje.Iznajmljivanje;
import net.unibl.pj2.servisi.prevoznasredstva.Automobil;
import net.unibl.pj2.servisi.prevoznasredstva.Bicikl;
import net.unibl.pj2.servisi.prevoznasredstva.Trotinet;
import net.unibl.pj2.servisi.putanja.NajkracaPutanjaMatrice;
/**
 * Klasa FinansijskiObracun pruža funkcionalnosti za obračun cijena i kreiranje računa
 * na osnovu iznajmljivanja vozila. Sadrži metode za izračunavanje osnovne cijene,
 * iznosa i kreiranje računa.
 */
public class FinansijskiObracun 
{
	/**
     * Metoda izračunava ukupnu cenu iznajmljivanja na osnovu unijetih podataka.
     *
     * @param iznajmljivanje Objekat koji sadrži sve podatke o iznajmljivanju.
     */
	public static void izracunajCijenu(Iznajmljivanje iznajmljivanje)
	{
		try 
		{
			if(iznajmljivanje == null)
				throw new IllegalArgumentException("Argument 'iznajmljivanje' ne može biti null.");
			// Kreira se čitač konfiguracione datoteke za dohvatanje parametara.
			CitacKonfiguracioneDatoteke citac = new CitacKonfiguracioneDatoteke("properties.txt");
			
			// Deklaracija potrebnih varijabli za obračun cijene.
			double osnovnaCijena;
			double popust = 0.0;
			double promocija = 0.0;
			double udaljenost = 0.0;
			double vrijemeUzegDijelaGrada = 0.0;
			double vrijemeSiregDijelaGrada = 0.0;
			double iznos = 0.0;
			double ukupnoZaPlacanje = 0.0;
			// Uvećava se broj iznajmljivanja zbog praćenja popusta na svakom desetom iznajmljivanju.
			Iznajmljivanje.brojIznajmljivanja++;
		
		
			// Pronalazak najkraće putanje između početnog i odredišnog polja mape.
			List<Polje> listaPolja = NajkracaPutanjaMatrice.pronalazakNajkracePutanje(iznajmljivanje);

			if(listaPolja == null)
				throw new NeispravanPronalazakPutanjeException("Lista polja je jednaka null! Došlo je do greške u pronalaženju putanje.");
			
			// Provjera da li je vozilo pokvareno. Ako jeste, osnovna cijena je 0.
			if(iznajmljivanje.isKvar())
			{
				osnovnaCijena = 0; 
			}
			else 
			{
				// Izračunavanje osnovne cijene na osnovu trajanja i tipa vozila.
				double sekundTrajanjaJednogPolja = (double)iznajmljivanje.getTrajanjeKoriscenja() / listaPolja.size();
				osnovnaCijena = izracunajOsnovnuCijenu(iznajmljivanje, citac, sekundTrajanjaJednogPolja);
				
				// Obračun udaljenosti i vremena u užem i širem dijelu grada.
				for(Polje polje : listaPolja)
				{
					if(!polje.uziDioGrada())
					{
						udaljenost += osnovnaCijena * citac.dohvatiParametar("DISTANCE_WIDE",Double.class);
						vrijemeSiregDijelaGrada += sekundTrajanjaJednogPolja;
					}
					else 
					{
						udaljenost += osnovnaCijena * citac.dohvatiParametar("DISTANCE_NARROW",Double.class);
						vrijemeUzegDijelaGrada += sekundTrajanjaJednogPolja;
					}
				}
				 // Provjera da li je aktivna promocija i obračun popusta promocije.
				if (iznajmljivanje.isPromocija()) 
				{
					promocija = osnovnaCijena * citac.dohvatiParametar("DISCOUNT_PROM",Double.class);
		        }
				 // Svako deseto iznajmljivanje dobija dodatni popust.
				if(Iznajmljivanje.brojIznajmljivanja % 10 == 0)
				{
					popust = osnovnaCijena * citac.dohvatiParametar("DISCOUNT",Double.class);
					
				}
				// Obračun konačnog iznosa za plaćanje.
				iznos = izracunajIznos(iznajmljivanje, citac, vrijemeSiregDijelaGrada, vrijemeUzegDijelaGrada);
				ukupnoZaPlacanje = iznos - popust - promocija;
			}
			// Kreira se račun i obračunavaju se gubici.
			kreirajRacun(iznajmljivanje, osnovnaCijena, popust, promocija, udaljenost, iznos, ukupnoZaPlacanje);
			ObracunGubitaka.izracunajGubitke(iznajmljivanje, ukupnoZaPlacanje);
		}
		catch (NepostojecePrevoznoSredstvoException npse) 
		{
			System.out.println("Greška u klasi Finansijski Obračun! " + npse.getMessage());
		}
		catch (IllegalArgumentException iae)
		{
			// Ispis greške ako dođe do problema prilikom obračuna.
			System.out.println("Greška u klasi Finansijski Obračun! " + iae.getMessage());
		}
		catch (NeispravanPronalazakPutanjeException nppe) {
			System.out.println("Greška u klasi Finansijski Obračun!" + nppe.getMessage());
		}
		
	}
	/**
     * Privatna metoda koja izračunava osnovnu cijenu iznajmljivanja na osnovu trajanja i tipa vozila.
     *
     * @param iznajmljivanje Objekat koji sadrži podatke o iznajmljivanju.
     * @param citac Čitač konfiguracione datoteke za dohvatanje parametara.
     * @param sekundTrajanjaJednogPolja Trajanje jednog polja u sekundama.
     * @return Osnovna cijena iznajmljivanja.
     * @throws Exception Ako prevozno sredstvo nije prepoznato.
     */
	private static double izracunajOsnovnuCijenu(Iznajmljivanje iznajmljivanje, 
			CitacKonfiguracioneDatoteke citac, double sekundTrajanjaJednogPolja) throws NepostojecePrevoznoSredstvoException
	{
		double osnovnaCijena;
		
		// Određivanje osnovne cene na osnovu tipa vozila.
		if(iznajmljivanje.getPrevoznoSredstvo() instanceof Automobil)
		{
			osnovnaCijena = sekundTrajanjaJednogPolja * citac.dohvatiParametar("CAR_UNIT_PRICE", Double.class);
		}
		else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Bicikl)
		{
			osnovnaCijena = sekundTrajanjaJednogPolja * citac.dohvatiParametar("BIKE_UNIT_PRICE",Double.class);
		}
		else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Trotinet)
		{
			osnovnaCijena = sekundTrajanjaJednogPolja * citac.dohvatiParametar("SCOOTER_UNIT_PRICE",Double.class);
		}
		else 
		{
			// Ako je vozilo nepoznato, baca se izuzetak.
			throw new NepostojecePrevoznoSredstvoException("Metoda: IzracunajOsnovnuCijenu: Nepoznato prevozno sredstvo!");
		}
		
		return osnovnaCijena;
	}
	/**
     * Privatna metoda koja izračunava konačni iznos za plaćanje na osnovu vremena
     * provedenog u užem i širem delu grada.
     *
     * @param iznajmljivanje Objekat koji sadrži podatke o iznajmljivanju.
     * @param citac Čitač konfiguracione datoteke za dohvatanje parametara.
     * @param vrijemeSiregDijelaGrada Vrijeme provedeno u širem dijelu grada.
     * @param vrijemeUzegDijelaGrada Vrjeme provedeno u užem dijelu grada.
     * @return Konačni iznos za plaćanje.
     */
	private static double izracunajIznos(Iznajmljivanje iznajmljivanje, 
			CitacKonfiguracioneDatoteke citac, double vrijemeSiregDijelaGrada, 
			double vrijemeUzegDijelaGrada) throws NepostojecePrevoznoSredstvoException
	{
		
		double iznos = 0.0;
		
		 // Obračun iznosa na osnovu tipa vozila i vremena provedenog u različitim dijelovima grada.
		if(iznajmljivanje.getPrevoznoSredstvo() instanceof Automobil)
		{
			iznos = ((citac.dohvatiParametar("CAR_UNIT_PRICE", Double.class) * 
					vrijemeSiregDijelaGrada) * citac.dohvatiParametar("DISTANCE_WIDE",Double.class)) 
					+ ((citac.dohvatiParametar("CAR_UNIT_PRICE", Double.class) * 
							vrijemeUzegDijelaGrada) * citac.dohvatiParametar("DISTANCE_NARROW",Double.class));
		}
		else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Bicikl)
		{
			iznos = ((citac.dohvatiParametar("BIKE_UNIT_PRICE", Double.class) * 
					vrijemeSiregDijelaGrada) * citac.dohvatiParametar("DISTANCE_WIDE",Double.class)) 
					+ ((citac.dohvatiParametar("BIKE_UNIT_PRICE", Double.class) * 
							vrijemeUzegDijelaGrada) * citac.dohvatiParametar("DISTANCE_NARROW",Double.class));
		}
		else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Trotinet)
		{
			iznos = ((citac.dohvatiParametar("SCOOTER_UNIT_PRICE", Double.class) * 
					vrijemeSiregDijelaGrada) * citac.dohvatiParametar("DISTANCE_WIDE",Double.class)) 
					+ ((citac.dohvatiParametar("SCOOTER_UNIT_PRICE", Double.class) * 
							vrijemeUzegDijelaGrada) * citac.dohvatiParametar("DISTANCE_NARROW",Double.class));
		}
		else 
		{
			throw new NepostojecePrevoznoSredstvoException("Metoda:IzracunajIznos: Nepoznata vrsta prevoznog sredstva!");
		}
		
		return iznos;
	}
	/**
     * Privatna metoda koja kreira račun za iznajmljivanje vozila i upisuje ga u tekstualni fajl.
     *
     * @param iznajmljivanje Objekat koji sadrži podatke o iznajmljivanju.
     * @param osnovnaCijena Osnovna cijena iznajmljivanja.
     * @param popust Iznos popusta.
     * @param promocija Iznos promocije.
     * @param udaljenost Udaljenost pređena vozilom.
     * @param iznos Konačni iznos za plaćanje.
     * @param ukupnoZaPlacanje Ukupno za plaćanje nakon obračuna popusta i promocija.
     */
	private static void kreirajRacun(Iznajmljivanje iznajmljivanje, double osnovnaCijena, 
			double popust, double promocija,
			double udaljenost, 
			double iznos, double ukupnoZaPlacanje)
	
	{
		// Definiše se glavni direktorijum za račune. Ako ne postoji, kreira se.
        File glavniDir= new File("racuni");
        if (!glavniDir.exists()) 
        {
            glavniDir.mkdir();
        }
        // Dohvata datum i vrijeme iz objekta iznajmljivanja i formatira ih za naziv direktorijuma.
        LocalDateTime datum = iznajmljivanje.getDatumIVrijeme();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d_M_yyyy HH_mm");
        String datumIVrijeme = datum.format(formatter);
        
        // Kreira direktorijum za određeni datum i vrijeme ako ne postoji.
        File datumDir = new File(glavniDir, datumIVrijeme);
        
        if(!datumDir.exists())
        {
        	datumDir.mkdir();
        }
        // Definiše ime fajla za račun na osnovu imena korisnika.
        String imeKorisnika = iznajmljivanje.getImeKorisnika();
        File racunFile = new File(datumDir, imeKorisnika + ".txt");
        
        // Priprema StringBuilder za kreiranje sadržaja računa.
        StringBuilder sadrzaj = new StringBuilder();
        double ukupnaCijena = 0.0;
        
        // Ako fajl sa računom već postoji, pročitaj postojeći sadržaj i ažuriraj ukupnu cijenu.
        if (racunFile.exists()) 
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(racunFile))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                	if (line.startsWith("Ukupno sa svih računa:")) 
                    {
                		continue;
                    }
               	  	if (line.startsWith("Ukupno za plaćanje:")) 
                    {
               	  		ukupnaCijena += Double.parseDouble(line.split(": ")[1]);
                    }
                    // Dodaj linije u sadržaj osim ako je to linija sa ukupnom cijenom.
                    sadrzaj.append(line).append("\n");
                }
            } 
            catch (IOException ioe) 
            {
            	// Ispis greške ako nije moguće čitati postojeći račun.
                System.out.println("Nije moguće čitati postojeći račun!" + ioe.getMessage());
            }
        }
        // Ažuriraj ukupnu cijenu dodajući novu ukupno za plaćanje.
    	ukupnaCijena += ukupnoZaPlacanje;

        // Dodavanje novog unosa u sadržaj računa.
        sadrzaj.append("=========================================\n");
        sadrzaj.append("Datum i Vrijeme: ").append(datumIVrijeme).append("\n");
        sadrzaj.append("Redni broj Iznajmljivanja: ").append(Iznajmljivanje.getBrojIznajmljivanja()).append("\n");
        sadrzaj.append("Ime korisnika: ").append(iznajmljivanje.getImeKorisnika()).append("\n");
        sadrzaj.append("ID prevoznog sredstva: ").append(iznajmljivanje.getPrevoznoSredstvo().getId()).append("\n");
        sadrzaj.append("Početna lokacija: ").append(iznajmljivanje.getPocetnaLokacija()).append("\n");
        sadrzaj.append("Odredišna lokacija: ").append(iznajmljivanje.getOdredisnaLokacija()).append("\n");
        sadrzaj.append("Trajanje korištenja: ").append(iznajmljivanje.getTrajanjeKoriscenja()).append("\n");
        
        sadrzaj.append("Kvar: ").append(iznajmljivanje.isKvar() ? "da" : "ne").append("\n");
        sadrzaj.append("Promocija: ").append(iznajmljivanje.isPromocija() ? "da" : "ne").append("\n");
        sadrzaj.append("Popust: ").append(String.format("%.2f", popust)).append("\n");
        sadrzaj.append("Udaljenost: ").append(String.format("%.2f", udaljenost)).append("\n");
        sadrzaj.append("Iznos promocije: ").append(String.format("%.2f", promocija)).append("\n");
        sadrzaj.append("Osnovna cijena: ").append(String.format("%.2f", osnovnaCijena)).append("\n");
        sadrzaj.append("=========================================\n");
        sadrzaj.append("Iznos: ").append(String.format("%.2f", iznos)).append("\n\n");
        sadrzaj.append("Ukupno za plaćanje: ").append(String.format("%.2f", ukupnoZaPlacanje)).append("\n");
        
        sadrzaj.append("Ukupno sa svih računa: ").append(String.format("%.2f", ukupnaCijena)).append("\n");
        
        // Upisivanje cijelog sadržaja u fajl.
        try (FileWriter writer = new FileWriter(racunFile)) 
        {
            writer.write(sadrzaj.toString());
        } 
        catch (IOException ioe) 
        {
        	// Ispis greške ako nije moguće zapisati novi račun.
            System.out.println("Nije moguće zapisati novi račun!" + ioe.getMessage());
        }
	}
	
}
