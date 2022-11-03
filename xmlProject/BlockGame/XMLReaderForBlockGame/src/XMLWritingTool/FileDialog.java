package XMLWritingTool;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
/**
 * file ������ �����ִ� Dialog
 * 
 * @author ����
 */
public class FileDialog extends JDialog {
	private String xmlFileName;
	private String filePath = "C:\\";
	private JButton file = new JButton("File location");
	private JTextField fileNameInput = new JTextField();
	JLabel fileLabel = new JLabel("���� ��� : "+filePath);
	JLabel warningLabel = new JLabel("");
	private JButton select = new JButton("����");
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
		
		JLabel fileNameLabel = new JLabel("XML ���� �̸� ");
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
					warningLabel.setText("file�̸��� .xml�� �ٿ��ּ���");
					return;
				}
				
				xmlFileName = filePath+xmlFileName;
				
				setVisible(false);
			}	
		});
		add(select);
		setLocationRelativeTo(null);
		setResizable(false); // âũ�� ���� (���� �Ұ�)
	}
	/**
	 * ���� �̸��� �������ִ� �Լ�
	 * @return fileName
	 */
	public String getFileName() {
		return xmlFileName;
	}
	/**
	 * ���� ������ ���� JButton�� ActionListener<br>
	 *  : Ŭ���� ��� JFileChooser ����
	 * 
	 * @author ����
	 */
	private class OpenActionListener implements ActionListener {
		private JFileChooser chooser;
		/**
		 * OpenActionListener ������
		 */
		public OpenActionListener() {
			chooser = new JFileChooser();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			chooser.setCurrentDirectory(new File("C:\\Users\\user\\OneDrive\\���� ȭ��\\���� �н� ������Ʈ\\xmlProject"));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int ret = chooser.showOpenDialog(null);
			if(ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�.",
						"���",JOptionPane.WARNING_MESSAGE);
				return;
			}
			filePath = chooser.getSelectedFile().getPath()+"\\";
			fileLabel.setText("���� ��� : " + filePath);
		}			
	}
}