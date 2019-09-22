// @GENERATOR:play-routes-compiler
// @SOURCE:/home/leonardo/Documents/Maestria/SistemaDeBaseDeDatos/trabajo_4/user-service/conf/routes
// @DATE:Sun Sep 15 17:10:25 BOT 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
