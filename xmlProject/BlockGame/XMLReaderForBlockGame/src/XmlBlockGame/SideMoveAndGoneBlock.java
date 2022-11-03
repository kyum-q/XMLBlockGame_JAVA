package XmlBlockGame;
import javax.swing.JLabel;

import org.w3c.dom.Node;
/**
 * ������ �¿�� �����̰� ������� ���� �̹��� ���̺� <br>
 * (extends SideMoveBlock implements GoneBlockInterface)
 * 
 * @author ����
 */
public class SideMoveAndGoneBlock extends SideMoveBlock implements GoneBlockInterface {
	
	protected int hitCount, score;
	protected BlockGameFrame gameFrame = null;
	/**
	 * SideMoveAndGoneBlock ������
	 * 
	 * @param node block ������ ��� ���� xml Node
	 * @param gameFrame score������ ���� GameFrame class
	 */
	public SideMoveAndGoneBlock(Node node, BlockGameFrame gameFrame) {
		super(node);
		this.gameFrame = gameFrame;
		this.score = Integer.parseInt(XMLReader.getAttr(node, "score"));
		this.hitCount  = Integer.parseInt(XMLReader.getAttr(node, "hitCount"));
	}
	/**
	 * hitCount �����ϴ� �Լ�<br>
	 * : hitCount�� 0���� ���� �� ���� score ������Ű��
	 * 
	 * @return ������ hitCount�� 0���� �۰ų� ���� �� true
	 */
	@Override
	public boolean checkHitCount() {
		if(hitCount<=0) {
			gameFrame.increaseScore(score);
			return true;
		}
		return false;
	}
	/**
	 * GoneBlock�� �¾Ҵ��� Ȯ���ϰ� �¾����� hitCount 1 �����ϰ� true ���� 
	 * 
	 * @param attack ���Ͽ� �¾Ҵ��� ���� label
	 * @return ������ attack�� �¾��� �� true
	 */
	@Override
	public boolean blockAttack(JLabel attack) {
		if(checkBlockMit(attack,0)) {
			hitCount--;
			return true;
		}
		return false;
	}

}