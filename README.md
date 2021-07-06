# project1

프로젝트 이름: Great Note

팀원: 장진혁, 손민지

프로젝트 설명: 

Tab1에는 연락처, Tab2는 갤러리, Tab3는 캔버스/노트로 구성하였다.

# Tab1(연락처)
핸드폰 주소록과 연동하여 연락처를 불러온다. recyclerview로 연락처 리스트를 보여준다.
연락처를 추가 가능하게 하였고 주소록 저장과 동시에 새로고침하면 보여지게 구현하였다.
<img src="https://user-images.githubusercontent.com/86706527/124607195-93c58700-dea8-11eb-9f8a-09ecb4c47478.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124607199-945e1d80-dea8-11eb-9943-1f8df8abac47.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124607202-94f6b400-dea8-11eb-84c7-b55ddd0056f2.jpg" width="270" height="570">


# Tab2(갤러리)
Tab2에는 drawable에 이미지 20장을 넣은 후, 각 이미지의 ID를 받아 RecyclerView로 구현하였다.


<img src="https://user-images.githubusercontent.com/86706527/124604679-21ec3e00-dea6-11eb-8963-7215529f8f4c.jpg" width="270" height="570">

# Tab3(캔버스/노트)
Tab3에는 터치이벤트를 감지하여 그림그려주는 PaintView class를 만들어서 그림을 그릴 수 있게 하였다. 
seekbar와 버튼을 추가하여 선의 굵기와 색을 조절할 수 있게 하였다. 
또한 erase 버튼을 추가하여 그린 그림을 모두 삭제하는 기능을 추가하였으며 터치 이벤트를 저장 할 수있는 ArrayList를 만들어서 redo와 undo를 구현을 추가하였다.
마지막으로 그림을 그리다 화면이 계속 넘어가는 불편함을 고치기 위해 lock을 만들어 켜놓으면 화면이 넘어가지 못하도록 만들었다.
<img src="https://user-images.githubusercontent.com/86706527/124604671-20bb1100-dea6-11eb-95a0-cf11454a38f8.jpg" width="270" height="570">
<img src="https://user-images.githubusercontent.com/86706527/124604677-21ec3e00-dea6-11eb-92a0-27658ff06d75.jpg" width="270" height="570">

<img src="https://user-images.githubusercontent.com/86706527/124606509-ee121800-dea7-11eb-982b-d6d57bef2ccf.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124606512-eeaaae80-dea7-11eb-982f-b11e294f35e2.jpg" width="270" height="570">
