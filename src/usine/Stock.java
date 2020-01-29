package usine;

import java.util.ArrayList;
import java.util.List;

public class Stock {
	private List<ProduitStock> stock;

	public Stock(List<ProduitStock> stock) {
		super();
		this.stock = new ArrayList<>();
		this.stock.addAll(stock);
	}

	public List<ProduitStock> getStock() {
		return stock;
	}

	public void setStock(List<ProduitStock> stock) {
		this.stock = stock;
	}
	
	public int getIndexProduit(String name){
		int i = 0,k = 0;
		for (ProduitStock produitStock : stock) {
			if(produitStock.getNameProduit().equals(name)){
				k = i;
			}
			i++;
		}
		return k;
	}
	
	
	public Boolean isRepture(ProduitCommande produit){
		ProduitStock p = stock.get(getIndexProduit(produit.getNameProduit()));
		return p.getQuantite() - produit.getQuantite() > 0 ? false : true;
	}
	
	public void livrerCommande(ProduitCommande produit){
		ProduitStock p = stock.get(getIndexProduit(produit.getNameProduit()));
		p.setQuantite(p.getQuantite() - produit.getQuantite());
	}
	public int qteBois(ProduitCommande produit){
		ProduitStock prduit = stock.get(getIndexProduit(produit.getNameProduit()));
		return prduit.getQteBois()*prduit.getQteInitial();
	}
	public String toString(){
		String st = stock.get(0).getNameProduit() + ":" + stock.get(0).getQuantite();
		for (int i = 1; i < stock.size(); i++) {
			st += ","+stock.get(i).getNameProduit() + ":" + stock.get(i).getQuantite();
		}
		return st;
	}
}
