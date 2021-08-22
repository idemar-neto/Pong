### PONG

Esse programa consiste no famoso jogo de Pong. Muito similar a tênis de mesa. 

A execução desse programa é dada pelos comandos:

```javac *.javac                       (para a complilar)```

```java Pong                           (para executar)```

```java Pong <número inteiro> true     (caso o comando anterior não funcionar)```

Os comandos que compõem a funcionalidade do jogo são:

+ A            -   Movimenta o jogador 1 (ou mais especificamente, o jogador da esquerda) para cima.
+ Z            -   Movimenta o jogador 1 (ou mais especificamente, o jogador da esquerda) para baixo.
+ K            -   Movimenta o jogador 2 (ou mais especificamente, o jogador da direita) para cima.
+ M            -   Movimenta o jogador 2 (ou mais especificamente, o jogador da direita) para baixo.
+ ESC (Escape) -   Pausa o jogo.
+ ESPAÇO       -   Começa o jogo / "Despausa" o jogo, em caso de jogo de pausado.

_(Os comandos correspondem a teclas do teclado)_

A física da bola consistem em bater na parte da frente (frente ao meio do campo), em cima e embaixo do player, com retornos semi-aleatórios, baseado na angulação de retorno e com um "índice" de aleatoriedade como "margem de erro". Assim também funciona com as paredes, sendo calculado o ângulo de colição pelo seno e cosseno da variável "direction", e com isso chegando nos quadrantes tanto de vinda como de ida da bola.

Quando o jogo é pausado pela tecla ESC (Escape), ambos os players permanecem imóveis, assim como não é mais acrescido nenhum ponto no placar, e o texto "JOGO PAUSADO" aparece no centro da tela. Para que o jogo retorne ao seu funcionamento padrão, basta presionar ESPAÇO, para "despausar".

O jogo é contínuo, logo só se encerra com o encerramento do programa ao fechar a janela do jogo.