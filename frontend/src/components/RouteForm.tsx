import React, { useState, useEffect } from 'react';
import { RouteRequest, EVSpec, EVPreset } from '../types/route';
import { routeApi } from '../api/client';
import './RouteForm.css';

interface RouteFormProps {
  onRoutePlanned: (plan: any) => void;
  onLoadingChange: (loading: boolean) => void;
}

export const RouteForm: React.FC<RouteFormProps> = ({ onRoutePlanned, onLoadingChange }) => {
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [selectedPreset, setSelectedPreset] = useState<string>('');
  const [customEV, setCustomEV] = useState<EVSpec>({
    batteryKwh: 75,
    usableSoCFraction: 0.9,
    consumptionWhPerKm: 160,
    maxChargeKw: 250,
    startSoC: 0.8,
    reserveSoC: 0.1
  });
  const [useCustomEV, setUseCustomEV] = useState(false);
  const [evPresets, setEvPresets] = useState<EVPreset[]>([]);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadEVPresets();
  }, []);

  const loadEVPresets = async () => {
    try {
      const presets = await routeApi.getEVPresets();
      setEvPresets(presets);
      if (presets.length > 0) {
        setSelectedPreset(presets[0].name);
        setCustomEV(presets[0]);
      }
    } catch (err) {
      console.error('Failed to load EV presets:', err);
    }
  };

  const handlePresetChange = (presetName: string) => {
    setSelectedPreset(presetName);
    const preset = evPresets.find(p => p.name === presetName);
    if (preset) {
      setCustomEV(preset);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!origin.trim() || !destination.trim()) {
      setError('Please enter both origin and destination');
      return;
    }

    setError('');
    onLoadingChange(true);

    try {
      const request: RouteRequest = {
        origin: origin.trim(),
        destination: destination.trim(),
        ev: customEV
      };

      const plan = await routeApi.planRoute(request);
      onRoutePlanned(plan);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to plan route. Please try again.');
    } finally {
      onLoadingChange(false);
    }
  };

  return (
    <div className="route-form">
      <h2>Plan Your Route</h2>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="origin">Origin</label>
          <input
            type="text"
            id="origin"
            value={origin}
            onChange={(e) => setOrigin(e.target.value)}
            placeholder="e.g., Seattle, WA"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="destination">Destination</label>
          <input
            type="text"
            id="destination"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
            placeholder="e.g., Boise, ID"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="ev-preset">EV Model</label>
          <select
            id="ev-preset"
            value={selectedPreset}
            onChange={(e) => handlePresetChange(e.target.value)}
            disabled={useCustomEV}
          >
            {evPresets.map(preset => (
              <option key={preset.name} value={preset.name}>
                {preset.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>
            <input
              type="checkbox"
              checked={useCustomEV}
              onChange={(e) => setUseCustomEV(e.target.checked)}
            />
            Use custom EV specifications
          </label>
        </div>

        {useCustomEV && (
          <div className="custom-ev-specs">
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="battery">Battery (kWh)</label>
                <input
                  type="number"
                  id="battery"
                  value={customEV.batteryKwh}
                  onChange={(e) => setCustomEV({...customEV, batteryKwh: parseFloat(e.target.value)})}
                  min="10"
                  max="200"
                  step="0.1"
                />
              </div>
              <div className="form-group">
                <label htmlFor="usable">Usable SOC (%)</label>
                <input
                  type="number"
                  id="usable"
                  value={customEV.usableSoCFraction * 100}
                  onChange={(e) => setCustomEV({...customEV, usableSoCFraction: parseFloat(e.target.value) / 100})}
                  min="50"
                  max="100"
                  step="1"
                />
              </div>
            </div>
            
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="consumption">Consumption (Wh/km)</label>
                <input
                  type="number"
                  id="consumption"
                  value={customEV.consumptionWhPerKm}
                  onChange={(e) => setCustomEV({...customEV, consumptionWhPerKm: parseFloat(e.target.value)})}
                  min="100"
                  max="300"
                  step="5"
                />
              </div>
              <div className="form-group">
                <label htmlFor="maxCharge">Max Charge (kW)</label>
                <input
                  type="number"
                  id="maxCharge"
                  value={customEV.maxChargeKw}
                  onChange={(e) => setCustomEV({...customEV, maxChargeKw: parseFloat(e.target.value)})}
                  min="10"
                  max="350"
                  step="10"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="startSOC">Start SOC (%)</label>
                <input
                  type="number"
                  id="startSOC"
                  value={customEV.startSoC * 100}
                  onChange={(e) => setCustomEV({...customEV, startSoC: parseFloat(e.target.value) / 100})}
                  min="10"
                  max="100"
                  step="5"
                />
              </div>
              <div className="form-group">
                <label htmlFor="reserve">Reserve SOC (%)</label>
                <input
                  type="number"
                  id="reserve"
                  value={customEV.reserveSoC * 100}
                  onChange={(e) => setCustomEV({...customEV, reserveSoC: parseFloat(e.target.value) / 100})}
                  min="5"
                  max="20"
                  step="1"
                />
              </div>
            </div>
          </div>
        )}

        {error && <div className="error-message">{error}</div>}

        <button type="submit" className="submit-button">
          Plan Route
        </button>
      </form>
    </div>
  );
};



