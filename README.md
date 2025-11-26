# Kahoot_PD_25_26

Sistema feito em Java com persistência de dados em SQLite, com três componentes Cliente, Servidor, Serviço de Diretoria

## Docente

- pode criar e editar registos pessoais (nome, email, password) devendo usar uma pass para o efeito
- pode autenticar-se fornecendo email e password
- criar perguntas, definindo enunciado, n de opcoes, as opcoes, a opcao correta, e o periodo em que esta disponivel (
  data e hora do fim)
- quando uma pergunta é criada gera automaticamente um código de acesso
- pode editar uma pergunta se ainda não tiver recebido respostas
- eliminar pergunta se ainda não tiver recebido respostas
- consultar a lista de perguntas do próprio, pode aplicar filtros
- para perguntas expiradas pode listar e/ou exportar para csv respostas submetidas da seguinte forma

```
"dia";"hora inicial";"hora final";"enunciado da pergunta";"opção certa"
"06-10-2025";"10:10";"10:12";”Para enviar e receber dados via TCP em Java, recorre-se a objetos do tipo:”;”a”
"opção";"texto da opção"
"a";"Socket"
"b";"ServerSocket"
"c";"DatagramSocket"
"número de estudante"; "nome"; "e-mail";”resposta"
"123456";"Johan Brochet";"jobro@gmail.com";"a"
"123457";"Jeanne Duval";"duvalj@gmail.com";"a"
"123458";"Rodolphe Pillon";"rodo.pillon@gmail.com";"b"
```

- encerrar sessão

## Estudante

- criar e editar registo pessoal (numero estudante, nome, email, password), EMAIL e N de ESTUDANTE UNIQUE
- autenticar no sistema (email e password)
- introduzir o código de uma pergunta não expirada
    - é-lhe apresentado então a pergunta e ele pode responder
- consultar perguntas expiradas a que já respondeu, mostrando se está errada ou correta, pode ter filtros
- encerrar sessão

## Arquitetura e Protocolos

- Só um servidor de cada vez é que fala com os clientes
- Esse mesmo servidor é responsável por propagar as mudanças recebidas pelos clientes aos outros servidores registados
  no serviço de diretoria
- Cada servidor tem a sua base de dados SQLite

### Servidores

- São lançados por linha de comando especificando
    - ip e porto do serviço de diretoria
    - o caminho da diretoria de armazenamento da base de dados local
    - ip da interface de rede local para multicast

1. No arranque, o servidor tenta registar-se no serviço de diretoria e aguarda confirmação
    1. o pedido deve incluir dois portos TCP automáticos usados
        1. para aceitar pedidos de ligação de clientes
        2. para permitir que outros servidores se conectem para receberem a cópia da base de dados
    2. a resposta deve ter o endereço ip e o porto de escuta para obtenção do servidor registado há mais tempo na
       diretoria
        - se for o 1 servidor na diretoria ele é o servidor de comunicação
    3. se não receber confirmação na fase de arranque termina
    4. se no arranque o principal não tiver base de dados, na diretoria especificada então cria uma nova com versão 0.
       Caso contrário usa a mais recente da diretoria especificada
2. Depois da resposta os servidores não principais pedem uma copia da base de dados, guardando-a na diretoria indicada
   na linha de comando. Se não conseguir fazê-lo termina e avisa o serviço de diretoria
    1. Durante esta transferência o servidor principal não pode fazer alterações aos dados.
3. Um servidor que não é o principal pode passar a sê-lo, se o principal deixar de estar operacional. Por isso todos os
   servidores aguardam pedidos de ligação TCP no porto indicado
4. Cada base de dados tem um número associado, este número incrementa sempre que há uma alteração a uma tabela da DB
5. As DB devem ter regras de criação de forma a que não seja necessário andar a apagá-las para evitar perdas de dados
   irreverssíveis
6. Depois do arranque um servidor manda um heartbeat a cada 5 segundos, para o porto 3030 do multicast `230.30.30.30` e
   para a diretoria(via UDP) que lhe devolve as informações do atual servidor principal
    1. Os heartbeats têm que especificar
        - Numero da versao atual da base de dados local
        - porto de escuta TCP automatico no qual se aguarda pedidos de conexão de clientes
        - porto de escuta tcp automatico no qual se aguarda pedidos de outros servidores para obtenção da cópia da DB
    2. Tambem sao mandados hearbeats sempre que for feita uma alteração à base de dados, este inclui a query sql que foi
       executada
7. Todos os servidores,apos o arranque, devem esperar hearbeats ao porto 3030 `230.30.30.30`
    1. A receção de heartbeats próprios e de servidores que não sejam o servidor principal é ignorada
    2. na receção dos hearbeats:
        1. com ou sem query sql, mas com versão de base de dados local errada -> termina execução
        2. com query sql, e numero de versao igual ao local + 1 leva à execução da query na db local
8. Depois de qualquer alteração na db, o principal deve informar os clientes, por tcp, para que atualizem as suas vistas
9. Quando um servidor é encerrado da maneira normal, deve informar a diretoria, para que esta anule o seu registo

### Diretoria

- Gere a lista de servidores ativos, ordenada por ordem de registo (dos mais antigos para os mais novos)
- Aguarda sempre envio de datagramas UDP no porto passado na linha de comandos
- Quando recebe um heartbeat de um servidor não registado ignora-o
- Quando um cliente faz um pedido, responde com o ip e porto TCP do servidor ativo
- Se um servidor não enviar heartbeats durante 17 segundos elimina-o da lista
- Se a diretoria deixar de funcionar todos os serviços deixarão de funcionar

### Clientes

- São lançados fornecendo o ip e o porto UDP da diretoria
- solicitam à diretoria o ip e porto TCP do servidor ao qual se devem ligar. Se não conseguirem fecham
- Começam por solicitar dados para autenticação ou registo
- Estabelem TCP com o principal e enviam a informação de autenticação ou registo
- Quando a autenticação falha ou não envia as credenciais em 30 segundos, o servidor encerra ligação TCP com o cliente
- Quando se encerra ligação com o servidor, o cliente volta à diretoria, se o novo principal for diferente do anterior,
  liga-se e autentica-se sem perguntar de novo as credenciais. Se não for diferente espera 20 segundos, tenta novamente
  e se falhar de novo, o cliente termina.
