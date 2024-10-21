package net.unibl.pj2.ekrani.prikaz;

import java.io.IOException;
import java.nio.file.Files;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.unibl.pj2.izuzeci.NepostojecePrevoznoSredstvoException;
import net.unibl.pj2.servisi.iznajmljivanje.Iznajmljivanje;
import net.unibl.pj2.servisi.prevoznasredstva.Automobil;
import net.unibl.pj2.servisi.prevoznasredstva.Bicikl;
import net.unibl.pj2.servisi.prevoznasredstva.Trotinet;

/**
 * Klasa RezultatPoslovanja prikazuje rezultate poslovanja kompanije,
 * uključujući sumarne i dnevne podatke o prihodima, popustima, udaljenostima,
 * troškovima održavanja i popravke vozila.
 */
public class RezultatPoslovanja 
{
	/**
     * Unutrašnja klasa koja predstavlja dnevne podatke o poslovanju.
     */
	private static class DnevniPodaci 
	{
	    double dnevniPrihod = 0.0; // Prihod ostvaren tokom dana
	    double dnevniPopust = 0.0; // Ukupni iznos popusta tokom dana
	    double dnevnaUdaljenost = 0.0; // Ukupni iznos udaljenosti tokom dana
	    double dnevniIznosPromocije = 0.0; // Ukupni iznos promocije tokom dana
	    double dnevniIznosPopravki = 0.0;  // Ukupni iznos popravki tokom dana
	}
	
	private JFrame prozor; // Glavni okvir (prozor) aplikacije
	private JTable tabelaSumarni, tabelaDnevni;  // Tabele za prikaz sumarnog i dnevnog prikaza podataka
	private DefaultTableModel sumarniModel,dnevniModel;  // Modeli podataka za tabele
	
	private double ukupniPrihod = 0.0; // Ukupan prihod kompanije
    private double ukupniPopust = 0.0;  // Ukupni iznos popusta
    private double ukupnaUdaljenost = 0.0; // Ukupna pređena udaljenost

    private double ukupniIznosPromocije = 0.0; // Ukupni iznos promocija
    private double ukupniIznosZaOdrzavanje = 0.0; // Ukupni iznos za održavanje
    private double ukupniIznosZaPopravke = 0.0; // Ukupni iznos za popravke
    private double ukupniTroskoviKompanije = 0.0;  // Ukupni troškovi kompanije
    private double ukupanPorez = 0.0; // Ukupan porez
	
    /**
     * Konstruktor klase RezultatPoslovanja, inicijalizuje GUI komponentu i modele tabela.
     */
	public RezultatPoslovanja()
	{
		// Kreiranje glavnog prozora aplikacije
		prozor = new JFrame("Prikaz rezultata poslovanja");
		
		sumarniModel = new NonEditableTableModel(); // Kreiranje modela za sumarnu tabelu
		dnevniModel = new NonEditableTableModel(); // Kreiranje modela za dnevnu tabelu 
		
		inicijalizacijaModelaTabele(); // Inicijalizacija modela tabela
		inicijalizacijaTabela(); // Inicijalizacija tabela

        prozor.setSize(800, 600);
        prozor.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maksimizacija prozora
        prozor.setVisible(true); // Prikazivanje prozora
	}
	/**
     * Inicijalizacija tabela za prikaz sumarnog i dnevnog prikaza podataka.
     */
	private void inicijalizacijaTabela() 
	{
        tabelaSumarni = new JTable(sumarniModel); // Kreiranje tabele za sumarne podatke
        tabelaDnevni = new JTable(dnevniModel); // Kreiranje tabele za dnevne podatke

        JScrollPane spSumarni = new JScrollPane(tabelaSumarni); // Omogućavanje skrolovanja za sumarnu tabelu
        JScrollPane spDnevni = new JScrollPane(tabelaDnevni); // Omogućavanje skrolovanja za dnevnu tabelu

        JTabbedPane tabbedPane = new JTabbedPane(); // Kreiranje tabova za prikaz različitih tabela
        tabbedPane.addTab("Sumarni", spSumarni); // Dodavanje taba za sumarni prikaz
        tabbedPane.addTab("Dnevni", spDnevni); // Dodavanje taba za dnevni prikaz

        prozor.add(tabbedPane); // Dodavanje tabova u prozor
    }

	/**
     * Inicijalizacija modela tabela dodavanjem odgovarajućih kolona.
     */
	private void inicijalizacijaModelaTabele() 
	{
        String[] sumarniKolone = {
            "Ukupan prihod", "Ukupan popust", "Ukupno promocije",
            "Ukupan iznos vožnji", "Ukupan iznos za održavanje",
            "Ukupan iznos za popravke kvarova", "Ukupni troškovi kompanije", "Ukupan porez"
        };
        for (String kolona: sumarniKolone) 
        {
            sumarniModel.addColumn(kolona); // Dodavanje kolona u sumarni model
        }

        String[] dnevniKolone = {
            "Datum", "Ukupan prihod", "Ukupan popust", "Ukupno promocije",
            "Ukupan iznos vožnji", "Ukupan iznos za održavanje", "Ukupan iznos za popravke kvarova"
        };
        for (String kolona : dnevniKolone) 
        {
            dnevniModel.addColumn(kolona); // Dodavanje kolona u dnevni model
        }
    }
	/**
     * Obrada pojedinačnog fajla i računanje dnevnih podataka na osnovu sadržaja fajla.
     *
     * @param putanja          Putanja do fajla
     * @param dnevniPodaci  Dnevni podaci koji se ažuriraju na osnovu sadržaja fajla
     */
	private void procesirajFajl(Path putanja, DnevniPodaci dnevniPodaci) 
	{
        try 
        {
            List<String> linije = Files.readAllLines(putanja); // Čitanje svih linija iz fajla
            for (String linija : linije) 
            {
                procesirajLiniju(linija, dnevniPodaci); // Obrada svake linije u fajlu
            }
        }
        catch (NumberFormatException nfe) 
        {
			System.out.println("Problem sa procesiranjem linije: " + nfe.getMessage());
		}
        catch (IOException ioe) 
        {
            System.out.println("Problem sa datotekom za čitanje: " + ioe.getMessage()); // Ispis greške u slučaju izuzetka
        }
    }
	/**
     * Obrada pojedinačne linije iz fajla i ažuriranje odgovarajućih podataka.
     *
     * @param linija        Linija iz fajla koja se obrađuje
     * @param dnevniPodaci  Dnevni podaci koji se ažuriraju na osnovu sadržaja linije
     */
	private void procesirajLiniju(String linija, DnevniPodaci dnevniPodaci) throws NumberFormatException
	{
		if (linija.startsWith("Ukupno sa svih računa:")) 
		{
            double cijena = Double.parseDouble(linija.split(": ")[1]);
            ukupniPrihod += cijena; // Ažuriranje ukupnog prihoda
            dnevniPodaci.dnevniPrihod += cijena; // Ažuriranje dnevnog prihoda
        } 
        else if (linija.startsWith("Popust:")) 
        {
            double popust = Double.parseDouble(linija.split(": ")[1]);
            ukupniPopust += popust; // Ažuriranje ukupnog popusta
            dnevniPodaci.dnevniPopust += popust; // Ažuriranje dnevnog popusta
        }
        else if (linija.startsWith("Udaljenost:")) 
        {
            double udaljenost = Double.parseDouble(linija.split(": ")[1]);
            ukupnaUdaljenost += udaljenost; // Ažuriranje ukupne udaljenosti
            dnevniPodaci.dnevnaUdaljenost += udaljenost; // Ažuriranje dnevne udaljenosti  
        } 
        else if (linija.startsWith("Iznos promocije:")) 
        {
            double iznosPromocije = Double.parseDouble(linija.split(": ")[1]);
            ukupniIznosPromocije += iznosPromocije; // Ažuriranje ukupnog iznosa promocije
            dnevniPodaci.dnevniIznosPromocije += iznosPromocije; // Ažuriranje dnevnog iznosa promocije
        }
	}
	
	/**
     * Obrada svih tekstualnih fajlova u datom direktorijumu.
     * @param folder Putanja do direktorijuma koji sadrži fajlove.
     * @param dnevniPodaci Podaci o dnevnim finansijama koji će biti ažurirani obradom fajlova.
     */
	private void procesirajFajloveUFolderu(Path folder, DnevniPodaci dnevniPodaci) throws IOException
	{
        try 
        {
        	// Prolazak kroz sve fajlove u direktorijumu i obrada samo tekstualnih fajlova.
            Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(putanja -> putanja.toString().endsWith(".txt"))
                .forEach(putanja -> procesirajFajl(putanja, dnevniPodaci));
        } 
        catch (IOException ioe) 
        {
        	throw new IOException("Problem prilikom procesiranja fajlova u folderu!");
        }
    }
	/**
     * Računanje sumarnog pregleda rezultata poslovanja na osnovu prikupljenih podataka.
     */
	private void izracunajSumarno() throws NepostojecePrevoznoSredstvoException
	{
		// Računanje različitih finansijskih podataka.
        ukupniIznosZaOdrzavanje = ukupniPrihod * 0.2;
        ukupniTroskoviKompanije = ukupniPrihod * 0.2;
        ukupniIznosZaPopravke = izracunajIznosZaPopravke();
        ukupanPorez = (ukupniPrihod - ukupniIznosZaOdrzavanje - ukupniIznosZaPopravke - ukupniTroskoviKompanije) * 0.1;

        // Dodavanje preračunatih vrednosti u sumarni model tabele.
        sumarniModel.addRow(new Object[]
        {
            String.format("%.2f", ukupniPrihod),
            String.format("%.2f", ukupniPopust),
            String.format("%.2f", ukupniIznosPromocije),
            String.format("%.2f", ukupnaUdaljenost),
            String.format("%.2f", ukupniIznosZaOdrzavanje),
            String.format("%.2f", ukupniIznosZaPopravke),
            String.format("%.2f", ukupniTroskoviKompanije),
            String.format("%.2f", ukupanPorez)
        });
    }

	/**
     * Procesira sve tekstualne fajlove u direktorijumu i prikazuje sumarni i dnevni prikaz rezultata poslovanja.
     * @param putanja Putanja do direktorijuma koji sadrži tekstualne fajlove.
     */
	 public void procesirajTxtFajlove(String putanja) 
	 {
        try 
        {
        	Path pocetnaPutanja = Paths.get(putanja);

        	// Pronalaženje svih direktorijuma unutar početnog direktorijuma.
        	List<Path> datumskiFolderi = Files.list(pocetnaPutanja).filter(Files::isDirectory).collect(Collectors.toList());
        	// Definisanje formata datuma i vremena.
        	DateTimeFormatter ulazniFormat = DateTimeFormatter.ofPattern("d_M_yyyy H_mm");
        	DateTimeFormatter izlazniFormat = DateTimeFormatter.ofPattern("d.M.yyyy H:mm");
        	
        	// Iteracija kroz svaki direktorijum (koji predstavlja određeni datum).
        	for (Path folder : datumskiFolderi) 
        	{
                String imeFoldera = folder.getFileName().toString();
                LocalDateTime datumVrijeme = LocalDateTime.parse(imeFoldera, ulazniFormat);
                
                String datumVrijemeFormat = datumVrijeme.format(izlazniFormat);
                
                LocalDateTime ponovniParseDatumVrijeme = LocalDateTime.parse(datumVrijemeFormat,izlazniFormat);
    
                DnevniPodaci dnevniPodaci = new DnevniPodaci();
                
                dnevniPodaci.dnevniIznosPopravki = izracunajIznosZaPopravkeZaDatum(ponovniParseDatumVrijeme);
                
                // Procesiranje svih fajlova unutar trenutnog direktorijuma.
                procesirajFajloveUFolderu(folder, dnevniPodaci);
                
                // Dodavanje dnevnih podataka u model tabele.
	            dnevniModel.addRow(new Object[]
	            {
	            		datumVrijemeFormat, 
	            		String.format("%.2f",dnevniPodaci.dnevniPrihod), 
	            		String.format("%.2f",dnevniPodaci.dnevniPopust), 
	            		String.format("%.2f",dnevniPodaci.dnevniIznosPromocije), 
	            		String.format("%.2f",dnevniPodaci.dnevnaUdaljenost), 
	            		String.format("%.2f",dnevniPodaci.dnevniPrihod * 0.2),
	            		String.format("%.2f",dnevniPodaci.dnevniIznosPopravki)
	            });
        	}
        	izracunajSumarno(); // Računanje sumarnog prikaza rezultata poslovanja.
        	
        }
        catch (NepostojecePrevoznoSredstvoException npse) 
        {
        	System.out.println("Greška u klasi RezultatPoslovanja! " + npse.getMessage());
		}
        catch (IOException ioe) 
        {
            System.out.println("Greška u klasi RezultatPoslovanja!" + ioe.getMessage()); // Ispisivanje greške u slučaju izuzetka.
        }  
    }
	 /**
	  * Računa ukupne troškove popravki na osnovu liste kvarova.
	  * @return Ukupni troškovi popravki.
	  */
	 private double izracunajIznosZaPopravke() throws NepostojecePrevoznoSredstvoException
	 {
		 double cijenaPopravki = 0.0;
		 
		// Iterira kroz listu kvarova i računa troškove popravki za svako prevozno sredstvo.
		 for(Iznajmljivanje iznajmljivanje : PrikazKvarova.listaKvarova)
		 {
			 if(iznajmljivanje.getPrevoznoSredstvo() instanceof Automobil)
			 {
				 cijenaPopravki += iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.07;
			 }
			 else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Bicikl)
			 {
				 cijenaPopravki += iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.04;
			 }
			 else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Trotinet)
			 {
				 cijenaPopravki += iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.02;
			 }
			 else 
			 {
				 throw new NepostojecePrevoznoSredstvoException("Nepostojeći tip prevoznog sredstva!");
			 }
		 }
		 return cijenaPopravki; // Vraća ukupan iznos popravki.
		 
	 }
	 /**
	  * Računa troškove popravki za određeni datum na osnovu liste kvarova.
	  * @param datum Datum za koji se računaju troškovi popravki.
	  * @return Ukupni troškovi popravki za dati datum.
	  */
	 private double izracunajIznosZaPopravkeZaDatum(LocalDateTime datum) throws NepostojecePrevoznoSredstvoException
	 {
		double cijenaPopravki = 0.0;
		
		// Iterira kroz listu kvarova i proverava da li se kvar dogodio na određeni datum.
	    for (Iznajmljivanje iznajmljivanje : PrikazKvarova.listaKvarova) 
	    {
	        LocalDateTime kvarDatum = iznajmljivanje.getDatumIVrijeme();
	  
	        if (kvarDatum.isEqual(datum)) 
	        {
	            if (iznajmljivanje.getPrevoznoSredstvo() instanceof Automobil) 
	            {
	                cijenaPopravki += iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.07;
	            } 
	            else if (iznajmljivanje.getPrevoznoSredstvo() instanceof Bicikl) 
	            {
	                cijenaPopravki += iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.04;
	            } 
	            else if (iznajmljivanje.getPrevoznoSredstvo() instanceof Trotinet) 
	            {
	                cijenaPopravki += iznajmljivanje.getPrevoznoSredstvo().getCijenaNabavke() * 0.02;
	            }
	            else 
	            {
	            	throw new NepostojecePrevoznoSredstvoException("Nepostojeći tip prevoznog sredstva!");
				}
	        }
	    }
	    return cijenaPopravki; // Vraća ukupan iznos popravki za određeni datum.
	}
	 	
}

