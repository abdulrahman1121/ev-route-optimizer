# 🚗 EV Route Optimizer

A Tesla-inspired electric vehicle route planning application that optimizes charging stops for long-distance journeys. Built with Spring Boot backend and React frontend.

## ✨ Features

- **Smart Route Planning**: Automatically calculates optimal charging stops based on your EV's range
- **Real-time Charging Station Data**: Integrates with OpenChargeMap for comprehensive station coverage
- **EV Model Presets**: Pre-configured settings for popular EV models (Tesla, Ford, Chevrolet, Nissan)
- **Interactive Map**: Visual route display with charging stops and detailed information
- **Custom EV Specifications**: Fine-tune battery capacity, consumption, and charging parameters
- **Responsive Design**: Works seamlessly on desktop and mobile devices

## 🏗️ Architecture

### Backend (Spring Boot)
- **Geocoding Service**: OpenRouteService integration for address resolution
- **Routing Engine**: Turn-by-turn directions with polyline data
- **Charging Station Service**: OpenChargeMap integration for station data
- **Planning Algorithm**: Greedy algorithm for optimal charging stop placement
- **Caching**: Redis-compatible caching with Caffeine
- **Rate Limiting**: Per-IP request throttling

### Frontend (React + TypeScript)
- **Modern UI**: Clean, intuitive interface with gradient design
- **Interactive Map**: Leaflet integration with custom markers
- **Form Validation**: Real-time input validation and error handling
- **Responsive Layout**: Mobile-first design approach
- **Type Safety**: Full TypeScript implementation


