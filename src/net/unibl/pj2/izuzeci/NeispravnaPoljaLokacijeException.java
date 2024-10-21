package net.unibl.pj2.izuzeci;

/**
 * Izuzetak koji se baca kada su neispravna polja početne i odredišne lokacije u csv datoteci.
 */
public class NeispravnaPoljaLokacijeException extends Exception
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
	public NeispravnaPoljaLokacijeException(String poruka) 
	{
		super(poruka);
	}
}
