package usine;

public class ProduitCommande extends Produit {
	
	private int idCommande;
	private Boolean isTraiter;
	private Boolean isPreparer;

	public ProduitCommande(String nameProduit, int quantite) {
		super(nameProduit, quantite);
		isTraiter = false;
		isPreparer = false;
	}

	public ProduitCommande(int id,String nameProduit, int quantite) {
		super(nameProduit, quantite);
		idCommande = id;
		isTraiter = false;
		isPreparer = false;
	}
	
	public Boolean getIsTraiter() {
		return isTraiter;
	}

	public void setIsTraiter(Boolean isTraiter) {
		this.isTraiter = isTraiter;
	}

	public Boolean getIsPreparer() {
		return isPreparer;
	}

	public void setIsPreparer(Boolean isPreparer) {
		this.isPreparer = isPreparer;
	}

	public int getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}
	
}
