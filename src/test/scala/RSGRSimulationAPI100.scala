package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RSGRSimulationAPI100 extends Simulation {

  object Browse {
    val headers_10 = Map("Content-Type" -> "application/json") // Note the headers specific to a given request
    // repeat is a loop resolved at RUNTIME
    val browse = repeat(5, "i") { // Note how we force the counter name so we can reuse it
	    exec(http("api_rsgr_user ${i}")
	      .get("/api/rsgr/user"))
        .pause(1)
    }
  }

  val httpConf = http
    .baseURL("http://runclub.akqatest.cn") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


  val users = scenario("RSGR API")
    .exec(
      addCookie(
        Cookie("openid", "rsgrtest05001").withDomain("runclub.akqatest.cn").withPath("/")
      ))
    .exec(Browse.browse)

  setUp(users.inject(atOnceUsers(100)).protocols(httpConf))
}
