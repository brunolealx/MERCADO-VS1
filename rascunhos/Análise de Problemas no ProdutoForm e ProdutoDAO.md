# Análise de Problemas no ProdutoForm e ProdutoDAO

## Problemas Identificados

### 1. ProdutoForm.java - Linha 54

**Problema:** Declaração local do botão `btnSalvar` que oculta o atributo da classe.

```java
// Linha 27: Atributo da classe
private JButton btnSalvar;

// Linha 54: Declaração local (PROBLEMA - oculta o atributo)
JButton btnSalvar = new JButton("Salvar");
```

**Consequência:** O atributo `btnSalvar` da classe nunca é inicializado, permanecendo `null`. Isso impede o controle de visibilidade do botão.

### 2. ProdutoForm.java - Linha 68

**Problema:** O botão "Novo" apenas chama `limparCampos()`, mas não implementa a lógica completa solicitada.

```java
// Linha 68: Implementação atual
btnNovo.addActionListener(e -> limparCampos());
```

**Requisitos não implementados:**
- Não exibe o botão `btnSalvar` após limpar os campos
- Não prepara o formulário para inserção de novo registro

### 3. ProdutoForm.java - Método salvarProduto()

**Problema:** O método sempre chama `dao.inserir()`, mesmo quando deveria atualizar um registro existente.

```java
// Linhas 176-185: Sempre insere
private void salvarProduto() {
    try {
        Produto p = criarProdutoDeCampos();
        dao.inserir(p);  // SEMPRE INSERE
        JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
        listarPorId();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
    }
}
```

**Consequência:** Não há lógica para diferenciar entre novo registro e atualização.

## Soluções Propostas

### Solução 1: Corrigir declaração do btnSalvar
Remover a palavra-chave `JButton` da linha 54 para usar o atributo da classe.

### Solução 2: Implementar lógica completa no botão "Novo"
- Limpar todos os campos
- Exibir o botão `btnSalvar`
- Ocultar o botão `btnAtualizar` (se necessário)
- Preparar o formulário para modo de inserção

### Solução 3: Melhorar método salvarProduto()
- Verificar se é novo registro (ID vazio) ou atualização (ID preenchido)
- Chamar `dao.inserir()` para novos registros
- Chamar `dao.atualizar()` para registros existentes

## Observações sobre ProdutoDAO.java

O método `inserir(Produto p)` está **corretamente implementado** nas linhas 37-79:
- Recebe um objeto Produto
- Executa INSERT no banco de dados
- Trata todos os campos necessários
- Não há problemas identificados neste método
