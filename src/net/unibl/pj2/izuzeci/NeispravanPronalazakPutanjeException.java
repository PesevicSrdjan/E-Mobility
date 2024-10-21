package net.unibl.pj2.izuzeci;

/**
 * Izuzetak koji se baca kada nije moguće formirati put između polja matrice.
 */
public class NeispravanPronalazakPutanjeException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // Dodato da se ukloni upozorenje.

	/**
     * Kreira izuzetak sa datom porukom.
     * 
     * @param poruka Poruka o grešci.
     */
	public NeispravanPronalazakPutanjeException(String poruka) 
	{
		super(poruka);
	}

}
