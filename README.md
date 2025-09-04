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

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+
- npm or yarn

### Backend Setup

1. **Clone and navigate to project**
   ```bash
   cd ev-route-optimizer
   ```

2. **Set environment variables**
   ```bash
   export ORS_API_KEY="your_openrouteservice_api_key"
   export OCM_API_KEY="your_openchargemap_api_key"  # Optional
   ```

3. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Backend will be available at `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm run dev
   ```

   Frontend will be available at `http://localhost:5173`

## 🔑 API Keys

### OpenRouteService (Required)
- Sign up at [OpenRouteService](https://openrouteservice.org/)
- Free tier: 2,000 requests/day
- Used for geocoding and routing

### OpenChargeMap (Optional)
- Sign up at [OpenChargeMap](https://openchargemap.org/)
- Free tier: 10,000 requests/month
- Used for charging station data

## 📱 Usage

1. **Enter Route Details**
   - Origin and destination addresses
   - Select your EV model from presets
   - Optionally customize EV specifications

2. **Plan Route**
   - Click "Plan Route" to generate optimized journey
   - View charging stops with arrival/departure SOC
   - See estimated charge times and energy requirements

3. **Explore Results**
   - Interactive map with route polyline
   - Detailed stop-by-stop breakdown
   - Total journey statistics

## 🧮 Planning Algorithm

The route planning algorithm follows these steps:

1. **Geocode** origin and destination addresses
2. **Calculate route** using OpenRouteService
3. **Find charging stations** within corridor around route
4. **Apply greedy algorithm**:
   - Start with initial SOC
   - Find farthest reachable station within range
   - Calculate charge needed for next leg
   - Repeat until destination is reachable
5. **Optimize charging stops** to minimize total journey time

### Key Assumptions
- Constant energy consumption (Wh/km)
- Linear charging curve (can be enhanced with real taper curves)
- Station availability (operational status from OCM)
- No elevation/temperature adjustments (MVP version)

## 🏗️ Project Structure

```
ev-route-optimizer/
├── src/main/java/com/evroute/
│   ├── controller/          # REST API endpoints
│   ├── service/            # Business logic services
│   ├── client/             # External API clients
│   ├── model/              # Data models
│   └── config/             # Configuration classes
├── frontend/
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── api/            # API client
│   │   ├── types/          # TypeScript interfaces
│   │   └── styles/         # CSS files
│   ├── package.json        # Frontend dependencies
│   └── vite.config.ts      # Build configuration
├── pom.xml                 # Maven configuration
└── README.md              # This file
```

## 🧪 Testing

### Backend Tests
```bash
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 🚀 Deployment

### Backend Deployment
- **Render**: Easy deployment with Maven build
- **Heroku**: Add `system.properties` for Java 17
- **AWS EC2**: Traditional server deployment
- **Docker**: Containerized deployment

### Frontend Deployment
- **Vercel**: Automatic deployments from Git
- **Netlify**: Drag-and-drop deployment
- **AWS S3**: Static website hosting

## 🔧 Configuration

### Backend Configuration
```properties
# application.properties
server.port=8080
app.geocode.provider=ORS
app.directions.provider=ORS
app.stations.provider=OCM
ors.api.key=${ORS_API_KEY}
cors.allowedOrigins=http://localhost:5173,http://localhost:3000
```

### Frontend Configuration
```typescript
// vite.config.ts
export default defineConfig({
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
})
```

## 🚧 Future Enhancements

- **Real-time Traffic**: Google Maps Platform integration
- **Elevation Adjustments**: Terrain-based consumption modeling
- **Weather Integration**: Temperature and wind effects
- **User Accounts**: Save vehicles and favorite routes
- **Mobile App**: React Native implementation
- **Advanced Planning**: Multiple route optimization strategies
- **Station Status**: Live availability and pricing
- **Social Features**: Route sharing and community ratings

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **OpenRouteService** for routing and geocoding
- **OpenChargeMap** for charging station data
- **Tesla** for inspiration in EV route planning
- **Spring Boot** and **React** communities for excellent frameworks

## 📞 Support

For questions, issues, or contributions:
- Open an issue on GitHub
- Check the documentation
- Review the code examples

---

**Built with ❤️ for the EV community**
