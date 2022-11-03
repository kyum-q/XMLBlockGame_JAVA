package XMLWritingTool;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
/**
 * file 선택을 도와주는 Dialog
 * 
 * @author 김경미
 */
public class FileDialog extends JDialog {
	private String xmlFileName;
	private String filePath = "C:\\";
	private JButton file = new JButton("File location");
	private JTextField fileNameInput = new JTextField();
	JLabel fileLabel = new JLabel("파일 경로 : "+filePath);
	JLabel warningLabel = new JLabel("");
	private JButton select = new JButton("결정");
	public FileDialog(JFrame frame, String title) {
		super(frame,title,true);
		setLayout(null);
		setSize(300,200);
		
		fileLabel.setSize(300,30);
		fileLabel.setLocation(0,20);
		fileLabel.setHorizontalAlignment(JLabel.CENTER);
		add(fileLabel);
		file.setSize(130,20);
		file.setLocation(85,50);
		
		file.addActionListener(new OpenActionListener());
		add(file);
		
		JLabel fileNameLabel = new JLabel("XML 파일 이름 ");
		fileNameLabel.setSize(140,30);
		fileNameLabel.setLocation(0,80);
		fileNameLabel.setHorizontalAlignment(JLabel.RIGHT);
		add(fileNameLabel);
		fileNameInput.setSize(100,20);
		fileNameInput.setLocation(140,85);
		fileNameInput.setHorizontalAlignment(JTextField.CENTER);
		add(fileNameInput);
		
		warningLabel.setSize(300,20);
		warningLabel.setLocation(0,115);
		warningLabel.setForeground(Color.RED);
		warningLabel.setHorizontalAlignment(JLabel.CENTER);
		add(warningLabel);
		
		select.setSize(100,20);
		select.setLocation(100,135);
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 

				xmlFileName = fileNameInput.getText().trim();
				if(xmlFileName.length()<=4 || !(xmlFileName.substring(xmlFileName.length()-4,xmlFileName.length()).equals(".xml"))) {
					System.out.println(xmlFileName.length());
					warningLabel.setText("file이름에 .xml을 붙여주세요");
					return;
				}
				
				xmlFileName = filePath+xmlFileName;
				
				setVisible(false);
			}	
		});
		add(select);
		setLocationRelativeTo(null);
		setResizable(false); // 창크기 고정 (수정 불가)
	}
	/**
	 * 파일 이름을 리턴해주는 함수
	 * @return fileName
	 */
	public String getFileName() {
		return xmlFileName;
	}
	/**
	 * 파일 선택을 위한 JButton의 ActionListener<br>
	 *  : 클릭할 경우 JFileChooser 실행
	 * 
	 * @author 김경미
	 */
	private class OpenActionListener implements ActionListener {
		private JFileChooser chooser;
		/**
		 * OpenActionListener 생성자
		 */
		public OpenActionListener() {
			chooser = new JFileChooser();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			chooser.setCurrentDirectory(new File("C:\\Users\\user\\OneDrive\\바탕 화면\\동계 학습 프로젝트\\xmlProject"));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int ret = chooser.showOpenDialog(null);
			if(ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.",
						"경고",JOptionPane.WARNING_MESSAGE);
				return;
			}
			filePath = chooser.getSelectedFile().getPath()+"\\";
			fileLabel.setText("파일 경로 : " + filePath);
		}			
	}
}
