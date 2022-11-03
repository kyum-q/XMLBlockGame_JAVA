package XMLWritingTool;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

/**
 * 게임 Block Type설정 제작 Panel
 * 
 * @author 김경미
 */
public class BlockTypeSetPanel extends JPanel {
	private JCheckBox gone = null;
	private JCheckBox moveSide = null;
	private JCheckBox moveDown = null;
	private JTabbedPane pane = new JTabbedPane();
	private SetGone setGone = new SetGone();
	private SetSideMove setSideMove = new SetSideMove();
	private int tabCount = 0, width = 0;
	private WritingBlock block;
	/**
	 * BlockTypeSetPanel 생성자
	 * 
	 * @param block type을 결정하고자 하는 block
	 * @param w JTabbedPane의 넓이
	 * @param h JTabbedPane의 높이
	 */
	public BlockTypeSetPanel(WritingBlock block, int w, int h) {
		super();
		setLayout(null);
		this.width = w;
		this.block = block;
		splitPane(w, h);
		setTab();
	}
	/**
	 * BlockToolsPanel에 있는 JTabbedPane의 위치 설정하는 함수<br>
	 *  : Tab에서 Bloc Type 설정
	 */
	private void splitPane(int w, int h) {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		hPane.setEnabled(false); // splitPane 위치 고정
		hPane.setLocation(0,30);
		hPane.setSize(w,h);
		
		hPane.setTopComponent(pane);
		hPane.setBottomComponent(null);
		add(hPane);
	}
	/**
	 * JTabbedPane을 block의 조건에 맞춰 설정하는 함수<br>
	 *  : block에 설정된 값을 확인할 수 있게 설정
	 */
	private void setTab() {
		if(block.goneCheck()) {
			gone = new JCheckBox("Gone",true);
			setGone.setGoneType(block);
    		 pane.add("Gone",setGone);
    		 pane.setSelectedIndex(tabCount++);
		}
		else
			gone = new JCheckBox("Gone",false);
		if(block.moveSideCheck()) {
			moveSide = new JCheckBox("Side",true);
			setSideMove.setSideMoveType(block);
			pane.add("SideMove",setSideMove);
			pane.setSelectedIndex(tabCount++);
		}
		else
			moveSide = new JCheckBox("Side",false);
		if(block.getBlockDown() == 1)
			 moveDown = new JCheckBox("Down",true);
		else
			 moveDown = new JCheckBox("Down",false);
		
		gone.setSize(60,20);
		gone.setLocation(0,5);
		gone.setToolTipText("사라지는 블록일 시 체크");
		gone.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 if(e.getStateChange()==1) {
	        		 block.setGoneCheck(1, setGone);
	        		 pane.add("Gone",setGone);
	        		 pane.setSelectedIndex(tabCount++);
	        	 }
	        	 else {
	        		 block.setGoneCheck(0, setGone);
	        		 pane.remove(setGone);
	        		 tabCount--;
	        	 }
	         }
	    });
		add(gone);
		moveSide.setSize(60,20);
		moveSide.setLocation(60,5);
		moveSide.setToolTipText("옆으로 움직이는 블록일 시 체크");
		moveSide.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 if(e.getStateChange()==1) {
	        		 block.setMoveCheck(1,setSideMove);
	        		 pane.add("SideMove",setSideMove);
	        		 pane.setSelectedIndex(tabCount++);
	        	 }
	        	 else {
	        		 block.setMoveCheck(0,setSideMove);
	        		 pane.remove(setSideMove);
	        		 tabCount--;
	        	 }
	         }
	    });
		add(moveSide);
		moveDown.setSize(60,20);
		moveDown.setLocation(120,5);
		moveDown.setToolTipText("유저가 공격할때마다 아래로 떨어지는 블록일 시 체크");
		moveDown.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 if(e.getStateChange()==1)
	        		 block.setMoveDownBlock(1);
	        	 else
	        		 block.setMoveDownBlock(0);
	         }
	    });
		add(moveDown);
	}
	/**
	 * 최종 blockType 설정하기
	 */
	public void finalSetBlocktype() {
		setGone.finalSetBlock(block);
		setSideMove.finalSetBlock(block);
	}
}
/**
 * 게임 Block Type 중 사라지는 블록 제작 Panel
 * 
 * @author 김경미
 */
class SetGone extends JPanel {
	private NumberField hitCount = new NumberField(3);
	private NumberField score = new NumberField(3);
	/**
	 * SetGone 생성자
	 */
	public SetGone() {
		add(new JLabel("HitCount"));
		hitCount.setToolTipText("블록의 목숨(몇번 맞아야지 사라지는 지)");
		add(hitCount);
		add(new JLabel("Score"));
		score.setToolTipText("블록을 없앴을 때 획득 점수");
		add(score);
	}
	/**
	 * setGonePanel을 block에 맞춰서 다시 설정하는 함수
	 * @param block 설정값이 들어있는 block
	 */
	public void setGoneType(WritingBlock block) {
		hitCount.setText(block.getHitCount());
		score.setText(block.getScore());
	}
	/**
	 * block에 사라지는 블록일 경우 필요한 값 설정하기
	 * @param block 설정할 block
	 */
	public void finalSetBlock(WritingBlock block) {
		block.setGoneBlock(hitCount.getNumber(), score.getNumber());
	}
}
/**
 * 게임 Block Type 중 좌우로 움직이는 블록 제작 Panel
 * 
 * @author 김경미
 */
class SetSideMove extends JPanel {
	private int moveDirection = 1;
	private NumberField moveDelay = new NumberField(3);
	private NumberField moveX = new NumberField(3);
	private JRadioButton rd1 = new JRadioButton("→");
	private JRadioButton rd2 = new JRadioButton("←");
	/**
	 * SetSideMove 생성자
	 */
	public SetSideMove() {
		setSize(300,200);
		
		add(new JLabel("       "));
		add(new JLabel("Delay"));
		moveDelay.setToolTipText("블록의 움직이는 속도");
		add(moveDelay);
		add(new JLabel("       "));
		add(new JLabel("   "));
		add(new JLabel("Distance"));
        moveX.setToolTipText("블록이 움직이는 거리");
		add(moveX);
		add(new JLabel("       "));
		
		JLabel direction = new JLabel("Direction");
		direction.setToolTipText("블록이 처음 움직이는 방향 선택");
		add(direction);
		
        rd1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				 if(e.getStateChange()==1)
					 moveDirection = 1;
			}
        });
        rd2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				 if(e.getStateChange()==1)
					 moveDirection = -1;
			}
        });
        // 1번 라디오 버튼 눌러져있도록
        rd1.setSelected(true);
        
        ButtonGroup groupRd = new ButtonGroup();

        groupRd.add(rd1);
        groupRd.add(rd2);

        add(rd1);
        add(rd2);
	}
	/**
	 * setSideMovePanel을 block에 맞춰서 다시 설정하는 함수
	 * @param block 설정값이 들어있는 block
	 */
	public void setSideMoveType(WritingBlock block) {
		moveDelay.setText(block.getMoveDelay());
		if(block.getMoveDirection()>0) {
			moveX.setText(block.getMoveDirection());
			rd1.setSelected(true);
		}
		else {
			moveX.setText(block.getMoveDirection()*-1);
			rd2.setSelected(true);
		}
	}
	/**
	 * block에 옆으로 움직이는 블록일 경우 필요한 값 설정하기
	 * @param block 설정할 block
	 */
	public void finalSetBlock(WritingBlock block) {
		block.setMoveSideBlock(moveDelay.getNumber(), moveDirection*moveX.getNumber());
	}
}