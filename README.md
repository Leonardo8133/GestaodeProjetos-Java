# Sistema de Gestão de Projetos

Sistema desenvolvido em Java para a disciplina de PROGRAMAÇÃO DE SOLUÇÕES COMPUTACIONAIS.

## Estrutura do Projeto

```
GestaodeProjetos-Java/
├── Usuario.java           # Classe de usuário
├── PerfilUsuario.java     # Enum de perfis (GERENTE, COLABORADOR)
├── Projeto.java           # Classe de projeto
├── StatusProjeto.java     # Enum de status
├── Equipe.java            # Classe de equipe
├── Task.java              # Classe de tarefa
├── SistemaGUI.java        # Interface gráfica principal
├── run.bat                # Script para compilar e executar
├── dados.dat              # Banco de dados local (gerado automaticamente)
└── v1/                    # Versão completa com recursos avançados
```

## Como Executar

### Windows
```bash
run.bat
```

### Manualmente
```bash
javac *.java
java SistemaGUI
```

## Banco de Dados

O banco de dados é salvo em um arquivo chamado `dados.dat` e é gerado automaticamente.