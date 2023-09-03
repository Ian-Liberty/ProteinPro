package com.example.proteinpro.util.Retrofit

class ServerData {

    // 컴패니언 오브젝트로 선언해주면
    // 다른 클래스에서 인스턴스 생성 없이 해당 변수로 접근 가능
    // java static 과 유사
    companion object {

        const val Server_URL = "http://203.234.103.123/api/"
        const val public_URL = "http://203.234.103.123"
        const val img_URL = Server_URL+"img/food/"
        const val img_URL_board = Server_URL+"img/board/"

        //이전 테섭 url 49.247.132.156
        //물리 테섭 203.234.103.123
        // 운영 서버 175.207.29.173

    }

}