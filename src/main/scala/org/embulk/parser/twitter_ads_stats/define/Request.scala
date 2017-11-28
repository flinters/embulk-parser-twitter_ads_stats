package org.embulk.parser.twitter_ads_stats.define

import java.time.LocalDate

case class Request(
    params: Params
)

object Request {
  val fieldNames: Array[String] = FieldNameUtil.fieldList[Request]
}

case class Params(
    start_time: StatsDateTime,
    end_time: StatsDateTime,
    placement: String
) {
  require(!start_time.utcDateTime.isAfter(end_time.utcDateTime))
  require(start_time.utcDateTime.toLocalTime == end_time.utcDateTime.toLocalTime)

  val startDate = start_time.adAccountLocalDate
  val endDate   = end_time.adAccountLocalDate

  /**
    * MetricTimeSeries の期間
    * 開始日~(終了日 - 1)
    */
  def targetDates: List[LocalDate] = {
    @scala.annotation.tailrec
    def loop(curDate: LocalDate, acc: List[LocalDate]): List[LocalDate] = {
      acc match {
        case x :: _ if !x.isBefore(endDate.minusDays(1)) => acc.reverse
        case _                                           => loop(curDate.plusDays(1), curDate :: acc)
      }
    }

    loop(startDate, Nil)
  }
}

object Params {
  val fieldNames: Array[String] = FieldNameUtil.fieldList[Params]
}
