package XmlBlockGame;
import javax.swing.JLabel;

import org.w3c.dom.Node;
/**
 * 게임의 좌우로 움직이고 사라지는 블록 이미지 레이블 <br>
 * (extends SideMoveBlock implements GoneBlockInterface)
 * 
 * @author 김경미
 */
public class SideMoveAndGoneBlock extends SideMoveBlock implements GoneBlockInterface {
	
	protected int hitCount, score;
	protected BlockGameFrame gameFrame = null;
	/**
	 * SideMoveAndGoneBlock 생성자
	 * 
	 * @param node block 정보를 얻기 위한 xml Node
	 * @param gameFrame score변경을 위한 GameFrame class
	 */
	public SideMoveAndGoneBlock(Node node, BlockGameFrame gameFrame) {
		super(node);
		this.gameFrame = gameFrame;
		this.score = Integer.parseInt(XMLReader.getAttr(node, "score"));
		this.hitCount  = Integer.parseInt(XMLReader.getAttr(node, "hitCount"));
	}
	/**
	 * hitCount 점검하는 함수<br>
	 * : hitCount가 0보다 작을 시 게임 score 증가시키기
	 * 
	 * @return 블럭의 hitCount가 0보다 작거나 같을 시 true
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
	 * GoneBlock은 맞았는지 확인하고 맞았으면 hitCount 1 감소하고 true 리턴 
	 * 
	 * @param attack 블록에 맞았는지 비교한 label
	 * @return 블록이 attack에 맞았을 시 true
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
