package com.example.moneybank;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.moneybank.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.operation.Operation;

import org.w3c.dom.Text;


public class MainScreen extends AppCompatActivity {

    protected double soldeUser = 0;
    protected double soldeCredit = 0;
    private View view;
    private boolean boutonCliqueOuNon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference refUser;
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://moneybank-3c887-default-rtdb.europe-west1.firebasedatabase.app/");
        refUser = database.getReference("soldes/user1");

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    soldeUser = dataSnapshot.child("solde").getValue(Double.class);
                    // Mettez à jour votre interface utilisateur ici avec le solde
                    TextView affSolde = findViewById(R.id.solde);
                    affSolde.setText(String.format("%.2f €", soldeUser));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "onCancelled: " + databaseError.getMessage()); // Ajoutez cette ligne pour déboguer les erreurs
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        View vue = findViewById(R.id.view);
        TextView affichageSolde = findViewById(R.id.solde);
        TextView block = findViewById(R.id.blocked);
        FloatingActionButton boutonFloat = findViewById(R.id.floatingActionButton);

        affichageSolde.setText(String.format("%.2f €", soldeUser));

        TextView textViewDescription = findViewById(R.id.texteBas);
        TextView textViewMontant = findViewById(R.id.montant);


        BottomNavigationView bottomNavigationView = findViewById(R.id.botNavView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (boutonCliqueOuNon) {
                    return true;
                }

                if (item.getItemId() == R.id.virements) {
                    // Ouvrir le premier popup pour la saisie de la somme à virer
                    ouvrirPopupSomme();
                    return true;
                } else if (item.getItemId() == R.id.accueil) {
                    return true;
                } else if (item.getItemId() == R.id.carte) {
                    ouvrirPopupInfo();
                    return true;
                } else if (item.getItemId() == R.id.rib) {
                    vue.setBackgroundColor(ContextCompat.getColor(MainScreen.this, R.color.rouge));
                    block.setText("Carte bloquée. Contactez la Banque");
                    boutonCliqueOuNon = true;
                    return true;
                } else {
                    return false;
                }
            }
        });
        boutonFloat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ouvrirPopupSomme2();
                }
            });
    }

    private void ouvrirPopupSomme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_somme, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextSomme = view.findViewById(R.id.editTextSomme);
        Button buttonSuivant = view.findViewById(R.id.buttonSuivant);


        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double somme = Double.parseDouble(editTextSomme.getText().toString());


                dialog.dismiss();


                ouvrirPopupDestinataire(somme);
            }
        });
    }

    private void ouvrirPopupSomme2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popum_somme2, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextSomme = view.findViewById(R.id.editTextSomme);
        Button buttonSuivant = view.findViewById(R.id.buttonSuivant);


        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double somme = Double.parseDouble(editTextSomme.getText().toString());

                dialog.dismiss();
                ouvrirPopupDestinataire2(somme);
            }
        });
    }


    private void ouvrirPopupDestinataire(double somme) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.ppopum_destinataire, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextDestinataire = view.findViewById(R.id.editTextDestinataire);
        Button buttonValider = view.findViewById(R.id.buttonValider);

        LinearLayout operationsContainer = findViewById(R.id.operationsContainer);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destinataire = editTextDestinataire.getText().toString();

                DatabaseReference refUser;
                DatabaseReference refCredit;
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://moneybank-3c887-default-rtdb.europe-west1.firebasedatabase.app/");
                refUser = database.getReference("soldes/user1");
                refCredit = database.getReference("soldes/user2");

                refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double montantActuelUser = dataSnapshot.child("solde").getValue(Double.class);


                            double nouveauSoldeUser = montantActuelUser - somme;

                            refUser.child("solde").setValue(nouveauSoldeUser);

                            soldeUser = nouveauSoldeUser;

                            refCredit.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot creditDataSnapshot) {
                                    if (creditDataSnapshot.exists()) {
                                        double montantActuelCredit = creditDataSnapshot.child("solde").getValue(Double.class);


                                        double nouveauSoldeCredit = montantActuelCredit + somme;

                                        refCredit.child("solde").setValue(nouveauSoldeCredit);

                                        soldeCredit = nouveauSoldeCredit;

                                        dialog.dismiss();

                                        String descriptionOperation = "Virement effectué à " + destinataire;
                                        ajouterOperation(descriptionOperation, somme, operationsContainer);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }


    private void ouvrirPopupDestinataire2(double somme) {
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

                DatabaseReference refUser;
                DatabaseReference refCredit;
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://moneybank-3c887-default-rtdb.europe-west1.firebasedatabase.app/");
                refUser = database.getReference("soldes/user1");
                refCredit = database.getReference("soldes/user2");

                // Récupérez le montant actuel depuis Firebase pour l'utilisateur
                refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double montantActuelUser = dataSnapshot.child("solde").getValue(Double.class);

                            // Déduisez la somme
                            double nouveauSoldeUser = montantActuelUser + somme;

                            // Mettez à jour la valeur dans Firebase pour l'utilisateur
                            refUser.child("solde").setValue(nouveauSoldeUser);

                            // Mettez à jour la variable soldeUser
                            soldeUser = nouveauSoldeUser;

                            // Récupérez le montant actuel depuis Firebase pour le crédit
                            refCredit.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot creditDataSnapshot) {
                                    if (creditDataSnapshot.exists()) {
                                        double montantActuelCredit = creditDataSnapshot.child("solde").getValue(Double.class);

                                        // Ajoutez la somme au crédit
                                        double nouveauSoldeCredit = montantActuelCredit + somme;

                                        // Mettez à jour la valeur dans Firebase pour le crédit
                                        refCredit.child("solde").setValue(nouveauSoldeCredit);

                                        // Mettez à jour la variable soldeCredit
                                        soldeCredit = nouveauSoldeCredit;

                                        // Fermer le deuxième popup
                                        dialog.dismiss();

                                        String descriptionOperation = "Prêt reçu de " + destinataire; // Créez la description de l'opération
                                        ajouterOperation(descriptionOperation, somme, operationsContainer);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Gérez les erreurs ici pour refCredit
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Gérez les erreurs ici pour refUser
                    }
                });
            }
        });
    }

    private PopupWindow popupWindow;



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
        soldeUser -= montant;
        TextView affSolde = findViewById(R.id.solde);
        affSolde.setText(String.format("%.2f €", soldeUser));
    }

    public void majSolde2(double montant) {
        soldeUser += montant;
        TextView affSolde = findViewById(R.id.solde);
        affSolde.setText(String.format("%.2f €", soldeUser));
    }

    private void ajouterOperation(String description, double montant, LinearLayout container) {
        // Créer un nouveau TextView pour représenter l'opération
        View operationCardView = getLayoutInflater().inflate(R.layout.elements_activites, null);

        // Récupérer les éléments de la carte
        TextView textViewHaut = operationCardView.findViewById(R.id.textHaut);
        TextView textViewBas = operationCardView.findViewById(R.id.texteBas);
        TextView textViewMontant = operationCardView.findViewById(R.id.montant);

        // Mettre à jour les éléments de l'opération dans la carte
        textViewHaut.setText("Virement"); // Mettez ici votre description
        textViewBas.setText(description);
        textViewMontant.setText(String.format("%.2f €", montant));

        // Ajouter la carte au conteneur des opérations
        LinearLayout operationsContainer = findViewById(R.id.operationsContainer);
        operationsContainer.addView(operationCardView, 0);
    }
}
