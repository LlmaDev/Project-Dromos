import React, { useState, useEffect } from 'react';
import { tariffApi } from './api';
import { Country, TariffRequestDto, TariffCalculationResponse } from './types';

function App() {
  const [countries, setCountries] = useState<Country[]>([]);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<TariffCalculationResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  
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
        setError('Erro ao carregar lista de países');
      }
    };
    
    loadCountries();
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await tariffApi.calculateTariff(formData);
      setResult(response);
    } catch (err) {
      console.error('Error calculating tariff:', err);
      setError('Erro ao calcular tarifa. Verifique os dados e tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">
            Calculadora de Tarifas Dromos
          </h1>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* HS Code */}
              <div>
                <label htmlFor="hsCode" className="block text-sm font-medium text-gray-700 mb-2">
                  Código HS do Produto
                </label>
                <input
                  type="text"
                  id="hsCode"
                  name="hsCode"
                  value={formData.hsCode}
                  onChange={handleInputChange}
                  placeholder="ex: 0901"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <p className="text-xs text-gray-500 mt-1">
                  Consulte códigos HS em: 
                  <a 
                    href="https://www.foreign-trade.com/reference/hscode.htm" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-blue-600 hover:underline ml-1"
                  >
                    foreign-trade.com/reference/hscode.htm
                  </a>
                </p>
              </div>

              {/* Total Price */}
              <div>
                <label htmlFor="totalPrice" className="block text-sm font-medium text-gray-700 mb-2">
                  Preço Total (USD)
                </label>
                <input
                  type="number"
                  id="totalPrice"
                  name="totalPrice"
                  value={formData.totalPrice}
                  onChange={handleInputChange}
                  placeholder="200"
                  step="0.01"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>

              {/* Start Location */}
              <div>
                <label htmlFor="startLocationCode" className="block text-sm font-medium text-gray-700 mb-2">
                  País de Origem
                </label>
                <select
                  id="startLocationCode"
                  name="startLocationCode"
                  value={formData.startLocationCode}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                >
                  <option value="">Selecione o país de origem</option>
                  {countries.map((country) => (
                    <option key={country.code} value={country.code}>
                      {country.name} ({country.code})
                    </option>
                  ))}
                </select>
              </div>

              {/* End Location */}
              <div>
                <label htmlFor="endLocationCode" className="block text-sm font-medium text-gray-700 mb-2">
                  País de Destino
                </label>
                <select
                  id="endLocationCode"
                  name="endLocationCode"
                  value={formData.endLocationCode}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                >
                  <option value="">Selecione o país de destino</option>
                  {countries.map((country) => (
                    <option key={country.code} value={country.code}>
                      {country.name} ({country.code})
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="text-center">
              <button
                type="submit"
                disabled={loading}
                className="bg-blue-600 text-white px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Calculando...' : 'Calcular Tarifa'}
              </button>
            </div>
          </form>

          {/* Error Message */}
          {error && (
            <div className="mt-6 p-4 bg-red-50 border border-red-200 rounded-md">
              <p className="text-red-600">{error}</p>
            </div>
          )}

          {/* Results */}
          {result && (
            <div className="mt-8 bg-gray-50 rounded-lg p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Resultado do Cálculo</h2>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                <div className="bg-white p-4 rounded-lg shadow">
                  <h3 className="font-semibold text-gray-700">Produto</h3>
                  <p className="text-lg">Código HS: {result.hsCode}</p>
                </div>
                
                <div className="bg-white p-4 rounded-lg shadow">
                  <h3 className="font-semibold text-gray-700">Preço Original</h3>
                  <p className="text-lg">${result.totalPrice}</p>
                </div>
                
                <div className="bg-white p-4 rounded-lg shadow">
                  <h3 className="font-semibold text-gray-700">Origem</h3>
                  <p className="text-lg">{result.startLocationName || result.startLocationCode}</p>
                </div>
                
                <div className="bg-white p-4 rounded-lg shadow">
                  <h3 className="font-semibold text-gray-700">Destino</h3>
                  <p className="text-lg">{result.endLocationName || result.endLocationCode}</p>
                </div>
              </div>

              {/* Tariff Details */}
              {result.tariffDetails && Array.isArray(result.tariffDetails) && result.tariffDetails.length > 0 && (
                <div className="mb-6">
                  <h3 className="text-xl font-semibold text-gray-900 mb-3">Detalhes das Tarifas</h3>
                  <div className="bg-white rounded-lg shadow overflow-hidden">
                    <table className="min-w-full">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Tipo
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Taxa
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Valor
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Descrição
                          </th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {result.tariffDetails?.map((detail, index) => (
                          <tr key={index}>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                              {detail.tariffType || 'N/A'}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                              {detail.rate || 0}%
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                              ${(detail.amount || 0).toFixed(2)}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-500">
                              {detail.description || 'N/A'}
                            </td>
                          </tr>
                        )) || []}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}

              {/* Final Results */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="bg-yellow-50 p-4 rounded-lg shadow border border-yellow-200">
                  <h3 className="font-semibold text-yellow-800">Total de Tarifas</h3>
                  <p className="text-2xl font-bold text-yellow-900">${result.totalTariff?.toFixed(2) || '0.00'}</p>
                </div>
                
                <div className="bg-green-50 p-4 rounded-lg shadow border border-green-200">
                  <h3 className="font-semibold text-green-800">Preço Final</h3>
                  <p className="text-2xl font-bold text-green-900">${result.finalPrice?.toFixed(2) || '0.00'}</p>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;