package org.embulk.parser.twitter_ads_stats.define

import java.time.LocalDate

import org.embulk.parser.twitter_ads_stats._

case class IDData(metrics: Metrics, segment: Option[String]) {
  private def resolveColumn(
      id: String,
      metricElementNames: MetricElementNames,
      date: (LocalDate, Int),
      placement: String
  ): Either[ParseException, Column] = {
    metricElementNames.names
      .map { name =>
        (name._1, metrics.findMetricsGroup(date._2, name._2))
      }
      .foldRight[Either[ParseException, Map[String, MetricsGroup]]](Right(Map.empty)) {
        case ((_, Left(e)), _)           => Left(e)
        case ((_, Right(_)), Left(e))    => Left(e)
        case ((s, Right(r)), Right(acc)) => Right(acc + (s -> r))
      }
      .map(Column(id, date._1, segment, placement, _))
  }

  private[define] def resolveColumns(id: String,
                                     metricElementNames: MetricElementNames,
                                     request: Request): Either[ParseException, Seq[Column]] = {
    request.params.targetDates.zipWithIndex
      .map { date =>
        resolveColumn(id, metricElementNames, date, request.params.placement)
      }
      .foldRight[Either[ParseException, Seq[Column]]](Right(Nil)) {
        case (Left(e), _)           => Left(e)
        case (Right(_), Left(e))    => Left(e)
        case (Right(a), Right(seq)) => Right(a +: seq)
      }
  }
}

object IDData {
  val fieldNames: Array[String] = FieldNameUtil.fieldList[IDData]
}
