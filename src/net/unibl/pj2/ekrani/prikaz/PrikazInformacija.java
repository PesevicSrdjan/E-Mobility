package net.unibl.pj2.ekrani.prikaz;

import java.util.LinkedHashMap;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.unibl.pj2.izuzeci.NepostojecePrevoznoSredstvoException;
import net.unibl.pj2.servisi.prevoznasredstva.Automobil;
import net.unibl.pj2.servisi.prevoznasredstva.Bicikl;
import net.unibl.pj2.servisi.prevoznasredstva.PrevoznoSredstvo;
import net.unibl.pj2.servisi.prevoznasredstva.Trotinet;

/**
 * Klasa PrikazInformacija prikazuje informacije o prevoznim sredstvima u GUI prozoru.
 * Podaci su organizovani u tri tabele: Automobili, Bicikli i Trotineti.
 */
public class PrikazInformacija
{
	private JFrame prozor;  // Glavni prozor aplikacije
    private JTable tabelaAutomobili; // Tabela za prikaz automobila
    private JTable tabelaBicikli; // Tabela za prikaz bicikala
    private JTable tabelaTrotineti;  // Tabela za prikaz trotineta
	
    /**
     * Konstruktor klase koji inicijalizuje GUI komponente i prikazuje informacije o prevoznim sredstvima.
     * @param prevoznaSredstva Mapa sa informacijama o prevoznim sredstvima.
     */
    public PrikazInformacija(LinkedHashMap<String, PrevoznoSredstvo> prevoznaSredstva)
	{
		try 
		{
			if(prevoznaSredstva == null)
			{
				throw new IllegalArgumentException("Mapa sa prevoznim sredstvima je null!");
			}
			// Kreiranje glavnog prozora aplikacije
			prozor = new JFrame("Prikaz Informacija o Prevoznim Sredstvima");

	        // Kreiranje modela tabele za automobile.
	        DefaultTableModel modelAutomobili = new NonEditableTableModel();
	        modelAutomobili.addColumn("ID Sredstva");
	        modelAutomobili.addColumn("Proizvodjac");
	        modelAutomobili.addColumn("Model");
	        modelAutomobili.addColumn("Datum Nabavke");
	        modelAutomobili.addColumn("Cijena");
	        modelAutomobili.addColumn("Opis");
	        modelAutomobili.addColumn("Vrsta");

	        // Kreiranje modela tabele za bicikle
	        DefaultTableModel modelBicikli = new NonEditableTableModel();
	        modelBicikli.addColumn("ID Sredstva");
	        modelBicikli.addColumn("Proizvodjac");
	        modelBicikli.addColumn("Model");
	        modelBicikli.addColumn("Cijena");
	        modelBicikli.addColumn("Domet");
	        modelBicikli.addColumn("Vrsta");
	        
	        // Kreiranje modela tabele za trotineti
	        DefaultTableModel modelTrotineti = new NonEditableTableModel();
	        modelTrotineti.addColumn("ID Sredstva");
	        modelTrotineti.addColumn("Proizvodjac");
	        modelTrotineti.addColumn("Model");
	        modelTrotineti.addColumn("Cijena");
	        modelTrotineti.addColumn("Max brzina");
	        modelTrotineti.addColumn("Vrsta");


	        // Popunjavanje modela tabele sa podacima
	        for (PrevoznoSredstvo prevoznoSredstvo : prevoznaSredstva.values()) 
	        {
	            if (prevoznoSredstvo instanceof Automobil) 
	            {
	                Automobil automobil = (Automobil) prevoznoSredstvo;
	                Object[] row = new Object[]
	                {
	                    automobil.getId(),
	                    automobil.getProizvodjac(),
	                    automobil.getModel(),
	                    automobil.getDatumNabavke(),
	                    automobil.getCijenaNabavke(),
	                    automobil.getOpis(),
	                    "Automobil"
	                };
	                modelAutomobili.addRow(row); // Dodavanje reda u model tabele za automobile
	            } 
	            else if (prevoznoSredstvo instanceof Bicikl) 
	            {
	                Bicikl bicikl = (Bicikl) prevoznoSredstvo;
	                Object[] row = new Object[]
	                {
	                    bicikl.getId(),
	                    bicikl.getProizvodjac(),
	                    bicikl.getModel(),
	                    bicikl.getCijenaNabavke(),
	                    bicikl.getAutonomija(),
	                    "Bicikl"
	                };
	                modelBicikli.addRow(row); // Dodavanje reda u model tabele za bicikle
	            } 
	            else if (prevoznoSredstvo instanceof Trotinet) 
	            {
	                Trotinet trotinet = (Trotinet) prevoznoSredstvo;
	                Object[] row = new Object[]
	                {
	                    trotinet.getId(),
	                    trotinet.getProizvodjac(),
	                    trotinet.getModel(),
	                    trotinet.getCijenaNabavke(),
	                    trotinet.getMaksimalnaBrzina(),
	                    "Trotinet"
	                };
	                modelTrotineti.addRow(row); // Dodavanje reda u model tabele za trotineti
	            }
	            else 
	            {
	            	throw new NepostojecePrevoznoSredstvoException("Nepostojeći tip prevoznog sredstva!");
				}
	        }

	        // Kreiranje tabela sa pripadajucim modelima
	        tabelaAutomobili = new JTable(modelAutomobili); // Kreiranje tabele za automobile
	        tabelaBicikli = new JTable(modelBicikli); // Kreiranje tabele za bicikle
	        tabelaTrotineti = new JTable(modelTrotineti); // Kreiranje tabele za trotineti

	        // Kreiranje scroll pane-ova za svaku tabelu
	        JScrollPane spAutomobili = new JScrollPane(tabelaAutomobili);
	        JScrollPane spBicikli = new JScrollPane(tabelaBicikli);
	        JScrollPane spTrotineti = new JScrollPane(tabelaTrotineti);

	        // Kreiranje tabova i dodavanje scroll pane-ova u njih
	        JTabbedPane tabbedPane = new JTabbedPane();
	        tabbedPane.addTab("Automobili", spAutomobili);
	        tabbedPane.addTab("Bicikli", spBicikli);
	        tabbedPane.addTab("Trotineti", spTrotineti);

	        // Dodavanje tabova u glavni prozor
	        prozor.add(tabbedPane);
	        prozor.setExtendedState(JFrame.MAXIMIZED_BOTH);
	        prozor.setSize(800, 600);
	        prozor.setVisible(true);
		}
		catch (NepostojecePrevoznoSredstvoException npse) 
		{
			System.out.println("Greška u klasi PrikazInformacija: " + npse.getMessage());
		}
		catch (IllegalArgumentException iae) 
		{
			System.out.println("Greška u klasi PrikazInformacija: " + iae.getMessage());
		}
	}
}
/**
 * Klasa koja omogućava da su informacije u tabelama samo za čitanje.
 */
class NonEditableTableModel extends DefaultTableModel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // Dodato kako bi se uklonilo upozorenje.

	/**
     * Provjerava da li je ćelija u tabeli editable.
     * @param red Red u tabeli.
     * @param kolona Kolona u tabeli.
     * @return false, jer su ćelije u tabeli samo za čitanje.
     */
    @Override
    public boolean isCellEditable(int red, int kolona) 
    {
        return false;
    }
}
