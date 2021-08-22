import java.awt.*;
import java.text.DecimalFormat;

/**
	Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
	instancia um objeto deste tipo quando a execução é iniciada.
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

public class Ball {
	
	double cx;
	double cy;
	double width;
	double height;
	Color color;
	double speed;
	
	double speed_original;	//Variavel utilizada como auxiliar para o pausamento do jogo
	double direction;	// Indica o angulo, em radianos, que a bola está tendo
	
	static boolean isPaused = false; 	// Variavel booleana para o pausamento do jogo
	String last_id = ""; 	// String auxiliar para evitar o contato duplo na mesma parede e contar mais de um ponto por vez
	
	//Variaveis auxiliares para os casos especiais de contato em cima e embaixo do player
	boolean top = false;
	boolean bottom = false;

	/**
		Construtor da classe Ball. Observe que quem invoca o construtor desta classe define a velocidade da bola 
		(em pixels por millisegundo), mas não define a direção deste movimento. A direção do movimento é determinada 
		aleatóriamente pelo construtor.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
	*/

	public Ball(double cx, double cy, double width, double height, Color color, double speed){

		this.cx=cx;
		this.cy=cy;
		this.width=width;
		this.height=height;
		this.color=color;
		this.speed=speed;
		
		this.speed_original=speed;
		
		// Variaveis auxiliares para o uso do Math.random() para a aleatoriedade da bola no inicio do jogo
		int max=2;
		int min=0;
		
		double rand = (Math.random() * (max - min)) + min;
		this.direction=/* 1.0 */rand*Math.PI;
	
	}


	/**
		Método chamado sempre que a bola precisa ser (re)desenhada.
	*/

	public void draw(){

		GameLib.setColor(this.color);
		GameLib.fillRect(this.cx, this.cy, this.width, this.height);
	}

	/**
		Método chamado quando o estado (posição) da bola precisa ser atualizado.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){
		this.cy += (this.speed*delta) * Math.sin(this.direction);
		this.cx += (this.speed*delta) * Math.cos(this.direction);

		//"Pausa" o jogo
		if(!isPaused && GameLib.isKeyPressed(5) /* || GameLib.isKeyPressed(1) */){
			
			if(this.speed>0.0)this.speed=0.0; 	// Zera a velocidade para "pausar" o jogo
			isPaused=true; 						// Indica que o jogo está pausado 
		}

		if(isPaused){ //Apenas é executado se o jogo está pausado
			
			GameLib.setColor(Color.YELLOW);								 //Define a cor AMARELA para o texto de jogo pausado
			GameLib.drawText("JOGO PAUSADO", 350, GameLib.ALIGN_CENTER); // Escreve na tela que o jogo está pausado
		}

		if(isPaused && GameLib.isKeyPressed(10) /* || GameLib.isKeyPressed(0) */){
			
			this.speed = this.speed_original;	// Restorna speed para seu valor original
			isPaused = false;					// Indica que o jogo NÃO está pausado
		}
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com um jogador.
	
		@param playerId uma string cujo conteúdo identifica um dos jogadores.
	*/

	public void onPlayerCollision(String playerId){
		double direction = this.direction; 		// Correspondente do ciruclo trigonometrico da direcao da bola
		double way_aux = (direction/Math.PI);	// Retira o pi da correspondente para facilitar os calculos a seguir

		double sen = Math.sin(direction); // Seno para ajuda do calculo do quadrante do angulo da bola
		double cos = Math.cos(direction); // Cosseno para ajuda do calculo do quadrante do angulo da bola
		int quadrant = 0;

		//Corresponde aos quadrantes do circulo trigonometrico para o auxilio nos calculos
		if(sen>0 && cos>0) quadrant = 4; 
		if(sen>0 && cos<0) quadrant = 3;
		if(sen<0 && cos>0) quadrant = 1;
		if(sen<0 && cos<0) quadrant = 2;

		//Pontos chaves de incidencia da bola (0 e 180 graus, ou 0 e pi no circulo trigonometrico)
		if(sen==0 && cos==1)quadrant = 360; 
		if(sen==0 && cos==-1)quadrant = 180;

		//Auxiliares para o metodo random
		double max=0; 
		double min=0;
		
		double ind_rand= 0.0;//0.02; //Indice de aleatoriedade, para a colisao semi-aleatoria da bola

		// System.out.println("Top: " + top + " | Bottom: " + bottom);

		// Casos especiais de colisao na perte de cima e de baixo do player
		if(this.top)	{if(playerId=="Player 2"){max=1.5;min= 1.5 - ind_rand;
						}if(playerId=="Player 1"){max=1.5 + ind_rand;min= 1.5;}
					}
		if(this.bottom)	{if(playerId=="Player 2"){max=0.5 + ind_rand;min= 0.5;
						}if(playerId=="Player 1"){max=0.5;min=0.5 - ind_rand;}
					}
		

		if(!this.top && !this.bottom){
		switch (playerId) {

			// Calculos baseados na angulacao de entrada para saida

			case "Player 2":    {if(quadrant == 1){max = 1.0 + Math.abs(2.0-way_aux)+ind_rand; min = 1.0 + Math.abs(2.0-way_aux) - ind_rand;
								}else if(quadrant==4){max = 0.5 + Math.abs(0.5-way_aux) + ind_rand; min = 0.5 + Math.abs(0.5-way_aux) - ind_rand;
								}else if(quadrant==360 || way_aux==0.0){max=1.0 + ind_rand;min=1.0 - ind_rand;
								}else{max=way_aux;min=way_aux;}} break;
								
			case "Player 1":    {if(quadrant == 3){max = 0.0 + Math.abs(1.0-way_aux)+ind_rand; min = 0.0 + Math.abs(1.0-way_aux) - ind_rand;
								}else if(quadrant==2){max = 1.5 + Math.abs(1.5-way_aux) + ind_rand; min = 1.5 + Math.abs(1.5-way_aux) - ind_rand;
								}else if(quadrant==180 || way_aux==1.0){max=0.0 + ind_rand;min=0.0 - ind_rand;
								}else{max=way_aux;min=way_aux;}} break;
		}}
		this.direction = ((Math.random() * (max - min)) + min) * Math.PI; // Metodo para o retorno semi-aleatorio da bola
		
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com uma parede.

		@param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	*/

	public void onWallCollision(String wallId){
		//System.out.println("bateu na parede " + wallId);
		double direction = this.direction;// Correspondente do ciruclo trigonometrico da direcao da bola
		double way_aux = (direction/Math.PI); // Retira o pi da correspondente para facilitar os calculos a seguir

		double sen = Math.sin(direction);// Seno para ajuda do calculo do quadrante do angulo da bola
		double cos = Math.cos(direction);// Cosseno para ajuda do calculo do quadrante do angulo da bola
		int quadrant = 0;

		// Corresponde aos quadrantes do circulo trigonometrico para o auxilio nos calculos
		if(sen>0 && cos>0) quadrant = 4;
		if(sen>0 && cos<0) quadrant = 3;
		if(sen<0 && cos>0) quadrant = 1;
		if(sen<0 && cos<0) quadrant = 2;

		// Pontos chaves de incidencia da bola (0 e 180 graus, ou 0 e pi no circulo trigonometrico)
		if(sen==1 && cos==0)quadrant = 270;
		if(sen==0 && cos==1)quadrant = 360;
		if(sen==-1 && cos==0)quadrant = 90;
		if(sen==0 && cos==-1)quadrant = 180;

		// Auxiliares para o metodo random
		double max=0.0; 
		double min=0.0;

		double ind_rand = 0.0;//0.04; // Indice de aleatoriedade, para a colisao semi-aleatoria da bola

		switch (wallId) {

			// Calculos baseados na angulacao de entrada para saida

			case "Bottom":  {if(quadrant == 4){max = 1.5 + Math.abs(0.5-way_aux)+ind_rand; min = 1.5 + Math.abs(0.5-way_aux) - ind_rand;
							}else if(quadrant==3){max = 1.0 + Math.abs(1.0-way_aux) + ind_rand; min = 1.0 + Math.abs(1.0-way_aux) - ind_rand;
							}else if(quadrant==270){max=(way_aux+1) + ind_rand*3;min=(way_aux+1) - ind_rand*3;
							}else{max=way_aux;min=way_aux;}} break;
							
			case "Top":     {if(quadrant == 1){max = 0.5 - Math.abs(way_aux-1.5)+ind_rand; min = 0.5 - Math.abs(way_aux-1.5) - ind_rand;
							}else if(quadrant==2){max = 0.5 + Math.abs(1.5-way_aux) + ind_rand; min = 0.5 + Math.abs(1.5-way_aux) - ind_rand;
							}else if(quadrant==90){max=(way_aux-1) + ind_rand*3;min=(way_aux-1) - ind_rand*3;
							}else{max=way_aux;min=way_aux;}} break;

			case "Right":   {if(quadrant == 1){max = 1.0 + Math.abs(2.0-way_aux)+ind_rand; min = 1.0 + Math.abs(2.0-way_aux) - ind_rand;
							}else if(quadrant==4){max = 0.5 + Math.abs(0.5-way_aux) + ind_rand; min = 0.5 + Math.abs(0.5-way_aux) - ind_rand;
							}else if(quadrant==360 || way_aux==0.0){max= 1.0 + ind_rand;min= 1.0 - ind_rand;
							}else{max=way_aux;min=way_aux;}} break;

			case "Left":    {if(quadrant == 3){max = 0.0 + Math.abs(1.0-way_aux)+ind_rand; min = 0.0 + Math.abs(1.0-way_aux) - ind_rand;
							}else if(quadrant==2){max = 1.5 + Math.abs(1.5-way_aux) + ind_rand; min = 1.5 + Math.abs(1.5-way_aux) - ind_rand;
							}else if(quadrant==180 || way_aux==1.0){max= 0.0 + ind_rand;min= 0.0 - ind_rand;
							}else{max=way_aux;min=way_aux;}} break;
		}
		this.direction = ((Math.random() * (max - min)) + min)*Math.PI; // Metodo para o retorno semi-aleatorio dabola

	}

	/**
		Método que verifica se houve colisão da bola com uma parede.

		@param wall referência para uma instância de Wall contra a qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/
	
	public boolean checkCollision(Wall wall){
		String id = wall.getId();
		
		if(last_id==id) id = id+" ";

		switch (id) {
			case "Bottom": if(this.cy+(this.height/2)>=wall.getCy()-(wall.getHeight()/2)){last_id=id;return true;}break;
			case "Top": if(this.cy-(this.height/2)<=wall.getCy()+(wall.getHeight()/2)){last_id=id;return true;}break;
			case "Right": if(this.cx+(this.width/2)>=wall.getCx()-(wall.getWidth()/2)){last_id=id;return true;}break;
			case "Left": if(this.cx-(this.width/2)<=wall.getCx()+(wall.getWidth()/2)){last_id=id;return true;}break;
		}

		return false;
	}

	/**
		Método que verifica se houve colisão da bola com um jogador.

		@param player referência para uma instância de Player contra o qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/	

	public boolean checkCollision(Player player){
		String id = player.getId();

		//Verifica casos especiais de colisao em cima e embaixo do player
		this.top = (Math.round(this.cy+this.height/2) >= Math.round(player.getCy()-player.getHeight()/2) &&
				Math.round((this.cy + this.height / 2) - 10) <= Math.round(player.getCy()-player.getHeight()/2));
		this.bottom = (Math.round(this.cy - this.height / 2) <= Math.round(player.getCy() + player.getHeight() / 2) &&
				Math.round((this.cy - this.height / 2) + 10) >= Math.round(player.getCy() + player.getHeight() / 2));

		switch (id) {
			case "Player 1": if(this.cx-(this.width/2)<=player.getCx()+(player.getWidth()/2)&&
								this.cx+(this.width/2)>=player.getCx()-(player.getWidth()/2) &&
								this.cy+this.height/2>=player.getCy()-player.getHeight()/2&&
								this.cy-this.height/2<=player.getCy()+player.getHeight()/2){
									last_id = id;
									return true;
								}break;
			case "Player 2": if(this.cx+(this.width/2)>=player.getCx()-(player.getWidth()/2) &&
								this.cx-(this.width/2)<=player.getCx()+(player.getWidth()/2) &&
								this.cy+this.height/2>=player.getCy()-player.getHeight()/2 &&
								this.cy-this.height/2<=player.getCy()+player.getHeight()/2){
									last_id = id;
									return true;
								}break;
		}

		return false;
	}

	/**
		Método que devolve a coordenada x do centro do retângulo que representa a bola.
		@return o valor double da coordenada x.
	*/
	
	public double getCx(){

		return this.cx;
	}

	/**
		Método que devolve a coordenada y do centro do retângulo que representa a bola.
		@return o valor double da coordenada y.
	*/

	public double getCy(){

		return this.cy;
	}

	/**
		Método que devolve a velocidade da bola.
		@return o valor double da velocidade.

	*/

	public double getSpeed(){

		return this.speed;
	}

}
