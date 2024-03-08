# 수정된 small_steps입니다.
</br>

*  자동으로 걸음수를 측정하는 앱</br>
*  자정이 되면 값이 리셋됨</br>
*  목표 걸음 수를 설정할 수 있음</br>

* 프로젝트 상세 설명 </br>
https://velog.io/@likethe/projectsmallsteps-App
</br>
</br>

<image src="https://github.com/hyewoon/Small_Steps/assets/113662682/78dabacb-63d0-424c-9dca-fdc8e7ca72ca" heigth="500" width="500"></br>
<image src="https://github.com/hyewoon/Small_Steps/assets/113662682/53448313-db2a-40ed-ab78-839bbdec1b8d" heigth="500" width="500"></br>
<image src="https://github.com/hyewoon/Small_Steps/assets/113662682/fb2a1fa9-2289-4690-9bef-5e464cd8c841" heigth="500" width="500"></br>

</br>

### ✔️ 자동걸음 수 측정 : Sensor TYPE_STEP_COUNTER

- 매일 오늘의 걸음 수 측정하기 위해 TYPE_STEP_COUNTER 수정 하여 사용
- progressBar 통해 걸음수를 시각적으로 표현함

### ✔️ 자정되면 리셋되는 걸음 수 : WorkManager

- 매일 자정 오늘 측정된 걸음 수와 날짜를 db에 저장하고 값을 리셋하도록 구현

### ✔️  측정된 걸음 수 저장 : sharedPreference & Room

- 측정된 걸음수를 sharedpreference 전역변수로 선언하여 다른 fragment에서도 접근가능하도록 함
- 로컬DB인 ROOM 라이브러리를 이용해 값을 저장하고, 저장된 값을 가져옴





