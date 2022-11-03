package XMLWritingTool;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import XmlBlockGame.Music;

/**
 * music 선택을 도와주는 Panel
 * @author 김경미
 */
public class SelectMusicPanel extends JPanel {
	private XmlString xmlObj = null;
	private Music music = new Music();
	private JLabel selectLabel = null;
	/**
	 * SelectMusicPanel 생성자
	 * @param xmlObj 음악 설정하고자하는 xmlObj
	 */
	public SelectMusicPanel(XmlString xmlObj) {
		this.xmlObj = xmlObj;
		File dir = null;
		FileFilter filter = null;
		
		dir = new File("C:\\Users\\user\\OneDrive\\바탕 화면\\동계 학습 프로젝트\\xmlProject\\music\\");
		filter = new FileFilter() { 
			public boolean accept(File f) { 
				return f.getName().endsWith("wav");
			}	 
		};
		File files[] = dir.listFiles(filter); 
		Box box = Box.createVerticalBox();
		for (int i = 0; i < files.length; i++) {
			String fileNameList[] = files[i].toString().split("music");
			String fileName = fileNameList[fileNameList.length-1];
			JLabel label = new JLabel(fileName.substring(1,fileName.length()));
			label.addMouseListener(new SelectMouseListener(files[i].toString()));
			if(xmlObj.getObj()!=null && xmlObj.getObj().equals(files[i].toString())) {
				label.setForeground(Color.blue);
				selectLabel = label;
			}
			box.add(label);
		}
				
		add(box);
	}
	/**
	 * 음악 레이블 클릭했을 시 음악이 선택되는 MouseAdapter
	 * 
	 * @author 김경미
	 */
	class SelectMouseListener extends MouseAdapter {
		private BevelBorder border = new BevelBorder(BevelBorder.RAISED);
		private String fileName;
		/**
		 * SelectMouseListener 생성자
		 * @param fileName 음악파일 이름
		 */
		public SelectMouseListener(String fileName) {
			this.fileName = fileName;
		}
		@Override
		public void mouseClicked(MouseEvent e) { 
			xmlObj.setObj(fileName);
			if(selectLabel != null)
				selectLabel.setForeground(Color.black);
			((JLabel)e.getSource()).setForeground(Color.blue);
			selectLabel = ((JLabel)e.getSource());
			}
		@Override
		public void mouseEntered(MouseEvent e) {//마우스가 해당 컴포넌트 영역 안에 있을 때
			((JLabel)e.getSource()).setBorder(border);
			music.play(fileName);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			((JLabel)e.getSource()).setBorder(null);
			music.stop();
		}
	}
}