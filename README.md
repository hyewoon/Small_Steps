# 수정된 small_steps입니다.
# 🏃‍앱 소개

  * 하루 걸음수를 자동으로 측정하는 앱
  * 자정이 되면 걸음수가 리셋됨
  * 목표 걸음수를 정할 수 있음
  (주간, 월간 측정값을 보여주는 화면 및 기록 공유 기능을 추가하려 합니다.)  

### 프로젝트 상세 설명 </br>
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
  
# 🔗 개발 환경
  * 개발환경 : Android Studio 
  * 언어 : kotlin
  * DB : Room 라이브러리
  * 안드로이드 Sensor
  * 안드로이드 Jectpack : viewbinding, workManager

# 🔗 기능 구현
  ### ✔️ 걸음수 측정 : Sensor.TYPE_STEP_COUNTER
  * 핸드폰이 리부트 되지 않는 이상 자동으로 걸음수를 측정해줌
  * 오늘 걸음수를 측정하기 위해 'initialStepCount' (sensor 통해 측정되는 걸음 수)를 직접 수정해줘야 함
  > currentSteps : 오늘 걸음 수
    `currentSteps = initialStepCount- previousSteps`
  	initialStepCount : sensor 통해 측정되는 걸음 수
   	previousSteps : 자정에 측정한 오늘 걸음 수 
  
 📌 매일 자정이 되면 previousSteps이 업데이트 되고, currentSteps는 '0'이 됨 
  
  ```kotlin
 @RequiresApi(Build.VERSION_CODES.O)
    private fun resetSessionSteps() {
        // Reset initialStepCount to null so it will be set again with the next sensor event
        previousSteps = initialStepCount!!
        initialStepCount = null
        currentSteps = 0
       isStepsChanged = true
    //save
        MyApplication.prefs.setString("currentSteps",currentSteps)
        MyApplication.prefs.setString("previousSteps",previousSteps )
     
        saveData()

    }```   
  
###  ✔️ 매일 자정 걸음수를 저장함 : workManager
  * workManager 이용하여 db에 값을 저장 
  

📌 workManager 
```kotlin
class MyWorkManager(context : Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        //db연결
        val db = AppDataBase.getInstance(applicationContext)
        val date = LocalDateTime.now()

     //데이터 받가
        val steps : Int = inputData.getInt("steps", -1)
        val totalSteps : Int = inputData.getInt("totalSteps",-1)
        Log.d("MyWorkManager", "receive$steps$totalSteps")
        val myData : MyData = MyData(date!!, totalSteps, steps)

        //데이터 저장
       db.myDataDao.insert(myData)

        return Result.success()
    }
}
```

# 🔗 프로젝트를 통해 배운점
 ### 📌 Activity 및 fragment 생명주기의 중요성
기존에 만들었던 앱은 사용자가 입력한 값을 intent 또는 bundle에 담아 전달하면 되었기에 onCreate()또는 onCreateView()에서 작업하는 것만으로 충분했습니다. 
  
  하지만 Sensor 를 통해 전달되는 실시간으로 변하는 값을 보여주기 위해서는 다른 생명주기에 대해 이해해야 했습니다. 
  
  > * onStop() :  fragment가 화면에서 벗어난 경우에도 걸음수가 측정되고 값을 유지해야 한다. </br> 
 * onPause() : senor 연결되지 않아 걸음수 측정 안됨
 
  
### 📌 workManager 에 대해 알게 되었습니다. 
특정 시간 반복적인 작업을 위해 alarmManager를 이용하려 했습니다. 하지만 alarmManager를 이용하는 작업에 DB 저장까지 있다면 workManager를 이용하는 것이 훨씬 간편한 방법이라는 것을 알게 되었습니다.   
  
### 📌 room 라이브러리 entitiy에 Date를 적용하려면 typeConverter를 이용해야 합니다.
 앱에서 날짜(Date)를 primary key로 활용하려고 하니 적용이 되지 않았습니다. 
 typeConverter를 선언해서 날짜를 String으로 변환해주는 과정이 필요했습니다. room의 기능을 더 학습해야 한다는 것을 알게 되었습니다. 
 
 ![](https://velog.velcdn.com/images/likethe/post/67759039-bc35-47bf-9411-7ce27124fcfc/image.png)
 
 ![](https://velog.velcdn.com/images/likethe/post/afb65c7b-3485-4d4a-91cf-7168a4c3709a/image.png)






