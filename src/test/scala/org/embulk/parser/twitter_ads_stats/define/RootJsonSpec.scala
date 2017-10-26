package org.embulk.parser.twitter_ads_stats.define

import org.embulk.parser.twitter_ads_stats
import org.embulk.parser.twitter_ads_stats.UnitSpec
import spray.json._

class RootJsonSpec extends UnitSpec {
  "JSONをパースできる" in {

    val largeJsonSource = scala.io.Source
      .fromInputStream(
        getClass.getResourceAsStream("/test.json")
      )
      .mkString
      .parseJson

    val root = new RootJson(twitter_ads_stats.metricElementNames).RootReader.read(largeJsonSource)
    //    println(root)
  }
}
