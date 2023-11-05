package com.example.moneybank;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.moneybank.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;


public class MainScreen extends AppCompatActivity {

    private double solde = 420.69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        TextView affichageSolde = findViewById(R.id.solde);

        affichageSolde.setText(String.format("%.2f €", solde));

        TextView textViewDescription = findViewById(R.id.texteBas);
        TextView textViewMontant = findViewById(R.id.montant);


        BottomNavigationView bottomNavigationView = findViewById(R.id.botNavView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.virements) {
                    // Ouvrir le premier popup pour la saisie de la somme à virer
                    ouvrirPopupSomme();
                    return true;
                } else if(item.getItemId() == R.id.accueil){
                    return true;
                } else if(item.getItemId() == R.id.vide){
                    return true;
                }else if(item.getItemId() == R.id.carte){
                    ouvrirPopupInfo();
                    return true;
                }else if(item.getItemId() == R.id.rib){
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    private void ouvrirPopupSomme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_somme, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Récupérer les éléments du premier popup
        EditText editTextSomme = view.findViewById(R.id.editTextSomme);
        Button buttonSuivant = view.findViewById(R.id.buttonSuivant);

        // Définir le comportement du bouton "Suivant"
        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer la somme saisie
                double somme = Double.parseDouble(editTextSomme.getText().toString());

                // Fermer le premier popup
                dialog.dismiss();

                // Ouvrir le deuxième popup pour la saisie du destinataire
                ouvrirPopupDestinataire(somme);
            }
        });
    }


    private void ouvrirPopupDestinataire(double somme) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.ppopum_destinataire, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Récupérer les éléments du deuxième popup
        EditText editTextDestinataire = view.findViewById(R.id.editTextDestinataire);
        Button buttonValider = view.findViewById(R.id.buttonValider);

        LinearLayout operationsContainer = findViewById(R.id.operationsContainer);

        // Définir le comportement du bouton "Valider"
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le destinataire saisi
                String destinataire = editTextDestinataire.getText().toString();

                // Fermer le deuxième popup
                dialog.dismiss();
                majSolde(somme);
                String descriptionOperation = "Virement à " + destinataire; // Créez la description de l'opération
                // ajouterOperation(descriptionOperation, somme);
                ajouterOperation(destinataire, somme, operationsContainer);

                // Effectuer les actions nécessaires avec la somme et le destinataire
                // Par exemple, enregistrez-les dans la base de données Firebase
                // ...

            }
        });

    }

    private void ouvrirPopupInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View popupinfos = inflater.inflate(R.layout.popup_infos, null);
        builder.setView(popupinfos);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonFermer = popupinfos.findViewById(R.id.boutonFermer);
        buttonFermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }


    public void majSolde(double montant) {
        solde -= montant;
        TextView affSolde = findViewById(R.id.solde);
        affSolde.setText(String.format("%.2f €", solde));
    }

    private void ajouterOperation(String description, double montant, LinearLayout container) {
        // Créer un nouveau TextView pour représenter l'opération
        View operationCardView = getLayoutInflater().inflate(R.layout.elements_activites, null);

        // Récupérer les éléments de la carte
        TextView textViewHaut = operationCardView.findViewById(R.id.textHaut);
        TextView textViewBas = operationCardView.findViewById(R.id.texteBas);
        TextView textViewMontant = operationCardView.findViewById(R.id.montant);

        // Mettre à jour les éléments de l'opération dans la carte
        textViewHaut.setText("Virement au Destinataire"); // Mettez ici votre description
        textViewBas.setText(description);
        textViewMontant.setText(String.format("%.2f €", montant));

        // Ajouter la carte au conteneur des opérations
        LinearLayout operationsContainer = findViewById(R.id.operationsContainer);
        operationsContainer.addView(operationCardView, 0);
    }
}
