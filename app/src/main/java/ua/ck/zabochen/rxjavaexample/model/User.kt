package ua.ck.zabochen.rxjavaexample.model

data class User(
        var id: Int,
        var name: String,
        var email: String = "user@mail.com"
)