import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PaireChaineEntier {
    private String chaine;
    private int entier;

    public PaireChaineEntier(String chaine, int entier) {
        this.chaine = chaine;
        this.entier = entier;
    }

    public int getEntier() {
        return entier;
    }

    public String getChaine() {
        return chaine;
    }
}
