package com.resolve.gustavobrunoromeira.resolve_patrimonio.activity.introducao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoFirebase;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.Base64Custom;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText Nome,Email,Senha;
    private Button Cadastra;
    private FirebaseAuth autenticacao;
    private Usuario usuario = new Usuario();
    private ConfiguracaoSharedPreferences preferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_cadastro_usuario);

        preferences = new ConfiguracaoSharedPreferences ( getApplicationContext() );

        Nome = findViewById(R.id.Cadastra_NomeID);
        Email = findViewById(R.id.Cadastra_EmailID);
        Senha = findViewById(R.id.Cadastra_SenhaID);
        Cadastra = findViewById(R.id.bt_Cadastrar);

        Cadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Nome.getText().toString().isEmpty()){

                    if (!Email.getText().toString().isEmpty()){

                        if (!Senha.getText().toString().isEmpty()){

                            usuario.setNome(Nome.getText().toString());
                            usuario.setEmail(Email.getText().toString());
                            usuario.setSenha(Senha.getText().toString());

                            // Chama Metodo para cadastro usuario
                            CadastrarUsuario(usuario);

                        }
                        else{
                           // Toast.makeText(CadastroUsuarioActivity.this,"Preencha o Senha",Toast.LENGTH_SHORT).show();
                            Senha.setError("Informe a Senha!");
                        }
                    }
                    else {
                       // Toast.makeText(CadastroUsuarioActivity.this,"Preencha o Email",Toast.LENGTH_SHORT).show();
                        Email.setError("Informe o Email!");
                    }
                }
                else {
                    //Toast.makeText(CadastroUsuarioActivity.this,"Preencha o Nome",Toast.LENGTH_SHORT).show();
                    Nome.setError("Informe o Nome!");
                }
            }
        });
    }

    /** Metodo Responsavel por Efetuar o Cadastro do Usuario
     * @param Usuario
     */
    public void CadastrarUsuario(final Usuario usuario){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticao();

        final UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(usuario.getNome());

        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String idUsuario = Base64Custom.CodificarBase64(usuario.getEmail());
                            usuario.setIdUsuario(idUsuario);

                            //Salva Usuario na Web
                            usuario.SalvarUsuarioWeb(idUsuario);

                            //Salva Usuario Local
                            usuario.SalvarUsuarioLocal(getApplicationContext());

                            //Atualia o DisplayNamae
                            autenticacao.getCurrentUser().updateProfile(builder.build());

                            // Salva no SharedPreference
                            preferences.atualizaDadosPessoais( usuario );

                            // Fechar a tela de Cadastro
                            finish();


                         // Verificar as Excessões que podem ocorre como email já cadastro, senha fraca, etc....
                        }else{

                            String excessao;

                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e ){
                                excessao="Digite uma senha mais forte!";
                            }catch (FirebaseAuthInvalidCredentialsException e ){
                                excessao="Email inválidoe";
                            }catch (FirebaseAuthUserCollisionException e ){
                                excessao="Email já se Encontra Cadastrado";
                            }catch (Exception e ){
                                excessao="Erro ao Cadastra usuario" + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroUsuarioActivity.this, excessao,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
