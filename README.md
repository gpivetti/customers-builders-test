
# Teste Builders - API de Clientes (Customers)

# Descrição

Este serviço foi desenvolvido com o objetivo de que seja possível buscar, salvar e alterar clientes (customers) através de uma API "userfriendly".

As seguintes rotas estão implementadas:

- **GET Customers**: Retorna todos os clientes de forma paginada
- **GET Customer**: Retorna um customer através de seu Id
- **POST Customer**: Insere um novo customer na base de dados
- **PUT Customer**: Atualizar as informações de um customers (obrigatório o parâmetro do customerId)
- **DELETE Customer**: Deleta um customer da base de dados (obrigatório o parâmetro do customerId)

### Observações

1. Na busca de clientes (GET Customers) um motor básico de filtro foi desenvolvido para que se possa filtrar os dados através dos campos que retornam na API. (Leia a documentação do Swagger para maiores informações).

2. A busca de clientes (GET Customers) é paginada, ou seja, retornará somente alguns itens da base. (Leia a documentação do Swagger para maiores informações).

3. Os endpoints de busca de cliente (GET Customer), atualização (PUT Customer) e deleção (DELETE Customer) retornarão um NotFound caso o Cliente do parâmetro (customerId) não exista.

4. A busca de um único cliente (GET Customer) através de seu Id, possui um cache com 1 min de TTL. Caso o cliente for removido e continuar a ser retornado pelo endpoint, aguarde alguns instantes que ele será removido (o mesmo vale para a atualização).

5. Na atualização do cliente (PUT Customer) é possível atualizar todos os dados do customer ou somente alguns.

6O campo "age" retornando pelas buscas de Clientes é calculado em tempos de execução. Sendo assim, o mesmo não será salvo no banco e nem utilizado como filter/sort nesta primeira versão. 

### Implementação:
- **Tecnologia:** Java 11
- **Database:** MongoDB
- **Cache:** Redis
- **Framework:** Spring Boot

### Requisitos

O único requisito para a execução da aplicação será o **Docker** e o **Docker Compose** para a geração dos containers.
<p>***Java11 e Maven***: Requisito para realizão dos testes localmente (testes integrados possuem um banco específico que será inicializado via docker-compose)</p>

### Documentação

Toda a API está documentada no swagger: http://localhost:8080/swagger.html
<p>Também possuímos um arquivo do Postman na raíz da aplicação (Gabriel - Builders Teste.postman_collection) contendo todas as apis do serviço.</p>

### Autenticação

Foi utilizada uma autenticação simples (Basic Auth), com os dados:
- **user**: builders
- **password**: test

# Sobre o Desenvolvimento

O desenvolvimento foi pautado seguindo os seguintes princípios:
- Princípios do SOLID para o desenvolvimento, principalmente tratando-se de Responsabilidade Única, Inversão de Dependência e Segregação de Interfaces.
- Clean Architecture para o desacoplamento das camadas (Services e Repositories).
- DDD para desenvolvimento pautado sobre o domínio "Customer".
- Uma ideia de arquitetura hexagonal, com as camadas de Apresentação (controllers), Domínios e Infra (Adapters/Repositories).
- Utilização de Testes para garantir uma maior qualidade do entregável. Infelizmente os testes de integração criados são básicos, devido ao tempo de entrega.

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

### Docker

Implementamos os seguintes serviços dentro do arquivo docker-compose, que serão utilizados para:
- **test-db**: Banco MongoDB para testes integrados.
- **db**: Banco MongoDB utilizado pela aplicação.
- **maven**: Container contendo o maven. Assim, não será necessário ter o maven instalado na máquina para o build da aplicação.
- **app**: Container principal da aplicação. (Após realizado o build pelo maven, subir este container)

<p>OBS 1: Nunca utilizar o docker-compose up, pois os serviços são indepentendes. Ao invés, utilizar **docker-container run {service}**</p>
<p>OBS 2: Caso queira, poderá refazer o serviços através de: **docker-compose build {service}**</p>

## Rodando a aplicação

Para a execução da aplicação, orientamos a utilização do Docker, no qual já irá executar tanto o Mongo quanto o Redis em conjunto:

```bash
$ docker-composer run app
```

O serviço "app" descrito no docker-compose, contém as dependência para: aplicação, banco de dados mongo e cache redis.

OBS: Por padrão a api irá inicializar na porta 8080.

# Testes

Os testes da aplicação estão divididos em: Unitários e Integração.

Para executá-los localmente, tenha o Maven, Docker e Java11 instalado na máquina, e execute os comandos abaixo. (Para integration tests, iremos subir um banco de testes antes)

```bash
# testes unitarios
$ mvn clean && mvn test

# testes de integração
$ docker-compose run test-db
$ mvn clean && mvn integration-test
```

### Problemas nos Testes

Caso qualquer problema de permissão aconteça durante a execução dos testes, favor remover o pasta "target" (build da aplicação), caso a mesma tenha sido gerada anteriormente pelo docker (maven):

```bash
# removendo o target gerado anteriormente
$ sudo rm -Rf target
$ mvn clean
```

OBS: Este serviço foi desenvolvido em um ambiente Linux Ubuntu, e estes problemas podem ocorrer

# Sobre o Autor
- Gabriel Pivetti
- **Email**: gapivetti@gmail.com
- **Mobile/Whatsapp**: (18) 991199597

<br>
Obrigado :)