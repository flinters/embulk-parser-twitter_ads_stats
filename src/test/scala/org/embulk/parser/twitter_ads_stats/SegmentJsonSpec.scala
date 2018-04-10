package org.embulk.parser.twitter_ads_stats

import spray.json.{pimpAny, JsObject, JsString}

class SegmentJsonSpec extends UnitSpec {
  "json write" in {
    import SegmentJson._

    val v: Segment = Map("segment_name" -> "hoge", "segment_value" -> "fuga")

    val actual = v.toJson

    val expected = JsObject(
      "segment_name"  -> JsString("hoge"),
      "segment_value" -> JsString("fuga")
    )
    assert(actual == expected)
  }
}
