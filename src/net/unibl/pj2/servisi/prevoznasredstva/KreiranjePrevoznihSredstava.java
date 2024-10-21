package net.unibl.pj2.servisi.prevoznasredstva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import net.unibl.pj2.izuzeci.NepostojecePrevoznoSredstvoException;
import net.unibl.pj2.servisi.iznajmljivanje.CitacKonfiguracioneDatoteke;

/**
 * Klasa {@code KreiranjePrevoznihSredstava} pruža metode za kreiranje objekata tipa {@link PrevoznoSredstvo} 
 * iz konfiguracione datoteke i njihovo dodavanje u mapu.
 * Ova klasa omogućava automatsko učitavanje i instanciranje različitih tipova prevoznih sredstava 
 * (automobil, bicikl, trotinet) na osnovu podataka iz datoteke.
 */
public class KreiranjePrevoznihSredstava 
{
	/**
     * Konstantna veličina parametara koji se očekuju za automobil.
     */
	private static final int velicinaParamAutomobila = 7;
	/**
     * Konstantna veličina parametara koji se očekuju za bicikl i trotinet.
     */
	private static final int velicinaParamBiciklaITrotineta = 6;
	
	/**
     * Kreira i vraća mapu prevoznih sredstava na osnovu podataka iz konfiguracione datoteke.
     * @return {@code LinkedHashMap} koja sadrži parove (ID prevoznog sredstva, objekat prevoznog sredstva).
     */
	public static LinkedHashMap<String, PrevoznoSredstvo> kreirajPrevoznaSredstva()
	{
		// Kreiranje povezane mape koja sadrži parove (ID prevoznog sredstva, objekat prevoznog sredstva).
		// Korištena je povezana Hash mapa iz razloga da bude isti redosljed dodavanja prevoznih sredstava kao u datoteci.
		LinkedHashMap<String, PrevoznoSredstvo> prevoznaSredstva = new LinkedHashMap<>();
		
		// Kreiranje objekta klase CitacKonfiguracioneDatoteke, kako bi mogli jednostavno da parsiramo naziv,
		// datoteke iz koje želimo da čitamo prevozna sredstva (Korisno jer ne moramo mijenjati izvorni kod, već datoteku).
		CitacKonfiguracioneDatoteke citac = new CitacKonfiguracioneDatoteke("PathProperties.txt");
		String putanja = citac.dohvatiParametar("PREVOZNA_SREDSTVA", String.class);
		
		// Kreiranje putanje do datoteke sa prevoznim sredstvima, kako bi omogućili čitanje.
		File fajl = new File(putanja);
		// String koji će nam biti od koristi prilikom čitanja.
		String linija;
		
		try(BufferedReader br = new BufferedReader(new FileReader(fajl)))
		{
			br.readLine(); // Preskačemo liniju sa zaglavljem datoteke.
			while((linija = br.readLine()) != null)
			{
				
				String elementi[] = linija.split(","); // Razdvajamo liniju prema separatoru ',', i dodajemo u niz.
				String tip = elementi[elementi.length - 1]; // Tip prevoznog sredstva je posljednji i njega smještamo u varijablu.

				// U zavisnosti od tipa, uz pomoć naredbe switch nastavljamo obradu.
				switch(tip)
				{
					// U slučaju ako je vrsta prevoznog sredstva: Automobil
					// Uz pomoć metode 'kreiranjeNizaParametara', dobijamo sve parametre Automobila bez praznih stringova (mjesta), što je slučaj u datoteci.
					// Kreira se objekat klase Automobil, i dodaje se u mapu (naravno ukoliko već ne postoji takvo prevozno sredstvo, tj. sa tim ID).
					case "automobil":
						String parametriAutomobila[] = kreiranjeNizaParametara(tip, elementi);
						Automobil automobil = new Automobil(parametriAutomobila[0],parametriAutomobila[1],parametriAutomobila[2],parametriAutomobila[3],Double.parseDouble(parametriAutomobila[4]),parametriAutomobila[5],100);
						if(!prevoznaSredstva.containsKey(parametriAutomobila[0]))
							prevoznaSredstva.put(automobil.getId(), automobil);
						break;
					// Ista situacija kao kod slučaja Automobila.
					case "bicikl":
						String parametriBicikla[] = kreiranjeNizaParametara(tip, elementi);
						Bicikl bicikl = new Bicikl(parametriBicikla[0],parametriBicikla[1],parametriBicikla[2],Double.parseDouble(parametriBicikla[3]),Integer.parseInt(parametriBicikla[4]),100);
						if(!prevoznaSredstva.containsKey(parametriBicikla[0]))
							prevoznaSredstva.put(bicikl.getId(), bicikl);
						break;
						// Ista situacija kao kod slučaja Automobila.
					case "trotinet":
						String parametriTrotineta[] = kreiranjeNizaParametara(tip, elementi);
						Trotinet trotinet = new Trotinet(parametriTrotineta[0],parametriTrotineta[1],parametriTrotineta[2],Double.parseDouble(parametriTrotineta[3]),Integer.parseInt(parametriTrotineta[4]),100);
						if(!prevoznaSredstva.containsKey(parametriTrotineta[0]))
							prevoznaSredstva.put(trotinet.getId(), trotinet);
						break;	
				}
			}
		}
		catch (NumberFormatException nfe) 
		{
            System.out.println("Greška u formatu parametra: " + nfe.getMessage());
        }
		catch (IOException ioe) 
		{
			System.out.println("Greška prilikom čitanja datoteke: " + ioe.getMessage());
		}
		catch(NepostojecePrevoznoSredstvoException npse)
		{
			System.out.println("Greška u parametru: " + npse.getMessage());
		}
		return prevoznaSredstva;
	}
	
	/**
     * Pomoćna metoda koja kreira niz parametara na osnovu tipa prevoznog sredstva i niza elemenata.
     * @param tip - tip prevoznog sredstva (automobil, bicikl, trotinet).
     * @param elementi - niz stringova koji sadrži informacije o prevoznom sredstvu.
     * @return Niz stringova koji sadrži relevantne parametre za zadati tip prevoznog sredstva.
     */
	private static String[] kreiranjeNizaParametara(String tip, String elementi[]) throws NepostojecePrevoznoSredstvoException
	{
		int i = 0; // Inicijalizacija brojača.
		// Ukoliko je tip tipa 'automobil'.
		if(tip.equals("automobil"))
		{
			// Kreira se niz, koji će sadržavati parametre za određeni tip iz datoteke.
			String parametriAutomobila[] = new String[velicinaParamAutomobila];
			// Petljom se prolazi kroz elemente niza koji je prosljeđen kao argument,
			// i vrši se provjera ako  element nije tipa prazan string, dodaje se u niz.
			for(String element : elementi)
			{
				if(!element.equals(""))
					parametriAutomobila[i++] = element;
			}
			return parametriAutomobila;
		}
		// Ista situacija kao za Automobil.
		else if(tip.equals("bicikl"))
		{
			String parametriBicikla[] = new String[velicinaParamBiciklaITrotineta];
			for(String element : elementi)
			{
				if(!element.equals(""))
					parametriBicikla[i++] = element;
			}
			return parametriBicikla;
		}
		// Ista situacija kao za automobil.
		else if(tip.equals("trotinet"))
		{
			String parametriTrotineta[] = new String[velicinaParamBiciklaITrotineta];
			for(String element : elementi)
			{
				if(!element.equals(""))
					parametriTrotineta[i++] = element;
			}
			return parametriTrotineta;
		}
		else 
		{
			// Bacanje izuzetka u slučaju da imamo nepoznat tip prevoznog sredstva.
			throw new NepostojecePrevoznoSredstvoException("Nepoznat tip prevoznog sredstva: " + tip);
		}
	}	
}
