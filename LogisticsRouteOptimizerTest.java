package com.logistics.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Domain entities (you'll need to implement these)
enum TransportMode {
    SEA, AIR, LAND
}

class Route {
    private final String origin;
    private final String destination;
    private final TransportMode transportMode;
    private final double distanceKm;
    private final BigDecimal baseCostPerKg;
    private final int estimatedDays;

    public Route(String origin, String destination, TransportMode transportMode, 
                double distanceKm, BigDecimal baseCostPerKg, int estimatedDays) {
        this.origin = origin;
        this.destination = destination;
        this.transportMode = transportMode;
        this.distanceKm = distanceKm;
        this.baseCostPerKg = baseCostPerKg;
        this.estimatedDays = estimatedDays;
    }

    // Getters
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public TransportMode getTransportMode() { return transportMode; }
    public double getDistanceKm() { return distanceKm; }
    public BigDecimal getBaseCostPerKg() { return baseCostPerKg; }
    public int getEstimatedDays() { return estimatedDays; }
}

class TariffInfo {
    private final String countryCode;
    private final String productCategory;
    private final BigDecimal tariffRate; // percentage
    private final BigDecimal customsFee; // flat fee

    public TariffInfo(String countryCode, String productCategory, 
                     BigDecimal tariffRate, BigDecimal customsFee) {
        this.countryCode = countryCode;
        this.productCategory = productCategory;
        this.tariffRate = tariffRate;
        this.customsFee = customsFee;
    }

    // Getters
    public String getCountryCode() { return countryCode; }
    public String getProductCategory() { return productCategory; }
    public BigDecimal getTariffRate() { return tariffRate; }
    public BigDecimal getCustomsFee() { return customsFee; }
}

class OptimizedRoute {
    private final Route route;
    private final BigDecimal totalCost;
    private final int totalDays;
    private final BigDecimal tariffCost;
    private final BigDecimal shippingCost;

    public OptimizedRoute(Route route, BigDecimal totalCost, int totalDays, 
                         BigDecimal tariffCost, BigDecimal shippingCost) {
        this.route = route;
        this.totalCost = totalCost;
        this.totalDays = totalDays;
        this.tariffCost = tariffCost;
        this.shippingCost = shippingCost;
    }

    // Getters
    public Route getRoute() { return route; }
    public BigDecimal getTotalCost() { return totalCost; }
    public int getTotalDays() { return totalDays; }
    public BigDecimal getTariffCost() { return tariffCost; }
    public BigDecimal getShippingCost() { return shippingCost; }
}

// Service interfaces (you'll need to implement these)
interface TariffService {
    TariffInfo getTariffInfo(String origin, String destination, String productCategory);
}

interface RouteService {
    List<Route> getAvailableRoutes(String origin, String destination);
}

// Main class to be implemented (this is your TDD target)
class LogisticsRouteOptimizer {
    private final TariffService tariffService;
    private final RouteService routeService;

    public LogisticsRouteOptimizer(TariffService tariffService, RouteService routeService) {
        this.tariffService = tariffService;
        this.routeService = routeService;
    }

    public OptimizedRoute optimizeRoute(String origin, String destination, 
                                      double weightKg, String productCategory, 
                                      String priority) {
        // TODO: Implement this method to make the tests pass
        // This is where your TDD implementation will go
        throw new UnsupportedOperationException("Not implemented yet - implement to make tests pass!");
    }
}

// Exception classes
class NoRoutesAvailableException extends RuntimeException {
    public NoRoutesAvailableException(String message) {
        super(message);
    }
}

@ExtendWith(MockitoExtension.class)
class LogisticsRouteOptimizerTest {

    @Mock
    private TariffService mockTariffService;

    @Mock
    private RouteService mockRouteService;

    private LogisticsRouteOptimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = new LogisticsRouteOptimizer(mockTariffService, mockRouteService);
    }

    @Test
    void shouldOptimizeRoutePrioritizingCostWhenMultipleRoutesAvailable() {
        // Given - TDD: Write the test first, then implement the functionality
        String origin = "Shanghai";
        String destination = "Los Angeles";
        double weightKg = 1000.0;
        String productCategory = "electronics";
        String priority = "cost";

        // Mock available routes - sea route (cheaper) vs air route (faster)
        Route seaRoute = new Route(origin, destination, TransportMode.SEA, 
                                 11000.0, new BigDecimal("2.50"), 25);
        Route airRoute = new Route(origin, destination, TransportMode.AIR, 
                                 11000.0, new BigDecimal("8.00"), 3);
        List<Route> mockRoutes = Arrays.asList(seaRoute, airRoute);

        // Mock tariff information
        TariffInfo mockTariff = new TariffInfo("US", productCategory, 
                                             new BigDecimal("15.0"), // 15% tariff
                                             new BigDecimal("50.00")); // $50 customs fee

        // Configure mocks
        when(mockRouteService.getAvailableRoutes(origin, destination))
            .thenReturn(mockRoutes);
        when(mockTariffService.getTariffInfo(origin, destination, productCategory))
            .thenReturn(mockTariff);

        // When
        OptimizedRoute result = optimizer.optimizeRoute(origin, destination, 
                                                      weightKg, productCategory, priority);

        // Then
        // Verify service interactions
        verify(mockRouteService).getAvailableRoutes(origin, destination);
        verify(mockTariffService, times(2)).getTariffInfo(origin, destination, productCategory);

        // Verify sea route is selected (cost priority)
        assertEquals(TransportMode.SEA, result.getRoute().getTransportMode());
        assertEquals(25, result.getTotalDays());

        // Verify cost calculations
        // Sea route: 1000kg * $2.50 = $2500 shipping cost
        // Tariff: ($2500 * 15% + $50) = $375 + $50 = $425 tariff cost
        // Total: $2925
        BigDecimal expectedShippingCost = new BigDecimal("2500.00");
        BigDecimal expectedTariffCost = new BigDecimal("425.00");
        BigDecimal expectedTotalCost = new BigDecimal("2925.00");

        assertEquals(0, expectedShippingCost.compareTo(result.getShippingCost()));
        assertEquals(0, expectedTariffCost.compareTo(result.getTariffCost()));
        assertEquals(0, expectedTotalCost.compareTo(result.getTotalCost()));
    }

    @Test
    void shouldOptimizeRoutePrioritizingTimeWhenTimePrioritySpecified() {
        // Given
        String origin = "London";
        String destination = "Tokyo";
        double weightKg = 100.0;
        String productCategory = "pharmaceuticals";
        String priority = "time";

        // Mock routes with different time characteristics
        Route slowRoute = new Route(origin, destination, TransportMode.SEA, 
                                  20000.0, new BigDecimal("1.80"), 35);
        Route fastRoute = new Route(origin, destination, TransportMode.AIR, 
                                  9600.0, new BigDecimal("12.00"), 2);
        List<Route> mockRoutes = Arrays.asList(slowRoute, fastRoute);

        TariffInfo mockTariff = new TariffInfo("JP", productCategory, 
                                             new BigDecimal("8.0"), 
                                             new BigDecimal("25.00"));

        when(mockRouteService.getAvailableRoutes(origin, destination))
            .thenReturn(mockRoutes);
        when(mockTariffService.getTariffInfo(origin, destination, productCategory))
            .thenReturn(mockTariff);

        // When
        OptimizedRoute result = optimizer.optimizeRoute(origin, destination, 
                                                      weightKg, productCategory, priority);

        // Then
        assertEquals(TransportMode.AIR, result.getRoute().getTransportMode());
        assertEquals(2, result.getTotalDays());

        // Verify cost calculation for air route
        // 100kg * $12.00 = $1200 shipping
        // Tariff: ($1200 * 8% + $25) = $96 + $25 = $121
        // Total: $1321
        BigDecimal expectedShippingCost = new BigDecimal("1200.00");
        BigDecimal expectedTariffCost = new BigDecimal("121.00");
        BigDecimal expectedTotalCost = new BigDecimal("1321.00");

        assertEquals(0, expectedShippingCost.compareTo(result.getShippingCost()));
        assertEquals(0, expectedTariffCost.compareTo(result.getTariffCost()));
        assertEquals(0, expectedTotalCost.compareTo(result.getTotalCost()));
    }

    @Test
    void shouldThrowExceptionWhenNoRoutesAvailable() {
        // Given
        String origin = "InvalidCity";
        String destination = "AnotherInvalidCity";
        double weightKg = 500.0;
        String productCategory = "textiles";
        String priority = "cost";

        when(mockRouteService.getAvailableRoutes(origin, destination))
            .thenReturn(Collections.emptyList());

        // When & Then
        NoRoutesAvailableException exception = assertThrows(
            NoRoutesAvailableException.class,
            () -> optimizer.optimizeRoute(origin, destination, weightKg, productCategory, priority)
        );

        assertTrue(exception.getMessage().contains("No routes available"));
        
        // Verify service interactions
        verify(mockRouteService).getAvailableRoutes(origin, destination);
        verify(mockTariffService, never()).getTariffInfo(anyString(), anyString(), anyString());
    }

    @Test 
    void shouldHandleBalancedPriorityOptimization() {
        // Given
        String origin = "Hamburg";
        String destination = "New York";
        double weightKg = 750.0;
        String productCategory = "machinery";
        String priority = "balanced";

        // Create routes with different cost/time trade-offs
        Route economicalRoute = new Route(origin, destination, TransportMode.SEA, 
                                        6000.0, new BigDecimal("3.20"), 18);
        Route expressRoute = new Route(origin, destination, TransportMode.AIR, 
                                     6000.0, new BigDecimal("9.50"), 4);
        Route standardRoute = new Route(origin, destination, TransportMode.LAND, 
                                      6500.0, new BigDecimal("5.80"), 12);
        
        List<Route> mockRoutes = Arrays.asList(economicalRoute, expressRoute, standardRoute);

        TariffInfo mockTariff = new TariffInfo("US", productCategory, 
                                             new BigDecimal("12.0"), 
                                             new BigDecimal("75.00"));

        when(mockRouteService.getAvailableRoutes(origin, destination))
            .thenReturn(mockRoutes);
        when(mockTariffService.getTariffInfo(origin, destination, productCategory))
            .thenReturn(mockTariff);

        // When
        OptimizedRoute result = optimizer.optimizeRoute(origin, destination, 
                                                      weightKg, productCategory, priority);

        // Then
        // Should select the balanced option (likely the standard land route)
        // This test validates that balanced scoring works correctly
        assertNotNull(result);
        assertNotNull(result.getRoute());
        assertTrue(result.getTotalCost().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.getTotalDays() > 0);
        
        verify(mockRouteService).getAvailableRoutes(origin, destination);
        verify(mockTariffService, times(3)).getTariffInfo(origin, destination, productCategory);
    }
}
