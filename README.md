# MetroMoov

MetroMoov e um aplicativo Android nativo, desenvolvido em Java, para registrar pesquisas de origem e destino de usuarios do metro. O app permite que entrevistadores cadastrem participantes, capturem data, hora e localizacao aproximada da entrevista, salvem os dados em SQLite local e exibam resultados agregados para administradores.

## Funcionalidades

- Tela inicial com splash screen e logotipo do app.
- Login com perfis de administrador e entrevistador.
- Criacao automatica de usuarios padrao na primeira abertura do app.
- Cadastro de participantes da pesquisa com:
  - nome;
  - celular;
  - data e hora da entrevista;
  - endereco obtido por geolocalizacao;
  - estacao de origem;
  - estacao de destino.
- Consulta dos dados dos participantes cadastrados.
- Relatorio com total de respostas, distribuicao por origem, destino e pares origem-destino.
- Exclusao/zeragem dos registros de pesquisa.
- Tela administrativa para gerenciar entrevistadores.
- Persistencia local com SQLite.

## Credenciais padrao

Na primeira execucao, o app cria automaticamente dois usuarios:

| Login | Senha | Perfil |
| --- | --- | --- |
| `admin` | `1234` | Administrador |
| `ent` | `1234` | Entrevistador |

O usuario `admin` acessa o painel administrativo. Qualquer outro usuario valido acessa diretamente a tela de pesquisa.

## Tecnologias

- Android nativo
- Java 11
- Gradle Kotlin DSL
- Android Gradle Plugin 8.8.0
- Gradle Wrapper 8.10.2
- SQLite
- AndroidX AppCompat
- Material Components
- ConstraintLayout
- Google Play Services Location
- Google Geocoding API
- MPAndroidChart 3.1.0
- JUnit e Espresso para testes

## Requisitos

- Android Studio recente com suporte ao Android Gradle Plugin 8.8.0.
- JDK 11 ou superior.
- SDK Android com `compileSdk` 35 instalado.
- Dispositivo ou emulador Android com API 24 ou superior.
- Chave de API do Google Maps/Geocoding para a consulta de endereco pela localizacao.

## Configuracao da chave de API

O projeto le a chave pela propriedade `MAPS_API_KEY`, definida em `gradle.properties`:

```properties
MAPS_API_KEY=COLOQUE SUA CHAVE DE API AQUI
```

Substitua o valor pelo token real da Google Geocoding API:

```properties
MAPS_API_KEY=SUA_CHAVE_AQUI
```

Essa chave e usada em tempo de compilacao para gerar `BuildConfig.MAPS_API_KEY`, acessado pela tela de pesquisa ao chamar:

```text
https://maps.googleapis.com/maps/api/geocode/json?latlng=LATITUDE,LONGITUDE&key=MAPS_API_KEY
```

Sem uma chave valida, o app ainda pode abrir, mas a conversao de latitude/longitude para endereco pode falhar.

## Como executar

### Pelo Android Studio

1. Abra o diretorio do projeto no Android Studio.
2. Aguarde a sincronizacao do Gradle.
3. Configure `MAPS_API_KEY` em `gradle.properties`.
4. Selecione um emulador ou dispositivo fisico.
5. Execute o modulo `app`.

### Pela linha de comando

No Windows:

```powershell
.\gradlew.bat assembleDebug
```

Em Linux/macOS:

```bash
./gradlew assembleDebug
```

O APK de debug sera gerado em:

```text
app/build/outputs/apk/debug/
```

## Permissoes Android

O aplicativo declara as seguintes permissoes em `AndroidManifest.xml`:

| Permissao | Uso |
| --- | --- |
| `ACCESS_FINE_LOCATION` | Obter localizacao precisa da entrevista. |
| `ACCESS_COARSE_LOCATION` | Permitir localizacao aproximada. |
| `INTERNET` | Consultar a API de geocodificacao do Google. |

Durante o uso da tela de pesquisa, o app solicita permissao de localizacao ao usuario.

## Fluxo do aplicativo

1. `MainActivity`
   - Exibe a splash screen.
   - Cria os usuarios padrao `admin` e `ent`, caso ainda nao existam.
   - Redireciona para `TelaLogin` apos cerca de 2 segundos.

2. `TelaLogin`
   - Valida login e senha no banco SQLite.
   - Redireciona `admin` para `TelaAdmin`.
   - Redireciona entrevistadores para `TelaPesquisa`.

3. `TelaPesquisa`
   - Coleta dados do participante.
   - Obtem localizacao via `FusedLocationProviderClient`.
   - Consulta o endereco pela Google Geocoding API.
   - Salva a resposta na tabela `pessoa`.
   - Reinicia a propria tela apos finalizar o cadastro.

4. `TelaAdmin`
   - Menu administrativo com acesso a:
     - resultados da pesquisa;
     - dados dos participantes;
     - configuracoes de entrevistadores.

5. `TelaResultadosPesquisa`
   - Lista resultados agregados por origem, destino e origem-destino.
   - Permite zerar todos os registros da tabela `pessoa`.

6. `TelaDadosParticipantes`
   - Exibe os participantes cadastrados.
   - Mostra nome, celular, data, hora e localizacao.
   - Permite zerar todos os registros da tabela `pessoa`.

7. `TelaConfig`
   - Permite salvar, consultar, atualizar e excluir usuarios entrevistadores.

## Estacoes disponiveis na pesquisa

As opcoes de origem e destino sao carregadas diretamente no codigo da `TelaPesquisa`.

Linha 2 - Verde:

- Vila Prudente
- Tamanduatei
- Sacoma
- Alto do Ipiranga
- Santos-Imigrantes
- Chacara Klabin
- Ana Rosa
- Paraiso
- Brigadeiro
- Trianon-Masp
- Consolacao
- Clinicas
- Sumare
- Vila Madalena

Linha 4 - Amarela:

- Luz
- Republica
- Higienopolis-Mackenzie
- Paulista
- Faria Lima
- Pinheiros
- Butanta
- Sao Paulo-Morumbi
- Fradique Coutinho
- Oscar Freire
- Vila Sonia

## Banco de dados

O app usa SQLite local por meio da classe `DatabaseHelper`.

Nome do banco:

```text
bdMetroMoov
```

Versao atual:

```text
2
```

### Tabela `usuario`

Armazena as credenciais e o tipo de acesso dos usuarios.

| Coluna | Tipo | Descricao |
| --- | --- | --- |
| `id` | INTEGER | Chave primaria autoincremental. |
| `login` | TEXT | Nome de login. |
| `senha` | TEXT | Senha do usuario. |
| `isAdmin` | INTEGER | `1` para administrador, `0` para usuario comum. |

### Tabela `pessoa`

Armazena as respostas das pesquisas.

| Coluna | Tipo | Descricao |
| --- | --- | --- |
| `id` | INTEGER | Chave primaria autoincremental. |
| `nome` | TEXT | Nome do participante. |
| `celular` | TEXT | Telefone celular. |
| `data` | TEXT | Data da entrevista. |
| `hora` | TEXT | Hora da entrevista. |
| `endereco` | TEXT | Endereco obtido por geolocalizacao. |
| `origem` | TEXT | Estacao de origem. |
| `destino` | TEXT | Estacao de destino. |

### Tabela `estacoes`

Existe suporte de DAO/modelo para estacoes, embora o fluxo atual da interface carregue as estacoes diretamente em `TelaPesquisa`.

| Coluna | Tipo | Descricao |
| --- | --- | --- |
| `id` | INTEGER | Chave primaria autoincremental. |
| `nome` | TEXT | Nome da estacao. |
| `descricao` | TEXT | Linha ou descricao da estacao. |

## Estrutura do projeto

```text
.
|-- app/
|   |-- build.gradle.kts
|   |-- proguard-rules.pro
|   `-- src/
|       |-- androidTest/
|       |-- main/
|       |   |-- AndroidManifest.xml
|       |   |-- java/com/example/metromoov/
|       |   |-- res/drawable/
|       |   |-- res/layout/
|       |   |-- res/mipmap-*/
|       |   |-- res/values/
|       |   `-- res/xml/
|       `-- test/
|-- gradle/
|   |-- libs.versions.toml
|   `-- wrapper/
|-- build.gradle.kts
|-- gradle.properties
|-- gradlew
|-- gradlew.bat
`-- settings.gradle.kts
```

## Principais classes

| Classe | Responsabilidade |
| --- | --- |
| `MainActivity` | Splash screen, criacao dos usuarios padrao e redirecionamento inicial. |
| `TelaLogin` | Autenticacao dos usuarios. |
| `TelaAdmin` | Menu do administrador. |
| `TelaConfig` | Cadastro, consulta, atualizacao e exclusao de usuarios. |
| `TelaPesquisa` | Formulario principal de pesquisa e captura de localizacao. |
| `TelaDadosParticipantes` | Listagem dos participantes cadastrados. |
| `TelaResultadosPesquisa` | Relatorio agregado das respostas. |
| `DatabaseHelper` | Criacao e configuracao do banco SQLite. |
| `UsuarioDAO` | Operacoes de banco para usuarios. |
| `PessoaDAO` | Operacoes de banco para participantes/respostas. |
| `EstacoesDAO` | Operacoes de banco para estacoes. |
| `Usuario` | Modelo de usuario. |
| `Pessoa` | Modelo de participante/resposta. |
| `Estacoes` | Modelo de estacao. |

## Testes

O projeto contem testes basicos gerados pelo template Android:

- `ExampleUnitTest`: teste unitario local.
- `ExampleInstrumentedTest`: teste instrumentado que valida o package name da aplicacao.

Para executar testes unitarios:

```powershell
.\gradlew.bat test
```

Para executar testes instrumentados, com dispositivo/emulador conectado:

```powershell
.\gradlew.bat connectedAndroidTest
```

## Observacoes tecnicas

- O app usa `Theme.Material3.DayNight.NoActionBar`.
- O `release` esta configurado com `isMinifyEnabled = false`.
- A tela de resultados calcula percentuais em memoria a partir dos registros salvos no SQLite.
- A acao "ZERAR TOTAIS" apaga todos os registros da tabela `pessoa`.
- O metodo `onUpgrade` do banco ainda nao aplica migracoes entre versoes.
- A dependencia MPAndroidChart esta declarada, mas a tela atual de resultados exibe dados em texto.
- Alguns textos presentes no codigo fonte aparentam estar com caracteres acentuados corrompidos por encoding. O comportamento do app nao foi alterado por este README.

## Licenca

Este repositorio nao possui arquivo de licenca definido.
