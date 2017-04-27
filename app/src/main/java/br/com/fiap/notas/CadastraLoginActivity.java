package br.com.fiap.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.HashMap;

import br.com.fiap.notas.util.ArquivoDB;

public class CadastraLoginActivity extends AppCompatActivity {

    private EditText etNome, etSobrenome, etNascimento, etEmail, etSenha;
    private RadioGroup rgSexo;
    private ArquivoDB arquivoDB;

    private final String ARQ = "dados.txt";
    private final String SP = "dados";
    private HashMap<String, String> mapDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_login);

        etNome = (EditText) findViewById(R.id.etNome);
        etSobrenome = (EditText) findViewById(R.id.etSobrenome);
        etNascimento = (EditText) findViewById(R.id.etNome);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etSenha = (EditText) findViewById(R.id.etSenha);
        rgSexo = (RadioGroup) findViewById(R.id.rgSexo);

        arquivoDB = new ArquivoDB();
        mapDados = new HashMap<>();
    }

    private boolean capturaDadosDaTela(){
        String nome, sobrenome, nascimento, email, senha, sexo;
        boolean dadosOk = false;

        nome = etNome.getText().toString();
        sobrenome = etSobrenome.getText().toString();
        nascimento = etNascimento.getText().toString();
        email = etEmail.getText().toString();
        senha = etSenha.getText().toString();

        // verifica qual radio dentro do group está checkado
        int sexoId = rgSexo.getCheckedRadioButtonId();
        RadioButton rbSexo = (RadioButton) findViewById(sexoId);

        // (sexoId!= -1) quando nenhum radioButton for selecionado o método getChecked retornará -1
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(senha) &&
                !TextUtils.isEmpty(nome) &&
                !TextUtils.isEmpty(sobrenome) &&
                !TextUtils.isEmpty(nascimento) &&
                (sexoId!= -1))
        {
            dadosOk =true;

            sexo = rbSexo.getText().toString();
            mapDados.put("usuario", email);
            mapDados.put("senha", senha);
            mapDados.put("nome", nome);
            mapDados.put("sobrenome", sobrenome);
            mapDados.put("nascimento", nascimento);
            mapDados.put("sexo", sexo);

        } else {
            Toast.makeText(this, R.string.dados_conta_ok, Toast.LENGTH_SHORT).show();
        }


        return dadosOk;
    }

    public void gravarChaves(){
        if (capturaDadosDaTela()) {
            arquivoDB.gravarChaves(this, SP, mapDados);
            Toast.makeText(this, R.string.cadastro_ok, Toast.LENGTH_SHORT).show();
        }
    }

    /*public boolean verificarChaves(){
        if (arquivoDB.verificarChave(this, SP, "usuario") && arquivoDB.verificarChave(this, SP, "senha")) {
            Toast.makeText(this, R.string.dados_login_ok, Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(this, R.string.dados_login_ok, Toast.LENGTH_SHORT).show();
            return false;
        }
    }*/

    public boolean verificarChaves(View v) {
        if (capturaDadosDaTela()) {
            arquivoDB.gravarChaves(this, SP, mapDados);
            Toast.makeText(this, R.string.dados_verificados, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void carregarChaves(View v){
        if(verificarChaves(v)){
            etNome.setText(arquivoDB.retornarValor(this, SP, "nome"));
            etSobrenome.setText(arquivoDB.retornarValor(this, SP, "sobrenome"));
            etNascimento.setText(arquivoDB.retornarValor(this, SP, "nascimento"));
            etNome.setText(arquivoDB.retornarValor(this, SP, "usuario"));
            etSenha.setText(arquivoDB.retornarValor(this, SP, "senha"));

            String sexo = arquivoDB.retornarValor(this, SP, "sexo");
            if (sexo.equals(getString(R.string.feminino))){
                rgSexo.check(R.id.rbFeminino);
            }else{
                rgSexo.check(R.id.rbMasculino);
            }
        }
    }

    // metodo que gravva o arquivo txt
    public void gravarArquivo(View v){
        if (capturaDadosDaTela()){
            try {
                arquivoDB.gravarArquivo(this, ARQ, mapDados.toString());
                Toast.makeText(this, R.string.arquivo_gravado, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.gravar_arquivo_nok, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void lerArquivo(View v){
        try {
            String txt = arquivoDB.lerArquivo(this,SP);
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.ler_arquivo_hok, Toast.LENGTH_SHORT).show();
        }

    }

    public void excluirArquivo(View v){
        try {
           arquivoDB.lerArquivo(this, ARQ);
            Toast.makeText(this, R.string.arquivo_excluido, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.arquivo_nao_excluido, Toast.LENGTH_SHORT).show();
        }
    }

    public void voltar(View v){finish();}

}
