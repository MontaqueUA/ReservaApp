package com.example.reservabuses

/*
enum class RequestCode(val value: Int) {
    GO_TO_LOGIN_FROM_MAIN_ACTIVITY(1)
}
*/

class RequestCode {
    companion object {
        const val GO_TO_LOGIN_FROM_MAIN_ACTIVITY = 1
        const val REQUEST_TAKE_PHOTO = 2
        const val FINE_LOCATION_REQUEST = 3
    }
}