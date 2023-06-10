# XMLBlockShootingGame
XML을 통해 만들어지는 BlockShootingGame 게임

## Game 실행 화면

<h4>게임 실행 전 초기화면 | 게임 실행 전 초기화면 | Shooting 화면</h4>

![image](https://user-images.githubusercontent.com/109158497/199795984-a528f998-980d-48a9-a130-1e92ee5ba1be.png)

## Package XmlBlockGame

### Interface Summary
GoneBlockInterface: 맞으면 사라지는 블록일 경우에 필요한 함수를 가진 interface

### Class Summary

**AttackThread**: attack의 움직임을 나타내는 Thread
**BlockGameFrame**: Block 게임의 Frame을 설정하는 class
**DontGoneBlock**: 게임의 움직이지 않고 사라지지 않은 블록 이미지 레이블(extends JLabel)
**GameInitPanel**: Block 게임의 타이틀 혹은 게임 종료 후 Panel
**GamePanel**: Block 게임 실행 Panel
**GameThread**: game을 움직이는 Thread
**GoneBlock**: 게임의 움직이지 않지만 사라지는 블록 이미지 레이블 (extends DontGoneBlock implements GoneBlockInterface)
**Music**: 음악을 재생하는 class
**SideMoveAndGoneBlock**: 게임의 좌우로 움직이고 사라지는 블록 이미지 레이블(extends SideMoveBlock implements GoneBlockInterface)
**SideMoveBlock**: 게임의 좌우로 움직이고 사라지지 않는 블록 이미지 레이블(extends DontGoneBlock)

## Block의 계층 구조

### BlockGame Block 계층구조 그림 표현

![image](https://user-images.githubusercontent.com/109158497/199739565-731febf2-694a-46df-8235-5418b3059b08.png)

### Block 안에 GoneBlockInterface 계층 구조

![image](https://user-images.githubusercontent.com/109158497/199739702-47c8878b-4b42-4fc7-98ef-ebaed33a62fc.png)

