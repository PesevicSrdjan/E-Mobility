package net.unibl.pj2.ekrani.prikaz;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
 * Klasa PrikazKvarova je odgovorna za prikaz informacija o kvarovima na prevoznim sredstvima
 * u okviru GUI aplikacije koristeći tabelu sa karticama.
 */
public class PrikazKvarova 
{
	private JFrame prozor;  // JFrame objekat koji predstavlja glavni prozor aplikacije
	private JTable tabelaKvarovi; // JTable za prikaz informacija o kvarovima u tabeli
	private DefaultTableModel kvaroviModel;  // DefaultTableModel za upravljanje podacima unutar JTable
	public static List<Iznajmljivanje> listaKvarova = new ArrayList<>(); // Lista svih kvarova koji su dodati u aplikaciji
	// Fiksni niz stringova koji sadrži moguce opise kvarova
	public static final  String OPISI_KVAROVA[] = {"Pukla guma", "Problem sa motorom", "Oštećenje karoserije", "Kvar na elektrici"};
	
	/**
     * Konstruktor koji inicijalizuje GUI komponente i postavlja prozor sa tabelom za prikaz kvarova.
     */
	public PrikazKvarova()
	{

		// Inicijalizacija glavnog prozora sa naslovom
		prozor = new JFrame("Prikaz Informacija o kvarovima");
		// Inicijalizacija modela tabele koji ne dozvoljava uređivanje celija
		kvaroviModel = new NonEditableTableModel();
		// Dodavanje kolona u model tabele
		kvaroviModel.addColumn("Vrsta prevoznog sredstva");
        kvaroviModel.addColumn("ID");
        kvaroviModel.addColumn("Vrijeme");
        kvaroviModel.addColumn("Opis kvara");

        // Kreiranje JTable sa datim modelom
		tabelaKvarovi = new JTable(kvaroviModel);
		// Kreiranje JScrollPane za omogucavanje skrolovanja tabele
		JScrollPane spKvarovi = new JScrollPane(tabelaKvarovi);
		// Kreiranje JTabbedPane za dodavanje tabele u karticu
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Kvarovi", spKvarovi);
		 // Dodavanje kartica u glavni prozor
		prozor.add(tabbedPane);
		// Postavljanje prozora na maksimalnu velicinu
		prozor.setExtendedState(JFrame.MAXIMIZED_BOTH);
        prozor.setSize(800, 600);
        // Postavljanje prozora na vidljiv
        prozor.setVisible(true);
	}
	/**
     * Metoda za dodavanje novog kvara u tabelu i listu kvarova.
     * 
     * @param iznajmljivanje Objekat iznajmljivanja sa informacijama o prevoznom sredstvu i vremenu kvara
     */
	public synchronized void dodajKvar(Iznajmljivanje iznajmljivanje)
	{	
		try
		{
			if(iznajmljivanje == null)
			{
				throw new IllegalArgumentException("Objekat iznajmljivanja je null!");
			}
			
			// String koji predstavlja vrstu prevoznog sredstva
			String vrstaPrevoznogSredstva = null;
			
			// Provera tipa prevoznog sredstva i postavljanje odgovarajuce vrijednosti
			if(iznajmljivanje.getPrevoznoSredstvo() instanceof Automobil)
			{
				vrstaPrevoznogSredstva = "Automobil";
			}
			else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Bicikl)
			{
				vrstaPrevoznogSredstva = "Bicikl";
			}
			else if(iznajmljivanje.getPrevoznoSredstvo() instanceof Trotinet)
			{
				vrstaPrevoznogSredstva = "Trotinet";
			}
			else 
			{
				throw new NepostojecePrevoznoSredstvoException("Nepostojeći tip prevoznog sredstva!");
			}
			// Dohvatanje ID-ja prevoznog sredstva
			String id = iznajmljivanje.getPrevoznoSredstvo().getId();
			// Formatiranje datuma i vremena iznajmljivanja
			String vrijeme = iznajmljivanje.getDatumIVrijeme().format(DateTimeFormatter.ofPattern("d.M.yyyy HH:mm"));
			
			// Nasumicno odabiranje opisa kvara iz niza OPISI_KVAROVA
			int indeks = (int)(Math.random() * OPISI_KVAROVA.length);
			// Dohvatanje opisa kvara pozivom metode iz klase prevoznog sredstva
			String opisKvara = iznajmljivanje.getPrevoznoSredstvo().kvar(OPISI_KVAROVA[indeks], vrijeme);
	
			// Dodavanje iznajmljivanja u listu kvarova
			listaKvarova.add(iznajmljivanje);
			// Dodavanje novog reda u tabelu sa informacijama o kvaru
			kvaroviModel.addRow(new Object[]{vrstaPrevoznogSredstva, id, vrijeme, opisKvara});
		}
		catch (NepostojecePrevoznoSredstvoException npse) 
		{
			System.out.println("Greška u klasi PrikazKvarova: " + npse.getMessage());
		}
		catch (IllegalArgumentException iae) 
		{
			System.out.println("Greška u klasi PrikazKvarova: " + iae.getMessage());
		}
	}
	
}