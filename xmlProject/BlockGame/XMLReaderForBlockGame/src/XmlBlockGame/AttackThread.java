package XmlBlockGame;
import java.awt.*;
import javax.swing.*;
import org.w3c.dom.Node;
/**
 * 	attack�� �������� ��Ÿ���� Thread
 * 
 * @author ����
 */
public class AttackThread extends Thread {
	/**
	 * @param delay ���� �����̴� �ӵ�
	 * @param ballDelay ���� ���� �ӵ� ����
	 * @param moving attack�� x �Ÿ� ������
	 * @param startX ���� x��ǥ
	 * @param startY ���� y��ǥ
	 * @param reboundX ƨ���� �� x��ǥ
	 * @param reboundY ƨ���� �� y��ǥ
	 * @param directionY y�� �Ʒ��� �������� -1 ���� �ö󰡸� 1
	 * @param formulaY ������ ���� x�������� ���� y������
	 * @param lastAttackBlock ���������� ���� block (�Ȱ����ſ� �ι� ƨ��°� ����)
	 * @param hit block�� attack�� �¾��� �� ���� ���� ���� �̸�
	 * @param remove block�� attack�� �¾� ����� �� ���� ���� ���� �̸�
	 * @param gameState attackThread�� ���¸� ��Ÿ���� ���� ����(���� �ߴ܉���� ����������)
	 */
	private int delay, ballDelay, moving = 5;
	private int x, y, startX, startY, reboundX, reboundY;
	private int directionY = 1, lastAttackBlock = -1, i = 0;
	private double formulaY;
	private Attack attack;
	private DontGoneBlock block[] = null;
	private GamePanel gamePanel = null;
	private String hit, remove;
	private boolean gameState = true;
	/**
	 * AttackThread�� ������
	 * 
	 * @param gamePanelNode attack ������ ��� ���� xml Node
	 * @param attack attack �̹���
	 * @param i ���° attack ������ �˷��ִ� ������ ����
	 * @param block attack���� �������ϴ� block��
	 * @param gamePanel ���� ���� �г�
	 */
	public AttackThread(Node gamePanelNode,Attack attack, int i, DontGoneBlock block[], GamePanel gamePanel) {
		Node attackNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ATTACK);
		this.gamePanel = gamePanel;
		this.attack = attack;
		this.i = i;
		this.block = block;
		this.reboundY = this.startY = this.y = attack.getY();
		this.reboundX = this.startX = this.x = attack.getX();
		
		delay = Integer.parseInt(XMLReader.getAttr(attackNode, "delay"));
		ballDelay = Integer.parseInt(XMLReader.getAttr(attackNode, "ballCountDelay"));
		Node soundNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BALLSOUND);
		hit = XMLReader.getAttr(soundNode, "hitSound");
		remove = XMLReader.getAttr(soundNode, "removeSound");
	}
	/**
	 * attack�� �����̴� ������ �˾Ƴ��� �Լ�<br>
	 * : ������ ����Ʈ �� ���̿� ������ �˾Ƴ��� x�� ���� ���� ���� y�� ���� �� ���ϱ�
	 * 
	 * @param p �������� ������ ���ϰ��ϴ� ������ ����Ʈ
	 */
	private void nextXY(Point p) {
		double ratioX, ratioY;
		if(p.y == reboundY || p.x == startX && p.y == startY) {
			// ���ٿ���� y��ǥ�� �������� y��ǥ�� ���ų� 
			// ���� ���� �����ϰ� �Ȱ��� ��� ���� �������� �ʰ� ���ٿ�� ���� �������� ����
			reboundX = x;
			reboundY = y;
		}
		else {
			// ���� ��찡 �ƴѰ�� ������ ������ ������ ���� ���ϱ�
			ratioX = p.x - x;
			ratioY = p.y - y;
			formulaY = ratioY/ratioX;
			if(ratioX>0) 
				moving = -moving;
			
			startX = reboundX;
			startY = reboundY;
		}
	}
	/**
	 * ������ �ߴ��ϴ� �Լ�<br> 
	 * gameState�� false�� ����
	 */
	public void gameStop() { gameState = false; }
	/**
	 * ���� ���� ����(gameState)�� Ȯ���ϴ� �Լ�<br>
	 * gameState�� false�� ��� wait()
	 */
	synchronized private void checkGameState() {
		if(!gameState) {
			try { this.wait(); }
			catch (InterruptedException e) { return; }
		}
	}
	/**
	 * ������ ������ϴ� �Լ�<br> 
	 * gameState�� true�� ����� wait�� �͵��� notifyAll()
	 */
	synchronized public void startGame() {
		gameState = true;
		notifyAll();
	}
	/**
	 * Thread�� run �Լ�<br>
	 * attack�� �����δ�
	 */
	@Override
	public void run() {
		nextXY(gamePanel.getAimPoint());
		try {
			Thread.sleep(i*ballDelay); // �� ������ ������ �����
		} catch (InterruptedException e1) {
			return;
		}
		
		while(true) {
			checkGameState(); // ������ ���� ������ Ȯ��
			try {
				x -= moving;
				if(directionY == 1)
					y = y - (int)(moving*formulaY);
				else
					y = y + (int)(moving*formulaY);
				if(y<=0) 
					directionY = -directionY;
				if(y>=gamePanel.getHeight()) 
					interrupt();
				if(x<=0 || x>=gamePanel.getWidth()) {
					formulaY = -formulaY;
					moving = -moving;
					if(formulaY<0.1 && Math.random()>0.3) {
						formulaY -= 0.3;
					}
				}
				attack.setLocation(x,y);
				
				for(int i=0;i<gamePanel.getBlockCount();i++) {
					if(block[i]!=null && lastAttackBlock!=i && block[i].blockAttack(attack)) {
						// ������ ������ �¾��� ���
						Music hitSound = new Music(hit,0);
						hitSound.play();
						lastAttackBlock = i;
						
						nextXY(new Point(reboundX,reboundY));
						if(directionY==1)
							directionY = -directionY;
						else {
							formulaY = -formulaY;
							moving = -moving;
						}
						if(block[i] != null && gamePanel != null && 
								block[i] instanceof GoneBlockInterface && ((GoneBlockInterface)block[i]).checkHitCount()) {
							// ���ÿ� ������ �¾� ������� ���
							gamePanel.remove(block[i]);
							block[i] = null;
							Music removeSound = new Music(remove,0);
							removeSound.play();
							gamePanel.decreaseBlockCount();
							gamePanel.repaint();
						}
					}
				}
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				gamePanel.setDownAttack(this.i);
				return;
			}
		}
	}
}