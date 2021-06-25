import com.google.gson.Gson
import spark.Spark.*
import spark.Request
import java.sql.DriverManager

var savedLevel = String()

data class User(val nickname: String, val score: Int)

fun main(args: Array<String>){
    port(5000)
    staticFiles.location("/public")
    get("/"){_, _ ->}
    get("/editor"){ _, _ -> "editor.html"}
    post("/save-level"){req, _-> saveLevel(req)}
    get("/load-level"){_, _ -> loadLevel()}
    post("/save-score"){req, _-> saveScore(req)}
}

fun saveLevel(request: Request){
    val body = request.body()
    savedLevel = body
}

fun loadLevel(): String {
    return if (savedLevel == ""){
        Gson().toJson("")
    }
    else savedLevel
}

fun saveScore(request: Request): Int {
    val body = request.body()
    val data = Gson().fromJson(body, User::class.java)
    val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fps", "postgres", "admin")
    val stmt = conn.createStatement()
    val query = "INSERT INTO fps.public.fps_scores (nickname, score) VALUES ('${data.nickname}', ${data.score})"
    stmt.execute(query)
    conn.close()
    return 0
}