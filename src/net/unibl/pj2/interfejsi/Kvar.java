package net.unibl.pj2.interfejsi;

/**
 * Interfejs koji predstavlja kvar na prevoznom sredstvu.
 */
public interface Kvar 
{
	/**
     * Metoda koja opisuje kvar sa opisom i vremenom nastanka.
     *
     * @param opis Opis kvara.
     * @param datumIVrijeme Datum i vrijeme kada je kvar nastao.
     * @return String koji sadrži opis kvara i vrijeme kada je nastao.
     */
	String kvar(String opis, String datumIVrijeme);

}
