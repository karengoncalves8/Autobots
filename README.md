# Autobots

## Teste de Login

1. Cadastrar um usuário via POST request para a rota /usuario/cadastro com o seguinte modelo de requisição: 
```
{
  "nome": "João Silva",
  "nomeSocial": "João",
  "perfis": ["CLIENTE"],
  "credenciais": [
    {
      "tipoCredencial": "senha",
      "criacao": "2024-01-01T00:00:00.000Z",
      "inativo": false,
      "nomeUsuario": "joao.silva",
      "senha": "senha123"
    },
    {
      "tipoCredencial": "codigoBarra",
      "criacao": "2024-01-01T00:00:00.000Z",
      "inativo": false,
      "codigo": "123456789"
    }
  ],
  "telefones": [
    {
      "ddd": "11",
      "numero": "999999999"
    }
  ],
  "endereco": {
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Centro",
    "rua": "Rua das Flores",
    "numero": "123",
    "codigoPostal": "01234-567",
    "informacoesAdicionais": "Apto 45"
  },
  "documentos": [
    {
      "numero": "12345678901",
      "tipo": "CPF",
      "dataEmissao": "2024-05-20"
    }
  ],
  "emails": [
    {
      "endereco": "joao.silva@email.com"
    }
  ]
}
```
2. Testar login por senha enviando requisição POST para a rota /login com o seguinte corpo:
{
    "nomeUsuario": "joao.silva",
    "senha": "senha123"
}

3. Testar login por código enviando requisição POST para a rota /login-codigo com o seguinte corpo:
{
    "codigo": "123456789"
}