<img src="https://user-images.githubusercontent.com/109018926/193449385-70608a7e-5b44-49e1-9c08-f168783f5a22.png" >

#  <img src="https://user-images.githubusercontent.com/102898794/190404072-43a580b8-e606-404d-b1d2-96b31a08cca7.png" width="5%"> Homecoming Day
### 🔗사이트 ->  [HomecomingDay](https://www.homecomingdaycare.com)

### 👩‍🎓 대학교 졸업생 동문 사이트 [홈커밍데이 HomeComingDay] 에서 전국의 선후배를 만나보세요! 👨‍🎓

<br/>

🏫 홈커밍데이 HomeComingDay는 미국에서 고교 졸업자들이 30년 뒤에 모교를 방문하는 행사를 말합니다. 한국에서는 동창회란 표현이 있기 때문에 홈커밍은 동창회나 동문회, 동기회에서 추진하는 연간 모임이 아닌 학교 등 교육기관에서 공식적으로 추진하는 행사를 가리키는 표현이 되었습니다.[출처 : 나무위키]
<br/>
<br/>
청춘이 시작되는 20대를 함께 보낸 동기들, 의지하고 싶은 선후배를 찾고 싶지만 아쉽게도 동문을 만날 수 있는 방법은 많이 없습니다. 오픈채팅, 카페 등을 이용하여 동창들과 이어지는 것을 넘어 졸업 후에도 선후배를 만날 수 있는 커뮤니티를 만들고자 하는 아이디어에서 시작된 프로젝트입니다.

<br/>

### 🗓 프로젝트 기간 & 마케팅



- 2022.08.26 ~ 10.07 (6주)
- 10월 1일 배포, 마케팅 시작
- 10월 3일 기준 순 방문자 227명, 누적 가입자 수 83명
- 10월 6일 기준<br/>
> - 순 방문자 463명, 누적 가입자 수 170명<br/>
> - 유저 테스트 1,794개
>> - 작성 게시글 153개, 댓글 600개, 대댓글 58개, 채팅 메시지 534개, 좋아요 125개, 알림 324개

<br/>

### ⚙️ 개발 환경
- **Server** : AWS EC2(Linux 2 AMI)
- **Framework** : Springboot
- **Database** : Mysql (AWS RDS), Redis (Aws ElastiCache)
- **ETC** : AWS S3, AWS IAM, AWS Parameter Store, AWS Code deploy, NginX

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
#### 🗺 일정정하기<br/> : 만남일정 게시판에서 원하는 일정, 장소, 참여인원을 정할 수 있어요. 동창들과 만남일정을 정해보세요☺️
#### 👩‍👩‍👧‍👦 참여하기<br/> : 만남일정 게시판에서 참여하기를 눌러 일정에 참여할 수 있어요. 서두르세요 참여인원이 마감될 수 있어요🙊
#### 🔎 검색하기<br/> : 원하는 게시글을 검색으로 찾을 수 있어요


</br>

### Architecture
![image](https://user-images.githubusercontent.com/102898794/193447392-f4778d29-b2d2-4f9f-9ad1-61fa404e824c.png)

<br/>

### 🔥 트러블슈팅

<br/>

<details>
<summary><b>✅ 게시글 조회 속도(723% 개선)</b></summary>
<br/>
해당 트러블 슈팅은 백엔드 속도 개선이다. useEffect도 잘 되어 있는 것 같았고 아무리 코드를 수정해도 렌더링 속도가 너무 느리고 이미지는 더 느리게 나와서, BackEnd와 계속해서 속도를 확인해보다, Redis를 통해 속도를 개선할 수 있다는 것을 알게 됨
</br>
메인 페이지인 article/help 위치에선 무려 723%의 속도 개선율을 보였다. FrontEnd에서도 해당 과정을 알고 있었다면 더 빠르게 소통해볼 수 있었을 것 같아 좋은 러닝이 되어 기록.</br>
<img src="https://user-images.githubusercontent.com/109018926/194244336-268d2921-5a5c-4a5d-9859-06e15b8123e5.png">
</br>
</details>


<br/>

### 기술적 의사결정
| 사용 기술 | 기술 설명 |
| --- | --- |
| Redux-thunk | React에서 비동기 작업을 처리하기 위해 사용. 하나의 액션에서 dispatch를 여러 번 할 수 있고, 하나의 비동기 액션 안에 여러 가지 동기 액션을 넣을 수 있어 선택 |
| React-redux | Redux는 React 생태계에서 가장 사용률이 높은 상태 관리 라이브러리임. 페이지가 많아지면서 서버와의 연결을 명확하게 볼 수 있어야 했고 전역으로 상태 관리를 할 수 있어 선택. 프로젝트 규모가 큰 편이며, 비동기 작업을 자주 하게 되었으며, Redux를 사용하는 게 편해 Redux를 사용함 |
| Axios | promise 기반으로 데이터를 다루기 편했고 HTTP 비동기 통신을 하기 위해 선택 |
| Swiper | 최소한의 props로 내비게이션 구현이 가능하여 간편하고 CSS Styling 가능하여 높은 확장성을 가지고 있음 |
| GitHub Actions | FrontEnd와 BackEnd의 효율적인 작업 환경을 구축하기 위해 배포 자동화 |
| Nginx | HTTPS 연결을 위해 reverse proxy를 수행해줄 web server가 필요했고, 추후 무중단 배포를 염두에 두면 Nginx를 사용하는 것이 좋다고 생각했습니다. |
| AWS CodeDeploy | GitHub Actions로 자동 빌드를 시키고 자동 배포를 해주는 도구를 찾아야 했다. 무중단 배포는 Nginx를 사용할 예정이었고 Docker를 사용하지 않고 자동 배포 예정이었기에 AWS Elastic Beanstalk 대신에 codeDeploy를 선택하게 되었다.  |
| WebSocket | 서버의 부담을 줄이고 실시간으로 채팅 기능을 구현하기 위해 WebSocket을 사용 |
| Certbot | SSL 인증서를 발급 받기 위해서 무료로 인증서를 발급해주는 기관인 'Let's Encrypt'가 공식적으로 지원하는 Certbot을 사용하였다.  |

  


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


<br/>

    
### ERD
<img width="915" alt="image" src="https://user-images.githubusercontent.com/102898794/193406479-9c6a2917-4453-4998-9015-ff7a412661fb.png">



###  [API](https://www.notion.so/API-958fc1f5810045a684d94cc16b43772a)

###  [HomecomingDay 와이어 프레임](https://www.figma.com/file/b5xDAy68cty1Rguu5dqPZR/D%EB%B0%98-1%EC%A1%B0-%ED%99%88%EC%BB%A4%EB%B0%8D%EB%8D%B0%EC%9D%B4-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?node-id=995%3A3062)




<br/>

### Contributors
   
|  | 이름 | 담당 | github/blog |
| --- | --- | --- | --- |
| <img src="https://img.shields.io/badge/BackEnd-009639?style=flat&logo=BackEnd&logoColor=white"/> | 정우창 (리더) | 게시글 작성&수정&삭제, 게시글 검색, 댓글 작성&수정&삭제, 이미지 업로드&리사이징 |  https://github.com/JeongWilly<br>https://veganwithbacon.tistory.com/ |
| <img src="https://img.shields.io/badge/BackEnd-009639?style=flat&logo=BackEnd&logoColor=white"/> | 서솔 | 회원가입, 로그인, 소셜로그인, 학교정보 입력, 채팅, 게시글/마이페이지 조회 |  https://github.com/251643<br>https://yy-yyy-yy.tistory.com/ |
| <img src="https://img.shields.io/badge/BackEnd-009639?style=flat&logo=BackEnd&logoColor=white"/>| 신지영 | CI/CD, 배포관리, 마이페이지, 좋아요, 대댓글, 알림기능 | https://github.com/ji-0o0o0o<br>https://ji-0o0o0o.tistory.com/ |
| <img src="https://img.shields.io/badge/FrontEnd-2088FF?style=flat&logo=FrontEnd&logoColor=white"/>| 나청운(부리더) | 마이 페이지 프로필 수정, 무한 스크롤, 메인 페이지 게시글 get, post, 채팅 기능, 로그인 페이지 네이버 소셜로그인, 게시글 상세페이지 calendar, time 기능 CRUD | https://github.com/jennywoon<br>https://jenny0520.tistory.com/ |
| <img src="https://img.shields.io/badge/FrontEnd-2088FF?style=flat&logo=FrontEnd&logoColor=white"/> | 조수정 | 로그인 페이지, 회원가입 페이지, 학교정보입력 페이지, 회원가입 완성 페이지, 좋아요 기능,  알림 기능 | https://github.com/suzzeong<br>https://suzzeong.tistory.com/ |
| <img src="https://img.shields.io/badge/FrontEnd-2088FF?style=flat&logo=FrontEnd&logoColor=white"/>| 최형용 | 게시글, 댓글, 대댓글 기능 CRUD, 검색기능, 참여하기, 게시글 폼 페이지, 이미지 업로드 | https://github.com/hyeongyong-choi<br>https://blog.naver.com/guddyd6761 |
|Desgner | 김미래 | 전체 디자인 담당✨ |  |
   
[FE repo 바로가기][https://github.com/jennywoon/HomeComingDay]

<br/>
<img src="https://user-images.githubusercontent.com/109018926/194331117-a466f5ef-7c3e-4c5d-b813-d8b8c334ede6.png">
