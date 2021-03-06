﻿package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


import common.Utilisateur;

public class DBUtilisateur extends MySql
{
	public static boolean insererUtilisateur(Utilisateur util)
	{
		try
		{
			// Connexion a la base de donnees avec la classe MySQL
			Connection db= MySql.connexion();
			PreparedStatement pstmt = db.prepareStatement("INSERT INTO Utilisateur (Mail,Nom,Prenom,Mdp,Admin,vuMail, vuComp, vuDip) VALUES (?,?,?,?,?,?,?,?);");

			// Parametres
			pstmt.setString(1,util.getMail());
			pstmt.setString(2, util.getNom());
			pstmt.setString(3, util.getPrenom());
			pstmt.setString(4, util.getMotDePasse());
			pstmt.setInt(5, util.getSatuts());
			pstmt.setInt(6, util.getVuMail());
			pstmt.setInt(7, util.getVuComp());
			pstmt.setInt(8, util.getVuDip());
			
			pstmt.executeUpdate();
			db.close();
			
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean modifierUtilisateur(Utilisateur util)
	{
		try
		{
			// Connexion a la base de donnees avec la classe MySQL
			Connection db= MySql.connexion();
			PreparedStatement pstmt = db.prepareStatement("UPDATE Utilisateur SET Mail=?, Nom=?, Prenom=?, Mdp=?, Admin=?, vuMail=?, vuComp=?, vuDip=?  WHERE IdU=?;");

			// Parametres
			pstmt.setString(1,util.getMail());
			pstmt.setString(2, util.getNom());
			pstmt.setString(3, util.getPrenom());
			pstmt.setString(4, util.getMotDePasse());
			pstmt.setInt(5, util.getSatuts());
			pstmt.setInt(6, util.getVuMail());
			pstmt.setInt(7, util.getVuComp());
			pstmt.setInt(8, util.getVuDip());
			pstmt.setInt(9, util.getId());
			
			pstmt.executeUpdate();
			db.close();
			
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Cree une hashMap locaux correspondant a la table locaux
	 * 
	 * @return
	 */
	public static ArrayList<Utilisateur> lireUtilisateurs()
	{
		ArrayList<Utilisateur> utilisateurs= new ArrayList<Utilisateur>();
		
		try
		{
			Connection db= connexion();
			Statement s = null;
		    ResultSet r = null; 
			/* Création de l'objet gérant les requêtes */
	        s = db.createStatement();
	        /* Exécution d'une requête de lecture */
	        r = s.executeQuery( "SELECT * FROM Utilisateur;");
	 
	        /* Récupération des données du résultat de la requête de lecture */
	        while ( r.next() ) 
	        {
	        	utilisateurs.add(new Utilisateur(r.getInt("IdU"), r.getString("Nom"), r.getString("Prenom"), r.getString("Mail"), r.getString("Mdp"), r.getInt("Admin"), r.getInt("vuMail"),r.getInt("vuComp"), r.getInt("vuDip")));
	        } 
	        r.close();
	        s.close();
	        db.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return utilisateurs;
	}
	public static Utilisateur lireUtilisateur(int id)
	{
		ArrayList<Utilisateur> utilisateurs = lireUtilisateurs();
		Utilisateur u=new Utilisateur(0, "", "", "", "", 0);
		
		for (Utilisateur util : utilisateurs)
		{
			if(util.getId()==id)
			{
				u=util;
			}
		}
		
		return u;
	}
	
	public static Utilisateur checkConnexion (Utilisateur pU)
	{
		
		ArrayList<Utilisateur> utilisateurs = lireUtilisateurs();
		Utilisateur u=null;
		
		for (Utilisateur util : utilisateurs)
		{
			if(util.getMail().equals(pU.getMail()) && util.getMotDePasse().equals(pU.getMotDePasse()))
			{
				u=util;
			}
		}
		return u;
	}
}

