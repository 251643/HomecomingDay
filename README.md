![image](https://user-images.githubusercontent.com/102898794/190405115-3557e184-e6c8-4e2a-b335-3b3049553c9e.png)

#  <img src="https://user-images.githubusercontent.com/102898794/190404072-43a580b8-e606-404d-b1d2-96b31a08cca7.png" width="5%"> Homecoming Day
### 🔗사이트 ->  [HomecomingDay](https://www.homecomingdaycare.com)

### 👩‍🎓 대학교 졸업생 동문 사이트 [홈커밍데이 HomeComingDay] 에서 전국의 선후배를 만나보세요! 👨‍🎓

<br/>

🏫 홈커밍데이 HomeComingDay는 미국에서 고교 졸업자들이 30년 뒤에 모교를 방문하는 행사를 말합니다. 한국에서는 동창회란 표현이 있기 때문에 홈커밍은 동창회나 동문회, 동기회에서 추진하는 연간 모임이 아닌 학교 등 교육기관에서 공식적으로 추진하는 행사를 가리키는 표현이 되었습니다.[출처 : 나무위키]
<br/>
<br/>
청춘이 시작되는 20대를 함께 보낸 동기들, 의지하고 싶은 선후배를 찾고 싶지만 아쉽게도 동문을 만날 수 있는 방법은 많이 없습니다. 오픈채팅, 카페 등을 이용하여 동창들과 이어지는 것을 넘어 졸업 후에도 선후배를 만날 수 있는 커뮤니티를 만들고자 하는 아이디어에서 시작된 프로젝트입니다.

<br/>



<br/>

### 주요기능

#### 📲 네이버 로그인 / 이메일 로그인(이메일 인증)<br/>: 네이버를 통한 소셜로그인으로 간단하게 가입할 수 있어요. 이메일 회원가입에서는 본인 명의 이메일을 통해 인증메일을 받으실 수 있어요.
####  🏫 학교마다 독립적인 공간 사용 가능<br/> : 로그인시 선택하는 학교이름으로 독립적인 공간 사용이 가능해요.
#### 💬채팅<br/> : 친구들과 1:1 채팅을 할 수 있어요
#### 🔔 댓글, 좋아요 알림<br/> : 게시글에 달린 댓글과 좋아요를 알림으로 확인할 수 있어요
#### ✔️ 게시글 작성시 원하는 카테고리 선택<br/> : 게시글 작성시 페이지 하나에서 원하는 카테고리를 골라서 작성이 가능해요
#### 📄 대댓글<br/> : 댓글 안에 대댓글을 달아서 의견을 나눠보아요
#### 👍 좋아요<br/> : 공감이 가는 게시글에 좋아요를 누를 수 있어요 
#### 📝 마이페이지<br/> : 마이페이지에서 마음에 드는 프로필 사진 수정과 본인의 프로필 및 게시글을 확인해 보아요
#### 🏆 게시글 숫자에 따른 뱃지 생성<br/> : 게시글 숫자에 따른 뱃지가 생성이 됩니당
#### 👩‍👩‍👧‍👦 참여하기<br/> : 만남일정 게시판에서 참여하기를 눌러 일정에 참여할 수 있어요. 서두르세요 참여인원이 마감될 수 있어요🙊
#### 🗺 일정정하기<br/> : 만남일정 게시판에서 원하는 일정, 장소, 참여인원을 정할 수 있어요. 동창들과 만남일정을 정해보세요☺️
#### 🔎 검색하기<br/> : 원하는 게시글을 검색으로 찾을 수 있어요


</br>

### Architecture
![image](https://user-images.githubusercontent.com/102898794/193447392-f4778d29-b2d2-4f9f-9ad1-61fa404e824c.png)



### 🔥 트러블슈팅

<br/>

### 기술적 의사결정
| 사용 기술 | 기술 설명 |
| --- | --- |
| CloudFront | 사용자에게 제공되는 정적 컨텐츠의 전송 속도를 높이고 HTTPS를 적용시키기 위해 사용되었다. |
| mySql | 각 게시글에 유저간 사이에 참가자로서, 대기자로서, 방장으로서 각각 다양하게 연계가 되어있기 때문에 효율적인 서버관리를 위하여 관계형 DB를 선택하였다.  |
| socket.io | https를 이용한 실시간 데이터 통신으로서, 실시간으로 유저들이 채팅을 할 수 있다. 또한 실시간 알림을 통해 자신이 해당 게시글에 참여상태를 바로바로 알수 있게 만들 수 있었다. |
| github actions | 프론트엔드와 백엔드의 효율적인 협업을 위해, 자동배포를 진행하였다.  |
| nginx | DDos와 같은 공격으로부터 보호하고, 좀 더 빠른 응답을 위해 사용되었다.  |
| redux-toolkit | action type이나 action creator를 따로 생성해주지 않아도 되고. immer가 내장되어 있어 mutable 객체를 사용할 수 있었다. redux-thunk가 내장되어 있어 비동기 처리 및 미들웨어 추가가 편리하게 사용되었다. |

  

<br/>

### ⚙️ 개발 환경
- **Server** : AWS EC2(Linux 2 AMI)
- **Framework** : Springboot
- **Database** : Mysql (AWS RDS), Redis (Aws ElastiCache)
- **ETC** : AWS S3, AWS IAM, AWS Parameter Store, AWS Code deploy, NginX

<br/>

### 📜 기술스택
#### 🚀  스택
 <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white"/>  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=Spring Security&logoColor=white"/> <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white"/>   <img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=JWT&logoColor=white"/> 
 <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white"/>  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white"/>
 <img src="https://img.shields.io/badge/STOMP-885630?style=flat&logo=stomp&logoColor=white"/>  <img src="https://img.shields.io/badge/Socket-010101?style=flat-square&logo=Socket.io&logoColor=white"/>


  #### 🔧 툴
  <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/>   <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat&logo=IntelliJ IDEA&logoColor=white"/>   <img src="https://img.shields.io/badge/Sourcetree-0052CC?style=flat&logo=Sourcetree&logoColor=white"/>   <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white"/>
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=Postman&logoColor=white"/>  <img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=Slack&logoColor=white"/>   <img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white"/> 
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/>   <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=Figma&logoColor=white"/>  <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=Amazon AWS&logoColor=white"/>

 
  #### 🖥 서버
  <img src="https://img.shields.io/badge/NGINX-009639?style=flat&logo=NGINX&logoColor=white"/>  <img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=flat&logo=AmazonEC2&logoColor=orange"/>  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat&logo=GitHub Actions&logoColor=white"/>  <img src="https://img.shields.io/badge/codedeploy-6DB33F?style=flat&logo=codedeploy&logoColor=white"/>




    
### ERD
<img width="915" alt="image" src="https://user-images.githubusercontent.com/102898794/193406479-9c6a2917-4453-4998-9015-ff7a412661fb.png">



###  [API](https://www.notion.so/API-958fc1f5810045a684d94cc16b43772a)

###  [HomecomingDay 와이어 프레임](https://www.figma.com/file/b5xDAy68cty1Rguu5dqPZR/D%EB%B0%98-1%EC%A1%B0-%ED%99%88%EC%BB%A4%EB%B0%8D%EB%8D%B0%EC%9D%B4-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?node-id=995%3A3062)



<br/>

### 🗓 개발 기간 : 2022/08/26~2022/10/07

### Contributors
- 백엔드 :
   <td align="center"><b><a href="https://github.com/JeongWilly">정우창</a></b></td>
   <td align="center"><b><a href="https://github.com/251643">서솔</a></b></td>
    <td align="center"><b><a href="https://github.com/ji-0o0o0o">신지영</a></b></td>
- 프론트엔드 :
   <td align="center"><b><a href="https://github.com/jennywoon">나청운</a></b></td>
  <td align="center"><b><a href="https://github.com/suzzeong">조수정</a></b></td>
  <td align="center"><b><a href="https://github.com/hyeongyong-choi">최형용</a></b></td>
- 디자이너 :
   <td align="center"><b><a href="">김미래</a></b></td>
[FE repo 바로가기][https://github.com/jennywoon/HomeComingDay]

