import axios from 'axios';
import { Country, TariffRequestDto, TariffCalculationResponse } from './types';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const tariffApi = {
  // Get list of countries
  getCountries: async (): Promise<Country[]> => {
    const response = await api.get<Country[]>('/tariffs/countries');
    return response.data;
  },

  // Calculate tariff
  calculateTariff: async (request: TariffRequestDto): Promise<TariffCalculationResponse> => {
    const response = await api.post<TariffCalculationResponse>('/tariffs/calculate', request);
    return response.data;
  },
};