package net.unibl.pj2.ekrani.mapa;

/**
 * Klasa <code>Polje</code> predstavlja jedno polje u matrici dimenzija 20x20 koja prikazuje grad.
 * Svako polje ima svoje koordinate u toj matrici koje definisu njegovu poziciju.
 */
public class Polje 
{
	private int pozicijaX, pozicijaY; // X i  Y koordinata polja.

	/**
     * Podrazumijevani konstruktor koji kreira polje sa podrazumijevanim vrijednostima.
     */
	public Polje() {
		super();
		
	}
	/**
     * Konstruktor koji inicijalizuje polje sa datim koordinatama.
     * 
     * @param pozicijaX X koordinata polja
     * @param pozicijaY Y koordinata polja
     */
	public Polje(int pozicijaX, int pozicijaY) {
		super();
		this.pozicijaX = pozicijaX;
		this.pozicijaY = pozicijaY;
	}
	/**
     * Vraca X koordinatu polja.
     * 
     * @return X koordinata polja
     */
	public int getPozicijaX() {
		return pozicijaX;
	}
	/**
    * Postavlja X koordinatu polja.
    * 
    * @param pozicijaX X koordinata polja
    */
	public void setPozicijaX(int pozicijaX) {
		this.pozicijaX = pozicijaX;
	}
	/**
     * Vraca Y koordinatu polja.
     * 
     * @return Y koordinata polja
     */
	public int getPozicijaY() {
		return pozicijaY;
	}
	/**
     * Postavlja Y koordinatu polja.
     * 
     * @param pozicijaY Y koordinata polja
     */
	public void setPozicijaY(int pozicijaY) {
		this.pozicijaY = pozicijaY;
	}
	/**
    * Provjerava da li je polje u uzem dijelu grada koji je definisan kao 
    * kao kvadrat sa gornjim lijevim uglom na (5, 5) i donjim desnim uglom na (14, 14).
    * 
    * @return <code>true</code> ako je polje u uzem dijelu grada, <code>false</code> inace
    */
	public boolean uziDioGrada()
	{
		if((pozicijaX >= 5 && pozicijaX <= 14) && (pozicijaY >= 5 && pozicijaY <= 14))
			return true;
		return false;
	}
	/**
     * Uporedjuje ovo polje sa datim objektom. Dva polja su jednaka ako imaju iste koordinate.
     * 
     * @param o Objekt koji se poredi sa ovim poljem
     * @return <code>true</code> ako su polja jednaka, <code>false</code> inace
     */
	@Override
	public boolean equals(Object o)
	{
		Polje polje = (Polje)o;
		if (pozicijaX == polje.pozicijaX && pozicijaY == polje.pozicijaY)
			return true;
		return false;
	}
	/**
     * Vraca String reprezentaciju polja u formatu "X Y".
     * 
     * @return String reprezentacija polja
     */
	@Override
	public String toString()
	{
		return pozicijaX + " " + pozicijaY;
	}
	/**
     * Vraca hash kod za ovo polje na osnovu njegovih koordinata.
     * 
     * @return Hash kod za ovo polje
     */
	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * pozicijaX;
		result = 31 * pozicijaY;
		return result;
	}
}