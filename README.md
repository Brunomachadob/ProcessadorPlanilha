## Processador de Planilhas

Aplicação simples criada para realizar pequenas transformações em planilhas,
atualmente possui duas funcionalidades principais:


### Transformação de planilhas

Através da aba 'Processar planilhas', é possível selecionar um arquivo de configuração e a planilha a ser processada.
No objeto de configuração são informadas as colunas que devem ser enviadas a nova planilha criada e os transformações que serão aplicadas em cada coluna.

Exemplo de configuração

```
{
	"temCabecalho": true, //Informamos que há um cabeçalho, assim não aplicamos transformação na primeira linha
	"colunas": [ //Configuração das colunas
		{
			"nome": "A", //Informamos que queremos a coluna A
			"descricao": "Nome", //Que vamos alterar o nome dela para Nome
			"processadores": [ //Lista de processadores (vide a lista de processadores abaixo)
				"CaixaAlta", 
				"RemoverAcentos"
			]
		},
		{ //Outra configuração
			"nome": "C",
			"descricao": "Telefone",
			"processadores": [
				"ConverterTexto",
				"AjustarTelefone"
			]
		},
		...
	]
}
```

Processadores disponíveis

```
AjustarTelefone - Ajusta uma coluna com dados de telefone para o padrão
CaixaAlta - Altera os dados para estarem todos em maiúsculo
CaixaBaixa - Altera os dados para estarem todos em minúsculo
ConverterNumero - Altera os dados de uma coluna para serem do tipo numérico
ConverterTexto - Altera os dados de uma coluna para serem do tipo texto
RemoverAcentos - Remove os acentos da coluna
RemoverEspacos - Remove os espaços em branco da coluna
```

### Remover linhas duplicadas entre duas planilhas

Através da aba 'Intersecção planilhas', é possível selecionar um arquivo de configuração e duas planilhas que serão usadas para gerar uma nova, contendo as linhas das duas, sem repetições.

Exemplo de configuração

```
{
	"temCabecalho": true, //Informamos que há um cabeçalho, assim ignoramos ela
	"colunas": ["A", "B", "C"] //Informamos quais colunas devemos conferir para saber se um registro é igual ao outro
}
```
