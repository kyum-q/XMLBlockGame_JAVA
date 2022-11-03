package XmlBlockGame;
import java.awt.*;
import javax.swing.*;
import org.w3c.dom.Node;
/**
 * Block 게임의 타이틀 혹은 게임 종료 후 Panel
 * 
 * @author 김경미
 */
public class GameInitPanel extends JPanel {
	private JLabel initText = new JLabel("");
	private Image img;
	private String text[] = new String[3];
	/**
	 * GameInitPanel 생성자
	 * 
	 * @param initGameNode initPanel 정보를 얻기 위한 xml Node
	 */
	public GameInitPanel(Node initGameNode) {
		Node fontNode = XMLReader.getNode(initGameNode, XMLReader.E_FONT);
		Node sentenceNode = XMLReader.getNode(initGameNode, XMLReader.E_GAMESENTENCE);
		int r = Integer.parseInt(XMLReader.getAttr(fontNode, "r"));
		int g = Integer.parseInt(XMLReader.getAttr(fontNode, "g"));
		int b = Integer.parseInt(XMLReader.getAttr(fontNode, "b"));
		Color color = new Color(r,g,b);
		Font font = new Font(XMLReader.getAttr(fontNode, "font"),Font.BOLD, 
				Integer.parseInt(XMLReader.getAttr(fontNode, "fontSize")));
		Node initBgNode = XMLReader.getNode(initGameNode, XMLReader.INIT_BG);
		ImageIcon initBgIcon = new ImageIcon(initBgNode.getTextContent());
		img = initBgIcon.getImage();
		
		text[0] = XMLReader.getAttr(sentenceNode, "start");
		text[1] = XMLReader.getAttr(sentenceNode, "win");
		text[2] = XMLReader.getAttr(sentenceNode, "lose");
		
		initText.setText(" ");
		initText.setFont(font); // font 설정
		initText.setForeground(color);
		
		setLayout(new BorderLayout());
		initText.setHorizontalAlignment(JLabel.CENTER);
		add(initText);
	}
	/**
	 * initPanel의 나타날 Text 선택하는 함수
	 * @param select 정해진 문자중 몇번째 index 정수형 인자
	 */
	public void setInitText(int select) {
		initText.setText(text[select]);
	}
	@Override
	public void paintComponent(Graphics g) { //call back 함수
		super.paintComponent(g); // 패널을 모두 지운다 -> 배경색을 칠한다
		g.drawImage(img, 0,0, this.getWidth(), this.getHeight(), null);
	}
}