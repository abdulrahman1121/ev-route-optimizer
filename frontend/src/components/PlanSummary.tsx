import React from 'react';
import { RoutePlan, PlannedStop } from '../types/route';
import './PlanSummary.css';

interface PlanSummaryProps {
  routePlan: RoutePlan;
}

export const PlanSummary: React.FC<PlanSummaryProps> = ({ routePlan }) => {
  const formatTime = (minutes: number): string => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours > 0) {
      return `${hours}h ${mins}m`;
    }
    return `${mins}m`;
  };

  const formatSOC = (soc: number): string => {
    return `${Math.round(soc * 100)}%`;
  };

  return (
    <div className="plan-summary">
      <h2>Route Summary</h2>
      
      <div className="summary-stats">
        <div className="stat-item">
          <span className="stat-label">Total Distance:</span>
          <span className="stat-value">{routePlan.overall.distanceKm.toFixed(1)} km</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Drive Time:</span>
          <span className="stat-value">{formatTime(routePlan.totalDriveMinutes)}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Charge Time:</span>
          <span className="stat-value">{formatTime(routePlan.totalChargeMinutes)}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Total Time:</span>
          <span className="stat-value">{routePlan.getFormattedTotalTime()}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Energy Used:</span>
          <span className="stat-value">{routePlan.totalEnergyKwh.toFixed(1)} kWh</span>
        </div>
      </div>

      {routePlan.stops.length > 0 ? (
        <div className="charging-stops">
          <h3>Charging Stops ({routePlan.stops.length})</h3>
          <div className="stops-list">
            {routePlan.stops.map((stop, index) => (
              <div key={stop.station.id} className="stop-item">
                <div className="stop-header">
                  <span className="stop-number">{index + 1}</span>
                  <span className="stop-name">{stop.station.name}</span>
                </div>
                
                <div className="stop-details">
                  <div className="stop-location">
                    {stop.station.lat.toFixed(4)}, {stop.station.lng.toFixed(4)}
                  </div>
                  
                  <div className="stop-connectors">
                    {stop.station.connectors.join(', ')} • {stop.station.maxKw} kW
                  </div>
                  
                  <div className="stop-soc">
                    <span className="soc-label">Arrive:</span>
                    <span className={`soc-value ${stop.arriveSoC < 0.2 ? 'low' : ''}`}>
                      {formatSOC(stop.arriveSoC)}
                    </span>
                    <span className="soc-label">Depart:</span>
                    <span className="soc-value">{formatSOC(stop.departSoC)}</span>
                  </div>
                  
                  <div className="stop-charging">
                    <span className="charge-time">{formatTime(stop.chargeMinutes)}</span>
                    <span className="energy-added">+{stop.energyAddedKwh.toFixed(1)} kWh</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div className="no-stops">
          <h3>No Charging Stops Required</h3>
          <p>Your vehicle can reach the destination without charging!</p>
        </div>
      )}

      <div className="route-legs">
        <h3>Route Legs</h3>
        <div className="legs-list">
          {routePlan.legs.map((leg, index) => (
            <div key={index} className="leg-item">
              <div className="leg-header">
                <span className="leg-number">Leg {index + 1}</span>
                <span className="leg-distance">{leg.distanceKm.toFixed(1)} km</span>
              </div>
              <div className="leg-time">{formatTime(leg.driveMinutes)}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
