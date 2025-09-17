export interface EVSpec {
  batteryKwh: number;
  usableSoCFraction: number;
  consumptionWhPerKm: number;
  maxChargeKw: number;
  startSoC: number;
  reserveSoC: number;
}

export interface RouteRequest {
  origin: string;
  destination: string;
  ev: EVSpec;
  prefs?: RoutePrefs;
}

export interface RoutePrefs {
  targetArrivalSoC: number;
  planningSpeedKph: number;
}

export interface ChargingStation {
  id: string;
  name: string;
  lat: number;
  lng: number;
  connectors: string[];
  maxKw: number;
  operational: boolean;
}

export interface PlannedStop {
  station: ChargingStation;
  arriveSoC: number;
  departSoC: number;
  chargeMinutes: number;
  energyAddedKwh: number;
}

export interface LegSummary {
  polyline: [number, number][];
  distanceKm: number;
  driveMinutes: number;
}

export interface RoutePlan {
  overall: LegSummary;
  legs: LegSummary[];
  stops: PlannedStop[];
  totalEnergyKwh: number;
  totalDriveMinutes: number;
  totalChargeMinutes: number;
}

export interface EVPreset {
  name: string;
  batteryKwh: number;
  usableSoCFraction: number;
  consumptionWhPerKm: number;
  maxChargeKw: number;
  startSoC: number;
  reserveSoC: number;
}


