import axios from 'axios';
import { RouteRequest, RoutePlan, ChargingStation, EVPreset } from '../types/route';

export const api = axios.create({
  baseURL: '/api',
  timeout: 15000,
});

export const routeApi = {
  planRoute: async (request: RouteRequest): Promise<RoutePlan> => {
    const response = await api.post<RoutePlan>('/route/plan', request);
    return response.data;
  },

  findStationsNear: async (lat: number, lng: number, radiusKm: number = 10): Promise<ChargingStation[]> => {
    const response = await api.get<ChargingStation[]>('/stations/near', {
      params: { lat, lng, radiusKm }
    });
    return response.data;
  },

  getEVPresets: async (): Promise<EVPreset[]> => {
    const response = await api.get<EVPreset[]>('/ev/presets');
    return response.data;
  },

  health: async (): Promise<{ status: string; message: string }> => {
    const response = await api.get('/health');
    return response.data;
  }
};



