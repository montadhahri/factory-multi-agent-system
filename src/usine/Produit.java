package usine;

import jade.util.leap.Serializable;

public class Produit {
	private String NameProduit;
	private int Quantite;
	
	public Produit(String nameProduit, int quantite) {
		super();
		NameProduit = nameProduit;
		Quantite = quantite;
	}
	public String getNameProduit() {
		return NameProduit;
	}
	public void setNameProduit(String nameProduit) {
		NameProduit = nameProduit;
	}
	public int getQuantite() {
		return Quantite;
	}
	public void setQuantite(int quantite) {
		Quantite = quantite;
	}
	
}
