# Cron expressions com Spring

App usando spring boot para testar cron expressions usando `@Scheduled`

## Cron Expressions

### Campos e Valores

| Campo          | Valores              | Caracteres especiais   |
|----------------|----------------------|------------------------|
| Segundos       | 0-59                 | ,, -, *, /             |
| Minutos        | 0-59                 | ,, -, *, /             |
| Horas          | 0-23                 | ,, -, *, /             |
| Dias do Mês    | 1-31                 | ,, -, *, ?, /, L, W, C |
| Mês            | 0-11 ou JAN-DEC      | ,, -, *, /             |
| Dia da Semana  | 1-7 ou SUN-DEC       | ,, -, *, ?, /, L, C, # |
| Ano (Opcional) | Vazio para 1970-2099 | ,, -, *, /             |

### Caracteres especiais da expressão

- `-` especifica que o evento deve acontecer para cada unidade de tempo. Por exemplo, “*” no campo <minuto> significa
  “para cada minuto”.
- `?` é usado nos campos <day-of-month> e <day-of -week> para denotar o valor arbitrário e, portanto, ignorar o valor do
  campo. Por exemplo, se quisermos acionar um script no “5º dia de cada mês”, independentemente do dia da semana nessa
  data, especificamos um “?” no campo <day-of-week>.
- `-` determina o intervalo de valores. Por exemplo, “10-11” no campo <hour> significa “10ª e 11ª horas”.
- `,` especifica vários valores. Por exemplo, “SEG, QUA, SEX” no campo <dia da semana> significa nos dias “Segunda,
  Quarta e Sexta.”
- `/` especifica os valores incrementais. Por exemplo, um “5/15” no campo <minuto> significa “5, 20, 35 e 50 minutos de
  uma hora”.
- `L` tem significados diferentes quando usado em vários campos. Por exemplo, se for aplicado no campo <dia-do-mês>,
  significa o último dia do mês, ou seja, “31 de janeiro” e assim sucessivamente de acordo com o mês do calendário. Ele
  pode ser usado com um valor de deslocamento como "L-3", que denota o "terceiro ao último dia do mês calendário". Em <
  day-of-week>, especifica o “último dia da semana”. Também pode ser usado com outro valor em <dia-da-semana>, como
  “6L”, que denota “última sexta-feira”.
- `W` determina o dia da semana (segunda a sexta) mais próximo de um determinado dia do mês. Por exemplo, se
  especificarmos “10W” no campo <dia do mês>, significa o “dia da semana próximo ao dia 10 deste mês”. Portanto, se "10"
  for um sábado, o trabalho será acionado às "9" e se "10" for um domingo, ele será acionado às "11". Se especificarmos
  “1W” em <dia do mês> e se “1º” for sábado, o trabalho será acionado no dia “3º”, que é segunda-feira, e não voltará ao
  mês anterior.
- `#` especifica a “enésima” ocorrência de um dia da semana do mês, por exemplo, “terceira sexta-feira do mês” pode ser
  indicada como “6 # 3”.

## Como rodar a aplicação

Criar o arquivo docker-compose.yaml:

```yaml
version: "3"
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    networks:
      - broker-kafka
    environment:
      - ZOOKEEPER_CLIENT_PORT=2181
      - ZOOKEEPER_TICK_TIME=2000
  kafka:
    image: confluentinc/cp-kafka:latest
    networks:
      - broker-kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      - KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      - KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT
      - KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    networks:
      - broker-kafka
    depends_on:
      - kafka
    ports:
      - "9000:9000"
    environment:
      - KAFKA_BROKERCONNECT=kafka:29092
networks:
  broker-kafka:
    driver: bridge
```

Rodar os seguintes comandos:

```shell
docker pull gustosilva/spring-cron # Baixar a imagem no docker hub
docker run -d --net spring-cron_broker-kafka gustosilva/spring-cron # subir um container da imagem
docker logs -f <<id_container>> # Ver os logs da aplicação
```

## Tecnologias utilizadas

Projeto feito usando kotlin e Maven 3.8 como ferramenta de build.

## Dependencias

- spring-kafka
- Slf4j
- logback

## Autor

<div>
  <p>Feito por Gustavo Silva:</p>
  <a href="https://www.linkedin.com/in/gustavo-silva-69b84a15b/"><img src="https://img.shields.io/badge/linkedin-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" height=25></a>
  <a href="https://discordapp.com/users/616994765065420801"><img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" height=25></a>
  <a href="mailto:gustavoalmeidasilva41@gmail.com"><img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" height=25></a>
  <a href="mailto:gustavo_almeida11@hotmail.com"><img src="https://img.shields.io/badge/Microsoft_Outlook-0078D4?style=for-the-badge&logo=microsoft-outlook&logoColor=white" height=25></a>
</div>