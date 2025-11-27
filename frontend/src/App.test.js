import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';

jest.mock('react-leaflet', () => ({
  MapContainer: ({ children }) => <div data-testid="map-container">{children}</div>,
  TileLayer: () => <div data-testid="tile-layer" />,
  Marker: ({ children }) => <div data-testid="marker">{children}</div>,
  Popup: ({ children }) => <div>{children}</div>,
  useMap: () => ({
    fitBounds: jest.fn(),
    eachLayer: jest.fn(),
    removeLayer: jest.fn(),
    addLayer: jest.fn(),
  }),
}));

jest.mock('leaflet', () => ({
  polyline: () => ({ addTo: jest.fn() }),
  latLngBounds: () => ({}),
  Icon: {
    Default: {
      prototype: { _getIconUrl: jest.fn() },
      mergeOptions: jest.fn(),
    },
  },
}));

jest.mock('lucide-react', () => ({
  Package: () => <span>IconPackage</span>,
  Navigation: () => <span>IconNavigation</span>,
  FileText: () => <span>IconFileText</span>,
}));

describe('Dromos Logistics Frontend Tests', () => {

  beforeEach(() => {
    jest.clearAllMocks();
    jest.spyOn(window, 'alert').mockImplementation(() => {}); // Silencia e monitora o window.alert
    jest.spyOn(console, 'log').mockImplementation(() => {});  // Silencia e monitora o console.log
  });

  test('deve renderizar o título e todos os campos do formulário', () => {
    render(<App />);

    expect(screen.getByText(/DROMOS/i)).toBeInTheDocument();

    expect(screen.getByPlaceholderText(/0901 - Coffee/i)).toBeInTheDocument(); // HS Code
    expect(screen.getByPlaceholderText(/1250.00/i)).toBeInTheDocument();       // Preço
    expect(screen.getByPlaceholderText(/São Paulo, SP, Brazil/i)).toBeInTheDocument(); // Origem
    expect(screen.getByPlaceholderText(/New York, NY, USA/i)).toBeInTheDocument();     // Destino

    expect(screen.getByRole('button', { name: /Calculate Route/i })).toBeInTheDocument();
  });

  test('deve permitir digitar nos campos', () => {
    render(<App />);

    const priceInput = screen.getByPlaceholderText(/1250.00/i);

    fireEvent.change(priceInput, { target: { value: '5000' } });

    expect(priceInput.value).toBe('5000');
  });

  test('deve mostrar um alerta se tentar calcular com campos vazios', () => {
    render(<App />);

    const button = screen.getByRole('button', { name: /Calculate Route/i });

    fireEvent.click(button);

    expect(window.alert).toHaveBeenCalledWith('Please fill in all fields');
  });

  test('deve calcular a rota quando preenchido corretamente com cidades válidas', async () => {
    render(<App />);

    const hsInput = screen.getByPlaceholderText(/0901 - Coffee/i);
    fireEvent.focus(hsInput);
    fireEvent.change(hsInput, { target: { value: '0901' } });

    const hsSuggestion = await screen.findByText(/0901 - Coffee/i);
    fireEvent.click(hsSuggestion);

    const priceInput = screen.getByPlaceholderText(/1250.00/i);
    fireEvent.change(priceInput, { target: { value: '2000' } });

    const startInput = screen.getByPlaceholderText(/São Paulo, SP, Brazil/i);
    fireEvent.focus(startInput);
    fireEvent.change(startInput, { target: { value: 'Rio' } });

    const startSuggestion = await screen.findByText(/Rio de Janeiro, RJ, Brazil/i);
    fireEvent.click(startSuggestion);

    const endInput = screen.getByPlaceholderText(/New York, NY, USA/i);
    fireEvent.focus(endInput);
    fireEvent.change(endInput, { target: { value: 'Tokyo' } });

    const endSuggestion = await screen.findByText(/Tokyo, Tokyo, Japan/i);
    fireEvent.click(endSuggestion);

    const button = screen.getByRole('button', { name: /Calculate Route/i });
    fireEvent.click(button);

    expect(window.alert).not.toHaveBeenCalled();
    expect(console.log).toHaveBeenCalledWith(
      expect.stringContaining('Form data to send to backend:'),
      expect.objectContaining({
        totalPrice: '2000',
        startLocation: 'Rio de Janeiro, RJ, Brazil',
        endLocation: 'Tokyo, Tokyo, Japan'
      })
    );
    expect(screen.getAllByTestId('marker')).toHaveLength(2);
  });

  test('deve mostrar alerta se a cidade não for reconhecida', () => {
    render(<App />);

    fireEvent.change(screen.getByPlaceholderText(/1250.00/i), { target: { value: '100' } });
    fireEvent.change(screen.getByPlaceholderText(/0901 - Coffee/i), { target: { value: 'Test Code' } });

    fireEvent.change(screen.getByPlaceholderText(/São Paulo, SP, Brazil/i), { target: { value: 'Cidade Fantasma' } });
    fireEvent.change(screen.getByPlaceholderText(/New York, NY, USA/i), { target: { value: 'Tokyo, Tokyo, Japan' } });

    fireEvent.click(screen.getByRole('button', { name: /Calculate Route/i }));

    expect(window.alert).toHaveBeenCalledWith(expect.stringContaining('Start location not recognized'));
  });
});