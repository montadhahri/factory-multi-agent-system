package usine;

import java.util.ArrayList;
import java.util.List;

public class Commande {
	private int IdCommande;
	private List<ProduitCommande> Commandes;
	private Boolean isLivrer;
	private String AdressClient;
	
	
	public Commande(int id,String AdressClient,List<ProduitCommande> Commandes){
		this.Commandes = new ArrayList<ProduitCommande>();
		this.Commandes.addAll(Commandes);
		IdCommande = id;
		isLivrer = false;
		this.AdressClient = AdressClient;
	}
	
	
	public Commande(int id,String AdressClient){
		this.Commandes = new ArrayList<ProduitCommande>();
		IdCommande = id;
		isLivrer = false;
		this.AdressClient = AdressClient;
	}
	
	public String getAdressClient() {
		return AdressClient;
	}

	public void setAdressClient(String adressClient) {
		AdressClient = adressClient;
	}

	public Boolean getIsLivrer() {
		return isLivrer;
	}

	public void setIsLivrer(Boolean isLivrer) {
		this.isLivrer = isLivrer;
	}

	public void setCommandes(ProduitCommande cmd){
		Commandes.add(cmd);
	}
	
	public int getIdCommande() {
		return IdCommande;
	}
	public void setIdCommande(int idCommande) {
		IdCommande = idCommande;
	}
	public String a(){
		String q = "";
		for (ProduitCommande produitCommande : Commandes) {
			q += produitCommande.getQuantite() + " ";
		}
		return q;
	}
	
	public List<ProduitCommande> getCommandes() {
		return Commandes;
	}

	public Boolean isPreparer(){
		Boolean bool = true;
		for (ProduitCommande produitCommande : Commandes) {
			if(!produitCommande.getIsPreparer()){
				bool = false;
			}
		}
		return bool;
	}
	
	public Boolean isTraiter(){
		Boolean bool = true;
		for (ProduitCommande produitCommande : Commandes) {
			if(!produitCommande.getIsTraiter()){
				bool = false;
			}
		}
		return bool;
	}
}
