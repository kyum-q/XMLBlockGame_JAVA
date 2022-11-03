package XMLWritingTool;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import XmlBlockGame.Music;
import XmlBlockGame.XMLReader;

/**
 * ������ ���� ȭ�� Panel
 * 
 * @author ����
 */
public class GameGroundPanel extends JPanel {
	private Image bgImg = null, attackImg = null;
	private WritingUser user = null;
	private WritingAttack attack = null;
	private Music music = null;
	private WritingBlock block[] = new WritingBlock[10000];
	private WritingBlock moveBlock =  null; 
	private int blockCount = 0;
	private boolean mouseClick = false, removeClick = false;
	/**
	 * GameGroundPanel ������
	 * @param music ��� ����
	 */
	public GameGroundPanel(Music music) {
		setLayout(null);
		this.music = music;
	}
	/**
	 * user�� attack ���� �Լ�
	 */
	public void setUserAndAttack() {
		user =  new WritingUser(this.getWidth()/2 - (60/2), this.getHeight()-60,60,60);
		user.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {//���콺�� �ش� ������Ʈ ���� �ȿ� ���� ��
				((WritingUser)e.getSource()).setImg(1);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				((WritingUser)e.getSource()).setImg(0);
			}
		});
		add(user);
		attack = new WritingAttack(user.getX()+user.getWidth()/2,user.getY()-20);
		add(attack);
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(mouseClick) {
					moveBlock.setLocation(e.getX() , e.getY());
				}
			}
		});
	}
	/**
	 * xmlObj�� ���� ���� ȭ�鿡 ��ȭ�� �ִ� ���� ��� ��ȭ��Ű�� �Լ�
	 * @param xmlObj xmlObj
	 */
	public void setGroundPanel(XmlString xmlObj) {
		if(xmlObj.equalsObjName("GameBg")) {
			setBackGroundImage(xmlObj.getObj());
		}
		else if(xmlObj.equalsObjName("AttackImg")) {
			setAttackImg(xmlObj.getObj());
		}
		else if(xmlObj.equalsObjName("UserImg")) {
			setUserImg(xmlObj.getObj());
		}
		else if(xmlObj.equalsObjName("attackImg")) {
			setAttackingImg(xmlObj.getObj());
		}
		else if(xmlObj.equalsObjName("backGroundSound")) {
			music.play(xmlObj.getObj());
		}
	}
	/**
	 * ����̹��� �ٲٴ� �Լ�
	 * @param bgName ��� �̹��� ���� �̸�
	 */
	private void setBackGroundImage(String bgName) {
		ImageIcon BgIcon = new ImageIcon(bgName);
		bgImg = BgIcon.getImage();
		repaint();
	}
	/**
	 * ���� �̹��� �ٲٴ� �Լ�
	 * @param imgName �̹��� ���� �̸�
	 */
	private void setAttackImg(String imgName) {
		attack.setImage(imgName);
	}
	/**
	 * ���� �̹��� �ٲٴ� �Լ�
	 * @param imgName �̹��� ���� �̸�
	 */
	private void setUserImg(String imgName) {
		user.setUserImg(imgName);
	}
	/**
	 * ������ ������ �� �̹��� �ٲٴ� �Լ�
	 * @param imgName �̹��� ���� �̸�
	 */
	private void setAttackingImg(String imgName) {
		user.setAttackingImg(imgName);
	}
	/**
	 * removeClick �����ϴ� �Լ�
	 * @param removeClick removeClick�� �����ϴ� ��
	 */
	public void setRemoveClick(boolean removeClick) { removeClick = this.removeClick; }
	/**
	 * removeClick�� �����ϴ� �Լ�
	 * @return removeClick ����
	 */
	public boolean getRemoveClick() { return removeClick; }
	/**
	 * user ������ �����ϴ� �Լ�
	 * @param w user ����
	 * @param h user ����
	 */
	public void setUserSize(int w,int h) {
		if(w==-1)
			w = user.getWidth();
		if(h==-1)
			h = user.getHeight();
		user.setSize(w,h);
		user.setLocation(this.getWidth()/2 - (w/2), this.getHeight()-h);
		attack.setLocation(user.getX()+user.getWidth()/2,user.getY()-20);
	}
	/**
	 * GameGroundPanel�� ũ�Ⱑ ��ȭ���� �� groundPanel 
	 * �� component ��ġ �����ϴ� �Լ�
	 */
	public void setGroundSize() {
		if(user == null) return;
		user.setLocation(this.getWidth()/2 - (user.getWidth()/2), this.getHeight()-user.getHeight());
		attack.setLocation(user.getX()+user.getWidth()/2,user.getY()-20);
	}
	/**
	 * user ��ġ �����ϴ� �Լ�
	 * @return user Size ����
	 */
	public Dimension getUserSize()  { return user.getSize(); }
	/**
	 * ������ �����ϴ� �Լ�
	 * @param removeBlock �����ϰ��� �ϴ� ����
	 */
	public void removeBlock(WritingBlock removeBlock) {
		int i=0;
		for(i=0;i<blockCount;i++) {
			if(block[i]!=null && block[i].equals(removeBlock))
				break;
		}
		if(blockCount==i)
			return;
		remove(block[i]);
		block[i] = null;
		repaint();
	}
	/**
	 * ������ �߰��ϴ� �Լ�
	 * @param addBlock �߰��ϰ��� �ϴ� ����
	 */
	public void addBlock(WritingBlock addBlock) {
		block[blockCount] = addBlock.blockCopy();
		block[blockCount].setLocation(10,10);
		add(block[blockCount]);
		repaint();
		
		block[blockCount].addMouseListener(new BlockMouseAdapter());
		blockCount++;
	}
	/**
	 * xml�� �����ϴ� ��쿡 ���<br>
	 * xml���� ������ �о� ������ ������ �гο� ǥ���ϴ� �Լ�
	 * @param gamePanelNode ������ ��� ���� xml Node
	 */
	public void setXmlBlock(Node gamePanelNode) {
		//block ����
		Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		NodeList nodeList = blockNode.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				block[blockCount] = new WritingBlock();
				
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				block[blockCount].setLocation(x,y);
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				block[blockCount].setImg(XMLReader.getAttr(node, "img"));
				block[blockCount].setSize(w,h);
				int blockDown = Integer.parseInt(XMLReader.getAttr(node, "blockDown"));
				block[blockCount].setMoveDownBlock(blockDown);
				
				String type = XMLReader.getAttr(node, "type");
				
				if(type.equals("move") || type.equals("moveAndGone")) {
					int moveDelay = Integer.parseInt(XMLReader.getAttr(node, "moveDelay"));
					int moveDirection = Integer.parseInt(XMLReader.getAttr(node, "moveDirection"));
					block[blockCount].setMoveSideBlock(moveDelay, moveDirection);
				}
				if(type.equals("gone") || type.equals("moveAndGone")) {
					int score = Integer.parseInt(XMLReader.getAttr(node, "score"));
					int hitCount  = Integer.parseInt(XMLReader.getAttr(node, "hitCount"));
					block[blockCount].setGoneBlock(hitCount, score);
				}
				block[blockCount].addMouseListener(new BlockMouseAdapter());
				add(block[blockCount]);
				repaint();
				
				blockCount++;
			}
		}
	}
	/**
	 * ������ ������ �� ���Ͽ� tool�� �̿��� ������ �͵��� �Է��ϴ� �Լ�
	 * @param file ������ ���� �̸�
	 */
	public void xmlBlockWriting(File file) {
		try{
			FileOutputStream fw = new FileOutputStream(file, true); // true�� ���� �ִ� txt ���Ͽ� �̾���� ���� ���� 
			BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(fw, "utf-8"));
            if(file.isFile() && file.canWrite()){
                //����
                bufferedWriter.write("        <Block>\r\n");
                for(int i=0;i<blockCount;i++) {
                	if(block[i]!=null)
                		bufferedWriter.write("                "+block[i].getString());
                }
                bufferedWriter.write(
                		  "        </Block>\r\n"
                		+ "    </GamePanel>\r\n"
                		+ "</BlockGame>");
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        }catch (IOException e) {
            return;
        }
	}
	@Override
	public void setSize(int w,int h) {
		super.setSize(w, h);
	}
	@Override
	public void paintComponent(Graphics g) { //call back �Լ�
		super.paintComponent(g); // �г��� ��� ����� -> ������ ĥ�Ѵ�
		g.drawImage(bgImg, 0,0, this.getWidth(), this.getHeight(), null);
	}
	/**
	 * block MouseAdapter<br>
	 *  : Ŭ������ ��� ��ġ �̵� ���� �ٽ� Ŭ�� �� ����, �� �� Ŭ�� �� block���� â
	 * 
	 * @author ����
	 */
	public class BlockMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) { 
			if(removeClick) {
				removeBlock(((WritingBlock)e.getSource()));
			}
			if(e.getClickCount() == 2) {
				BlockSetDialog blockSetDialog = new BlockSetDialog((WritingBlock) e.getSource());
				blockSetDialog.setVisible(true);
				mouseClick = false;
			}
			else if(!mouseClick) {
				mouseClick = true;
				moveBlock = (WritingBlock) e.getSource();
				((JPanel)moveBlock.getParent()).setFocusable(true);
				((JPanel)moveBlock.getParent()).requestFocus();
			}
			else
				mouseClick = false;
		}	
	}
}
/**
 * ������ user ĳ���� �ۼ��� ���� �̹������̺� (extends JLabel)
 * 
 * @author ����
 */
class WritingUser extends JLabel {
	Image img[] = new Image[2];
	int imgSelect = 0;
	int w= 60 ,h= 60;
	/**
	 * WritingUser ������
	 * 
	 * @param x user �̹��� ��ġ x��
	 * @param y user �̹��� ��ġ y��
	 * @param w user �̹��� ����
	 * @param h user �̹��� ����
	 */
	public WritingUser(int x, int y, int w, int h) {
		this.setBounds(x,y,w,h);
	}
	/**
	 * user�� �̹��� ��ȭ �Լ�
	 * @param imgName user�̹��� ���� �̸�
	 */
	public void setUserImg(String imgName) {
		ImageIcon icon = new ImageIcon(imgName);
		img[0] = icon.getImage();
		getParent().repaint();
		imgSelect = 0;
	}
	/**
	 * user�� ������ �� �̹��� ��ȭ �Լ�
	 * @param imgName user�̹��� ���� �̸�
	 */
	public void setAttackingImg(String imgName) {
		ImageIcon icon = new ImageIcon(imgName);
		img[1] = icon.getImage();
		if(img[0]==null);
			imgSelect = 1;
		getParent().repaint();
	}
	/**
	 * �̹��� ��ȯ���ִ� �Լ�
	 * 
	 * @param i i�� 0�� ���� �������� ���� �� �̹����� ��ȯ i�� 1�� ���� ������ �� �̹����� ��ȯ
	 */
	public void setImg(int i) {
		imgSelect = i;
		repaint();
	}
	@Override
	public void setSize(int w,int h) {
		this.w = w; this.h = h;
		super.setSize(this.w, this.h);
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img[imgSelect], 0, 0, this.getWidth(), this.getHeight(), this);
	}		
}
/**
 * ������ attack(���� ��) �ۼ��� ���� �̹������̺� (extends JLabel)
 * 
 * @author ����
 */
class WritingAttack extends JLabel {
	Image img = null;
	/**
	 * WritingAttack ������
	 * 
	 * @param x attack �̹��� ��ġ x��
	 * @param y attack �̹��� ��ġ y��
	 */
	public WritingAttack(int x, int y) {
		this.setBounds(x,y,20,20);
	}
	/**
	 * ���� �̹��� ��ȯ���ִ� �Լ�
	 * @param imgName attack �̹��� ���� �̸�
	 */
	public void setImage(String imgName) {
		ImageIcon icon = new ImageIcon(imgName);
		img = icon.getImage();
		getParent().repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}		
}