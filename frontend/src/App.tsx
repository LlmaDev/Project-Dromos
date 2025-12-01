import React, { useState, useEffect } from 'react';
import { tariffApi } from './api';
import { Country, TariffRequestDto, TariffCalculationResponse } from './types';
import { Package, Navigation, FileText, ArrowLeft, MapPin, DollarSign } from 'lucide-react';

// Common HS Codes for autocomplete
const hsCodes = [
  { code: '0901', description: 'Coffee' },
  { code: '0801', description: 'Coconuts, Brazil nuts and cashew nuts' },
  { code: '1001', description: 'Wheat and meslin' },
  { code: '1701', description: 'Cane or beet sugar' },
  { code: '3004', description: 'Medicaments' },
  { code: '4011', description: 'New pneumatic tyres, of rubber' },
  { code: '6109', description: 'T-shirts, singlets and other vests' },
  { code: '8471', description: 'Automatic data processing machines' },
  { code: '8517', description: 'Telephone sets, smartphones' },
  { code: '8703', description: 'Motor cars and vehicles' },
  { code: '9018', description: 'Medical, surgical instruments' },
];

// Autocomplete Input Component
interface AutocompleteInputProps {
  value: string;
  onChange: (value: string) => void;
  suggestions: string[];
  placeholder: string;
}

const AutocompleteInput: React.FC<AutocompleteInputProps> = ({ 
  value, 
  onChange, 
  suggestions, 
  placeholder 
}) => {
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [filteredSuggestions, setFilteredSuggestions] = useState<string[]>([]);

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

  return (
    <div style={{ position: 'relative' }}>
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        onFocus={() => setShowSuggestions(true)}
        onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
        placeholder={placeholder}
        style={{
          width: '100%',
          padding: '12px 16px',
          border: '1px solid #cbd5e1',
          borderRadius: '8px',
          fontSize: '14px',
          outline: 'none',
          transition: 'all 0.2s',
        }}
      />
      {showSuggestions && filteredSuggestions.length > 0 && (
        <div style={{
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
        }}>
          {filteredSuggestions.map((suggestion, index) => (
            <div
              key={index}
              onClick={() => {
                onChange(suggestion);
                setShowSuggestions(false);
              }}
              style={{
                padding: '8px 16px',
                cursor: 'pointer',
                fontSize: '14px',
              }}
              onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = '#eff6ff')}
              onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = 'white')}
            >
              {suggestion}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

function App() {
  const [page, setPage] = useState<'form' | 'results'>('form');
  const [countries, setCountries] = useState<Country[]>([]);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<TariffCalculationResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [expandedDetail, setExpandedDetail] = useState<number | null>(null);
  
  const [formData, setFormData] = useState<TariffRequestDto>({
    hsCode: '',
    totalPrice: '',
    startLocationCode: '',
    endLocationCode: ''
  });

  // Load countries on component mount
  useEffect(() => {
    const loadCountries = async () => {
      try {
        const countriesData = await tariffApi.getCountries();
        setCountries(countriesData);
      } catch (err) {
        console.error('Error loading countries:', err);
        setError('Error loading countries list');
      }
    };
    
    loadCountries();
  }, []);

  const handleInputChange = (field: keyof TariffRequestDto, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleCalculate = async () => {
    if (!formData.hsCode || !formData.totalPrice || 
        !formData.startLocationCode || !formData.endLocationCode) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      // Call backend API
      const response = await tariffApi.calculateTariff(formData);
      setResult(response);
      setPage('results');
    } catch (err) {
      console.error('Error calculating tariff:', err);
      setError('Error calculating tariff. Please check your data and try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    setPage('form');
    setResult(null);
    setError(null);
    setExpandedDetail(null);
  };

  const hsCodeSuggestions = hsCodes.map(hs => `${hs.code} - ${hs.description}`);

  const getCountryName = (code: string): string => {
    const country = countries.find(c => c.code === code);
    return country?.name || code;
  };

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f8fafc',
      fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
    }}>
      {/* Header */}
      <div style={{
        backgroundColor: 'white',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        padding: '24px 32px',
      }}>
        <svg width="240" height="84" viewBox="0 0 400 140" xmlns="http://www.w3.org/2000/svg">
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
      </div>

      {/* Main Content */}
      <div style={{
        maxWidth: '1200px',
        margin: '0 auto',
        padding: '32px',
      }}>
        {page === 'form' ? (
          // FORM PAGE
          <div style={{
            backgroundColor: 'white',
            borderRadius: '12px',
            boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
            padding: '40px',
            maxWidth: '600px',
            margin: '0 auto',
          }}>
            <h2 style={{
              fontSize: '24px',
              fontWeight: '600',
              color: '#1e293b',
              marginBottom: '24px',
              textAlign: 'center',
            }}>
              International Tariff Calculator
            </h2>

            {/* HS Code Field */}
            <div style={{ marginBottom: '24px' }}>
              <label style={{
                display: 'flex',
                alignItems: 'center',
                fontSize: '14px',
                fontWeight: '500',
                color: '#334155',
                marginBottom: '8px',
              }}>
                <FileText size={16} style={{ marginRight: '8px', color: '#0066CC' }} />
                HS Code
              </label>
              <AutocompleteInput
                value={formData.hsCode}
                onChange={(value) => handleInputChange('hsCode', value.split(' - ')[0])}
                suggestions={hsCodeSuggestions}
                placeholder="0901 - Coffee"
              />
            </div>

            {/* Total Price Field */}
            <div style={{ marginBottom: '24px' }}>
              <label style={{
                display: 'flex',
                alignItems: 'center',
                fontSize: '14px',
                fontWeight: '500',
                color: '#334155',
                marginBottom: '8px',
              }}>
                <DollarSign size={16} style={{ marginRight: '8px', color: '#0066CC' }} />
                Product Value (USD)
              </label>
              <input
                type="number"
                value={formData.totalPrice}
                onChange={(e) => handleInputChange('totalPrice', e.target.value)}
                step="0.01"
                min="0"
                placeholder="1250.00"
                style={{
                  width: '100%',
                  padding: '12px 16px',
                  border: '1px solid #cbd5e1',
                  borderRadius: '8px',
                  fontSize: '14px',
                  outline: 'none',
                  transition: 'all 0.2s',
                }}
              />
            </div>

            {/* Start Location Field */}
            <div style={{ marginBottom: '24px' }}>
              <label style={{
                display: 'flex',
                alignItems: 'center',
                fontSize: '14px',
                fontWeight: '500',
                color: '#334155',
                marginBottom: '8px',
              }}>
                <MapPin size={16} style={{ marginRight: '8px', color: '#0066CC' }} />
                Origin Country
              </label>
              <select
                value={formData.startLocationCode}
                onChange={(e) => handleInputChange('startLocationCode', e.target.value)}
                style={{
                  width: '100%',
                  padding: '12px 16px',
                  border: '1px solid #cbd5e1',
                  borderRadius: '8px',
                  fontSize: '14px',
                  outline: 'none',
                  transition: 'all 0.2s',
                  backgroundColor: 'white',
                }}
              >
                <option value="">Select origin country</option>
                {countries.map((country) => (
                  <option key={country.code} value={country.code}>
                    {country.name} ({country.code})
                  </option>
                ))}
              </select>
            </div>

            {/* End Location Field */}
            <div style={{ marginBottom: '32px' }}>
              <label style={{
                display: 'flex',
                alignItems: 'center',
                fontSize: '14px',
                fontWeight: '500',
                color: '#334155',
                marginBottom: '8px',
              }}>
                <Navigation size={16} style={{ marginRight: '8px', color: '#0066CC' }} />
                Destination Country
              </label>
              <select
                value={formData.endLocationCode}
                onChange={(e) => handleInputChange('endLocationCode', e.target.value)}
                style={{
                  width: '100%',
                  padding: '12px 16px',
                  border: '1px solid #cbd5e1',
                  borderRadius: '8px',
                  fontSize: '14px',
                  outline: 'none',
                  transition: 'all 0.2s',
                  backgroundColor: 'white',
                }}
              >
                <option value="">Select destination country</option>
                {countries.map((country) => (
                  <option key={country.code} value={country.code}>
                    {country.name} ({country.code})
                  </option>
                ))}
              </select>
            </div>

            {/* Submit Button */}
            <button
              onClick={handleCalculate}
              disabled={loading}
              style={{
                width: '100%',
                backgroundColor: loading ? '#94a3b8' : '#0066CC',
                color: 'white',
                fontWeight: '500',
                padding: '14px 24px',
                borderRadius: '8px',
                border: 'none',
                cursor: loading ? 'not-allowed' : 'pointer',
                fontSize: '16px',
                transition: 'all 0.2s',
                boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
              }}
              onMouseEnter={(e) => {
                if (!loading) e.currentTarget.style.backgroundColor = '#0052a3';
              }}
              onMouseLeave={(e) => {
                if (!loading) e.currentTarget.style.backgroundColor = '#0066CC';
              }}
            >
              {loading ? 'Calculating...' : 'Calculate Tariff'}
            </button>

            {/* Error Message */}
            {error && (
              <div style={{
                marginTop: '16px',
                padding: '12px',
                backgroundColor: '#fee2e2',
                border: '1px solid #fecaca',
                borderRadius: '8px',
                color: '#991b1b',
                fontSize: '14px',
              }}>
                {error}
              </div>
            )}
          </div>
        ) : (
          // RESULTS PAGE
          <div>
            {/* Back Button */}
            <button
              onClick={handleBack}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                padding: '10px 20px',
                backgroundColor: 'white',
                border: '1px solid #cbd5e1',
                borderRadius: '8px',
                cursor: 'pointer',
                fontSize: '14px',
                color: '#64748b',
                marginBottom: '24px',
                transition: 'all 0.2s',
                boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.backgroundColor = '#f8fafc';
                e.currentTarget.style.borderColor = '#0066CC';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.backgroundColor = 'white';
                e.currentTarget.style.borderColor = '#cbd5e1';
              }}
            >
              <ArrowLeft size={16} />
              New Search
            </button>

            {/* Route Summary Card */}
            <div style={{
              backgroundColor: 'white',
              borderRadius: '12px',
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
              padding: '24px',
              marginBottom: '24px',
            }}>
              <div style={{ fontSize: '14px', color: '#64748b', marginBottom: '8px' }}>
                Shipping Route
              </div>
              <div style={{ 
                fontSize: '20px', 
                fontWeight: '600', 
                color: '#1e293b',
                marginBottom: '16px',
              }}>
                {getCountryName(formData.startLocationCode)} → {getCountryName(formData.endLocationCode)}
              </div>
              <div style={{ 
                display: 'grid', 
                gridTemplateColumns: 'repeat(2, 1fr)', 
                gap: '16px',
                fontSize: '14px',
              }}>
                <div>
                  <span style={{ color: '#64748b' }}>HS Code: </span>
                  <span style={{ fontWeight: '500', color: '#1e293b' }}>{formData.hsCode}</span>
                </div>
                <div>
                  <span style={{ color: '#64748b' }}>Product Value: </span>
                  <span style={{ fontWeight: '500', color: '#1e293b' }}>
                    ${parseFloat(formData.totalPrice).toFixed(2)}
                  </span>
                </div>
              </div>
            </div>

            {/* Results Display */}
            {result && (
              <>
                {/* Tariff Details - Expandable Cards */}
                {result.tariffDetails && result.tariffDetails.length > 0 && (
                  <div style={{ marginBottom: '24px' }}>
                    <h3 style={{
                      fontSize: '18px',
                      fontWeight: '600',
                      color: '#1e293b',
                      marginBottom: '16px',
                    }}>
                      Tariff Breakdown
                    </h3>
                    
                    {result.tariffDetails.map((detail, index) => {
                      const isExpanded = expandedDetail === index;
                      
                      return (
                        <div
                          key={index}
                          onClick={() => setExpandedDetail(isExpanded ? null : index)}
                          style={{
                            backgroundColor: 'white',
                            border: isExpanded ? '2px solid #0066CC' : '1px solid #e2e8f0',
                            borderRadius: '12px',
                            padding: '20px',
                            marginBottom: '12px',
                            cursor: 'pointer',
                            transition: 'all 0.2s',
                            boxShadow: isExpanded ? '0 4px 12px rgba(0,102,204,0.1)' : '0 2px 4px rgba(0,0,0,0.05)',
                          }}
                          onMouseEnter={(e) => {
                            if (!isExpanded) {
                              e.currentTarget.style.borderColor = '#0066CC';
                              e.currentTarget.style.boxShadow = '0 4px 8px rgba(0,0,0,0.1)';
                            }
                          }}
                          onMouseLeave={(e) => {
                            if (!isExpanded) {
                              e.currentTarget.style.borderColor = '#e2e8f0';
                              e.currentTarget.style.boxShadow = '0 2px 4px rgba(0,0,0,0.05)';
                            }
                          }}
                        >
                          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div style={{ flex: 1 }}>
                              <div style={{ 
                                fontWeight: '600', 
                                fontSize: '16px', 
                                color: '#1e293b',
                                marginBottom: '4px',
                              }}>
                                {detail.tariffType}
                              </div>
                              <div style={{ fontSize: '13px', color: '#64748b' }}>
                                Rate: {detail.rate}%
                              </div>
                            </div>
                            <div style={{ textAlign: 'right' }}>
                              <div style={{ 
                                fontSize: '24px', 
                                fontWeight: 'bold', 
                                color: '#0066CC',
                              }}>
                                ${detail.amount.toFixed(2)}
                              </div>
                            </div>
                          </div>
                          
                          {/* Expanded Content */}
                          {isExpanded && (
                            <div style={{
                              marginTop: '16px',
                              paddingTop: '16px',
                              borderTop: '1px solid #e2e8f0',
                            }}>
                              <div style={{ fontSize: '14px', color: '#64748b', lineHeight: '1.6' }}>
                                <strong style={{ color: '#1e293b' }}>Description:</strong>
                                <p style={{ marginTop: '8px' }}>{detail.description}</p>
                              </div>
                              <div style={{ 
                                marginTop: '12px',
                                padding: '12px',
                                backgroundColor: '#f8fafc',
                                borderRadius: '6px',
                                fontSize: '13px',
                              }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                                  <span style={{ color: '#64748b' }}>Base Amount:</span>
                                  <span style={{ fontWeight: '500' }}>
                                    ${parseFloat(formData.totalPrice).toFixed(2)}
                                  </span>
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                  <span style={{ color: '#64748b' }}>Applied Rate:</span>
                                  <span style={{ fontWeight: '500' }}>{detail.rate}%</span>
                                </div>
                              </div>
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </div>
                )}

                {/* Total Summary */}
                <div style={{
                  backgroundColor: 'white',
                  borderRadius: '12px',
                  boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                  padding: '24px',
                  border: '2px solid #0066CC',
                }}>
                  <h3 style={{
                    fontSize: '18px',
                    fontWeight: '600',
                    color: '#1e293b',
                    marginBottom: '16px',
                  }}>
                    Cost Summary
                  </h3>
                  
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                    <span style={{ color: '#64748b', fontSize: '14px' }}>Product Value:</span>
                    <span style={{ fontWeight: '500', fontSize: '14px' }}>
                      ${result.totalPrice.toFixed(2)}
                    </span>
                  </div>
                  
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '16px' }}>
                    <span style={{ color: '#64748b', fontSize: '14px' }}>Total Tariffs & Duties:</span>
                    <span style={{ fontWeight: '500', fontSize: '14px', color: '#d97706' }}>
                      ${result.totalTariff?.toFixed(2) || '0.00'}
                    </span>
                  </div>
                  
                  <div style={{
                    paddingTop: '16px',
                    borderTop: '2px solid #e2e8f0',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}>
                    <span style={{ fontWeight: '600', fontSize: '18px', color: '#1e293b' }}>
                      Final Price:
                    </span>
                    <span style={{ fontWeight: 'bold', fontSize: '32px', color: '#0066CC' }}>
                      ${result.finalPrice?.toFixed(2) || '0.00'}
                    </span>
                  </div>
                  
                  <div style={{
                    marginTop: '16px',
                    padding: '12px',
                    backgroundColor: '#fef3c7',
                    borderRadius: '8px',
                    fontSize: '13px',
                    color: '#92400e',
                  }}>
                    ℹ️ This is an estimate. Actual costs may vary based on specific circumstances and regulations.
                  </div>
                </div>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;