package org.embulk.parser.twitter_ads_stats.define

import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

/**
  * @param iso8601DateTime This datetime represents midnight in the timezone of the advertiser's account.
  */
case class StatsDateTime(iso8601DateTime: String) {

  private val utcDateTime: LocalDateTime = LocalDateTime.parse(iso8601DateTime, DateTimeFormatter.ISO_DATE_TIME)

  def adAccountLocalDate: LocalDate = utcDateTime.plusHours(StatsDateTime.DateLineOffsetHours).toLocalDate

  def isAfter(that: StatsDateTime): Boolean = this.utcDateTime.isAfter(that.utcDateTime)

  def isSameOffsetTime(that: StatsDateTime): Boolean = this.utcDateTime.toLocalTime == that.utcDateTime.toLocalTime

}

object StatsDateTime {

  val DateLineOffsetHours = 12

}
