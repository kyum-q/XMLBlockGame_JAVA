package XMLWritingTool;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.w3c.dom.Node;
import XmlBlockGame.XMLReader;

/**
 * 게임 기본설정 제작 Panel
 * 
 * @author 김경미
 */
public class BasicToolsPanel extends JPanel {
	private GameGroundPanel groundPanel = null;
	
	private XmlString aimObj, attackImgObj, attackingImgObj, userImgObj;
	
	private NumberField ballNumber = new NumberField(3);
	private NumberField attackDelay = new NumberField(3);
	private NumberField ballCountDelay = new NumberField(3);
	private NumberField userLife = new NumberField(3);
	private NumberField winScore = new NumberField(3);
	
	private JSlider xSlider = null;
	private JSlider ySlider = null;
	
	JButton aimBtn = new JButton("AimColor");
	
	/**
	 * BasicToolsPanel 생성자
	 * @param groundPanel 게임화면 패널
	 */
	public BasicToolsPanel(GameGroundPanel groundPanel) {
		this.groundPanel = groundPanel;
		setLayout(new FlowLayout());
		
		attackImgObj = new XmlString("AttackImg", groundPanel);
		aimObj = new XmlString("Aim",groundPanel,1);
		attackingImgObj = new XmlString("attackImg",groundPanel);
		userImgObj = new XmlString("UserImg", groundPanel);
		
		setTools();
		
	}
	/**
	 * BasicToolsPanel 기본 제작 component 설정 함수
	 */
	private void setTools() {
		add(new JLabel("User Img                                                          "));

		JButton attackingImgBtn = new JButton("attack User Image");
		attackingImgBtn.setToolTipText("공격할 때 유저의 이미지");
		attackingImgBtn.addActionListener(new ImgActionListener(attackingImgObj,attackingImgBtn));
		add(attackingImgBtn);
		JButton userImgBtn = new JButton("User Image");
		userImgBtn.setToolTipText("공격하지 않을 때 유저의 이미지");
		userImgBtn.addActionListener(new ImgActionListener(userImgObj,userImgBtn));
		add(userImgBtn);
		
		xSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, 60);
		xSlider.setPaintLabels(false);
		xSlider.setPaintTicks(false);
		xSlider.setPaintTrack(true);
		xSlider.setMajorTickSpacing(50);
		xSlider.setMinorTickSpacing(10);  
		xSlider.setToolTipText("유저 이미지의 너비 값");
		xSlider.addChangeListener(new ChangeListener(){
	    @Override
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source=(JSlider)e.getSource();
	    		int val = (int) source.getValue();
	    		groundPanel.setUserSize(val,-1);
	    	}
	    });
	    add(xSlider);
	    
	    ySlider = new JSlider(JSlider.HORIZONTAL, 0, 150, 60);
	    ySlider.setPaintLabels(true);
	    ySlider.setPaintTicks(true);
	    ySlider.setPaintTrack(true);
	    ySlider.setMajorTickSpacing(50);
	    ySlider.setMinorTickSpacing(10); 
	    ySlider.setToolTipText("유저 이미지의 높이 값");
	    ySlider.addChangeListener(new ChangeListener(){
	    @Override
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source=(JSlider)e.getSource();
	    		int val = (int) source.getValue();
	    		groundPanel.setUserSize(-1,val);
	    	}
	    });
	    add(ySlider);
	    
	    add(new JLabel("                                                                         "));
	    add(new JLabel("Attack Img                                                        "));
	    JButton attackImgBtn = new JButton("attack Image");
	    attackImgBtn.setToolTipText("공격 볼의 이미지");
		attackImgBtn.addActionListener(new ImgActionListener(attackImgObj,attackImgBtn));
		add(attackImgBtn);
		
		aimBtn.setBackground(new Color(194,211,235));
		aimBtn.setToolTipText("조준 점까지의 예상 경로 색상");
		aimBtn.addActionListener(new ColorchooseListener(aimObj));
		add(aimBtn);
		add(new JLabel("                                                                         "));
		add(new JLabel("Attack Delay"));
		attackDelay.setToolTipText("볼의 공격 속도");
		add(attackDelay);
		add(new JLabel("Ball Delay"));
		ballCountDelay.setToolTipText("볼들 사이의 공격 속도 차이");
		add(ballCountDelay);
		add(new JLabel("Attack Number"));
		ballNumber.setToolTipText("볼의 갯수");
		add(ballNumber);
		
		add(new JLabel("                                                                              "));
		add(new JLabel("                     "));
		
		add(new JLabel("User Life"));
		userLife.setToolTipText("유저의 생명 수");
		add(userLife);
		add(new JLabel("                  "));
		add(new JLabel("                 "));
		add(new JLabel("Win Score"));
		winScore.setToolTipText("게임 승리 점수");
		add(winScore);
		add(new JLabel("               "));
	}
	/**
	 * 게임 제작에 필요한 기본 설정이 다 되었는지 확인하는 함수
	 * @return 필요한 설정이 모두 되어있으면 true 아니면 false 리턴
	 */
	public boolean checkBasicXml() {
		//aimObj, attackImgObj, attackingImgObj, userImgObj
		if(userLife.getNumber()<1 || winScore.getNumber()<1)
			return true;
		if(attackImgObj.getObj() == null)
			return true;
		if(attackingImgObj.getObj() == null)
			return true;
		if(userImgObj.getObj() ==  null)
			return true;
		if(aimObj.getObj() ==  null)
			return true;
		if(ballNumber.getNumber()<1 || attackDelay.getNumber()<1)
			return true;
		return false;
	}
	/**
	 * xml을 수정하는 경우에 사용<br>
	 * xml에서 파일을 읽어 설정된 정보를 패널에 표시하는 함수
	 * @param gamePanelNode 정보를 얻기 위한 xml Node
	 */
	public void setXmlBasic(Node gamePanelNode) {
		Node UserNode = XMLReader.getNode(gamePanelNode, XMLReader.E_USER);
		attackingImgObj.setObj(XMLReader.getAttr(UserNode, "attackImg"));
		userImgObj.setObj(XMLReader.getAttr(UserNode, "img"));
		xSlider.setValue(Integer.parseInt(XMLReader.getAttr(UserNode, "w")));
		ySlider.setValue(Integer.parseInt(XMLReader.getAttr(UserNode, "h")));
		userLife.setText(XMLReader.getAttr(UserNode, "life"));
		
		Node aimNode = XMLReader.getNode(gamePanelNode, XMLReader.E_AIM);
		int r = Integer.parseInt(XMLReader.getAttr(aimNode, "r"));
		int g = Integer.parseInt(XMLReader.getAttr(aimNode, "g"));
		int b = Integer.parseInt(XMLReader.getAttr(aimNode, "b"));
		aimBtn.setBackground(new Color(r,g,b));
		aimObj.setObj("r=\""+r+"\" g=\""+g+"\" b=\""+b+"\"");
		// 어택 이미지 설정
		Node attackNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ATTACK);
		attackImgObj.setObj(XMLReader.getAttr(attackNode, "img"));
		
		ballNumber.setText(XMLReader.getAttr(attackNode, "count"));
		attackDelay.setText(XMLReader.getAttr(attackNode, "delay"));
		ballCountDelay.setText(XMLReader.getAttr(attackNode, "ballCountDelay"));
		Node score = XMLReader.getNode(gamePanelNode, XMLReader.E_FINALSCORE);
		winScore.setText(XMLReader.getAttr(score, "winScore"));
	}
	/**
	 * 파일을 저장할 때 파일에 tool을 이용해 설정된 것들을 입력하는 함수
	 * @param gameBg 게임 배경화면을 알아내기 위한 xmlString 인자
	 * @param file 저장할 파일 이름
	 */
	public void xmlBasicWriting(XmlString gameBg, File file) {
		try{
			FileOutputStream fw = new FileOutputStream(file, true); // true는 원래 있던 txt 파일에 이어서쓰기 위해 존재 
			BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(fw, "utf-8")); // 한국어 지원하는 utf-8로 설정
            if(file.isFile() && file.canWrite()){
                //쓰기
                bufferedWriter.write(
                		  "        "+gameBg.getString() +"\r\n"
                		+ "        <Attack count=\""+ballNumber.getNumber()+"\" delay=\""+attackDelay.getNumber()+"\" ballCountDelay=\""+ballCountDelay.getNumber()+"\" img=\""+attackImgObj.getObj()+"\"/>\r\n"
                		+ "        "+aimObj.getString()
                		+ "        <User w=\""+xSlider.getValue()+"\" h=\""+ySlider.getValue()+"\" life=\""+userLife.getNumber()+"\" \r\n"
                		+ "            "+attackingImgObj.getString()+" \r\n"
                		+ "            img=\""+userImgObj.writingObj()+"\" />\r\n"
                		+ "        <FinalScore winScore=\""+ winScore.getNumber()+"\" />\r\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        }catch (IOException e) { return; }
	}
}
/**
 * img 선택을 위한 JButton의 ActionListener<br>
 *  : 클릭할 경우 SelectImageDialog 실행
 * 
 * @author 김경미
 */
class ImgActionListener implements ActionListener {
	private XmlString xmlObj;
	/**
	 * ImgActionListener의 생성자
	 * @param xmlObj 선택한 이미지 값인 xmlString
	 * @param btn ActionListener가 추가된 btn
	 */
	public ImgActionListener(XmlString xmlObj, JButton btn) {
		this.xmlObj = xmlObj;
		xmlObj.setButton(btn);
		btn.setBackground(new Color(194,211,235));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		SelectImageDialog selectFileFrame = new SelectImageDialog(xmlObj, ((JButton)e.getSource()).getParent());
	}			
}
/**
 * color 선택을 위한 JButton의 ActionListener<br>
 *  : 클릭할 경우 JColorChooser 실행하여 컬러 선택
 * 
 * @author 김경미
 */
class ColorchooseListener implements ActionListener {
	private XmlString xmlObj;
	private JLabel label = null;
	/**
	 * ColorchooseListener의 생성자
	 * @param xmlObj 선택한 컬러 값인 xmlString
	 */
	public ColorchooseListener(XmlString xmlObj) {
		this.xmlObj = xmlObj;
	}
	/**
	 * ColorchooseListener의 생성자
	 * @param xmlObj 선택한 컬러 값인 xmlString
	 * @param label 선택한 컬러 값으로 변경시키고 싶은 label
	 */
	public ColorchooseListener(XmlString xmlObj, JLabel label) {
		this.xmlObj = xmlObj;
		this.label = label;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(null, "Color", Color.WHITE);
		
		if(color == null) {
			return;
		}
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		((JButton)e.getSource()).setBackground(color);
		if(label!=null)
			label.setForeground(color);
		xmlObj.setObj("r=\""+r+"\" g=\""+g+"\" b=\""+b+"\"");
	}
}