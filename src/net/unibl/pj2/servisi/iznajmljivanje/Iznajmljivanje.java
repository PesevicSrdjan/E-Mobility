/**
 * Iznajmljivanje.java
 * 
 * Opis: Implementacija klase koja predstavlja proces iznajmljivanja prevoznog sredstva.
 * Ova klasa upravlja informacijama o iznajmljivanjima, uključujući podatke o prevoznom sredstvu,
 * korisniku, lokacijama i vremenskim intervalima. Takođe, ova klasa omogućava pokretanje niti 
 * za proces iznajmljivanja i učitavanje iznajmljivanja iz datoteke.
 * 
 * @author Srđan Pešević
 * @date 14.07.2024
 */

package net.unibl.pj2.servisi.iznajmljivanje;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import net.unibl.pj2.ekrani.mapa.Mapa;
import net.unibl.pj2.ekrani.mapa.Polje;
import net.unibl.pj2.ekrani.prikaz.PrikazInformacija;
import net.unibl.pj2.ekrani.prikaz.PrikazKvarova;
import net.unibl.pj2.izuzeci.NeispravnaPoljaLokacijeException;
import net.unibl.pj2.izuzeci.NeodgovarajucaDuzinaRedaException;
import net.unibl.pj2.izuzeci.NepostojecePrevoznoSredstvoException;
import net.unibl.pj2.servisi.finansije.FinansijskiObracun;
import net.unibl.pj2.servisi.prevoznasredstva.KreiranjePrevoznihSredstava;
import net.unibl.pj2.servisi.prevoznasredstva.PrevoznoSredstvo;

/**
* Klasa koja predstavlja proces iznajmljivanja prevoznog sredstva.
* <p>
* Ova klasa sadrzi informacije o prevoznom sredstvu koje se iznajmljuje, korisniku koji iznajmljuje,
* trenutnoj i odredisnoj lokaciji, trajanju iznajmljivanja, te eventualnim kvarovima i promocijama.
* Klasa takodje omogucava rad sa vise niti za simulaciju iznajmljivanja i ucitavanje podataka iz datoteke.
* </p>
*/
public class Iznajmljivanje extends Thread
{
	private PrevoznoSredstvo prevoznoSredstvo;
	private static PrikazKvarova prikazKvarova;
	private LocalDateTime datumIVrijeme; 
	private String imeKorisnika, identifikacioniDokument; 
	private Polje pocetnaLokacija, odredisnaLokacija; 
	private int trajanjeKoriscenja, vozackaDozvola;
	private boolean kvar, promocija;
	/**
	 * Broj iznajmljivanja.
	 */
	public static int brojIznajmljivanja;
	private static Mapa mapa = new Mapa();
	
	 /**
     * Vraća trenutni broj iznajmljivanja.
     * @return Broj iznajmljivanja kao {@code int}.
     */
	public static int getBrojIznajmljivanja() {
		return brojIznajmljivanja;
	}

	/**
     * Podrazumijevani konstruktor.
     * <p>
     * Inicijalizuje novu instancu klase {@code Iznajmljivanje} bez podešavanja početnih vrijednosti.
     * </p>
     */
	public Iznajmljivanje() {
		super();
		
	}
	 /**
     * Parametrizovani konstruktor za kreiranje novog iznajmljivanja sa svim potrebnim informacijama.
     * @param prevoznoSredstvo Prevozno sredstvo koje se iznajmljuje.
     * @param datumIVrijeme Datum i vrijeme iznajmljivanja.
     * @param imeKorisnika Ime korisnika koji iznajmljuje.
     * @param trenutnaLokacija Trenutna lokacija prevoznog sredstva.
     * @param odredisnaLokacija Odredišna lokacija prevoznog sredstva.
     * @param trajanjeKoriscenja Trajanje korišćenja prevoznog sredstva.
     * @param kvar Da li je došlo do kvara tokom iznajmljivanja.
     * @param promocija Da li iznajmljivanje sadrži promociju.
     */
	public Iznajmljivanje(PrevoznoSredstvo prevoznoSredstvo,LocalDateTime datumIVrijeme, String imeKorisnika,
			Polje trenutnaLokacija, Polje odredisnaLokacija, int trajanjeKoriscenja, boolean kvar, boolean promocija) {
		super();
		this.prevoznoSredstvo = prevoznoSredstvo;
		this.datumIVrijeme = datumIVrijeme;
		this.imeKorisnika = imeKorisnika;
		this.identifikacioniDokument = generisiIdDokument();
		this.vozackaDozvola = generisiBrojVozackeDozvole();
		this.pocetnaLokacija = trenutnaLokacija;
		this.odredisnaLokacija = odredisnaLokacija;
		this.trajanjeKoriscenja = trajanjeKoriscenja;
		this.kvar = kvar;
		this.promocija = promocija;
	}
	
	// Getteri i setteri za privatne atribute
	
	/**
     * Vraća status kvara tokom iznajmljivanja.
     * @return {@code true} ako je došlo do kvara, {@code false} inače.
     */
	public boolean isKvar() {
		return kvar;
	}

	/**
     * Postavlja status kvara tokom iznajmljivanja.
     * @param kvar {@code true} ako je došlo do kvara, {@code false} inače.
     */
	public void setKvar(boolean kvar) {
		this.kvar = kvar;
	}

	/**
     * Vraća status promocije tokom iznajmljivanja.
     * @return {@code true} ako je iznajmljivanje podložno promociji, {@code false} inače.
     */
	public boolean isPromocija() {
		return promocija;
	}

	/**
     * Postavlja status promocije tokom iznajmljivanja.
     * @param promocija {@code true} ako je iznajmljivanje podložno promociji, {@code false} inače.
     */
	public void setPromocija(boolean promocija) {
		this.promocija = promocija;
	}

	/**
     * Vraća prevozno sredstvo koje se iznajmljuje.
     * @return Prevozno sredstvo kao {@code PrevoznoSredstvo}.
     */
	public PrevoznoSredstvo getPrevoznoSredstvo() {
		return prevoznoSredstvo;
	}

	/**
     * Postavlja prevozno sredstvo koje se iznajmljuje.
     * @param prevoznoSredstvo Prevozno sredstvo kao {@code PrevoznoSredstvo}.
     */
	public void setPrevoznoSredstvo(PrevoznoSredstvo prevoznoSredstvo) {
		this.prevoznoSredstvo = prevoznoSredstvo;
	}
	/**
     * Formatira datum i vrijeme iznajmljivanja u format "d.M.yyyy HH:mm".
     * @return Formatirani datum i vrijeme kao {@code String}.
     */
	public String formatirajDatumIVrijeme() 
	{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
        return datumIVrijeme.format(formatter);
    }
	/**
     * Vraća datum i vrijeme iznajmljivanja.
     * @return Datum i vrijeme iznajmljivanja kao {@code LocalDateTime}.
     */
	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}
	/**
     * Postavlja datum i vrijeme iznajmljivanja.
     * @param datumIVrijeme Datum i vrijeme iznajmljivanja kao {@code LocalDateTime}.
     */
	public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
		this.datumIVrijeme = datumIVrijeme;
	}
	 /**
     * Vraća ime korisnika koji iznajmljuje prevozno sredstvo.
     * @return Ime korisnika kao {@code String}.
     */
	public String getImeKorisnika() {
		return imeKorisnika;
	}
	/**
     * Postavlja ime korisnika koji iznajmljuje prevozno sredstvo.
     * @param imeKorisnika Ime korisnika kao {@code String}.
     */
	public void setImeKorisnika(String imeKorisnika) {
		this.imeKorisnika = imeKorisnika;
	}
	/**
     * Vraća identifikacioni dokument korisnika.
     * @return Identifikacioni dokument kao {@code String}.
     */
	public String getIdentifikacioniDokument() {
		return identifikacioniDokument;
	}
	/**
     * Postavlja identifikacioni dokument korisnika.
     * @param identifikacioniDokument Identifikacioni dokument kao {@code String}.
     */
	public void setIdentifikacioniDokument(String identifikacioniDokument) {
		this.identifikacioniDokument = identifikacioniDokument;
	}
	 /**
     * Vraća broj vozačke dozvole korisnika.
     * @return Broj vozačke dozvole kao {@code int}.
     */
	public int getVozackaDozvola() {
		return vozackaDozvola;
	}
	/**
     * Postavlja broj vozačke dozvole korisnika.
     * @param vozackaDozvola Broj vozačke dozvole kao {@code int}.
     */
	public void setVozackaDozvola(int vozackaDozvola) {
		this.vozackaDozvola = vozackaDozvola;
	}
	/**
     * Vraća početnu lokaciju prevoznog sredstva.
     * @return Početna lokacija kao {@code Polje}.
     */
	public Polje getPocetnaLokacija() {
		return pocetnaLokacija;
	}
	/**
     * Postavlja početnu lokaciju prevoznog sredstva.
     * @param pocetnaLokacija Početna lokacija kao {@code Polje}.
     */
	public void setPocetnaLokacija(Polje pocetnaLokacija) {
		this.pocetnaLokacija = pocetnaLokacija;
	}
	/**
     * Vraća odredišnu lokaciju prevoznog sredstva.
     * @return Odredišna lokacija kao {@code Polje}.
     */
	public Polje getOdredisnaLokacija() {
		return odredisnaLokacija;
	}
	/**
     * Postavlja odredišnu lokaciju prevoznog sredstva.
     * @param odredisnaLokacija Odredišna lokacija kao {@code Polje}.
     */
	public void setOdredisnaLokacija(Polje odredisnaLokacija) {
		this.odredisnaLokacija = odredisnaLokacija;
	}
	/**
     * Vraća trajanje korišćenja prevoznog sredstva.
     * @return Trajanje korišćenja kao {@code int}.
     */
	public int getTrajanjeKoriscenja() {
		return trajanjeKoriscenja;
	}
	/**
     * Postavlja trajanje korišćenja prevoznog sredstva.
     * @param trajanjeKoriscenja Trajanje korišćenja kao {@code int}.
     */
	public void setTrajanjeKoriscenja(int trajanjeKoriscenja) {
		this.trajanjeKoriscenja = trajanjeKoriscenja;
	}
	
	/**
     * Metoda koja pokreće niti za simulaciju iznajmljivanja.
     */
	@Override
	public void run()
	{
		mapa.azurirajMapu(this);
		FinansijskiObracun.izracunajCijenu(this);
		if(this.isKvar())
		{
			prikazKvarova.dodajKvar(this);
		}
	}
	
	/**
	 * Učitava podatke o iznajmljivanjima iz konfiguracione datoteke i obavlja sledeće radnje:
	 * <ul>
	 *   <li>Kreira objekat {@code CitacKonfiguracioneDatoteke} za čitanje konfiguracione datoteke.</li>
	 *   <li>Dohvata putanju do datoteke sa podacima o iznajmljivanjima iz konfiguracione datoteke.</li>
	 *   <li>Postavlja format datuma i vremena za parsiranje iz datoteke.</li>
	 *   <li>Sortira podatke iz datoteke prema datumu iznajmljivanja.</li>
	 *   <li>Prikuplja podatke iz datoteke, parsira ih i kreira objekat {@code Iznajmljivanje} za svaku liniju.</li>
	 *   <li>Kreira mape za prevozna sredstva i iznajmljivanja po datumima.</li>
	 *   <li>Pokreće niti za simulaciju iznajmljivanja i čeka da sve niti završe.</li>
	 * </ul>
	 * 
	 * Ova metoda koristi {@code BufferedReader} za čitanje podataka iz datoteke i parsira podatke u formatu:
	 * <ul>
	 *   <li>Datum i vrijeme</li>
	 *   <li>Ime korisnika</li>
	 *   <li>ID prevoznog sredstva</li>
	 *   <li>Početna lokacija</li>
	 *   <li>Odredišna lokacija</li>
	 *   <li>Trajanje korišćenja</li>
	 *   <li>Informaciju o kvaru</li>
	 *   <li>Informaciju o promociji</li>
	 * </ul>
	 * 
	 * Nakon učitavanja, metoda organizuje podatke po datumima i pokreće niti za simulaciju svakog iznajmljivanja.
	 * 
	 */
	public static void ucitajIznajmljivanja()
	{
		 // Kreira objekat za čitanje konfiguracione datoteke
		CitacKonfiguracioneDatoteke citac = new CitacKonfiguracioneDatoteke("PathProperties.txt");
		
		// Dohvata putanju do datoteke sa podacima o iznajmljivanjima iz konfiguracione datoteke
		String putanja = citac.dohvatiParametar("IZNAJMLJIVANJE", String.class);
		
		 // Postavlja format datuma i vremena koji će se koristiti za parsiranje
		DateTimeFormatter format = DateTimeFormatter.ofPattern("d.M.yyyy H:mm");
	
		//Sortiranje Excel datoteke.
		
		
		String linija;
		
		// Lista koja će sadržati listu stringova za svaku liniju iz datoteke
		List<List<String>> linijePodataka = new ArrayList<>();
		
		 // Pokušava da otvori i pročita datoteku (koristi try - with resource)
		try(BufferedReader br = new BufferedReader(new FileReader(putanja)))
		{
			br.readLine(); // Preskače prvu liniju koja sadrži zaglavlje
			
			 // Čita svaku liniju iz datoteke dok ne dođe do kraja
			while((linija = br.readLine()) != null)
			{
				// Dodaje svaku liniju u listu kao listu stringova, razdvojeno zarezima, koristi se regex obrazac, jer je potrebno da na mjestu polja ne vrši odvajanje po zapetama.
				linijePodataka.add(Arrays.asList(linija.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));
			}
			
			// Sortira listu linija po datumu iznajmljivanja
			linijePodataka.sort(new Comparator<List<String>>() {
				@Override
				public int compare(List<String> linija1, List<String> linija2)
				{
					 // Parsira datume iz stringova
					LocalDateTime datum1 = LocalDateTime.parse(linija1.get(0), format);
                    LocalDateTime datum2 = LocalDateTime.parse(linija2.get(0), format);
                    // Poredi datume i vraća rezultat
                    return datum1.compareTo(datum2);
				}
			});
		
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("Datoteka nije pronađena: " + putanja);
		}
		catch (IOException ioe) 
		{
			// Ispisuje poruku o grešci ako nije moguće učitati datoteku
			System.out.println("Problem sa ucitavanjem datoteke!" + ioe.getMessage());
		}
		
		 // Kreira mapu prevoznih sredstava
		LinkedHashMap<String, PrevoznoSredstvo> prevoznaSredstva = KreiranjePrevoznihSredstava.kreirajPrevoznaSredstva();
		
		@SuppressWarnings("unused")
		PrikazInformacija prikazInformacija = new PrikazInformacija(prevoznaSredstva);
		
		prikazKvarova = new PrikazKvarova();
		
		// Kreira mapu koja će čuvati iznajmljivanja po datumima
		LinkedHashMap<LocalDateTime, ArrayList<Iznajmljivanje>> iznajmljivanjaPoDatumu = new LinkedHashMap<>();
		
		
		// Iterira kroz sve validne elemente
		for(List<String> element : linijePodataka)
		{
			try
			{
				if (element.size() != 8) 
					throw new NeodgovarajucaDuzinaRedaException("Neodgovarajuća dužina reda!");
				
				// Parsira podatke iz linije u odgovarajuće tipove
				LocalDateTime datumIVrijeme = LocalDateTime.parse(element.get(0), format);
				String imeKorisnika = element.get(1);
				String idPrevoznogSredstva = element.get(2);
				
				 // Parsira podatke o trenutnoj i odredišnoj lokaciji
				Polje pocetnaLokacija, odredisnaLokacija;
				
				if(!element.get(3).contains(","))
					throw new NeispravnaPoljaLokacijeException("Neispravna polja početne lokacije");
				else if(!element.get(4).contains(","))
					throw new NeispravnaPoljaLokacijeException("Neispravna polja odredišne lokacije");
				
				String poljaPocetneLokacije[] = element.get(3).split(",");
				String poljaOdredisneLokacije[] = element.get(4).split(",");
				
				
				pocetnaLokacija = new Polje(Integer.parseInt(poljaPocetneLokacije[0].replace("\"", "")),Integer.parseInt(poljaPocetneLokacije[1].replace("\"", "")));	
				odredisnaLokacija = new Polje(Integer.parseInt(poljaOdredisneLokacije[0].replace("\"", "")),Integer.parseInt(poljaOdredisneLokacije[1].replace("\"", "")));
				
				// Parsira trajanje korišćenja, promociju i kvar
				int trajanje = Integer.parseInt(element.get(5));
				boolean kvar, promocija;
				
				if(element.get(6).equals("da"))
				{
					kvar = true;
				}
				else 
				{
					kvar = false;
				}
				
				if(element.get(7).equals("da"))
				{
					promocija = true;
				}
				else 
				{
					promocija = false;
				}
				// Dohvata prevozno sredstvo iz mape
				PrevoznoSredstvo prevoznoSredstvo = prevoznaSredstva.get(idPrevoznogSredstva);
				if(prevoznoSredstvo == null)
					throw new NepostojecePrevoznoSredstvoException("Ne postoji prevozno sredstvo!");
				
				// Ako prevozno sredstvo postoji, kreira objekat Iznajmljivanje
				Iznajmljivanje iznajmljivanje = new Iznajmljivanje(prevoznoSredstvo,datumIVrijeme,imeKorisnika,pocetnaLokacija,odredisnaLokacija,trajanje,kvar,promocija);
				
				
				LocalDateTime datum = iznajmljivanje.getDatumIVrijeme();
	
				// Provjera da li postoji iznajmljivanje sa istim ID, za određeni datum.

				if(iznajmljivanjaPoDatumu.containsKey(datum))
				{
					ArrayList<Iznajmljivanje> lista = iznajmljivanjaPoDatumu.get(datum);
					
					
					boolean postojiSaIstimId = false;
					
					for (Iznajmljivanje iz : lista) 
					{
				        if (iz.prevoznoSredstvo.getId() == iznajmljivanje.prevoznoSredstvo.getId()) 
				        {
				            postojiSaIstimId = true;
				            break;
				        }
				    }
					
					if(!postojiSaIstimId)
					{
						iznajmljivanjaPoDatumu.get(datum).add(iznajmljivanje);
					}
					else 
					{
						System.out.println("Postoji iznajmljivanje za datum: " + datum + " sa istim ID:  " + iznajmljivanje.prevoznoSredstvo.getId());
					}
				}
				else 
				{
					ArrayList<Iznajmljivanje> lista = new ArrayList<>();
					lista.add(iznajmljivanje);
					iznajmljivanjaPoDatumu.put(datum, lista);
				}
			}
			catch (NumberFormatException nfe) {
				System.out.println("Greška prilikom parsiranja podatka: " + nfe.getMessage() + " " + element);
				continue;
			}
			catch (NepostojecePrevoznoSredstvoException npse) 
			{
				System.out.println("Neispravno učitavanje! " + npse.getMessage() + " " + element);
				continue;
			}
			catch (NeodgovarajucaDuzinaRedaException ndre) 
			{
				System.out.println("Neispravno učitavanje! " + ndre.getMessage() + " " + element);
				continue;
			}
			catch (NeispravnaPoljaLokacijeException nple) 
			{
				System.out.println("Neispravno učitavanje! " + nple.getMessage() + " " + element);
				continue;
			}
		}
		
			
		for (LocalDateTime datum : iznajmljivanjaPoDatumu.keySet()) 
		{
			try
			{
	            List<Iznajmljivanje> iznajmljivanja = iznajmljivanjaPoDatumu.get(datum);
	            // Pokretanje svih niti sa istim datumom i vremenom
	            for (Iznajmljivanje iznajmljivanje : iznajmljivanja) 
	            {
	                iznajmljivanje.start();
	            }
	
	            // Sačekaj da sve niti završe
	            for (Iznajmljivanje iznajmljivanje : iznajmljivanja) 
	            {
	                iznajmljivanje.join();
	            }
	            	
	            // Ispis poruke i pauza
	            System.out.println("Završena je jedna simulacija!");
	            Thread.sleep(5000);
	            mapa.resetGUI();
	        }
			catch (InterruptedException ie) 
			{
		        System.out.println("Prekid tokom čekanja ili spavanja: " + ie.getMessage());
		        Thread.currentThread().interrupt(); // Vraćanje prekida na trenutni thread
		    } 
			catch (IllegalThreadStateException itse) 
			{
		        System.out.println("Nevažeće stanje niti: " + itse.getMessage());
		    } 
			catch (Exception e) 
			{
		        System.out.println("Problem sa nitima: " + e.getMessage());
		    }
		}

	}
	/**
     * Generiše identifikacioni dokument.
     * @return Identifikacioni dokument kao string
     */
	private String generisiIdDokument()
	{
		String dokument[] = {"pasos", "licna karta"};
		Random random = new Random();
		return dokument[random.nextInt(dokument.length)];
	}
	
	/**
     * Generiše broj vozačke dozvole.
     * @return Broj vozačke dozvole kao int
     */
    private int generisiBrojVozackeDozvole() 
    {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // Generiše nasumičan broj između 100000 i 999999
    }
	
    /**
     * Provjera da li je korisnik stranac ili ne.
     * @return Boolean vrijednost (true ako je stranac, false ako nije).
     */
    public boolean jeStraniKorisnik()
    {
    	if(this.identifikacioniDokument.equals("pasos"))
    		return true;
    	return false;
    }
	
    /**
     * Vraća string prikaz objekta sa svim relevantnim informacijama.
     *
     * @return String koji sadrži datum i vrijeme, ime korisnika, ID prevoznog sredstva, 
     *         početnu i odredišnu lokaciju, trajanje korišćenja, stanje kvara i da li je bilo promocije.
     */
	@Override
	public String toString() {
		return "Datum i Vrijeme: " + datumIVrijeme + 
				" Korisnik:\n" + imeKorisnika + 
				" ID Prevoznog sredstva: " + prevoznoSredstvo.getId() + 
				" Početna Lokacija: " + pocetnaLokacija + 
				" Odredišna lokacija:\n" + odredisnaLokacija +
				" Trajanje: " + trajanjeKoriscenja + 
				" Kvar:\n" + kvar + 
				" Promocija: " + promocija;
	}
	
}
