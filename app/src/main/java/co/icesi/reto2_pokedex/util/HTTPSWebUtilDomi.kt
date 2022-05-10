package co.icesi.reto2_pokedex.util

import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HTTPSWebUtilDomi {

    fun GETRequest(url: String): String {
        val url = URL(url)
        val user = url.openConnection() as HttpsURLConnection
        user.requestMethod = "GET"
        return user.inputStream.bufferedReader().readText()
    }

    fun POSTRequest(url: String, json: String): String {
        val url = URL(url)
        val user = url.openConnection() as HttpsURLConnection
        user.requestMethod = "POST"
        user.setRequestProperty("Content-Type", "application/json")
        user.doOutput = true
        user.outputStream.bufferedWriter().use {
            it.write(json)
            it.flush()
        }
        return user.inputStream.bufferedReader().readText()
    }

    fun POSTtoFCM(json: String): String {
        val url = URL("https://fcm.googleapis.com/fcm/send")
        val user = url.openConnection() as HttpsURLConnection
        user.requestMethod = "POST"
        user.setRequestProperty("Content-Type", "application/json")
        user.setRequestProperty("Authorization", "key=$FCM_KEY")
        user.doOutput = true
        user.outputStream.bufferedWriter().use {
            it.write(json)
            it.flush()
        }
        return user.inputStream.bufferedReader().readText()
    }

    fun PUTRequest(url: String, json: String): String {
        val url = URL(url)
        val user = url.openConnection() as HttpsURLConnection
        user.requestMethod = "PUT"
        user.setRequestProperty("Content-Type", "application/json")
        user.doOutput = true
        user.outputStream.bufferedWriter().use {
            it.write(json)
            it.flush()
        }
        return user.inputStream.bufferedReader().readText()
    }

    fun DELETERequest(url: String): String {
        val url = URL(url)
        val user = url.openConnection() as HttpsURLConnection
        user.requestMethod = "DELETE"
        return user.inputStream.bufferedReader().readText()
    }


    companion object {
        const val FCM_KEY:String = "AAAA5Y28dsA:APA91bHvrWHBdEZIbpl2hmPufyiNgJp1ZNiCenoAb58dM2ydWkGTx5cYtQzlVewJUL2PL0s1Rkir0mTLKtEA0vrrHyeG1bID3HuCd0UVtO91bcuciPPWxZcSsosmRGmGV9N4tEeH90Gr"
    }
}