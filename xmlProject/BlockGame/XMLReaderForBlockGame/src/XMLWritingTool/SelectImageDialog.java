package XMLWritingTool;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
/**
 * image 선택을 도와주는 Dialog
 * 
 * @author 김경미
 */
public class SelectImageDialog extends JDialog {
	private ImageIcon icon[] = null;
	private XmlString xmlObj = null;
	private WritingBlock block = null;
	/**
	 * SelectImageDialog 생성자
	 * @param block 이미지 설정하고자하는 block
	 * @param container dialog가 위치할 container
	 */
	public SelectImageDialog(WritingBlock block,Container container) {
		super(new JFrame(),"select image",true);
		this.block = block;
		setDialog();
		setLocationRelativeTo(container);
		setVisible(true);
	}
	/**
	 * SelectImageDialog 생성자
	 * @param xmlObj 이미지 설정하고자하는 xmlObj
	 * @param container dialog가 위치할 container
	 */
	public SelectImageDialog(XmlString xmlObj,Container container) {
		super(new JFrame(),"select image",true);
		this.xmlObj = xmlObj;
		setDialog();
		setLocationRelativeTo(container);
		setVisible(true);
	}
	/**
	 * SelectImageDialog 생성자
	 * @param xmlObj 이미지 설정하고자하는 xmlObj
	 * @param x dialog가 위치할 x좌표
	 * @param y dialog가 위치할 y좌표
	 */
	public SelectImageDialog(XmlString xmlObj, int x, int y) {
		setLocation(x,y);
		this.xmlObj = xmlObj;
		setDialog();
		setVisible(true);
	}
	/**
	 * dialog 기본 제작 component 설정 함수
	 */
	private void setDialog() {
		setSize(300,300);
		File dir = null;
		FileFilter filter = null;
		setLayout(new GridLayout(5,5));
		dir = new File("C:\\Users\\user\\OneDrive\\바탕 화면\\동계 학습 프로젝트\\xmlProject\\image\\");
		filter = new FileFilter() { 
			public boolean accept(File f) { 
				if(f.getName().endsWith("png") || f.getName().endsWith("jpg"))
					return true;
				return false;
			} 
		};
		File files[] = dir.listFiles(filter); 
	
		for(int i=0;i<files.length;i++) {
			ImageIcon icon = null;
			ImgFileLabel label = null;
			icon = new ImageIcon(files[i].toString());
			label = new ImgFileLabel(icon);
			label.addMouseListener(new SelectMouseListener(files[i].toString()));
			add(label);
		}
	}
	/**
	 * 사진레이블을 클릭했을 시 사진이 선택되고 창이 닫혀지는 MouseAdapter
	 * 
	 * @author 김경미
	 */
	class SelectMouseListener extends MouseAdapter {
		private BevelBorder border = new BevelBorder(BevelBorder.RAISED);
		private String fileName;
		/**
		 * SelectMouseListener 생성자
		 * @param fileName 이미지파일 이름
		 */
		public SelectMouseListener(String fileName) {
			this.fileName = fileName;
		}
		@Override
		public void mouseClicked(MouseEvent e) { 
			if(xmlObj!=null)
				xmlObj.setObj(fileName);
			else
				block.setImg(fileName);
			setVisible(false);
		}
		@Override
		public void mouseEntered(MouseEvent e) {//마우스가 해당 컴포넌트 영역 안에 있을 때
			((ImgFileLabel)e.getSource()).setBorder(border);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			((ImgFileLabel)e.getSource()).setBorder(null);
		}
	}
}
/**
 * 이미지레이블 (extends JLabel)
 * 
 * @author 김경미
 */
class ImgFileLabel extends JLabel {
	Image img = null;
	/**
	 * ImgFileLabel 생성자
	 * @param icon 이미지 icon
	 */
	public ImgFileLabel(ImageIcon icon) {
		img = icon.getImage();
	}
	/**
	 * ImgFileLabel 생성자
	 * @param icon 이미지 icon
	 * @param x 이미지 x 좌표
	 * @param y 이미지 y 좌표
	 */
	public ImgFileLabel(ImageIcon icon,int x,int y) {
		//super();
		img = icon.getImage();
		this.setBounds(x,y,30,30);
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}		
}