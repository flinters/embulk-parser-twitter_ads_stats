package org.embulk.parser.twitter_ads_stats

sealed abstract class ParseException(
    message: String,
    cause: Throwable
) extends Throwable

case class InvalidMetricTimeSeriesException(
    message: String,
    cause: Throwable
) extends ParseException(message, cause) {
  def this(cause: Throwable, index: Int) = {
    this(s"Not Found index: $index", cause)
  }
}

case class InvalidInputFileException(
    message: String,
    cause: Throwable
) extends ParseException(message, cause) {

  def this(cause: Throwable) = {
    this(s"Input file can't parse", cause)
  }
}
