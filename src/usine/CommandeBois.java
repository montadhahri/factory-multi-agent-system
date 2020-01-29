package usine;

public class CommandeBois {
	private String TypeBois;
	private int Qte;
	private String AdressAtelier;
	private String nameProduit;
	
	public CommandeBois(String typeBois, int qte, String adressAtelier) {
		super();
		TypeBois = typeBois;
		Qte = qte;
		AdressAtelier = adressAtelier;
	}
	public String getTypeBois() {
		return TypeBois;
	}
	public void setTypeBois(String typeBois) {
		TypeBois = typeBois;
	}
	public int getQte() {
		return Qte;
	}
	public void setQte(int qte) {
		Qte = qte;
	}
	public String getAdressAtelier() {
		return AdressAtelier;
	}
	public void setAdressAtelier(String adressAtelier) {
		AdressAtelier = adressAtelier;
	}
	public String getNameProduit() {
		return nameProduit;
	}
	public void setNameProduit(String nameProduit) {
		this.nameProduit = nameProduit;
	}
	
}
