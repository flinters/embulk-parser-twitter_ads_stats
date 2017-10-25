package org.embulk.parser.twitter_ads_stats.define

import org.embulk.parser.twitter_ads_stats._

import scala.util.control.NonFatal

case class Metrics(map: Map[String, MetricTimeSeries]) {

  private[define] def findMetricsGroup(index: Int, metricNames: Seq[String]): Either[ParseException, MetricsGroup] = {
    try {
      val metricsGroup = metricNames.map { name =>
        val n = MetricElementNames.replaceSeparator(name)
        (
          n,
          map.get(n).flatten.map(v => v(index))
        )
      }.toMap
      Right(metricsGroup)
    } catch {
      case NonFatal(e) => Left(new InvalidMetricTimeSeriesException(e, index))
    }
  }
}
