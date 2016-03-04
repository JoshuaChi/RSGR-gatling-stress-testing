package lunarepic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadMap extends Simulation {

  object Browse {
    // repeat is a loop resolved at RUNTIME
    val browse = repeat(5, "i") { // Note how we force the counter name so we can reuse it
	    exec(
			http("api_lunar_epic_load_map ${i}").
			get("/api/run-forever/stream/map/load")).pause(1)
    }
  }

  val httpConf = http
    .baseURL("http://runclub.akqatest.cn") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


  val users = scenario("Lunar Epic API")
    .exec(
      addCookie(
        Cookie("openid", "oaSyGtxnkDqY6GEDfyFVrCNCfs-A").withDomain("runclub.akqatest.cn").withPath("/")
      ))
    .exec(Browse.browse)

  setUp(users.inject(atOnceUsers(100)).protocols(httpConf))
}
