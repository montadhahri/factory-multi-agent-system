package usine;

public class ProduitStock extends Produit {
	private int QteInitial;
	private String TypeBois;
	private int qteBois;
	
	
	public ProduitStock(String nameProduit, int quantite,String TypeBois,int qteBois) {
		super(nameProduit, quantite);
		this.QteInitial = quantite;
		this.TypeBois = TypeBois;
		this.qteBois = qteBois;
	}


	public int getQteInitial() {
		return QteInitial;
	}


	public void setQteInitial(int qteInitial) {
		QteInitial = qteInitial;
	}


	public String getTypeBois() {
		return TypeBois;
	}


	public void setTypeBois(String typeBois) {
		TypeBois = typeBois;
	}


	public int getQteBois() {
		return qteBois;
	}


	public void setQteBois(int qteBois) {
		this.qteBois = qteBois;
	}
	
	
}
