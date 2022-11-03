package XmlBlockGame;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Block 게임의 Frame을 설정하는 class
 * 
 * @author 김경미
 */
public class BlockGameFrame extends JFrame {
	/**
	 * 기본 함수 설명 <br><br>
	 * 
	 * xml xml파일을 읽어들인 XMLReader<br>
	 * gameThread 게임의 시작과 종료에 따라 움직이는 Thread<br>
	 * gamePanel 게임 진행을 담당하는 Panel<br>
	 * initPanel 게임 시작전 타이틀, 게임 종료 후 결과 창을 담당하는 Panel<br>
	 * music 배경 음악 BGM<br>
	 * endMusic 게임 종료 음악 (이겼을 때, 졌을 때 둘은 다른 음악이 나옴)<br>
	 * life 게임 user가 게임 시작시 가지는 목숨 수<br>
	 * finalScore 게임을 이기기 위한 점수 값인 정수형 인자<br>
	 * score 게임을 통해 얻게 된 user의 점수 값인 정수형 인자<br>
	 * lifeMaxCount 게임에서 처음 주는 목숨 값(최대 목숨 값)인 정수형 인자<br>
	 * lifeCount user가 가지고 있는 목숨 값인 정수형 인자<br><br>
	 */
	private XMLReader xml = null;
	private GameThread gameThread = null;
	private GamePanel gamePanel = null;
	private GameInitPanel initPanel = null;
	private Music music = null, endMusic[] = new Music[2];
	private JLabel life[] = null, lifeLabel = new JLabel("     Life: "), scoreLabel = new JLabel("  Score: "+ 0);
	private int finalScore = 0, score = 0, lifeMaxCount, lifeCount = 0;
	/**
	 * BlockGameFrame 생성자
	 * 
	 * @param xmlFileName xml 파일 이름 문자열
	 */
	public BlockGameFrame(String xmlFileName) {
		setXml(xmlFileName);
		makeMenu();
		setGameSentence(0);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Frame 창 닫으면 종료시키기
		setLocationRelativeTo(null); // 윈도우 가운데 배치
		setResizable(false); // 창크기 고정
		setVisible(true);
	}
	/**
	 * xml 파일 설정하고 frame 기본 설정하는 함수 (frame Size, frame MenuBar)
	 * 
	 * @param fileName xml 파일 이름 문자열
	 */
	private void setXml(String fileName) { 
		String loseEndBgm, winEndBgm, bgm, w, h;
		
		// xml set
		xml = new XMLReader(fileName); // XMLReader 생성 -> XML을 읽어와라
		Node blockGameNode = xml.getBlockGameElement(); 
		
		// frame Size 설정
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.FRAME_SIZE);
		w = XMLReader.getAttr(sizeNode, "w");
		h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w)+12, Integer.parseInt(h)+60);
		
		// 게임 사운드 xml에서 가져와서 설정
		Node musicNode = XMLReader.getNode(blockGameNode, XMLReader.E_GAMESOUND);
		bgm = XMLReader.getAttr(musicNode, "backGroundSound");
		loseEndBgm = XMLReader.getAttr(musicNode, "loseEndSound");
		winEndBgm = XMLReader.getAttr(musicNode, "winEndSound");
		music = new Music(bgm,1);
		endMusic[0] = new Music(winEndBgm,0);
		endMusic[1] = new Music(loseEndBgm,0);
		
		// 게임 정보 설정
		Node score = XMLReader.getNode(xml.getGamePanelElement(), XMLReader.E_FINALSCORE);
		finalScore =  Integer.parseInt(XMLReader.getAttr(score, "winScore")); 
		Node UserNode = XMLReader.getNode(xml.getGamePanelElement(), XMLReader.E_USER);
		lifeMaxCount = lifeCount = Integer.parseInt(XMLReader.getAttr(UserNode, "life"));
		
		// gamePanel 등록
		initPanel = new GameInitPanel(xml.getInitPanelElement());	
		gamePanel = new GamePanel(xml.getGamePanelElement(),this);
	}
	/**
	 * 게임의 시작과 종료에 따른 화면(Panel) 변화 도와주는 함수 
	 * ( 게임 시작, 게임 종료 후 게임의 승패 결정 후 결과에 따른 화면 송출 )
	 * 
	 * @param gameSituation 게임의 상황을 알려주는 정수형 인자
	 */
	public void setGameSentence(int gameSituation) {
		// gameSituation == 0 : gameStart
		if(gameSituation == 0) 
			initPanel.setInitText(0);
		// gameSituation == 1 : gameEnd
		if(gameSituation == 1) { // 최종 score 확인 후 게임의 승패 결정 (checkScore가 true일 경우 승리)
			if(checkScore())
				initPanel.setInitText(1);
			else
				initPanel.setInitText(2);
			endGameThread();
		}
		// gamePanel 변경
		setGamePanel(false);
	}
	/**
	 * gamePanel을 변경해주는 함수
	 *   : 게임 진행하면 GamePanel을 보이게 게임이 끝나거나 시작 전에는 GameInitPanel이 보이게 설정해줌
	 *   
	 *   @param check 어떤 패널을 표시해야하는지 알려주는 논리형 인자
	 */
	private void setGamePanel(boolean check) {
		if(check) { 
			add(gamePanel,BorderLayout.CENTER);
			initPanel.setVisible(false); // 게임 초기화면 숨기기
			gamePanel.setVisible(true); // 게임화면 나타내기
		}
		else {
			add(initPanel,BorderLayout.CENTER);
			initPanel.setVisible(true); // 게임화면 나타내기
			gamePanel.setVisible(false); // 게임 초기화면 숨기기
		}
	}
	/**
	 * MenuBar를 생성하는 함수<br>
	 * 
	 * (메뉴 기능 : 파일 열기/ 게임 시작, 중지, 재시작, 종료 / BGM 볼륨조절, on, off )
	 */
	private void makeMenu() {
		JMenuBar mBar = new JMenuBar();
		setJMenuBar(mBar);
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		fileMenu.add(open);
		open.addActionListener(new OpenActionListener());
		
		JMenu gameMenu = new JMenu("Game");
		gameMenu.addSeparator();
		JMenuItem goTitle = new JMenuItem("GoTitle");
		JMenuItem gameStart = new JMenuItem("Play");
		JMenuItem gameOver = new JMenuItem("Over");
		JMenuItem gameStop = new JMenuItem("Stop");
		JMenuItem rePlay = new JMenuItem("RePlay");
		
		gameMenu.add(goTitle);
		gameMenu.add(gameStart);
		gameMenu.add(gameOver);
		gameMenu.add(gameStop);
		gameMenu.add(rePlay);
		
		goTitle.addActionListener(new ActionListener() { // title 버튼을 누를시 게임 타이틀로 이동
			@Override
			public void actionPerformed(ActionEvent e) { 
				if(gamePanel.getStartCheck()==1) // 게임이 시작되었을 때
				gamePanel.gameOver();
				setGameSentence(0);
			}	
		});
		gameStart.addActionListener(new ActionListener() { // start 버튼을 누를시 게임 시작
			public void actionPerformed(ActionEvent e) {
				if(gamePanel.getStartCheck()==0) { // game이 시작되지 않았을 때
					gamePanel.setStartCheck(-1);
					setGameMenu();
					setGamePanel(true);
				}
			}
		}); 
		gameOver.addActionListener(new ActionListener() { // Over 버튼을 누를시 게임 종료
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gamePanel.getStartCheck()==1) { // 게임이 시작되었을 때
					endGameThread();
				}
			}
		}); 
		gameStop.addActionListener(new ActionListener() { // stop 버튼을 누를시 게임 일시 중단
			@Override
			public void actionPerformed(ActionEvent e) { 
				if(gamePanel.getStartCheck()==1) {
					music.stop();
					gameThread.gameStop();
					gamePanel.gameStop();
				}
			}	
		});
		rePlay.addActionListener(new ActionListener() { // rePlay 버튼을 누를시 게임 재시작
			@Override
			public void actionPerformed(ActionEvent e) { 
				if(gamePanel.getStartCheck()==1) {
					if(music.checkMusicState())
						music.play();
					gameThread.startGame();
					gamePanel.gameRePlay();
				}
			}	
		});
		
		JMenu musicMenu = new JMenu("Music");
		JMenuItem musicStart = new JMenuItem("Play");
		JMenuItem musicStop = new JMenuItem("Stop");
		JMenuItem VolUp = new JMenuItem("VolUp");
		JMenuItem VolDown = new JMenuItem("VolDown");
		musicMenu.add(musicStart);
		musicMenu.add(musicStop);
		musicMenu.add(VolUp);
		musicMenu.add(VolDown);
		
		musicStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				music.play(); 
			}	
		});
		musicStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				music.stop(); 
				music.setMusicState(false);
			}	
		});
		VolUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { music.volumeUp(); }	
		});
		VolDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { music.volumeDown(); }	
		});

		mBar.add(fileMenu);
		mBar.add(gameMenu);
		mBar.add(musicMenu);
		mBar.add(scoreLabel);
		mBar.add(lifeLabel);
		life = new JLabel[lifeMaxCount];
		for(int i=0;i<lifeMaxCount;i++) {
			life[i] = new JLabel("♥");
			life[i].setForeground(Color.RED);
			mBar.add(life[i]);
		}
	}
	/**
	 * gameMenu를 다시 세팅하는 함수 (life나 score를 수정)
	 */
	private void setGameMenu() {
		for(int i=0;i<lifeMaxCount;i++) 
			life[i].setForeground(Color.RED);
		lifeCount = lifeMaxCount;
		score = 0;
		scoreLabel.setText(" Score: "+ score);
	}

	/**
	 * score를 n만큼 증가 시키는 함수
	 * 
	 * @param n 은 증가되는 score값인 정수형 인수
	 */
	public void increaseScore(int n) {
		score += n;
		scoreLabel.setText(" Score: "+ score);
		if(checkScore())
			setGameSentence(1);
	}
	/**
	 * score 확인하는 함수 (승패 여부 결정)
	 * 
	 * @return user의 점수가 승리점수를 넘었는지 확인하고 리턴하는 논리형 인자
	 */
	private boolean checkScore() {
		if(score >= finalScore)
			return true;
		return false;
	}
	/**
	 * life를 감소시키고 life가 모두 없어졌을 시 패배로 게임 종료 시키는 함수
	 */
	public void decreaseLife() {
		lifeCount--;
		if(lifeCount<=0) {
			life[0].setForeground(Color.GRAY);
			gameThread.interrupt(); // gameTh종료
		}
		else
			life[lifeCount].setForeground(Color.GRAY);
	}
	/**
	 * gameThread 시작과 동시에 게임을 시작하는 함수
	 */
	public void startGameThread() {
		endMusic[0].stop();
		endMusic[1].stop();
		if(music.checkMusicState())
			music.play();
		gameThread = new GameThread(gamePanel);
		gameThread.start();
	}
	/**
	 * gameThread 종료와 동시에 게임이 종료하는 함수
	 */
	public void endGameThread() {
		music.stop();
		endMusic[1].play();
		gameThread.interrupt(); // 게임 종료	
	}
	private class OpenActionListener implements ActionListener {
		private JFileChooser chooser;
		public OpenActionListener() {
			chooser = new JFileChooser();
		}
		public void actionPerformed(ActionEvent e) {
			chooser.setCurrentDirectory(new File("C:\\Users\\user\\OneDrive\\바탕 화면\\동계 학습 프로젝트\\xmlProject"));
			FileNameExtensionFilter filter = 
					new FileNameExtensionFilter("xml", "xml");
			chooser.setFileFilter(filter);
			
			int ret = chooser.showOpenDialog(null);
			if(ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.",
						"경고",JOptionPane.WARNING_MESSAGE);
				return;
			}
			String filePath = chooser.getSelectedFile().getPath();
			if(music!=null)
				music.stop();
			new BlockGameFrame(filePath);
			dispose();
		}			
	}
	/**
	 * @param args main함수
	 */
	public static void main(String[] args) {
		new BlockGameFrame("src/xml/block.xml");
	}
}
/**
 * 게임의 user 캐릭터 이미지 (extends JLabel)
 * 
 * @author 김경미
 */
class User extends JLabel {
	Image img[] = new Image[2];
	int imgSelect = 0;
	/**
	 * User 생성자
	 * 
	 * @param x user 이미지 위치 x값
	 * @param y user 이미지 위치 y값
	 * @param w user 이미지 넓이
	 * @param h user 이미지 높이
	 * @param icon 기본 이미지
	 * @param attackIcon 공격 이미지
	 */
	public User(int x, int y, int w, int h, ImageIcon icon, ImageIcon attackIcon) {
		this.setBounds(x,y,w,h);
		img[0] = icon.getImage();
		img[1] = attackIcon.getImage();
	}
	/**
	 * 이미지 변환해주는 함수
	 * 
	 * @param i i가 0일 때는 공격하지 않을 때 이미지로 변환 i가 1일 때는 공격할 때 이미지로 변환
	 */
	public void setImg(int i) {
		imgSelect = i;
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img[imgSelect], 0, 0, this.getWidth(), this.getHeight(), this);
	}		
}
/**
 * 게임의 attack(공격 볼) 이미지 (extends JLabel)
 * 
 * @author 김경미
 */
class Attack extends JLabel {
	Image img;
	/**
	 * Attack 생성자
	 * 
	 * @param x attack 이미지 위치 x값
	 * @param y attack 이미지 위치 y값
	 * @param icon 기본 이미지
	 */
	public Attack(int x, int y, ImageIcon icon) {
		this.setBounds(x,y,20,20);
		img = icon.getImage();
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}		
}