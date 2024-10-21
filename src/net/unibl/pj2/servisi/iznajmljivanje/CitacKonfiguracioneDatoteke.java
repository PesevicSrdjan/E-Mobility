package net.unibl.pj2.servisi.iznajmljivanje;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa CitacKonfiguracioneDatoteke omogućava čitanje konfiguracionih datoteka
 * i dohvatanje parametara iz tih datoteka.
 * Konkretno za parsiranje .csv datoteka i prilikom kreiranja računa za iznajmljivanja.
 */
public class CitacKonfiguracioneDatoteke 
{
	//Objekat Properties klase koji omogućava dohvatanje parametara iz konfiguracione datoteke.
	private Properties properties;
	// Putanja do konfiguracione datoteke
	private String putanja = System.getProperty("user.dir") + File.separator;
	
	/**
    * Konstruktor koji učitava konfiguracionu datoteku.
    *
    * @param naziv Naziv konfiguracione datoteke
    */
	public CitacKonfiguracioneDatoteke(String naziv)
	{
		properties = new Properties(); // Kreiranje objekta Properties klase.
		
		putanja = putanja + naziv; // Postavljanje putanje do konfiguracione datoteke
		
		// Pokušaj učitavanja datoteke sa konfiguracionim parametrima.
		try(InputStream input = new FileInputStream(putanja))
		{ 
			properties.load(input); // Učitavanje parametara iz datoteke u  properties objekat.
		}
		catch (FileNotFoundException fnfe) 
		{
			System.out.println("Konfiguraciona datoteka nije pronađena: " + putanja); // Ispis greške u slučaju izuzetka.
		}
		catch (IOException ioe) {
			System.out.println("Greška prilikom čitanja konfiguracione datoteke: " + ioe.getMessage());
		}
	}
	/**
     * Metoda koja dohvaća parametar iz konfiguracione datoteke.
     *
     * @param <T> Tip parametra koji se očekuje, koristi se generički tip, jer se dohvataju i String i Double tipovi.
     * @param kljuc Ključ parametra
     * @param tip Klasa tipa parametra
     * @return Parametar tipa T ili null ako dođe do greške
     */
	public <T> T dohvatiParametar(String kljuc, Class<T> tip) 
	{
		// Dohvatanje vrijednosti parametra kao string.
		String vrijednost = properties.getProperty(kljuc);
		try 
		{
			// Ako je tip parametra String, kastovanje vrijednosti.
			// (Iako je možda kastovanje i suvišno jer 'vrijednost' je već tipa String, 
			// zbog dodatne bezbjednosti je urađeno kastovanje). :)
			if(tip == String.class) 
			{
				return tip.cast(vrijednost);
			}
			// Ako je tip parametra Double, parsiranje i kastovanje vrijednosti.
			else if(tip == Double.class) 
			{
				return tip.cast(Double.parseDouble(vrijednost));
			}
		}
		catch (NumberFormatException nfe) 
		{
			// Ispis poruke u slučaju greške
			 System.out.println("Greška u formatu vrijednosti: " + nfe.getMessage());
		}
		catch (ClassCastException cce) 
		{
		    System.out.println("Greška prilikom kastovanja vrijednosti za ključ: " + cce.getMessage());
		}
		// Vraćanje null ako dođe do greške
		return null;
    }
}