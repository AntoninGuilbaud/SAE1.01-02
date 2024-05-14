import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        // Création d'une nouvelle liste d'objets PaireChaineEntier pour stocker le lexique
        lexique = new ArrayList<PaireChaineEntier>();

        try {
            // Ouverture du fichier en lecture
            FileInputStream file = new FileInputStream(nomFichier);
            // Création d'un scanner pour parcourir le fichier ligne par ligne
            Scanner scanner = new Scanner(file);

            // Parcours de chaque ligne du fichier
            while (scanner.hasNextLine()) {
                // Lecture de la ligne
                String ligne = scanner.nextLine();
                // Recherche de l'index du dernier caractère ":" dans la ligne
                int indexDeuxPoints = ligne.lastIndexOf(":");
                // Extraction du mot en fonction de l'index des deux-points
                String mot = ligne.substring(0, indexDeuxPoints);
                // Extraction du poids en fonction de l'index des deux-points
                int poids = Integer.parseInt(ligne.substring(indexDeuxPoints + 1));
                // Ajout d'une nouvelle paire (mot, poids) à la liste lexique
                lexique.add(new PaireChaineEntier(mot, poids));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        //retourne l’entier correspondant au score de la depêche d pour la catégorie sur laquelle l’appel est réalisé
        int score = 0;
        for (int i = 0; i < d.getMots().size(); i++) {
            String mot = d.getMots().get(i);
            // Utilisation de entierPourChaine pour obtenir le poids du mot
            int poidsDuMot = UtilitairePaireChaineEntier.entierPourChaine(lexique, mot);
            score += poidsDuMot;
        }
        return score;
    }


    public void afficherLexique() {
        System.out.println("Lexique chargé :");
        for (int i = 0; i < lexique.size(); i++) {
            PaireChaineEntier paire = lexique.get(i);
            System.out.println(paire.getChaine() + ": " + paire.getEntier());
        }
    }
}