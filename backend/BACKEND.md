# Backend de Tarifas - API REST

Backend do sistema Project Dromos que fornece funcionalidades de consulta e cálculo de tarifas usando a API da WTO (World Trade Organization).

### 1. **Lista de Países** (`GET /api/tariffs/countries`)
- Retorna lista com código e nome dos países
- Usado pelo frontend para popular dropdowns e autocomplete

### 2. **Cálculo de Tarifas** (`POST /api/tariffs/calculate`)
- Calcula tarifas baseado em códigos HS e países
- Retorna tarifa detalhada e preço final

## Configuração

### application.properties
```properties
spring.application.name=tariff

# WTO API Configuration  
wto.api.key=${WTO_API_KEY}
wto.api.base-url=https://api.wto.org/timeseries/v1
```

### Variáveis de Ambiente
```bash
export WTO_API_KEY="sua_chave_da_api_wto"
```

## Como Executar

```bash
# Compilar e executar
./mvnw spring-boot:run

# Ou usando Maven diretamente
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/tariffs/countries` | Lista países da API WTO |
| POST | `/api/tariffs/calculate` | Calcula tarifas para transação |
