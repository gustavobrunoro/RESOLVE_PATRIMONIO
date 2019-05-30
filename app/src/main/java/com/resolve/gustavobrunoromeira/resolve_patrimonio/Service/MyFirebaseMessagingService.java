package com.resolve.gustavobrunoromeira.resolve_patrimonio.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Sicronizacao_Organizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.SobreActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage notificaccao) {
        super.onMessageReceived( notificaccao );

        if ( notificaccao.getNotification() != null ){

            Map<String,String> data = notificaccao.getData();

            if ( data.size() > 0){

                Log.i("recuperaToken", "Nome: " + data.get("nome"));
                Log.i("recuperaToken", "Sobrenome: " + data.get("sobrenome"));

            }

            enviaNotificacao( notificaccao );

        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken( s );
    }

    public void enviaNotificacao (RemoteMessage notificacao){

        Intent intent = new Intent(this, Sicronizacao_Organizacao.class);
        //intent.setAction( "Atualizar" );
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, intent, 0);

        NotificationCompat.Action notificationCompatAtualiza = new NotificationCompat.Action(R.drawable.ic_confirma_24dp,"Atualizar", pendingIntent);

        Uri uriSom = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

        // Cria Notificação
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, getString( R.string.default_notification_channel_id ) )
                                                              .setContentTitle( notificacao.getNotification().getTitle())
                                                              .setContentText( notificacao.getNotification().getBody())
                                                              .setSmallIcon( R.drawable.ic_notificacao_24dp )
                                                              .setSound( uriSom )
                                                              .addAction( notificationCompatAtualiza )
                                                              .setAutoCancel( true );

        //Recupera NOtificação
        NotificationManager notificationManager  = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            NotificationChannel notificationChannel = new NotificationChannel( getString( R.string.default_notification_channel_id ),"canal",NotificationManager.IMPORTANCE_DEFAULT );
            notificationManager.createNotificationChannel( notificationChannel );

        }

        //Enviar Notificação
        notificationManager.notify( 0,notificationCompat.build() );


    }

    public void recuperaToken(){

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                Log.i("recuperaToken", "recuperaToken " + String.valueOf( instanceIdResult.getToken() ));

            }
        } );
    }
}
