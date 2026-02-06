# DSS (Desenvolvimento de Sistemas de Software) (Português)

Implementação de um **Sistema Integrado de Cadeia de Restaurantes**, desenvolvido no âmbito da unidade curricular de *Desenvolvimento de Sistemas de Software* da **Universidade do Minho**.

É possível consultar:
- o [enunciado do projeto](Enunciado.pdf);
- o relatório da [Fase 1](fase-1/Relatorio-fase-1.pdf);
- o relatório da [Fase 2](fase-2/Relatorio-fase-2.pdf).

### Membros do Grupo
- Duarte Escairo Brandão Reis Silva (a106936) — [darteescar](https://github.com/darteescar)
- Luís António Peixoto Soares (a106932) — [luis7788](https://github.com/luis7788)
- Tiago Silva Figueiredo (a106856) — [tiagofigueiredo7](https://github.com/tiagofigueiredo7)
- Inês Ferreira Ribeiro (a104704) — [inesferribeiro](https://github.com/inesferribeiro)

## Dependências

### Base de Dados

O programa é compatível com **MariaDB** e **MySQL**. Após instalar um dos sistemas de gestão de base de dados, devem ser executados os seguintes comandos no cliente SQL:

```sql
CREATE DATABASE EatHub;

CREATE USER IF NOT EXISTS 'me'@'localhost' IDENTIFIED BY 'Mypass12345678!';

GRANT ALL PRIVILEGES ON *.* TO 'me'@'localhost';

FLUSH PRIVILEGES;
```

Após a execução destes comandos, o programa pode ser iniciado.

Para configurações adicionais, consultar o ficheiro [fase-2/README.md](fase-2/README.md).

> ⚠️ **Aviso**  O programa utiliza **MySQL por omissão**. Para utilizar **MariaDB**, é necessário alterar a configuração correspondente no ficheiro [DAOconfig.java](fase-2/eat-hub/src/eathubDL/DAOconfig.java).


## Execução do Programa

Vá para a diretoria *fase-2/eat-hub/*

```sh
cd fase-2/eat-hub/
```

Para compilar:

```sh
make compile
```

Para compilar e executar:

```sh
make run
```

## Limpeza de ficheiros

Para remover os ficheiros gerados durante a compilação:

```sh
cd fase-2/eat-hub/
make clean
```

# DSS (Software Systems Development) (English)
Implementation of an **Integrated Restaurant Chain System**, developed within the scope of the *Software Systems Development* course at the **University of Minho**.

You can consult:
- the [project statement](Enunciado.pdf);
- the report of the [First Phase](fase-1/Relatorio-fase-1.pdf);
- the report of the [Second Phase](fase-2/Relatorio-fase-2.pdf).

### Group Members

- Duarte Escairo Brandão Reis Silva (a106936) — [darteescar](https://github.com/darteescar)
- Luís António Peixoto Soares (a106932) — [luis7788](https://github.com/luis7788)
- Tiago Silva Figueiredo (a106856) — [tiagofigueiredo7](https://github.com/tiagofigueiredo7)
- Inês Ferreira Ribeiro (a104704) — [inesferribeiro](https://github.com/inesferribeiro)

## Dependencies

### Database 

The program is compatible with **MariaDB** and **MySQL**. After installing one of the database management systems, the following commands must be executed in the SQL client:

```sql
CREATE DATABASE EatHub;

CREATE USER IF NOT EXISTS 'me'@'localhost IDENTIFIED BY 'Mypass12345678!';

GRANT ALL PRIVILEGES ON *.* TO 'me'@'localhost';

FLUSH PRIVILEGES;
```

After executing these commands, the program can be started.

For additional configurations, consult the file [fase-2/README.md](fase-2/README.md).

> ⚠️ **Warning**  The program uses **MySQL by default**. To use **MariaDB**, it is necessary to change the corresponding configuration in the file [DAOconfig.java](fase-2/eat-hub/src/eathubDL/DAOconfig.java).

## Program Execution

Go to the *fase-2/eat-hub/* directory

```sh
cd fase-2/eat-hub/
```

To compile:

```sh
make compile
```

To compile and run:

```sh
make run
```

## File Cleanup

To remove the files generated during compilation:

```sh
cd fase-2/eat-hub/
make clean
```
