package org.embulk.parser.twitter_ads_stats.define

import java.time.LocalDate

import org.embulk.parser.twitter_ads_stats.UnitSpec

class StatsDateTimeSpec extends UnitSpec {

  "Twitter APIの日時パラメータをアドアカウントでのローカル日付に変換できる" should {
    "2017-08-20T15:00:00Z は 2017/08/21になる(JST)" in {
      assert(StatsDateTime("2017-08-20T15:00:00Z").adAccountLocalDate == LocalDate.of(2017, 8, 21))
    }
    "2017-08-20T08:00:00Z は 2017/08/20になる(PST)" in {
      assert(StatsDateTime("2017-08-20T08:00:00Z").adAccountLocalDate == LocalDate.of(2017, 8, 20))
    }
    "2017-08-20T10:00:00Z は 2017/08/20になる(Pacific/Honolulu)" in {
      assert(StatsDateTime("2017-08-20T10:00:00Z").adAccountLocalDate == LocalDate.of(2017, 8, 20))
    }
  }

}
