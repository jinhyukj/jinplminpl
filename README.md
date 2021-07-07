# project1

프로젝트 이름: Great Note

팀원: 장진혁, 손민지

프로젝트 설명: 

Tab1에는 연락처, Tab2는 갤러리, Tab3는 캔버스/노트로 구성하였다.

# Tab1(연락처)
핸드폰 주소록과 연동하여 연락처를 불러온다. recyclerview로 연락처 리스트를 보여준다.
연락처를 추가 가능하게 하였고 주소록 저장과 동시에 새로고침하면 보여지게 구현하였다.



<img src="https://user-images.githubusercontent.com/86706527/124692671-f0608a80-df18-11eb-9188-86a9f576fa25.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124692713-053d1e00-df19-11eb-9edf-336eb0432137.jpg" width="270" height="570">


# Tab2(갤러리)
Tab2에는 drawable에 이미지 20장을 넣은 후, 각 이미지의 ID를 받아 RecyclerView로 구현하였다.



<img src="https://user-images.githubusercontent.com/86706527/124692765-23a31980-df19-11eb-8585-459aeb4f8131.jpg" width="270" height="570">



# Tab3(캔버스/노트)
Tab3에는 터치이벤트를 감지하여 그림그려주는 PaintView class를 만들어서 그림을 그릴 수 있게 하였다. 
seekbar와 버튼을 추가하여 선의 굵기와 색을 조절할 수 있게 하였다. 
또한 erase 버튼을 추가하여 그린 그림을 모두 삭제하는 기능을 추가하였으며 터치 이벤트를 저장 할 수있는 ArrayList를 만들어서 redo와 undo를 구현을 추가하였다.
마지막으로 그림을 그리다 화면이 계속 넘어가는 불편함을 고치기 위해 lock을 만들어 켜놓으면 화면이 넘어가지 못하도록 만들었다.



<img src="https://user-images.githubusercontent.com/86706527/124692814-3c133400-df19-11eb-8351-731cf1cbaf1f.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124692815-3ddcf780-df19-11eb-810c-a66b91353d0b.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124692816-3e758e00-df19-11eb-9161-86f9c03545c4.jpg" width="270" height="570"><img src="https://user-images.githubusercontent.com/86706527/124692818-3f0e2480-df19-11eb-92a2-311c128e9f77.jpg" width="270" height="570">

