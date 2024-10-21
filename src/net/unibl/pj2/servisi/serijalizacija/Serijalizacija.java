package net.unibl.pj2.servisi.serijalizacija;
import java.io.*;

import net.unibl.pj2.servisi.finansije.ObracunGubitaka;

/**
 * Klasa Serijalizacija pruža metode za serijalizaciju i deserializaciju objekata tipa ObracunGubitaka.
 * 
 * Ova klasa omogućava čuvanje objekata u fajl i kasnije njihovo učitavanje.
 */
public class Serijalizacija
{
	/**
     * Serijalizuje dati objekat tipa ObracunGubitaka i čuva ga u fajl sa zadatim imenom.
     *
     * @param objekat objekat tipa ObracunGubitaka koji se serijalizuje
     * @param imeDatoteke ime fajla u koji će se objekat sačuvati
     */
	public static void serijalizujObjekat(ObracunGubitaka objekat, String imeDatoteke) 
	 {
        try (FileOutputStream datoteka = new FileOutputStream(imeDatoteke))
        {
        	ObjectOutputStream oos = new ObjectOutputStream(datoteka);
            oos.writeObject(objekat);
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("Fajl nije pronađen: " + e.getMessage());
        }
        catch (IOException e) 
        {
            System.out.println("Greška pri radu sa fajlom: " + e.getMessage());
        }
	 }
	 
	 /**
     * Deserijalizuje objekat tipa ObracunGubitaka iz fajla sa zadatim imenom.
     *
     * @param imeDatoteke ime fajla iz kog se objekat učitava
     * @return objekat tipa ObracunGubitaka koji je učitan iz fajla, ili null ako deserializacija nije uspela
     */
	 public static ObracunGubitaka deserijalizujObjekat(String imeDatoteke)
	 {
		try (FileInputStream datoteka = new FileInputStream(imeDatoteke))
        {
            ObjectInputStream ois = new ObjectInputStream(datoteka);
            return (ObracunGubitaka) ois.readObject();
        } 
		catch (FileNotFoundException e) 
        {
            System.out.println("Fajl nije pronađen: " + e.getMessage());
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("Problem sa kastovanjem: " + e.getMessage());
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri radu sa fajlom: " + e.getMessage());
        }
        return null;
	 }
}
