package br.com.fapa.firebasetesteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fapa.firebasetesteapp.modelo.Pessoa;



public class MainActivity extends AppCompatActivity {


    EditText usuNome, usuEmail;
    ListView lv_Dados;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<Pessoa> listPessoas = new ArrayList<>();
    private ArrayAdapter<Pessoa> pessoaArrayAdapter;

    private Pessoa pessoaSelecionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuNome = (EditText)findViewById(R.id.usuNome);
        usuEmail = (EditText)findViewById(R.id.usuEmail);

        lv_Dados = (ListView)findViewById(R.id.lv_dados);

        //aqui é inicializado os valores do objeto Pessoa e os campos são inseridos no banco
        inicializarFirebase();

        //método responsável por exibir e atualizar os registros no app em sincronia com o BD
        eventoDatabase();

        lv_Dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoaSelecionada = (Pessoa)parent.getItemAtPosition(position);
                usuNome.setText(pessoaSelecionada.getNome().toString());
                usuEmail.setText(pessoaSelecionada.getEmail().toString());
            }
        });


    }

    //método responsável por exibir e atualizar os registros no app em sincronia com o BD
    private void eventoDatabase() {
        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {

            //nesse método ele trás todos os elementos da "tabela" pessoa
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPessoas.clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){

                    Pessoa p = objSnapshot.getValue(Pessoa.class);

                    listPessoas.add(p);

                }

                pessoaArrayAdapter = new ArrayAdapter<Pessoa>(MainActivity.this,
                        android.R.layout.simple_list_item_1, listPessoas);

                //inserindo os dados no layout do app
                lv_Dados.setAdapter(pessoaArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //aqui é inicializado os valores do objeto Pessoa e os campos são inseridos no banco
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        //salvando as informações no app além de serem salvos na nuvem também
        firebaseDatabase.setPersistenceEnabled(true);

        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){

            Pessoa p = new Pessoa();

            p.setUid(UUID.randomUUID().toString());
            p.setNome(usuNome.getText().toString());
            p.setEmail(usuEmail.getText().toString());

            databaseReference.child("Pessoa").child(p.getUid()).setValue(p);
            limparCampos();
        }else if(id == R.id.menu_atualizar){
            Pessoa p = new Pessoa();

            p.setUid(pessoaSelecionada.getUid());
            p.setNome(usuNome.getText().toString().trim());
            p.setEmail(usuEmail.getText().toString().trim());

            databaseReference.child("Pessoa").child(p.getUid()).setValue(p);
            limparCampos();

        }else if (id == R.id.menu_deletar){
            Pessoa p = new Pessoa();

            p.setUid(pessoaSelecionada.getUid());

            databaseReference.child("Pessoa").child(p.getUid()).removeValue();
            limparCampos();


        }
        return true;
    }

    private void limparCampos() {
        usuNome.setText("");
        usuEmail.setText("");
    }
}
