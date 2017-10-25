package org.embulk.parser.twitter_ads_stats.define

import org.embulk.parser.twitter_ads_stats.{MetricElementNames, UnitSpec}
import spray.json._

class MetricsJsonSpec extends UnitSpec {

  "メトリクスのネストしたJSON構造を .つなぎの要素名を指定してRead できる" in {

    val metricElementNames = MetricElementNames(
      Map(
        "x" -> Seq("a", "b"),
        "y" -> Seq("c.e", "c.f", "d.e", "d.f")
      )
    )

    val jsValue =
      """
        |{
        |    "a": [
        |      510,
        |      494,
        |      364
        |    ],
        |    "b": [1,2,3],
        |    "c": {
        |      "e": [1,2,3],
        |      "f": null
        |    },
        |    "d": {
        |      "e": [
        |        2,
        |        3,
        |        4
        |      ],
        |      "f": [
        |        12,
        |        19,
        |        13
        |      ]
        |    }
        |}
      """.stripMargin.parseJson
    val actual = new RootJson(metricElementNames).MetricsReader.read(jsValue)
    val expected = Metrics(
      Map(
        "a" -> Some(Vector(510, 494, 364)),
        "b" -> Some(Vector(1, 2, 3)),
        "c_e" -> Some(Vector(1, 2, 3)),
        "c_f" -> None,
        "d_e" -> Some(Vector(2, 3, 4)),
        "d_f" -> Some(Vector(12, 19, 13))
      )
    )
    assert(actual == expected)
  }
}
