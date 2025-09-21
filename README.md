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

## 🚀 Setup Instructions

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- npm or yarn
- Maven 3.6 or higher

### API Keys Required
You'll need to obtain API keys from:
- **OpenRouteService**: Sign up at [openrouteservice.org](https://openrouteservice.org/) for geocoding and routing
- **OpenChargeMap**: Sign up at [openchargemap.org](https://openchargemap.org/) for charging station data

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/abdulrahman1121/ev-route-optimizer.git
   cd ev-route-optimizer
   ```

2. Create environment file:
   ```bash
   cp env.example .env
   ```

3. Edit `.env` file and add your API keys:
   ```env
   ORS_API_KEY=your_openrouteservice_api_key_here
   OCM_API_KEY=your_openchargemap_api_key_here
   ```

4. Start the backend:
   ```bash
   mvn spring-boot:run
   ```
   The backend will start on `http://localhost:8081`

### Frontend Setup
1. Navigate to frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```
   The frontend will start on `http://localhost:5173`

### Running the Application
1. Make sure both backend and frontend are running
2. Open your browser and go to `http://localhost:5173`
3. Enter your origin, destination, and EV details to plan your route
4. View the optimized route with charging stops on the interactive map

### Production Build
To build the frontend for production:
```bash
cd frontend
npm run build
```

The built files will be in the `frontend/dist` directory.


