import java.awt.*;

/**
	Esta classe representa um placar no jogo. A classe princial do jogo (Pong)
	instancia dois objeto deste tipo, cada um responsável por gerenciar a pontuação
	de um player, quando a execução é iniciada.
*/

/*********************************************************************/
/**   ACH2053 - Computacao Orientada a Objetos                      **/
/**   EACH-USP - Primeiro Semestre de 2021                          **/
/**   <2021194> - Prof. Flavio Luiz Coutinho                        **/
/**                                                                 **/
/**   EP1 - Pong                                                    **/
/**                                                                 **/
/**   <Idemar Burssed dos Santos Neto>             <11857282>       **/
/**   <Raquel Cristiane da Silva Almeida>          <11838245>       **/
/**                                                                 **/
/*********************************************************************/

public class Score {

	String playerId;
	int score;

	/**
		Construtor da classe Score.

		@param playerId uma string que identifica o player ao qual este placar está associado.
	*/

	public Score(String playerId){
		
		this.playerId = playerId;
	}

	/**
		Método de desenho do placar.
	*/

	public void draw(){

		if(playerId == "Player 1"){
			GameLib.setColor(Color.GREEN); //Cor do player 1
			GameLib.drawText(/* "JOGADOR 1:" */ this.playerId + ":" + getScore(), 70, GameLib.ALIGN_LEFT);
		}		
		if(playerId == "Player 2"){
			GameLib.setColor(Color.BLUE); //Cor do player 2
			GameLib.drawText(/* "JOGADOR 2:" */ this.playerId + ":" + getScore(), 70, GameLib.ALIGN_RIGHT);
		}	
	}

	/**
		Método que incrementa em 1 unidade a contagem de pontos.
	*/

	public void inc(){		
		if(!Ball.isPaused)score=score+1;
	}

	/**
		Método que devolve a contagem de pontos mantida pelo placar.

		@return o valor inteiro referente ao total de pontos.
	*/

	public int getScore(){

		return this.score;
	}

}
