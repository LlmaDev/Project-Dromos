# Configuração dos Jobs Jenkins - Project Dromos Backend

Este documento descreve como configurar os três jobs do Jenkins para o backend do Project-Dromos.

## Pré-requisitos

### No Jenkins:
1. **Maven**: Configure uma instalação do Maven no Jenkins
   - Vá em: `Manage Jenkins > Global Tool Configuration > Maven`
   - Adicione uma instalação chamada `Maven-3.9.0`

2. **JDK**: Configure o JDK 17
   - Vá em: `Manage Jenkins > Global Tool Configuration > JDK`
   - Adicione uma instalação chamada `JDK-17`

3. **Plugins necessários**:
   - Pipeline
   - Email Extension Plugin
   - HTML Publisher Plugin
   - JUnit Plugin

### Configuração de Email:
1. Vá em `Manage Jenkins > Configure System`
2. Configure o `Extended E-mail Notification`:
   - SMTP Server: `smtp.gmail.com`
   - SMTP Port: `587`
   - Configure credenciais se necessário

## Jobs a Criar

### 1. Job de Build (dromos-backend-build)

**Tipo**: Pipeline

**Configuração**:
- Nome: `dromos-backend-build`
- Pipeline script from SCM
- SCM: Git
- Repository URL: [URL do seu repositório]
- Script Path: `backend/Jenkinsfile-build`

**Descrição**: Compila e empacota a aplicação Spring Boot

### 2. Job de Testes (dromos-backend-test)

**Tipo**: Pipeline

**Configuração**:
- Nome: `dromos-backend-test`
- Pipeline script from SCM
- SCM: Git
- Repository URL: [URL do seu repositório]
- Script Path: `backend/Jenkinsfile-test`

**Triggers**: 
- Pode ser configurado para executar após o job de build

**Descrição**: Executa todos os testes unitários e gera relatórios

### 3. Job de Notificação (dromos-backend-email)

**Tipo**: Pipeline

**Configuração**:
- Nome: `dromos-backend-email`
- Pipeline script from SCM
- SCM: Git
- Repository URL: [URL do seu repositório]
- Script Path: `backend/Jenkinsfile-email`

**Triggers**: 
- Pode ser configurado para executar após os jobs de build e test

**Variáveis de Ambiente a Configurar**:
- `EMAIL_FROM`: seu-email@gmail.com
- `EMAIL_TO`: destinatario@email.com

## Pipeline Sugerido

Você pode criar um pipeline que executa os jobs em sequência:

1. **Build** → Se sucesso → **Test** → **Email**
2. Se qualquer job falhar → **Email** (com notificação de falha)

### Pipeline Mestre (Opcional)

Crie um job adicional que orquestra todos os outros:

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                build job: 'dromos-backend-build', wait: true
            }
        }
        
        stage('Test') {
            steps {
                build job: 'dromos-backend-test', wait: true
            }
        }
        
        stage('Notify') {
            steps {
                build job: 'dromos-backend-email', wait: false
            }
        }
    }
    
    post {
        failure {
            build job: 'dromos-backend-email', wait: false
        }
    }
}
```

## Personalização

### Modificar Configurações de Email:
Edite o arquivo `Jenkinsfile-email` e altere as variáveis:
- `EMAIL_SMTP_SERVER`
- `EMAIL_SMTP_PORT`
- `EMAIL_FROM`
- `EMAIL_TO`

### Adicionar Mais Testes:
No `Jenkinsfile-test`, você pode adicionar:
- Testes de integração
- Análise de código (SonarQube)
- Testes de performance

### Personalizar Build:
No `Jenkinsfile-build`, você pode adicionar:
- Deploy automático
- Docker build
- Análise de vulnerabilidades

## Troubleshooting

### Problema com Maven:
- Verifique se o nome da instalação do Maven corresponde ao configurado
- Teste executando `mvn --version` no terminal do Jenkins

### Problema com Email:
- Verifique as credenciais do SMTP
- Teste a conectividade com o servidor de email
- Verifique se o plugin Email Extension está instalado

### Problema com Testes:
- Verifique se todos os testes passam localmente
- Verifique os logs detalhados no console do Jenkins

## Estrutura dos Arquivos

```
backend/
├── Jenkinsfile-build    # Job de build
├── Jenkinsfile-test     # Job de testes
├── Jenkinsfile-email    # Job de notificação
└── JENKINS-SETUP.md     # Este arquivo
```

## Próximos Passos

1. Configure os pré-requisitos no Jenkins
2. Crie os três jobs conforme descrito
3. Teste cada job individualmente
4. Configure o pipeline mestre (opcional)
5. Personalize conforme suas necessidades