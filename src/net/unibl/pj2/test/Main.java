package net.unibl.pj2.test;

import java.io.File;

import net.unibl.pj2.ekrani.prikaz.PrikazGubitaka;
import net.unibl.pj2.ekrani.prikaz.RezultatPoslovanja;
import net.unibl.pj2.servisi.finansije.ObracunGubitaka;
import net.unibl.pj2.servisi.iznajmljivanje.Iznajmljivanje;

/**
 * Glavna klasa koja pokreće aplikaciju.
 * Ova klasa inicijalizuje procese vezane za iznajmljivanje,
 * obradu računa, serijalizaciju podataka i prikaz gubitaka.
 */
public class Main 
{
	/**
     * Glavna metoda koja pokreće aplikaciju.
     * 
     * @param args argumenti komandne linije (ne koriste se)
     */
	public static void main(String[] args) 
	{
		// Dohvata putanju do korisničkog direktorijuma
		String userDir = System.getProperty("user.dir");
		// Kreira putanju do direktorijuma gde se nalaze računi
		String racuniPutanja = userDir + File.separator + "racuni";
		// Kreira putanju do direktorijuma gde će se serijalizovati podaci
		String serijalizacijaPutanja = userDir + File.separator + "serijalizacija";
		 // Kreira objekat File za direktorijum sa računima
		File racuniDir = new File(racuniPutanja);
		// Kreira objekat File za direktorijum sa serijalizovanim podacima
		File serDir = new File(serijalizacijaPutanja);
		
		// Provjerava da li direktorijum sa računima postoji, i ako postoji briše ga
		if(racuniDir.exists())
		{
			obrisiDirektorijum(racuniDir);
		}
		// Provjerava da li direktorijum za serijalizaciju postoji, i ako postoji briše ga
		if(serDir.exists())
		{
			obrisiDirektorijum(serDir);
		}

		// Učitava informacije o iznajmljivanjima iz datoteke
		Iznajmljivanje.ucitajIznajmljivanja();
		// Kreira instancu klase RezultatPoslovanja za obradu txt fajlova sa računima
		RezultatPoslovanja rezultatPoslovanja = new RezultatPoslovanja();
		// Procesira txt fajlove u direktorijumu racuniPutanja
		rezultatPoslovanja.procesirajTxtFajlove(racuniPutanja);

		 // Ako direktorijum za serijalizaciju ne postoji, kreira ga
		if(!serDir.exists())
			serDir.mkdir();
		// Serijalizuje podatke o najvećim gubicima u odgovarajući direktorijum
		ObracunGubitaka.serijalizujNajveceGubitke(serijalizacijaPutanja);
		// Kreira instancu klase PrikazGubitaka za prikaz gubitaka
		@SuppressWarnings("unused")
		PrikazGubitaka prikazGubitaka = new PrikazGubitaka();
		
	}
	/**
     * Rekurzivno briše dati direktorijum i sve njegove poddirektorijume i fajlove.
     * 
     * @param dir direktorijum koji treba obrisati
     */
	public static void obrisiDirektorijum(File dir) 
	{
		// Provjerava da li je dati fajl direktorijum
        if (dir.isDirectory()) 
        {
        	// Lista sve fajlove i poddirektorijume unutar datog direktorijuma
            File[] poddirektorijumi = dir.listFiles();
            if (poddirektorijumi != null) 
            {
            	// Rekurzivno briše svaki poddirektorijum
                for (File poddirektorijum : poddirektorijumi)
                {
                	 obrisiDirektorijum(poddirektorijum);
                }
                   
            }
        }
        // Briše trenutni fajl ili direktorijum
        dir.delete();
    }
}