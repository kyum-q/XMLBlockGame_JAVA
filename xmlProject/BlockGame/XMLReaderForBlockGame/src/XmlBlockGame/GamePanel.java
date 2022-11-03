package XmlBlockGame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Block 게임 실행 Panel
 * 
 * @author 김경미
 */
public class GamePanel extends JPanel {
	private ImageIcon bgImg;
	private User user = null;
	private Node gamePanelNode = null;
	private JLabel life = null;
	private Attack attack[] = null;
	private AttackThread attackThread[] = null; 
	private DontGoneBlock block[] = null;
	private BlockGameFrame gameFrame = null;
	private Color aimColor = null;
	private Point aimPoint;
	private int ready=-1, mouseOn = 0, gameValue = 0, score, blockMaxCount = 0, blockCount = 0;
	private int attackCount = 1, firstDownAttack=0, downAttack=0, startCheck = 0; // 시작하면 1 아니면 0
	/**
	 * GamePanel 생성자
	 * 
	 * @param gamePanelNode gamePanel 정보를 얻기 위한 xml Node
	 * @param gameFrame gameFrame
	 */
	public GamePanel(Node gamePanelNode, BlockGameFrame gameFrame) {
		setLayout(null);
		this.gamePanelNode = gamePanelNode;
		this.gameFrame = gameFrame;
		Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.GAME_BG);
		bgImg = new ImageIcon(bgNode.getTextContent());
	}
	/**
	 * startCheck를 리턴하는 함수 <br>
	 * startCheck : 시작하면 1 아니면 0
	 * @return startCheck
	 */
	public int getStartCheck() { return startCheck; }
	/**
	 * startCheck 설정하는 함수
	 * @param i startCheck를 변경하는 값
	 */
	public void setStartCheck(int i) { startCheck = i; }
	/**
	 * blockMaxCount를 리턴하는 함수
	 * @return blockMaxCount (block의 최대 갯수)
	 */
	public int getBlockCount() { return blockMaxCount; }
	/**
	 * blockCount를 하나 줄이는 함수
	 */
	public void decreaseBlockCount() { 
		blockCount--; 
		if(blockCount<=0)
			gameOver();
	}
	/**
	 * 블럭을 세팅하는 함수
	 * @param nodeList block 정보를 얻기 위한 xml Node
	 */
	private void setBlock(NodeList nodeList) {
		blockCount = 0;
		block = new DontGoneBlock[nodeList.getLength()];
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				
				String type = XMLReader.getAttr(node, "type");
				
				if(type.equals("dontGone"))
					block[blockCount] = new DontGoneBlock(node);
				else if(type.equals("move"))
					block[blockCount] = new SideMoveBlock(node);
				else if(type.equals("gone"))
					block[blockCount] = new GoneBlock(node,gameFrame);
				else if(type.equals("moveAndGone"))
					block[blockCount] = new SideMoveAndGoneBlock(node,gameFrame);
				else
					continue;
				add(block[blockCount]);
				blockCount++;
			}
		}
		blockMaxCount = blockCount;
	}
	/**
	 * 게임을 일시 중단하는 함수
	 */
	public void gameStop() {
		for(int i=0;i<attackThread.length;i++) 
			if(attackThread[i]!=null) 
				attackThread[i].gameStop();
		for(int i=0;i<blockMaxCount;i++)
			if(block[i]!=null && block[i] instanceof SideMoveBlock) {
				((SideMoveBlock)block[i]).gameStop();
			}
		if(ready == 0) {
			ready = 1;
			gameValue = 1;
		}
	}
	/**
	 * 게임을 재시작하는 함수
	 */
	public void gameRePlay() {
		for(int i=0;i<attackThread.length;i++) 
			if(attackThread[i]!=null) 
				attackThread[i].startGame();
		
		for(int i=0;i<blockMaxCount;i++)
			if(block[i]!=null && block[i] instanceof SideMoveBlock) 
				((SideMoveBlock)block[i]).startGame();
		if(gameValue == 1) {
			ready = 0;
			gameValue = 0;
		}
	}
	/**
	 * 게임을 시작할 때 게임을 세팅하는 함수
	 */
	private void gameRun() {
		// read <Fish><Obj>s from the XML parse tree, make Food objects, and add them to the FishBowl panel. 
		startCheck = 1;
		Node UserNode = XMLReader.getNode(gamePanelNode, XMLReader.E_USER);
		ImageIcon icon = new ImageIcon(XMLReader.getAttr(UserNode, "img"));
		ImageIcon attackIcon = new ImageIcon(XMLReader.getAttr(UserNode, "attackImg"));

		int x = this.getWidth()/2;
		int y = this.getHeight();
		int w = Integer.parseInt(XMLReader.getAttr(UserNode, "w"));
		int h = Integer.parseInt(XMLReader.getAttr(UserNode, "h"));
		this.user = new User( x-(w/2),y-h,w,h, icon, attackIcon);
		add(user);

		addMouseListener(new AimMouseListener());
		addMouseMotionListener(new AimMouseMotionListener());
		
		Node aimNode = XMLReader.getNode(gamePanelNode, XMLReader.E_AIM);
		int r = Integer.parseInt(XMLReader.getAttr(aimNode, "r"));
		int g = Integer.parseInt(XMLReader.getAttr(aimNode, "g"));
		int b = Integer.parseInt(XMLReader.getAttr(aimNode, "b"));
		aimColor = new Color(r,g,b);
		
		Node attackNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ATTACK);
		attackCount = Integer.parseInt(XMLReader.getAttr(attackNode, "count"));
		attack = new Attack[attackCount];
		attackThread = new AttackThread[attackCount];
		icon = new ImageIcon(XMLReader.getAttr(attackNode, "img"));
		
		for(int i=0; i<attackCount;i++) {
			attack[i] = new Attack(user.getX()+user.getWidth()/2,user.getY()-20,icon);
			add(attack[i]);
		}
		Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		NodeList nodeList = blockNode.getChildNodes();
		setBlock(nodeList);
		
		repaint();
		gameFrame.startGameThread();
	}
	/**
	 * attack을 움직이게 하는 함수<br>
	 * attackThread를 생성해 움직여 공격 실행
	 */
	public void attack() {
		ready = 0;
		downAttack=0;
		for(int i=0; i<attackCount;i++)
			attackThread[i] = new AttackThread(gamePanelNode, attack[i], i, block, this);
	}
	/**
	 * attack 볼이 내려온 순서 확인하여 
	 * 맨 처음 내려온 함수를 다음 공격 시작 위치로 지정
	 * 마지막에 함수가 내려오면 다시 준비 동작으로 하는 함수
	 * @param i 내려온 볼의 attack index
	 */
	public void setDownAttack(int i) {
		if(downAttack==0)
			firstDownAttack = i;
		if(downAttack==attackCount-1) {
			setUser();
			setReady(-1);
		}
		downAttack++;
	}
	/**
	 * 게임을 종료시키는 함수
	 */
	public void gameOver() {
		for(int i=0;i<blockMaxCount;i++)
			if(block[i]!=null) 
				remove(block[i]);
		remove(user);
		for(int i=0; i<attackCount;i++)
			remove(attack[i]);
		ready = -1;
		startCheck = 0;
		blockMaxCount = 0;
		repaint();
		gameFrame.setGameSentence(1);
	}
	/**
	 * 공격한 뒤 블록이 내려오는 블럭인지 확인하고
	 * 그럴 경우 블럭을 움직이는 함수
	 */
	public void setBlockDown() {
		for(int i=0;i<blockMaxCount;i++) {
			if(block[i]!=null && block[i].checkBlockDown()) {
				block[i].setLocation(block[i].getX(),block[i].getY()+block[i].getHeight());
				if(block[i].getY()>=getHeight()-user.getHeight()-20-block[i].getHeight()) {
					if(block[i] instanceof GoneBlockInterface) {
						Node soundNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BALLSOUND);
						String die = XMLReader.getAttr(soundNode, "dieSound");
						Music dieSound = new Music(die,0);
						dieSound.play();
						gameFrame.decreaseLife();
					}
					remove(block[i]);
					block[i] = null;
					decreaseBlockCount();
					repaint();
					
				}
			}	
		}
	}
	/**
	 * user의 위치를 공격 후 attack이 맨 처음으로 떨어진 위치로 이동시키는 함수
	 */
	private void setUser() { 
		user.setImg(0); 
		for(int i=0; i<attackCount;i++)
			attack[i].setLocation(attack[firstDownAttack].getX(),getHeight()-user.getHeight()-attack[firstDownAttack].getHeight());
		user.setLocation(attack[firstDownAttack].getX()-user.getWidth()/2,attack[firstDownAttack].getY()+attack[firstDownAttack].getHeight()); 
	}
	/**
	 * 옆으로 움직이는 블록이 다른 블록과 만났을 시 반대편으로 움직이게 하는 함수
	 * 
	 * @param direction 현재 움직이는 블록의 방향
	 * @param myBlock 다른 블록과 만났는지 확인하고자 하는 움직이는 블록
	 * @param lastAttackBlock 움직이는 블록이 마지막으로 만난 블록
	 * @return 블록이 다른 블록과 만났을 경우 true
	 */
	public boolean checkBlockSide(int direction, SideMoveBlock myBlock, Point lastAttackBlock) { //direction<0이면 <-방향 || >0이면 ->방향
		for(int i=0;i<blockMaxCount;i++) {
			if(block[i]!=null && !(block[i].equals(myBlock)) && block[i].checkBlockMit(myBlock, 2)) {
				if(lastAttackBlock==null || !(block[i].getLocation().equals(lastAttackBlock)) ) {
					if(direction<0 &&  block[i].checkBlockMit(myBlock, 1)) {
						myBlock.setLastAttackBlockPoint(block[i].getLocation());
						return true;
					}
					if(direction>0 &&  block[i].checkBlockMit(myBlock, 1)) {
						myBlock.setLastAttackBlockPoint(block[i].getLocation());
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * ready 정수 인자를 리턴하는 함수 <br>
	 * ready : 0일 경우 attack 조준점 잡는, 1일 경우 attack 움직이는, -1일 경우 조준점 잡기 전 game set 
	 * 
	 * @return ready 리턴
	 */
	public int getReady() { return ready; }
	/**
	 * ready를 설정
	 * @param ready 설정하고자 하는 ready 값
	 */
	private void setReady(int ready) { this.ready = ready; }
	/**
	 * aimPoint를 알아내기
	 * @return aimPoint 리턴
	 */
	public Point getAimPoint() { return aimPoint; }
	/**
	 * 마우스가 움직일때마다 마우스 위치로 aimPoint 변경
	 * 
	 * @author 김경미
	 */
	public class AimMouseMotionListener extends MouseMotionAdapter  {
		@Override
		public void mouseMoved(MouseEvent e) { //해당 컴포넌트위에서 마우스가 움직일때 발생
			aimPoint = e.getPoint();
			mouseOn = 1;
			repaint();
		}
	}
	/**
	 * 마우스로 클릭하면 그 위치를 최종 aimPoint로 지정하는 함수
	 * 
	 * @author 김경미
	 */
	public class AimMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) { 
			if(ready==0) {
				aimPoint = e.getPoint();
				
				user.setImg(1);
				mouseOn = 0;
				repaint();
				for(int i=0; i<attackCount;i++)
					if(attackThread[i] != null && attackThread[i].getState() == Thread.State.NEW)
						attackThread[i].start();
				ready=1;
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {//마우스가 해당 컴포넌트 영역 밖으로 나갈때 발생
			mouseOn = 0;
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		if(startCheck==-1)
			gameRun();
		if(ready==0 && mouseOn==1) {
			g.setColor(aimColor);
			g.drawLine(attack[0].getX()+attack[0].getWidth()/2, attack[0].getY()+attack[0].getHeight()/2, aimPoint.x, aimPoint.y);
		}
	}
}
