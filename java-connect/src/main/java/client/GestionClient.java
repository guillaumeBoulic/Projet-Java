package client;

import java.io.IOException;
import java.rmi.server.SocketSecurityException;
import java.util.ArrayList;
import java.util.Scanner;

import common.Competence;
import common.Diplome;
import common.Message;
import common.Protocole;
import common.Utilisateur;
import serveur.Serveur;
import serveur.ServiceServerMessagerie;
import sql.DBCompetence;
import sql.DBDiplome;
import sql.DBUtilisateur;

/**
 * @author robin
 *
 */
public class GestionClient 
{
	private Protocole proto;
	private Client c;
	private Utilisateur u;

	public GestionClient() 
	{
		this.proto= new Protocole();
		final String listUserString = proto.getListUserString();
		this.u= new Utilisateur(0, "Anonyme", "", "", "", 0);
		this.c= new Client();
	}
	//traitement des message pour analyser et deduire le motif de la requete ainsi que les erreurs
	public String traiter (String message)
	{
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
			retour = connexion(splitMess);
		} else if (splitMess[0].equals(proto.getListCompString())){
			retour = listComp(splitMess);
		} else if (splitMess[0].equals(proto.getListDipString())){
			retour = listDip(splitMess);
		}else if (splitMess[0].equals(proto.getEcrireMail())){
			retour = ecrireMail(splitMess);
		}else if (splitMess[0].equals(proto.getReleverMessages())){
			retour = releverMessages(splitMess);
		}else if (splitMess[0].equals(proto.getLireMessage())){
			retour = lireMessage(splitMess);
		}else if (splitMess[0].equals(proto.getListUserCo())){
			retour = listUserCo(splitMess);
		}else if (splitMess[0].equals(proto.getPasserEnEcoute())){
			retour = passerEnEcoute(splitMess);
		}else if (splitMess[0].equals(proto.getParler())){
			retour = parler(splitMess);
		}else if (splitMess[0].equals(proto.getAddRecomendationString())){
			retour = addRecomendation(splitMess);
		}else if (splitMess[0].equals(proto.getDelRecomendationString())){
			retour = delRecommendation(splitMess);
		}else{ 
			retour = "CLIENT: Erreur message non reconnu";
		}


		return retour;
	}

	/**
	 * Dit si un utilisateur est connecte
	 * @return
	 */
	private boolean connecte ()
	{
		return this.u!=null;
	}

	/**
	 * Traite le listage des users
	 * @param splitMess
	 * @return
	 */
	private String listUsers(String[] splitMess){
		String mess="lol", id;
		Scanner sc= new Scanner(System.in);		


		String commande="LIST_USERS|0|";

		try 
		{
			String[] retour=this.c.communiquer(commande).split("\\|");
			int c=0;
			for(String s: retour)
			{
				String[] s1= s.split (";");
				for(String s2: s1)
				{
					System.out.print(s2+" ");
				}
				System.out.println("");
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lol";
	}

	/**
	 * Donne le detail d'un utilisateur
	 * @param splitMess
	 * @return
	 */
	private String detailUser(String[] splitMess){
		String mess="lol", id;
		Scanner sc= new Scanner(System.in);		


		String commande="DETAIL_USER|"+this.u.getId()+"|";

		try 
		{

			System.out.print("Id user:");
			id= sc.nextLine();
			
			String[] retour=this.c.communiquer(commande+id).split("\\|");
			
			String[] s1= retour[1].split (";");
			System.out.println("Id: "+s1[0]+", Nom: "+s1[1].toUpperCase()+", Prenom:"+s1[2]+", Mail:"+s1[3]);

			if(retour.length>2)
			{
				//Diplomes
				System.out.print("Diplomes: ");
				for(String s2: retour[2].split(";"))
				{
					System.out.print(s2+" ");
				}
				System.out.println("");
			}

			if(retour.length>3)
			{
				//Competences
				System.out.print("Competences: ");
				for(String s2: retour[3].split(";"))
				{
					System.out.print(s2+" ");
				}

				System.out.println("");

			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lol";
	}
	
	private String addRecomendation(String[] splitMess){
		String mess=" ", id, comp;
		Scanner sc= new Scanner(System.in);		


		String commande= proto.getAddRecomendationString() + "|"+this.u.getId()+"|";

		try 
		{

			System.out.print("Id user:");
			id= sc.nextLine();

			System.out.print("Id compétence:");
			comp= sc.nextLine();
			
			String[] retour=this.c.communiquer(commande+id+"|"+comp).split("\\|");
			if(retour[0].equalsIgnoreCase("200")){
				System.out.println("Recommendation Ajouté");
			}else{
				System.out.println(retour[0]+" "+retour[1]);
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return " ";
	}
	private String delRecommendation(String[] splitMess){
		String mess=" ", id, comp;
		Scanner sc= new Scanner(System.in);		


		String commande= proto.getDelRecomendationString() + "|"+this.u.getId()+"|";

		try 
		{

			System.out.print("Id user:");
			id= sc.nextLine();

			System.out.print("Id compétence:");
			comp= sc.nextLine();
			
			String[] retour=this.c.communiquer(commande+id+"|"+comp).split("\\|");
			if(retour[0].equalsIgnoreCase("200")){
				System.out.println("Recommendation supprimé");
			}else{
				System.out.println(retour[0]+" "+retour[1]);
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return " ";
	}

	/**
	 * Liste les competences de la base
	 * @param splitMess
	 * @return
	 */
	private String listComp(String[] splitMess){
		String mess="lol", id;
		Scanner sc= new Scanner(System.in);		


		String commande="LIST_COMP|0";

		try 
		{
			String[] retour=this.c.communiquer(commande).split("\\|");
			int c=0;
			for(String s: retour)
			{
				String[] s1= s.split (";");
				for(String s2: s1)
				{
					System.out.print(s2+" ");
				}
				System.out.println("");
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lol";
	}

	/**
	 * Cree un compte utilisateur
	 * @param splitMess
	 * @return
	 */
	private String creerCompte(String[] splitMess){
		String mess="lol", nom, prenom, mdp, mail, vuMail, vuComp,vuDip;
		Scanner sc= new Scanner(System.in);		


		String commande="CREER_COMPTE|0|";

		try 
		{

			System.out.print("Nom:");
			nom= sc.nextLine();

			System.out.print("Prenom:");
			prenom= sc.nextLine();

			System.out.print("Mail");
			mail= sc.nextLine();
			
			System.out.print("Mot de passe");
			mdp= sc.nextLine();

			System.out.print("Niveau visibilite mail:");
			vuMail= sc.nextLine();
			
			System.out.print("Niveau visibilite competences:");
			vuComp= sc.nextLine();
			
			System.out.print("Niveau visibiité diplomes");
			vuDip= sc.nextLine();

			String[] retour=this.c.communiquer(commande+nom+"|"+prenom+"|"+mdp+"|"+mail+"|"+vuMail+"|"+vuComp+"|"+vuDip).split("\\|");

			for(String s: retour)
			{
				System.out.println(s);
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lol";
	}

	/**
	 * Effectue une demande de connexion
	 * @param splitMess
	 * @return
	 */
	private String connexion (String[] splitMess){
		String pseudo, mdp;
		Scanner sc= new Scanner(System.in);
		String commande="CONNECTION|";
		try 
		{

			System.out.print("pseudo:");
			pseudo= sc.nextLine();

			System.out.print("mdp:");
			mdp= sc.nextLine();

			String[] retour=this.c.communiquer(commande+pseudo+"|"+mdp).split("\\|");

			if(retour[0].equalsIgnoreCase("200"))
			{
				System.out.println("Connexion OK");
				int id= Integer.parseInt(retour[1]);

				//Dire bonjour
				commande="DETAIL_USER|"+this.u.getId()+"|"+id;

				retour=this.c.communiquer(commande).split("\\|");

				String[] s1= retour[1].split (";");
				System.out.println("Bonjour "+s1[1].toUpperCase()+" "+s1[2].toLowerCase());

				this.u=new Utilisateur(Integer.parseInt(s1[0]), s1[1],s1[2], s1[3], "", 0);

				System.out.println("");

			}
			else
			{
				System.out.println(retour[0]+" "+retour[1]);
			}
		} 
		catch (Exception e) 
		{
			System.err.println(e);
			e.printStackTrace();
		}

		return "lol";
	}

	/**
	 * Modifie les infos d'un utilisateur
	 * @param splitMess
	 * @return
	 */
	private String modifInfo(String[] splitMess){

		String id, nom, prenom, mdp, mail ="", vuMail, vuComp, vuDip;
		Scanner sc= new Scanner(System.in);		
		try 
		{
			System.out.print("id:");
			id= sc.nextLine();

			System.out.print("Nom:");
			nom= sc.nextLine();

			System.out.print("Prenom:");
			prenom= sc.nextLine();

			System.out.print("Mot de passe:");
			mdp= sc.nextLine();

			System.out.print("Niveau visibilite mail:");
			vuMail= sc.nextLine();
			
			System.out.print("Niveau visibilite competences:");
			vuComp= sc.nextLine();
			
			System.out.print("Niveau visibiité diplomes:");
			vuDip= sc.nextLine();


			String[] retour=this.c.communiquer(proto.reqModifInfo(id, mail, mdp, nom, prenom, vuMail, vuComp, vuDip)).split("\\|");

			for(String s: retour)
			{
				System.out.println(s);
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lol";
	}

	/**
	 * Ajoute un diplome a un utilisateur
	 * @param splitMess
	 * @return
	 */
	private String addDip(String[] splitMess){
		String idU, idD, annee;
		Scanner sc= new Scanner(System.in);		


		String commande="AJOUT_DIPLOME|";

		try 
		{
			System.out.print("ID Utilisateur:");
			idU= sc.nextLine();

			System.out.print("ID Diplome:");
			idD= sc.nextLine();

			System.out.print("Annee d'obtention:");
			annee= sc.nextLine();



			String retour=this.c.communiquer(commande+idU+"|"+idD+"|"+annee);

			System.out.println(retour);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lol";
	}

	/**
	 * Enleve un diplome a un utilisateur
	 * @param splitMess
	 * @return
	 */
	private String delDip(String[] splitMess){
		String idU, idD;
		Scanner sc= new Scanner(System.in);		
		String commande="SUPP_DIPLOME|";

		try 
		{
			System.out.print("ID Utilisateur:");
			idU= sc.nextLine();

			System.out.print("ID Diplome:");
			idD= sc.nextLine();

			String retour=this.c.communiquer(commande+idU+"|"+idD);

			System.out.println(retour);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lol";
	}

	/**
	 * Ajoute une competence a un utilisateur
	 * @param splitMess
	 * @return
	 */
	private String addCompt(String[] splitMess){
		String idU, idC;
		Scanner sc= new Scanner(System.in);		
		String commande="AJOUT_COMP|";

		try 
		{
			System.out.print("ID Utilisateur:");
			idU= sc.nextLine();

			System.out.print("ID Competence:");
			idC= sc.nextLine();

			String retour=this.c.communiquer(commande+idU+"|"+idC);

			System.out.println(retour);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lol";
	}

	/**
	 * Enleve une competence a un utilisateur
	 * @param splitMess
	 * @return
	 */
	private String delComp(String[] splitMess){
		String idU, idC;
		Scanner sc= new Scanner(System.in);		
		String commande="DEL_COMP|";

		try 
		{
			System.out.print("ID Utilisateur:");
			idU= sc.nextLine();

			System.out.print("ID Competence:");
			idC= sc.nextLine();

			String retour=this.c.communiquer(commande+idU+"|"+idC);

			System.out.println(retour);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lol";
	}
	
	private String listDip(String[] splitMess)
	{
		String mess="lol", id;
		Scanner sc= new Scanner(System.in);		


		String commande="LIST_DIP|0";

		try 
		{
			String[] retour=this.c.communiquer(commande).split("\\|");
			int c=0;
			for(String s: retour)
			{
				String[] s1= s.split (";");
				for(String s2: s1)
				{
					System.out.print(s2+" ");
				}
				System.out.println("");
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lol";
	}
	
//MESSAGERIE DIFFEREE --------------------------------------------------------------------------------------
	private String ecrireMail(String[] splitMess)
	{
		Scanner sc= new Scanner(System.in);		
		String commande=proto.getEcrireMail()+"|", idDest, message;
		
		try 
		{
			System.out.print("ID Destinataire:");
			idDest= sc.nextLine();
			
			System.out.print("Message:");
			message= sc.nextLine();
			
			commande+="1"+"|"+idDest+"|"+message;
			String retour= this.c.communiquer(commande);
			
			System.out.println(retour);
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		return "lol";
	}
	
	private String releverMessages(String[] splitMess)
	{		
		String commande=proto.getReleverMessages();
		
		try 
		{	
			commande+="|2";
			
			String retour[]= this.c.communiquer(commande).split("\\|");
			
			if(Integer.parseInt(retour[0])==200)
			{
				for(String s: retour)
				{
					String[] s1= s.split (";");
					for(String s2: s1)
					{
						System.out.print(s2+" ");
					}
					System.out.println("");
				}
			}
			else
			{
				throw new Exception(retour[1]);
			}
			
			System.out.println(retour);
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return "lol relever messages";
	}
	private String lireMessage(String[] splitMess)
	{
		Scanner sc= new Scanner(System.in);
		String commande=proto.getLireMessage(), idMail=null;
		
		try 
		{	
			System.out.print("ID Message:");
			idMail= sc.nextLine();
			
			commande+="|2|"+idMail;
			
			String retour[]= this.c.communiquer(commande).split("\\|");
			
			if(Integer.parseInt(retour[0])==200)
			{
				for(String s: retour)
				{
					String[] s1= s.split (";");
					for(String s2: s1)
					{
						System.out.print(s2+" ");
					}
					System.out.println("");
				}
			}
			else
			{
				throw new Exception(retour[1]);
			}
			
			System.out.println(retour);
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return "lol relever messages";
	}
//MESSAGERIE INSTATANNEE --------------------------------------------------------------------------------------
	private String listUserCo(String[] splitMess)
	{
		String commande= new String(proto.getListUserCo());
		try 
		{	
			String retour[]= this.c.communiquer(commande).split("\\|");
			
			if(Integer.parseInt(retour[0])==200)
			{
				for(String s: retour)
				{
					String[] s1= s.split (";");
					for(String s2: s1)
					{
						System.out.print(s2+" ");
					}
					System.out.println("");
				}
			}
			else
			{
				throw new Exception(retour[1]);
			}
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return "fin listUserCo";
	}
	
	private String passerEnEcoute(String[] splitMess)
	{
		String commande= new String(proto.getPasserEnEcoute()+"|0|20");
		try 
		{	
			String retour[]= this.c.communiquer(commande).split("\\|");
			
			if(Integer.parseInt(retour[0])==200)
			{	
			 String ip=retour[1].split(";")[0];
			 String port=retour[1].split(";")[1];
			 System.out.println(retour[0]);
			 System.out.println("IP LOCALE: "+ip+" PORT: "+port);
			 
			 //A PARTIR D'ICI ON GERE LE SOCKET SERVEUR MESSAGERIE
			 Serveur serveur= new Serveur(Integer.parseInt(port));
			 ServiceServerMessagerie client= serveur.connectClientMessagerie();
			 
			 String parole;
			 Scanner sc= new Scanner(System.in);
			 
			 do{
				
				System.out.print("Dire: ");
				parole= sc.nextLine(); 
				
				client.envoyer(parole);
				//Quitter si on a envie
				if (parole.equals("q")|| parole==null || parole.equals(null)) break;
			 }while (true);
			 
			 client.quit();
			 client.stop();
			}
			else
			{
				throw new Exception(retour[1]);
			}
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return "fin passerEnEcoute";
	}
	
	private String parler(String[] splitMess)
	{
		
		try 
		{
			String ip, port, parole;
			Scanner sc= new Scanner(System.in);
			System.out.print("IP: ");
			ip= sc.nextLine();
			
			System.out.print("PORT: ");
			port= sc.nextLine();
			Client client= new Client(ip, Integer.parseInt(port));
			
			//Lancer la reception auto
			//UNIQUEMENT SI RECEVOIR DESACTIVE PLUS BAS
			client.start();
			do
			{
				//Scanner ce qu'on veut dire
				System.out.print("Dire: ");
				parole= sc.nextLine();
				
				//Envoyer
				client.envoyer(parole);

				//Quitter si on a envie
				if (parole.equals("q")) break;
//				//Recevoir
//				System.out.println(client.recevoir());				
			}while(true);
			//Fermer le socket
			client.fermer();
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return "finParler";
	}
}
