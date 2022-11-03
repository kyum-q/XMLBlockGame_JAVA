package XmlBlockGame;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * 음악을 재생하는 class
 * 
 * @author 김경미
 */
public class Music {
	private AudioInputStream ais;
	private Clip clip;
	private FloatControl gainControl;
	private float volume;
	private boolean musicState = true;
	/**
	 * Music 생성자
	 * 
	 * @param fileName 음악이 있는 파일 위치
	 * @param i 음악이 반복재생할 것인지 알아내는 (i가 1이면 무한 루프)
	 */
	public Music(String fileName, int i) {
		try {
			ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
			if(i==1)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume = gainControl.getValue(); // Reduce volume by 10 decibels.
		} catch (Exception e) { return; }
	}
	/**
	 * Music 생성자
	 */
	public Music() {  }
	/**
	 * music의 상태 리턴하는 함수 (자동재생 안시키기고할 경우 false)
	 * 
	 * @return musicState  music 상태 (false면 중지, true면 재생)
	 */
	public boolean checkMusicState() { return musicState; }
	/**
	 * musicState 값 변경하는 함수
	 * 
	 * @param i musicState 변경하고자 하는 값
	 */
	public void setMusicState(boolean i) { musicState = i; }
	/**
	 * 음악을 재생시키는 함수
	 */
	public void play() { 
		if(clip!=null)
			clip.start();
		musicState = true;
	}
	/**
	 * 음악을 변경하고 재생시키는 함수<br>
	 * ( musicState가 false일 경우 재생 x)
	 * @param fileName 변경할 음악이 있는 파일 위치
	 */
	public void play(String fileName) { 
		try {
			if(clip!=null)
				clip.stop();
			ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
			gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume = gainControl.getValue(); // Reduce volume by 10 decibels.
		} catch (Exception e) {
			return;
		}
		if(musicState == false)
			return;
		musicState = true;
		clip.start();
	}
	/**
	 * 음악을 중지시키는 함수
	 */
	public void stop() { 
		if(clip!=null)
		clip.stop(); 
	}
	/**
	 * 볼륨을 낮추는 함수
	 */
	public void volumeDown() { 
		FloatControl gainControl = 
			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		
		if(volume-5>=-80) volume -= 5;
		else volume = -80;
		gainControl.setValue(volume); // Reduce volume by 10 decibels.
		clip.start();
	}
	/**
	 * 볼륨을 높이는 함수
	 */
	public void volumeUp() { 
		FloatControl gainControl = 
			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		
		if(volume+5<=80) volume += 5;
		else volume = 80;
		gainControl.setValue(volume); // Reduce volume by 10 decibels.
		clip.start();
	}
}
