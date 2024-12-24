# Simulador de Crédito

## Instruções de setup
Este projeto foi desenvolvido na linguagem de programação Kotlin na IDE IntelliJ IDEA Community.
Guia de instalação do Redis para Windows: https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/install-redis-on-windows/

## Exemplos de requisições para os endpoints.
URL - http://localhost:8080/loans/simulations
Método - POST
Body - {
    "loanAmount": "10000.00", 
    "birthDate": "1990-06-10",  
    "paymentTermInMonths": 24
}
Response - {
    "totalPayment": "10315.44",
    "monthlyInstallment": "429.81",
    "totalInterest": "315.44"
}

URL - http://localhost:8080/loans/simulations/batch
Método - POST
Body - [{
"loanAmount": "10000.00",
"birthDate": "1990-06-10",  
"paymentTermInMonths": 24
}]
Response - [{
"totalPayment": "10315.44",
"monthlyInstallment": "429.81",
"totalInterest": "315.44"
}]

## Estrutura do projeto e Arquitetura
Kotlin foi a linguagem escolhida pelos seus paradigmas não funcionais e interoperabilidade com o Java.
Ktor foi o framework escolhido, por sua flexibilidade, leveza, desempenho ao trabalhar com alta concorrência e escalabilidade.
- PostgreSQL: Banco relacional, robusto e com bom suporte a consultas complexas.
- Redis: Minimiza latências em dados acessados com alta frequência.
- Docker: Promove a IaC com consistência, portabilidade e eficiência no gerenciamento de ambiente
- Kafka: Processa grandes volumes de mensagens, com alta disponibilidade e particionamento para escalabilidade.
- Lettuce: Biblioteca para manipulação de cache com o Redis
- ContentNegotiation: Suporte a serialização e deserialização automática
- Koin: Injeção de dependência para melhorar a testabilidade e desacoplamento
- CallLogging: Facilitar a auditoria e monitoramento
- SLF4: Logs mais limpos e consistentes 
- LogBack: Logging muito flexível e configurável de alta performance
- Routing: Definição e organização das rotas dos microserviços 
- Exposed: ORM flexível para bancos de dados relacionais, suporte a transação e segurança contra SQL Injection 
- Detekt e Ktlint: Linters para análise estática de código 
- Eureka: Serviço de descoberta de microsserviços 
- Hikari: Gerenciamento de conexões rápido e eficiente 
- Flyway: Controle de versão e migrações do banco de dados 
- Swagger: Documentação das APIs 
- StatusPages: Plugin para estruturar melhor as mensagens de retorno das requisições

## Features
Cálculo de parcelas para empréstimo com taxas definidas por faixa etária.

## Building & Running

Os linters podem ser executados assim:
gradlew ktlintCheck
gradlew ktlintFormat
gradlew detekt

Pode executar o microserviço com o comando `gradle run`
