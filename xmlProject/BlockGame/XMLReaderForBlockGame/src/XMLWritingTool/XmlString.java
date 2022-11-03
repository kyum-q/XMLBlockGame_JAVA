package XMLWritingTool;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 게임의 제작을 위한 xmlString
 * 
 * @author 김경미
 */
public class XmlString {
	private String obj=null, objName;
	private int type = -1;
	private JPanel panel = null;
	private JButton btn = null;
	/**
	 * XmlString 생성자
	 * @param objName obj의 형식 이름
	 * @param groundPanel 게임화면 패널
	 * @param type xml이 getString함수에서 리턴 될때 문자열 형식
	 */
	public XmlString(String objName, JPanel groundPanel, int type) {
		this.objName = objName;
		this.panel = groundPanel;
		this.type = type;
	}
	/**
	 * XmlString 생성자
	 * @param objName obj의 형식 이름
	 * @param groundPanel 게임화면 패널
	 */
	public XmlString(String objName, JPanel groundPanel) {
		this.objName = objName;
		this.panel = groundPanel;
	}
	/**
	 * xml의 값이 설정될 때 버튼 색 변경해주기 위해 버튼 등록하는 함수
	 * @param btn button
	 */
	public void setButton(JButton btn) {
		this.btn = btn;
	}
	/**
	 * xml 문자열로 바꿔서 리턴하는 함수
	 * @return xml에 작성되는 타입(생성자에서 등록된 타입)에 따라서 변경해서 리턴
	 */
	public String getString() {
		String obj = this.obj;
		if(obj==null)
			obj="";
		if(type == 0)
			return "<"+objName+">"+obj+"</"+objName+">\r\n";
		else if(type == 1)
			return "<"+objName+" "+obj+"/>\r\n";
		//return obj;
		return objName+"=\""+obj+"\"";
	}
	/**
	 * xmlString의 값(obj) 설정하는 함수
	 * @param obj obj 설정 값
	 */
	public void setObj(String obj) { 
		this.obj = obj; 
		if(panel instanceof GameGroundPanel)
			((GameGroundPanel) panel).setGroundPanel(this);
		if(panel instanceof GameInitToolsPanel)
			if(objName.equals("InitBg"))
				((GameInitToolsPanel) panel).setBgImg(obj);
		if(btn!=null)
			btn.setBackground(Color.LIGHT_GRAY);
	}
	/**
	 * xml에 쓰기 위해 obj 리턴하는 함수
	 * @return obj
	 */
	public String writingObj() {
		if(obj==null)
			return " ";
		return obj;
	}
	/**
	 * xmlString의 objName 리턴하는 함수
	 * @return objName
	 */
	public String getObjName() { return objName; }
	/**
	 * xmlString의 obj 리턴하는 함수
	 * @return obj
	 */
	public String getObj() { return obj; }
	/**
	 * objName이 s 값과 같은지 확인하는 함수
	 * @param s objName과 비교할 문자열
	 * @return 비교했을 때 두 문자열이 같을 시 true, 아닐 시 false
	 */
	public boolean equalsObjName(String s) { return (s.equals(objName)); }
}

