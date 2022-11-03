package XmlBlockGame;
import java.awt.*;
import javax.swing.*;
import org.w3c.dom.Node;
/**
 * 게임의 움직이지 않고 사라지지 않은 블록 이미지 레이블<br> 
 * (extends JLabel)
 * 
 * @author 김경미
 */
public class DontGoneBlock extends JLabel {
	protected Image img;
	protected int blockDown;
	/**
	 * DontGoneBlock 생성자
	 * 
	 * @param node block 정보를 얻기 위한 xml Node
	 */
	public DontGoneBlock(Node node) {
		int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
		int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
		int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
		int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
		blockDown = Integer.parseInt(XMLReader.getAttr(node, "blockDown"));
		ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
		img = icon.getImage();
		
		this.setBounds(x,y,w,h);
	}
	/**
	 * 블럭과 label이 닿았는지 확인하는 함수
	 *  
	 * @param label block과 비교할 label
	 * @param i i==0면 모두 확인, i==1면 x만 확인, i==2면 y만 확인
	 * @return 닿았으면 true 안닿았으면 false
	 */
	public boolean checkBlockMit(JLabel label,int i) { // i==0면 all i==1면 x만 확인 i==2면 y만 확인
		if(i==0) {
			if(getX()<=label.getX()+label.getWidth() && getX()+getWidth()>=label.getX()
					&& getY()<=label.getY()+label.getHeight() && getY()+getHeight()>=label.getY())
				return true;
		}
		else if(i==1) {
			if(getX()<label.getX()+label.getWidth() && getX()+getWidth()>label.getX())
				return true;
		}
		else if(i==2) {
			if(getY()<label.getY()+label.getHeight() && getY()+getHeight()>label.getY()) { 
				return true;
			}
		}
		return false;
	}
	/**
	 * GoneBlock은 맞았는지 확인하고 맞았으면 true 리턴 
	 * 
	 * @param attack 블록에 맞았는지 비교한 label
	 * @return 블록이 attack에 맞았을 시 true
	 */
	public boolean blockAttack(JLabel attack) {
		if(checkBlockMit(attack,0))             
			return true;        
		return false;    
	}
	/**
	 * block이 내려가는 블록인지 확인하는 함수
	 * 
	 * @return block이 내려가는 블록이면 true 아니면 false
	 */
	public boolean checkBlockDown() {
		if(blockDown>0)
			return true;
		return false;
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}