/**
 * Mapa.java



 * 
 * Opis: Implementacija klase Mapa za prikaz kretanja prevoznih sredstava iznajmljivanja na ekranu.
 * Autor: Srdjan Pesevic
 * Datum: 12.07.2024
 */

package net.unibl.pj2.ekrani.mapa;
import javax.swing.*;

import net.unibl.pj2.izuzeci.NeispravanPronalazakPutanjeException;
import net.unibl.pj2.servisi.iznajmljivanje.Iznajmljivanje;
import net.unibl.pj2.servisi.putanja.NajkracaPutanjaMatrice;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Klasa koja predstavlja mapu grada za kretanje prevoznih sredstava iznajmljivanja.
 */
public class Mapa 
{
    private static final int VELICINA = 20;
	private static final JButton[][] matricaDugmadi = new JButton[VELICINA][VELICINA];
	private static final ConcurrentHashMap<Polje, PoljeInfo> poljaMape = new ConcurrentHashMap<>();	
	private JFrame prozor;
	private Font font;
	/**
     * Konstruktor klase Mapa.
     */
	public Mapa()
	{
		// Implementacija konstruktora.
		
		prozor = new JFrame("Mapa matrica");  // Kreiranje nove JFrame komponente sa nazivom "Mapa matrica"
		//prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Postavljanje ponasanja na zatvaranje aplikacije kada se ekran zatvori
        prozor.setSize(1000, 1000); // Postavljanje dimenzija prozora na 1000x1000 piksela
        JPanel panel = new JPanel(new GridLayout(VELICINA, VELICINA)); // Kreiranje novog JPanela sa GridLayout-om dimenzija SIZE x SIZE
        font = new Font("Arial", Font.PLAIN, 9); // Kreiranje novog fonta sa Arial stilom, obicnim debljinom i velicinom 10

        inicijalizacijaDugmadi(panel, font); // Pozivanje metode inicijalizacijaDugmadi() za inicijalizaciju dugmadi i postavljanje na panel
        
        prozor.setExtendedState(JFrame.MAXIMIZED_BOTH);
        prozor.add(panel); // Dodavanje panela na JFrame
        prozor.setVisible(true);  // Postavljanje JFrame-a da bude vidljiv na ekranu
	}
	

	private void inicijalizacijaDugmadi(JPanel panel, Font font)
	{
		
		int indeksHorizontalno = 0; // Inicijalizacija brojaca za horizontalne indekse dugmadi
        int indeksVertikalno = 0; // Inicijalizacija brojaca za vertikalne indekse dugmadi
		
        // Dvostruka petlja za prolazak kroz matricu dugmadi
        for (int i = 0; i < VELICINA; i++) 
        {
            for (int j = 0; j < VELICINA; j++) 
            {
                JButton dugme = new JButton(); // Kreiranje novog dugmeta
                dugme.setMargin(new Insets(10, 10, 10, 10)); // Postavljanje margina dugmeta
                
                matricaDugmadi[i][j] = dugme;  // Dodavanje dugmeta na odgovarajucu poziciju u matrici buttons
                dugme.setBackground(Color.WHITE); // Postavljanje boje pozadine dugmeta na bijelu
                panel.add(dugme); // Dodavanje dugmeta na panel
                dugme.setFont(font); // Postavljanje fonta teksta dugmeta prema definisanom fontu
                if(j == 5) // Ako je trenutna kolona jednaka 5
                {
                	dugme.setText(String.valueOf(indeksVertikalno)); // Postavljanje teksta dugmeta na vertikalni indeks
                	poljaMape.put(new Polje(i,j), new PoljeInfo(dugme.getBackground(), indeksVertikalno + "", false, true));// Dodavanje informacija o polju u mapu polja
                	indeksVertikalno++; // Inkrementiranje vertikalnog indeksa za sledece dugme
                } 
                if(i == 5) // Ako je trenutni red jednak 5
                {
                	dugme.setText(String.valueOf(indeksHorizontalno)); // Postavljanje teksta dugmeta na horizontalni indeks
                	poljaMape.put(new Polje(i,j), new PoljeInfo(dugme.getBackground(), indeksHorizontalno + "", false, true));// Dodavanje informacija o polju u mapu polja
                	indeksHorizontalno++; // Inkrementiranje horizontalnog indeksa za sledece dugme
                }
                if((i >= 5 && i <= 14) && (j >= 5 && j <= 14)) // Ako se trenutno dugme nalazi unutar zadanih granica
                {
                	
                	dugme.setBackground(Color.BLUE); // Postavljanje boje pozadine dugmeta na plavu
                	poljaMape.forEach((polje, poljeInfo) -> // Iteracija kroz mapu polja i njihovih informacija
                	{
//                		int x = polje.getPozicijaX(); // Dobavljanje X koordinate polja
//                		int y = polje.getPozicijaY(); // Dobavljanje Y koordinate polja
                		
                		if(polje.uziDioGrada()) // Ako se polje nalazi unutar zadanih granica
                			poljeInfo.setBoja(Color.BLUE); // Postavljanje boje polja na plavu, ali onog polja koje je u mapi.
                	});
                }
            }
        }   
	}
	/**
     * Metoda za azuriranje mape na osnovu iznajmljivanja.
     * 
     * @param iznajmljivanje Objekat koji predstavlja iznajmljivanje.
     */
	public void azurirajMapu(Iznajmljivanje iznajmljivanje)
	{
		// Implementacija metode
		try 
		{
			if(iznajmljivanje == null)
				throw new IllegalArgumentException("Argument 'iznajmljivanje' je jednak null!");
			
			// Pronalazak najkrace putanje na osnovu pocetnog i odredisnog polja iznajmljivanja i smjestanje u listu.
			List<Polje> listaPolja = NajkracaPutanjaMatrice.pronalazakNajkracePutanje(iznajmljivanje);
	
			if(listaPolja == null)
				throw new NeispravanPronalazakPutanjeException("Neuspješan pronalazak putanje između polja matrice!");
			
			int brojPolja = listaPolja.size();
			// Podesi boju na slucajno generisanu boju, ali izbjegavaj vrlo tamne ili svijetle boje.
			Random random = new Random();
			int r, g, b;
			do {
	            r = random.nextInt(256);
	            g = random.nextInt(256);
	            b = random.nextInt(256);
	        }  while ((r + g + b) / 3 < 100 || 
	                (r < 70 && g < 70 && b < 70) ||  
	                (r > 185 && g > 185 && b > 185));
			
			Color randomBoja = new Color(r,g,b);
			
			if(iznajmljivanje.isKvar())
			{
				// Generisanje random polja na kome će se desiti kvar, ukoliko je u pitanju iznajmljivanje sa kvarom.
				brojPolja = random.nextInt(listaPolja.size());
				if(brojPolja == 0)
					brojPolja = 1;
				System.out.println("Dogodio se kvar na prevoznom sredstvu sa ID: " + iznajmljivanje.getPrevoznoSredstvo().getId() + " na polju: " + listaPolja.get(Math.abs(brojPolja - 1)));
			}
			
			// Prolazak kroz listu najkrace putanje.
			for(int i = 0; i < brojPolja; i++)
			{
				Polje polje = listaPolja.get(i);
				int x = polje.getPozicijaX();
				int y = polje.getPozicijaY(); 
				
				String tekst = iznajmljivanje.getPrevoznoSredstvo().getId() + " " + iznajmljivanje.getPrevoznoSredstvo().getTrenutniNivoBaterije() + "%";
			
				// Uzimanje reference na odgovarajuce dugme pozicije matrice i postavljanje nasumicne boje i teksta (labele).
				JButton dugme = matricaDugmadi[x][y];	
				dugme.setBackground(randomBoja);
				dugme.setText(tekst);
				double vrijemeTrajanja = ((double)iznajmljivanje.getTrajanjeKoriscenja() / listaPolja.size()) * 1000; // Vrijeme trajanja prelaska izmedju polja matrice.
				
				
				Thread.sleep(Math.round(vrijemeTrajanja));	// Uspavljivanje niti na prethodno izracunato vrijeme.	

				if(i == brojPolja - 1) // Ukoliko je rijec o posljednjem polju putanje.
				{
					
					if(!poljaMape.containsKey(polje)) // Ukoliko struktura poljaMape ne sadrzi posljednje polje.
					{
						poljaMape.put(polje, new PoljeInfo(randomBoja,tekst,true,false));//Posljednje polje se dodaje u strukturu i oznacava kao posljednje.
					}
					else // Ukkoliko struktura sadrzi posljednje polje.
					{
						// Vrsi se dohvatanje tog polja, sa njegovim informacijama (bojom, labelom).
						PoljeInfo poljeInfo = poljaMape.get(polje);
						
						// Vrsi se provjera da li je dohvaceno polje INDEKSIRANO.
						if(poljeInfo.isIndeksirano())
						{
							// Ukoliko jeste, vraca se u strukturu, i oznacava kao POSLJEDNJE.
							poljaMape.put(polje, new PoljeInfo(randomBoja,tekst,true,true));
						}
						else if(poljeInfo.isPosljednje())
						{
							// Vrsi se provjera da li je dohvaceno polje POSLJEDNJE.
							
							// Slucaj kada dvije niti zavrse na istom polju. Prikazuju se obe labele (ID  + Trenutni nivo baterije).
							dugme.setText(tekst + poljeInfo.getLabela());
							dugme.setBackground(randomBoja);
							
							// Zbog male velicine dugmeta, postavlja se ActionListener, koji daje informacije o prevoznim sredstvima na tom polju.
							ActionListener poruka = new ActionListener() 
							{
								@Override
								public void actionPerformed(ActionEvent e) 
								{
									JOptionPane.showMessageDialog(null,tekst  + "\n" + poljeInfo.getLabela() , null, JOptionPane.INFORMATION_MESSAGE);
								}
							};
							dugme.addActionListener(poruka);
								
						}
					}
				}
				
				if(poljaMape.containsKey(polje)) // Ukoliko je struktura sadrzi polje, a iteracija je razlicita od posljednje.
				{
					
					PoljeInfo poljeInfo = poljaMape.get(polje); // Vrsi se dohvatanje polja iz strukture.
				
					if((poljeInfo.isPosljednje() && i != listaPolja.size() - 1)) // Kada nit samo prelazi preko nekog polja na kojem je zavrsila neka druga nit.
					{
						dugme.setText(poljeInfo.getLabela());
						dugme.setBackground(poljeInfo.getBoja());
					} 
					else if(poljeInfo.isIndeksirano() && poljeInfo.isPosljednje()) // Kada nit zavrsava na nekom polju koje je indeksirano i posljednje.
					{
						dugme.setText(poljeInfo.getLabela());
						dugme.setBackground(poljeInfo.getBoja());
					}
					else if(poljeInfo.isIndeksirano()) //Kada nit prelazi preko nekog indeksiranog polja.
					{
						dugme.setText(poljeInfo.getLabela());
						dugme.setBackground(poljeInfo.getBoja());
					}
				}
				else 
				{
					
					if(polje.uziDioGrada()) //Kada nit prelazi preko nekog polja koje nije ni indeksirano ni posljednje.
					{
						dugme.setText("");
						dugme.setBackground(Color.BLUE);
					}
					else 
					{
						dugme.setText("");
						dugme.setBackground(Color.WHITE);
					}         
				}
				
				if(iznajmljivanje.getPrevoznoSredstvo().getTrenutniNivoBaterije() == 0)
				{
					System.out.println("Punjenje baterije za vozilo... " + iznajmljivanje.getPrevoznoSredstvo().getId());
					
					iznajmljivanje.getPrevoznoSredstvo().puniBateriju(100);
				}
				else 
				{
					iznajmljivanje.getPrevoznoSredstvo().setTrenutniNivoBaterije(iznajmljivanje.getPrevoznoSredstvo().getTrenutniNivoBaterije() - 5);
				}	
			}
		}
		catch (InterruptedException e) 
		{
		    System.out.println("Problem sa Mapom! Nit je prekinuta: " + e.getMessage());
		    Thread.currentThread().interrupt(); // Obezbjeđuje da se status prekinute niti ne izgubi, čak i kada se InterruptedException uhvati i obradi.
		}
		catch (IllegalArgumentException e) 
		{
		    System.out.println("Problem sa Mapom: " + e.getMessage());
		}
		catch (NeispravanPronalazakPutanjeException nppe) 
		{
			System.out.println("Problem sa Mapom: " + nppe.getMessage());
		}
		catch (Exception e) 
		{
			System.out.println("Neočekivan problem sa mapom!" + e.getMessage());
			e.printStackTrace();
		}
			
	}
	/**
     * Metoda namijenjena za vracanje matrice 20x20, zbog pronalazenja najkrace putanje.
     * 
     * @return Matrica[20][20]
     */
    public static int[][] getMatrica()
    {
    	int matrica[][] = new int[VELICINA][VELICINA];
    	for(int i = 0; i < VELICINA; i++)
    	{
    		for(int j = 0; j < VELICINA; j++)
    		{
    			matrica[i][j] = 1;
    		}
    	}
    	return matrica;
    }
    /**
     * Metoda namijenjena za vracanje velicine matrice
     */
    public static int getVelicina() 
    {
		return VELICINA;
	}
    /**
     * Metoda namijenjena za resetovanje ekrana, nakon zavrsetka jednog dijela iznajmljivanja.
     */
    public void resetGUI()
    {
    	 poljaMape.clear();
    	 JPanel panel = (JPanel) prozor.getContentPane().getComponent(0);
         panel.removeAll();
         inicijalizacijaDugmadi(panel, font);
         prozor.revalidate();
         prozor.repaint();

    }
}
