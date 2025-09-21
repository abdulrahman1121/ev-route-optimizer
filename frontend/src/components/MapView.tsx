import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { RoutePlan } from '../types/route';
import './MapView.css';

// Fix for default markers in Leaflet
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

interface MapViewProps {
  routePlan: RoutePlan | null;
  isLoading: boolean;
}

export const MapView: React.FC<MapViewProps> = ({ routePlan, isLoading }) => {
  const mapRef = useRef<HTMLDivElement>(null);
  const mapInstanceRef = useRef<L.Map | null>(null);
  const markersRef = useRef<L.Marker[]>([]);
  const polylineRef = useRef<L.Polyline | null>(null);

  useEffect(() => {
    if (mapRef.current && !mapInstanceRef.current) {
      // Initialize map centered on US
      const map = L.map(mapRef.current).setView([39.8283, -98.5795], 4);
      mapInstanceRef.current = map;

      // Add OpenStreetMap tiles
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(map);

      // Force map to resize after a short delay to ensure proper sizing
      setTimeout(() => {
        if (mapInstanceRef.current) {
          mapInstanceRef.current.invalidateSize();
        }
      }, 100);
    }

    return () => {
      if (mapInstanceRef.current) {
        mapInstanceRef.current.remove();
        mapInstanceRef.current = null;
      }
    };
  }, []);

  useEffect(() => {
    if (!mapInstanceRef.current || !routePlan) return;

    const map = mapInstanceRef.current;

    // Clear previous markers and polyline
    markersRef.current.forEach(marker => marker.remove());
    markersRef.current = [];
    if (polylineRef.current) {
      polylineRef.current.remove();
      polylineRef.current = null;
    }

    // Add route polyline
    if (routePlan.overall.polyline && routePlan.overall.polyline.length > 0) {
      const polyline = L.polyline(routePlan.overall.polyline as [number, number][], {
        color: '#3b82f6',
        weight: 4,
        opacity: 0.8
      }).addTo(map);
      polylineRef.current = polyline;

      // Fit map to polyline bounds
      map.fitBounds(polyline.getBounds(), { padding: [20, 20] });
    }

    // Add origin and destination markers
    if (routePlan.overall.polyline && routePlan.overall.polyline.length > 0) {
      const origin = routePlan.overall.polyline[0];
      const destination = routePlan.overall.polyline[routePlan.overall.polyline.length - 1];

      // Origin marker (green)
      const originMarker = L.marker([origin[0], origin[1]], {
        icon: L.divIcon({
          className: 'custom-marker origin-marker',
          html: '🟢',
          iconSize: [24, 24]
        })
      }).addTo(map);
      originMarker.bindPopup('<b>Origin</b>').openPopup();
      markersRef.current.push(originMarker);

      // Destination marker (red)
      const destMarker = L.marker([destination[0], destination[1]], {
        icon: L.divIcon({
          className: 'custom-marker destination-marker',
          html: '🔴',
          iconSize: [24, 24]
        })
      }).addTo(map);
      destMarker.bindPopup('<b>Destination</b>');
      markersRef.current.push(destMarker);
    }

    // Add charging station markers
    routePlan.stops.forEach((stop, index) => {
      const marker = L.marker([stop.station.lat, stop.station.lng], {
        icon: L.divIcon({
          className: 'custom-marker charging-marker',
          html: '⚡',
          iconSize: [24, 24]
        })
      }).addTo(map);

      const popupContent = `
        <div class="station-popup">
          <h4>${stop.station.name}</h4>
          <p><strong>Stop ${index + 1}</strong></p>
          <p>Arrive: ${Math.round(stop.arriveSoC * 100)}%</p>
          <p>Depart: ${Math.round(stop.departSoC * 100)}%</p>
          <p>Charge: ${stop.chargeMinutes} min</p>
          <p>Energy: +${stop.energyAddedKwh.toFixed(1)} kWh</p>
          <p>Connectors: ${stop.station.connectors.join(', ')}</p>
          <p>Max Power: ${stop.station.maxKw} kW</p>
        </div>
      `;

      marker.bindPopup(popupContent);
      markersRef.current.push(marker);
    });

  }, [routePlan]);

  return (
    <div className="map-view">
      <div className="map-container">
        {isLoading && (
          <div className="map-loading">
            <div className="loading-spinner"></div>
            <p>Planning route...</p>
          </div>
        )}
        <div ref={mapRef} className="map" />
      </div>
      
      {!routePlan && !isLoading && (
        <div className="map-placeholder">
          <div className="placeholder-content">
            <h3>🚗 Ready to Plan Your Route?</h3>
            <p>Enter your origin, destination, and EV details to get started.</p>
            <p>Your optimized route with charging stops will appear here.</p>
          </div>
        </div>
      )}
    </div>
  );
};



