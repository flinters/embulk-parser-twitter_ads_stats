package org.embulk.parser.twitter_ads_stats.define

import java.time.LocalDate

import org.embulk.parser.twitter_ads_stats.UnitSpec

class ParamsSpec extends UnitSpec {
  "開始日~(終了日-1)の日程を取得する" in {
    val period = Params(StatsDateTime("2016-12-31T15:00:00Z"), StatsDateTime("2017-01-03T15:00:00Z"), "", None)
    val actual = period.targetDates
    val expected = List(
      LocalDate.of(2017, 1, 1),
      LocalDate.of(2017, 1, 2),
      LocalDate.of(2017, 1, 3)
    )
    assert(actual == expected)
  }
  "開始日から終了日までの全日程は、開始日から終了日が同一な場合は同一な日程となる" in {
    val period = Params(StatsDateTime("2016-12-31T15:00:00Z"), StatsDateTime("2017-01-01T15:00:00Z"), "", None)
    val actual = period.targetDates
    val expected = List(
      LocalDate.of(2017, 1, 1)
    )
    assert(actual == expected)
  }
}
