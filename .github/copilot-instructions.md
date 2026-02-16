# MERCADO-VS1 Development Guide for AI Agents

## Project Overview
MERCADO-VS1 is a desktop POS/inventory management system for retail markets built with **Java 21 + Swing UI + PostgreSQL**. This is a Maven project with a simplified MVC architecture separating data models, database access (DAO pattern), and Swing UI forms.

## Architecture & Data Flow

### Three-Layer Structure
1. **Model** (`src/main/java/com/creativex/model/*`): Plain Java POJOs with BigDecimal for prices and Timestamp for dates
   - Example: [Produto.java](src/main/java/com/creativex/model/produto/Produto.java) uses BigDecimal for pricing to avoid float precision issues
2. **DAO Layer** (`src/main/java/com/creativex/dao/*`): Database access logic using JDBC, implements DAO pattern
   - Each entity has a DAO class (ProdutoDAO, ClienteDAO, UsuarioDAO, etc.)
   - Example: [ProdutoDAO.java](src/main/java/com/creativex/dao/produto/ProdutoDAO.java) uses try-with-resources for Connection/PreparedStatement
3. **UI Layer** (`src/main/java/com/creativex/ui/*`): Swing JFrame forms for CRUD operations
   - Each module has its own form (ProdutoForm.java, ClientesForm.java, etc.)

### Session Management Pattern
- Singleton static utility [Sessao.java](src/main/java/com/creativex/util/Sessao.java) stores logged-in Usuario object
- Access anywhere via `Sessao.getUsuarioLogado()` and `Sessao.isLogado()`
- LoginForm authenticates via UsuarioDAO.autenticar() before setting session

### Database Connection
- Single centralized connection factory [Conexao.java](src/main/java/com/creativex/db/Conexao.java)
- Uses PostgreSQL JDBC driver on localhost:5432, database `bco_dados_mercado`
- Credentials in Conexao class need updating for local dev environments

## Key Conventions

### DAO Method Patterns
- **buscarUltimo()**: Returns last inserted record (ORDER BY id DESC LIMIT 1)
- **listar()**: Returns List<Model>, often with pagination support
- **buscarPorId(long id)**: Returns single record or null
- **inserir(Model obj)**: Throws SQLException on failure
- **atualizar(Model obj)**: Updates existing record
- **deletar(long id)**: Soft or hard delete per module
- Use Java text blocks (""" """) for multi-line SQL queries

### Model Conventions
- Fields use snake_case in database, camelCase in Java
- Prices stored as BigDecimal(12,2) - never use float/double
- Quantities use BigDecimal(10,3) for decimal precision
- Timestamps use java.sql.Timestamp for database serialization
- IDs are long (BIGSERIAL in PostgreSQL)

### UI Form Patterns
- Extend JFrame, initialize UI components in constructor
- Use GridLayout or BorderLayout for simple layouts
- Form validation before DAO calls (check empty fields with JOptionPane)
- Method naming: `efetuarAcao()` (Portuguese: "perform action")
- LoginForm must execute first (see [Main.java](src/main/java/com/creativex/Main.java) line 13)

## Build & Run

```bash
# Compile and package
mvn clean package

# Run the main class (requires UI environment)
mvn exec:java -Dexec.mainClass="com.creativex.Main"
```

**Prerequisites:**
- Java 21+ JDK (defined in pom.xml)
- PostgreSQL server running on localhost:5432
- Database created and SQL script executed: `src/main/resources/CRIA-TABELAS-POSTGRE.sql`
- Update credentials in [Conexao.java](src/main/java/com/creativex/db/Conexao.java) (USER="pera", PASSWORD="postboot" for this dev setup)

## Current Feature Modules

- **Produtos**: Full CRUD with barcode, pricing (ICMS/PIS/COFINS taxes), stock quantities
- **Fornecedores**: Supplier management with credit limits
- **Clientes/ClientepjDAO**: Both PF (pessoa física) and PJ (pessoa jurídica) customers
- **Usuarios**: User auth with bcrypt password hashing (jbcrypt library)
- **Vendas**: Sales/checkout with ItemVenda line items
- **Caixas**: Cash register management and finalization
- **Estoque**: Inventory control
- **Impressoras**: Printer configuration
- **Listagens**: Report generation

## Important Details

- Package prefix is **com.creativex** - maintain this structure for new modules
- Comments in Portuguese; keep them for consistency with existing code
- UI responsiveness: long DB operations should run in separate threads (SwingWorker recommended)
- Password hashing: Use bcrypt (jbcrypt library) in UsuarioDAO, never store plaintext
- All SQL uses PreparedStatement to prevent SQL injection
- Main entry point always routes to LoginForm first (security pattern)

## Common Tasks

**Adding a new CRUD entity:**
1. Create model in `model/{entity}/Entity.java` with getters/setters
2. Create DAO in `dao/{entity}/EntityDAO.java` with standard CRUD methods
3. Create form in `ui/{entity}/EntityForm.java` extending JFrame
4. Add database table to SQL script with proper indexes
5. Integrate form into MainWindow navigation menu

**Modifying database schema:**
- Update CRIA-TABELAS-POSTGRE.sql
- Regenerate database and test
- Update corresponding model class fields

