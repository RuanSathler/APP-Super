package com.example.super1;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private ArrayList<Marker> markers;
    // This is the key for the map view bundle
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa2);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
//        initGoogleMap(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Clear the map
        googleMap.clear();

        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_no_pois));
        if (!success) {
            // Handle map style load failure
            Toast.makeText(this, "Erro ao carregar o estilo do mapa", Toast.LENGTH_SHORT).show();
        }

        // Limitar a Manaus
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(-3.25, -60.25), new LatLng(-2.75, -59.75)));

        // Posicionar a camera no centro de Manaus
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-3.119027, -60.021731), 12));

        // Pontos de coleta
        ArrayList<LatLng> pontosDeColeta = new ArrayList<>();
        pontosDeColeta.add(new LatLng(-3.1190, -60.0217)); // Exemplo 1
        pontosDeColeta.add(new LatLng(-3.1050, -60.0150)); // Exemplo 2
        pontosDeColeta.add(new LatLng(-3.0850, -60.0200)); // Exemplo 3
        pontosDeColeta.add(new LatLng(-3.0750, -60.0300)); // Exemplo 4

        for (LatLng ponto : pontosDeColeta) {
            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                            .position(ponto)
                            .title("Ponto de Coleta")))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Tratar zoom
        googleMap.setOnCameraMoveListener(() -> {
            if (googleMap.getCameraPosition().zoom < 12) {
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                removeStreetMarkers();
            } else {
                addStreetMarkers();
            }
        });
    }


    private void removeStreetMarkers() {
        if (markers != null) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
    }

    // Adicionar marcadores de rua
    private void addStreetMarkers() {
        removeStreetMarkers();

        // Exemplo de marcadores de rua
        ArrayList<LatLng> ruas = new ArrayList<>();
        ruas.add(new LatLng(-3.1190, -60.0217)); // Exemplo 1
        ruas.add(new LatLng(-3.1050, -60.0150)); // Exemplo 2
        ruas.add(new LatLng(-3.0850, -60.0200)); // Exemplo 3
        ruas.add(new LatLng(-3.0750, -60.0300)); // Exemplo 4

        for (LatLng rua : ruas) {
            markers.add(Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                    .position(rua)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Rua: " + rua.latitude + ", " + rua.longitude))));
        }
//            markers.trimToSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
}
