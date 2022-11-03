package XMLWritingTool;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * 게임 Block설정 제작 Panel
 * 
 * @author 김경미
 */
public class BlockToolsPanel extends JPanel {
	private GameGroundPanel groundPanel = null;
	private WritingBlock block = new WritingBlock();
	private BlockTypeSetPanel blockTypePanel = null;
	/**
	 * BlockToolsPanel 생성자
	 * @param groundPanel 게임화면 패널
	 */
	public BlockToolsPanel(final GameGroundPanel groundPanel) {
		this.groundPanel = groundPanel;
		
		setLayout(null);
		
		blockTypePanel = new BlockTypeSetPanel(block, 300, 120);
		splitPane();
		setPanel();
	}
	/**
	 * BlockToolsPanel에 있는 JTabbedPane의 위치 설정하는 함수<br>
	 *  : Tab에서 Bloc Type 설정
	 */
	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		hPane.setEnabled(false); // splitPane 위치 고정
		hPane.setDividerSize(0);
		hPane.setBorder(null);
		hPane.setLocation(0,0);
		hPane.setSize(300,150);
		
		hPane.setTopComponent(blockTypePanel);
		hPane.setBottomComponent(null);
		add(hPane);
	}
	/**
	 * BlockToolsPanel의 기본 제작 component 설정 함수
	 */
	private void setPanel() {
		JButton imgBtn = new JButton("Block Image");
		imgBtn.setSize(120,20);
		imgBtn.setLocation(20,150);
		imgBtn.setBackground(new Color(194,211,235));
		imgBtn.setToolTipText("블록의 이미지");
		imgBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectImageDialog selectFileFrame = new SelectImageDialog(block, ((JButton)e.getSource()).getParent());
			}
		});
		add(imgBtn);
		
	    JButton addBtn = new JButton("add");
	    addBtn.setBackground(new Color(51,96,155));
	    addBtn.setForeground(Color.white);
	    addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(block.checkImg())
					groundPanel.addBlock(block);
			}
		});
	    addBtn.setSize(100,20);
	    addBtn.setLocation(150,150);
		add(addBtn);
		
		block.setSize(80,80);
		block.setLocation(0,180);
		add(block);

		JSlider xSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, 60);
		xSlider.setPaintLabels(false);
		xSlider.setPaintTicks(false);
		xSlider.setPaintTrack(true);
		xSlider.setMajorTickSpacing(50);
		xSlider.setMinorTickSpacing(10);  
		xSlider.setToolTipText("블럭 이미지의 너비 값");
		xSlider.addChangeListener(new ChangeListener(){
	    @Override
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source=(JSlider)e.getSource();
	    		int val = (int) source.getValue();
	    		block.setSize(val,block.getHeight());
	    	}
	    });
		xSlider.setSize(200,20);
		xSlider.setLocation(40,330);
	    add(xSlider);
	    
	    JSlider ySlider = new JSlider(JSlider.HORIZONTAL, 0, 150, 60);
	    ySlider.setPaintLabels(true);
	    ySlider.setPaintTicks(true);
	    ySlider.setPaintTrack(true);
	    ySlider.setMajorTickSpacing(50);
	    ySlider.setMinorTickSpacing(10);  
	    ySlider.setToolTipText("블럭 이미지의 높이 값");
	    ySlider.addChangeListener(new ChangeListener(){
	    @Override
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source=(JSlider)e.getSource();
	    		int val = (int) source.getValue();
	    		block.setSize(block.getWidth() ,val);
	    	}
	    });
	    ySlider.setSize(200,45);
	    ySlider.setLocation(40,355);
	    add(ySlider); 
	}
}
