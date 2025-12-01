export interface Country {
  code: string;
  name: string;
  iso3A?: string;
  displayOrder?: number;
}

export interface TariffRequestDto {
  hsCode: string;
  totalPrice: string;
  startLocationCode: string;
  endLocationCode: string;
}

export interface TariffDetail {
  tariffType: string;
  rate: number;
  amount: number;
  description: string;
}

export interface TariffCalculationResponse {
  hsCode: string;
  totalPrice: string;
  startLocationCode: string;
  endLocationCode: string;
  startLocationName: string;
  endLocationName: string;
  tariffDetails: TariffDetail[];
  totalTariff: number;
  finalPrice: number;
}