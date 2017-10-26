package org.embulk.parser.twitter_ads_stats.define

import org.embulk.parser.twitter_ads_stats.{Column, MetricElementNames, ParseException}

case class Root(
    data: Seq[Data],
    request: Request
) {
  def resolveColumns(metricElementNames: MetricElementNames): Either[ParseException, Seq[Column]] = {
    data
      .map { d =>
        d.resolveColumns(metricElementNames, request)
      }
      .foldRight[Either[ParseException, Seq[Column]]](Right(Nil)) {
        case (Left(e), _)               => Left(e)
        case (Right(_), Left(e))        => Left(e)
        case (Right(seq1), Right(seq2)) => Right(seq1 ++: seq2)
      }
  }
}
object Root {
  val fieldNames: Array[String] = FieldNameUtil.fieldList[Root]
}
