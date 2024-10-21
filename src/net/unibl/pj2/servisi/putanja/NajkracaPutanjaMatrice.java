package net.unibl.pj2.servisi.putanja;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

import net.unibl.pj2.ekrani.mapa.Mapa;
import net.unibl.pj2.ekrani.mapa.Polje;
import net.unibl.pj2.servisi.iznajmljivanje.Iznajmljivanje;

/**
* Klasa NajkracaPutanjaMatrice pronalazi najkraći put u matrici mape koristeći BFS algoritam.
* Algoritam pronalazi put od početne do odredišne lokacije za zadato iznajmljivanje.
* 
* Implementacija BFS algoritma inspirisana je kodom pronađenim na
* 
* <a href="https://github.com/sarthak-srivastava/Competitive_Ques/blob/master/ShortestMazeBFS.java"> ovom linku</a>
*/
public class NajkracaPutanjaMatrice 
{
	// Pomoćni nizovi za kretanje po susjednim ćelijama matrice (gore, dole, lijevo, desno).
    private static final int[] redKretanje = {-1, 1, 0, 0}; // Kretanje po redovima matrice.
    private static final int[] kolonaKretanje = {0, 0, -1, 1}; // Kretanje po kolonama matrice.

    /**
     * Unutrašnja klasa koja predstavlja čvor u BFS pretrazi.
     * Svaki čvor sadrži trenutnu poziciju, distancu od početne lokacije i pokazivač na roditelja.
     */
    static class Cvor 
    {
        int red, kolona, distanca; // Pozicija i distanca čvora.
        Cvor roditelj; // Pokazivač na roditelja.

        // Konstruktor koji inicijalizuje čvor sa datim redom, kolonom, distancom i roditeljem.
        Cvor(int red, int kolona, int distanca, Cvor roditelj) 
        {
            this.red = red;
            this.kolona = kolona;
            this.distanca = distanca;
            this.roditelj = roditelj;
        }
    }

    /**
     * Metoda koja provjerava da li je polje validno za dalje kretanje.
     * Polje je validno ako je unutar granica matrice i ako nije već posjećeno.
     *
     * @param red trenutni red.
     * @param kolona trenutna kolona.
     * @param posjecen matrica koja označava posjećena polja.
     * @return true ako je polje validno za kretanje, inače false.
     */
    private static boolean poljeValidacija(int red, int kolona, boolean[][] posjecen) 
    {
        return (red >= 0) && (red < Mapa.getVelicina()) && (kolona >= 0) && (kolona < Mapa.getVelicina()) && !posjecen[red][kolona];
    }

    /**
     * Metoda koja pronalazi najkraći put između početne i odredišne lokacije za zadato iznajmljivanje.
     *
     * @param iznajmljivanje instanca klase Iznajmljivanje koja sadrži početnu i odredišnu lokaciju.
     * @return lista objekata tipa Polje koji predstavljaju najkraći put, ili null ako put ne postoji.
     */
    public static List<Polje> pronalazakNajkracePutanje(Iznajmljivanje iznajmljivanje) 
    {
    	// Provjera da li se može formirati put između početne i odredišne lokacije.
        if (Mapa.getMatrica()[iznajmljivanje.getPocetnaLokacija().getPozicijaX()][iznajmljivanje.getPocetnaLokacija().getPozicijaY()] == 0 || Mapa.getMatrica()[iznajmljivanje.getOdredisnaLokacija().getPozicijaX()][iznajmljivanje.getOdredisnaLokacija().getPozicijaY()] == 0) 
        {
            return null; // Ukoliko to nije moguće, vraća se null.
        }

        // Matrica koja označava da li je određeno polje već posjećeno.
        boolean[][] posjecen = new boolean[Mapa.getVelicina()][Mapa.getVelicina()];
        // Red za BFS pretragu.
        Queue<Cvor> red = new LinkedList<>();
        // Obilježavanje početne lokacije kao posjećene i dodavanje prvog čvora u red.
        posjecen[iznajmljivanje.getPocetnaLokacija().getPozicijaX()][iznajmljivanje.getPocetnaLokacija().getPozicijaY()] = true;
        red.add(new Cvor(iznajmljivanje.getPocetnaLokacija().getPozicijaX(), iznajmljivanje.getPocetnaLokacija().getPozicijaY(), 0, null));

        Cvor krajnjiCvor = null;// Zadržava krajnji čvor kada ga pronađemo.
        
        while (!red.isEmpty()) 
        {
            Cvor trenutniCvor = red.poll(); // Uzimanje čvora iz reda.

            // Provjera da li je trenutni čvor na odredišnoj lokaciji.
            if (trenutniCvor.red == iznajmljivanje.getOdredisnaLokacija().getPozicijaX() && trenutniCvor.kolona == iznajmljivanje.getOdredisnaLokacija().getPozicijaY()) 
            {
                krajnjiCvor = trenutniCvor; // Ako jeste, zapamti ovaj čvor kao krajnji.
                break; // Prekini pretragu.
            }

            // Iteracija kroz susjedne ćelije (gore, dole, lijevo, desno).
            for (int i = 0; i < 4; i++) 
            {
                int noviRed = trenutniCvor.red + redKretanje[i]; // Novi red posle kretanja.
                int novaKolona = trenutniCvor.kolona + kolonaKretanje[i]; // Nova kolona posle kretanja.

                // Provjera validnosti novog polja i da li je prohodno.
                if (poljeValidacija(noviRed, novaKolona, posjecen) && Mapa.getMatrica()[noviRed][novaKolona] == 1) 
                {
                    posjecen[noviRed][novaKolona] = true; // Obilježavanje novog polja kao posećenog.
                    red.add(new Cvor(noviRed, novaKolona, trenutniCvor.distanca + 1, trenutniCvor));  // Dodavanje novog čvora u red.
                }
            }
        }

        if (krajnjiCvor == null) 
        {
            return null; // Ako krajnji čvor nije pronađen, put ne postoji.
        }

        // Kreiranje liste koja čuva najkraći put.
        LinkedList<Polje> putanja = new LinkedList<>();
        while (krajnjiCvor != null) 
        {
            putanja.addFirst(new Polje(krajnjiCvor.red, krajnjiCvor.kolona)); // Dodavanje polja u putanju.
            krajnjiCvor = krajnjiCvor.roditelj; // Nastavak do roditelja čvora.
        }

        return putanja; // Vraćanje liste sa najkraćim putem.
    }
}