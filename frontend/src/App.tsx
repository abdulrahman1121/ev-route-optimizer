import { useState } from 'react';
import { RouteForm } from './components/RouteForm';
import { PlanSummary } from './components/PlanSummary';
import { MapView } from './components/MapView';
import { RoutePlan } from './types/route';
import './App.css';

function App() {
  const [routePlan, setRoutePlan] = useState<RoutePlan | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleRoutePlanned = (plan: RoutePlan) => {
    setRoutePlan(plan);
  };

  const handleLoadingChange = (loading: boolean) => {
    setIsLoading(loading);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>🚗 EV Route Optimizer</h1>
        <p>Plan your electric vehicle journey with optimal charging stops</p>
      </header>
      
      <main className="app-main">
        <div className="left-panel">
          <RouteForm 
            onRoutePlanned={handleRoutePlanned}
            onLoadingChange={handleLoadingChange}
          />
          {routePlan && <PlanSummary routePlan={routePlan} />}
        </div>
        
        <div className="right-panel">
          <MapView 
            routePlan={routePlan}
            isLoading={isLoading}
          />
        </div>
      </main>
    </div>
  );
}

export default App;


