package com.example.super1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private ArrayList<Marker> markers;
    // This is the key for the map view bundle
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa2);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocation();
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


        googleMap.setOnMarkerClickListener(marker -> {
            drawRoute(marker.getPosition());
            return false;
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
        if (markers == null) {
            markers = new ArrayList<>();
        } else {
            removeStreetMarkers();
        }

        if (googleMap == null) {
            Log.e("Erro", "googleMap está nulo");
            return;
        }

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


    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(userLocation).title("Você está aqui"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        });
    }


    public interface DirectionsAPI {
        @GET("maps/api/directions/json")
        Call<DirectionsResponse> getDirections(
                @Query("origin") String origin,
                @Query("destination") String destination,
                @Query("key") String apiKey
        );
    }


    private void drawRoute(LatLng destination) {
        if (userLocation == null) {
            Toast.makeText(this, "Localização do usuário não encontrada!", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DirectionsAPI service = retrofit.create(DirectionsAPI.class);
        String origin = userLocation.latitude + "," + userLocation.longitude;
        String dest = destination.latitude + "," + destination.longitude;

        service.getDirections(origin, dest, "AIzaSyDq6bTgUB7T8loCh2-_0D_U7MXm6OKk2_4").enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LatLng> routePoints = decodePolyline(response.body().routes.get(0).overviewPolyline.points);
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(routePoints).color(Color.BLUE).width(10);
                    googleMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e("MapsError", "Erro ao obter rota: " + t.getMessage());
            }
        });
    }

    // Método para decodificar a polylinha
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((lat / 1E5), (lng / 1E5));
            poly.add(p);
        }

        return poly;
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
