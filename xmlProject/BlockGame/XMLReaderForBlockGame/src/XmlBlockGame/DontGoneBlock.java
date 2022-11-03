package XmlBlockGame;
import java.awt.*;
import javax.swing.*;
import org.w3c.dom.Node;
/**
 * ������ �������� �ʰ� ������� ���� ���� �̹��� ���̺�<br> 
 * (extends JLabel)
 * 
 * @author ����
 */
public class DontGoneBlock extends JLabel {
	protected Image img;
	protected int blockDown;
	/**
	 * DontGoneBlock ������
	 * 
	 * @param node block ������ ��� ���� xml Node
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
	 * ������ label�� ��Ҵ��� Ȯ���ϴ� �Լ�
	 *  
	 * @param label block�� ���� label
	 * @param i i==0�� ��� Ȯ��, i==1�� x�� Ȯ��, i==2�� y�� Ȯ��
	 * @return ������� true �ȴ������ false
	 */
	public boolean checkBlockMit(JLabel label,int i) { // i==0�� all i==1�� x�� Ȯ�� i==2�� y�� Ȯ��
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
	 * GoneBlock�� �¾Ҵ��� Ȯ���ϰ� �¾����� true ���� 
	 * 
	 * @param attack ���Ͽ� �¾Ҵ��� ���� label
	 * @return ������ attack�� �¾��� �� true
	 */
	public boolean blockAttack(JLabel attack) {
		if(checkBlockMit(attack,0))             
			return true;        
		return false;    
	}
	/**
	 * block�� �������� �������� Ȯ���ϴ� �Լ�
	 * 
	 * @return block�� �������� �����̸� true �ƴϸ� false
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