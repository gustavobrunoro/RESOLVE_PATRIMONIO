package com.resolve.gustavobrunoromeira.resolve_patrimonio.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage notificaccao) {
        super.onMessageReceived( notificaccao );

        if ( notificaccao.getNotification() != null ){

            enviaNotificacao( notificaccao );

        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken( s );
    }

    public void enviaNotificacao (RemoteMessage notificacao){

        Uri uriSom = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

        // Cria Notificação
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, getString( R.string.default_notification_channel_id ) )
                                                                              .setContentTitle( notificacao.getNotification().getTitle())
                                                                              .setContentText( notificacao.getNotification().getBody())
                                                                              .setSmallIcon( R.drawable.ic_notificacao_24dp )
                                                                              .setSound( uriSom )
                                                                              ;

        //Recupera NOtificação
        NotificationManager notificationManager  = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            NotificationChannel notificationChannel = new NotificationChannel( getString( R.string.default_notification_channel_id ),"canal",NotificationManager.IMPORTANCE_DEFAULT );
            notificationManager.createNotificationChannel( notificationChannel );

        }

        //Enviar Notificação
        notificationManager.notify( 0,notificationCompat.build() );


    }
}
