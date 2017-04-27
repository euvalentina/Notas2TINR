package br.com.fiap.notas;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.fiap.notas.util.CloudantRequestInterface;
import br.com.fiap.notas.util.CloudantResponseNota;
import br.com.fiap.notas.util.DataAdapter;
import br.com.fiap.notas.util.Row;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotasCardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Row> rows;

    //Adapter criado por nós para trabalhar com o Card e a RecyclerView
    private DataAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_card);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        iniciarViews();
        super.onResume();
    }

    private void iniciarViews() {
        //Cria uma referência para a nossa RecyclerView no
        // layout da Activity NotasCardActivity
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_notas);
        //Usamos para melhorar a performance deixando o tamanho fixo
        recyclerView.setHasFixedSize(true);
        //Usamos um linear layout manager
        //exibir itens em uma lista de rolagem vertical
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadJASON();
    }


    private void loadJASON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6d8665a8-8352-4d10-aa54-6ddcfd9f4daf-bluemix.cloudant.com/fiap-notas/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CloudantRequestInterface api = retrofit.create(CloudantRequestInterface.class);

        api.getAllJSON().enqueue(new Callback<CloudantResponseNota>() {

            @Override
            public void onResponse(Call<CloudantResponseNota> call, Response<CloudantResponseNota> response) {

                CloudantResponseNota json = response.body();

                rows = new ArrayList<>(Arrays.asList(json.getRows()));

                for (Row item : rows) {
                    Log.i(getString(br.com.fiap.notas.R.string.nota), item.getDoc().toString());
                }

                //Instanciamos o nosso adapter e passamos os dados vindos do Cloudant
                adapter = new DataAdapter(rows);
                //Setamos o nosso RecyclerView com o Adapter populado com os dados do Cloudant
                //Aqui a tela é carregada
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<CloudantResponseNota> call, Throwable t) {

                Log.d("Error", t.getMessage());

            }
        });
    }


    public void voltar(View v) {
        finish();
    }

    public void novaNota(View v) {
        Intent intentChamaCadastraNota = new Intent(this, CadastraNota.class);
        intentChamaCadastraNota.putExtra("docId", rows.size());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NotasCard Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
