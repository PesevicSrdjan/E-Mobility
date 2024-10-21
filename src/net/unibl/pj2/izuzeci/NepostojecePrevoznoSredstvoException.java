package net.unibl.pj2.izuzeci;

/**
 * Izuzetak koji se baca kada ne postoji prevozno sredstvo za iznajmljivanje.
 */
public class NepostojecePrevoznoSredstvoException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // Dodato kako bi se obrisalo upozorenje.

	/**
     * Kreira izuzetak sa datom porukom.
     * 
     * @param poruka Poruka o grešci.
     */
	public NepostojecePrevoznoSredstvoException(String poruka) 
	{
		super(poruka);
	}

}
