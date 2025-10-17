import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import { Package, Navigation, FileText } from 'lucide-react';
import 'leaflet/dist/leaflet.css';

// Fix for default markers in React-Leaflet
import L from 'leaflet';
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

// Common HS Codes for autocomplete
const hsCodes = [
  { code: '0101', description: 'Live horses, asses, mules and hinnies' },
  { code: '0201', description: 'Meat of bovine animals, fresh or chilled' },
  { code: '0301', description: 'Live fish' },
  { code: '0401', description: 'Milk and cream' },
  { code: '0701', description: 'Potatoes, fresh or chilled' },
  { code: '0801', description: 'Coconuts, Brazil nuts and cashew nuts' },
  { code: '0901', description: 'Coffee' },
  { code: '1001', description: 'Wheat and meslin' },
  { code: '1701', description: 'Cane or beet sugar' },
  { code: '2701', description: 'Coal' },
  { code: '3004', description: 'Medicaments' },
  { code: '4011', description: 'New pneumatic tyres, of rubber' },
  { code: '6109', description: 'T-shirts, singlets and other vests' },
  { code: '7108', description: 'Gold' },
  { code: '8471', description: 'Automatic data processing machines' },
  { code: '8517', description: 'Telephone sets, smartphones' },
  { code: '8703', description: 'Motor cars and vehicles' },
  { code: '9018', description: 'Medical, surgical instruments' },
];

// Common cities for autocomplete
const cities = [
  'São Paulo, SP, Brazil',
  'Rio de Janeiro, RJ, Brazil',
  'New York, NY, USA',
  'Los Angeles, CA, USA',
  'London, England, UK',
  'Paris, Ile-de-France, France',
  'Tokyo, Tokyo, Japan',
  'Beijing, Beijing, China',
  'Shanghai, Shanghai, China',
  'Dubai, Dubai, UAE',
  'Singapore, Singapore, Singapore',
  'Sydney, NSW, Australia',
  'Mumbai, Maharashtra, India',
  'Toronto, ON, Canada',
  'Mexico City, CDMX, Mexico',
  'Buenos Aires, Buenos Aires, Argentina',
  'Berlin, Berlin, Germany',
  'Madrid, Madrid, Spain',
  'Rome, Lazio, Italy',
  'Moscow, Moscow, Russia',
];

// Autocomplete component
const AutocompleteInput = ({ value, onChange, suggestions, placeholder }) => {
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [filteredSuggestions, setFilteredSuggestions] = useState([]);

  useEffect(() => {
    if (value.length > 0) {
      const filtered = suggestions.filter(item =>
        item.toLowerCase().includes(value.toLowerCase())
      );
      setFilteredSuggestions(filtered);
    } else {
      setFilteredSuggestions([]);
    }
  }, [value, suggestions]);

  const inputStyle = {
    width: '100%',
    padding: '12px 16px',
    border: '1px solid #cbd5e1',
    borderRadius: '8px',
    fontSize: '14px',
    outline: 'none',
    transition: 'all 0.2s',
  };

  const suggestionBoxStyle = {
    position: 'absolute',
    zIndex: 10,
    width: '100%',
    marginTop: '4px',
    backgroundColor: 'white',
    border: '1px solid #cbd5e1',
    borderRadius: '8px',
    boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
    maxHeight: '240px',
    overflowY: 'auto',
  };

  const suggestionItemStyle = {
    padding: '8px 16px',
    cursor: 'pointer',
    fontSize: '14px',
  };

  return (
    <div style={{ position: 'relative' }}>
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        onFocus={() => setShowSuggestions(true)}
        onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
        placeholder={placeholder}
        style={inputStyle}
      />
      {showSuggestions && filteredSuggestions.length > 0 && (
        <div style={suggestionBoxStyle}>
          {filteredSuggestions.map((suggestion, index) => (
            <div
              key={index}
              onClick={() => {
                onChange(suggestion);
                setShowSuggestions(false);
              }}
              style={suggestionItemStyle}
              onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#eff6ff'}
              onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'white'}
            >
              {suggestion}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

// Curved path component
const CurvedPath = ({ start, end }) => {
  const map = useMap();

  useEffect(() => {
    if (!start || !end) return;

    map.eachLayer((layer) => {
      if (layer instanceof L.Polyline && layer.options.className === 'shipping-route') {
        map.removeLayer(layer);
      }
    });

    const midLat = (start[0] + end[0]) / 2;
    const midLng = (start[1] + end[1]) / 2;
    const offset = Math.abs(end[1] - start[1]) * 0.3;
    const controlPoint = [midLat + offset, midLng];

    const points = [];
    for (let t = 0; t <= 1; t += 0.01) {
      const lat = Math.pow(1 - t, 2) * start[0] + 2 * (1 - t) * t * controlPoint[0] + Math.pow(t, 2) * end[0];
      const lng = Math.pow(1 - t, 2) * start[1] + 2 * (1 - t) * t * controlPoint[1] + Math.pow(t, 2) * end[1];
      points.push([lat, lng]);
    }

    L.polyline(points, {
      color: '#0066CC',
      weight: 3,
      opacity: 0.7,
      className: 'shipping-route',
      dashArray: '10, 10',
    }).addTo(map);

    const bounds = L.latLngBounds([start, end]);
    map.fitBounds(bounds, { padding: [50, 50] });

  }, [start, end, map]);

  return null;
};

export default function DromosLogistics() {
  const [formData, setFormData] = useState({
    hsCode: '',
    totalPrice: '',
    startLocation: '',
    endLocation: '',
  });

  const [coordinates, setCoordinates] = useState({
    start: null,
    end: null,
  });

  const geocodeLocation = (location) => {
    const cityCoords = {
      'são paulo, sp, brazil': [-23.5505, -46.6333],
      'rio de janeiro, rj, brazil': [-22.9068, -43.1729],
      'new york, ny, usa': [40.7128, -74.0060],
      'los angeles, ca, usa': [34.0522, -118.2437],
      'london, england, uk': [51.5074, -0.1278],
      'paris, ile-de-france, france': [48.8566, 2.3522],
      'tokyo, tokyo, japan': [35.6762, 139.6503],
      'beijing, beijing, china': [39.9042, 116.4074],
      'shanghai, shanghai, china': [31.2304, 121.4737],
      'dubai, dubai, uae': [25.2048, 55.2708],
      'singapore, singapore, singapore': [1.3521, 103.8198],
      'sydney, nsw, australia': [-33.8688, 151.2093],
      'mumbai, maharashtra, india': [19.0760, 72.8777],
      'toronto, on, canada': [43.6532, -79.3832],
      'mexico city, cdmx, mexico': [19.4326, -99.1332],
      'buenos aires, buenos aires, argentina': [-34.6037, -58.3816],
      'berlin, berlin, germany': [52.5200, 13.4050],
      'madrid, madrid, spain': [40.4168, -3.7038],
      'rome, lazio, italy': [41.9028, 12.4964],
      'moscow, moscow, russia': [55.7558, 37.6173],
    };

    const normalized = location.toLowerCase().trim();
    return cityCoords[normalized] || null;
  };

  const handleCalculate = () => {
    if (!formData.hsCode || !formData.totalPrice || !formData.startLocation || !formData.endLocation) {
      alert('Please fill in all fields');
      return;
    }

    const startCoords = geocodeLocation(formData.startLocation);
    const endCoords = geocodeLocation(formData.endLocation);

    if (!startCoords) {
      alert('Start location not recognized. Please select from suggestions.');
      return;
    }

    if (!endCoords) {
      alert('End location not recognized. Please select from suggestions.');
      return;
    }

    setCoordinates({
      start: startCoords,
      end: endCoords,
    });

    console.log('Form data to send to backend:', formData);
  };

  const hsCodeSuggestions = hsCodes.map(hs => `${hs.code} - ${hs.description}`);

  const containerStyle = {
    display: 'flex',
    height: '100vh',
    backgroundColor: '#f8fafc',
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
  };

  const leftPanelStyle = {
    width: '40%',
    backgroundColor: 'white',
    boxShadow: '2px 0 8px rgba(0,0,0,0.1)',
    overflowY: 'auto',
  };

  const rightPanelStyle = {
    width: '60%',
    position: 'relative',
  };

  const headerStyle = {
    padding: '32px',
  };

  const logoStyle = {
    width: '280px',
    height: 'auto',
    marginBottom: '24px',
  };

  const subtitleStyle = {
    fontSize: '16px',
    color: '#64748b',
    marginTop: '8px',
  };

  const formContainerStyle = {
    padding: '0 32px 32px 32px',
  };

  const fieldStyle = {
    marginBottom: '24px',
  };

  const labelStyle = {
    display: 'flex',
    alignItems: 'center',
    fontSize: '14px',
    fontWeight: '500',
    color: '#334155',
    marginBottom: '8px',
  };

  const iconStyle = {
    marginRight: '8px',
    color: '#0066CC',
  };

  const inputStyle = {
    width: '100%',
    padding: '12px 16px',
    border: '1px solid #cbd5e1',
    borderRadius: '8px',
    fontSize: '14px',
    outline: 'none',
    transition: 'all 0.2s',
  };

  const buttonStyle = {
    width: '100%',
    backgroundColor: '#0066CC',
    color: 'white',
    fontWeight: '500',
    padding: '12px 24px',
    borderRadius: '8px',
    border: 'none',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'all 0.2s',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
  };

  const totalBoxStyle = {
    marginTop: '24px',
    padding: '16px',
    backgroundColor: '#f1f5f9',
    borderRadius: '8px',
  };

  const totalLabelStyle = {
    fontSize: '14px',
    color: '#64748b',
    marginBottom: '4px',
  };

  const totalValueStyle = {
    fontSize: '24px',
    fontWeight: 'bold',
    color: '#1e293b',
  };

  const overlayStyle = {
    position: 'absolute',
    top: '16px',
    right: '16px',
    backgroundColor: 'white',
    padding: '16px',
    borderRadius: '8px',
    boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
    zIndex: 1000,
  };

  const overlayTitleStyle = {
    fontWeight: '600',
    color: '#1e293b',
    marginBottom: '8px',
  };

  const overlayTextStyle = {
    fontSize: '14px',
    color: '#64748b',
  };

  return (
    <div style={containerStyle}>
      {/* Left Side - Form (40%) */}
      <div style={leftPanelStyle}>
        <div style={headerStyle}>
          <svg width="280" height="98" viewBox="0 0 400 140" xmlns="http://www.w3.org/2000/svg" style={logoStyle}>
            <defs>
              <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stopColor="#0B3C5D"/>
                <stop offset="100%" stopColor="#00AEEF"/>
              </linearGradient>
            </defs>
            <g transform="translate(20,40)">
              <circle cx="40" cy="30" r="30" fill="none" stroke="url(#grad)" strokeWidth="3"/>
              <path d="M10 30 A30 15 0 0 0 70 30 A30 15 0 0 0 10 30Z" fill="none" stroke="#00AEEF" strokeWidth="2"/>
              <path d="M40 0 A30 30 0 0 0 40 60" fill="none" stroke="#00AEEF" strokeWidth="2"/>
              <circle cx="40" cy="30" r="2" fill="#00AEEF"/>
            </g>
            <text x="100" y="60" fontFamily="Montserrat, sans-serif" fontSize="42" fontWeight="700" fill="url(#grad)">
              DROMOS
            </text>
            <text x="102" y="90" fontFamily="Raleway, sans-serif" fontSize="18" letterSpacing="2" fill="#7D8CA3">
              LOGISTICS
            </text>
          </svg>
          {/* <p style={subtitleStyle}>International Logistics Planning</p> */}
        </div>

        <div style={formContainerStyle}>
          {/* HS Code */}
          <div style={fieldStyle}>
            <label style={labelStyle}>
              <FileText size={16} style={iconStyle} />
              HS Code
            </label>
            <AutocompleteInput
              value={formData.hsCode}
              onChange={(value) => setFormData(prev => ({ ...prev, hsCode: value }))}
              suggestions={hsCodeSuggestions}
              placeholder="0901 - Coffee"
            />
          </div>

          {/* Total Price */}
          <div style={fieldStyle}>
            <label style={labelStyle}>
              <Package size={16} style={iconStyle} />
              Total Price - Product + Shipping (USD)
            </label>
            <input
              type="number"
              value={formData.totalPrice}
              onChange={(e) => setFormData(prev => ({ ...prev, totalPrice: e.target.value }))}
              step="0.01"
              min="0"
              placeholder="1250.00"
              style={inputStyle}
            />
          </div>

          {/* Start Location */}
          <div style={fieldStyle}>
            <label style={labelStyle}>
              <Navigation size={16} style={iconStyle} />
              Start Location
            </label>
            <AutocompleteInput
              value={formData.startLocation}
              onChange={(value) => setFormData(prev => ({ ...prev, startLocation: value }))}
              suggestions={cities}
              placeholder="São Paulo, SP, Brazil"
            />
          </div>

          {/* End Location */}
          <div style={fieldStyle}>
            <label style={labelStyle}>
              <Navigation size={16} style={iconStyle} />
              End Location
            </label>
            <AutocompleteInput
              value={formData.endLocation}
              onChange={(value) => setFormData(prev => ({ ...prev, endLocation: value }))}
              suggestions={cities}
              placeholder="New York, NY, USA"
            />
          </div>

          {/* Submit Button */}
          <button
            onClick={handleCalculate}
            style={buttonStyle}
            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#0052a3'}
            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#0066CC'}
          >
            Calculate Route
          </button>

          {/* Total Cost Display */}
          {formData.totalPrice && (
            <div style={totalBoxStyle}>
              <p style={totalLabelStyle}>Total Cost</p>
              <p style={totalValueStyle}>
                ${parseFloat(formData.totalPrice || 0).toFixed(2)}
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Right Side - Map (60%) */}
      <div style={rightPanelStyle}>
        <MapContainer
          center={[20, 0]}
          zoom={2}
          style={{ height: '100%', width: '100%' }}
          scrollWheelZoom={true}
        >
          <TileLayer
            attribution='&copy; OpenStreetMap'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          
          {coordinates.start && (
            <Marker position={coordinates.start}>
              <Popup>Start: {formData.startLocation}</Popup>
            </Marker>
          )}
          
          {coordinates.end && (
            <Marker position={coordinates.end}>
              <Popup>End: {formData.endLocation}</Popup>
            </Marker>
          )}

          {coordinates.start && coordinates.end && (
            <CurvedPath start={coordinates.start} end={coordinates.end} />
          )}
        </MapContainer>

        {/* Map Overlay Info */}
        <div style={overlayStyle}>
          <h3 style={overlayTitleStyle}>Route Information</h3>
          {coordinates.start && coordinates.end ? (
            <div>
              <p style={overlayTextStyle}>✓ Route calculated</p>
              <p style={{ ...overlayTextStyle, fontSize: '12px', marginTop: '4px' }}>
                Path shown with curved arc
              </p>
            </div>
          ) : (
            <p style={overlayTextStyle}>Enter locations to see route</p>
          )}
        </div>
      </div>
    </div>
  );
}