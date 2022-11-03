package XMLWritingTool;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 게임의 제작을 위한 Block 이미지 레이블 (extends JLabel)
 * @author 김경미
 */
public class WritingBlock extends JLabel {
	private String type = "dontGone";
	private String imgName = null;
	private int w=80, h=80, x, y, score=0, hitCount=0, moveDelay=0, moveDirection=0, blockDown=-1;
	private int goneCheck=0, moveSideCheck=0;
	private Image img;
	private SetSideMove setSideMove = null;
	private SetGone setGone = null;
	/**
	 * block을 복제해서 새로운 block 리턴하는 함수
	 * @return 복제된 block 리턴
	 */
	public WritingBlock blockCopy() {
		WritingBlock copy = new WritingBlock();
		
		copy.setSize(this.w,this.h);
		copy.setImg(imgName);
		if(goneCheck==1)
			setGone.finalSetBlock(copy);
		if(moveSideCheck==1)
			setSideMove.finalSetBlock(copy);
		copy.setMoveDownBlock(blockDown);
		
		return copy;
	}
	/**
	 * 블록이 사라지는 블록인지 체크하는 함수<br>
	 * @return goneCheck가 1일 시(사라지는 블록일 시) true
	 */
	public boolean goneCheck() { 
		if(goneCheck == 1)
			return true;
		return false;
	}
	/**
	 * 블록이 좌우로 움직이는 블록인지 체크하는 함수<br>
	 * @return moveSideCheck가 1일 시(좌우로 움직이는 블록일 시) true
	 */
	public boolean moveSideCheck() { 
		if(moveSideCheck == 1)
			return true;
		return false;
	}
	/**
	 * blockDown을 리턴하는 함수
	 * @return blockDown (1이면 내려가는 block -1이면 안내려가는 block)
	 */
	public int getBlockDown() { return blockDown; }
	/**
	 * hitCount을 리턴하는 함수
	 * @return hitCount
	 */
	public int getHitCount() { return hitCount; }
	/**
	 * score을 리턴하는 함수
	 * @return score
	 */
	public int getScore() { return score; }
	/**
	 * moveDelay을 리턴하는 함수
	 * @return moveDelay
	 */
	public int getMoveDelay() { return moveDelay; }
	/**
	 * moveDirection을 리턴하는 함수
	 * @return moveDirection
	 */
	public int getMoveDirection() { return moveDirection; }
	/**
	 * 사라지는 블록일 시 사라지는 블록에 필요한 값 변경하는 함수
	 * @param hitCount 블록이 맞아야하는 정수형 인자
	 * @param score 블록이 맞아 사라졌을 때 얻는 점수 정수형 인자
	 */
	public void setGoneBlock(int hitCount, int score) {
		this.hitCount = hitCount;
		this.score = score;
		goneCheck=1;
	}
	/**
	 * 사라지는 블록인지 아닌지 설정하는 함수
	 * @param goneCheck 사라지는 블록일 시 1, 아닐 시 0
	 * @param setGone 사라지는 블록일 시 값을 가지고 있는 SetGone
	 */
	public void setGoneCheck(int goneCheck, SetGone setGone) { 
		this.goneCheck = goneCheck; 
		if(goneCheck==1)
			this.setGone = setGone;
	}
	/**
	 * 좌우로 움직이는 블록일 시 사라지는 블록에 필요한 값 변경하는 함수
	 * @param moveDelay 좌우로 움직이는 Delay 정수형 인자
	 * @param moveDirection 움직이는 방향과 움직이는 거리 정수형 인자
	 */
	public void setMoveSideBlock(int moveDelay, int moveDirection) {
		this.moveDelay = moveDelay;
		this.moveDirection = moveDirection;
		 moveSideCheck=1;
	}
	/**
	 * 움직이는 블록인지 아닌지 설정하는 함수
	 * @param moveSideCheck 움직이는 블록일 시 1, 아닐 시 0
	 * @param setSideMove 움직이는 블록일 시 값을 가지고 있는 SetSideMove
	 */
	public void setMoveCheck(int moveSideCheck,SetSideMove setSideMove) { 
		this.moveSideCheck = moveSideCheck; 
		if(moveSideCheck==1)
			this.setSideMove = setSideMove;
	}
	/**
	 * blockDown 설정하는 함수
	 * @param blockDown blockDown를 변경하는 값
	 */
	public void setMoveDownBlock(int blockDown) {
		this.blockDown = blockDown;
	}
	@Override
	public void setSize(int w,int h) {
		super.setSize(w, h);
		this.w = w;
		this.h = h;
	}
	@Override
	public void setLocation(int x,int y) {
		this.x = x;
		this.y = y;
		super.setLocation(this.x, this.y);	
	}
	/**
	 * 이미지 설정하는 함수
	 * @param imgName 이미지 파일 이름
	 */
	public void setImg(String imgName) {
		this.imgName = imgName;
		ImageIcon icon = new ImageIcon(imgName);
		img = icon.getImage();
		if(this.getParent() != null)
			this.getParent().repaint();
	}
	/**
	 * 이미지가 현재 있는지 체크하는 함수
	 * @return 이미지가 null일 시 false, 아닐 시 true 
	 */
	public boolean checkImg() {
		if(this.imgName == null)
			return false;
		return true;
	}
	/**
	 * xml 문자열로 바꿔서 리턴하는 함수
	 * @return xml이 block type에 따라서 변경해서 리턴 
	 */
	public String getString() {
		if(imgName==null)
			imgName = "";
		String basic = "x=\""+x+"\" y=\""+y+"\" w=\""+w+"\" h=\""+h+"\" blockDown=\""+blockDown+"\" img=\""+imgName+"\"";
		if(moveSideCheck == 0) {
			if(goneCheck == 0)
				return "<Obj type=\"dontGone\" "+basic+" />\r\n";
			else
				return "<Obj type=\"gone\" "+basic+" hitCount=\""+hitCount+"\" score=\""+score+"\" />\r\n";
		}
		else {
			if(goneCheck == 0)
				return "<Obj type=\"move\" "+basic+" moveDelay=\""+moveDelay+"\" moveDirection=\""+moveDirection+"\" />\r\n";
			else
				return "<Obj type=\"moveAndGone\" "+basic+" moveDelay=\""+moveDelay+"\" moveDirection=\""+moveDirection+"\" hitCount=\""+hitCount+"\" score=\""+score+"\" />\r\n";
		}
	}
	@Override
	public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}	
}
