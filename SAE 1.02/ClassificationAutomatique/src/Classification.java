import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Classification {

    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }


    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        long startTime = System.currentTimeMillis();
        try {
            FileWriter file = new FileWriter(nomFichier);
            ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
            // pour chacune des depeches
            for (int i = 0; i < depeches.size(); i++) {
                file.write(depeches.get(i).getId() + ":");
                ArrayList<PaireChaineEntier> scores = new ArrayList<>();

                for (int j = 0; j < categories.size(); j++) {
                    scores.add(new PaireChaineEntier(categories.get(j).getNom(), categories.get(j).score(depeches.get(i))));
                }

                // Trouver la catégorie avec le meilleur score

                String categorieMeilleurScore = UtilitairePaireChaineEntier.chaineMax(scores);
                file.write(categorieMeilleurScore + "\n");
                resultat.add(new PaireChaineEntier(categorieMeilleurScore, i));
            }
            // calcul du % de réussite par catégorie

            int j = 0;
            // Parcours de toutes les catégories
            for (int i = 0; i < categories.size(); i++) {
                int bonneReponse = 0;
                int taille = 0;
                // Tant que nous ne sommes pas à la fin de la liste de dépêches et que la catégorie de la dépeche correspond à la catégorie actuelle
                while (j < depeches.size() && categories.get(i).getNom().equals(depeches.get(j).getCategorie())) {
                    // Si la catégorie prédite est égale à la catégorie réelle de la dépeche incrémenter le nombre de bonnes réponses
                    if (resultat.get(j).getChaine().equals(depeches.get(j).getCategorie())) {
                        bonneReponse++;
                    }
                    taille++;
                    j++;
                }
                file.write(categories.get(i).getNom() + " : " + ((bonneReponse * 100) / taille) + "% \n");
            }

            // caclule du % de réussite moyenne
            int bonneReponse = 0;
            for (int i = 0; i < resultat.size(); i++) {
                if (depeches.get(i).getCategorie().equals(resultat.get(i).getChaine())) {
                    bonneReponse += 1;
                }
            }
            file.write("MOYENNE : " + ((bonneReponse * 100) / 500) + "%"); // 500 ou resultat.size()
            file.close();
            long endTime = System.currentTimeMillis();
            System.out.print("La Génération du classement à durée : " + (endTime - startTime) + "ms ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        // Retourne une ArrayList<PaireChaineEntier> contenant tous les mots présents dans au moins une dépêche de la catégorie categorie
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        int i = 0;
        // Trouver la première dépêche de la catégorie
        while (i < depeches.size() && depeches.get(i).getCategorie().compareTo(categorie) != 0) {
            i++;
        }
        // Parcourir toutes les dépêches de la catégorie
        while (i < depeches.size() && depeches.get(i).getCategorie().compareTo(categorie) == 0) {
            Depeche depeche = depeches.get(i);
            int j = 0;
            // Parcourir tous les mots de la dépêche
            while (j < depeche.getMots().size()) {
                int k = 0;
                // Vérifier si le mot est déjà dans la liste resultat
                while (k < resultat.size() && resultat.get(k).getChaine().compareTo(depeche.getMots().get(j)) != 0) {
                    k++;
                }
                // Ajouter le mot à la liste resultat s'il n'est pas déjà présent et si il est contient plus de 3 caractères
                if (k == resultat.size() && depeche.getMots().get(j).length() > 3) {
                    resultat.add(new PaireChaineEntier(depeche.getMots().get(j), 0));
                }
                j++;
            }
            i++;
        }
        return triFusion(resultat, 0, resultat.size() - 1);
    }

//                              ---------- calculScores sans recherche dichotomique ----------

//    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
//        // Met à jour les scores des mots présents dans dictionnaire
//        // Lorsqu'un mot présent dans dictionnaire apparaît dans une dépêche de depeches, son score est :
//        // - décrémenté si la dépêche n'est pas dans la catégorie categorie
//        // - incrémenté si la dépêche est dans la catégorie categorie
//
//        // Parcourir tous les mots du dictionnaire
//
//        for (int i = 0; i < dictionnaire.size(); i++) {
//            String mot = dictionnaire.get(i).getChaine();
//            // Parcourir toutes les dépêches
//            for (int j = 0; j < depeches.size(); j++) {
//                // Vérifier si le mot est présent dans la dépêche et si la catégorie correspond
//                if (depeches.get(j).getMots().contains(mot) && depeches.get(j).getCategorie().equals(categorie)) {
//                    // Incrémenter le score si le mot est présent dans la bonne catégorie
//                    dictionnaire.set(i, new PaireChaineEntier(mot, dictionnaire.get(i).getEntier() + 1));
//                } else if (depeches.get(j).getMots().contains(mot) && !depeches.get(j).getCategorie().equals(categorie)) {
//                    // Décrémenter le score si le mot est présent dans une catégorie différente
//                    dictionnaire.set(i, new PaireChaineEntier(mot, dictionnaire.get(i).getEntier() - 1));
//                }
//            }
//        }
//    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        // Met à jour les scores des mots présents dans dictionnaire
        // Lorsqu'un mot présent dans dictionnaire apparaît dans une dépêche de depeches, son score est :
        // - décrémenté si la dépêche n'est pas dans la catégorie categorie
        // - incrémenté si la dépêche est dans la catégorie categorie

        // Trier le dictionnaire par chaîne pour utiliser la recherche dichotomique

        triFusion(dictionnaire, 0, dictionnaire.size() - 1);

        // Parcourir toutes les dépêches
        for (int j = 0; j < depeches.size(); j++) {
            // Parcourir tous les mots du dictionnaire
            for (int i = 0; i < dictionnaire.size(); i++) {
                String mot = dictionnaire.get(i).getChaine();

                // Utiliser la recherche dichotomique pour trouver le mot dans le dictionnaire
                int indexMot = rechercheDichotomique(dictionnaire, mot);

                // Vérifier si le mot est présent dans la dépêche et si la catégorie correspond
                if (indexMot != -1 && depeches.get(j).getMots().contains(mot) && depeches.get(j).getCategorie().equals(categorie)) {
                    // Incrémenter le score si le mot est présent dans la bonne catégorie
                    dictionnaire.set(indexMot, new PaireChaineEntier(mot, dictionnaire.get(indexMot).getEntier() + 1));
                } else if (indexMot != -1 && depeches.get(j).getMots().contains(mot) && !depeches.get(j).getCategorie().equals(categorie)) {
                    // Décrémenter le score si le mot est présent dans une catégorie différente
                    dictionnaire.set(indexMot, new PaireChaineEntier(mot, dictionnaire.get(indexMot).getEntier() - 1));
                }
            }
        }
    }

    // Méthode de recherche dichotomique
    public static int rechercheDichotomique(ArrayList<PaireChaineEntier> dictionnaire, String mot) {
        int inf = 0;
        int sup = dictionnaire.size() - 1;

        while (inf <= sup) {
            int milieu = (inf + sup) / 2;
            int comparaison = dictionnaire.get(milieu).getChaine().compareTo(mot);

            if (comparaison == 0) {
                return milieu; // Mot trouvé, retourner l'index
            } else if (comparaison < 0) {
                inf = milieu + 1;
            } else {
                sup = milieu - 1;
            }
        }
        return -1; // Mot non trouvé
    }

    public static int poidsPourScore(int score) {
        if (score >= 2) {
            return 3;
        } else if (score > 0) {
            return 2;
        } else return 1;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        // Crée pour la catégorie categorie le fichier lexique de nom nomFichier à partir du vecteur de dépêches depeches
        long startTime = System.currentTimeMillis();
        try {
            ArrayList<PaireChaineEntier> lexique = initDico(depeches, categorie);
            calculScores(depeches, categorie, lexique);
            FileWriter file = new FileWriter(nomFichier);
            // Parcourir le dictionnaire et écrire chaque paire chaine-entier dans le fichier
            for (int i = 0; i < lexique.size(); i++) {
                // Écrire dans le fichier la chaine et le score et passer à la ligne suivante
                file.write(lexique.get(i).getChaine() + ":" + poidsPourScore(lexique.get(i).getEntier()) + "\n");
            }
            file.close();
            System.out.print("Fichier créé avec succès : " + nomFichier);
            long endTime = System.currentTimeMillis();
            System.out.println(", La Génération à durée : " + (endTime - startTime) + "ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PaireChaineEntier> triFusion(ArrayList<PaireChaineEntier> dico, int inf, int sup) {
        if (inf < sup) {
            int milieu = (inf + sup) / 2;

            // Tri des parties gauche et droite
            triFusion(dico, inf, milieu);
            triFusion(dico, milieu + 1, sup);

            // Fusion des parties triées
            fusion(dico, inf, milieu, sup);
        }
        return dico;
    }

    private static void fusion(ArrayList<PaireChaineEntier> dico, int inf, int milieu, int sup) {
        int n1 = milieu - inf + 1;
        int n2 = sup - milieu;

        // Création de tableaux temporaires
        ArrayList<PaireChaineEntier> gauche = new ArrayList<>(n1);
        ArrayList<PaireChaineEntier> droite = new ArrayList<>(n2);

        // Copie des données dans les tableaux temporaires
        for (int i = 0; i < n1; ++i)
            gauche.add(dico.get(inf + i));
        for (int j = 0; j < n2; ++j)
            droite.add(dico.get(milieu + 1 + j));

        // Fusion des tableaux temporaires
        int i = 0, j = 0;
        int k = inf;

        while (i < n1 && j < n2) {
            if (gauche.get(i).getChaine().compareTo(droite.get(j).getChaine()) <= 0) {
                dico.set(k, gauche.get(i));
                i++;
            } else {
                dico.set(k, droite.get(j));
                j++;
            }
            k++;
        }

        // Copie des éléments restants de gauche et droite, s'il y en a
        while (i < n1) {
            dico.set(k, gauche.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            dico.set(k, droite.get(j));
            j++;
            k++;
        }
    }


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

//        -------------------- Chargement des dépêches en mémoire --------------------

        ArrayList<Depeche> depeches = lectureDepeches("test.txt");

//        -----------------------------------------------------------------------------

//        for (int i = 0; i < depeches.size(); i++) {
//           depeches.get(i).afficher();
//        }

//       Categorie categorie = new Categorie("Culture");
//       categorie.initLexique("Lexique_Culture.txt");
//        categorie.afficherLexique();
//
//        Scanner lecteur = new Scanner(System.in);
//        System.out.println("Saisir un mot");
//        String unMot = lecteur.nextLine();
//        System.out.println(UtilitairePaireChaineEntier.entierPourChaine(categorie.getLexique(),unMot));

//        Depeche depecheExemple = new Depeche("1", "2022-01-01", "CULTURE", "décés ceci est un test pièces compositeur");
//        int score = categorie.score(depecheExemple);
//        System.out.println("Score pour la depeche n°"+ depecheExemple.getId() + " : " + score);

        ArrayList<Categorie> categoriesfinal = new ArrayList<>();
        categoriesfinal.add(new Categorie("ENVIRONNEMENT-SCIENCES"));
        categoriesfinal.add(new Categorie("CULTURE"));
        categoriesfinal.add(new Categorie("ECONOMIE"));
        categoriesfinal.add(new Categorie("POLITIQUE"));
        categoriesfinal.add(new Categorie("SPORTS"));

//       ---------- Initialisation des lexiques des 5 catégories à partir des fichiers respectifs ----------

//              categoriesfinal.get(0).initLexique("Lexique_Science.txt");
//              categoriesfinal.get(1).initLexique("Lexique_Culture.txt");
//              categoriesfinal.get(2).initLexique("Lexique_Economie.txt");
//              categoriesfinal.get(3).initLexique("Lexique_Politique.txt");
//              categoriesfinal.get(4).initLexique("Lexique_Sport.txt");

        categoriesfinal.get(0).initLexique("Lexique_ENVIRONNEMENT-SCIENCES_Auto.txt");
        categoriesfinal.get(1).initLexique("Lexique_CULTURE_Auto.txt");
        categoriesfinal.get(2).initLexique("Lexique_ECONOMIE_Auto.txt");
        categoriesfinal.get(3).initLexique("Lexique_POLITIQUE_Auto.txt");
        categoriesfinal.get(4).initLexique("Lexique_SPORTS_Auto.txt");


        generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "Lexique_ENVIRONNEMENT-SCIENCES_Auto.txt");
        generationLexique(depeches, "CULTURE", "Lexique_CULTURE_Auto.txt");
        generationLexique(depeches, "ECONOMIE", "Lexique_ECONOMIE_Auto.txt");
        generationLexique(depeches, "POLITIQUE", "Lexique_POLITIQUE_Auto.txt");
        generationLexique(depeches, "SPORTS", "Lexique_SPORTS_Auto.txt");

        Classification.classementDepeches(depeches, categoriesfinal, "resultats_classement.txt");
        System.out.println(", Résultats enregistrés dans le fichier resultats_classement.txt");

        long endTime = System.currentTimeMillis();
        System.out.println("l'éxécution totale de Classification est " + (endTime - startTime) + "ms");

    }
}

