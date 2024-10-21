package net.unibl.pj2.izuzeci;

/**
 * Izuzetak koji se baca kada je dužina reda neodgovarajuća.
 */
public class NeodgovarajucaDuzinaRedaException extends Exception
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
	public NeodgovarajucaDuzinaRedaException(String poruka)
	{
		super(poruka);
	}
}
