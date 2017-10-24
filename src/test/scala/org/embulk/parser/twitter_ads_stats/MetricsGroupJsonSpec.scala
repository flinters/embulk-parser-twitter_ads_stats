package org.embulk.parser.twitter_ads_stats

import spray.json.{JsNull, JsNumber, JsObject, pimpAny}

class MetricsGroupJsonSpec extends UnitSpec {
  "json write" in {
    import MetricsGroupJson._

    val v: MetricsGroup = Map("a" -> Some(3), "b" -> Some(6), "c_e" -> None)

    val actual = v.toJson

    val expected = JsObject(
      "a" -> JsNumber(3),
      "b" -> JsNumber(6),
      "c_e" -> JsNull
    )
    assert(actual == expected)
  }
}
