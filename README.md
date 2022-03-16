
# Teste Builders - API de Clientes (Customers)

# Descrição

Este serviço foi desenvolvido com o objetivo de que seja possível buscar, salvar e alterar clientes (customers) através de uma API "userfriendly".

As seguintes rotas estão implementadas:

- **GET customer**: Retorna todos os clientes de forma paginada
- **GET customer**: Retorna um customer através de seu Id
- **POST customer**: Insere um novo customer na base de dados
- **PUT customer**: Atualizar as informações de um customers
- **DELETE customer**: Deleta um customer da base de dados

## Observações

1. Na busca de Clientes (GET customer) um motor básico de filtro foi desenvolvido para que se possa filtrar os dados através dos campos que retornam na API. (Leia a documentação do Swagger para maiores informações).

2. A busca de Clientes (GET customer) é paginada, ou seja, retornará somente alguns itens da base. (Leia a documentação do Swagger para maiores informações).

3. A busca de Client (GET customer) retornará um NotFound caso o Cliente não exista.

4. Na atualização do cliente (PUT customer) é possível atualizar todos os dados do customer, ou somente alguns.

## Implementação:
- **Tecnologia:** Java 11
- **Database:** MongoDB
- **Cache:** Redis
- **Framework:** Spring Boot

## Requisitos

O único requisito será o Docker e o Docker Compose para a geração dos containers e execução da aplicação.

## Swagger

Toda a API está documentada no swagger: http://localhost:8080/swagger.html

## Autenticação

Foi utilizada uma autenticação simples (Basic Auth), com os dados:
- **user**: builders
- **password**: test

## Sobre o Autor
- Gabriel Pivetti
- Email: gapivetti@gmail.com
- Mobile/Whatsapp: (18) 991199597

# Sobre o Desenvolvimento

O desenvolvimento foi pautado seguindo os seguintes princípios:
- Princípios do SOLID para o desenvolvimento, principalmente tratando-se de Responsabilidade Única, Inversão de Dependência e Segregação de Interfaces.
- Clean Architecture para o desacoplamento das camadas (Services e Repositories).
- DDD para desenvolvimento pautado sobre o domínio "Customer".
- Uma ideia de arquitetura hexagonal, com as camadas de Apresentação (controllers), Domínios e Infra (Adapters/Repositories).
- Utilização de Testes para garantir uma maior qualidade do entregável. Infelizmente os testes de integração criados são básicos, devido ao tempo de entrega.

## Docker

Implementamos os seguintes serviços dentro do arquivo docker-compose, e que serão utilizados para:
- **test-db**: Banco MongoDB para testes integrados.
- **db**: Banco MongoDB utilizado pela aplicação.
- **maven**: Container contendo o maven. Assim, não será necessário ter o maven instalado na máquina para o build da aplicação.
- **app**: Container principal da aplicação. (Após realizado o build pelo maven, subir este container)

OBS: Nunca utilizar o docker-compose up, pois os serviços são indepentendes. Ao invés, utilizar "docker-container run [service]";

# Instalação
O serviço está totalente empacotado sobre o Docker, sendo este o único requisito para sua instalação.

Inicialmente iremos executar o build da aplicação (utilizaremos o maven para a criação do .jar)

```bash
$ docker-compose run maven
```

Porém, caso queira realizar o build localmente com o maven instalado, execute:
```bash
$ mvn clean && mvn package
```

## Rodando a aplicação

Para a execução da aplicação, orientamos a utilização do Docker, no qual já irá executar tanto o Mongo quanto o Redis em conjunto:

```bash
$ docker-composer run app
```

O serviço "app" descrito no docker-compose, contém as dependência para: aplicação, banco de dados mongo e cache redis.

# Testes

Os testes da aplicação estão divididos em: Unitários e Integração.

Para executá-los, execute os comandos abaixo. (Para integration tests, iremos subir um banco de testes antes)

```bash
# testes unitarios
$ mvn clean && mvn test

# testes de integração
$ docker-compose run test-db
$ mvn clean && mvn integration-test
```

<br>
Obrigado :)