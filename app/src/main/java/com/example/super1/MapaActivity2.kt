package com.example.super1

/*import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds*/

/*class MapaActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa2)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        val localizacao = LatLng(-3.1190, -60.0217) // Manaus
        googleMap.addMarker(MarkerOptions().position(localizacao).title("Estou Aqui"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 15f))
    }*/

    override fun onMapReady(googleMap: GoogleMap) {
        val localizacao = LatLng(-3.1190, -60.0217) // Ponto central de Manaus

        // Definir os limites da cidade de Manaus
        val limitesManaus = LatLngBounds(
            LatLng(-3.2100, -60.1050), // Sudoeste (ponto inferior esquerdo)
            LatLng(-3.0000, -59.9000)  // Nordeste (ponto superior direito)
        )

        // Adicionar marcador
        googleMap.addMarker(MarkerOptions().position(localizacao).title("Estou Aqui"))

        // Mover a câmera para Manaus
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 12f))

        // Aplicar restrições de limites de movimento do mapa
        googleMap.setLatLngBoundsForCameraTarget(limitesManaus) // Limita a área visível
        googleMap.setMinZoomPreference(12f)  // Zoom mínimo
        googleMap.setMaxZoomPreference(15f) // Zoom máximo
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}*/

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.widget.Toast
import com.google.android.gms.maps.model.MapStyleOptions
import android.graphics.BitmapFactory
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

val blueIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)

class MapaActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private val markersList = mutableListOf<Marker>() // Marcadores devem ser do tipo 'Marker'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa2)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Limpar todas as marcações existentes
        googleMap.clear()

        // Carregar o estilo personalizado para ocultar POIs (shoppings, restaurantes, etc.)
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.style_no_pois)
        )
        if (!success) {
            // Caso o estilo não seja carregado corretamente
            Toast.makeText(this, "Erro ao carregar o estilo do mapa", Toast.LENGTH_SHORT).show()
        }

        // Definir os limites da cidade de Manaus
        val localizacao = LatLng(-3.1190, -60.0217) // Ponto central de Manaus
        val limitesManaus = LatLngBounds(
            LatLng(-3.2100, -60.1050), // Sudoeste (ponto inferior esquerdo)
            LatLng(-3.0000, -59.9000)  // Nordeste (ponto superior direito)
        )

        // Mover a câmera para Manaus
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 12f))

        // Aplicar restrições de limites de movimento do mapa
        googleMap.setLatLngBoundsForCameraTarget(limitesManaus) // Limita a área visível
        googleMap.setMinZoomPreference(12f)  // Zoom mínimo
        googleMap.setMaxZoomPreference(21f) // Zoom máximo (máximo permitido pelo Google Maps)

        // Definir pontos de coleta de lixo (exemplo com 2 pontos)
        val pontosColetaLixo = listOf(
            LatLng(-3.1190, -60.0217), // Exemplo 1
            LatLng(-3.1050, -60.0150)  // Exemplo 2
        )

        // Adicionar marcações de coleta de lixo em vermelho
        for (ponto in pontosColetaLixo) {
            googleMap.addMarker(MarkerOptions()
                .position(ponto)
                .title("Ponto de Coleta de Lixo")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        }

        // Adicionar marcações azuis nas ruas quando o usuário der zoom
        googleMap.setOnCameraIdleListener {
            val zoomLevel = googleMap.cameraPosition.zoom
            if (zoomLevel >= 14f) {  // Define o zoom mínimo para mostrar as ruas
                addStreetMarkers() // Chama a função para adicionar os marcadores de ruas
            } else {
                removeStreetMarkers() // Remove os marcadores de ruas se o zoom for baixo
            }
        }
    }

    // Função para adicionar marcadores nas ruas
    private fun addStreetMarkers() {
        // Limpar os marcadores anteriores para evitar duplicação
        removeStreetMarkers()  // Alterado para a função correta

        // Definir as coordenadas das ruas que você quer marcar com a cor azul
        val ruas = listOf(
            LatLng(-3.1195, -60.0220),  // Exemplo de rua 1
            LatLng(-3.1055, -60.0120),  // Exemplo de rua 2
            LatLng(-3.1135, -60.0200)   // Exemplo de rua 3
        )

        // Adicionar marcadores azuis nas ruas
        for (rua in ruas) {
            val marker: Marker = googleMap.addMarker(MarkerOptions()
                .position(rua)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Rua: $rua")  // Título de exemplo para as ruas
            ) ?: return  // Garantir que o marcador não seja nulo, caso contrário, retorna

            markersList.add(marker) // Adiciona o marcador na lista
        }
    }

    // Função para remover os marcadores das ruas
    private fun removeStreetMarkers() {
        for (marker in markersList) {
            marker.remove() // Remove cada marcador da lista
        }
        markersList.clear() // Limpa a lista de marcadores
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
