package com.example.proteinpro.util.Retrofit

class ServerData {

    // 컴패니언 오브젝트로 선언해주면
    // 다른 클래스에서 인스턴스 생성 없이 해당 변수로 접근 가능
    // java static 과 유사
    companion object {

        const val Server_URL = "http://49.247.132.156/api/"
        const val public_URL = "http://49.247.132.156"

    }

}