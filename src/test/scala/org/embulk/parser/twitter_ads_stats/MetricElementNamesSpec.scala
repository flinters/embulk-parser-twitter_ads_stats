package org.embulk.parser.twitter_ads_stats

class MetricElementNamesSpec extends UnitSpec {
  "ソートされたメトリクスグループの名前を取得する" in {
    val emptySeq = Seq.empty
    val names = MetricElementNames(
      Map("b" -> emptySeq, "a" -> emptySeq, "c" -> emptySeq)
    )
    val actual = names.getSortedMetricsGroupNames
    val expected = List("a", "b", "c")

    assert(actual == expected)
  }
}
