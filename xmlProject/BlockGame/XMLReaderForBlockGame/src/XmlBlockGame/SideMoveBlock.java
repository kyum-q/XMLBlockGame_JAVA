package XmlBlockGame;
import java.awt.Point;
import javax.swing.JLabel;
import org.w3c.dom.Node;

/**
 * 게임의 좌우로 움직이고 사라지지 않는 블록 이미지 레이블 <br>
 * (extends DontGoneBlock)
 * 
 * @author 김경미
 */
public class SideMoveBlock extends DontGoneBlock implements Runnable {
	private int moveDelay, moveX, moveDirection = 5, moveValue = 1;
	private boolean gameState = true;
	private Point lastAttackBlock = null;
	private Thread th = null;
	/**
	 * SideMoveBlock 생성자
	 * 
	 * @param node block 정보를 얻기 위한 xml Node
	 */
	public SideMoveBlock(Node node) {
		super(node);
		moveDelay = Integer.parseInt(XMLReader.getAttr(node, "moveDelay"));
		moveDirection = Integer.parseInt(XMLReader.getAttr(node, "moveDirection"));
		moveX = moveDirection;
		if(moveDirection<0)
			moveValue = -1;
		th = new Thread(this);
		th.start();
	}
	/**
	 * 좌우로 움직이면서 마지막으로 만난 블록을 변경하는 함수
	 * 
	 * @param lastAttackBlock 변경하고자 하는 마지막으로 만난 블록
	 */
	public void setLastAttackBlockPoint(Point lastAttackBlock) {
		this.lastAttackBlock = lastAttackBlock;
	}
	/**
	 * 게임을 중단하는 함수<br> 
	 * gameState를 false로 만듦
	 */
	public void gameStop() {gameState = false;}
	/**
	 * 현재 게임 상태(gameState)를 확인하는 함수<br>
	 * gameState가 false일 경우 wait()
	 */
	synchronized private void checkGameState() {
		if(!gameState) {
			try { wait();}
			catch (InterruptedException e) { return; }
		}
	}
	/**
	 * 게임을 재시작하는 함수<br> 
	 * gameState를 true로 만들고 wait한 것들을 notifyAll()
	 */
	synchronized public void startGame() {
		gameState = true;
		notifyAll();
	}
	/**
	 * Thread의 run 함수<br>
	 * block을 움직인다
	 */
	@Override
	public void run() {
		while(true) {
			checkGameState();
			try {
				Thread.sleep(moveDelay);
				
				if(getParent() != null && getX()+getWidth()>=getParent().getWidth()) {
					moveX = -(moveDirection*moveValue);
					lastAttackBlock = null;
				}
				else if(getX()<=0) {
					moveX = moveDirection*moveValue;
					lastAttackBlock = null;
				}
				setLocation(getX() + moveX, getY());
				
				if(getParent()!=null && ((GamePanel) getParent()).checkBlockSide(moveX,this,lastAttackBlock)) {
					if(moveX>0)
						moveX = -(moveDirection*moveValue);
					else
						moveX = moveDirection*moveValue;
				}
				
			} catch (InterruptedException e) { return; }	
		}
	}
}
