package XMLWritingTool;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.w3c.dom.Node;
import XmlBlockGame.XMLReader;

/**
 * 게임 음악설정 제작 Panel
 * 
 * @author 김경미
 */
public class MusicToolsPanel extends JPanel {
	private GameGroundPanel groundPanel;
	private XmlString hitSoundObj, removeSoundObj, dieSoundObj;
	private XmlString winEndSoundObj, loseEndSoundObj, bgmObj;
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
	private JScrollPane scrollPane;
	private int tabCount = 0;
	/**
	 * MusicToolsPanel 생성자
	 * @param groundPanel 게임화면 패널
	 */
	public MusicToolsPanel(GameGroundPanel groundPanel) {
		this.groundPanel = groundPanel;
		
		setLayout(null);
		
		hitSoundObj = new XmlString("hitSound", groundPanel); 
		removeSoundObj = new XmlString("removeSound", groundPanel);
		dieSoundObj = new XmlString("dieSound", groundPanel);
		winEndSoundObj = new XmlString("winEndSound", groundPanel); 
		loseEndSoundObj = new XmlString("loseEndSound", groundPanel);
		bgmObj = new XmlString("backGroundSound", groundPanel);
		
		JButton hitSoundBtn = new JButton("Hit");
		hitSoundBtn.setSize(80,20);
		hitSoundBtn.setLocation(5,10);
		hitSoundBtn.setToolTipText("블록이 볼에 맞았을 때 나는 소리");
		hitSoundBtn.addActionListener(new MusicActionListener(hitSoundObj,hitSoundBtn));
		add(hitSoundBtn);
		
		JButton removeSoundBtn = new JButton("Remove");
		removeSoundBtn.setSize(80,20);
		removeSoundBtn.setLocation(95,10);
		removeSoundBtn.setToolTipText("블록을 없애 점수를 획득했을 때 나는 소리");
		removeSoundBtn.addActionListener(new MusicActionListener(removeSoundObj,removeSoundBtn));
		add(removeSoundBtn);
		
		JButton dieSoundBtn = new JButton("Die");
		dieSoundBtn.setSize(80,20);
		dieSoundBtn.setLocation(185,10);
		dieSoundBtn.setToolTipText("유저의 목숨이 줄어드는 소리");
		dieSoundBtn.addActionListener(new MusicActionListener(dieSoundObj,dieSoundBtn));
		add(dieSoundBtn);
		
		JButton winEndSoundBtn = new JButton("Win");
		winEndSoundBtn.setSize(80,20);
		winEndSoundBtn.setLocation(5,40);
		winEndSoundBtn.setToolTipText("게임이 승리로 끝났을 때 나는 엔딩 음악");
		winEndSoundBtn.addActionListener(new MusicActionListener(winEndSoundObj,winEndSoundBtn));
		add(winEndSoundBtn);
		
		JButton loseEndSoundBtn = new JButton("Lose");
		loseEndSoundBtn.setSize(80,20);
		loseEndSoundBtn.setLocation(95,40);
		loseEndSoundBtn.setToolTipText("게임이 패배로 끝났을 때 나는 엔딩 음악");
		loseEndSoundBtn.addActionListener(new MusicActionListener(loseEndSoundObj,loseEndSoundBtn));
		add(loseEndSoundBtn);
		
		JButton backGroundSoundBtn = new JButton("BGM");
		backGroundSoundBtn.setSize(80,20);
		backGroundSoundBtn.setLocation(185,40);
		backGroundSoundBtn.setToolTipText("게임 배경 음악");
		backGroundSoundBtn.addActionListener(new MusicActionListener(bgmObj,backGroundSoundBtn));
		add(backGroundSoundBtn);
		
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		hPane.setEnabled(false); // splitPane 위치 고정
		hPane.setLocation(0,80);
		hPane.setSize(280,300);
		hPane.setTopComponent(pane);
		hPane.setBottomComponent(null);
		add(hPane);
	}
	/**
	 * 게임 제작에 필요한 기본 설정이 다 되었는지 확인하는 함수
	 * @return 필요한 설정이 모두 되어있으면 true 아니면 false 리턴
	 */
	public boolean checkMusicXml() {
		//XmlString hitSoundObj, removeSoundObj, dieSoundObj;
		//XmlString winEndSoundObj, loseEndSoundObj, bgmObj;
		if(bgmObj.getObj() ==  null)
			return true;
		return false;
	}
	/**
	 * xml을 수정하는 경우에 사용<br>
	 * xml에서 파일을 읽어 설정된 정보를 패널에 표시하는 함수
	 * @param musicPanelNode 정보를 얻기 위한 xml Node
	 */
	public void setXmlMusic(Node musicPanelNode) {
		Node gameSoundNode = XMLReader.getNode(musicPanelNode, XMLReader.E_GAMESOUND);
		String bgm, loseEndBgm, winEndBgm, hit, remove, die;
		bgm = XMLReader.getAttr(gameSoundNode, "backGroundSound");
		bgmObj.setObj(bgm);
		loseEndBgm = XMLReader.getAttr(gameSoundNode, "loseEndSound");
		loseEndSoundObj.setObj(loseEndBgm);
		winEndBgm = XMLReader.getAttr(gameSoundNode, "winEndSound");
		winEndSoundObj.setObj(winEndBgm);
		Node ballNode = XMLReader.getNode(musicPanelNode, XMLReader.E_BALLSOUND);
		hit = XMLReader.getAttr(ballNode, "hitSound");
		hitSoundObj.setObj(hit);
		remove = XMLReader.getAttr(ballNode, "removeSound");
		removeSoundObj.setObj(remove);
		die = XMLReader.getAttr(ballNode, "dieSound");
		dieSoundObj.setObj(die);
	}
	/**
	 * 파일을 저장할 때 파일에 tool을 이용해 설정된 것들을 입력하는 함수
	 *
	 * @param file 저장할 파일 이름
	 */
	public void xmlMusicWriting(File file) {
		try{
			FileOutputStream fw = new FileOutputStream(file, true); // true는 원래 있던 txt 파일에 이어서쓰기 위해 존재 
			BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(fw, "utf-8"));
            if(file.isFile() && file.canWrite()){
                //쓰기
            	//XmlString hitSoundObj, removeSoundObj, dieSoundObj;
        		//XmlString winEndSoundObj, loseEndSoundObj, bgmObj;
                bufferedWriter.write(
                		  "    <GamePanel>\r\n"
                		+ "        <Sound> \r\n"
                		+ "            <BallSound "+hitSoundObj.getString()+" \r\n"
                		+ "                "+removeSoundObj.getString()+" \r\n"
                		+ "                "+dieSoundObj.getString()+" />\r\n"
                		+ "            <GameSound "+winEndSoundObj.getString()+" \r\n"
                		+ "                "+loseEndSoundObj.getString()+" \r\n"
                		+ "                "+bgmObj.getString()+" />\r\n"
                		+ "        </Sound>\r\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        }catch (IOException e) {
            return;
        }
	}
	/**
	 * music 선택을 위한 JButton의 ActionListener<br>
	 *  : 클릭할 경우 SelectImageDialog 실행
	 * 
	 * @author 김경미
	 */
	private class MusicActionListener implements ActionListener {
		private XmlString xmlObj;
		private boolean clickCheck = false;
		private JScrollPane scroll = null;
		/**
		 * MusicActionListener의 생성자
		 * @param xmlObj 선택한 이미지 값인 xmlString
		 * @param btn ActionListener가 추가된 btn
		 */
		public MusicActionListener(XmlString xmlObj, JButton btn) {
			this.xmlObj = xmlObj;
			xmlObj.setButton(btn);
			btn.setBackground(new Color(194,211,235));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!clickCheck) {
				SelectMusicPanel selectMusicPanel =new SelectMusicPanel(xmlObj);
				scroll = new JScrollPane(selectMusicPanel);
				scroll.setPreferredSize(new Dimension(100, 100));
				pane.add(scroll, xmlObj.getObjName());
				pane.setSelectedIndex(tabCount++);
				clickCheck = true;
			}
			else {
				pane.remove(scroll);
				clickCheck = false;
				tabCount--;
			}
		}			
	}
}
