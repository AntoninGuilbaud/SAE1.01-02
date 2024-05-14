import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int i = 0;
        while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
            i++;
        }
        if (i == listePaires.size()) {
            return -1;
        } else {
            return i;
        }
    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        // Retourne l’entier associé à la chaîne de caractères chaine dans listePaires Si la chaîne n'est pas présente, retourne 0
        int i = 0;
        // Recherche linéaire de la chaîne dans la liste
        while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
            i++;
        }
        // Vérification si la chaîne n'a pas été trouvée
        if (i == listePaires.size()) {
            return 0;
        } else {
            // Retourne l'entier associé à la chaîne trouvée
            return listePaires.get(i).getEntier();
        }
    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        // Retourne la chaîne associée au plus grand entier de listePaires

        // Initialisation d'une paire chaîne-entier avec une chaîne vide et un entier à zéro
        PaireChaineEntier chaineMax = new PaireChaineEntier("", 0);

        // Parcours de la liste de paires chaîne-entier
        for (int i = 0; i < listePaires.size(); i++) {
            // Vérification si l'entier de la paire actuelle est supérieur à l'entier maximal
            if (listePaires.get(i).getEntier() > chaineMax.getEntier()) {
                // Mise à jour de la paire chaîne-entier maximale
                chaineMax = listePaires.get(i);
            }
        }

        // Vérification si l'entier maximal est égal à zéro
        if (chaineMax.getEntier() == 0) {
            return "TEST";
        } else {
            // Retourne la chaîne associée à l'entier maximal
            return chaineMax.getChaine();
        }
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        int somme = 0;
        for (int i = 0; i < listePaires.size(); i++) {
            PaireChaineEntier paire = listePaires.get(i);
            somme += paire.getEntier();
        }
        return (float) somme / listePaires.size();
    }
}
