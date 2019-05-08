package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Destino;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaLocalizacao extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mapa_localizacao );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Recupera Localizção
        recuperaLocalizacaoUsuario();
    }

    private void recuperaLocalizacaoUsuario() {

        locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double Latitude, Longitude;

                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
                LatLng MeuLocal = new LatLng( Latitude , Longitude );

                mMap.clear();
                mMap.addMarker( new MarkerOptions()
                        .position( MeuLocal )
                        .title( "Meu Local" )
                );

                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( MeuLocal , 15 ) );

            }

            @Override
            public void onStatusChanged(String s , int i , Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        //Solicita atualizações de Localização
        if ( ActivityCompat.checkSelfPermission( this , Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER , 1000 , 0, locationListener );
        }

    }



    public void trajeto(){

        String enderecoDestino = "Rua Roberto Santo,137,Centro,Eunapolis" ;

        if( !enderecoDestino.equals( "" ) && enderecoDestino != null ){

            Address addressDestino = recuperaEndereco( enderecoDestino );

            if (addressDestino != null ){

                Destino destino = new Destino();

                destino.setCidade( addressDestino.getAdminArea() );
                destino.setCep( addressDestino.getPostalCode() );
                destino.setBairro( addressDestino.getSubLocality() );
                destino.setRua( addressDestino.getThoroughfare() );
                destino.setNumero( addressDestino.getFeatureName() );
                destino.setLatitude( String.valueOf( addressDestino.getLatitude() ) );
                destino.setLongitude( String.valueOf( addressDestino.getLongitude() ) );

            }

        }

    }

    private Address recuperaEndereco (String endereco){

        Geocoder geocoder = new Geocoder( this, Locale.getDefault() );

        try {

            List<Address> listaEndereco = geocoder.getFromLocationName( endereco,1 );

            if (listaEndereco != null && listaEndereco.size() > 0){

                Address address = listaEndereco.get( 0 );

                return  address;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }



        return  null;

    }

}
