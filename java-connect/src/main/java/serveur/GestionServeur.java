package serveur;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import common.Competence;
import common.Diplome;
import common.Protocole;
import common.Utilisateur;
import common.GestionConnexions;
import sql.DBCompetence;
import sql.DBDiplome;
import sql.DBUtilisateur;

public class GestionServeur
{	
	private Protocole proto;
	private GestionConnexions gc;
	
	public GestionServeur()
	{
		// TODO Auto-generated constructor stub
		this.proto = new Protocole();
		this.gc= new GestionConnexions();
	}
	
	/**
	 * fonction de traitement du message reçu par le serveur
	 * @param message le message reçu par le serveur
	 * @return la réponse que le serveur envoie au client
	 */
	public String traiter (int id,PrintStream pC, String message)
	{
		if(!this.gc.streamExiste(id))
		{
			this.gc.nouveauClient(id, pC);
		}
			
		String retour ="";
		String[] splitMess = message.split("\\|");
		if (splitMess[0].equals(proto.getListUserString())){
			retour = listUsers(splitMess);
		}else if (splitMess[0].equals(proto.getDetailUserString())){
			retour = detailUser(splitMess);
		}else if (splitMess[0].equals(proto.getCreerCompteString())){
			retour = creerCompte(splitMess);
		}else if (splitMess[0].equals(proto.getModifInfoString())){
			retour = modifInfo(splitMess);
		}else if (splitMess[0].equals(proto.getAjoutDiplomeString())){
			retour = addDip(splitMess);
		}else if (splitMess[0].equals(proto.getSuppDiplomeString())){
			retour = delDip(splitMess);
		}else if (splitMess[0].equals(proto.getAddCompString())){
			retour = addCompt(splitMess);
		}else if (splitMess[0].equals(proto.getDelCompString())){
			retour = delComp(splitMess);
		}else if (splitMess[0].equals(proto.getConnectionString())){
			retour = connexion(splitMess, id);
		} else if (splitMess[0].equals(proto.getListCompString())){
			retour = listComp(splitMess);
		} else { 
			retour = "erreur message non reconnu";
		}
		return retour;
	}
	
	private String listUsers(String[] splitMess){
		String retour;
		String id = "0"; //contient l'id de l'utilisateur qui demande la liste des utilisateurs
		if ( splitMess.length > 1){
			id = splitMess[1];
		}
		ArrayList<Utilisateur> users = DBUtilisateur.lireUtilisateurs(); //ici apelle a la bdd pour récupérer la liste des utilisateurs
		//ajout de la gestion d'erreur.
		String mess = "";
		for (Utilisateur user : users) {
			mess = mess + user.getId() + ";" + user.getNom() + ";" + user.getPrenom() + ";" + user.getMail() + "|";
		}
		retour = proto.reponse(mess);
		return retour;
	}
	
	private String detailUser(String[] splitMess){
		String retour;
		String id = "0"; //contient l'id de l'utilisateur qui demande la liste des utilisateurs
		String idc; // id de l'utilisateur dont on récupére les détails
		String mess = "";
		if ( splitMess.length > 2){
			id = splitMess[1];
			idc = splitMess[2];

			Utilisateur user = DBUtilisateur.lireUtilisateur(Integer.parseInt(idc)); //Utilisateur dont on récupérer les détails
			Utilisateur demandeur = DBUtilisateur.lireUtilisateur(Integer.parseInt(id));
			ArrayList<Diplome> dips = DBDiplome.lireDiplomesUtilisateur(user); // tableau de string contenant les diplome de l'utilisateur
			ArrayList<Competence> comps = DBCompetence.lireCompetencesUtilisateur(user); //tableau de compétence de l'utilisateur
			mess = user.getId() + ";" + user.getNom() + ";" + user.getPrenom() + ";" + user.getMail() + "|";
			for (Diplome dip : dips) {
				mess = mess + dip.getId() + ";" + dip.getDiplome() + ";" + dip.getAnnee() + ";" + "/";
			}
			mess = mess  + "|";
			for (Competence comp : comps) {
				mess = mess + comp.getId() + ";" + comp.getCompetence() + "/";
			}
			retour = proto.reponse(mess);
		}else{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
	
	private String creerCompte(String[] splitMess){
		String retour;
		String mess;
		if ( splitMess.length > 4){
			String nom = splitMess[2];
			String prenom = splitMess[3];
			String mdp = splitMess[4];
			String email = splitMess[5];
			if (DBUtilisateur.insererUtilisateur(new Utilisateur(0, nom, prenom, email, mdp, 0))){ //replacer la condition par une vérification de la bonne execution de l'ajout user
				mess = "OK";
				retour = proto.reponse(mess);
			}else{
				mess = "erreur de création de compte, déjà existant ?";
				retour = proto.erreur("400", mess);
			}
		}else{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
	
	private String modifInfo(String[] splitMess){
		String retour ="";
		String id = splitMess[1];
		Utilisateur user = DBUtilisateur.lireUtilisateur(Integer.parseInt(id));
		user.setMail(splitMess[2]);
		user.setNom(splitMess[4]);
		user.setPrenom(splitMess[5]);
		DBUtilisateur.modifierUtilisateur(user);
		return retour;
	}
	
	private String addDip(String[] splitMess){
		String retour;
		String mess;
		if ( splitMess.length > 3){
			String id = splitMess[1];
			String idd = splitMess[2];
			String annee = splitMess[3];
			//requet d'ajout du diplome
			if (DBDiplome.ajoutDiplomeUtilisateur(new Diplome(Integer.parseInt(idd), ""), new Utilisateur(Integer.parseInt(id), "", "", "", "", 0), Integer.parseInt(annee))){ //retour de la bdd
				mess = "ok";
				retour = proto.reponse(mess);
			}else{
				mess = "erreur pendant l'ajout du diplome";
				retour = proto.erreur("400", mess);
			}
		}else{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
	
	private String delDip(String[] splitMess){
		String retour;
		String mess;
		if ( splitMess.length > 2){
			String id = splitMess[1];
			String idd = splitMess[2];
			if (DBDiplome.supprimerDiplomeUtilisateur(new Diplome(Integer.parseInt(idd), ""), new Utilisateur(Integer.parseInt(id), "", "", "", "", 0))){ //retour de la bdd
				mess = "ok";
				retour = proto.reponse(mess);
			}else{
				mess = "erreur pendant la supression du diplome";
				retour = proto.erreur("400", mess);
			}
		}else{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
	
	private String addCompt(String[] splitMess){
		String retour;
		String mess;
		if ( splitMess.length > 2){
			String id = splitMess[1];
			String idc = splitMess[2];
			if (DBCompetence.ajoutCompetenceUtilisateur(new Competence(Integer.parseInt(idc), ""), new Utilisateur(Integer.parseInt(id), "", "", "", "", 0))){ //retour de la bdd
				mess = "ok";
				retour = proto.reponse(mess);
			}else{
				mess = "erreur pendant l'ajout de compétence";
				retour = proto.erreur("400", mess);
			}
		}else{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
	
	private String listComp(String[] splitMess){
		String mess = "";
		ArrayList<Competence> comps = DBCompetence.lireCompetences(); //tableau de compétence de l'utilisateur
		for (Competence comp : comps) {
			mess = mess + comp.getId() + ";" + comp.getCompetence() + "|";
		}
		return proto.reponse(mess);
	}
	
	private String connexion (String[] splitMess, int idS){
		String mess, retour;
		if ( splitMess.length > 2)
		{
			String pseudo = splitMess[1];
			String mdp = splitMess[2];
			//requet de connection retournant l'id de l'utilisateur
			Utilisateur user = DBUtilisateur.checkConnexion(new Utilisateur(0, "", "", pseudo, mdp, 0)); 
			if (!gc.streamUtilisateurExiste(idS))
			{
				if(!gc.estConnecte(user))
				{
					if( user != null)
					{ 
						//TO DO
						mess = Integer.toString(user.getId());
						retour = proto.reponse(mess);
						
						//Initialiser la co dans Gestion connexion
						gc.identification(idS, user);
						
						//Dire bonjour
						System.out.println("CONNECTE"+user);

					} 
					else 
					{
						mess = "Erreur de connexion";
						retour = proto.erreur("400", mess);
					}
				}
				else
				{
					mess = "Vous etes deja connecte ailleurs";
					retour = proto.erreur("400", mess);
				}
			}
			else
			{
				mess = "Vous etes deja connecte sous:"+user.getMail();
				retour = proto.erreur("400", mess);
			}
		} 
		else 
		{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
	
	private String delComp(String[] splitMess){
		String mess, retour;
		if ( splitMess.length > 2){
			String id = splitMess[1];
			String idc = splitMess[2];
			if (DBCompetence.supprimerCompetenceUtilisateur(new Competence(Integer.parseInt(idc), ""), new Utilisateur(Integer.parseInt(id), "", "", "", "", 0))){ //retour de la bdd
				mess = "ok";
				retour = proto.reponse(mess);
			}else{
				mess = "erreur pendant la supression de compétence";
				retour = proto.erreur("400", mess);
			}
		}else{
			mess ="Erreur nombre de parametre invalide";
			retour = proto.erreur("400", mess);
		}
		return retour;
	}
}
