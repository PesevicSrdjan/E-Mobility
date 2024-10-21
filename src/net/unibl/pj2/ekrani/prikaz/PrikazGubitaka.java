package net.unibl.pj2.ekrani.prikaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.unibl.pj2.servisi.finansije.ObracunGubitaka;
import net.unibl.pj2.servisi.serijalizacija.Serijalizacija;

/**
 * Klasa PrikazGubitaka predstavlja GUI za prikaz gubitaka prevoznih sredstava.
 * Kreira se prozor sa tabelom koja prikazuje podatke o prevoznim sredstvima i njihovim gubicima.
 * Koristeći dugme "Učitaj Podatke", korisnik moze učitati podatke iz serijalizovanih datoteka
 * i prikazati ih u tabeli. Dugme pokreće proces deserijalizacije podataka i ažurira tabelu
 * sa informacijama o prevoznim sredstvima i njihovim gubicima.
 */
public class PrikazGubitaka 
{
	private JFrame prozor; // Glavni prozor aplikacije.
	private JTable tabela; // Tabela koja prikazuje podatke o prevoznim sredstvima.
	private JButton dugme; // Dugme za ucitavanje podataka.
	private DefaultTableModel gubiciModel; // Model tabele koji sadrzi podatke o prevoznim sredstvima.
	
	/**
     * Konstruktor koji inicijalizuje GUI komponente.
     */
	public PrikazGubitaka() 
	{
		inicijalizujProzor(); // Inicijalizuje glavni prozor.
		inicijalizujTabelu(); // Inicijalizuje tabelu.
		inicijalizujDugme(); // Inicijalizuje dugme za ucitavanje podataka.
        prozor.setVisible(true); // Postavlja prozor da bude vidljiv.
	}
	
	/**
     * Inicijalizuje glavni prozor aplikacije.
     */
	private void inicijalizujProzor()
	{
		prozor = new JFrame("Prikaz Prevoznih Sredstava"); // Kreira novi JFrame sa naslovom.
		prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Zatvara aplikaciju kada se zatvori prozor.
        prozor.setLayout(new BorderLayout()); // Postavlja BorderLayout za raspored komponenata.
        prozor.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maksimizuje prozor na cijelom ekranu.
	}
	
	/**
     * Inicijalizuje tabelu sa modelom podataka.
     */
	private void inicijalizujTabelu()
	{
		gubiciModel = new DefaultTableModel(); // Kreira model tabele bez podataka.
        
		// Dodaje kolone u model tabele.
        gubiciModel.addColumn("ID");
        gubiciModel.addColumn("Proizvodjač");
        gubiciModel.addColumn("Model");
        gubiciModel.addColumn("Cijena nabavke");
        gubiciModel.addColumn("Prihod");
        gubiciModel.addColumn("Iznos za održavanje");
        gubiciModel.addColumn("Iznos za popravke");
        gubiciModel.addColumn("Gubitak / Najmanja zarada");
        
        tabela = new JTable(gubiciModel); // Kreira JTable sa prethodno definisanim modelom.
        prozor.add(new JScrollPane(tabela), BorderLayout.CENTER);
	}
	
	/**
     * Inicijalizuje dugme za učitavanje podataka.
     */
	private void inicijalizujDugme()
	{
		dugme = new JButton("Ucitaj Podatke"); // Kreira dugme sa natpisom.
				
        dugme.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	ucitajPodatke(); // Poziva metodu za ucitavanje podataka kada se dugme pritisne.
            }
        });
        
        prozor.add(dugme, BorderLayout.PAGE_END); // Dodaje dugme na dno prozora.
	}
	
	/**
     * Učitava podatke iz serijalizovanih datoteka i dodaje ih u tabelu.
     */
	private void ucitajPodatke()
	{
        	
		// Definise putanju do foldera sa serijalizovanim datotekama.
    	String putanjaZaSerijalizaciju = System.getProperty("user.dir") + File.separator + "serijalizacija";
       
    	File folder = new File(putanjaZaSerijalizaciju);
        
        // Filtrira datoteke da bi se dobile samo datoteke sa ekstenzijom .ser.
        File datoteke[] = folder.listFiles((dir,name) -> name.toLowerCase().endsWith(".ser"));
        
        if(datoteke != null)
        {
        	// Iterira kroz sve pronadjene serijalizovane datoteke.
        	for(File datoteka : datoteke)
        	{
        		// Deserijalizuje objekat iz datoteke.
        		ObracunGubitaka obracun = Serijalizacija.deserijalizujObjekat(datoteka.getAbsolutePath());
        		
        		if(obracun != null)
        		{
        			 // Dodaje red u model tabele sa podacima iz deserijalizovanog objekta.
        			gubiciModel.addRow(new Object[] 
        			{
    				    obracun.getPrevoznoSredstvo().getId(),
    				    obracun.getPrevoznoSredstvo().getProizvodjac(),
    				    obracun.getPrevoznoSredstvo().getModel(),
    				    String.format("%.2f", obracun.getPrevoznoSredstvo().getCijenaNabavke()),
    				    String.format("%.2f", obracun.getPrihod()),
    				    String.format("%.2f", obracun.getTroskoviOdrzavanja()),
    				    String.format("%.2f", obracun.getTroskoviPopravke()),
    				    String.format("%.2f", obracun.getGubitak())
        			});
        		}
        	}
        }
        else 
        {
			System.out.println("Greška: Ne mogu da pročitam sadržaj foldera: " + putanjaZaSerijalizaciju);
		}
	}
}
