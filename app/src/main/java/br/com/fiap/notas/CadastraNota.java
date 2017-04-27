package br.com.fiap.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import br.com.fiap.notas.entity.Doc;

public class CadastraNota extends AppCompatActivity {
    EditText Titulo, Conteudo;
    Spinner spAssunto;
    int docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_nota);

        Titulo = (EditText) findViewById(R.layout.Titulo);
        Conteudo = (EditText) findViewById(R.layout.Conteudo);
        spAssunto = (Spinner) findViewById(R.layout.Assunto);
        docId = getIntent().getExtras().getInt("docId");
    }

    //ctrl o digitar OncreateOptionsMenu
    // metodo que cria o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // cria um objeto para criar um xml que criamos com um menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastra, menu);

        return super.onCreateOptionsMenu(menu);


    }

    //metodo chamado quando o usuario seleciona um item do menu inflater
    //item recebido como parametro será o menu selecionado pelo usuario
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        Switch(item.getItemId()) {
        case R.id.menu_cadastra_ok:
            Doc doc = new Doc();
            doc.set_id(String.valueOf(docId + 1));
            doc.setTitulo(Titulo.getText().toString());
            doc.setConteudo(Conteudo.getText().toString());
            doc.setAssunto(spAssunto.getSelectedItem().toString());


    }
    return super.onOptionsItemSeleceted(item);
}





}
