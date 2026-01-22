## Como configurar e popular a Base de Dados

- Instalar `MariaDB` ou `MySQL`;
- Iniciar o servidor da base de dados;
- Executar os scripts na pasta [*scripts SQL*](scripts-SQL/) pela seguinte ordem:
  1. `pre_Start.sql` — executar após iniciar o servidor;
  2. `init_EatHub.sql` — cria as tabelas;
  3. `Populacao.sql` — popula a base de dados após a criação das tabelas;

> ⚠️ **Aviso Importante**  
> Antes de iniciar o programa, **apenas o passo 1 é obrigatório**.  
> O programa cria automaticamente as tabelas caso estas não existam, pelo que **não é obrigatório executar os scripts de criação ou de população da base de dados**.  
> Além disso, o programa dispõe de um modo próprio que permite **alterar e gerir a base de dados diretamente**, tornando a população manual opcional.


