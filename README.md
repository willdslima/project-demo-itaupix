# Projeto: Cadastro de Chaves PIX

## 1. Visão Geral do Projeto:
[Diagrama de Sequência](https://drive.google.com/file/d/1Oa5XnU0WhEMmtyPrbiaV-p6a3FTqhxQP/view?usp=sharing).

## ℹ️ Sobre a Pix API

Esta aplicação demonstra um sistema de cadastro e gerenciamento de chaves PIX, utilizando Java 17, Spring Boot, banco de dados H2 e princípios RESTful.

---

## ✅ Aplicação dos fatores do [12factor.net](https://12factor.net)

| Fator                        | Aplicado? | Detalhes                                                                                                                                                                             |
|-----------------------------|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **I. Código base**          | ✅        | Projeto versionado com Git e mantido em um único repositório.                                                                                                                        |
| **II. Dependências**        | ✅        | Gerenciadas via Maven (`pom.xml`) com uso de `mvnw` e `mvnw.cmd` para portabilidade.                                                                                                 |
| **III. Configurações**      | ✅        | Configurações externas via `application.properties`, facilitando uso de variáveis de ambiente.                                                                                       |
| **IV. Backing services**    | ✅        | Uso de banco H2 como serviço substituível. Ex. facilmente trocado por MySQL, alterando as dependências no `pom.xml` e as config no `application.properties`. |
| **V. Build, release, run**  | ✅        | Processo separado: `mvn clean install` para build, e execução separada via `java -jar`.                                                                                              |
| **VI. Processos**           | ✅        | API mantém a lógica separada do armazenamento dos dados usando H2 para armazenamento local.                                                                                          |
| **VII. Port binding**       | ✅        | Serviço web exposto via porta definida (ex: `server.port` no `application.properties`).                                                                                              |
| **VIII. Concurrency**       | ⚠️ Parcial | A aplicação permite múltiplas instâncias, mas não explora todos os aspectos de concorrência ainda.                                                                                   |
| **IX. Descartabilidade**    | ✅        | A aplicação pode ser iniciada ou parada rapidamente via `java -jar`.                                                                                                                 |
| **X. Dev/prod parity**      | ✅        | Mesmo código, build e config adaptável via perfis para dev/test/prod.                                                                                                                |
| **XI. Logs**                | ✅        | Logs enviados para console                                                                                      |
| **XII. Admin processes**    | ⚠️ Parcial | Ainda não há processos administrativos (ex: migração de dados), mas estrutura permite inclusão futura.                                                                               |

---

## 🚀 Como executar o projeto

### Build (compilar e gerar o JAR)

```bash
mvn clean package -DskipTests